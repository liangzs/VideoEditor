package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.ijoysoft.mediasdk.module.entity.PretreatConfig
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment.FRAME_BORDER
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common.Common8PreTreatment

class HD32PreTreatment: Common8PreTreatment() {
    override fun addFrame(pretreatConfig: PretreatConfig?): Bitmap {
        val src = super.addFrame(pretreatConfig)
        val borderWidth: Int = BasePreTreatment.FRAME_BORDER * 2 / 3
        val borderHeight: Int = BasePreTreatment.FRAME_BORDER * 2 / 3
        val result = Bitmap.createBitmap(src.width + 2 * borderWidth, src.height + 2 * borderHeight, src.config)
        val canvas = Canvas(result)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(src, borderWidth.toFloat(), borderHeight.toFloat(), Paint())
        src.recycle()
        return result
    }
}
