package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.daily

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment

class Daily3Pretreatment: BaseTimePreTreatment() {
//    override fun createMimapBitmaps() = listOf(
//        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyaer0_title"),
//
//        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyaer0_title")
//    )

    override fun dealDuration(index: Int): Long {
        return if (index % 3 == 0) {
            666L
        } else {
            667L
        }
    }

    override fun createMimapBitmaps(): MutableList<Bitmap> = mutableListOf(
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/5"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/4"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/3"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/2"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/1"),
    )



    override fun getMipmapsCount() = Integer.MAX_VALUE
}