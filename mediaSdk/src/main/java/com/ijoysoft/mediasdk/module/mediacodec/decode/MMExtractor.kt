package com.ijoysoft.mediasdk.module.mediacodec.decode

import android.media.MediaExtractor
import android.media.MediaFormat
import com.ijoysoft.mediasdk.common.utils.LogUtils
import java.nio.ByteBuffer

/**
 * 音视频抽取分离器
 */
class MMExtractor(path: String) : IExtractor {
    //视频轨道
    private var videoTrack = -1;

    //抽取器
    private var extractor: MediaExtractor? = null;

    //音频轨道
    private var audioTrack = -1;

    //当前帧的时间戳
    private var curSampleTime: Long = 0;

    //解码时间点
    private var startPos: Long = 0;

    private var mediaFormat: MediaFormat? = null;

    init {
        extractor = MediaExtractor();
        extractor?.setDataSource(path);
        //getVideoMediaFormat()
        //getAudioTrack()
    }

    /**
     * 获取视频的格式参数
     */
    fun getVideoMediaFormat(): MediaFormat? {
        if (mediaFormat != null) {
            return mediaFormat;
        }
        extractor?.apply {
            try {
                for (i in 0 until trackCount) {
                    val mediaFormat = getTrackFormat(i);
                    val mime = mediaFormat.getString(MediaFormat.KEY_MIME);
                    if (mime!!.startsWith("video/")) {
                        videoTrack = i;
                        break
                    }
                }
                if (videoTrack >= 0) {
                    mediaFormat = extractor?.getTrackFormat(videoTrack);
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        selectTrack();
        return mediaFormat;
    }

    /**
     * 获取视频的格式参数
     */
    fun getAudioMediaFormat(): MediaFormat? {
        extractor?.apply {
            try {
                for (i in 0 until trackCount) {
                    val mediaFormat = getTrackFormat(i);
                    val mime = mediaFormat.getString(MediaFormat.KEY_MIME);
                    if (mime!!.startsWith("audio/")) {
                        audioTrack = i;
                        break
                        //LogUtils.v(javaClass.simpleName, "audioTrack:" + audioTrack);
                    }
                }
                if (audioTrack > 0) {
                    return extractor?.getTrackFormat(audioTrack);
                }
                selectTrack();
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null;
    }

    override fun getFormat(): MediaFormat? {
        if (videoTrack >= 0) {
            return getVideoMediaFormat()
        }
        return getAudioMediaFormat()
    }

    //override fun getFormat(isVideo: Boolean): MediaFormat? {
    //    if (isVideo) {
    //        return getVideoMediaFormat()
    //    }
    //    return getAudioMediaFormat()
    //}

    /**
     * 读取视频数据,返回数据的长度，视频做advance操作
     */
    override fun readVideoBuffer(byteBuffer: ByteBuffer): Int {
        byteBuffer.clear()
        extractor?.selectTrack(videoTrack)
        extractor?.let {
            val readSampleCount = it.readSampleData(byteBuffer, 0)
            if (readSampleCount < 0) {
                return -1;
            }
            curSampleTime = extractor?.sampleTime ?: 0;
            extractor?.advance()
            return readSampleCount;
        }
        return 0;
    }

    /**
     * 读取视频数据,返回数据的长度,不做advance的seek操作
     */
    override fun readAudioBuffer(byteBuffer: ByteBuffer): Int {
        byteBuffer.clear()
        extractor?.selectTrack(audioTrack)
        extractor?.let {
            val readSampleCount = it.readSampleData(byteBuffer, 0)
            if (readSampleCount < 0) {
                return -1;
            }
            curSampleTime = extractor?.sampleTime ?: 0;
            //extractor?.advance()
            return readSampleCount;
        }
        return 0;
    }

    /**
     * 读取视频数据,返回数据的长度
     */
    override fun readBuffer(byteBuffer: ByteBuffer): Int {
        byteBuffer.clear()
        selectTrack()
        extractor?.let {
            val readSampleCount = it.readSampleData(byteBuffer, 0)
            if (readSampleCount < 0) {
                return -1;
            }
            curSampleTime = extractor?.sampleTime ?: 0;
            extractor?.advance()
            return readSampleCount;
        }
        return 0;
    }


    /**
     * 选择轨道
     */
    fun selectTrack() {
        if (videoTrack >= 0) {
            extractor?.selectTrack(videoTrack)
        } else if (audioTrack >= 0) {
            extractor?.selectTrack(audioTrack)
        }
    }

    /**
     * seek 并返回具体时间戳
     * 因为seek到前一关键帧位置与curpos有时候出入很大，所以需要重新判断是否seek成功，然后再做一个advance移动
     *
     */
    override fun seek(curPos: Long): Long {
        selectTrack()
        var start = System.currentTimeMillis();
        extractor?.seekTo(curPos, MediaExtractor.SEEK_TO_PREVIOUS_SYNC)
        curSampleTime = extractor?.sampleTime ?: 0
        //LogUtils.v("", "seek_time:" + (System.currentTimeMillis() - start));
        return curSampleTime
    }

    override fun stop() {
        //【4，释放提取器】
        extractor?.release()
        extractor = null
    }

    override fun getTrack(): Int {
        TODO("Not yet implemented")
    }

    fun getVideoTrack(): Int {
        return videoTrack
    }

    fun getAudioTrack(): Int {
        return audioTrack
    }

    override fun setStartPos(pos: Long) {
        startPos = pos
    }

    /**
     *  获取当前帧时间
     */
    override fun getCurrentTimestamp(): Long {
        return curSampleTime;
    }

    fun release() {
        extractor?.release();
        extractor = null;
    }

    /**
     * 往前读取一帧
     */
    override fun advance() {
        extractor?.advance()
    }
}