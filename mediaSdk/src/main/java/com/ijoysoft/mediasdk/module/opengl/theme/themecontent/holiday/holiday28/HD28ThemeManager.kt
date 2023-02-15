package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday28

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.particle.PAGNoBgParticle
import com.ijoysoft.mediasdk.module.opengl.theme.ThemePagParticleDrawerProxy
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemePagParticleContainer
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common11.Common11ThemeManager

/**
 * @author hayring
 * @date 2022/1/13  16:04
 */
class HD28ThemeManager : Common11ThemeManager() {


    val backgrounds = ArrayList<Bitmap>()

    override fun customCreateWidget(widgetMipmaps: MutableList<Bitmap>?): Boolean {
        super.customCreateWidget(widgetMipmaps)

        val time = computeThemeCycleTime()

        if (particleProxy != null) {
            particleProxy.onDestroy()
        }
        particleProxy = ThemePagParticleDrawerProxy(width, height).apply { rotateVertical = true }
        renders.add(
            TimeThemePagParticleContainer(ThemeWidgetInfo(0, time.toLong()),
                PAGNoBgParticle(true, ConstantMediaSize.themePath + "/holiday28.pag")
                , particleProxy)
        )

        backgrounds.addAll(widgetMipmaps!!)
        val index = if (width < height) {
            0
        } else if (width > height) {
            1
        } else {
            2
        }
        ThemeWidgetInfo(0, time, 0, 0, time.toLong()).apply {
            bitmap = widgetMipmaps[index]
        }.let {
            createWidgetTimeExample(it)
        }
        return false
    }


    override fun onSurfaceChanged(
        offsetX: Int,
        offsetY: Int,
        width: Int,
        height: Int,
        screenWidth: Int,
        screenHeight: Int
    ) {
        //先切换bitmap
        val index = if (width < height) {
            0
        } else if (width > height) {
            1
        } else {
            2
        }
        widgetRenders[0].widgetInfo.bitmap = backgrounds[index]
        widgetRenders[0].init()
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight)
    }


    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample) {
        widgetTimeExample.adjustImageScalingStretch(width , height)
    }
}