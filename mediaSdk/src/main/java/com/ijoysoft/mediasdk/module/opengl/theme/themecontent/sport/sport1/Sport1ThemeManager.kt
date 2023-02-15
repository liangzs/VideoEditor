package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.sport.sport1

import android.graphics.Matrix
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.common.utils.MatrixUtils
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter
import com.ijoysoft.mediasdk.module.opengl.filter.ThemePagFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager.ReflectOpenglThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.action.EmptyBlurAction
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType

class Sport1ThemeManager: ReflectOpenglThemeManager() {
    override fun getFinalActionClasses() = mutableListOf(Sport1Action::class.java)


    val rt = ImageOriginFilter()
    val rb = ImageOriginFilter()
    val lb = ImageOriginFilter()





    override fun onDestroy() {
        super.onDestroy()
        rt.bitmap?.recycle()
        rt.onDestroy()
        rb.bitmap?.recycle()
        rb.onDestroy()
        lb.bitmap?.recycle()
        lb.onDestroy()
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
        rt.bitmap?:run{
            val bitmap = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/rt" + ConstantMediaSize.SUFFIX)
            rt.initTexture(bitmap)
        }
        var cube = ImageOriginFilter.adjustScalingWithoutSettingCube(rt, width, height, rt.bitmap.width, rt.bitmap.height, AnimateInfo.ORIENTATION.RIGHT_TOP, 0f, 3f)
        cube?.let {
            for (i in 0 until 8 step 2){
                cube[i] -= 0.1f
                cube[i+1] += 0.1f
            }
            rt.adjustScalingByCube(cube)
        }

        rb.bitmap?:run{
            val bitmap = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/rb" + ConstantMediaSize.SUFFIX)
            rb.initTexture(bitmap)
        }
        cube = ImageOriginFilter.adjustScalingWithoutSettingCube(rb, width, height, rb.bitmap.width, rb.bitmap.height, AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 2f)
        cube?.let {
            for (i in 0 until 8 step 2){
                cube[i] -= 0.1f
                cube[i+1] -= 0.1f
            }
            rb.adjustScalingByCube(cube)
        }

        lb.bitmap?:run{
            val bitmap = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/lb" + ConstantMediaSize.SUFFIX)
            lb.initTexture(bitmap)
        }
        cube = ImageOriginFilter.adjustScalingWithoutSettingCube(lb, width, height, lb.bitmap.width, lb.bitmap.height, AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, 2f)
        cube?.let {
            for (i in 0 until 8 step 2){
                cube[i] += 0.1f
                cube[i+1] -= 0.1f
            }
            lb.adjustScalingByCube(cube)
        }


    }

    override fun onSurfaceCreated() {
        super.onSurfaceCreated()
        rt.create()
        rb.create()
        lb.create()
    }

    override fun onDrawFrame() {
        super.onDrawFrame()
        rt.draw()
        rb.draw()
        lb.draw()
    }

    override fun themeTransition() = TransitionType.PAG_CARTON10
}