package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding16;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 婚礼16
 */
public class Wedding16Action extends BaseBlurThemeTemplate {


    public Wedding16Action(int totalTime, int width, int height, int index) {
        super(totalTime, width, height, true, true );
        this.index = index % 4;
        setCoverWidgetsCount(1, 1);
    }

    /**
     * 当前顺序
     */
    int index;



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
        widgets[2] = buildNoneAnimationWidget();

    }


    /**
     * 空间元数据
     */
    private static final float[][] WIDGETS_META = new float[][] {
            //渐变效果
            {0f, Float.MAX_VALUE, 1f, 1f, 1f},
            //情侣
            {0f, Float.MAX_VALUE, 1f, 1f, 1f},
            //粉色效果
            {-0.75f, -0.9f, 1f, 1f, 1f}
    };






    @Override
    protected float[][] getWidgetsMeta() {
        if (index == 1 || index == 2) {
            WIDGETS_META[2][0] = 0.75f;
        } else {
            WIDGETS_META[2][0] = -0.75f;
        }
        if (width >= height) {
            WIDGETS_META[1][1] = 0.6f;
        } else  {
            WIDGETS_META[1][1] = Float.MAX_VALUE;
        }
        return WIDGETS_META;
    }






}
