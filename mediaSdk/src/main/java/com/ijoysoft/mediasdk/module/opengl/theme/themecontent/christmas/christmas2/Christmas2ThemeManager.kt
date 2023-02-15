package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.christmas.christmas2

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter.OnSizeChangedListener
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo

class Christmas2ThemeManager : Christmas2Template() {
    override fun createWidgetByIndex(index: Int) = null


    val borders = ArrayList<Bitmap>(6)


    inner class BorderOnSizeChangeListener(val pos: Int) : OnSizeChangedListener {
        override fun onSizeChanged(width: Int, height: Int) {
            val index = if (width < height) {
                1 + if (pos > 3) 3 else 0
            } else if (width > height) {
                2 + if (pos > 3) 3 else 0
            } else {
                0 + if (pos > 3) 3 else 0
            }
            widgetRenders[pos].widgetInfo.bitmap = borders[index]
            widgetRenders[pos].init()
            widgetRenders[pos].adjustPaddingScaling()
        }

    }


    override fun customCreateWidget(widgetMipmaps: MutableList<Bitmap>?): Boolean {
        if (widgetMipmaps == null || widgetMipmaps.isEmpty()) {
            return true
        }

        borders.addAll(widgetMipmaps.subList(8, widgetMipmaps.size))


        var themeWidgetInfo = ThemeWidgetInfo(0, 1333, 0, 0, 1333)
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
        createWidgetTimeExample(themeWidgetInfo).setOnSizeChangedListener(BorderOnSizeChangeListener(0))
        themeWidgetInfo = themeWidgetInfo.createForNextWithBitmap(2666)
        createWidgetTimeExample(themeWidgetInfo).setOnSizeChangedListener(BorderOnSizeChangeListener(1))
        themeWidgetInfo = themeWidgetInfo.createForNextWithBitmap(2666)
        createWidgetTimeExample(themeWidgetInfo).setOnSizeChangedListener(BorderOnSizeChangeListener(2))
        themeWidgetInfo = ThemeWidgetInfo(0, 800, 0, 7465, 8265).apply { bitmap = borders[index] }
        createWidgetTimeExample(themeWidgetInfo).setOnSizeChangedListener(BorderOnSizeChangeListener(3))



        index = if (width < height) {
            4
        } else if (width > height) {
            5
        } else {
            3
        }
        themeWidgetInfo = ThemeWidgetInfo(0, 1333, 0, 1333, 2666).apply {
            bitmap = borders[index]
        }
        createWidgetTimeExample(themeWidgetInfo).setOnSizeChangedListener(BorderOnSizeChangeListener(4))
        themeWidgetInfo = themeWidgetInfo.createForNextWithBitmap(2666)
        createWidgetTimeExample(themeWidgetInfo).setOnSizeChangedListener(BorderOnSizeChangeListener(5))
        themeWidgetInfo = ThemeWidgetInfo(0, 800, 0, 6665, 7465).apply { bitmap = borders[index] }
        createWidgetTimeExample(themeWidgetInfo).setOnSizeChangedListener(BorderOnSizeChangeListener(6))
        themeWidgetInfo = themeWidgetInfo.createForNextWithBitmap(1600)
        createWidgetTimeExample(themeWidgetInfo).setOnSizeChangedListener(BorderOnSizeChangeListener(7))


        //0
        themeWidgetInfo = ThemeWidgetInfo(0, 1333, 0, 0, 1333)
        themeWidgetInfo.bitmap = widgetMipmaps[0]
        createWidgetTimeExample(themeWidgetInfo).apply {

        }

        themeWidgetInfo = themeWidgetInfo.clone()
        themeWidgetInfo.bitmap = widgetMipmaps[1]
        createWidgetTimeExample(themeWidgetInfo).apply {

        }


        //1
        themeWidgetInfo = ThemeWidgetInfo(0, 1333, 0, 1333, 2666)
        themeWidgetInfo.bitmap = widgetMipmaps[2]
        createWidgetTimeExample(themeWidgetInfo).apply {

        }
        themeWidgetInfo = themeWidgetInfo.clone()
        themeWidgetInfo.bitmap = widgetMipmaps[7]
        createWidgetTimeExample(themeWidgetInfo).apply {

        }

        //2

        themeWidgetInfo = ThemeWidgetInfo(0, 1333, 0, 2666, 3999)
        themeWidgetInfo.bitmap = widgetMipmaps[3]
        createWidgetTimeExample(themeWidgetInfo).apply {

        }

        themeWidgetInfo = themeWidgetInfo.clone()
        themeWidgetInfo.bitmap = widgetMipmaps[4]
        createWidgetTimeExample(themeWidgetInfo).apply {

        }


        //3
        themeWidgetInfo = ThemeWidgetInfo(0, 1333, 0, 3999, 5332)
        themeWidgetInfo.bitmap = widgetMipmaps[5]
        createWidgetTimeExample(themeWidgetInfo).apply {

        }

        //4

        themeWidgetInfo = ThemeWidgetInfo(0, 1333, 0, 5332, 6665)
        themeWidgetInfo.bitmap = widgetMipmaps[6]
        createWidgetTimeExample(themeWidgetInfo).apply {

        }

        themeWidgetInfo = themeWidgetInfo.clone()
        themeWidgetInfo.bitmap = widgetMipmaps[1]
        createWidgetTimeExample(themeWidgetInfo).apply {

        }


        //5
        themeWidgetInfo = ThemeWidgetInfo(0, 800, 0, 6665, 7465)
        themeWidgetInfo.bitmap = widgetMipmaps[2]
        createWidgetTimeExample(themeWidgetInfo).apply {

        }

        themeWidgetInfo = themeWidgetInfo.clone()
        themeWidgetInfo.bitmap = widgetMipmaps[7]
        createWidgetTimeExample(themeWidgetInfo).apply {

        }

        //6
        themeWidgetInfo = ThemeWidgetInfo(0, 800, 0, 7465, 8265)
        themeWidgetInfo.bitmap = widgetMipmaps[3]
        createWidgetTimeExample(themeWidgetInfo).apply {

        }

        themeWidgetInfo = themeWidgetInfo.clone()
        themeWidgetInfo.bitmap = widgetMipmaps[4]
        createWidgetTimeExample(themeWidgetInfo).apply {

        }

        //7

        themeWidgetInfo = ThemeWidgetInfo(0, 800, 0, 8265, 9065)
        themeWidgetInfo.bitmap = widgetMipmaps[6]
        createWidgetTimeExample(themeWidgetInfo).apply {

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
        //先切换bitmap
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight)
        var i = 8
        //0
        widgetRenders[i++].adjustScaling(AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, 1.25f)
        widgetRenders[i++].adjustScaling(AnimateInfo.ORIENTATION.TOP, 0f, 1f)
        //1
        widgetRenders[i++].adjustScaling(AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, 1.25f)
        widgetRenders[i++].adjustScaling(AnimateInfo.ORIENTATION.RIGHT_TOP, 0f, 2f)
        //2
        widgetRenders[i++].adjustScaling(AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, 1.25f)
        widgetRenders[i++].adjustScaling(AnimateInfo.ORIENTATION.TOP, 0f, 1f)
        //3
        widgetRenders[i++].adjustScaling(AnimateInfo.ORIENTATION.BOTTOM, 0f, 2f)
        //4
        widgetRenders[i++].adjustScaling(AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 1.25f)
        widgetRenders[i++].adjustScaling(AnimateInfo.ORIENTATION.TOP, 0f, 1f)

        //5
        widgetRenders[i++].adjustScaling(AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, 1.25f)
        widgetRenders[i++].adjustScaling(AnimateInfo.ORIENTATION.RIGHT_TOP, 0f, 2f)
        //6
        widgetRenders[i++].adjustScaling(AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, 1.25f)
        widgetRenders[i++].adjustScaling(AnimateInfo.ORIENTATION.TOP, 0f, 1f)

        //7
        widgetRenders[i++].adjustScaling(AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 1.25f)
    }

}