package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.daily

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment

class Daily9Pretreatment : BaseTimePreTreatment() {
    override fun createMimapBitmaps(): List<Bitmap>? {
        return null
    }

    override fun getMipmapsCount(): Int {
        return 20
    }

    override fun dealDuration(index: Int): Long {
        return 600
    }

    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType): Array<Array<String>> {
        return arrayOf(arrayOf("/start", "/text"))
    }
}