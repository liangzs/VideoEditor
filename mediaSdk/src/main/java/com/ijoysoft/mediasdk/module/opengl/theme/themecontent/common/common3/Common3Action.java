package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common3;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 通用3
 */
public class Common3Action extends BaseBlurThemeTemplate {


    public Common3Action(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
        setCoverWidgetsCount(1, 1);
        initWidgetAfter();
    }






    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }



    public void initWidgetAfter() {
        widgets[0] = buildNoneAnimationWidget();
    }

    /**
     * 空间元数据
     */
    private static final float[][] widgetsMeta = new float[][]{
            //边框
            {0f, 0f, 1f, 1f},
    };




    @Override
    protected float[][] getWidgetsMeta() {
        return widgetsMeta;
    }


}
