package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.PretreatConfig
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment
import java.util.*

/**
 * @author hayring
 * @date 2021/12/30  19:22
 */
class HD27PreTreatment : BaseTimePreTreatment() {
    override fun createMimapBitmaps(): List<Bitmap> {
        return Arrays.asList(
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday27_rt"),
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday27_lb")
        )
    }

    override fun getMipmapsCount(): Int {
        return 5
    }

    override fun dealDuration(index: Int): Long {
        return 2240
    }

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