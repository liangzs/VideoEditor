package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday27;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 生日27
 */
public class Birthday27Action3 extends BaseBlurThemeTemplate {




    public Birthday27Action3(int totalTime, int width, int height) {
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
        widgets[1] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.RIGHT,  AnimateInfo.ORIENTATION.LEFT);
        widgets[2] = buildNewScaleInOutTemplateAnimateAtSelfCenter(2);
        widgets[3] = buildNewScaleInOutTemplateAnimateAtSelfCenter(2);

    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;



    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //挂星星
                    {0f, -0.85f, 1f, 1f},
                    //标题
                    {0f, Float.MAX_VALUE, 1.2f, 2.4f},
                    //紫光
                    {1f, 0.25f, 1f, 2f},
                    //SSAO
                    {-0.3f, -0.5f, 0.7f, 1.4f},
            };
        }
        return widgetsMeta;
    }

}
