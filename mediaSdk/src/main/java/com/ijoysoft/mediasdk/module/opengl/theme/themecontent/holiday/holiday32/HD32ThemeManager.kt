package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday32

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common8.Common8ThemeManager

class HD32ThemeManager: Common8ThemeManager() {

    /**
     * 背景
     */
    protected var bgFilter: AFilter? = null


    override fun blurBackground(): Boolean {
        return false
    }


    override fun onDrawFrame(currenPostion: Int) {
        bgFilter!!.draw()
        super.onDrawFrame(currenPostion)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (bgFilter != null) {
            bgFilter!!.onDestroy()
        }
    }


    override fun onSurfaceCreated() {
        super.onSurfaceCreated()
        initFilter()
    }


    protected fun initFilter() {

        bgFilter = ImageOriginFilter()
        ImageOriginFilter.init(
            bgFilter as ImageOriginFilter, BitmapUtil.decriptImage(
                ConstantMediaSize.themePath +
                    if (width < height) {
                        "/holiday32_9_16"
                    } else if (width > height) {
                        "/holiday32_16_9"
                    } else {
                        "/holiday32_1_1"
                    }
            ), width, height)
    }

    fun onFilterSizeChanged(width: Int, height: Int) {
        with(bgFilter as ImageOriginFilter) {
            ImageOriginFilter.init(this@with, BitmapUtil.decriptImage(ConstantMediaSize.themePath +
                    if (width < height) {
                        "/holiday32_9_16"
                    } else if (width > height) {
                        "/holiday32_16_9"
                    } else {
                        "/holiday32_1_1"
                    }
            ), width, height)
            onSizeChanged(width, height)
        }
    }


    override fun onSurfaceChanged(
        offsetX: Int,
        offsetY: Int,
        width: Int,
        height: Int,
        screenWidth: Int,
        screenHeight: Int
    ) {
        onFilterSizeChanged(width, height)
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight)
    }
}