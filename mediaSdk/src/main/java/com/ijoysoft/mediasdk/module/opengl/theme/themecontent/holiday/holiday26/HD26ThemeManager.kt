package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday26

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common6.Common6ThemeManager
import java.util.*

class HD26ThemeManager: Common6ThemeManager() {




    override fun onGifDrawerCreated(mediaItem: MediaItem, index: Int) {
        val gifOriginFilter1 = GifOriginFilter()
        gifOriginFilter1.setFrames(mediaItem.dynamicMitmaps[0])
        globalizedGifOriginFilters = listOf(gifOriginFilter1)
    }

    override fun onGifSizeChanged(index: Int, width: Int, height: Int) {
        if (globalizedGifOriginFilters == null) {
            return
        }
        if(width == height) {
            globalizedGifOriginFilters[0].adjustScaling(
                width,
                height,
                AnimateInfo.ORIENTATION.BOTTOM,
                0f,
                2.5f
            )
        } else {
            globalizedGifOriginFilters[0].adjustScaling(
                width,
                height,
                AnimateInfo.ORIENTATION.BOTTOM,
                0f,
                2f
            )
        }
    }
}