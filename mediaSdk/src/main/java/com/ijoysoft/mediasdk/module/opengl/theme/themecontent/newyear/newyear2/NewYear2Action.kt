package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.newyear.newyear2

import androidx.annotation.Keep
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.BaseThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation

class NewYear2Action: BaseBlurThemeTemplate {




    @Keep
    constructor(totalTime: Int, width: Int, height: Int) : super(totalTime, width, height, true, true)


    init {
//        this.width = width;
//        this.height = height;
//        stayAction = initStayAction();
//        enterAnimation = initEnterAnimation();
//        outAnimation = initOutAnimation();
//
//        setZView(initZView())
        setCoverWidgetsCount(1, 1)
    }



    override fun initStayAction(): BaseEvaluate {
        var res = StayScaleAnimation(DEFAULT_STAY_TIME, false, 1f, 0.1f, 1.1f)
        res.setZView(0F)
        return res
    }

    override fun initWidget() {
        super.initWidget()
        widgets[0] = buildNoneAnimationWidget()
    }


    var widgetsMetas: Array<FloatArray>? = null

    override fun getWidgetsMeta(): Array<FloatArray> {
        if (widgetsMetas == null) {
            widgetsMetas = arrayOf(
                floatArrayOf(0f, 0f, 1f, 1f, 1f),
            )
        }
        return widgetsMetas!!
    }



}