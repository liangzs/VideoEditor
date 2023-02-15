package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday12;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 缩小-放大-缩小
 */
public class HDTwelveThemeFour extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    public HDTwelveThemeFour(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 3, 0.1f, 1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, -2f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.TOP));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 2f, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT));
        this.isNoZaxis = isNoZaxis;


        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 1, 0f, 0f).setIsNoZaxis(true).build());
        widgetOne.setZView(0);
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());

        //two
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).
                setCoordinate(0, -1f, 0f, 0f).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());
        widgetTwo.setZView(-0.0f);

    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        //文字
        widgetOne.init(mimaps.get(0), width, height);
        float[] pos = width < height ? new float[]{
                -0.9f, 0.95f,
                -0.9f, 0.8f,
                0.9f, 0.95f,
                0.9f, 0.8f,
        } : (width == height ? new float[]{
                -0.8f, 0.95f,
                -0.8f, 0.75f,
                0.8f, 0.95f,
                0.8f, 0.75f,
        } : new float[]{
                -0.5f, 0.95f,
                -0.5f, 0.75f,
                0.5f, 0.95f,
                0.5f, 0.75f,
        });
        widgetOne.setVertex(pos);
        widgetTwo.init(mimaps.get(1), width, height);

        pos = width < height ? new float[]{
                -1f, -0.65f,
                -1f, -1,
                1f, -0.65f,
                1f, -1,
        } : (width == height ? new float[]{
                -1f, -0.6f,
                -1f, -1,
                1f, -0.6f,
                1f, -1,
        } : new float[]{
                -1f, -0.4f,
                -1f, -1,
                1f, -0.4f,
                1f, -1,
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
