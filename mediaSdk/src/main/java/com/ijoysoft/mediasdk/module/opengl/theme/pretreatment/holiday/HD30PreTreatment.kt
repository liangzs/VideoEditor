package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager.ReflectOpenglThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionSeries
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType

class HD30PreTreatment: BasePreTreatment() {



    override fun createTransition(index: Int): TransitionFilter {
        return TransitionSeries.SERIES2.array.let {
            TransitionFactory.initFilters(
                it[index % it.size]
            )
        }
    }


    override fun dealDuration(index: Int): Long {
        return 3000
    }


    override fun getMimapBitmaps(ratioType: RatioType, index: Int): MutableList<Bitmap> {
        val border = if (ratioType.ratioValue < 1f) {
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday30_9_16")
        } else if (ratioType.ratioValue > 1f) {
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday30_16_9")
        } else {
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday30_1_1")
        }
//        LogUtils.i("HD30PreTreatment", border.toString())
        return mutableListOf(
            border,
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday30_title")
        )
    }

    override fun getMipmapsCount() = 1



}