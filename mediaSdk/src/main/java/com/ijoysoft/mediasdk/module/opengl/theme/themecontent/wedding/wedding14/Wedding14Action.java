package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding14;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 婚礼13
 */
public class Wedding14Action extends BaseBlurThemeTemplate {


    public Wedding14Action(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true );
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
        widgets[1] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.LEFT, AnimateInfo.ORIENTATION.RIGHT);


        widgets[2] = buildNewScaleInOutTemplateAnimate(0f, 0f);
        widgets[3] = buildNoneAnimationWidget();
        widgets[4] = buildNoneAnimationWidget();
        widgets[5] = buildNoneAnimationWidget();
        widgets[6] = buildNoneAnimationWidget();
        widgets[7] = buildNoneAnimationWidget();
        widgets[8] = buildNoneAnimationWidget();
    }

    /**
     * 空间元数据
     */
    private static final float[][] WIDGETS_META = new float[][] {
            //边框
            {0f, Float.MIN_VALUE, 1f, 1f, 1f},
            //标题
            {0f, 0.8f, 1.2f, 2.4f, 1.2f},
            //戒指
            {0f, 0.6f, 3f, 5f, 3f},
            {-0.9f, -0.6667f, 1f, 1f, 1f},
            {-0.9f, 0f, 1f, 1f, 1f},
            {-0.9f, 0.6667f, 1f, 1f, 1f},
            {0.9f, -0.6667f, 1f, 1f, 1f},
            {0.9f, 0f, 1f, 1f, 1f},
            {0.9f, 0.6667f, 1f, 1f, 1f},
    };






    @Override
    protected float[][] getWidgetsMeta() {
        if(width == height) {
            WIDGETS_META[2][1] = 0.3f;
        } else if (width > height) {
            WIDGETS_META[2][1] = 0.4f;
        } else {
            WIDGETS_META[2][1] = 0.47f;
        }
        return WIDGETS_META;
    }




}
