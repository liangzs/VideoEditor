package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.valentine

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment
import org.libpag.PAGFile
import java.util.*
import kotlin.collections.ArrayList

class Valentine19Pretreatment : BaseTimePreTreatment() {

    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType): Array<Array<String>> {
        return arrayOf(arrayOf("/love.gif"))
    }


    override fun getThemePags(): MutableList<PAGFile> {
        return Arrays.asList(PAGFile.Load(ConstantMediaSize.themePath + "/christmas_pag1"),
            PAGFile.Load(ConstantMediaSize.themePath + "/christmas_pag2"));
    }

    override fun createMimapBitmaps(): MutableList<Bitmap> {
        val list = ArrayList<Bitmap>();
        when (ConstantMediaSize.ratioType) {
            RatioType._16_9, RatioType._4_3 -> {
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/ratio169_1"))
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/ratio169_2"))
            }
            RatioType._1_1 -> {
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/ratio11_1"))
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/ratio11_2"))
            }
            RatioType._3_4, RatioType._9_16 -> {
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/ratio916_1"))
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/ratio916_2"))
            }
            else -> {}
        }
        return list
    }


    /**
     * 图片时长
     */
    override fun dealDuration(index: Int): Long {
        return 1200;
    }

    override fun existRatio(): Boolean {
        return true;
    }
}