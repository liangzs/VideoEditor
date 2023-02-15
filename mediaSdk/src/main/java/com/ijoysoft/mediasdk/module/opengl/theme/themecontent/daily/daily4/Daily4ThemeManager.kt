package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily4

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager.ReflectOpenglThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.action.EmptyBlurAction
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType

open class Daily4ThemeManager : ReflectOpenglThemeManager() {
    override fun getFinalActionClasses() = mutableListOf(
        EmptyBlurAction::class.java
    )


    override fun themeTransition(): TransitionType {
        return TransitionType.PAG_INTEREST_CAMERA1
    }


    val imageOriginFilter = ImageOriginFilter().also {
        it.setOnSizeChangedListener { width, height ->
            if(width < height) {
                it.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/_9_16"))
            } else if (height < width) {
                it.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/_16_9"))
            } else {
                it.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/_1_1"))
            }
        }
    }


    override fun onSurfaceCreated() {
        super.onSurfaceCreated()
        imageOriginFilter.create()
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
        imageOriginFilter.onSizeChanged(width, height)
    }

    override fun onDrawFrame() {
        super.onDrawFrame()
        imageOriginFilter.draw()
    }
}