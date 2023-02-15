package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment
import java.util.*

class Holiday19PreTreatment : BaseTimePreTreatment() {
    override fun createMimapBitmaps(): List<Bitmap> {
        return Arrays.asList(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday19_1"),
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday19_2"),
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday19_3"))
    }

    override fun getMipmapsCount(): Int {
        return 30
    }

    override fun dealDuration(index: Int): Long {
//        index = 29;
        return when (index % mipmapsCount) {
            0 -> 600
            1 -> 400
            3, 14, 15, 16, 2 -> 1200
            7, 8, 9, 10, 11, 12 -> 400
            13 -> 800             //缩放平移
            17 -> 800              //左右移动，旋转
            26 -> 800
            27 -> 1200
            28 -> 1500
            29 -> 2000
            else -> 600
        }
    }
    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType?): Array<Array<String>>? {
        return arrayOf(arrayOf("/holiday19_gif1"))
    }
}