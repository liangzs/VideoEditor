package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.christmas.christmas6

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeManagerWithwidget
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.ThemeWidgetHelper
import com.ijoysoft.mediasdk.module.opengl.theme.ternary
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType

class Christmas6ThemeManager : ThemeManagerWithwidget() {
    val actionDurations: Array<Triple<Int, Int, Int>> = arrayOf(Triple(ENTER, STAY, OUT), Triple(ENTER, STAY, OUT), Triple(ENTER, STAY, OUT))

    val widgetDurations: Array<Array<Triple<Int, Int, Int>>> = arrayOf(
        /*1*/
        arrayOf(Triple(ENTER, 0, OUT), Triple(ENTER, 0, OUT), Triple(ENTER, 0, OUT)),
        /*2*/
        arrayOf(Triple(ENTER, 0, OUT)),
        /*3*/
        arrayOf(Triple(ENTER, 0, OUT)))

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
    val widgetActions: List<Array<Array<BaseEvaluate?>?>?> = listOf(arrayOf(arrayOf(
        ThemeWidgetHelper.fade(true, ENTER),
        null,
        ThemeWidgetHelper.fade(false, OUT),
    ), arrayOf(
        ThemeWidgetHelper.fade(true, ENTER),
        null,
        ThemeWidgetHelper.fade(false, OUT),
    ), arrayOf(
        ThemeWidgetHelper.fade(true, ENTER),
        null,
        ThemeWidgetHelper.fade(false, OUT),
    )),
        /*1*/
        arrayOf(arrayOf(
            ThemeWidgetHelper.normalMove(ENTER, AnimateInfo.ORIENTATION.TOP),
            null,
            ThemeWidgetHelper.fade(false, OUT),
        )),
        /*2*/
        arrayOf(arrayOf(
            ThemeWidgetHelper.normalMove(ENTER, AnimateInfo.ORIENTATION.TOP),
            null,
            ThemeWidgetHelper.fade(false, OUT),
        )))


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
        when (index) {
            0 -> {
                when (indexWidge) {
                    0 -> widget.adjustScaling(width, height, bitW, bitH, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, (width < height).ternary(1f, 1f))
                    1 -> widget.adjustScaling(width, height, bitW, bitH, AnimateInfo.ORIENTATION.RIGHT_TOP, 0f, (width < height).ternary(1f, 1f))
                    2 -> widget.adjustScalingFixX(width, height, bitW, bitH, AnimateInfo.ORIENTATION.BOTTOM)
                }
            }
            1 -> {
                when (indexWidge) {
                    0 -> if (width < height) {
                        widget.adjustScalingFixX(width, height, bitW, bitH, AnimateInfo.ORIENTATION.TOP)
                    } else {
                        widget.adjustScaling(width, height, bitW, bitH, AnimateInfo.ORIENTATION.TOP, 0f, 2f)
                    }
                }
            }
            2 -> {
                when (indexWidge) {
                    0 -> if (width < height) {
                        widget.adjustScalingFixX(width, height, bitW, bitH, AnimateInfo.ORIENTATION.TOP)
                    } else {
                        widget.adjustScaling(width, height, bitW, bitH, AnimateInfo.ORIENTATION.TOP, 0f, 2f)
                    }
                }
            }
        }
    }


    override fun createPreiod()
            : Int = 3

    override fun blurExample(): Boolean {
        return true
    }

    override fun createGlobalWidget(mimaps: List<Bitmap>) {
        TODO("Not yet implemented")
    }

    override fun themeTransition()
            : TransitionType {
        return TransitionType.FLASH_WHITE
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
//        if (width < height) {
//            globalizedGifOriginFilters?.get(0)?.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 1f)
//        } else {
        globalizedGifOriginFilters?.get(0)?.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 2f)
//        }
    }

    override fun checkGifshow(gifIndex: Int)
            : Boolean {
        if (currentIndex == 0) {
            return false;
        }
        return true;
    }

    override fun createThemePags()
            : List<ThemeWidgetInfo> {
        return listOf(ThemeWidgetInfo(Long.MIN_VALUE, Long.MAX_VALUE))
    }

}