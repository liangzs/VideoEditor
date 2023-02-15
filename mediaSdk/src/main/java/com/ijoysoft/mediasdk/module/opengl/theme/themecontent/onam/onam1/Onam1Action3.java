package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam1;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 欧南节1
 */
public class Onam1Action3 extends BaseBlurThemeTemplate {


    public Onam1Action3(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
        notCoverWidgetsStartIndex = 1;
    }

    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }



    @Override
    public void initWidget() {
        super.initWidget();
        //边框
        widgets[0] = buildNewFadeInOutTemplateAnimate(totalTime);
    }

    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    {0f, 0f, 1f, 1f},
            };
        }
        return widgetsMeta;
    }

    float[][][] rotateWidgetMeta = new float[][][]{{
            {-1f, -1f, 0f, 0.7f},
            {1f, -1f, 0f, 0.7f},
            {-1f, 1f, 0f, 0.7f},
            {1f, 1f, 0f, 0.7f},
    }};

    @Override
    protected float[][][] getRotateWidgetsMeta() {
        return rotateWidgetMeta;
    }


}
