package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding11;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 婚礼11
 */
public class Wedding11Action extends BaseBlurThemeTemplate {


    public Wedding11Action(int totalTime, int width, int height) {
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
        widgets[0] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.LEFT, AnimateInfo.ORIENTATION.RIGHT);

    }

    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
    }

    /**
     * 空间元数据
     */
    private static final float[][] WIDGETS_META_9_16 = new float[][]{
            //标题
            {0f, 0.7f, 1.2f, 2.4f},
    };

    private static final float[][] WIDGETS_META_16_9 = new float[][]{
            //标题
            {0f, 0f, 1.2f, 2.4f},
    };

    private static final float[][] WIDGETS_META_1_1 = new float[][]{
            //标题
            {0f, 0.4f, 1.2f, 2.4f},
    };



    @Override
    protected float[][] getWidgetsMeta() {
        if (width > height) {
            return WIDGETS_META_16_9;
        } else if (width < height) {
            return WIDGETS_META_9_16;
        } else {
            return WIDGETS_META_1_1;
        }
    }




}
