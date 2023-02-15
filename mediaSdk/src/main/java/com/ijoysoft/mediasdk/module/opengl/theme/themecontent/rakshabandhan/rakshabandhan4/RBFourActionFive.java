package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan4;

import com.ijoysoft.mediasdk.module.opengl.particle.RotateImageDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 兄妹节2
 */
public class RBFourActionFive extends BaseBlurThemeTemplate {


    public RBFourActionFive(int totalTime, int width, int height) {
        super(totalTime, width, height, true);
        notCoverWidgetsStartIndex = 1;
    }

    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }

    @Override
    protected BaseEvaluate initEnterAnimation() {
        return new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT).setScale(1.1f, 1.1f));
    }

    @Override
    protected BaseEvaluate initOutAnimation() {
        return new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, -2f, 0f).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT).setScale(1.1f, 1.1f));
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
        //环境光
        widgets[1] = buildNewScaleInOutTemplateAnimate(totalTime, -1f, -1f);


    }

    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[][]{
                    //边框
                    {0f, -0.299f, 1f, 1f},
                    //环境光
                    {0f, -0.556f, 1f, 2f},
            };
        }
        return widgetMeta;
    }

    final float[][][] rotateWidgetsMeta = new float[][][]{{
        //右下角大圆
        {-0.5f, 0.71875f, 0f, 0.6f},
            //左上角从大到小四个园
            {0.6f, -0.7f, 0f, 0.4f},
            {0.17f, -0.87f, 0f, 0.2f},
            {-0.1f, -0.9f, 0f, 0.1f}
    }};


    @Override
    protected float[][][] getRotateWidgetsMeta() {
        return rotateWidgetsMeta;
    }


    @Override
    public void drawWiget() {
        widgets[0].drawFrame();
        for (RotateImageDrawer rotateWidget : rotateWidgets) {
            rotateWidget.onDrawFrame();
        }
        widgets[1].drawFrame();
    }


}
