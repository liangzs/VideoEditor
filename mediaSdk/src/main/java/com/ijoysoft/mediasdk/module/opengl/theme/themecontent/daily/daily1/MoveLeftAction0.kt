package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily1

import androidx.annotation.Keep
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeTemplate
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder
import com.ijoysoft.mediasdk.module.playControl.GifDecoder

class MoveLeftAction0: BaseThemeTemplate {

    @Keep
    constructor(totalTime: Int, width: Int, height: Int) : super(totalTime, 1200, totalTime - 1200, 40, false) {
        this.width = width
        this.height = height
        stayAction = initStayAction()
        enterAnimation = initEnterAnimation()
        outAnimation = initOutAnimation()
        setZView(initZView())
    }


    override fun initEnterAnimation() = AnimationBuilder(1200).setStartX(0.1f).setEndX(0.1f).build()



    override fun initStayAction() = AnimationBuilder(totalTime - 1200).setStartX(0.1f).setEndX(-0.5f).build()

    override fun initOutAnimation() = AnimationBuilder(40).setStartX(0f).setEndX(0f).build()

    override fun adjustImageScaling(width: Int, height: Int) {
        if (bitmap == null) {
            return
        }
        var framewidth = bitmap.width
        var frameheight = bitmap.height
        val rotation = bitmapAngle % 360
        if (rotation == 90 || rotation == 270) {
            framewidth = bitmap.height
            frameheight = bitmap.width
        }
        val ratio1 = 1f * width / (1f * framewidth)
        val ratio2 = 1f * height / (1f * frameheight)
        val ratioMax = Math.max(ratio1, ratio2)
        // 居中后图片显示的大小
        // 居中后图片显示的大小

        //与super不同之处，在这里 * 2

        val imageWidthNew = Math.round(framewidth * ratioMax) * 2
        val imageHeightNew = Math.round(frameheight * ratioMax) * 2
        //频繁打印的日志放至-verbose级别中
        //频繁打印的日志放至-verbose级别中
        LogUtils.v(
            TAG, "width:" + width + ",height:" + height + ",imageWidthNew:"
                    + imageWidthNew + ",imageHeightNew:" + imageHeightNew
        )
        // 图片被拉伸的比例,以及显示比例时的偏移量
        // 图片被拉伸的比例,以及显示比例时的偏移量
        val ratioWidth = 1f * width / (1f * imageWidthNew)
        val ratioHeight = 1f * height / (1f * imageHeightNew)
        // 根据拉伸比例还原顶点
        // 根据拉伸比例还原顶点
        cube = floatArrayOf(
            pos[0] / ratioWidth, pos[1] / ratioHeight, pos[2] / ratioWidth, pos[3] / ratioHeight,
            pos[4] / ratioWidth, pos[5] / ratioHeight, pos[6] / ratioWidth, pos[7] / ratioHeight
        )
        mVerBuffer.clear()
        mVerBuffer.put(cube).position(0)

        setRotation(0)
    }

    var gifDrawer1: GifOriginFilter? = null
    var gifDrawer2: GifOriginFilter? = null

    override fun initGif(
        gifs: MutableList<MutableList<GifDecoder.GifFrame>>?,
        width: Int,
        height: Int
    ) {
        gifDrawer1 = GifOriginFilter()
        gifDrawer1!!.onCreate()
        gifDrawer1!!.setFrames(gifs!![0])

        gifDrawer2 = GifOriginFilter()
        gifDrawer2!!.onCreate()
        gifDrawer2!!.setFrames(gifs!![1])
        gifDrawer1?.adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, if (width < height) 1f else 1.5f)
        gifDrawer2?.adjustScalingWithoutSettingCube(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, if (width <= height) 2f else 3f)?.let { cb ->
            cb[1] -= 0.1f
            cb[3] -= 0.1f
            cb[5] -= 0.1f
            cb[7] -= 0.1f
            gifDrawer2!!.adjustScalingByCube(cb)
        }
    }

    override fun onSizeChanged(width: Int, height: Int) {
        super.onSizeChanged(width, height)
        gifDrawer1?.adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, if (width < height) 1f else 1.5f)
        gifDrawer2?.adjustScalingWithoutSettingCube(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, if (width <= height) 2f else 3f)?.let { cb ->
            cb[1] -= 0.1f
            cb[3] -= 0.1f
            cb[5] -= 0.1f
            cb[7] -= 0.1f
            gifDrawer2!!.adjustScalingByCube(cb)
        }
    }


    override fun onDraw() {
        super.onDraw()
        gifDrawer1?.draw()
        gifDrawer2?.draw()
    }

    override fun onDestroy() {
        super.onDestroy()
        gifDrawer1?.onDestroy()
        gifDrawer2?.onDestroy()
    }
}