package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude8;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 态度8
 */
public class Attitude8Action3 extends BaseBlurThemeTemplate {


    public Attitude8Action3(int totalTime, int width, int height) {
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
        widgets[1] = buildNewScaleInOutTemplateAnimateAtSelfCenter(1);
        widgets[2] = buildNewScaleInOutTemplateAnimateAtSelfCenter(2);
        widgets[3] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);
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
                    //光晕效果
                    {-0.6f, -0.6f, 0.5f, 0.5f, 0.5f},
                    //黄色效果
                    {-0.1f, 0.75f, 0.3f, 0.3f, 0.3f},
                    //标题
                    {0f, 0.65f, 1.2f, 2.4f, 2f}
            };
        }
        if (width < height) {
            widgetsMeta[3][0] = -0.1f;
            widgetsMeta[3][1] = 0.75f;
        } else if (height < width) {
            widgetsMeta[3][0] = 0f;
            widgetsMeta[3][1] = 0.6f;
        } else {
            widgetsMeta[3][0] = 0f;
            widgetsMeta[3][1] = 0.5f;
        }
        return widgetsMeta;
    }

}
