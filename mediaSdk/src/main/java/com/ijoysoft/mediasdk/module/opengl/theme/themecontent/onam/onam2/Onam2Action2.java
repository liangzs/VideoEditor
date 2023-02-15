package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam2;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 欧南节2
 */
public class Onam2Action2 extends BaseBlurThemeTemplate {



    public Onam2Action2(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
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
        //标题
        widgets[0] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[0][0], getWidgetsMeta()[0][1]);
        //日
        widgets[1] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[1][0], getWidgetsMeta()[1][1]);
    }

    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[][]{
                    //标题
                    {0f, 0.8f, 2f, 2f},
                    //日
                    {-0.6f, -0.75f, 3f, 3f},
            };
        }
        return widgetMeta;
    }




}
