package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine2;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 放大-缩小-放大
 */
public class VTTwoThemeTwo extends BaseBlurThemeExample {

    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;


    public VTTwoThemeTwo(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 3, 0.1f, 1f);
//        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2f, 0, 0).
//                setOrientation(AnimateInfo.ORIENTATION.TOP));
        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.1f, 1.1f).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);


        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(0.01f, 1f).setIsNoZaxis(true).setZView(0).build());
        widgetOne.getEnterAnimation().setIsSubAreaWidget(true, 0.65f, 0.9f);
        widgetOne.setOutAnimation(new AnimationBuilder().build());
        widgetOne.setZView(0);

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(0.01f, 1f).setIsNoZaxis(true).setZView(0).build());
        widgetTwo.getEnterAnimation().setIsSubAreaWidget(true, 0.85f, 0.8f);
        widgetTwo.setOutAnimation(new AnimationBuilder().build());
        widgetTwo.setZView(0);

        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(0.01f, 1f).setIsNoZaxis(true).setZView(0).build());
        widgetThree.getEnterAnimation().setIsSubAreaWidget(true, 0.85f, 0.6f);
        widgetThree.setOutAnimation(new AnimationBuilder().build());
        widgetThree.setZView(0);

        widgetFour = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetFour.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(0.01f, 1f).setIsNoZaxis(true).setZView(0).build());
        widgetFour.getEnterAnimation().setIsSubAreaWidget(true, -1f, -0.8f);
        widgetFour.setOutAnimation(new AnimationBuilder().setFade(1, 0).build());
        widgetFour.setZView(0);
    }


    @Override
    public void drawLast() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
        widgetFour.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap bitmap1, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, bitmap1, mimaps, width, height);

        float scale = width < height ? 5 : (width == height ? 6 : 5);
        float centerX = width < height ? 0.65f : (width == height ? 0.65f : 0.72f);
        float centerY = width < height ? 0.9f : (width == height ? 0.85f : 0.85f);

        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);


        centerX = width < height ? 0.85f : (width == height ? 0.85f : 0.85f);
        centerY = width < height ? 0.8f : (width == height ? 0.7f : 0.7f);
        widgetTwo.init(mimaps.get(0), width, height);
        widgetTwo.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);

        centerX = width < height ? 0.85f : (width == height ? 0.85f : 0.85f);
        centerY = width < height ? 0.65f : (width == height ? 0.45f : 0.4f);
        widgetThree.init(mimaps.get(0), width, height);
        widgetThree.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);


        widgetFour.init(mimaps.get(1), width, height);
        scale = width < height ? 4 : (width == height ? 4 : 4);
        centerX = width < height ? -0.8f : (width == height ? -0.8f : -0.85f);
        centerY = width < height ? -0.8f : (width == height ? -0.7f : -0.7f);
        widgetFour.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);
    }


    @Override
    public float[] getPos() {
        return cube;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
        widgetFour.onDestroy();
    }


}
