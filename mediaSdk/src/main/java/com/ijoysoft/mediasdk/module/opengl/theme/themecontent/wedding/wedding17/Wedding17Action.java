package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding17;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 婚礼17
 */
public class Wedding17Action extends BaseBlurThemeTemplate {


    public Wedding17Action(int totalTime, int width, int height) {
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
        widgets[1] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.BOTTOM, AnimateInfo.ORIENTATION.BOTTOM);
        widgets[2] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.LEFT, AnimateInfo.ORIENTATION.LEFT);
        widgets[3] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.RIGHT);
    }

    /**
     * 空间元数据
     */
    private static final float[][] WIDGETS_META = new float[][] {
            //边框
            {0f, 0f, 1f, 1f, 1f},
            //彩带
            {Float.MIN_VALUE, Float.MAX_VALUE, 0.4f, 0.75f, 0.6f},
            //花
            {Float.MIN_VALUE, Float.MIN_VALUE, 1.25f, 2.5f, 2.5f},
            {Float.MAX_VALUE, Float.MAX_VALUE, 1f, 1.6f, 1.25f}
    };






    @Override
    protected float[][] getWidgetsMeta() {
        return WIDGETS_META;
    }




}
