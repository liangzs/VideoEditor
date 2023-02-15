package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.valentine

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common.Common15PreTreatment

class Valentine16Pretreatment : Common15PreTreatment() {
    override fun createMimapBitmaps() = listOf(
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/valentine1"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/valentine2"),
    )

}