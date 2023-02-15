package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan5;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 兄妹节5
 */
public class Rakshabandhan5Action4 extends BaseBlurThemeTemplate {


    public Rakshabandhan5Action4(int totalTime, int width, int height) {
        super(totalTime, width, height, true,  true);
    }

    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }



    @Override
    protected float initZView() {
        return 0;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        //顶部tag， 中心变大
        widgets[0] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[0][0], getWidgetsMeta()[0][1]);
        //底部tag，中心变大
        widgets[1] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[1][0], getWidgetsMeta()[1][1]);
        //顶部文字，中心变大
        widgets[2] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[2][0], getWidgetsMeta()[2][1]);
    }

    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[][]{
                    //顶部tag， 中心变大
                    {0f, -0.6867f, 1f, 2f},
                    //底部tag
                    {0f, 0.6867f, 1f, 2f},
                    //顶部文字，中心变大
                    {0f, -0.8f, 1.8f, 3.6f},
            };
        }
        return widgetMeta;
    }


    float[][][] rotateWidgetMeta = new float[][][]{{
            {0f, 0.8f, 0f, 0.333f},
            {-0.65f, 0.85f, 0f, 0.12f},
            {0.65f, 0.85f, 0f, 0.12f},
    }};

    @Override
    protected float[][][] getRotateWidgetsMeta() {
        return rotateWidgetMeta;
    }




}
