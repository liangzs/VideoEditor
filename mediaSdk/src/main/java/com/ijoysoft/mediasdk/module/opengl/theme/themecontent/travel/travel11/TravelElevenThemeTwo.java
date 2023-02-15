package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel11;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayShakeAnimation;

import java.util.List;

/**
 * @ author       lfj
 * @ date         2020/10/29
 * @ description
 */
public class TravelElevenThemeTwo extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;
    private BaseThemeExample widgetFive;

    public TravelElevenThemeTwo(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, 0);
        stayAction = new StayScaleAnimation(2500, true, 3, 0.1f, 1f);
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        //one
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setZView(0);

        //从下往上平移
        widgetTwo = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 0, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(-1f, 1f, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true).build());
        widgetTwo.setZView(0);

        //从下往上平移
        widgetThree = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 0, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(1f, 1f, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true).build());
        widgetThree.setZView(0);

        //平移后摇摆
        widgetFour = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 0, DEFAULT_OUT_TIME, true);
        widgetFour.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(-1f, 0, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true).build());
        float centerX = width == height ? -0.8f : (width < height ? -0.8f : -0.8f);
        float centerY = width == height ? -0.7f : (width < height ? -0.7f : -0.7f);
        widgetFour.setStayAction(new StayShakeAnimation(2500, width, height, centerX, centerY));
        widgetFour.setZView(0);

        //从左往右平移
        widgetFive = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 0, DEFAULT_OUT_TIME, true);
        widgetFive.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(-1f, 0, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true).build());
        centerX = width == height ? -0.5f : (width < height ? -0.4f : -0.6f);
        centerY = width == height ? -0.5f : (width < height ? -0.5f : -0.5f);
        widgetFive.setStayAction(new StayShakeAnimation(2500, width, height, centerX, centerY));
        widgetFive.setZView(0);
    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
        widgetFour.drawFrame();
        widgetFive.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float pos[] = width < height ? new float[]{
                -1f, 1f,
                -1f, 0.8f,
                1f, 1f,
                1f, 0.8f,
        } : (width == height ? new float[]{
                -1f, 1f,
                -1f, 0.82f,
                1f, 1f,
                1f, 0.82f,
        } : new float[]{
                -1f, 1f,
                -1f, 0.8f,
                1f, 1f,
                1f, 0.8f,
        });
        widgetOne.setVertex(pos);

        float scaleX = width == height ? 3 : (width < height ? 3 : 3);
        float scaleY = width == height ? 3 : (width < height ? 3 : 3);
        float centerX = width == height ? -0.8f : (width < height ? -0.8f : -0.8f);
        float centerY = width == height ? 0.8f : (width < height ? 0.83f : 0.8f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleY);

        scaleX = width == height ? 3 : (width < height ? 3 : 3);
        scaleY = width == height ? 3 : (width < height ? 3 : 3);
        centerX = width == height ? 0.8f : (width < height ? 0.8f : 0.8f);
        centerY = width == height ? 0.8f : (width < height ? 0.84f : 0.8f);
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleY);

        scaleX = width == height ? 8 : (width < height ? 8 : 8);
        scaleY = width == height ? 8 : (width < height ? 8 : 8);
        centerX = width == height ? -0.8f : (width < height ? -0.8f : -0.8f);
        centerY = width == height ? -0.7f : (width < height ? -0.7f : -0.7f);
        widgetFour.init(mimaps.get(3), width, height);
        widgetFour.adjustScaling(width, height,
                mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scaleX, scaleY);

        scaleX = width == height ? 10 : (width < height ? 10 : 10);
        scaleY = width == height ? 10 : (width < height ? 10 : 10);
        centerX = width == height ? -0.5f : (width < height ? -0.4f : -0.6f);
        centerY = width == height ? -0.5f : (width < height ? -0.5f : -0.5f);
        widgetFive.init(mimaps.get(4), width, height);
        widgetFive.adjustScaling(width, height,
                mimaps.get(4).getWidth(), mimaps.get(4).getHeight(), centerX, centerY, scaleX, scaleY);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
        widgetFour.onDestroy();
        widgetFive.onDestroy();
    }

}

