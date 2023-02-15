package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily2

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeManagerWithwidget
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.ThemeWidgetHelper
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.ternary
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType

class Daily2ThemeManager : ThemeManagerWithwidget() {
    val actionDurations: Array<Triple<Int, Int, Int>> = arrayOf(Triple(ENTER, STAY, OUT), Triple(ENTER, STAY, OUT), Triple(ENTER, STAY, OUT))

    val widgetDurations: Array<Array<Triple<Int, Int, Int>>> = emptyArray()

    /**
     * 主片段动作
     */
    val clipActions: List<Array<BaseEvaluate?>> = emptyList()

    /**
     * 控件动作
     */
    val widgetActions: List<Array<Array<BaseEvaluate?>?>?> = emptyList()


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

    override fun createClipLocation(): Array<FloatArray> {
        return emptyArray()
    }

    override fun createWidgetLocation(index: Int, indexWidge: Int, widget: BaseThemeExample, bitW: Int, bitH: Int) {

    }


    override fun createPreiod()
            : Int = 3

    override fun blurExample(): Boolean = true

    override fun existGlobalWidget(): Boolean {
        return true;
    }

    override fun createGlobalWidget(mimaps: List<Bitmap>) {
        globalWidget = mutableListOf(
            ImageOriginFilter().apply {
                create()
                onSizeChanged(width, height)
                initTexture(mimaps.get(0))
            }
        )
    }

    override fun themeTransition()
            : TransitionType {
        return TransitionType.PAG_BOOM5
    }


}