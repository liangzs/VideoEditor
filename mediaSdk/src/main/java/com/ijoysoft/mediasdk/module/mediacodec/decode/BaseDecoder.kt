package com.ijoysoft.mediasdk.module.mediacodec.decode

import android.media.AudioTrack
import android.media.MediaCodec
import android.media.MediaCodec.BufferInfo
import android.media.MediaFormat
import android.view.Surface
import androidx.annotation.CallSuper
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.common.utils.ObjectUtils
import com.ijoysoft.mediasdk.module.mediacodec.decode.IDecodeStateListenr.AudioDecodeListener
import kotlinx.coroutines.*
import java.nio.ByteBuffer

/**
 * 跟自身差距最小的数值
 */
fun Long.getNearValue(one: Long, two: Long): Long {
    return if (Math.abs(this - one) < Math.abs(this - two)) one else two
}

/**
 * 补充解码通用的状态信息
 * 当进行合成时{@link com.ijoysoft.mediasdk.module.mediacodec.MediaClipper}
 * 需要手动传入时间进行同步
 */
abstract class BaseDecoder(val control: Triple<String, Float, Boolean>, val listenr: IDecodeStateListenr?, val audioListenr: AudioDecodeListener?) : IDecode {
    //, var extractor:IExtractor?
    //线程相关参数
    protected var isRunning = true;
    private val lock = Object()
    private val renderLock = Object()
    private var isReadyDecode = false;

    /**
     * 读取器
     */
    protected var extractor: IExtractor? = null;
    //解码相关参数
    /**
     * 解码器
     */
    protected var codec: MediaCodec? = null;


    /**
     * 解码输入缓存区
     */
    protected var inputBuffers: Array<ByteBuffer>? = null;

    /**
     * 解码输出缓存区
     */
    protected var outputBuffers: Array<ByteBuffer>? = null;

    /**
     * 解码数据信息
     */
    protected var bufferInfo: BufferInfo = BufferInfo();

    /**
     * 当前的解码状态
     */
    protected var state: DecodeState = DecodeState.START;


    /**
     * 流数据是否结束
     */
    private var mIsEOS = false

    //常用的参数
    protected var filepath = control.first;
    protected var duration: Long = 0;
    protected var endPos: Long = 0;

    //音频和视频流同步PTS渲染时间,即用系统正常的数据进行对比来进行修整
    protected var synPtstime: Long = 0L;

    private var passTime = 0L;
    private var decodeTime = 0L;
    private var offsetTime = 0L;

    //文件合成的当前帧时间，合成时赋值进来
    var currentMurgePts: Long? = 0;

    //音频播放
    var audioTrack: AudioTrack? = null;

    var hadInitCodec: Boolean = false;

    var seekRunAdvance = false;

    companion object {
        val EQUEUE_SIZE: Long = 1000;

        //最多做5次seek操作
        val SEEK_MAX = 30;

        val SEEK_MAX_AGAIN = 50;
    }

    //播放速度
    var speed: Float = if (control.second == 0f) {
        1f
    } else {
        control.second
    };
    var index = 0;

