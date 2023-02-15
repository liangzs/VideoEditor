package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.food

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeManagerWithwidget
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.ThemeWidgetHelper
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType

class Food3ThemeManager : ThemeManagerWithwidget() {
    val actionDurations: Array<Triple<Int, Int, Int>> = arrayOf(Triple(ENTER, STAY, OUT), Triple(ENTER, STAY, OUT), Triple(ENTER, STAY, OUT))

    val widgetDurations: Array<Array<Triple<Int, Int, Int>>> = emptyArray()

    /**
     * 主片段动作
     */
    val clipActions: List<Array<BaseEvaluate?>> = listOf(arrayOf(
        null,
        null,
        ThemeWidgetHelper.fade(true, ENTER),
    ))

    /**
     * 控件动作
     */
    val widgetActions: List<Array<Array<BaseEvaluate?>?>?> = emptyList()

    init {
        bgImageFilter = ImageOriginFilter()
    }


    override fun isforGround(): Boolean {
        return true
    }

    override fun ratioChange() {
        bgImageFilter?.let {
            when (ConstantMediaSize.ratioType) {
                RatioType._3_4 -> it.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/34"))
                RatioType._9_16 -> it.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/916"))
                RatioType._4_3 -> it.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/43"))
                RatioType._16_9 -> it.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/169"))
                RatioType._1_1 -> it.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/11"))
            }
        }

    }

    override fun createClip(): Array<Triple<Int, Int, Int>> {
        return actionDurations
    }

    override fun createWidget(): Array<Array<Triple<Int, Int, Int>>> {
        return widgetDurations;
    }

    override fun createClipAction(): List<Array<BaseEvaluate?>> {
        //return clipActions;
        return emptyList()
    }

    override fun createWidgetAction(): List<Array<Array<BaseEvaluate?>?>?> {
        return widgetActions;
    }

    override fun createWidgetLocation(index: Int, widgetIndex: Int, widget: BaseThemeExample, bitW: Int, bitH: Int) {
    }

    override fun createPreiod(): Int {
        return 1
    }

    override fun createClipLocation(): Array<FloatArray> {
        return emptyArray()
    }


    override fun blurExample(): Boolean {
        return true
    }

    override fun createGlobalWidget(mimaps: List<Bitmap>) {
    }

    override fun themeTransition()
            : TransitionType {
        return TransitionType.WIPE_LEFT
    }

}