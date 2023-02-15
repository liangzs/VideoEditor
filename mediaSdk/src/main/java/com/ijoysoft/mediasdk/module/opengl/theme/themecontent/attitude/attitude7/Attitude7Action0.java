package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude7;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 态度7
 */
public class Attitude7Action0 extends BaseBlurThemeTemplate {


    public Attitude7Action0(int totalTime, int width, int height) {
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
        widgets[0] = buildNewFadeInOutTemplateAnimate();
        widgets[1] = buildNewFadeInOutTemplateAnimate();
        widgets[2] = buildNewScaleInOutTemplateAnimate(1f, 0.5f);
        widgets[3] = buildNewScaleInOutTemplateAnimate(-1f, 1f);
    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //青色效果
                    {-0.8f, -0.8f, 0.3f, 0.3f, 0.6f},
                    //黄色效果
                    {-0.8f, 0.8f, 0.3f, 0.3f, 0.6f},
                    //yaar
                    {Float.MAX_VALUE, 0.2f, 2f, 4f, 2f},
                    //hearts
                    {Float.MIN_VALUE, Float.MAX_VALUE, 0.6667f, 1.333f, 0.6667f}
            };
        }
        return widgetsMeta;
    }




}
