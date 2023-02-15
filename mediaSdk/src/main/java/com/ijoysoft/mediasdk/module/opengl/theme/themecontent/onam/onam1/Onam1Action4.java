package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam1;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 欧南节1
 */
public class Onam1Action4 extends BaseBlurThemeTemplate {


    public Onam1Action4(int totalTime, int width, int height) {
        super(totalTime, width, height, true,  true);
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
                    {0f, -0.5773f, 1f, 2f},
                    //底部tag
                    {-0.6f, 0.75f, 4f, 4f},
                    //顶部文字，中心变大
                    {0.6f, 0.75f, 4f, 4f},
            };
        }
        return widgetMeta;
    }


    float[][][] rotateWidgetMeta = new float[][][]{
            {{0f, 0.88f, 0f, 0.12f}},
            {{-0.2f, 0.77f, 0f, 0.09f}},
            {{0f, 0.75f, 0f, 0.07f}},
            {{0.2f, 0.77f, 0f, 0.09f}},
    };

    @Override
    protected float[][][] getRotateWidgetsMeta() {
        return rotateWidgetMeta;
    }




}
