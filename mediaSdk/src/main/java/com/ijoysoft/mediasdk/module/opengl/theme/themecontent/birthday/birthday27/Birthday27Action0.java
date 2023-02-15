package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday27;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 生日27
 */
public class Birthday27Action0 extends BaseBlurThemeTemplate {


    public Birthday27Action0(int totalTime, int width, int height) {
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
        widgets[1] = buildNewScaleInOutTemplateAnimateAtSelfCenter(1);
        widgets[2] = buildNewScaleInOutTemplateAnimateAtSelfCenter(1);
        widgets[3] = buildNewScaleInOutTemplateAnimate(1f, 1f);

    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;



    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //标题
                    {-0.4f, Float.MAX_VALUE, 1.35f, 1.35f},
                    //紫光
                    {-0.75f, 0.4f, 1f, 2f},
                    //SSAO
                    {0.2f, -0.1f, 0.7f, 1.4f},
                    //气球
                    {Float.MAX_VALUE, Float.MAX_VALUE, 0.9f, 1.8f}
            };
        }
        return widgetsMeta;
    }


}
