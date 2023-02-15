package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan4;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 兄妹节4
 */
public class RBFourActionOne extends BaseBlurThemeTemplate {


    public RBFourActionOne(int totalTime, int width, int height) {
        super(totalTime, width, height, true);
        notCoverWidgetsStartIndex = 1;
    }


    /**
     * 动画设置
     */
    @Override
    public void initWidget() {
        super.initWidget();
        //边框
        widgets[0] = buildNewFadeInOutTemplateAnimate(totalTime);
        //环境光
        widgets[1] = buildNewScaleInOutTemplateAnimate(totalTime, -1f, -1f);

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
                    //环境光
                    {0f, -0.556f, 1f, 2f},
            };
        }
        return widgetsMeta;
    }

    /**
     * 旋转控件元数据
     */
    final float[][][] rotateWidgetsMeta = new float[][][]{{
            //圆圈
            {-0.55f, -0.7f, 0f, 0.3f},
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


    /**
     * 重写绘制顺序
     */
    @Override
    public void drawWiget() {
        widgets[0].drawFrame();
        rotateWidgets[0].onDrawFrame();
        widgets[1].drawFrame();
    }
}
