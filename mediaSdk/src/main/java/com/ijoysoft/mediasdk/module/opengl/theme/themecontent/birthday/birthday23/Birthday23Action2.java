package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday23;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 生日23
 */
public class Birthday23Action2 extends BaseBlurThemeTemplate {




    public Birthday23Action2(int totalTime, int width, int height) {
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
        widgets[1] = buildNewScaleInOutTemplateAnimateAtSelfCenter(1);
    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //礼物
                    {Float.MAX_VALUE, Float.MIN_VALUE, 2f, 4f, 2f},
                    {Float.MIN_VALUE, Float.MAX_VALUE, 0.7f, 1.4f, 0.7f},
            };
        }
        return widgetsMeta;
    }

    @Override
    protected void fixNotCoverWidgetsPositionManually(int i) {
        if (i == 1) {
            widgets[1].setVertex(transitionRatioPosArray(widgets[1].getCube(), -0.2f, 0.2f));
        }
    }
}
