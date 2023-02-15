package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.newyear.newyear1

import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.SinWaveAction
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common15.Common15ThemeManager

class NewYear1ThemeManager : Common15ThemeManager() {

    override fun computeThemeCycleTime() = 8000

    override fun createWidgetByIndex(index: Int): ThemeWidgetInfo =
        ThemeWidgetInfo(0, 0, 8000, 0, 8000)

    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample?) {
        when (index) {
            0 -> {
                val cube = widgetTimeExample!!.adjustScalingWithoutSettingCube(
                    AnimateInfo.ORIENTATION.LEFT_BOTTOM,
                    0f,
                    2f
                )
                val center = AFilter.getCubeCenter(cube)
                widgetTimeExample.adjustScaling(0f, 0f, 2f)
                widgetTimeExample.setOutAction(
                    AnimationBuilder(8000)
                        .setStayPos(center)
                        .setScale(1f, 1.1f)
                        .setScaleAction(SinWaveAction(1000, false, true, false, false))
                        .setLog(true)
                )
            }
            1 -> {
                //原地缩放模板
                val cube = widgetTimeExample!!.adjustScalingWithoutSettingCube(
                    AnimateInfo.ORIENTATION.RIGHT_BOTTOM,
                    0f,
                    2f
                )
                val center = AFilter.getCubeCenter(cube)
                widgetTimeExample.adjustScaling(0f, 0f, 2f)
                widgetTimeExample.setOutAction(
                    AnimationBuilder(8000)
                        .setStayPos(center)
                        .setScale(1f, 1.1f)
                        .setScaleAction(SinWaveAction(1000, false, true, false, false))
                        .setLog(true)
                )
            }
            2 -> {
                widgetTimeExample?.adjustScaling(
                    AnimateInfo.ORIENTATION.BOTTOM,
                    0f,
                    if (width < height) 1.5f else if (height < width) 3f else 2f)
            }
        }
    }
}