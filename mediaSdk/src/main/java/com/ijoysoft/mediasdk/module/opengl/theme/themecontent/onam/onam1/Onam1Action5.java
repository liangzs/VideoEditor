package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam1;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 欧南节1
 */
public class Onam1Action5 extends BaseBlurThemeTemplate {


    public Onam1Action5(int totalTime, int width, int height) {
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
        //标题
        widgets[0] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[0][0], getWidgetsMeta()[0][1]);

    }

    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[][]{
                    //标题
                    {0f, 0.7f, 1.6f, 3.2f},
            };
        }
        return widgetMeta;
    }

    float[][][] rotateWidgetMeta = new float[][][]{{
            //旋转圈圈
            {0f, -1f, 0f, 0.7f},
    }};

    @Override
    protected float[][][] getRotateWidgetsMeta() {
        return rotateWidgetMeta;
    }




}
