package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily1

import androidx.annotation.Keep
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeTemplate
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder
import com.ijoysoft.mediasdk.module.playControl.GifDecoder

class SmallerAction: BaseThemeTemplate {

    @Keep
    constructor(totalTime: Int, width: Int, height: Int) : super(totalTime, totalTime, 40, 40, false) {
        this.width = width
        this.height = height
        stayAction = initStayAction()
        enterAnimation = initEnterAnimation()
        outAnimation = initOutAnimation()
        setZView(initZView())
    }

    override fun initEnterAnimation() = AnimationBuilder(totalTime).setScale(1.4f, 1f).build()



    override fun initStayAction() = AnimationBuilder(40).setScale(1f, 1f).build()

    override fun initOutAnimation() = AnimationBuilder(40).setScale(1f, 1f).build()

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
        val scale = if (width < height) 3f else 1.5f
        gifDrawer1?.adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, scale)
        gifDrawer2?.adjustScaling(width, height, AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, scale)
    }

    override fun onSizeChanged(width: Int, height: Int) {
        super.onSizeChanged(width, height)
        val scale = if (width < height) 3f else 1.5f
        gifDrawer1?.adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, scale)
        gifDrawer2?.adjustScaling(width, height, AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, scale)
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