package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common4;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 通用4
 */
public class Common4Action extends BaseBlurThemeTemplate {


    public Common4Action(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
    }




    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }


    @Override
    public void initWidget() {
        super.initWidget();
        widgets[0] = buildNoneAnimationWidget();
    }

    /**
     * 边框元数据
     */
    private static final float[][] WIDGETS_META = new float[][]
            {{0f, Float.MAX_VALUE, 1f, 1f, 1f}};






    @Override
    protected float[][] getWidgetsMeta() {
        return WIDGETS_META;

    }


    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
        if (width == height) {
            setCoverWidgetsCount( 1, 1);
        } else {
            setCoverWidgetsCount(0, 0);
        }
    }
}
