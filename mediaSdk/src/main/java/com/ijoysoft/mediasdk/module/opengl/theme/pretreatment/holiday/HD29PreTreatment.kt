package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common.Common6PreTreatment
import java.util.*

class HD29PreTreatment: Common6PreTreatment() {

    val dynamicMipmap = arrayOf(arrayOf("/holiday29_1dynamic"))

    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType?) = dynamicMipmap


    override fun createMimapBitmaps(): MutableList<Bitmap> {
        return Arrays.asList(
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday29_lt"),
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday29_t"),
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday29_rt"),
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday29_title")
        )
    }


    override fun getMipmapsCount(): Int {
        return 9
    }

    override fun dealDuration(index: Int): Long {
        return if (index % 9 == 0) {
            3400
        } else 800
        //        if (index == 5) {
//            return 5000;
//        }
    }
}