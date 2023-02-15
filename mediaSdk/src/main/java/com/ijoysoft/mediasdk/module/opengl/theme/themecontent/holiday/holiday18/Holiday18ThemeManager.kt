package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday15

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.action.ThemeWidgetHelper
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common8.Common8ThemeManager

class Holiday18ThemeManager : Common8ThemeManager() {
    override fun createWidgetByIndex(index: Int): ThemeWidgetInfo? {
        return null
    }

    override fun customCreateWidget(widgetMipmaps: List<Bitmap>): Boolean {
        //粒子 2400+2400+2400
        //var render = ParticleBuilder(ParticleType.RANDOM, ArrayList(widgetMipmaps.subList(5, 6))).setParticleCount(5)
        //    .setParticleSizeRatio(0.05f)
        //    .setAlpha(true)
        //    .build()
        //renders.add(TimeThemeRenderContainer(Collections.singletonList(ThemeWidgetInfo(1600, 3200)), render))
        //banner的全局控件
        var themeWidgetInfo = ThemeWidgetInfo(0, 10000, 0, 0, 10000)
        themeWidgetInfo.bitmap = widgetMipmaps[0]
        createWidgetTimeExample(themeWidgetInfo)

        themeWidgetInfo = ThemeWidgetInfo(1200, 0, 1000, 0, 2400)
        themeWidgetInfo.bitmap = widgetMipmaps[1]
        var widgetExample = createWidgetTimeExample(themeWidgetInfo)
        widgetExample.setEnterAction(ThemeWidgetHelper.springMove(2000, AnimateInfo.ORIENTATION.BOTTOM))
        widgetExample.setOutAction(AnimationBuilder().setFade(1f, 0f))

        //控件
        themeWidgetInfo = ThemeWidgetInfo(1200, 0, 800, 3200, 5600)
        themeWidgetInfo.bitmap = widgetMipmaps[2]
        widgetExample = createWidgetTimeExample(themeWidgetInfo)
        widgetExample.setEnterAction(ThemeWidgetHelper.springMove(800, AnimateInfo.ORIENTATION.BOTTOM))
        widgetExample.setOutAction(AnimationBuilder().setFade(1f, 0f))

        //三
        themeWidgetInfo = ThemeWidgetInfo(800, 0, 800, 6400, 8800)
        themeWidgetInfo.bitmap = widgetMipmaps[3]
        widgetExample = createWidgetTimeExample(themeWidgetInfo)
        widgetExample.setEnterAction(ThemeWidgetHelper.normalMove(800, AnimateInfo.ORIENTATION.RIGHT))
        widgetExample.setOutAction(AnimationBuilder().setFade(1f, 0f))
        return true
    }

    override fun computeThemeCycleTime(): Int {
        return 10000;
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
                        adjustScaling(AnimateInfo.ORIENTATION.TOP, 0f, 2.2f)
                    } else {
                        adjustScalingFixX(AnimateInfo.ORIENTATION.TOP)
                    }
                }
                1 -> {
                    if (width > height) {
                        adjustScaling(AnimateInfo.ORIENTATION.BOTTOM, 0.6f, 2.2f)
                        return
                    }
                    adjustScaling(AnimateInfo.ORIENTATION.BOTTOM, 0.5f, 3.2f)
                }
                2 -> adjustScaling(AnimateInfo.ORIENTATION.BOTTOM, -0.5f, 2.2f)
                3 -> adjustScaling(AnimateInfo.ORIENTATION.BOTTOM, 0.5f, 2.2f)
            }
        }


    }
}