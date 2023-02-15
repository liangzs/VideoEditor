package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday25

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeRenderContainer
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common11.Common11ThemeManager
import java.util.*

/**
 * @author hayring
 * @date 2022/1/13  16:04
 */
class HD25ThemeManager : Common11ThemeManager() {
    override fun customCreateWidget(widgetMipmaps: List<Bitmap>): Boolean {
        super.customCreateWidget(widgetMipmaps)

        val render =
            ParticleBuilder(ParticleType.CENTER_EXPAND, ArrayList(widgetMipmaps))
                .setParticleCount(30)
                .setPointSize(2, 20)
                .setExpanSpeedMulti(2f)
                .setSelfRotate(true)
                .setLoadFirstFrameByGenerator(true)
                .build()
        renders.add(TimeThemeRenderContainer(
            listOf(ThemeWidgetInfo(0,
            computeThemeCycleTime().toLong()
        )), render))

        return true
    }

    override fun onGifDrawerCreated(mediaItem: MediaItem, index: Int) {
        val gifOriginFilter = GifOriginFilter()
        globalizedGifOriginFilters = listOf(gifOriginFilter)
        gifOriginFilter.setFrames(mediaItem.dynamicMitmaps[0])
    }

    override fun onGifSizeChanged(index: Int, width: Int, height: Int) {
        if (globalizedGifOriginFilters == null) {
            return
        }
        if (width < height) {
            globalizedGifOriginFilters[0].adjustScaling(
                width,
                height,
                AnimateInfo.ORIENTATION.TOP,
                0f,
                1f
            )
        } else if (width > height) {
            globalizedGifOriginFilters[0].adjustScaling(
                width,
                height,
                AnimateInfo.ORIENTATION.TOP,
                0f,
                2f
            )
        } else {
            globalizedGifOriginFilters[0].adjustScaling(
                width,
                height,
                AnimateInfo.ORIENTATION.TOP,
                0f,
                1.5f
            )
        }
    }


}