package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude7;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 态度5
 */
public class Attitude7Action4 extends BaseBlurThemeTemplate {



    public Attitude7Action4(int totalTime, int width, int height) {
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
        widgets[2] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);
        widgets[3] = buildNewScaleInOutTemplateAnimate(1f, 1f);

    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //粉色效果
                    {-0.75f, -0.5f, 0.3f, 0.3f, 0.6f},
                    //橘色效果
                    {0.75f, 0.5f, 0.3f, 0.3f, 0.6f},
                    //emojis
                    {0f, Float.MIN_VALUE, 1f, 2f, 1f},
                    //ROCK
                    {Float.MAX_VALUE, Float.MAX_VALUE, 1f, 2f, 1f}
            };
        }
        return widgetsMeta;
    }

}
