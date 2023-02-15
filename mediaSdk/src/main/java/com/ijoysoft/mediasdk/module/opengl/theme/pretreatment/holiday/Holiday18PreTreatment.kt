package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment
import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import java.util.ArrayList
import java.util.Arrays

/**
 * @author hayring
 * @date 2021/12/30  19:22
 */
class Holiday18PreTreatment : BaseTimePreTreatment() {
    override fun createMimapBitmaps(): List<Bitmap> {
        return Arrays.asList(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday18_1"),
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday18_2"),
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday18_3"),
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday18_4"))
    }

    override fun getMipmapsCount(): Int {
        return 5
    }

    override fun dealDuration(index: Int): Long {
        return 2240
    }
}