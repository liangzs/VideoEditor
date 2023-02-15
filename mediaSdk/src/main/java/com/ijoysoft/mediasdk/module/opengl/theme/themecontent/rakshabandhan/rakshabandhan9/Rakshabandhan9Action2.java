package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan9;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 兄妹节9
 */
public class Rakshabandhan9Action2 extends BaseBlurThemeTemplate {



    public Rakshabandhan9Action2(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
        notCoverWidgetsStartIndex = 1;
    }

    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }



    @Override
    protected float initZView() {
        return 0;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        //边框
        widgets[0] = buildNewFadeInOutTemplateAnimate(totalTime);
        //绳子
        widgets[1] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.RIGHT);
        //标题
        widgets[2] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[2][0], getWidgetsMeta()[2][1]);

    }

    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[][]{
                    //边框
                    {0f, 0f, 1f, 1f},
                    //绳子
                    {0.7f, 0.57f, 2.4f, 4.8f},
                    //标题
                    {0f, 0.2f, 1.4f, 2.8f},
            };
        }
        return widgetMeta;
    }




    @Override
    public void drawFramePreview() {
        super.drawFramePreview();
        for (BaseThemeExample widget : widgets) {
            widget.drawFramePreview();
        }
    }
}
