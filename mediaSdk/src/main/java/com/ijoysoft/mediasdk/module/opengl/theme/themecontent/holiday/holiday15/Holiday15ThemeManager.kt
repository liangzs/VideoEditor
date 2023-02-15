package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday15

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.action.ThemeWidgetHelper
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeRenderContainer
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common15.Common15ThemeManager
import java.util.*

class Holiday15ThemeManager : Common15ThemeManager() {
    override fun createWidgetByIndex(index: Int): ThemeWidgetInfo? {
        return null
    }

    override fun customCreateWidget(widgetMipmaps: List<Bitmap>): Boolean {
        //粒子 2400+2400+2400
        var render = ParticleBuilder(ParticleType.RANDOM, ArrayList(widgetMipmaps.subList(2, 3))).setParticleCount(8)
            .setParticleSizeRatio(0.02f)
            .setAlpha(true)
            .build()
        renders.add(TimeThemeRenderContainer(Collections.singletonList(ThemeWidgetInfo(1200, 4200)), render))
        //控件
        var themeWidgetInfo = ThemeWidgetInfo(1200, 0, 1000, 0, 2200)
        themeWidgetInfo.bitmap = widgetMipmaps[0]
        var widgetExample = createWidgetTimeExample(themeWidgetInfo)
        widgetExample.setEnterAction(ThemeWidgetHelper.springMove(2000, AnimateInfo.ORIENTATION.BOTTOM))
        widgetExample.setOutAction(AnimationBuilder().setFade(1f, 0f))

        //控件
        themeWidgetInfo = ThemeWidgetInfo(800, 0, 800, 2400, 5000)
        themeWidgetInfo.bitmap = widgetMipmaps[1]
        widgetExample = createWidgetTimeExample(themeWidgetInfo)
        widgetExample.setEnterAction(ThemeWidgetHelper.normalMove(800, AnimateInfo.ORIENTATION.BOTTOM))
        widgetExample.setOutAction(AnimationBuilder().setFade(1f, 0f))
        return true
    }

    /**
     * 获取了width后再进行定位
     */
    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample?) {
        super.intWidget(index, widgetTimeExample)
        widgetTimeExample?.apply {
            when (index) {
                0 -> {
                    if (width > height) {
                        adjustScaling(0f, 0.7f, 2.5f)
                    } else {
                        adjustScaling(0f, 0.7f, 3.2f)
                    }
                }
                1 -> adjustScaling(AnimateInfo.ORIENTATION.BOTTOM, 0f, 1.2f)
            }
        }


    }

    override fun computeThemeCycleTime(): Int {
        return 5400
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
        globalizedGifOriginFilters[0].adjustScaling(width, height, 0f, -0.85f, 1.2f, 1.2f)
    }
}