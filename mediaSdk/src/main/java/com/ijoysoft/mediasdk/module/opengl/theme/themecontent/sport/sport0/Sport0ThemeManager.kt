package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.sport.sport0

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

class Sport0ThemeManager: ReflectOpenglThemeManager() {
    override fun getFinalActionClasses() = mutableListOf(EmptyBlurAction::class.java)


    val imageOriginFilter = ImageOriginFilter()

    override fun onGifDrawerCreated(mediaItem: MediaItem?, index: Int) {
        mediaItem?.let {
            globalizedGifOriginFilters = ArrayList()
            globalizedGifOriginFilters.add(GifOriginFilter().also {
                it.setFrames(mediaItem.dynamicMitmaps[0])
            })
        }
    }

    override fun onGifSizeChanged(index: Int, width: Int, height: Int) {
        super.onGifSizeChanged(index, width, height)
        val cube = globalizedGifOriginFilters?.get(0)?.adjustScalingWithoutSettingCube(width, height, AnimateInfo.ORIENTATION.RIGHT_TOP, 0f, 2f)
        cube?.let {
            for (i in 0 until 8 step 2){
                cube[i] -= 0.1f
                cube[i+1] += 0.1f
            }
            globalizedGifOriginFilters[0].adjustScalingByCube(cube)
        }
    }



    override fun createThemePags(): List<ThemeWidgetInfo> {
        //return listOf(ThemeWidgetInfo(0, recyleTime.toLong()), ThemeWidgetInfo(0, recyleTime.toLong()))
        return listOf(ThemeWidgetInfo(0, Long.MAX_VALUE))
    }

    override fun dealThemePag(index: Int, themePagFilter: ThemePagFilter?) {
        super.dealThemePag(index, themePagFilter)
        themePagFilter?.pagFile?.pagFile?.let { pag ->
            pag.setMatrix(Matrix().also {
                val scale = 0.3f
                val offsetx = 0.95f * width - pag.width() * 0.3f
                val offsety = 0.95f * height - pag.height() * 0.3f
                it.postScale(scale, scale)
                it.postTranslate(offsetx, offsety)
            })
        }
        if (index == 0) {
            themePagFilter?.matrix = MatrixUtils.getOriginalMatrix().run {
                MatrixUtils.flip(this, false, true)
            }
        }
    }


    override fun drawPrepare(mediaDrawer: GlobalDrawer?, mediaItem: MediaItem?, index: Int) {
        super.drawPrepare(mediaDrawer, mediaItem, index)
        mThemePagFilters.get(0).pagFile?.pagFile?.let {
            val text = it.getTextData(0)
            text.text = index.toString()
            it.replaceText(0, text)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        imageOriginFilter.bitmap?.recycle()
        imageOriginFilter.onDestroy()
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
        imageOriginFilter.bitmap?:run{
            val bitmap = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/runner" + ConstantMediaSize.SUFFIX)
            imageOriginFilter.initTexture(bitmap)
        }
        val cube = ImageOriginFilter.adjustScalingWithoutSettingCube(imageOriginFilter, width, height, imageOriginFilter.bitmap.width, imageOriginFilter.bitmap.height, AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, 2f)
        cube?.let {
            for (i in 0 until 8 step 2){
                cube[i] += 0.1f
                cube[i+1] -= 0.1f
            }
            imageOriginFilter.adjustScalingByCube(cube)
        }
    }

    override fun onSurfaceCreated() {
        super.onSurfaceCreated()
        imageOriginFilter.create()
    }

    override fun onDrawFrame() {
        super.onDrawFrame()
        imageOriginFilter.draw()
    }
}