package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday26;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 生日26
 */
public class Birthday26Action1 extends BaseBlurThemeTemplate {



    public Birthday26Action1(int totalTime, int width, int height) {
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
        widgets[0] = buildNewFadeInOutTemplateAnimate();
        widgets[1] = buildNewFadeInOutTemplateAnimate();
        widgets[2] = buildNewFadeInOutTemplateAnimate();
        widgets[3] = buildNewScaleInOutTemplateAnimateAtSelfCenter(3);

    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;



    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //暗色效果
                    {0f, Float.MAX_VALUE, 1f, 1f},
                    //粉色效果
                    {1f, -0.2f, 0.6f, 1.2f},
                    //黄色效果
                    {-0.8f, -0.85f, 0.6f, 1.2f},
                    //标题
                    {0f, 0.65f, 1.1f, 2.2f}
            };
        }
        return widgetsMeta;
    }


}
