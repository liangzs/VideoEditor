package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan3;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 兄妹节3
 */
public class Rakshabandhan3Action3 extends BaseBlurThemeTemplate {


    public Rakshabandhan3Action3(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
    }

    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }



    @Override
    protected float initZView() {
        return 0;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        widgets[0] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[0][0], getWidgetsMeta()[0][1]);
        widgets[1] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.TOP, AnimateInfo.ORIENTATION.BOTTOM);
        widgets[2] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.TOP, AnimateInfo.ORIENTATION.BOTTOM);
        widgets[3] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.TOP, AnimateInfo.ORIENTATION.BOTTOM);
        widgets[4] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.TOP, AnimateInfo.ORIENTATION.BOTTOM);
        widgets[5] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.TOP, AnimateInfo.ORIENTATION.BOTTOM);



    }

    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    {0f, 0.7f, 1.25f, 2.5f},
                    {-0.77f, -0.55f, 2.2f, 2.2f},
                    {-0.5f, -0.7f, 3.2f, 3.2f},
                    {0f, -0.65f, 2.6f, 2.6f},
                    {0.45f, -0.6f, 2.5f, 2.5f},
                    {0.75f, -0.7f, 2.5f, 2.5f},
            };
        }
        return widgetsMeta;
    }




}
