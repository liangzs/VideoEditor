package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting2;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * @ author       lfj
 * @ date         2020/10/16
 * @ description
 */
public class GreetTwoThemeThree extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    public GreetTwoThemeThree(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 1, 0.1f, 1.0f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 0f, 2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT)));
        widgetOne.setOutAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 0f, 2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM)));
        widgetOne.setZView(0);

        //two
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setZView(0);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(-1f, 0, 0, 0).build());
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.setVertex(pos);

        //文案
        widgetTwo.init(mimaps.get(1), width, height);
        //顶点坐标
        pos = width == height ? new float[]{
                -0.5f, 0.9f,
                -0.5f, 0.8f,
                0.5f, 0.9f,
                0.5f, 0.8f,
        } : (width < height ? new float[]{
                -0.3f, 0.9f,
                -0.3f, 0.85f,
                0.9f, 0.9f,
                0.9f, 0.85f,
        } : new float[]{
                -0.3f, 0.84f,
                -0.3f, 0.74f,
                0.3f, 0.84f,
                0.3f, 0.74f,
        });
        widgetTwo.setVertex(pos);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }

}
