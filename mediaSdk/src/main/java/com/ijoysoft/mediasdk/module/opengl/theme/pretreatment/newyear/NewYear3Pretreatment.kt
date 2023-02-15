package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.newyear

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common.Common6PreTreatment

class NewYear3Pretreatment: Common6PreTreatment() {
    override fun createMimapBitmaps() = listOf(
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear3_title"),

        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear3_1_1"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear3_9_16"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear3_16_9")
    )

}