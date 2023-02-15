package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.newyear

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment

class NewYear0Pretreatment: BaseTimePreTreatment() {
    override fun createMimapBitmaps() = listOf(
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyaer0_title"),

        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyaer0_title")
    )

    override fun dealDuration(index: Int): Long {
        return if (index % 9 == 0) 2500 else 1000
    }
}