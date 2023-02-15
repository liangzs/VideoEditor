package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday22;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 生日22
 */
public class Birthday22Action1 extends BaseBlurThemeTemplate {




    public Birthday22Action1(int totalTime, int width, int height) {
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
        widgets[0] = buildNoneAnimationWidget();
        widgets[1] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.LEFT, AnimateInfo.ORIENTATION.RIGHT);
        widgets[2] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);
        widgets[3] = buildNewFadeInOutTemplateAnimate();
        widgets[4] = buildNewFadeInOutTemplateAnimate();


    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //边框
                    {0f, 0f, 1f, 1f, 1f},
                    //标题
                    {0f, 0.8f, 1.6f, 3.2f, 2.8f},
                    //蝴蝶
                    {0f, 0.57f, 3f, 4f, 4f},
                    //红色光效
                    {-0.75f, -0.2f, 0.8f, 1.6f, 1.6f},
                    {0.75f, 0f, 0.8f, 1.6f, 1.6f},
            };
        }
        return widgetsMeta;
    }

}
