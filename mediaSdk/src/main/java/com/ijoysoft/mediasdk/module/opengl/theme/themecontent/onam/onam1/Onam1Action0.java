package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam1;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 欧南节1
 */
public class Onam1Action0 extends BaseBlurThemeTemplate {


    public Onam1Action0(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
        rotateScaleSpeed = 9f;
    }




    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }




    float[][][] rotateWidgetMeta = new float[][][]{{
            {-1f, -1f, 0f, 0.7f},
            {1f, -1f, 0f, 0.7f},
            {-1f, 1f, 0f, 0.7f},
            {1f, 1f, 0f, 0.7f},
            {0f, -0.85f, 0f, 0.7f},
            {0f, 0.85f, 0f, 0.7f},
    }};

    @Override
    protected float[][][] getRotateWidgetsMeta() {
        return rotateWidgetMeta;
    }



}
