package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday27;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 生日27
 */
public class Birthday27Action4 extends BaseBlurThemeTemplate {



    public Birthday27Action4(int totalTime, int width, int height) {
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
            //TODO: 对翻转动画的探索
//        widgets[0] = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
//        widgets[0].setZView(-3f);
//        widgets[0].setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME)
//                        .setZView(-3f)
//                        .setConors(AnimateInfo.ROTATION.X_ANXIS, -90, -180)
////                .setBgConor(endConorOffset)
//                        .build()
//        );

        widgets[1] = buildNewScaleInOutTemplateAnimateAtSelfCenter(1);
        widgets[2] = buildNewScaleInOutTemplateAnimateAtSelfCenter(1);

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
                    {0f, Float.MIN_VALUE, 1f, 1f},
                    //紫光
                    {-0.75f, 0.4f, 1f, 2f},
                    //SSAO
                    {0.1f, -0.1f, 0.7f, 1.4f},
            };
        }
        return widgetsMeta;
    }

}
