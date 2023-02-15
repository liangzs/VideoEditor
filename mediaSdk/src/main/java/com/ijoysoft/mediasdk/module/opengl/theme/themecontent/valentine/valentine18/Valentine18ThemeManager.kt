package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine18

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeManagerWithwidget
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType

class Valentine18ThemeManager : ThemeManagerWithwidget() {
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

    init {
        bgImageFilter = ImageOriginFilter()
    }

    override fun createClip(): Array<Triple<Int, Int, Int>> {
        return actionDurations
    }

    override fun createWidget(): Array<Array<Triple<Int, Int, Int>>> {
        return widgetDurations;
    }

    override fun createClipAction(): List<Array<BaseEvaluate?>> {
        return emptyList()
    }

    override fun createWidgetAction(): List<Array<Array<BaseEvaluate?>?>?> {
        return widgetActions;
    }

    override fun createWidgetLocation(index: Int, widgetIndex: Int, widget: BaseThemeExample, bitW: Int, bitH: Int) {
    }

    override fun createClipLocation(): Array<FloatArray> {
        return emptyArray()
    }


    override fun createPreiod()
            : Int = 3

    override fun blurExample(): Boolean {
        return true
    }

    override fun existGlobalWidget(): Boolean {
        return true
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
        return TransitionType.WAVE
    }

    override fun onGifDrawerCreated(mediaItem: MediaItem?, index: Int
    ) {
        mediaItem?.let {
            globalizedGifOriginFilters = ArrayList()
            globalizedGifOriginFilters.add(GifOriginFilter().also {
                it.setFrames(mediaItem.dynamicMitmaps[0])
            })
        }
    }

    override fun onGifSizeChanged(index: Int, width: Int, height: Int) {
        super.onGifSizeChanged(index, width, height)
        globalizedGifOriginFilters?.get(0)?.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 2f)
    }

}