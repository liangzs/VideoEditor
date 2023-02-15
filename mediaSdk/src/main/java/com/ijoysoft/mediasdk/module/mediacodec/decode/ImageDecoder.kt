package com.ijoysoft.mediasdk.module.mediacodec.decode

import android.media.MediaCodec
import android.media.MediaFormat
import android.view.Surface
import java.nio.ByteBuffer

/**
 * 视频合成时，图片的渲染部分,
 * 重新抽取Mediaclipper对象，把imagep的渲染移到此类
 */
class ImageDecoder(path: String,control: Triple<String,Float, Boolean>) : BaseDecoder(control,null,null) {
    override fun initRender(): Boolean {
        TODO("Not yet implemented")
    }

    override fun check(): Boolean {
        TODO("Not yet implemented")
    }

    override fun initExtractor(path: String): IExtractor {
        TODO("Not yet implemented")
    }

    override fun render(outputBuffers: ByteBuffer, bufferInfo: MediaCodec.BufferInfo) {
        TODO("Not yet implemented")
    }

    override fun configCodec(codec: MediaCodec, format: MediaFormat): Boolean {
        TODO("Not yet implemented")
    }

    override fun doneDecode() {
       super.doneDecode()
    }

    override fun initSpecParams(format: MediaFormat) {
        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun isDecoding(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isSeeking(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isStop(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getMediaFormat(): MediaFormat {
        TODO("Not yet implemented")
    }

    override fun getTrack(): Int {
        TODO("Not yet implemented")
    }


    override fun initSurface(surface: Surface) {
        TODO("Not yet implemented")
    }

     fun seek(curPos: Long) {
        TODO("Not yet implemented")
    }

    override fun getCurrentPosition(): Long {
        TODO("Not yet implemented")
    }
}