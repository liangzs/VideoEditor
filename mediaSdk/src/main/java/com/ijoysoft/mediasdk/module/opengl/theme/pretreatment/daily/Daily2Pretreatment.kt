package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.daily

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment

class Daily2Pretreatment : BasePreTreatment() {

    override fun getRatio916(index: Int): MutableList<Bitmap> {
        return mutableListOf(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/border_9_16"))
    }

    override fun getRatio169(index: Int): MutableList<Bitmap> {
        return mutableListOf(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/border_16_9"))
    }

    override fun getRatio11(index: Int): MutableList<Bitmap> {
        return mutableListOf(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/border_1_1"))
    }

}