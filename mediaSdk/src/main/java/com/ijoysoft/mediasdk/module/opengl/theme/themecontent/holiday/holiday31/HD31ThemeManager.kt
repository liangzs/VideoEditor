package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday31

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter
import com.ijoysoft.mediasdk.module.opengl.particle.PAGNoBgParticle
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager.ReflectOpenglThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.ThemePagParticleDrawerProxy
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType

class HD31ThemeManager: ReflectOpenglThemeManager() {


    val actions = mutableListOf(HD31Action::class.java)


    override fun getFinalActionClasses() = actions


    val pagParticleProxy = ThemePagParticleDrawerProxy(width, height).apply { rotateVertical = true }

    override fun themeTransition() = TransitionType.FADE_IN

    override fun onDrawFrame() {
        super.onDrawFrame()
        pagParticleProxy.drawPagParticle((actionRender as AFilter).textureId)
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
        pagParticleProxy.pagParticle?:run {
            pagParticleProxy.switchParticle(PAGNoBgParticle(true, ConstantMediaSize.themePath + "/holiday31.pag"))
        }
        pagParticleProxy.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight)
    }



    override fun onDestroy() {
        super.onDestroy()
        pagParticleProxy.onDestroy()
    }

}