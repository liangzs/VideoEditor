package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.newyear

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common.Common15PreTreatment

class NewYear2Pretreatment: BasePreTreatment() {

    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType?) =
        arrayOf(arrayOf("/newyear2_lb_dynamic",
            "/newyear2_lt_dynamic",
            "/newyear2_rb_dynamic",
            "/newyear2_rt_dynamic"))

    override fun getMimapBitmaps(ratioType: RatioType?, index: Int): MutableList<Bitmap> {
        return if (ratioType != null) {
            if (ratioType.ratioValue < 1) {
                mutableListOf(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear2_border_9_16"))
            } else if (ratioType.ratioValue > 1) {
                mutableListOf(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear2_border_16_9"))
            } else {
                mutableListOf(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear2_border_1_1"))
            }
        } else {
            mutableListOf(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear2_border_1_1"))
        }
    }
}