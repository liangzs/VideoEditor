package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam4;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 欧南节4
 */
public class Onam4Action3 extends BaseBlurThemeTemplate {


    public Onam4Action3(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
    }

    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }




    float[][][] rotateWidgetMeta = new float[][][]{
            //红色
            {
                    {-0.75f, -0.875f, 0f, 0.23f},
                    {0.75f, -0.875f, 0f, 0.23f},
                    {-0.75f, 0.875f, 0f, 0.23f},
                    {0.75f, 0.875f, 0f, 0.23f},
            },
            //紫色
            {

                    {0f, 0.8f, 0f, 0.5f},
                    {0f, -0.92f, 0f, 0.18f},
            }
    };

    @Override
    protected float[][][] getRotateWidgetsMeta() {
        return rotateWidgetMeta;
    }



}
