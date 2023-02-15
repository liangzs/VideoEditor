package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday29

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
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common6.Common6ThemeManager

class HD29ThemeManager: Common6ThemeManager() {

    override fun customCreateWidget(widgetMipmaps: MutableList<Bitmap>?): Boolean {
        super.customCreateWidget(widgetMipmaps)

        if (particleProxy != null) {
            particleProxy.onDestroy()
        }
        particleProxy = ThemePagParticleDrawerProxy(width, height)
        val particle1 = TimeThemePagParticleContainer(
            ThemeWidgetInfo(0, 10600),
            PAGNoBgParticle(true, ConstantMediaSize.themePath + "/holiday29.pag")
            , particleProxy)

        renders.add(particle1)
        return false
    }


    override fun createWidgetByIndex(index: Int): ThemeWidgetInfo? {
        return if (index < 3) {
            ThemeWidgetInfo(1000, 5600, 0, 0, 6600)
        } else if (index == 3) {
            ThemeWidgetInfo(2000, 2600, 2000, 0, 6600)
        } else {
            null
        }
    }

    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample?) {
        widgetTimeExample?.setEnterAction(AnimationBuilder(1000).setFade(0f,1f))
        when(index) {
            0-> {
                widgetTimeExample?.adjustScaling(AnimateInfo.ORIENTATION.LEFT_TOP, 0f,
                    if (width < height) {
                        1.2f
                    } else if (width > height) {
                        1.33f
                    } else {
                        1.2f
                    })
            }
            1-> {
                widgetTimeExample?.adjustScaling(AnimateInfo.ORIENTATION.TOP, 0f,
                    if (width > height) {
                    2.4f
                } else if (width < height) {
                    2.4f
                } else {
                    2f
                })
            }
            2-> {
                widgetTimeExample?.adjustScaling(AnimateInfo.ORIENTATION.RIGHT_TOP, 0f,
                    if (width < height) {
                        1.2f
                    } else if (width > height) {
                        1.33f
                    } else {
                        1.2f
                    })
            }
            3 -> {
                if (width < height) {
                    widgetTimeExample?.let {
                        it.adjustScaling(it.width, it.height, it.widgetInfo.bitmap!!.width, it.widgetInfo.bitmap!!.height, 0f, 0.6f, 1.2f, 1.2f)
                    }
                } else {
                    widgetTimeExample?.let {
                        it.adjustScaling(it.width, it.height, it.widgetInfo.bitmap!!.width, it.widgetInfo.bitmap!!.height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 2f)
                    }
                }
            }
        }
    }

    override fun drawPrepare(mediaDrawer: GlobalDrawer?, mediaItem: MediaItem?, index: Int) {
        super.drawPrepare(mediaDrawer, mediaItem, index)
        if (index == 0 && renders.size < 2) {
            val title = GifOriginFilter()
            title.setFrames(mediaItem!!.dynamicMitmaps[0])
            title.onCreate()
            title.setOnSizeChangedListener { width, height ->
                if (width < height) {
                    title.adjustScaling(width, height, 0f, 0.6f, 1.2f, 1.2f)
                } else {
                    title.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 2f)
                }
            }
            title.onSizeChanged(width, height)
            renders.add(TimeThemeFilterContainer(ThemeWidgetInfo(6600, 10600), title))
        }
    }
}