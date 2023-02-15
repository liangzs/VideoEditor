package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam1;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 欧南节1
 */
public class Onam1Action1 extends BaseBlurThemeTemplate {

    public Onam1Action1(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
        notCoverWidgetsStartIndex = 1;
    }

    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }





    /**
     * 动画设置
     */
    @Override
    public void initWidget() {
        super.initWidget();
        //顶tag,从上边出现
        widgets[0] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.TOP, AnimateInfo.ORIENTATION.TOP);
        //飘带
        widgets[1] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.LEFT, AnimateInfo.ORIENTATION.LEFT);
        widgets[2] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.RIGHT);
        //双烛台
        widgets[3] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[3][0], getWidgetsMeta()[3][1]);
        //中间烛台
        widgets[4] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.BOTTOM, AnimateInfo.ORIENTATION.BOTTOM);

    }



    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //顶部tag
                    {0f, 0f, 1f, 1f},
                    //飘带
                    {-0.55f, -0.8f, 2.6f, 2.6f},
                    {0.55f, -0.8f, 2.6f, 2.6f},
                    //双烛台
                    {0f, 0.65f, 1.4f, 1.4f},
                    //中间烛台
                    {0f, 0.83f, 3f, 3f}
            };
        }
        return widgetsMeta;
    }

}
