package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily3

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.*

class Daily3ThemeManager: BaseTimeThemeManager() {


    init {
        borderImageFilter = ImageOriginFilter().also {
            it.setOnSizeChangedListener { width, height ->
                if (width < height) {
                    it.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/916"))
                } else if (height < width) {
                    it.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/169"))
                } else {
                    it.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/11"))
                }

            }
        }
    }

    override fun onDrawFrame(currenPostion: Int) {
        val temp: ImageOriginFilter?
        if (currenPostion >= 3333) {
            temp = borderImageFilter
            borderImageFilter = null
        } else {
            temp = null
        }
        super.onDrawFrame(currenPostion)
        temp?.let { borderImageFilter = it }
    }

    override fun createWidgetByIndex(index: Int) =
        when(index) {
            0 -> ThemeWidgetInfo(400, 0, 266, 0, 666)
            1 -> ThemeWidgetInfo(400, 0, 267, 666, 1333)
            2 -> ThemeWidgetInfo(400, 0, 267, 1333, 2000)
            3 -> ThemeWidgetInfo(400, 0, 266, 2000, 2666)
            4 -> ThemeWidgetInfo(400, 0, 267, 2666, 3333)
            else -> null
        }

    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample?) {
        widgetTimeExample?.setEnterAction(
            AnimationBuilder(widgetTimeExample.enterTime).setIsNoZaxis(true).setScale(0.2f, 0.5f).setLog(true).build()
        )
        widgetTimeExample?.setOutAction(
            AnimationBuilder(widgetTimeExample.outTime).setIsNoZaxis(true).setScale(0.5f, 0.3f).setLog(true).setFade(1f, 0f).build()
        )
        widgetTimeExample?.adjustScaling(0f, 0f, 1.2f)
    }

    override fun createAnimateEvaluate(index: Int, duration: Long): ArrayList<BaseEvaluate> {
        if (index < 5) {
            val list = ArrayList<BaseEvaluate>()
            list.add(
                AnimationBuilder(
                    (duration - 166).toInt(),
                    width,
                    height,
                    true
                ).build()
            )
            list.add(
                AnimationBuilder(
                    166,
                    width,
                    height,
                    true
                ).setScale(1f, 2f)
                    .setRatioBlurRange(1f, 5f, true)
                    .build()
            )
            return list
        } else {
            val list = ArrayList<BaseEvaluate>()
            list.add(
                AnimationBuilder(
                    166,
                    width,
                    height,
                    true
                ).setScale(0.3f, 1f)
                    .setRatioBlurRange(5f, 1f, false)
                    .build()
            )
            list.add(
                AnimationBuilder(
                    (duration - 166).toInt(),
                    width,
                    height,
                    true
                ).build()
            )
            return list
        }

    }

    override fun createActions() = Integer.MAX_VALUE


    override fun computeThemeCycleTime() = Integer.MAX_VALUE

    override fun blurBackground(): Boolean {
        return true
    }
}