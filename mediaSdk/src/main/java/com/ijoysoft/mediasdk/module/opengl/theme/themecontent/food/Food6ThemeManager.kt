package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.food

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.utils.ObjectUtils
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeManagerWithwidget
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.ThemeWidgetHelper
import com.ijoysoft.mediasdk.module.opengl.theme.ternary
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType

class Food6ThemeManager : ThemeManagerWithwidget() {
    val actionDurations: Array<Triple<Int, Int, Int>> = arrayOf(Triple(ENTER, STAY, OUT), Triple(ENTER, STAY, OUT), Triple(ENTER, STAY, OUT))

    val widgetDurations: Array<Array<Triple<Int, Int, Int>>> = arrayOf(
        /*1*/
        arrayOf(Triple(ENTER, STAY, OUT)),
        arrayOf(Triple(ENTER, STAY, OUT)),
        arrayOf(Triple(ENTER, STAY, OUT)),
        //
    )


    /**
     * 主片段动作
     */
    val clipActions: List<Array<BaseEvaluate?>> = emptyList()

    /**
     * 控件动作
     */
    val widgetActions: List<Array<Array<BaseEvaluate?>?>?> = listOf(
        //1
        arrayOf(
            arrayOf(
                ThemeWidgetHelper.springMove(ENTER, AnimateInfo.ORIENTATION.TOP),
                null,
                null,
            )),

        //2
        arrayOf(
            arrayOf(
                ThemeWidgetHelper.normalMove(ENTER, AnimateInfo.ORIENTATION.BOTTOM),
                null,
                null,
            )),
        //3
        arrayOf(
            arrayOf(
                ThemeWidgetHelper.normalMove(ENTER, AnimateInfo.ORIENTATION.BOTTOM),
                null,
                null,
            )
        ),
    )


    override fun isforGround(): Boolean {
        return true
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


    /**
     * 控件位置
     */
    override fun createWidgetLocation(index: Int, widgetIndex: Int, widget: BaseThemeExample, bitW: Int, bitH: Int) {
        when (index) {
            0 -> {
                widget.adjustScaling(width, height, bitW, bitH, AnimateInfo.ORIENTATION.TOP, 0f, (width < height).ternary(2f, 3f))
            }
            1 -> {
                widget.adjustScaling(width, height, bitW, bitH, -0.4f, 0.7f, (width < height).ternary(2f, 3f))
            }
            2 -> {
                widget.adjustScaling(width, height, bitW, bitH, 0.4f, 0.7f, (width < height).ternary(4f, 4.5f))
            }
        }

    }

    override fun createPreiod(): Int {
        return 3
    }

    override fun createClipLocation(): Array<FloatArray> {
        return emptyArray()
    }


    override fun blurExample(): Boolean {
        return true
    }

    /**
     * 创建全局，这里自定义创建
     */
    override fun createGlobalWidget(mimaps: List<Bitmap>) {
    }

    override fun themeTransition()
            : TransitionType {
        return TransitionType.PAG_CARTON11
    }


}