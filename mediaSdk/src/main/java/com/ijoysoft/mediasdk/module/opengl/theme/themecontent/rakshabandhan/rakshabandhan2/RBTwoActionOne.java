package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan2;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 兄妹节2
 */
public class RBTwoActionOne extends BaseBlurThemeTemplate {


    public RBTwoActionOne(int totalTime, int width, int height) {
        super(totalTime, width, height, true);
        notCoverWidgetsStartIndex = 1;
    }


    /**
     * 动画设置
     */
    @Override
    public void initWidget() {
        super.initWidget();
        widgets[0] = buildNewFadeInOutTemplateAnimate(totalTime);
        widgets[1] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.BOTTOM);
        widgets[2] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[2][0], getWidgetsMeta()[2][1]);
        widgets[3] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[3][0], getWidgetsMeta()[3][1]);
        widgets[4] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[4][0], getWidgetsMeta()[4][1]);


    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    {0f, 0f, 1f, 1f},
                    {0.5f, 0.7f, 2f, 2f},
                    {-1f, 0.2f, 2.5f, 2.5f},
                    {-0.65f, -0.92f, 1.7f, 1.7f},
                    {0.85f, -0.85f, 2.5f, 2.5f}
            };
        }
        return widgetsMeta;
    }

    /**
     * 旋转控件元数据
     */
    final float[][][] rotateWidgetsMeta = new float[][][]{{
            {0.55f, 0.75f, 0f, 0.3f},
    }};

    @Override
    protected float[][][] getRotateWidgetsMeta() {
        return rotateWidgetsMeta;
    }


    @Override
    public void drawFramePreview() {
        super.drawFramePreview();
        for (BaseThemeExample widget : widgets) {
            widget.drawFramePreview();
        }
    }





    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }

    @Override
    protected BaseEvaluate initEnterAnimation() {
        return new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -2.5f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.TOP).setScale(1.1f, 1.1f));
    }

    @Override
    protected BaseEvaluate initOutAnimation() {
        return new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0, -2.5f).
                setOrientation(AnimateInfo.ORIENTATION.TOP).setScale(1.1f, 1.1f));
    }

    /**
     * z轴摄影机位置
     * @return 0
     */
    @Override
    protected float initZView() {
        return 0;
    }


}
