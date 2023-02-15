package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday33

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation

class HD33Action(totalTime: Int, width: Int, height: Int): BaseBlurThemeTemplate(totalTime,
    DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true) {

    init {
        this.width = width
        this.height = height
        stayAction = initStayAction()
        enterAnimation = initEnterAnimation()
        setZView(initZView())
        paddingCount = 1
    }


    override fun initStayAction() = StayScaleAnimation(2500, false, 1f, 0.2f, 1.2f)

    override fun initEnterAnimation() = AnimationBuilder(BaseBlurThemeExample.DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f)
            .setIsNoZaxis(true).build()


    override fun initWidget() {
        super.initWidget()
        widgets[0] = buildNoneAnimationWidget()
    }


    override fun getWidgetsMeta(): Array<FloatArray> {
        return arrayOf(floatArrayOf(0f, 0f, 1f, 1f, 1f))
    }


}