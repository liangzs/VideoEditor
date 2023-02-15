package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude8;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 态度8
 */
public class Attitude8Action0 extends BaseBlurThemeTemplate {


    public Attitude8Action0(int totalTime, int width, int height) {
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
        widgets[1] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.LEFT, AnimateInfo.ORIENTATION.RIGHT);
        widgets[2] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);
        widgets[3] = buildNewScaleInOutTemplateAnimateAtSelfCenter(3);
        widgets[4] = buildNewScaleInOutTemplateAnimateAtSelfCenter(4);
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
                    //灯
                    {-0.5f, Float.MIN_VALUE, 3.6f, 2.4f, 3.6f},
                    {0.5f, Float.MIN_VALUE, 3.6f, 2.4f, 3.6f},
                    //光晕效果
                    {-0.5f, 0.5f, 0.5f, 0.5f, 0.5f},
                    //青色效果
                    {1f, -1f, 0.3f, 0.3f, 0.3f},
            };
        }
        return widgetsMeta;
    }

    @Override
    protected void drawWidgets() {
        widgets[1].drawFrame();
        widgets[2].drawFrame();
        widgets[3].drawFrame();
        widgets[4].drawFrame();
        widgets[0].drawFrame();
    }
}
