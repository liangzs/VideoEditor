package com.ijoysoft.mediasdk.module.opengl.theme.action;

import androidx.annotation.Keep;

import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * @author hayring
 * @date 2021/12/16  19:49
 */
@Keep
public class EmptyBlurAction extends BaseBlurThemeTemplate {


    public EmptyBlurAction(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
    }

    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }
}
