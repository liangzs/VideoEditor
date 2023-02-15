package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan8;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 兄妹节8
 */
public class RBEightActionOne extends BaseBlurThemeTemplate {


    public RBEightActionOne(int totalTime, int width, int height) {
        super(totalTime, width, height, true);
//        notCoverWidgetsStartIndex = 1;
    }


    /**
     * 动画设置
     */
    @Override
    public void initWidget() {
        super.initWidget();
        //边框
        widgets[0] = buildNewScaleInOutTemplateAnimate(totalTime, -1f, 1f);
        //左上角绳子
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
                    {0f, 0.48f, 1f, 1f},
                    //左上角绳子
                    {-0.55f, -0.87f, 2f, 4f},
            };
        }
        return widgetsMeta;
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
