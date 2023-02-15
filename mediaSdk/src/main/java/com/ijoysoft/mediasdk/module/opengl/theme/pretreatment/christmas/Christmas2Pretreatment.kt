package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.christmas

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment

class Christmas2Pretreatment: BaseTimePreTreatment() {

    override fun dealDuration(index: Int) = if (index % 8 < 5) 1333L else 800L


    override fun createMimapBitmaps() = mutableListOf(
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas2_1lt"), //0
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas2_1t"),    //1
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas2_2lb"),   //2
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas2_3lt"),   //3
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/chrsitmas2_3t"),    //4
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas2_4b"),    //5
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas2_5rb"),   //6
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas2_6rb"),   //7


        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas2_bg1_1_1"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas2_bg1_9_16"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas2_bg1_16_9"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas2_bg2_1_1"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas2_bg2_9_16"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas2_bg2_16_9")
    )

    override fun getMipmapsCount() = 8
}