package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday23

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.particle.PAGNoBgParticle
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.ThemePagParticleDrawerProxy
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeFilterContainer
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemePagParticleContainer
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common8.Common8ThemeManager

/**
 * @author hayring
 * @date 2022/11/14
 */
class HD23ThemeManager : Common8ThemeManager() {


    override fun setWidgetMipmaps(widgetMipmaps: MutableList<Bitmap>?) {
        if (particleProxy != null) {
            particleProxy.onDestroy()
        }
        particleProxy = ThemePagParticleDrawerProxy(width, height)
        val particle1 = TimeThemePagParticleContainer(ThemeWidgetInfo(2240, 4480),
            PAGNoBgParticle(true, ConstantMediaSize.themePath + "/hd23action2particle.pag")
            , particleProxy)
        val particle2= TimeThemePagParticleContainer(ThemeWidgetInfo(6720, 8960),
            PAGNoBgParticle(true, ConstantMediaSize.themePath + "/hd23action4particle.pag")
            , particleProxy)

        renders.add(particle1)
        renders.add(particle2)
    }

    override fun drawPrepare(mediaDrawer: GlobalDrawer?, mediaItem: MediaItem?, index: Int) {
        super.drawPrepare(mediaDrawer, mediaItem, index)
        if (index == 0 && renders.size < 3) {
            val gifFilter = GifOriginFilter()
            gifFilter.setFrames(mediaItem!!.dynamicMitmaps[0])
            gifFilter.onCreate()
            gifFilter.setOnSizeChangedListener { width, height ->
                gifFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 1.5f)
            }
            gifFilter.onSizeChanged(width, height)
            renders.add(TimeThemeFilterContainer(ThemeWidgetInfo(0, 2240), gifFilter))
        }
        if (index == 2 && renders.size < 4) {
            val top = GifOriginFilter()
            top.setFrames(mediaItem!!.dynamicMitmaps[0])
            top.onCreate()
            top.setOnSizeChangedListener { width, height ->
                top.adjustScaling(width, height, AnimateInfo.ORIENTATION.TOP, 0f, 1f)
            }
            top.onSizeChanged(width, height)
            renders.add(TimeThemeFilterContainer(ThemeWidgetInfo(4480, 6720), top))

            val bottom = GifOriginFilter()
            bottom.setFrames(mediaItem.dynamicMitmaps[0])
            bottom.onCreate()
            bottom.setOnSizeChangedListener { width, height ->
                bottom.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 1f)
            }
            bottom.onSizeChanged(width, height)
            renders.add(TimeThemeFilterContainer(ThemeWidgetInfo(4480, 6720), bottom))
        }
    }

    override fun computeThemeCycleTime() = 8960

    override fun createActions() = 4


    override fun isTextureInside() = true

    override fun blurBackground() = true
}