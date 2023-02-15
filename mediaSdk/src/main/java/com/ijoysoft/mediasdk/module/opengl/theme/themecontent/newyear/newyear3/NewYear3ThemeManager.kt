package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.newyear.newyear3

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.SinWaveAction
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common6.Common6ThemeManager

class NewYear3ThemeManager: Common6ThemeManager() {

    override fun computeThemeCycleTime() = cycleTime

    override fun createWidgetByIndex(index: Int) = if (index == 0) {
        ThemeWidgetInfo(0, 0, cycleTime, 0, cycleTime.toLong())
    } else null

    val borders = ArrayList<Bitmap>(3)

    override fun customCreateWidget(widgetMipmaps: MutableList<Bitmap>?): Boolean {
        super.customCreateWidget(widgetMipmaps)
        borders.addAll(widgetMipmaps!!.subList(1, 4))
        var themeWidgetInfo = ThemeWidgetInfo(0, cycleTime, 0, 0, cycleTime.toLong())
        var index = if (width < height) {
            1
        } else if (width > height) {
            2
        } else {
            0
        }
        themeWidgetInfo.apply {
            bitmap = borders[index]
        }
        createWidgetTimeExample(themeWidgetInfo).setOnSizeChangedListener { w, h ->
            val index = if (width < height) {
                1
            } else if (width > height) {
                2
            } else {
                0
            }
            widgetRenders[0].widgetInfo.bitmap = borders[index]
            widgetRenders[0].init()
            widgetRenders[0].adjustPaddingScaling()
        }
        return false
    }

    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample?) {
        if (index == 1) {
            val cube: FloatArray
            if (width < height) {
                cube = widgetTimeExample!!.adjustScaling(0f, 0.35f, 2f)
            } else if (width > height) {
                cube = widgetTimeExample!!.adjustScaling(0f, 0.25f, 5f)
            } else {
                cube = widgetTimeExample!!.adjustScaling(0f, 0.3f, 4f)
            }
            val center = AFilter.getCubeCenter(cube)
            if (width < height) {
                widgetTimeExample!!.adjustScaling(0f, 0f, 2f)
            } else if (width > height) {
                widgetTimeExample!!.adjustScaling(0f, 0f, 4f)
            } else {
                widgetTimeExample!!.adjustScaling(0f, 0f, 3f)
            }
            widgetTimeExample.setOutAction(
                AnimationBuilder(cycleTime)
                    .setStayPos(center)
                    .setScale(1f, 1.1f)
                    .setScaleAction(SinWaveAction(1000, false, true, false, false))
                    .setLog(true)
            )
        }
    }

    companion object {
        const val cycleTime = 9800
    }

}