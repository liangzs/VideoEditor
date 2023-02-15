package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan6;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 兄妹节6
 */
public class Rakshabandhan6Action1 extends BaseBlurThemeTemplate {

    public Rakshabandhan6Action1(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
        notCoverWidgetsStartIndex = 1;
    }

    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }





    /**
     * 动画设置
     */
    @Override
    public void initWidget() {
        super.initWidget();
        //边框
        widgets[0] = buildNewFadeInOutTemplateAnimate(totalTime);
        //烛灯,从中心出现
        widgets[1] = buildNewScaleInOutTemplateAnimate(totalTime, 0f, 0f);
        widgets[2] = buildNewScaleInOutTemplateAnimate(totalTime, 0f, 0f);
        widgets[3] = buildNewScaleInOutTemplateAnimate(totalTime, 0f, 0f);

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
                    {0f, 0f, 1f, 1f},
                    //烛灯
                    {-0.5f, 0.5f, 2.5f, 5f},
                    {0.5f, 0.5f, 2.5f, 5f},
                    {0f, 0.65f, 1.25f, 2.5f}
            };
        }
        return widgetsMeta;
    }

    @Override
    protected float initZView() {
        return 0;
    }




    @Override
    public void drawFramePreview() {
        super.drawFramePreview();
        for (BaseThemeExample widget : widgets) {
            widget.drawFramePreview();
        }
    }
}
