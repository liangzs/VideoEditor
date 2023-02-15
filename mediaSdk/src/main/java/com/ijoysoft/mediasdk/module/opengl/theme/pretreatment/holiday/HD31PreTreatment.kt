package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager.ReflectOpenglThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionSeries
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType

class HD31PreTreatment: BasePreTreatment() {





    override fun dealDuration(index: Int): Long {
        return 3000
    }


    override fun getMimapBitmaps(ratioType: RatioType, index: Int): MutableList<Bitmap> {
        return mutableListOf(
            if (ratioType.ratioValue < 1f) {
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday31_9_16")
            } else if (ratioType.ratioValue > 1f) {
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday31_16_9")
            } else {
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday31_1_1")
            },
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday31_title")
        )
    }

    override fun getMipmapsCount() = 1



}