    /**
     * 解码动作
     */
    suspend fun run() {
        state = DecodeState.START
        //【解码步骤：1. 初始化，并启动解码器】
        if (!init()) return
        listenr?.decoderPrepare(this@BaseDecoder)
        while (isRunning && CoroutineScope(currentCoroutineContext()).isActive) {
            if (state != DecodeState.START && state != DecodeState.DECODING) {
                delay(50);
                //waitDecode()
                //恢复播放时候，进行时间同步，因为暂停时，系统时间会一直走，
                if (state != DecodeState.SEEKING) {
                    //LogUtils.v(javaClass.simpleName, "run--waitDecode..:" + state.name + ",isRunning:" + isRunning);
                    synPtstime = System.currentTimeMillis() - getCurrentDecodeTime()
                }
                continue
            }
            if (!isRunning || state == DecodeState.STOP) {
                isRunning = false
                break
            }
            if (synPtstime == 0L) {
                synPtstime = System.currentTimeMillis();
            }
            //LogUtils.v(javaClass.simpleName, "run:renderLock...");
            synchronized(renderLock) {
                //如果数据没有解码完毕，将数据推入解码器解码
                if (!mIsEOS) {
                    //【解码步骤：2. 将数据压入解码器输入缓冲】
                    try {
                        mIsEOS = pushBufferToDecoder(true)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                //【解码步骤：3. 将解码好的数据从缓冲区拉取出来】
                try {
                    index = pullBufferFromDecoder()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                //LogUtils.v(javaClass.simpleName, "pullBufferFromDecoder::" + index);
                //LogUtils.v(javaClass.simpleName, "run-getCurrentDecodeTime:" + getCurrentDecodeTime() + ",pullBufferFromDecoder:$index");
                if (index >= 0) {
                    audioListenr?.let {
                        LogUtils.v(javaClass.simpleName, "run:synDecodeTime..................");
                    }
                    //与系统时间进行校正，正常，或者加速播放
                    seekRunAdvance = false;
                    if (state == DecodeState.DECODING) {
                        runBlocking {
                            seekRunAdvance = synDecodeTime();
                            //continue
                        }
                        //if (seekRunAdvance) {
                        //    return@synchronized
                        //}
                    }
                    //audioListenr?.let {
                    //LogUtils.v(javaClass.simpleName, "run:after pre");
                    //}
                    //【解码步骤：4. 渲染】
                    if (state != DecodeState.SEEKING) {
                        render(outputBuffers!![index], bufferInfo!!)
                    }
                    //audioListenr?.let {
                    //    LogUtils.v(javaClass.simpleName, "run:after render");
                    //}
                    //【解码步骤：5. 释放输出缓冲】
                    try {
                        codec?.releaseOutputBuffer(index, true)
                    } catch (e: Exception) {
                        LogUtils.v("releaseOutputBuffer", "codec:" + codec.toString());
                        e.printStackTrace()
                    }
                    //从外面控制是否开始自动播放
                    if (state == DecodeState.START) {
                        state = DecodeState.PAUSE
                        synPtstime = System.currentTimeMillis() - getCurrentDecodeTime()
                        audioListenr?.audioReadyPlay()
                        if (audioListenr == null) {
                            for (i in 0 until 5) {
                                previewCurrentFrame()
                                updateNextFrame()
                            }

                        }
                        //回调会在当前线回到，这就导致了while(runing)中的waitDecode不能执行
                        //LogUtils.v(javaClass.simpleName, "setSeekTo...2-readyPlay," + this.toString());
                        listenr?.readyPlay()
                    }
                }
                //audioListenr?.let {
                //    LogUtils.v(javaClass.simpleName, "run:period");
                //}
                //【解码步骤：6. 判断解码是否完成】
                if (bufferInfo?.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                    LogUtils.e(javaClass.simpleName, "run---:BUFFER_FLAG_END_OF_STREAM---DecodeState.FINISH");
                    state = DecodeState.FINISH
                    listenr?.decoderFinish(this@BaseDecoder)
                }
            }
        }
        doneDecode()
        reset()
        //LogUtils.e(javaClass.simpleName, "release: runing is end.....");
    }


    /**
     * 同步解码时间
     */
    private suspend fun synDecodeTime(): Boolean {
        if (currentMurgePts != 0L) {
            currentMurgePts = (currentMurgePts!! * speed).toLong()
            if (bufferInfo.presentationTimeUs > currentMurgePts!!) {
                delay((bufferInfo.presentationTimeUs - currentMurgePts!!) / 1000);
            }
            return false
        }

        passTime = ((System.currentTimeMillis() - synPtstime) * speed).toLong();
        decodeTime = getCurrentDecodeTime();
        //offsetTime=getCurrentDecodeTime-(System.currentTimeMillis() - synPtstime)
        //offsetTime=getCurrentDecodeTime-lastCurrentDecodeTime -(System.currentTimeMillis()-System.lastcurrentTim())
        offsetTime = decodeTime - passTime;
        //LogUtils.e(javaClass.simpleName, "decodeTime:$decodeTime,passTime:$passTime,offsetTime:$offsetTime");
        if (offsetTime > 0 && offsetTime <= 1000) {
            delay(offsetTime);
        }
        //突然超过范围，重新赋值
        if (offsetTime > 1000) {
            synPtstime = System.currentTimeMillis() - getCurrentDecodeTime();
        }
        //seek不准备还差一大段时间
        if (offsetTime < -100 && audioListenr == null) {
            val frame = Math.min((Math.abs(offsetTime) / 200).toInt(), SEEK_MAX)
            //for (i in 0 until frame) {
            //    extractor?.advance()
            //}
            extractor?.advance()
            //LogUtils.e(javaClass.simpleName, "synDecodeTime:frame:$frame  offsetTime:$offsetTime");
            return true
        }
        return false
    }

    /**
     * 当前解码到的时间
     */
    protected fun getCurrentDecodeTime(): Long {
        return bufferInfo.presentationTimeUs / 1000;
    }

    /**
     * 等待解码器
     */
    private fun waitDecode() {
        try {
            if (state == DecodeState.PAUSE) {
                listenr?.decoderPause(this)
            }
            synchronized(lock) {
                //LogUtils.v(TAG, "waitDecode:lock.wait()");
                lock.wait()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun notifyDecode() {
        synchronized(lock) {
            LogUtils.v(javaClass.simpleName, "notifyDecode:....");
            lock.notifyAll()
        }
        if (state == DecodeState.DECODING) {
            listenr?.decoderRunning(this)
        }
    }


    fun init(): Boolean {
        if (ObjectUtils.isEmpty(filepath)) {
            listenr?.decoderError(this, "文件路径为空")
            return false;
        }
        //2.初始化数据提取器
        synchronized(renderLock) {
            try {
                extractor = initExtractor(filepath)
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
            if (extractor?.getFormat() == null) return false
            //if (extractor?.getFormat(audioListenr == null) == null) return false
            //3.初始化参数
            if (!initParams()) return false
            //4.初始化渲染器
            if (!initRender()) return false
            //5.初始化解码器
            if (!initCodec()) return false
        }
        return true
    }


    fun initParams(): Boolean {
        try {
            val format = extractor!!.getFormat()!!
            duration = format.getLong(MediaFormat.KEY_DURATION) / 1000
            if (endPos == 0L) endPos = duration
            initSpecParams(extractor!!.getFormat()!!)
        } catch (e: Exception) {
            return false
        }
        return true
    }


    private fun initCodec(): Boolean {
        try {
            //1.根据音视频编码格式初始化解码器
            val type = extractor!!.getFormat()!!.getString(MediaFormat.KEY_MIME)
            LogUtils.v(javaClass.simpleName, "initCodec:type" + type);
            codec = MediaCodec.createDecoderByType(type!!)
            //2.配置解码器
            if (!configCodec(codec!!, extractor!!.getFormat()!!)) {
                //LogUtils.v(TAG, "initCodec--waitDecode..");
                //waitDecode()
            }
            //3.启动解码器
            codec!!.start()
            //4.获取解码器缓冲区
            inputBuffers = codec?.inputBuffers
            outputBuffers = codec?.outputBuffers
            LogUtils.v(javaClass.simpleName, "initCodec....");
            hadInitCodec = true;
        } catch (e: Exception) {
            return false
        }
        return true
    }

    var endFlag = 0;

    /**
     *待解码数据放入解码缓存
     */
    fun pushBufferToDecoder(advance: Boolean): Boolean {
        var isEndOfStream = false
        codec?.let {
            val inputBufferIndex = codec!!.dequeueInputBuffer(EQUEUE_SIZE)
            //LogUtils.v("pushBufferToDecoder", "inputBufferIndex:" + inputBufferIndex + ",codec:" + codec.toString());
            extractor.let {
                inputBuffers?.let {
                    if (inputBufferIndex >= 0) {
                        val inputb = inputBuffers!![inputBufferIndex]
                        //val sampleSize = if(isVideo){
                        //    LogUtils.v(javaClass.simpleName, "readVideoBuffer..");
                        //    extractor!!.readVideoBuffer(inputb)
                        //}else{
                        //    LogUtils.v(javaClass.simpleName, "readAudioBuffer..");
                        //    extractor!!.readAudioBuffer(inputb)
                        //}
                        val sampleSize = extractor!!.readBuffer(inputb)
                        if (sampleSize < 0 && advance) {
                            LogUtils.e(javaClass.simpleName, "sampleSize < 0..BUFFER_FLAG_END_OF_STREAM");
                            endFlag++;
                            //if (endFlag < 3) {
                            //如果数据已经取完，压入数据结束标志：BUFFER_FLAG_END_OF_STREAM
                            codec?.queueInputBuffer(inputBufferIndex, 0, 0,
                                0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                            isEndOfStream = true
                            //}
                        } else {
                            endFlag = 0;
                            codec?.queueInputBuffer(inputBufferIndex, 0,
                                sampleSize, extractor!!.getCurrentTimestamp(), 0)
                        }
                    }
                }
            }
        }
        return isEndOfStream
    }

    /**
     * 获取解码好的数据，即解码好的index位置
     */
    fun pullBufferFromDecoder(): Int {
        // 查询是否有解码完成的数据，index >=0 时，表示数据有效，并且index为缓冲区索引
        var index = -1;
        codec?.let {
            index = codec!!.dequeueOutputBuffer(bufferInfo, EQUEUE_SIZE)
        }
        when (index) {
            MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> {}
            MediaCodec.INFO_TRY_AGAIN_LATER -> {}
            MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED -> {
                outputBuffers = codec!!.outputBuffers
            }
            else -> {
                return index
            }
        }
        return index
    }


    /**
     * 释放资源
     */
    override fun release() {
        stop()
        mIsEOS = false
        //这里需要强制等待里面的cocde走完后进入等待状态，不然会报错
        LogUtils.v(javaClass.simpleName, "release....(lock),codec:" + codec.toString());
        synchronized(renderLock) {
            try {
                val temExtractor = extractor;
                val tempCodec = codec;
                val tempAudioTrack = audioTrack;
                extractor = null;
                codec = null;
                audioTrack = null;
                temExtractor?.stop()
                tempAudioTrack?.stop();
                tempAudioTrack?.release()
                tempCodec?.stop()
                tempCodec?.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            listenr?.decoderDestroy(this@BaseDecoder)
        }
    }

    /**
     * 重置资源
     */
    override fun reset() {
        //try {
        //    codec?.reset()
        //    extractor?.seek(0)
        //    codec?.start()
        //} catch (e: Exception) {
        //    e.printStackTrace()
        //}

    }

    abstract fun initRender(): Boolean

    /**
     * 检测子类是否有具体项
     */
    abstract fun check(): Boolean;

    /**
     * 初始化数据提取器
     */
    abstract fun initExtractor(path: String): IExtractor


    /**
     *渲染
     */
    abstract fun render(outputBuffers: ByteBuffer, bufferInfo: BufferInfo)

    /**
     * 初始化解码器
     */
    abstract fun configCodec(codec: MediaCodec, format: MediaFormat): Boolean

    /**
     * 结束解码
     */
    @CallSuper
    open fun doneDecode() {
        state = DecodeState.PAUSE
        isRunning = false
    }

    /**
     * 子类特有项
     */
    abstract fun initSpecParams(format: MediaFormat)

    override fun pause() {
        state = DecodeState.PAUSE
    }

    override fun resume() {
        state = DecodeState.DECODING
        notifyDecode()
        LogUtils.v(javaClass.simpleName, "resume..");
    }

    override fun stop() {
        state = DecodeState.STOP
        isRunning = false
        notifyDecode()
        LogUtils.v(javaClass.simpleName, "stop..");
    }

    override fun isDecoding(): Boolean {
        return state == DecodeState.DECODING
    }

    override fun isSeeking(): Boolean {
        return state == DecodeState.SEEKING
    }

    override fun isStop(): Boolean {
        return state == DecodeState.STOP
    }

    override fun getMediaFormat(): MediaFormat {
        return extractor!!.getFormat()!!
    }

    override fun getTrack(): Int {
        return extractor!!.getTrack()
    }


    /**
     * seek后预览当前帧
     */
    fun previewCurrentFrame(): Boolean {
        try {
            //【解码步骤：2. 将数据压入解码器输入缓冲】
            mIsEOS = pushBufferToDecoder(false)
            //【解码步骤：3. 将解码好的数据从缓冲区拉取出来】
            val index = pullBufferFromDecoder()
            if (index >= 0) {
                //【解码步骤：4. 渲染】,不播放音频
                //render(outputBuffers!![index], bufferInfo!!)
                //【解码步骤：5. 释放输出缓冲】
                codec?.releaseOutputBuffer(index, true)
            }
        } catch (e: Exception) {
            LogUtils.e(javaClass.simpleName, "previewCurrentFrame..............return true", e);
            return true;
        }
        return false;
    }

    override fun getCurrentPosition(): Long {
        return bufferInfo!!.presentationTimeUs / 1000
    }

    override fun initSurface(surface: Surface) {
        TODO("Not yet implemented")
    }

    override fun updateNextFrame() {
        listenr?.updateNextFrame()
    }

    override fun setStateListener(listenr: IDecodeStateListenr) {
    }

    /**
     * 设置当前的合成时间
     */
    override fun setCurrentMurPts(pts: Long?) {
        currentMurgePts = pts;
    }

    /**
     * video和audio  seek成功之后，在一起同步时间，不然很大概率是video失败，audio是成功的
     */
    fun seekFinish() {

    }
}