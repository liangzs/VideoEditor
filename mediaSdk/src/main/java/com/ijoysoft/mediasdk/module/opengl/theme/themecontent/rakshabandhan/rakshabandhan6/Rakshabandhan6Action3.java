package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan6;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 兄妹节6
 */
public class Rakshabandhan6Action3 extends BaseBlurThemeTemplate {


    public Rakshabandhan6Action3(int totalTime, int width, int height) {
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
    protected float initZView() {
        return 0;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        //边框
        widgets[0] = buildNewFadeInOutTemplateAnimate(totalTime);
        //组合控件
        widgets[1] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[1][0], getWidgetsMeta()[1][1]);

    }

    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //边框
                    {0f, 0f, 1f, 1f},
                    //组合控件
                    {0f, 0f, 1f, 1f},
            };
        }
        return widgetsMeta;
    }




}
