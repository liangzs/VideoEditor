package com.ijoysoft.mediasdk.module.mediacodec.decode

import android.media.MediaFormat
import java.nio.ByteBuffer

class AudioExtractor(path: String) : IExtractor {
    val mmExtractor: MMExtractor = MMExtractor(path)
    override fun getFormat(): MediaFormat? {
        return mmExtractor.getAudioMediaFormat()
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
        mmExtractor.stop()
    }

    override fun getTrack(): Int {
        return mmExtractor.getAudioTrack()
    }

    override fun advance() {
        mmExtractor.advance()
    }
}