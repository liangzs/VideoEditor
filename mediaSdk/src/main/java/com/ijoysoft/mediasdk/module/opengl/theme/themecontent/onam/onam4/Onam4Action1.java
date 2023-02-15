package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam4;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 欧南节4
 */
public class Onam4Action1 extends BaseBlurThemeTemplate {

    public Onam4Action1(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
    }

    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }



    float[][][] rotateWidgetMeta = new float[][][]{
            //紫色
            {
                    {-5f/6f, -0.90625f, 0f, 1f/6f},
                    {-5f/6f, 0.90625f, 0f, 1f/6f},
                    {-1f/6f, -0.90625f, 0f, 1f/6f},
                    {-1f/6f, 0.90625f, 0f, 1f/6f},
                    {3f/6f, -0.90625f, 0f, 1f/6f},
                    {3f/6f, 0.90625f, 0f, 1f/6f},
            },
            //橘色
            {
                    {1f/6f, -0.90625f, 0f, 1f/6f},
                    {1f/6f, 0.90625f, 0f, 1f/6f},
            },
            //红色
            {

                    {5f/6f, -0.90625f, 0f, 1f/6f},
                    {5f/6f, 0.90625f, 0f, 1f/6f},
                    {-3f/6f, -0.90625f, 0f, 1f/6f},
                    {-3f/6f, 0.90625f, 0f, 1f/6f},
            }
    };

    @Override
    protected float[][][] getRotateWidgetsMeta() {
        return rotateWidgetMeta;
    }

}
