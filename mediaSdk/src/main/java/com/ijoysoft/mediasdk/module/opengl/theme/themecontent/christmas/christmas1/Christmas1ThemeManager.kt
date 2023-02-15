package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.christmas.christmas1

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder
import com.ijoysoft.mediasdk.module.playControl.GifDecoder

class Christmas1ThemeManager : Christmas1Template() {
    override fun createWidgetByIndex(index: Int) = null

    val borders = ArrayList<Bitmap>(3)


    override fun customCreateWidget(widgetMipmaps: MutableList<Bitmap>?): Boolean {
        if (widgetMipmaps == null || widgetMipmaps.isEmpty()) {
            return true
        }

        val recycleTime = computeThemeCycleTime()
        var themeWidgetInfo = ThemeWidgetInfo(0, recycleTime, 0, 0, recycleTime.toLong())
        borders.addAll(widgetMipmaps.subList(7, widgetMipmaps.size))
        val index = if (width < height) {
            1
        } else if (width > height) {
            2
        } else {
            0
        }
        themeWidgetInfo.apply {
            bitmap = borders[index]
        }.let {
            createWidgetTimeExample(themeWidgetInfo).setOnSizeChangedListener { width, height ->
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
        }

        var showTime = recycleTime / 2

        var startTime = getDuration(showTime.toLong(), 17, 32)
        showTime -= startTime



        themeWidgetInfo = ThemeWidgetInfo(0, showTime / 2, showTime / 2,
            startTime.toLong(), (startTime + showTime).toLong()
        )

        showTime /= 2

        createWidgetTimeExample(themeWidgetInfo.clone().apply { bitmap = widgetMipmaps[0] })
            .apply {
                setStayAction(AnimationBuilder(showTime).setScale(1f, 1.1f))
                setOutAction(AnimationBuilder(showTime).setScale(1.1f, 1f))
            }

        createWidgetTimeExample(themeWidgetInfo.clone().apply { bitmap = widgetMipmaps[1] })
            .apply {
                setStayAction(AnimationBuilder(showTime).setScale(1f, 1.1f))
                setOutAction(AnimationBuilder(showTime).setScale(1.1f, 1f))
            }
        createWidgetTimeExample(themeWidgetInfo.clone().apply { bitmap = widgetMipmaps[2] })
            .apply {
                setStayAction(AnimationBuilder(showTime).setScale(1f, 1.1f))
                setOutAction(AnimationBuilder(showTime).setScale(1.1f, 1f))
            }
        createWidgetTimeExample(themeWidgetInfo.clone().apply { bitmap = widgetMipmaps[3] })
            .apply {
                setStayAction(AnimationBuilder(showTime).setScale(1f, 1.1f))
                setOutAction(AnimationBuilder(showTime).setScale(1.1f, 1f))
            }
        createWidgetTimeExample(themeWidgetInfo.clone().apply { bitmap = widgetMipmaps[4] })
            .apply {
                setStayAction(AnimationBuilder(showTime).setScale(1f, 1.1f))
                setOutAction(AnimationBuilder(showTime).setScale(1.1f, 1f))
            }
        createWidgetTimeExample(themeWidgetInfo.clone().apply { bitmap = widgetMipmaps[5] })
            .apply {
                setStayAction(AnimationBuilder(showTime).setScale(1f, 1.1f))
                setOutAction(AnimationBuilder(showTime).setScale(1.1f, 1f))
            }
        createWidgetTimeExample(themeWidgetInfo.clone().apply { bitmap = widgetMipmaps[6] })
            .apply {
                setStayAction(AnimationBuilder(showTime).setScale(1f, 1.1f))
                setOutAction(AnimationBuilder(showTime).setScale(1.1f, 1f))
            }


        showTime = recycleTime / 2 - getDuration((recycleTime / 2).toLong(), 17, 32)

        startTime += startTime + showTime
        themeWidgetInfo = ThemeWidgetInfo(0, showTime / 2, showTime / 2,
            startTime.toLong(), (startTime + showTime).toLong()
        )


        showTime /= 2

        createWidgetTimeExample(themeWidgetInfo.clone().apply { bitmap = widgetMipmaps[0] })
            .apply {
                setStayAction(AnimationBuilder(showTime).setScale(1f, 1.1f))
                setOutAction(AnimationBuilder(showTime).setScale(1.1f, 1f))
            }

        createWidgetTimeExample(themeWidgetInfo.clone().apply { bitmap = widgetMipmaps[1] })
            .apply {
                setStayAction(AnimationBuilder(showTime).setScale(1f, 1.1f))
                setOutAction(AnimationBuilder(showTime).setScale(1.1f, 1f))
            }
        createWidgetTimeExample(themeWidgetInfo.clone().apply { bitmap = widgetMipmaps[2] })
            .apply {
                setStayAction(AnimationBuilder(showTime).setScale(1f, 1.1f))
                setOutAction(AnimationBuilder(showTime).setScale(1.1f, 1f))
            }
        createWidgetTimeExample(themeWidgetInfo.clone().apply { bitmap = widgetMipmaps[3] })
            .apply {
                setStayAction(AnimationBuilder(showTime).setScale(1f, 1.1f))
                setOutAction(AnimationBuilder(showTime).setScale(1.1f, 1f))
            }
        createWidgetTimeExample(themeWidgetInfo.clone().apply { bitmap = widgetMipmaps[4] })
            .apply {
                setStayAction(AnimationBuilder(showTime).setScale(1f, 1.1f))
                setOutAction(AnimationBuilder(showTime).setScale(1.1f, 1f))
            }
        createWidgetTimeExample(themeWidgetInfo.clone().apply { bitmap = widgetMipmaps[5] })
            .apply {
                setStayAction(AnimationBuilder(showTime).setScale(1f, 1.1f))
                setOutAction(AnimationBuilder(showTime).setScale(1.1f, 1f))
            }
        createWidgetTimeExample(themeWidgetInfo.clone().apply { bitmap = widgetMipmaps[6] })
            .apply {
                setStayAction(AnimationBuilder(showTime).setScale(1f, 1.1f))
                setOutAction(AnimationBuilder(showTime).setScale(1.1f, 1f))
            }




        return true
    }


    override fun onSurfaceChanged(
        offsetX: Int,
        offsetY: Int,
        width: Int,
        height: Int,
        screenWidth: Int,
        screenHeight: Int
    ) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight)
        widgetRenders[1].adjustScaling(AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, 2.5f)
        widgetRenders[2].adjustScaling(AnimateInfo.ORIENTATION.LEFT, 0f, 5f)
        widgetRenders[3].adjustScaling(AnimateInfo.ORIENTATION.LEFT_TOP, 0f, 2.5f)
        widgetRenders[4].adjustScaling(AnimateInfo.ORIENTATION.TOP, 0f, 5f)
        widgetRenders[5].adjustScaling(AnimateInfo.ORIENTATION.RIGHT_TOP, 0f, 2.5f)
        widgetRenders[6].adjustScaling(AnimateInfo.ORIENTATION.RIGHT, 0f, 5f)
        widgetRenders[7].adjustScaling(AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 2.5f)
        widgetRenders[8].adjustScaling(AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, 2.5f)
        widgetRenders[9].adjustScaling(AnimateInfo.ORIENTATION.LEFT, 0f, 5f)
        widgetRenders[10].adjustScaling(AnimateInfo.ORIENTATION.LEFT_TOP, 0f, 2.5f)
        widgetRenders[11].adjustScaling(AnimateInfo.ORIENTATION.TOP, 0f, 5f)
        widgetRenders[12].adjustScaling(AnimateInfo.ORIENTATION.RIGHT_TOP, 0f, 2.5f)
        widgetRenders[13].adjustScaling(AnimateInfo.ORIENTATION.RIGHT, 0f, 5f)
        widgetRenders[14].adjustScaling(AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 2.5f)
    }

    override fun onGifDrawerCreated(mediaItem: MediaItem?, index: Int) {
        super.onGifDrawerCreated(mediaItem, index)
        globalizedGifOriginFilters = ArrayList();
    }

    override fun customCreateGifWidget(
        gifMipmaps: MutableList<MutableList<GifDecoder.GifFrame>>?,
        timeLineStart: Int
    ) {
        val gifOriginFilter1 = GifOriginFilter()
        gifOriginFilter1.setFrames(gifMipmaps!![0])
        gifOriginFilter1.setOnSizeChangedListener { width1: Int, height1: Int ->
            gifOriginFilter1.adjustScaling(
                width1,
                height1,
                AnimateInfo.ORIENTATION.BOTTOM,
                0f,
                2f
            )
        }
        gifOriginFilter1.onCreate()
        gifOriginFilter1.onSizeChanged(width, height)
        globalizedGifOriginFilters.add(gifOriginFilter1)
    }
}