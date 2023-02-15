package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday20;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 生日20
 */
public class Birthday20Action3 extends BaseBlurThemeTemplate {




    public Birthday20Action3(int totalTime, int width, int height) {
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
        widgets[0] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.BOTTOM, AnimateInfo.ORIENTATION.BOTTOM);
        widgets[1] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[1][0], getWidgetsMeta()[1][1], 1.1f, 1f);
        widgets[1].setStayAction(new StayScaleAnimation(2500, false, 1, 0.1f, 1.1f));
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
                    //变暗效果
                    {0f, Float.MAX_VALUE, 1f, 1f, 1f},
                    //标题
                    {0f, 0.5f, 1.2f, 2.4f, 2f},
                    //紫色效果
                    {-1f, 0.5f, 0.7f, 0.7f, 0.7f},
            };
        }
        return widgetsMeta;
    }

}
