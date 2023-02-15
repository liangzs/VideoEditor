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
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common13.Common13ThemeManager
import java.util.*

class Holiday16ThemeManager : Common13ThemeManager() {
    override fun createWidgetByIndex(index: Int): ThemeWidgetInfo? {
        return null
    }


    override fun computeThemeCycleTime(): Int {
        return 12000
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
        globalizedGifOriginFilters[0].adjustScaling(width, height, 0f, 0.85f, 1.2f, 1.2f)
    }
}