package com.ijoysoft.mediasdk.module.mediacodec.decode

import android.media.MediaFormat
import java.nio.ByteBuffer

/**
 * 视频参数解析
 */
class VideoExtractor(path: String) : IExtractor {

    private val mmExtractor = MMExtractor(path)
    override fun getFormat(): MediaFormat? {
        return mmExtractor.getVideoMediaFormat();
    }

    //override fun getFormat(isVideo: Boolean): MediaFormat? {
    //    TODO("Not yet implemented")
    //}

    override fun readBuffer(byteBuffer: ByteBuffer): Int {
        return mmExtractor.readBuffer(byteBuffer)
    }

    override fun getCurrentTimestamp(): Long {
        return mmExtractor.getCurrentTimestamp()
    }

    override fun seek(pos: Long): Long {
        return mmExtractor.seek(pos)
    }

    override fun setStartPos(pos: Long) {
        return mmExtractor.setStartPos(pos)
    }

    override fun stop() {
        return mmExtractor.stop()
    }

    override fun getTrack(): Int {
        return mmExtractor.getVideoTrack()
    }

    override fun advance() {
        mmExtractor.advance()
    }
}