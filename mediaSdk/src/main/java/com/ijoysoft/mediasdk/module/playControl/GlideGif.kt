package com.ijoysoft.mediasdk.module.playControl

import com.ijoysoft.mediasdk.module.playControl.GifDecoder.GifFrame

data class GlideGif(
    val frames: List<GifFrame>,
    val width: Int,
    val height: Int
) {

    fun getBitmaps() = frames.map { it.image }

    fun getDelays() = frames.map { it.delay }
}