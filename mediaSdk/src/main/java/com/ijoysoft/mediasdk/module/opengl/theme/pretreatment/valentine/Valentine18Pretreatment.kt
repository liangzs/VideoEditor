package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.valentine

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment
import org.libpag.PAGFile
import java.util.*
import kotlin.collections.ArrayList

class Valentine18Pretreatment : BasePreTreatment() {

    override fun getRatio916(index: Int): List<Bitmap?> {
        return listOf<Bitmap>(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/valentine9_16" + SUFFIX))
    }

    override fun getRatio11(index: Int): List<Bitmap> {
        return listOf<Bitmap>(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/valentine1_1" + SUFFIX))
    }

    override fun getRatio169(index: Int): List<Bitmap> {
        return listOf<Bitmap>(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/valentine16_9" + SUFFIX))
    }

    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType): Array<Array<String>> {
        return arrayOf(arrayOf("/heart"))
    }


    override fun createDuration(): Int {
        return super.createDuration()
    }
}