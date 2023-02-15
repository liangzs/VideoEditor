package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.christmas.christmas0

import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager
import com.ijoysoft.mediasdk.module.opengl.theme.action.EmptyBlurAction
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType

class Christmas0ThemeManager: ThemeOpenglManager.ReflectOpenglThemeManager() {
    override fun themeTransition(): TransitionType {
        return TransitionType.PAG_HALO6
    }

    override fun getFinalActionClasses() = actions

    val actions = mutableListOf(EmptyBlurAction::class.java)

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
        }
    }

    override fun onGifSizeChanged(index: Int, width: Int, height: Int) {
        super.onGifSizeChanged(index, width, height)
        globalizedGifOriginFilters?.get(0)?.adjustScaling(width, height, AnimateInfo.ORIENTATION.TOP, 0f, 1f)
        globalizedGifOriginFilters?.get(1)?.adjustScaling(width, height, AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f,
            if (width < height) {
                2f
            } else if (width > height) {
                2f
            } else {
                2f
            }
        )
        globalizedGifOriginFilters?.get(2)?.adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f,
            if (width < height) {
                0.8f
            } else if (width > height) {
                0.8f
            } else {
                0.8f
            }
        )
    }
}