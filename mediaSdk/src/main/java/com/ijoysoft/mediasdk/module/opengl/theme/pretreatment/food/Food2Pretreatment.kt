package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.food

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment
import org.libpag.PAGFile
import java.util.*

class Food2Pretreatment : BaseTimePreTreatment() {

    override fun createMimapBitmaps(): MutableList<Bitmap> {

        return Arrays.asList(
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/food1"),
            BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/food2"),
        )
    }


    override fun getThemePags(): MutableList<PAGFile> {
        return Arrays.asList(PAGFile.Load(ConstantMediaSize.themePath + "/pag1"));
    }


    /**
     * 图片时长
     */
    override fun dealDuration(index: Int): Long {
        if (index % 12 < 6) {
            return 1200;
        }
        return 800;
    }
}