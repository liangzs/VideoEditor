package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday24;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 生日24
 */
public class Birthday24Action3 extends BaseBlurThemeTemplate {




    public Birthday24Action3(int totalTime, int width, int height) {
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
        widgets[2] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);
    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //绿色效果
                    {0.5f, 0.6f, 0.6f, 0.6f, 0.6f},
                    //黄色效果
                    {0f, -0.9f, 0.6f, 0.6f, 0.6f},
                    //标题
                    {0f, Float.MAX_VALUE, 1.1f, 2.2f, 1.2f},
            };
        }
        return widgetsMeta;
    }

    @Override
    protected void fixNotCoverWidgetsPositionManually(int i) {
        if (i == 2) {
            widgets[2].setVertex(transitionOpenglPosArray(widgets[2].getCube(), 0f, -0.05f));
        }
    }

}
