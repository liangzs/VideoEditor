package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily1

import androidx.annotation.Keep
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeTemplate
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.friend.friend4.Friend4Action
import com.ijoysoft.mediasdk.module.playControl.GifDecoder

class LargerAction: BaseThemeTemplate {



    @Keep
    constructor(totalTime: Int, width: Int, height: Int) : super(totalTime, totalTime, 40, 40, false) {
        this.width = width
        this.height = height
        stayAction = initStayAction()
        enterAnimation = initEnterAnimation()
        outAnimation = initOutAnimation()
        setZView(initZView())
    }


    override fun initEnterAnimation() = AnimationBuilder(totalTime).setScale(1f, 1.4f).build()

    override fun initStayAction() = AnimationBuilder(40).setScale(1.4f, 1.4f).build()

    override fun initOutAnimation() = AnimationBuilder(40).setScale(1.4f, 1.4f).build()

    override fun adjustImageScaling(width: Int, height: Int) {
        adjustImageScalingStretch(width, height)
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