package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday

import android.graphics.*
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.PretreatConfig
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment

/**
 * 加边框
 */
class Holiday17PreTreatment : BasePreTreatment() {
    override fun afterPreRotation(): Int {
        return 0
    }

    override fun addFrame(pretreatConfig: PretreatConfig): Bitmap {
        val bitmap = fitRatioBitmap(pretreatConfig)
        val num = FRAME_BORDER / 2
        val width = bitmap.width + num
        val height = bitmap.height + num
        // 背图
        val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(newBitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        // 生成白色的
        paint.color = Color.WHITE
        canvas.drawBitmap(bitmap, (num / 2).toFloat(), (num / 2).toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
        // 设置20圆角
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), 20f, 20f, paint)
        bitmap.recycle()
        return newBitmap
    }

    override fun isNeedFrame(): Boolean {
        return true
    }

    private val MIPCOUNT = 5
    override fun getMipmapsCount(): Int {
        return MIPCOUNT
    }

    override fun getRatio916(index: Int): List<Bitmap> {
//        index = 1;
        val list: MutableList<Bitmap> = ArrayList()
        when (index % MIPCOUNT) {
            0 -> list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday17_2" + SUFFIX))
            1,3 -> list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday17_3" + SUFFIX))
            4 -> list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday17_1" + SUFFIX))
        }
        return list
    }

    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType): Array<Array<String>> {
        return arrayOf(arrayOf("/holiday17_gif1"))
    }
}