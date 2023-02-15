package com.ijoysoft.mediasdk.module.mediacodec.decode

import android.media.*
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.module.mediacodec.decode.IDecodeStateListenr.AudioDecodeListener
import kotlinx.coroutines.Job
import java.nio.ByteBuffer

/**
 * 视频播放时，音频的解码部分
 * 需要用audioTrack播放原始数据
 */
class AudioDecoder(control: Triple<String, Float, Boolean>, audioListener: AudioDecodeListener?,extractor:IExtractor?) : BaseDecoder(control, null, audioListener) {
    //采样率
    private var sampleRate = 0;

    //位数
    private var pcmBit = 1;

    //通道
    private var channel = AudioFormat.ENCODING_PCM_16BIT;


    //播放缓存
    private var audioTempBuffer: ShortArray? = null;

    private var volume: Float = 1f;

    /**
     * 这里初始化音频的播放器
     */
    override fun initRender(): Boolean {
        //声道
        val channelConfig = if (channel == 1) {
            AudioFormat.CHANNEL_OUT_MONO//单声道
        } else {
            AudioFormat.CHANNEL_OUT_STEREO//双声道
        }
        val bufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, pcmBit);
        audioTempBuffer = ShortArray(bufferSize / 2);
        audioTrack = AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelConfig, pcmBit, bufferSize, AudioTrack.MODE_STREAM);
        audioTrack?.play()
        audioTrack?.setVolume(volume)
        return true;
    }

    /**
     * 初始化参数
     */
    override fun initSpecParams(format: MediaFormat) {
        try {
            sampleRate = 44100
            if (format.getString(MediaFormat.KEY_MIME)!!.startsWith(MediaFormat.MIMETYPE_AUDIO_AMR_NB)) {
                sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE)
            }
            channel = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT)
            pcmBit = if (format.containsKey(MediaFormat.KEY_PCM_ENCODING)) {
                format.getInteger(MediaFormat.KEY_PCM_ENCODING)
            } else {
                AudioFormat.ENCODING_PCM_16BIT
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun check(): Boolean {
        return true;
    }

    override fun initExtractor(path: String): IExtractor {
        return AudioExtractor(path)
    }

    /**
     * 音频播放
     * 需要把解码数据由ByteBuffer类型转换为ShortBuffer，这时Short数据类型的长度要减半
     */
    override fun render(outputBuffers: ByteBuffer, bufferInfo: MediaCodec.BufferInfo) {
        //LogUtils.v(javaClass.simpleName, "render-presentationTimeUs:" + bufferInfo.presentationTimeUs + ",bufferInfo.size:" + bufferInfo.size);
        //补充缓存
        if (audioTempBuffer!!.size < bufferInfo.size / 2) {
            audioTempBuffer = ShortArray(bufferInfo.size / 2);
        }
        outputBuffers.position(0);
        outputBuffers.asShortBuffer().get(audioTempBuffer, 0, bufferInfo.size / 2);
        try {
            audioTrack?.write(audioTempBuffer!!, 0, bufferInfo.size / 2);
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun configCodec(codec: MediaCodec, format: MediaFormat): Boolean {
        codec.configure(format, null, null, 0);
        //notifyDecode()
        LogUtils.v(javaClass.simpleName, "configCodec...");
        return true
    }

    override fun doneDecode() {
        super.doneDecode()
    }


    /**
     * 设置音量
     */
    fun setVolume(volume: Float) {
        this.volume = volume;
        audioTrack?.setVolume(volume);
    }


    /**
     *seek具体操作，音视频共用一个extractor抽取器，达到资源共用的音视频同步的目的
     */
    fun seekImpl(curPos: Long, job: Job?): Boolean {
        if (state == DecodeState.STOP || extractor == null) {
            return true
        }
        state = DecodeState.SEEKING;
        val position: Long = curPos * 1000;
        //最多做max次轮询
        extractor?.seek(position)
        var sampleTime = extractor?.getCurrentTimestamp() ?: -1;
        if (sampleTime == -1L) {
            return false
        }
        var count = 0;
        while (count < SEEK_MAX && job?.isActive == true) {
            extractor?.advance()
            count++
            val tempTime = extractor?.getCurrentTimestamp() ?: -1;
            if (tempTime != -1L) {
                sampleTime = position.getNearValue(sampleTime, tempTime)
                //seek定位成功 100ms内
                if (Math.abs(position - sampleTime) < 100000) {
                    break
                }
            } else {
                count = SEEK_MAX;
            }
        }
        extractor?.seek(sampleTime)
        ////取手动的时间
        //synPtstime = System.currentTimeMillis() - getCurrentDecodeTime()
        ////出现播放直接结束的结果
        //bufferInfo.flags=0
        var countMax = 0;
        //if (count == SEEK_MAX) {
        //    extractor?.seek(sampleTime)
        //    while (Math.abs(position - sampleTime) > 200000 && sampleTime != 0L && state != DecodeState.STOP && job?.isActive == true) {//200ms
        //        sampleTime = bufferInfo.presentationTimeUs
        //        //预览当前帧
        //        codec?.let {
        //            LogUtils.v(javaClass.simpleName, "count == SEEK_MAX,countMax:" + countMax);
        //            if (previewCurrentFrame()) {
        //                LogUtils.v(javaClass.simpleName, "count ==  SEEK_MAX seek11111111---222222222222,count:" + count);
        //                return false
        //            }
        //        } ?: break
        //        //extractor时间轴已经对的上号了，但是视频图片数据依然对不上好，所以只能是单独取出来,不走advance过程
        //        if (Math.abs(position - (extractor?.getCurrentTimestamp() ?: 0)) < 50000) {
        //            break
        //        }
        //        countMax++
        //        //防止意外
        //        if (countMax > SEEK_MAX_AGAIN) {
        //            break
        //        }
        //    }
        //} else {//不然就重走一次seek,然后根据sampleTime走正常的advance重走一次过程
        //    count = 0;
        //    extractor?.seek(sampleTime)
        //    while (bufferInfo.presentationTimeUs != sampleTime && state != DecodeState.STOP && job?.isActive == true) {
        //        //预览当前帧
        //        codec?.let {
        //            LogUtils.v(javaClass.simpleName, "count < SEEK_MAX,count:" + count);
        //            if (previewCurrentFrame()) {
        //                LogUtils.v(javaClass.simpleName, "count < SEEK_MAX seek11111111222222222222,count:" + count);
        //                return false
        //            }
        //        } ?: break
        //        //LogUtils.v(javaClass.simpleName, "fix--seek-sampleTime:" + sampleTime + "extractor!!.getCurrentTimestamp():" + extractor?.getCurrentTimestamp() +
        //        //        ",bufferInfo.presentationTimeUs:" + bufferInfo.presentationTimeUs);
        //        count++
        //        //防止意外
        //        if (count > SEEK_MAX) {
        //            break
        //        }
        //    }
        //}
        synPtstime = System.currentTimeMillis() - getCurrentDecodeTime()
        LogUtils.v(javaClass.simpleName, "seek1111111111111111,count:$count");
        return true;
    }


    /**
     * 以vidoedecode的解码速度为主，video解码结束后，同步到audiodecode这边来，而audiodecode不用进行seek操作
     */
    fun synFileSeekinfo() {

    }

}