package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting9;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 问候8
 */
public class Greeting9Action extends BaseBlurThemeTemplate {


    public Greeting9Action(int totalTime, int width, int height, int index) {
        super(totalTime, width, height, true, true);
        setCoverWidgetsCount(1, 1);
        this.index = index;
        initWidgetAfter();
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



    public void initWidgetAfter() {
        widgets[0] = buildNoneAnimationWidget();
        if (index == 0) {
            widgets[1] = buildNewScaleInOutTemplateAnimateAtSelfCenter(1);
        } else {
            widgets[1] = buildNoneAnimationWidget();
        }
    }

    /**
     * 空间元数据
     */
    private static final float[][] widgetsMeta = new float[][]{
            //边框
            {0f, 0f, 1f, 1f, 1f},
            //标题
            {0f, -0.85f, 1.2f, 2.4f, 1.2f},
    };



    @Override
    protected float[][] getWidgetsMeta() {
        return widgetsMeta;
    }


}
