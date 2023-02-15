package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday23;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 生日23
 */
public class Birthday23Action1 extends BaseBlurThemeTemplate {



    public Birthday23Action1(int totalTime, int width, int height) {
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
        widgets[0] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.TOP, AnimateInfo.ORIENTATION.TOP);
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
                    //旗子
                    {0f, Float.MIN_VALUE, 1f, 1f, 1f},
                    //标题
                    {0f, 0.6f, 1.2f, 2.4f, 2.4f},
            };
        }
        return widgetsMeta;
    }
}
