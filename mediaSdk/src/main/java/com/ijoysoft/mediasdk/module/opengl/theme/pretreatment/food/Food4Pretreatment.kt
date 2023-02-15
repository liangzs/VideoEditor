package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.food

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment
import org.libpag.PAGFile
import java.util.*
import kotlin.collections.ArrayList

class Food4Pretreatment : BasePreTreatment() {
    /**
     * 转成全局时间轴控件
     */
    private val MIPCOUNT = 3
    override fun getMipmapsCount(): Int {
        return MIPCOUNT
    }

    override fun getRatio916(index: Int): List<Bitmap?> {
        if (index == 0) {
            return Arrays.asList(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/food1"));
        }
        return emptyList();
    }

    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType): Array<Array<String>> {
        return arrayOf(arrayOf("/gif1", "/gif2"))
    }
}