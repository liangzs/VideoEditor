package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan9;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 兄妹节9
 */
public class Rakshabandhan9Action1 extends BaseBlurThemeTemplate {

    public Rakshabandhan9Action1(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
        notCoverWidgetsStartIndex = 1;
        //旋转控件变大变快点
        rotateScaleSpeed = 10f;
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
        //绳子
        widgets[1] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[1][0], getWidgetsMeta()[1][1]);
        //花
//        widgets[2] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[2][0], getWidgetsMeta()[2][1]);

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
                    //绳子
                    {0f, 0.4f, 1f, 2f},
                    //花
//                    {0f, 0.4f, 2.5f, 5f},
            };
        }
        return widgetsMeta;
    }

    @Override
    protected float initZView() {
        return 0;
    }



    float[][][] rotateWidgetMeta = new float[][][]{
            {
                //花
                {0f, 0.4f, 0f, 0.45f},
            }
    };

    @Override
    protected float[][][] getRotateWidgetsMeta() {
        return rotateWidgetMeta;
    }



    @Override
    public void drawFramePreview() {
        super.drawFramePreview();
        for (BaseThemeExample widget : widgets) {
            widget.drawFramePreview();
        }
    }
}
