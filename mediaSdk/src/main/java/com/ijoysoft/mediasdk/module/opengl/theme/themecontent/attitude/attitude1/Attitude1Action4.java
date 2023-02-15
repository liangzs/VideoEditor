package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude1;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 态度1
 */
public class Attitude1Action4 extends BaseBlurThemeTemplate {



    public Attitude1Action4(int totalTime, int width, int height) {
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
        //背景模糊效果
        widgets[0] = buildNewFadeInOutTemplateAnimate(totalTime);
        //黄色效果
        widgets[1] = buildNewScaleInOutTemplateAnimateAtSelfCenter(1);
        //标题
        widgets[2] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.LEFT, AnimateInfo.ORIENTATION.RIGHT);
    }

    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //背景模糊效果
                    {0f, 0.42f, 1f, 1f, 1f},
                    //黄色效果
                    {-0.85f, -0.5f, 0.5f, 0.5f, 0.5f},
                    //标题
                    {0f, Float.MAX_VALUE, 1.2f, 2.4f, 1.2f},
            };
        }
        return widgetsMeta;
    }


}
