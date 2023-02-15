package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday23;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 生日23
 */
public class Birthday23Action0 extends BaseBlurThemeTemplate {


    public Birthday23Action0(int totalTime, int width, int height) {
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
        widgets[0] = buildNewScaleInOutTemplateAnimate(-1f, 1f);
        widgets[1] = buildNewScaleInOutTemplateAnimate(1f, 1f);
    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //礼炮
                    {Float.MIN_VALUE, Float.MAX_VALUE, 1.1f, 1.5f, 1.1f},
                    {Float.MAX_VALUE, Float.MAX_VALUE, 1.1f, 1.5f, 1.1f},
            };
        }
        return widgetsMeta;
    }


}
