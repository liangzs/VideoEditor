package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily0

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.*

class Daily0ThemeManager: BaseTimeThemeManager() {

    init {
        borderImageFilter = ImageOriginFilter().also {
            it.setOnSizeChangedListener { width, height ->
                if (width < height) {
                    it.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/_9_16"))
                } else if (height < width) {
                    it.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/_16_9"))
                } else {
                    it.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/_1_1"))
                }

            }
        }
    }


    override fun onGifDrawerCreated(mediaItem: MediaItem?, index: Int) {
        if (index == 0 && globalizedGifOriginFilters == null) {
            globalizedGifOriginFilters = ArrayList(2)
            val gif1 = GifOriginFilter().also {
                mediaItem?.let { mediaItem -> it.setFrames(mediaItem.dynamicMitmaps[0]) }
                it.setOnSizeChangedListener { width, height ->
                    val cube: FloatArray
                    if (width < height) {
                        cube = it.adjustScalingWithoutSettingCube(width, height, AnimateInfo.ORIENTATION.RIGHT_TOP, 0f, 1.8f)
                        cube[0] -= 0.125f
                        cube[2] -= 0.125f
                        cube[4] -= 0.125f
                        cube[6] -= 0.125f
                        val deltaY = 116f/1280f
                        cube[1] += deltaY
                        cube[3] += deltaY
                        cube[5] += deltaY
                        cube[7] += deltaY
                    } else if (width > height) {
                        cube = it.adjustScalingWithoutSettingCube(width, height, AnimateInfo.ORIENTATION.RIGHT_TOP, 0f, 1.2f)
                        cube[0] -= 0.1125f
                        cube[2] -= 0.1125f
                        cube[4] -= 0.1125f
                        cube[6] -= 0.1125f
                        val deltaY = 64f/720f
                        cube[1] += deltaY
                        cube[3] += deltaY
                        cube[5] += deltaY
                        cube[7] += deltaY
                    } else {
                        cube = it.adjustScalingWithoutSettingCube(width, height, AnimateInfo.ORIENTATION.RIGHT_TOP, 0f, 1.8f)
                        cube[0] -= 0.125f
                        cube[2] -= 0.125f
                        cube[4] -= 0.125f
                        cube[6] -= 0.125f
                        val deltaY = 64f/720f
                        cube[1] += deltaY
                        cube[3] += deltaY
                        cube[5] += deltaY
                        cube[7] += deltaY
                    }
                    it.adjustScalingByCube(cube)
                }
            }
            globalizedGifOriginFilters.add(gif1)
            val gif2 = GifOriginFilter().also {
                mediaItem?.let { mediaItem -> it.setFrames(mediaItem.dynamicMitmaps[1]) }
                it.setOnSizeChangedListener { width, height ->
                    val cube: FloatArray
                    if (width < height) {
                        cube = it.adjustScalingWithoutSettingCube(width, height, AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, 2f)
                        cube[0] += 0.125f
                        cube[2] += 0.125f
                        cube[4] += 0.125f
                        cube[6] += 0.125f
                        val deltaY = 116f/1280f
                        cube[1] -= deltaY
                        cube[3] -= deltaY
                        cube[5] -= deltaY
                        cube[7] -= deltaY
                    } else if (width > height) {
                        cube = it.adjustScalingWithoutSettingCube(width, height, AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, 1.3f)
                        cube[0] += 0.1125f
                        cube[2] += 0.1125f
                        cube[4] += 0.1125f
                        cube[6] += 0.1125f
                        val deltaY = 82f/720f
                        cube[1] -= deltaY
                        cube[3] -= deltaY
                        cube[5] -= deltaY
                        cube[7] -= deltaY
                    } else {
                        cube = it.adjustScalingWithoutSettingCube(width, height, AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, 2f)
                        cube[0] += 0.125f
                        cube[2] += 0.125f
                        cube[4] += 0.125f
                        cube[6] += 0.125f
                        val deltaY = 64f/720f
                        cube[1] -= deltaY
                        cube[3] -= deltaY
                        cube[5] -= deltaY
                        cube[7] -= deltaY
                    }
                    it.adjustScalingByCube(cube)
                }
            }
            globalizedGifOriginFilters.add(gif2)
        }
    }

    override fun customCreateWidget(widgetMipmaps: MutableList<Bitmap>?): Boolean {
        widgetRenders.add(
            createWidgetTimeExample(ThemeWidgetInfo(0, 1000, widgetMipmaps!![0])).also {

                it.setOnSizeChangedListener { width, height ->
                    val cube: FloatArray
                    if (width > height) {
                        cube = it.adjustScalingWithoutSettingCube(AnimateInfo.ORIENTATION.RIGHT, 0.4f, 3f)
                        cube[0] -= 0.1125f
                        cube[2] -= 0.1125f
                        cube[4] -= 0.1125f
                        cube[6] -= 0.1125f
                    } else {
                        cube = it.adjustScalingWithoutSettingCube(AnimateInfo.ORIENTATION.RIGHT, 0.4f, 2f)
                        cube[0] -= 0.125f
                        cube[2] -= 0.125f
                        cube[4] -= 0.125f
                        cube[6] -= 0.125f
                    }
                    it.adjustScalingByCube(cube)
                }
            }
        )
        return true
    }

    override fun createWidgetByIndex(index: Int) =  null

    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample?) {}

    override fun createAnimateEvaluate(index: Int, duration: Long): ArrayList<BaseEvaluate> {
        val list = ArrayList<BaseEvaluate>()
        list.add(
            AnimationBuilder(
                duration.toInt(),
                width,
                height,
                true
            ).build()
        )
        return list

    }

    override fun createActions() = 9


    override fun computeThemeCycleTime() = 1000
}