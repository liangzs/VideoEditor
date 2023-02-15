package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday27

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common20.Common20ThemeManager

class HD27ThemeManager: Common20ThemeManager() {


    override fun initFilter() {
        bgFilter = ImageOriginFilter()
        ImageOriginFilter.init(
            bgFilter as ImageOriginFilter, BitmapUtil.decriptImage(ConstantMediaSize.themePath +
                if (width < height) {
                    "/holiday27_bg_9_16"
                } else if (width > height) {
                    "/holiday27_bg_16_9"
                } else {
                    "/holiday27_bg_1_1"
                }
        ), width, height)
    }

    override fun onFilterSizeChanged(width: Int, height: Int) {
        with(bgFilter as ImageOriginFilter) {
            ImageOriginFilter.init(this@with, BitmapUtil.decriptImage(ConstantMediaSize.themePath +
                    if (width < height) {
                        "/holiday27_bg_9_16"
                    } else if (width > height) {
                        "/holiday27_bg_16_9"
                    } else {
                        "/holiday27_bg_1_1"
                    }
            ), width, height)
            onSizeChanged(width, height)
        }
    }

    override fun customCreateWidget(widgetMipmaps: MutableList<Bitmap>?) = false


    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample?) {
        if (index == 0) {
            widgetTimeExample!!.adjustScaling(AnimateInfo.ORIENTATION.RIGHT_TOP, 0f, 1.6f)
        } else if (index == 1) {
            widgetTimeExample!!.adjustScaling(AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, 1.3f)
        }
    }

    override fun createWidgetByIndex(index: Int): ThemeWidgetInfo? {
        return if (index == 0) {
            ThemeWidgetInfo(0, 11200, 0, 0, 11200)
        } else if (index == 1) {
            ThemeWidgetInfo(0, 11200, 0, 0, 11200)
        } else {
            null
        }
    }

    override fun drawPrepare(mediaDrawer: GlobalDrawer?, mediaItem: MediaItem?, index: Int) {
        superDrawPrepare(mediaDrawer, mediaItem, index)
    }
}