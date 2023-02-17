package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude5;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 态度5
 */
public class Attitude5Action2 extends BaseBlurThemeTemplate {



    public Attitude5Action2(int totalTime, int width, int height) {
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
        widgets[0] = buildNewScaleInOutTemplateAnimateAtSelfCenter(0);
        widgets[1] = buildNewFadeInOutTemplateAnimate();
    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //标题
                    {0f, Float.MAX_VALUE, 1f, 2f, 1f},
                    //emoji
                    {Float.MIN_VALUE, Float.MIN_VALUE, 2.5f, 2.5f, 2.5f}
            };
        }
        return widgetsMeta;
    }

    @Override
    protected void fixNotCoverWidgetsPositionManually(int i) {
        if (i == 1) {
            widgets[1].setVertex(transitionRatioPosArray(widgets[1].getCube(), 0.3f, 0.3f));
        }
    }

}