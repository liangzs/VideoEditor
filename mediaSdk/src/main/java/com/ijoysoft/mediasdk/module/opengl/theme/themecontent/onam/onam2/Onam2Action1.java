package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam2;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 欧南节2
 */
public class Onam2Action1 extends BaseBlurThemeTemplate {

    public Onam2Action1(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
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
//        //左云, 由大变小
//        widgets[0] = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
//        widgets[0].setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME)
//                .setIsNoZaxis(true).setScale(2f, 1f)
//                .setStartX(getWidgetsMeta()[0][0]).setStartY(getWidgetsMeta()[0][1])
//                .build());
//        widgets[0].setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setFade(1f, 0f).build());
//        widgets[0].setZView(0);
//
//        //右云, 由大变小
//        widgets[1] = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
//        widgets[1].setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME)
//                .setIsNoZaxis(true).setScale(2f, 1f)
//                .setStartX(getWidgetsMeta()[1][0]).setStartY(getWidgetsMeta()[1][1])
//                .build());
//        widgets[1].setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setFade(1f, 0f).build());
//        widgets[1].setZView(0);
        widgets[0] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);
        widgets[1] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);

        //左鸽,一直往上飞
        widgets[2] = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME + BaseThemeExample.DEFAULT_STAY_TIME, 0, DEFAULT_OUT_TIME, true);
        widgets[2].setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME + DEFAULT_STAY_TIME)
                .setCoordinate(0f, 2f, 0, -2f)
                .setIsNoZaxis(true).build());
        widgets[2].setZView(0);
        //右鸽，一直往上飞
        widgets[3] = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME + BaseThemeExample.DEFAULT_STAY_TIME, 0, DEFAULT_OUT_TIME, true);
        widgets[3].setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME + DEFAULT_STAY_TIME)
                .setCoordinate(0f, 2f, 0, -2f)
                .setIsNoZaxis(true).build());
        widgets[3].setZView(0);

    }



    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //左云
                    {-0.5861f, -0.8375f, 2.416f, 4.832f},
                    //右云
                    {0.3736f, -0.696f, 1.6f, 3.2f},
                    //左鸽
                    {-0.7f, -0.3f, 3f, 3f},
                    //右鸽
                    {0.5f, 0.7f, 3f, 3f},
            };
        }
        return widgetsMeta;
    }

}
