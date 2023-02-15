package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.daily

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment

class Daily6Pretreatment: BaseTimePreTreatment() {

    override fun dealDuration(index: Int) = if (index % 9 < 4) 1300L else 1000L

    override fun createMimapBitmaps(): MutableList<Bitmap> = mutableListOf(
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/capture"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/b_b"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/b"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/b_y"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/y"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/b_pi"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/pi"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/b_r"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/r"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/b_pu"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/pu"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/b_g"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/g"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/b_bl"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bl"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/b_w"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/w"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/b_c"),
        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/c"),
    )



    override fun getMipmapsCount() = 9
}