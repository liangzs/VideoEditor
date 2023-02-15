package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine17

import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.*

class Valentine17ThemeManager : BaseTimeThemeManager() {

    override fun createWidgetByIndex(index: Int) =
        ThemeWidgetInfo(0, 0, 10500, 0, 10500)


    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample?) {
        val scale = if (width < height) 1.5f else 3f
        val cube = widgetTimeExample!!.adjustScalingWithoutSettingCube(
            AnimateInfo.ORIENTATION.BOTTOM,
            0f,
            scale
        )
        val center = AFilter.getCubeCenter(cube)
        widgetTimeExample.adjustScaling(0f, 0f, scale)
        widgetTimeExample.setOutAction(
            AnimationBuilder(10500)
                .setStayPos(center)
                .setScale(1f, 1.1f)
                .setScaleAction(SinWaveAction(1050, false, true, false, false))
                .setLog(true)
        )
    }

    override fun createAnimateEvaluate(index: Int, duration: Long): ArrayList<BaseEvaluate> {
        if (index % 9 == 0) {
            val list = ArrayList<BaseEvaluate>()
            list.add(
                AnimationBuilder(
                    1000,
                    width,
                    height,
                    true
                )
                    .setMoveAction(EaseOutQuart(false, true, false, false))
                    .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                    .setScale(1.4f, 1f)
                    .setRatioBlurRange(5f, 0f, false)
                    .build()
            )
            list.add(
                AnimationBuilder(
                    500,
                    width,
                    height,
                    true
                ).setAdjective(false)
                    .build()
            )
            list.add(
                AnimationBuilder(
                    1000,
                    width,
                    height,
                    true
                )
                    .setMoveAction(EaseInQuart(false, true, false, false))
                    .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                    .setScale(1f, 1.4f)
                    .setRatioBlurRange(0f, 5f, true)
                    .build()
            )
            if (duration > currentDuration) {
                list.add(
                    AnimationBuilder((duration - currentDuration).toInt(), width, height, true)
                        .build()
                )
            }
            return list
        } else if (index % 9 == 1) {
            val list = ArrayList<BaseEvaluate>()
            list.add(
                AnimationBuilder(
                    200,
                    width,
                    height,
                    true
                )
                    .setMoveAction(EaseOutQuart(false, true, false, false))
                    .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                    .setScale(1.4f, 1f)
                    .setRatioBlurRange(5f, 0f, false)
                    .build()
            )
            list.add(
                AnimationBuilder(
                    800,
                    width,
                    height,
                    true
                )
                    .build()
            )
            return list
        } else {
            return ArrayList<BaseEvaluate>().also {
                it.add(AnimationBuilder(
                    duration.toInt(),
                    width,
                    height,
                    true
                )
                    .build())
            }
        }
    }

    override fun createActions() = 9


    override fun computeThemeCycleTime() = 10500

    override fun onGifDrawerCreated(mediaItem: MediaItem?, index: Int
    ) {
        mediaItem?.let {
            globalizedGifOriginFilters = ArrayList()
            globalizedGifOriginFilters.add(GifOriginFilter().also {
                it.setFrames(mediaItem.dynamicMitmaps[0])
            })
        }
    }

}