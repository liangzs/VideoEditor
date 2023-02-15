package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.food

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment
import org.libpag.PAGFile
import java.util.*
import kotlin.collections.ArrayList

class Food5Pretreatment : BasePreTreatment() {
    /**
     * 转成全局时间轴控件
     */
    private val MIPCOUNT = 3
    override fun getMipmapsCount(): Int {
        return MIPCOUNT
    }

    override fun getRatio916(index: Int): List<Bitmap?> {
        when (index) {
            0 -> return Arrays.asList(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/food1"))
            1 -> return Arrays.asList(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/food2"))
            2 -> return Arrays.asList(
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/food3"),
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/food4"))
        }
        return emptyList();
    }

}