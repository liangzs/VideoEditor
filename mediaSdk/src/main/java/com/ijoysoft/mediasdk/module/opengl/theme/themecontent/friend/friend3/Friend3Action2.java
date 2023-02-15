package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.friend.friend3;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 朋友3
 */
public class Friend3Action2 extends BaseBlurThemeTemplate {




    public Friend3Action2(int totalTime, int width, int height) {
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
        widgets[2] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.BOTTOM, AnimateInfo.ORIENTATION.BOTTOM);

    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;



    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //红
                    {-0.9f, 0.9f, 0.3f, 0.3f, 0.3f},
                    //蓝
                    {0.9f, -0.9f, 0.3f, 0.3f, 0.3f},
                    //标题
                    {0f, Float.MAX_VALUE, 1f, 2f, 1.4f},
            };
        }
        return widgetsMeta;
    }

}
