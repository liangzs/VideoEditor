package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday22;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 生日22
 */
public class Birthday22Action4 extends BaseBlurThemeTemplate {



    public Birthday22Action4(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
        setCoverWidgetsCount(1, 1);
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
        widgets[0] = buildNoneAnimationWidget();
        widgets[1] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.BOTTOM, AnimateInfo.ORIENTATION.TOP);
        widgets[2] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);
        widgets[3] = buildNewFadeInOutTemplateAnimate();
        widgets[4] = buildNewFadeInOutTemplateAnimate();


    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //边框
                    {0f, 0f, 1f, 1f, 1f},
                    //气球
                    {0f, Float.MAX_VALUE, 1.4f, 1f, 1f},
                    //蝴蝶
                    {0f, Float.MIN_VALUE, 3f, 4f, 4f},
                    //红色光效
                    {0.8f, 0.8f, 0.8f, 1.6f, 1.6f},
                    {0.6f, -0.9f, 1f, 2f, 2f},
            };
        }
        return widgetsMeta;
    }

    @Override
    protected void fixNotCoverWidgetsPositionManually(int i) {
        if (i == 1) {
            widgets[1].setVertex(transitionRatioPosArray(widgets[1].getCube(), 0f, 0.5652f));
        }
    }
}