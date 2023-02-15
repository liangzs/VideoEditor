package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.newyear

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common.Common15PreTreatment

class NewYear1Pretreatment: Common15PreTreatment() {
    override fun createMimapBitmaps() = listOf(
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear1_balloon_l"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear1_balloon_r"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear1_title")
    )

}