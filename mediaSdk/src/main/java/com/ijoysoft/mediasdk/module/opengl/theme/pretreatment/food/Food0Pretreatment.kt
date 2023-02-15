package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.food

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment
import org.libpag.PAGFile
import java.util.*

class Food0Pretreatment : BaseTimePreTreatment() {

    override fun createMimapBitmaps(): MutableList<Bitmap> {

        return Arrays.asList(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/food0"))
    }


    override fun getThemePags(): MutableList<PAGFile> {
        return Arrays.asList( PAGFile.Load(ConstantMediaSize.themePath + "/text_pag"));
        //return Arrays.asList(PAGFile.Load(ConstantMediaSize.themePath + "/text_pag"), PAGFile.Load(ConstantMediaSize.themePath + "/food0_particle"));
    }


    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType?): Array<Array<String>> {
        return arrayOf(arrayOf("/food0_gif1"))
    }

    override fun getMipmapsCount() = Integer.MAX_VALUE

    /**
     * 图片时长
     */
    override fun dealDuration(index: Int): Long {
        return if (index < 6) {
            1000
        } else {
            600
        }
    }



}