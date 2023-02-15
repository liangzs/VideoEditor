package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday15

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.action.ThemeWidgetHelper
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.ternary
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common12.Common12ThemeManager
import java.util.*

class Holiday19ThemeManager : Common12ThemeManager() {
    override fun createWidgetByIndex(index: Int): ThemeWidgetInfo? {
        return null
    }

    override fun customCreateWidget(widgetMipmaps: List<Bitmap>): Boolean {
        super.customCreateWidget(widgetMipmaps)
        var themeWidgetInfo = ThemeWidgetInfo(0, 6400, 0, 0, 6400)
        themeWidgetInfo.bitmap = widgetMipmaps[0]
        var widgetExample = createWidgetTimeExample(themeWidgetInfo)

        //控件
        themeWidgetInfo = ThemeWidgetInfo(0, 6400, 0, 0, 6400)
        themeWidgetInfo.bitmap = widgetMipmaps[1]
        widgetExample = createWidgetTimeExample(themeWidgetInfo)


        //三
        themeWidgetInfo = ThemeWidgetInfo(0, 6400, 0, 0, 6400)
        themeWidgetInfo.bitmap = widgetMipmaps[2]
        widgetExample = createWidgetTimeExample(themeWidgetInfo)
        //widgetExample.setEnterAction(ThemeWidgetHelper.fade(true, 800))
        return true
    }

    override fun computeThemeCycleTime(): Int {
        return 6400;
    }

    override fun blurBackground(): Boolean {
        return true
    }

    /**
     * 获取了width后再进行定位
     */
    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample?) {
        super.intWidget(index, widgetTimeExample)
        widgetTimeExample?.apply {
            when (index) {
                0 -> adjustScalingFixX(AnimateInfo.ORIENTATION.LEFT_BOTTOM, (width < height).ternary(0.9f, 1.2f))
                1 -> {
                    setStayAction(ThemeWidgetHelper.zoomScale(2400, adjustScaling(0.6f, 0.7f, 3.2f)))
                }
                2 -> {
                    setStayAction(ThemeWidgetHelper.zoomScale(2400, adjustScaling(0.6f, -0.7f, 3.2f)))
                }
            }
        }
    }

    override fun onGifDrawerCreated(mediaItem: MediaItem, index: Int) {
        val gifOriginFilter1 = GifOriginFilter()
        gifOriginFilter1.setFrames(mediaItem.dynamicMitmaps[0])
        globalizedGifOriginFilters = Arrays.asList(gifOriginFilter1)
    }

    override fun onGifSizeChanged(index: Int, width: Int, height: Int) {
        if (globalizedGifOriginFilters == null) {
            return
        }
        globalizedGifOriginFilters[0].adjustScaling(width, height, AnimateInfo.ORIENTATION.TOP, -0.6f, 1.4f)
    }

}