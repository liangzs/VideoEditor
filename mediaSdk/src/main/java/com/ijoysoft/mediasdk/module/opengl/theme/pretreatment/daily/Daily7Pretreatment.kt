package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.daily

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment

class Daily7Pretreatment: BaseTimePreTreatment() {

    override fun dealDuration(index: Int) = 1000L





    override fun getMipmapsCount() = 1

    override fun createMimapBitmaps() = emptyList<Bitmap>()

    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType?): Array<Array<String>> {
        return arrayOf(arrayOf("/border"))
    }
}