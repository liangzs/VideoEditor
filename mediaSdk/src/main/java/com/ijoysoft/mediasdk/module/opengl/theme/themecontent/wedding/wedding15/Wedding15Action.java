package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding15;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 婚礼15
 */
public class Wedding15Action extends BaseBlurThemeTemplate {


    public Wedding15Action(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true );
        setCoverWidgetsCount(1, 1);
    }




    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }

    /**
     * 在获取到index后再初始化控件
     */
    @Override
    public void initWidget() {
        super.initWidget();
        widgets[0] = buildNoneAnimationWidget();
        widgets[1] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);

    }

    /**
     * 空间元数据
     */
    private static final float[][] WIDGETS_META = new float[][] {
            //边框
            {0f, 0f, 1f, 1f, 1f},
            //标题
            {0f, 0.7f, 1.1f, 2f, 1.2f},
    };






    @Override
    protected float[][] getWidgetsMeta() {
        return WIDGETS_META;
    }




}
