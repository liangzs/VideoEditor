package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan1;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 兄妹节3
 */
public class RBOneActionThree extends BaseBlurThemeTemplate {



    public RBOneActionThree(int totalTime, int width, int height) {
        super(totalTime, width, height, true);
        notCoverWidgetsStartIndex = 1;
    }

    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
//        return new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
    }

    @Override
    protected BaseEvaluate initEnterAnimation() {
//        return new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(0.3f,1.0f).
//                setCoordinate(0, -2.5f, 0, 0).
//                setConors(AnimateInfo.ROTATION.NONE, 0, 0).
//                setZView(0).setScale(1.2f, 1.2f).build();\
        return new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT).setScale(1.1f, 1.1f));
    }

    @Override
    protected BaseEvaluate initOutAnimation() {
//        return new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 0, 2.5f).
//                setConors(AnimateInfo.ROTATION.NONE, 0, 0).
//                setZView(0).build();
        return new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, -2f, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT).setScale(1.1f, 1.1f));
    }

    @Override
    protected float initZView() {
        return 0;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        widgets[0] = buildNewFadeInOutTemplateAnimate(totalTime);
        widgets[1] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.TOP, AnimateInfo.ORIENTATION.BOTTOM);




    }

    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[][]{
//                    {0f, 0f, 1f, 1f},
                    {0f, 0f, 1f, 1f},
                    {0f, -0.75f, 1.6f, 3.6f}
//                    {-0.75f, 0.8f, 4.2f, 4.2f},
//                    {-0.45f, 0.63f, 1.7f, 1.7f}
            };
        }
        return widgetMeta;
    }

    //0.411 = 260/632    720p下 Min wh = 632
    final float[][][] rotateWidgetMeta = new float[][][]{{{-0.4f, 0.42f, 0f, 0.411f}}};

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
