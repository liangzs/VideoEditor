package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.christmas

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment
import org.libpag.PAGFile
import java.util.*

class Christmas5Pretreatment : BaseTimePreTreatment() {

    override fun createMimapBitmaps(): MutableList<Bitmap> {

        return Arrays.asList(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas1"), BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas2"), BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas3"))
    }


    override fun getThemePags(): MutableList<PAGFile> {
        return Arrays.asList(PAGFile.Load(ConstantMediaSize.themePath + "/christmas_pag1"),
            PAGFile.Load(ConstantMediaSize.themePath + "/christmas_pag2"));
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