package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily1

import com.ijoysoft.mediasdk.module.opengl.filter.AFilter
import com.ijoysoft.mediasdk.module.opengl.filter.PureColorFilter
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager.ReflectOpenglThemeManager
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType

class Daily1ThemeManager : ReflectOpenglThemeManager() {
    override fun getFinalActionClasses() = mutableListOf(
        LargerAction::class.java,
        MoveLeftAction0::class.java,
        MoveLeftAction::class.java,
        MoveLeftAction::class.java,
        SmallerAction0::class.java,
        SmallerAction::class.java,
    )


    override fun themeTransition(): TransitionType {
        return super.themeTransition()
    }

    override fun setIsPureColor() {
        pureColorFilter?.setIsPureColor(floatArrayOf(0f, 0f, 0f, 1f))
    }

    override fun onDestroy() {
        pureColorFilter?.onDestroy()
        super.onDestroy()
    }

    override fun onSurfaceCreated() {
        pureColorFilter?.create()
        super.onSurfaceCreated()
    }

    override fun onSurfaceChanged(
        offsetX: Int,
        offsetY: Int,
        width: Int,
        height: Int,
        screenWidth: Int,
        screenHeight: Int
    ) {
        pureColorFilter?.onSizeChanged(width, height)
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight)
    }

    override fun onDrawFrame() {
        pureColorFilter?.draw()
        super.onDrawFrame()
    }
}