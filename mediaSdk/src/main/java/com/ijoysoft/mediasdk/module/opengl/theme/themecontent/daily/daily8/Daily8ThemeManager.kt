package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily7

import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.*
import com.ijoysoft.mediasdk.module.playControl.GifDecoder

class Daily8ThemeManager: BaseTimeThemeManager() {



    override fun customCreateGifWidget(
        gifMipmaps: MutableList<MutableList<GifDecoder.GifFrame>>?,
        timeLineStart: Int
    ) {
        val gifOriginFilter1 = GifOriginFilter()
        gifOriginFilter1.setFrames(gifMipmaps!![0])
        gifOriginFilter1.setOnSizeChangedListener { _, _ ->
            gifOriginFilter1.adjustScalingByCube(floatArrayOf(
                -1.0f, 1.0f,
                -1.0f, -1.0f,
                1.0f, 1.0f,
                1.0f, -1.0f
            ))
        }
        gifOriginFilter1.onCreate()
        gifOriginFilter1.onSizeChanged(width, height)

        val gifOriginFilter2 = GifOriginFilter()
        gifOriginFilter2.setFrames(gifMipmaps!![1])
        gifOriginFilter2.setOnSizeChangedListener { _, _ ->
            gifOriginFilter2.adjustScaling(width, height, 0f, 0f, 2f, 2f)
        }
        gifOriginFilter2.onCreate()
        gifOriginFilter2.onSizeChanged(width, height)

        globalizedGifOriginFilters = listOf(gifOriginFilter1, gifOriginFilter2)
    }

    override fun createWidgetByIndex(index: Int) =  null

    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample?) {}

    override fun createAnimateEvaluate(index: Int, duration: Long): ArrayList<BaseEvaluate> {
        val list = ArrayList<BaseEvaluate>()
        list.add(
            AnimationBuilder(
                240,
                width,
                height,
                true
            )
                .setScale(1.2f, 1f)
                .build()
        )
        list.add(
            AnimationBuilder(
                900,
                width,
                height,
                true
            )
                .build()
        )
        return list

    }

    override fun createActions() = 1


    override fun computeThemeCycleTime() = 1140
}