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
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType

class Food4ThemeManager : ThemeManagerWithwidget() {
    val actionDurations: Array<Triple<Int, Int, Int>> = arrayOf(Triple(ENTER, STAY, OUT), Triple(ENTER, STAY, OUT), Triple(ENTER, STAY, OUT))

    val widgetDurations: Array<Array<Triple<Int, Int, Int>>> = arrayOf(
        /*1*/
        arrayOf(Triple(ENTER, 0, 0)),

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
                ThemeWidgetHelper.normalMove(ENTER, AnimateInfo.ORIENTATION.TOP),
                null,
                null,
            ))

        //
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
        widget.adjustScaling(width, height, bitW, bitH, AnimateInfo.ORIENTATION.TOP, 0f, 2f)
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
        return TransitionType.SHADE_SQUARE_RE
    }


    override fun onGifDrawerCreated(mediaItem: MediaItem?, index: Int
    ) {
        mediaItem?.let {
            globalizedGifOriginFilters = ArrayList()
            globalizedGifOriginFilters.add(GifOriginFilter().also {
                it.setFrames(mediaItem.dynamicMitmaps[0])
            })
            globalizedGifOriginFilters.add(GifOriginFilter().also {
                it.setFrames(mediaItem.dynamicMitmaps[1])
            })
        }
    }

    var gifVertext1: FloatArray? = null;
    var gifVertext2: FloatArray? = null;
    override fun onGifSizeChanged(index: Int, width: Int, height: Int) {
        super.onGifSizeChanged(index, width, height)
        gifVertext1 = globalizedGifOriginFilters?.get(0)?.adjustScaling(width, height, -0.4f, 0.5f, 2f)
        gifVertext2 = globalizedGifOriginFilters?.get(0)?.adjustScaling(width, height, 0.4f, -0.2f, 2f)
        globalizedGifOriginFilters?.get(1)?.adjustScaling(width, height, 0.4f, 0.6f, 2f)
    }

    override fun onDrawFrameGif() {
        if (!ObjectUtils.isEmpty(globalizedGifOriginFilters)) {
            for (i in globalizedGifOriginFilters.indices) {
                if (i == 0) {
                    globalizedGifOriginFilters[i].draw()
                    globalizedGifOriginFilters[i].setVertex(gifVertext1)
                    globalizedGifOriginFilters[i].draw()
                    globalizedGifOriginFilters[i].setVertex(gifVertext2)
                } else {
                    globalizedGifOriginFilters[i].draw()
                }
            }
        }
    }


}