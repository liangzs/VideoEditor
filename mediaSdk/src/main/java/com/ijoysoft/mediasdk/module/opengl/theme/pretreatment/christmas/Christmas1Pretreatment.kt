package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.christmas

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment

class Christmas1Pretreatment: BaseTimePreTreatment() {

    override fun createMimapBitmaps() = mutableListOf(
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas1_lb"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas1_l"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas1_lt"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas1_t"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas1_rt"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas1_r"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas1_rb"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas1_border_1_1"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas1_border_9_16"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas1_border_16_9")
    )

    override fun dealDuration(index: Int) = 2120L


    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType?): Array<Array<String>> {
        return arrayOf(arrayOf("/christmas1_title_dynamic"))
    }

    override fun getMipmapsCount() = 2
}