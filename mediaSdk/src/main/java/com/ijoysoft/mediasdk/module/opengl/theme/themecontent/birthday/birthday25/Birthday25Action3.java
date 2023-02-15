package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday25;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 生日25
 */
public class Birthday25Action3 extends BaseBlurThemeTemplate {




    public Birthday25Action3(int totalTime, int width, int height) {
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
        widgets[1] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.LEFT, AnimateInfo.ORIENTATION.RIGHT);
        widgets[2] = buildNewScaleInOutTemplateAnimateAtSelfCenter(2);

    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;



    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //颜色效果
                    {0f, Float.MIN_VALUE, 1f, 1f},
                    //标题
                    {0f, Float.MIN_VALUE, 1f, 2f},
                    //蛋糕
                    {0f, Float.MAX_VALUE, 1.8f, 3.6f},
            };
        }
        return widgetsMeta;
    }

}
