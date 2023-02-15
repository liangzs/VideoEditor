package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan3;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 兄妹节3
 */
public class Rakshabandhan3Action1 extends BaseBlurThemeTemplate {

    public Rakshabandhan3Action1(int totalTime, int width, int height) {
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
        //底部边框
        widgets[0] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.BOTTOM, AnimateInfo.ORIENTATION.BOTTOM);
        //挂件6
        widgets[1] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.TOP, AnimateInfo.ORIENTATION.TOP);
        //挂件挂着的花
        widgets[2] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.TOP, AnimateInfo.ORIENTATION.TOP);

    }



    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //底部边框
                    {0f, 0.7625f, 1f, 1f},
                    //挂件6
                    {-0.7f, -0.4f, 1.5f, 1.5f},
                    //挂件挂着的花
                    {-0.7f, 0.25f, 6f, 12f}
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
                {0.8f, -0.8f, 0f, 0.2f}, {0.8f, 0.2f, 0f, 0.1f},
            },
            {
                {-0.5f, -0.8f, 0f, 0.1f},
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
