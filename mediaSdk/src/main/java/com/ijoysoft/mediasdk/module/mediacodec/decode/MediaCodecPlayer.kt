package com.ijoysoft.mediasdk.module.mediacodec.decode

import android.graphics.LinearGradient
import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.view.Surface
import com.ijoysoft.mediasdk.common.global.ThreadPoolMaxThread
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.module.mediacodec.decode.IDecodeStateListenr.AudioDecodeListener
import com.ijoysoft.mediasdk.module.playControl.IRender
import kotlinx.coroutines.*
import java.util.Stack
import javax.microedition.khronos.opengles.GL10

/**
 * mediacode播放器
 * 持有纹理对象，可以返回原始的渲染纹理，在经过videorender中其他的业务处理(eg videoplayer背景模糊)
 * TODO
 *集成的功能有播放速度
 * 播放音量
 * 是否翻转播放
 */
class MediaCodecPlayer(val control: Triple<String, Float, Boolean>, val isMurging: Boolean, var listener: IDecodeStateListenr?,
                       val audioListener: AudioDecodeListener?) : IRender, IDecode {
    private var audioDecoder: AudioDecoder? = null;

    private var videoDecoder: VideoDecoder? = null;

    // 纹理创建surface纹理
    private var surfaceTexture: SurfaceTexture? = null

    //创建纹理
    private var videoTexure = IntArray(1)

    //创建surface
    private var surface: Surface? = null

    /**
     * 播放速度
     */
    private var speed: Float = 1f;

    private var volume: Int = 100;

    /**
     * 读取器，视频和音频公用一个读取，以视频读取为准。
     * 即视频会进行extractor的seek和advance()操作，音频不做advance操作
     */
    private var extractor: IExtractor? = null;

    //是否播放音频,只有主播放器才播放音频
    val job = Job()
    val launch: CoroutineScope = CoroutineScope(job)

    //seek任务
    var seekJob: Job? = null;
    var seekVideo: Job? = null;
    var seekAudio: Job? = null;
    var videoRender: Job? = null;
    var audioRender: Job? = null;


    /**
     * seek栈，针对seekend 或者seekforce的情况
     */
    var stack: Stack<Long> = Stack();

    /**
     *初始化播放
     */
    fun execute(playAudio: Boolean) {
        videoDecoder = VideoDecoder(control, surface!!, listener, null)
        videoRender = launch.launch {
            withContext(Dispatchers.Default) {
                videoDecoder?.run()
            }
        }
        if (playAudio && !isMurging) {
            audioDecoder = AudioDecoder(control, audioListener, null)
            audioRender = launch.launch {
                withContext(Dispatchers.Default) {
                    audioDecoder?.run()
                }
            }
        }
        LogUtils.v(javaClass.simpleName, "execute....");
        ThreadPoolMaxThread.getInstance().execute {
            LogUtils.v("MediaCodecPlayer", "ThreadPoolMaxThread.size:" + Thread.currentThread().threadGroup.activeCount());
        }

    }

    /**
     * 恢复音频播放
     */
    fun concatAudioPlay() {
        if (audioDecoder == null && !isMurging) {
            audioDecoder = AudioDecoder(control, object : AudioDecodeListener {
                override fun audioReadyPlay() {
                    audioDecoder?.resume()
                }
            }, null)
            audioRender = launch.launch {
                withContext(Dispatchers.Default) {
                    audioDecoder?.run();
                }
            }
        }
    }

    override fun pause() {
        audioDecoder?.pause()
        videoDecoder?.pause()
    }

    override fun resume() {
        //launch.launch {
        //    seekVideo?.let { (seekVideo as Deferred<*>).await() }
        //    seekAudio?.let {
        //        (seekAudio as Deferred<*>).await()
        //    }
        //}
        seekJob?.cancel()
        audioDecoder?.resume()
        videoDecoder?.resume()
    }

    override fun stop() {
    }

    override fun isDecoding(): Boolean {
        return videoDecoder?.isDecoding() ?: false
    }

    override fun isSeeking(): Boolean {
        return false
    }

    override fun isStop(): Boolean {
        return false
    }

    fun resumeAudio() {
        audioDecoder?.resume()
    }

    override fun release() {
        ThreadPoolMaxThread.getInstance().execute {
            seekJob?.cancel()
            seekVideo?.cancel()
            seekAudio?.cancel()
            videoRender?.cancel()
            audioRender?.cancel()
            try {
                audioDecoder?.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                videoDecoder?.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            LogUtils.v(javaClass.simpleName, "release....finish");
            surface = null;
            surfaceTexture = null;
            GLES20.glDeleteTextures(1, videoTexure, 0)
            for (i in 0 until videoTexure.size) {
                videoTexure.set(i, 0)
            }
            listener = null;
            job.cancel()
        }

    }

    /**
     * 匹配渲染流程
     */
    override fun onSurfaceCreated() {
        createAndExcute()
    }

    fun createAndExcute() {
        createVideoTexure()
        surface = Surface(surfaceTexture)
        execute(true)
    }

    /**
     * 创建纹理
     */
    fun buiSurface() {
        createVideoTexure()
        surface = Surface(surfaceTexture)
    }

    /**
     * 匹配渲染流程
     */
    override fun onSurfaceChanged(offsetX: Int, offsetY: Int, width: Int, height: Int, screenWidth: Int, screenHeight: Int) {
    }

    /**
     * 触发数据源的拉取
     */
    override fun onDrawFrame() {
        try {
            surfaceTexture?.updateTexImage()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 检测是否已经在播放
     */
    fun checkPlayer() {
        if (!isPlaying()) {
            resume()
        }
    }

    override fun onDestroy() {
        release()
    }

    override fun setCurrentMurPts(pts: Long?) {
        videoDecoder?.currentMurgePts = pts;
    }

    /**
     * 创建视频纹理
     */
    private fun createVideoTexure() {
        GLES20.glGenTextures(1, videoTexure, 0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, videoTexure.get(0))
        //GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, 256, 2, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, null)
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR.toFloat())
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR.toFloat())
        surfaceTexture = SurfaceTexture(videoTexure.get(0))
    }

    /**
     * 返回原始纹理数据
     */
    fun getSurfaceTexture(): SurfaceTexture? {
        return surfaceTexture
    }

    /**
     * 返回texture
     */
    fun getOutTexture(): Int {
        return videoTexure.get(0);
    }

    /**
     * 这里添加视频和音频的同步
     * 以为seek是异步，要跟resume播放进行配合，要等seek完成后才进行resume的run操作
     */
    fun seek(curPos: Long, list: ArrayDeque<String>?, isPlaying: Boolean): Boolean {
        LogUtils.v(javaClass.simpleName, "seek...videoDecoder:$videoDecoder");
        seekJob = launch.launch {
            withContext(Dispatchers.IO) {
                //seek同时只能运行一次
                //seekVideo?.join()
                pause()
                try {
                    seekVideo = async {
                        val flag = videoDecoder?.seekVideoByLock(curPos, list, seekJob)
                        //if (flag == false) {
                        //    delay(100)
                        //}
                    }
                    //videoDecoder?.seekVideoByLock(curPos, list, seekJob)
                    seekAudio = async { audioDecoder?.seekImpl(curPos, seekJob) }
                    if ((seekVideo as Deferred<*>).await() == true && (seekAudio as Deferred<*>).await() == true) {
                        //音视频同步
                    }

                    //withTimeout(200) {
                    //if ((seekVideo as Deferred<*>).await() == true && (seekAudio as Deferred<*>).await() == true) {
                    //    //音视频同步
                    //}
                    //(seekVideo as Deferred<*>).await()
                    //音视频同步
                    //}
                    if (isPlaying) {
                        resume()
                    } else {
                        updateNextFrame()
                    }
                } catch (e: CancellationException) {
                    e.printStackTrace()
                    if (isPlaying) {
                        resume()
                    } else {
                        updateNextFrame()
                    }
                }
                LogUtils.v(javaClass.simpleName, "seek-seekJob22:" + ",isPlaying:$isPlaying");
            }
        }
        if (videoDecoder == null) {
            list?.clear()
        }
        return false
    }

    override fun interruptSeeking() {
        seekVideo?.cancel()
        seekAudio?.cancel()
        seekJob?.cancel()
        LogUtils.v(javaClass.simpleName, "interruptSeeking:" + seekJob?.isActive);
        videoDecoder?.interruptSeeking()
        audioDecoder?.interruptSeeking()
    }

    override fun getCurrentPosition(): Long {
        return videoDecoder?.getCurrentPosition() ?: 0;
    }

    /**
     * 是否在播放
     */
    fun isPlaying(): Boolean {
        return videoDecoder?.isDecoding() ?: false;
    }

    /**
     * 设置播放速度
     */
    fun setSpeed(speed: Float) {
        videoDecoder?.speed = speed;
        audioDecoder?.speed = speed;
    }

    override fun reset() {
        videoDecoder?.reset()
        audioDecoder?.reset()
    }


    /**
     * 设置音量
     */
    fun setVolume(volume: Float) {
        LogUtils.v(javaClass.simpleName, "setVolume:$volume");
        audioDecoder?.setVolume(volume)
    }


}