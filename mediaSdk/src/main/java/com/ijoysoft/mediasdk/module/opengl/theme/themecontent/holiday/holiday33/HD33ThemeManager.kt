package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday33

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter
import com.ijoysoft.mediasdk.module.opengl.particle.PAGNoBgParticle
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager.ReflectOpenglThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.ThemePagParticleDrawerProxy
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType

class HD33ThemeManager: ReflectOpenglThemeManager() {

    override fun themeTransition(): TransitionType? {
        return TransitionType.READ_BOOK
    }

    override fun getFinalActionClasses() = actions

    val actions = mutableListOf(HD33Action::class.java)



    val pagParticleProxy = ThemePagParticleDrawerProxy(width, height).apply { rotateVertical = true }




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
            pagParticleProxy.switchParticle(PAGNoBgParticle(true, ConstantMediaSize.themePath + "/holiday33.pag"))
        }
        pagParticleProxy.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight)
    }



    override fun onDestroy() {
        super.onDestroy()
        pagParticleProxy.onDestroy()
    }
}