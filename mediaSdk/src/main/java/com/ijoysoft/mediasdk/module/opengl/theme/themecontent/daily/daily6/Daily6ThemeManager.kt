package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily6

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.*

class Daily6ThemeManager: BaseTimeThemeManager() {



    override fun customCreateWidget(widgetMipmaps: MutableList<Bitmap>?): Boolean {


        for ( i in 0 until 4) {

            val start = 1300 * i
            val mid = start + 400
            val end = mid + 900

            widgetRenders.add(
                createWidgetTimeExample(ThemeWidgetInfo(start.toLong(),
                    end.toLong(), widgetMipmaps!![1 + 2 * i])).also {
                        it.setOnSizeChangedListener { _, _ ->
                            it.adjustScaling(0f, 0f, 1f)
                        }
                }
            )
            widgetRenders.add(
                createWidgetTimeExample(ThemeWidgetInfo(start.toLong(),
                    mid.toLong(), widgetMipmaps[0])).also {
                    it.setOnSizeChangedListener { _, _ ->
                        it.adjustScaling(0f, 0f, 2f)
                    }
                }
            )
            widgetRenders.add(
                createWidgetTimeExample(ThemeWidgetInfo(mid.toLong(),
                    end.toLong(), widgetMipmaps[2 + 2 * i])).also {
                    it.setOnSizeChangedListener { _, _ ->
                        it.adjustScaling(0f, 0f, 2f)
                    }
                }
            )
        }
        for ( i in 4 until 9) {

            val start = 5200 + (i - 4) * 1000
            val mid = start + 400
            val end = mid + 600

            widgetRenders.add(
                createWidgetTimeExample(ThemeWidgetInfo(start.toLong(),
                    end.toLong(), widgetMipmaps!![1 + 2 * i])).also {
                    it.setOnSizeChangedListener { _, _ ->
                        it.adjustScaling(0f, 0f, 1f)
                    }
                }
            )
            widgetRenders.add(
                createWidgetTimeExample(ThemeWidgetInfo(400, 600, 0, start.toLong(),
                    end.toLong()).also { it.bitmap = widgetMipmaps[2 + 2 * i] }).also {
                    it.setOnSizeChangedListener { _, _ ->
                        it.adjustScaling(0f, 0f, 2f)
                    }
                }
            )
        }

        return true
    }

    override fun createWidgetByIndex(index: Int) =  null

    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample?) {


    }

    override fun createAnimateEvaluate(index: Int, duration: Long): ArrayList<BaseEvaluate> {
        if (index % 9 < 4) {
            val list = ArrayList<BaseEvaluate>()
            list.add(
                AnimationBuilder(
                    134,
                    width,
                    height,
                    true
                )
                    .setScale(1f, 1.1f)
                    .build()
            )
            list.add(
                AnimationBuilder(
                    266,
                    width,
                    height,
                    true
                )
                    .setScale(1.1f, 1f)
                    .build()
            )
            list.add(
                AnimationBuilder(
                    900,
                    width,
                    height,
                    true
                ).build()
            )
            return list
        } else {

            val list = ArrayList<BaseEvaluate>()
            list.add(
                AnimationBuilder(
                    400,
                    width,
                    height,
                    true
                )
                    .setScale(1.2f, 1f)
                    .build()
            )
            list.add(
                AnimationBuilder(
                    600,
                    width,
                    height,
                    true
                ).build()
            )
            return list
        }
    }

    override fun createActions() = 9


    override fun computeThemeCycleTime() = 10200
}