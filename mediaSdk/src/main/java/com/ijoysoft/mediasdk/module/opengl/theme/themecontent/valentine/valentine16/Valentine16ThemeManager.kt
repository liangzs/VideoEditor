package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine16

import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.action.ThemeWidgetHelper
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.SinWaveAction
import com.ijoysoft.mediasdk.module.opengl.theme.ternary
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common15.Common15ThemeManager

class Valentine16ThemeManager : Common15ThemeManager() {

    override fun computeThemeCycleTime() = 8000

    override fun createWidgetByIndex(index: Int): ThemeWidgetInfo =
        when (index) {
            0 -> {
                ThemeWidgetInfo(0, 0, 8000, 0, 8000)

            }
            else -> {
                ThemeWidgetInfo(0, 0, 8000, 0, 8000)
            }
        }

    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample?) {
        when (index) {
            0 -> {
                val cube: FloatArray = widgetTimeExample!!.adjustScaling(AnimateInfo.ORIENTATION.LEFT_TOP, 0f, 2f)
                AFilter.vertextMove(cube, (width > height).ternary(0.1f, 0.25f), AnimateInfo.ORIENTATION.LEFT)
                AFilter.vertextMove(cube, (width > height).ternary(0.1f, 0.1f), AnimateInfo.ORIENTATION.TOP)
                val center = AFilter.getCubeCenter(cube)
                widgetTimeExample.adjustScaling(0f, 0f, (width > height).ternary(4f, 2.5f))
                widgetTimeExample!!.setOutAction(
                    AnimationBuilder(8000)
                        .setStayPos(center)
                        .setScale(1f, 1.1f)
                        .setScaleAction(SinWaveAction(1000, false, true, false, false))
                        .setLog(true)
                )
            }
            1 -> {
                widgetTimeExample!!.adjustScaling(AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, (width > height).ternary(1.6f, 2.5f))
                widgetTimeExample.setOutAction(
                    ThemeWidgetHelper.springMove(5000, AnimateInfo.ORIENTATION.BOTTOM)
                )
            }
        }
    }
}