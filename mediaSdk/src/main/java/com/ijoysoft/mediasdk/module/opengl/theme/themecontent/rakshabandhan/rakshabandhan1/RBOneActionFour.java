package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan1;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 兄妹节4
 */
public class RBOneActionFour extends BaseBlurThemeTemplate {


    public RBOneActionFour(int totalTime, int width, int height) {
        super(totalTime, width, height, true);
        notCoverWidgetsStartIndex = 2;
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
        return new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 2f, 0f).
                setOrientation(AnimateInfo.ORIENTATION.LEFT).setScale(1.1f, 1.1f));
    }

    @Override
    protected float initZView() {
        return 0;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        widgets[0] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.TOP, AnimateInfo.ORIENTATION.TOP);
        //two
        widgets[1] = buildNewFadeInOutTemplateAnimate(totalTime);

    }

    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    {0f, -0.5f, 1f, 0.6667f},
                    {0f, 0f, 1f, 1f}
//                    {0.4f, 0.8f, 1.7f, 1.7f}
            };
        }
        return widgetsMeta;
    }

    //0.538 = 340/632    720p下 Min wh = 632
    float[][][] rotateWidgetsMeta = new float[][][]{{{0.6f, -0.65f, 0f, 0.538f}}};

    @Override
    protected float[][][] getRotateWidgetsMeta() {
        return rotateWidgetsMeta;
    }


}
