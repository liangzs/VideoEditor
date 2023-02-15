package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.sport

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionSeries
import org.libpag.PAGFile
import java.util.*

class Sport2Pretreatment : BasePreTreatment() {


    override fun getThemePags(): MutableList<PAGFile> {
        return mutableListOf( PAGFile.Load(ConstantMediaSize.themePath + "/t.pag"), PAGFile.Load(ConstantMediaSize.themePath + "/b.pag"))
    }


    override fun createTransition(index: Int): TransitionFilter {
        return TransitionSeries.SERIES7.array.let {
            TransitionFactory.initFilters(it[index % it.size])
        }
    }
}