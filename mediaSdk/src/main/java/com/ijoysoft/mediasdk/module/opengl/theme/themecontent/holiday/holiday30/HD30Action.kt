package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday30

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.module.opengl.particle.PAGNoBgParticle
import com.ijoysoft.mediasdk.module.opengl.theme.action.*
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation

class HD30Action(totalTime: Int, width: Int, height: Int) :
    BaseThemeTemplate(totalTime,
        ConstantMediaSize.TRANSITION_DURATION, totalTime - ConstantMediaSize.TRANSITION_DURATION, 0, true) {

    init {
        paddingCount = 1
        this.width = width
        this.height = height
        stayAction = initStayAction()
        enterAnimation = initEnterAnimation()
        setZView(initZView())
    }


    override fun initStayAction() = StayScaleAnimation(2500, false, 1f, 0.2f, 1.2f)

    override fun initEnterAnimation() = AnimationBuilder(BaseBlurThemeExample.DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f)
        .setIsNoZaxis(true).build()


    override fun initWidget() {
        super.initWidget()
        widgets[0] = buildNoneAnimationWidget()
        widgets[1] = buildNoneAnimationWidget()
    }


    override fun getWidgetsMeta(): Array<FloatArray> {
        return arrayOf(floatArrayOf(0f, 0f, 1f, 1f, 1f), floatArrayOf(0f, 0.5f, 1.8f, 3.6f, 4f))
    }

    override fun init(bitmap: Bitmap?, width: Int, height: Int) {
        super.init(bitmap, width, height)
        adjustImageScalingStretch(width, height)
    }
}