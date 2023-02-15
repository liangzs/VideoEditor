package com.ijoysoft.mediasdk.module.mediacodec.decode

import android.media.MediaFormat
import java.nio.ByteBuffer

/**
 * 文件读取器 eg MediaExtractor
 */
interface IExtractor {

    /**
     * 获取音视频格式参数
     */
    //fun getFormat(isVideo: Boolean): MediaFormat?
    fun getFormat(): MediaFormat?

    /**
     * 读取视频数据
     */
    fun readVideoBuffer(byteBuffer: ByteBuffer): Int{
        return 0;
    }

    /**
     * 读取音频数据
     */
    fun readAudioBuffer(byteBuffer: ByteBuffer): Int{
        return 0;
    }

    fun readBuffer(byteBuffer: ByteBuffer):Int

    /**
     * 获取当前帧时间
     */
    fun getCurrentTimestamp(): Long

    /**
     * Seek到指定位置，并返回实际帧的时间戳
     */
    fun seek(pos: Long): Long
    fun setStartPos(pos: Long)

    /**
     * 停止读取数据
     */
    fun stop()

    fun getTrack(): Int

    /**
     * 往前读取一帧
     */
    fun advance()

}