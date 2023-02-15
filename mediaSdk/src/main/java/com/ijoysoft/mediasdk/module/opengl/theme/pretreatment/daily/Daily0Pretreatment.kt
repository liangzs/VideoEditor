package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.daily

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment

class Daily0Pretreatment: BaseTimePreTreatment() {
//    override fun createMimapBitmaps() = listOf(
//        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyaer0_title"),
//
//        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyaer0_title")
//    )

    override fun dealDuration(index: Int): Long {
        return 1000
    }

    override fun createMimapBitmaps(): MutableList<Bitmap> = mutableListOf(
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/title"),
    )


    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType?) =
        arrayOf(arrayOf("/cloud", "/animal"))

    override fun getMipmapsCount() = 1
}