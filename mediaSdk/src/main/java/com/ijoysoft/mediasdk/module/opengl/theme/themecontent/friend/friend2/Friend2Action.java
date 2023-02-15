package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.friend.friend2;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 朋友2
 */
public class Friend2Action extends BaseBlurThemeTemplate {


    public Friend2Action(int totalTime, int width, int height, int index) {
        super(totalTime, width, height, true, true);
        this.index = index % MOD;
        initWidgetAfter();
    }

    /**
     * 当前顺序
     */
    int index;

    /**
     * 不同种类数量
     */
    private static final int MOD = 5;



    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }



    public void initWidgetAfter() {
        widgets[0] = buildNoneAnimationWidget();
        widgets[1] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);
    }

    /**
     * 边框元数据
     */
    private static final float[] WIDGETS_PADDING = new float[]{0f, Float.MAX_VALUE, 1f, 1f, 1f};






    @Override
    protected float[][] getWidgetsMeta() {
        switch (index) {
            case 2: return new float[][]{WIDGETS_PADDING, WIDGETS_ACTION_TWO};
            default: return new float[][]{WIDGETS_PADDING, WIDGETS_DEFAULT};
        }

    }

    private static final float[] WIDGETS_ACTION_TWO = new float[]{0f, Float.MAX_VALUE, 1f, 2f, 1f};

    private static final float[] WIDGETS_DEFAULT = new float[]{0f, Float.MAX_VALUE, 1.2f, 2f, 1.2f};


    @Override
    protected void fixNotCoverWidgetsPositionManually(int i) {
        if (index != 2 && i == 1) {
            widgets[1].setVertex(transitionOpenglPosArray(widgets[1].getCube(), 0f, -0.1f));
        }
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
