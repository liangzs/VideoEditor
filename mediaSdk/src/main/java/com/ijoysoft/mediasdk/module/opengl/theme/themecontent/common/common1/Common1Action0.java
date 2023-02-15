package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common1;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 通用1
 */
public class Common1Action0 extends BaseBlurThemeTemplate {

    public Common1Action0(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
        setCoverWidgetsCount(1, 1);
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
        widgets[1] = buildNewScaleInOutTemplateAnimateAtSelfCenter(1);
    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //背景模糊效果
                    {0f, 0.42f, 1f, 1f},
                    //蓝色模糊
                    {1f, -1f, 0.5f, 0.5f},
            };
        }
        return widgetsMeta;
    }


}
