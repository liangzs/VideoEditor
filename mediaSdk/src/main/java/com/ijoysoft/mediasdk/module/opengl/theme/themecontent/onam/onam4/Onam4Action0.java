package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam4;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 欧南节4
 */
public class Onam4Action0 extends BaseBlurThemeTemplate {


    public Onam4Action0(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
    }


    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }


    float[][][] rotateWidgetMeta = new float[][][]{
            {
                    {0f, -1f, 0f, 0.7f},
                    {0f, 1f, 0f, 0.7f},
            }
    };

    @Override
    protected float[][][] getRotateWidgetsMeta() {
        return rotateWidgetMeta;
    }



}
