package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.sport

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment
import org.libpag.PAGFile
import java.util.*

class Sport0Pretreatment : BasePreTreatment() {


    override fun getThemePags(): MutableList<PAGFile> {
        return mutableListOf( PAGFile.Load(ConstantMediaSize.themePath + "/text.pag"))
    }


    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType?)
        = arrayOf(arrayOf("/rec"))
}