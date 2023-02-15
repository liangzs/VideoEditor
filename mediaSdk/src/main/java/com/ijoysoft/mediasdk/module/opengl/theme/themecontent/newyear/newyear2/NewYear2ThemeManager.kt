package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.newyear.newyear2

import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager.ReflectOpenglThemeManager
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType

class NewYear2ThemeManager: ReflectOpenglThemeManager() {

    override fun themeTransition() = TransitionType.BLUR


    override fun getFinalActionClasses() = mutableListOf(NewYear2Action::class.java)

    override fun onGifDrawerCreated(mediaItem: MediaItem?, index: Int) {
        mediaItem?.let {
            globalizedGifOriginFilters = ArrayList()
            globalizedGifOriginFilters.add(GifOriginFilter().also {
                it.setFrames(mediaItem.dynamicMitmaps[0])
            })
            globalizedGifOriginFilters.add(GifOriginFilter().also {
                it.setFrames(mediaItem.dynamicMitmaps[1])
            })
            globalizedGifOriginFilters.add(GifOriginFilter().also {
                it.setFrames(mediaItem.dynamicMitmaps[2])
            })
            globalizedGifOriginFilters.add(GifOriginFilter().also {
                it.setFrames(mediaItem.dynamicMitmaps[3])
            })
        }
    }

    override fun onGifSizeChanged(index: Int, width: Int, height: Int) {
        super.onGifSizeChanged(index, width, height)
        globalizedGifOriginFilters?.get(0)?.adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, 2f)
        globalizedGifOriginFilters?.get(1)?.adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, 2f)
        globalizedGifOriginFilters?.get(2)?.adjustScaling(width, height, AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 2f)
        globalizedGifOriginFilters?.get(3)?.adjustScaling(width, height, AnimateInfo.ORIENTATION.RIGHT_TOP, 0f, 2f)

    }
}