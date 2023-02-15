package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily7

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.*
import com.ijoysoft.mediasdk.module.playControl.GifDecoder

class Daily7ThemeManager: BaseTimeThemeManager() {



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
        globalizedGifOriginFilters = listOf(gifOriginFilter1)
    }

    override fun createWidgetByIndex(index: Int) =  null

    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample?) {}

    override fun createAnimateEvaluate(index: Int, duration: Long): ArrayList<BaseEvaluate> {
        val list = ArrayList<BaseEvaluate>()
        list.add(
            AnimationBuilder(
                134,
                width,
                height,
                true
            )
                .setFade(0f, 1f)
                .build()
        )
        list.add(
            AnimationBuilder(
                733,
                width,
                height,
                true
            )
                .build()
        )
        list.add(
            AnimationBuilder(
                133,
                width,
                height,
                true
            )
                .setFade(1f, 0f)
                .build()
        )
        return list

    }

    override fun createActions() = 1


    override fun computeThemeCycleTime() = 1000
}