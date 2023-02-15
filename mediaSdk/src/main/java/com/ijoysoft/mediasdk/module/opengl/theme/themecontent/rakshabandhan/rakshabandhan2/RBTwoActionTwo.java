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
public class RBTwoActionTwo extends BaseBlurThemeTemplate {

    public RBTwoActionTwo(int totalTime, int width, int height) {
        super(totalTime, width, height, true);
    }

    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }

    @Override
    protected BaseEvaluate initEnterAnimation() {
        return new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2.5f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT).setScale(1.1f, 1.1f));
    }

    @Override
    protected BaseEvaluate initOutAnimation() {
        return new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, -2.5f, 0f).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT).setScale(1.1f, 1.1f));

    }



    /**
     * 动画设置
     */
    @Override
    public void initWidget() {
        super.initWidget();
        for (int i = 0; i < 10; i++) {
            widgets[i] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[i][0], getWidgetsMeta()[i][1]);
        }

    }



    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    {-0.8f, -1.02f, 5f, 2.5f},
                    {0.8f, -1.02f, 5f, 2.5f},
                    {-0.8f, 1.02f, 5f, 2.5f},
                    {0.8f, 1.02f, 5f, 2.5f},
                    {-0.45f, -0.95f, 5f, 2.5f},
                    {0.45f, -0.95f, 5f, 2.5f},
                    {-0.45f, 0.95f, 5f, 2.5f},
                    {0.45f, 0.95f, 5f, 2.5f},
                    {0f, -0.92f, 3.33f, 1.667f},
                    {0f, 0.92f, 3.33f, 1.667f}

            };
        }
        return widgetsMeta;
    }

    @Override
    protected float initZView() {
        return 0;
    }



//    float[][][] rotateWidgetMeta = new float[][][]{
//            {
//                {-0.8f, -1.02f, 0f, 0.2f},
//                    {0.8f, -1.02f, 0f, 0.2f},
//                    {-0.8f, 1.02f, 0f, 0.2f},
//                    {0.8f, 1.02f, 0f, 0.2f},
//                    {-0.45f, -0.95f, 0f, 0.2f},
//                    {0.45f, -0.95f, 0f, 0.2f},
//                    {-0.45f, 0.95f, 0f, 0.2f},
//                    {0.45f, 0.95f, 0f, 0.2f},
//            },
//            {
//                {0f, -0.92f, 0f, 0.3f},
//                    {0f, 0.92f, 0f, 0.3f}
//            }
//    };
//
//    @Override
//    protected float[][][] getRotateWidgetsMeta() {
//        return rotateWidgetMeta;
//    }



    @Override
    public void drawFramePreview() {
        super.drawFramePreview();
        for (BaseThemeExample widget : widgets) {
            widget.drawFramePreview();
        }
    }
}
