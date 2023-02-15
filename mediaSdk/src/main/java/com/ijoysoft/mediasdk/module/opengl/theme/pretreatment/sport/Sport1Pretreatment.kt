package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.sport

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment
import org.libpag.PAGFile
import java.util.*

class Sport1Pretreatment : BasePreTreatment() {


    override fun getMimapBitmaps(ratioType: RatioType?, index: Int): MutableList<Bitmap> {
        return mutableListOf<Bitmap>(
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/t" + SUFFIX)
        )
    }
}