package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam3;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 欧南节3
 */
public class Onam3Action2 extends BaseBlurThemeTemplate {



    public Onam3Action2(int totalTime, int width, int height) {
        super(totalTime, width, height, true);
    }

    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }


    @Override
    protected BaseEvaluate initEnterAnimation() {

        return new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2.5f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT).setScale(1.1f, 1.1f));
    }

    @Override
    protected BaseEvaluate initOutAnimation() {

        return new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0f, -2.5f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM).setScale(1.1f, 1.1f));
    }

    float[][][] rotateWidgetMeta = new float[][][]{
            {
                    //左上， 右下
                    {-0.8f, -0.77f, 0f, 0.7f},
                    {0.6f, 0.75f, 0f, 0.7f},
            },
            {
                    //左下，右上
                    {-0.75f, 0.9f, 0f, 0.5f},
                    {0.9f, -0.85f, 0f, 0.5f}
            }
    };

    @Override
    protected float[][][] getRotateWidgetsMeta() {
        return rotateWidgetMeta;
    }







}
