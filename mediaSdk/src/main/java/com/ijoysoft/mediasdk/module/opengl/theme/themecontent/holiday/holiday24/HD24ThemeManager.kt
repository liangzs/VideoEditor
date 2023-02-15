package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday24

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.particle.PAGNoBgParticle
import com.ijoysoft.mediasdk.module.opengl.theme.ThemePagParticleDrawerProxy
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common14.Common14ThemeManager

/**
 * 片段铺满展示，不展示模糊背景
 * 动态组合动画，完成片段的展示。片段切换时，一个切换动画组合，二是切换片段纹理
 */
class HD24ThemeManager : Common14ThemeManager() {

    override fun createWidgetByIndex(index: Int): ThemeWidgetInfo? {
        return null
    }



    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample) {
        widgetTimeExample.adjustScaling(0f, 0f, 1f)
    }

    val widgetMipmaps = ArrayList<Bitmap>()

    override fun customCreateWidget(widgetMipmaps: List<Bitmap>): Boolean {
        super.customCreateWidget(widgetMipmaps)



        particleProxy = ThemePagParticleDrawerProxy(width, height)






        this.widgetMipmaps.addAll(widgetMipmaps)
        val index = if (width < height) {
            0
        } else if (width > height) {
            3
        } else {
            6
        }
        ThemeWidgetInfo(0, 2000, 0, 0, 2000).apply {
            bitmap = widgetMipmaps[index]
        }.let {
            createWidgetTimeExample(it)
        }

        ThemeWidgetInfo(0, 2000, 0, 2000, 4000).apply {
            bitmap = widgetMipmaps[1 + index]
        }.let {
            createWidgetTimeExample(it)
        }

        ThemeWidgetInfo(0, 2000, 0, 4000, 6000).apply {
            bitmap = widgetMipmaps[2 + index]
        }.let {
            createWidgetTimeExample(it)
        }

        ThemeWidgetInfo(0, 2000, 0, 6000, 8000).apply {
            bitmap = widgetMipmaps[index]
        }.let {
            createWidgetTimeExample(it)
        }

        ThemeWidgetInfo(0, 2000, 0, 8000, 10000).apply {
            bitmap = widgetMipmaps[1 + index]
        }.let {
            createWidgetTimeExample(it)
        }

        ThemeWidgetInfo(0, 2000, 0, 10000, 12000).apply {
            bitmap = widgetMipmaps[2 + index]
        }.let {
            createWidgetTimeExample(it)
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
        val index = if (width < height) {
            0
        } else if (width > height) {
            3
        } else {
            6
        }
        widgetRenders[0].widgetInfo.bitmap = widgetMipmaps[index]
        widgetRenders[1].widgetInfo.bitmap = widgetMipmaps[index + 1]
        widgetRenders[2].widgetInfo.bitmap = widgetMipmaps[index + 2]
        widgetRenders[3].widgetInfo.bitmap = widgetMipmaps[index]
        widgetRenders[4].widgetInfo.bitmap = widgetMipmaps[index + 1]
        widgetRenders[5].widgetInfo.bitmap = widgetMipmaps[index + 2]
        //先切换bitmap再执行super
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight)
        particleProxy.onSurfaceChanged(offsetX,offsetY,width,height,screenWidth, screenHeight)
        particleProxy.switchParticle(PAGNoBgParticle(true, ConstantMediaSize.themePath + "/holiday24.pag"))
    }



    override fun createActions(): Int {
        return 6
    }

    override fun computeThemeCycleTime(): Int {
        return 12000
    }

    override fun onDrawFrame(currenPostion: Int) {
        super.onDrawFrame(currenPostion)
        particleProxy.drawPagParticle(actionRender.textureId)
    }
}