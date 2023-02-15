package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel11;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * @ author       lfj
 * @ date         2020/10/29
 * @ description
 */
public class TravelElevenThemeThree extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;
    private BaseThemeExample widgetFive;
    private BaseThemeExample widgetSix;

    public TravelElevenThemeThree(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, 0);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.1f, 1.1f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.1f, 1.1f).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        //从下往上平移
        widgetOne = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 0, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(-1f, 1f, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true).build());
        widgetOne.setZView(0);
//        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());

        //从下往上平移
        widgetTwo = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 0, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(1f, 1f, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true).build());
        widgetTwo.setZView(0);
//        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());

        //渐变
        widgetThree = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 0, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(0, 1).setEnterDelay(500).setIsNoZaxis(true).build());
        widgetThree.setZView(0);

        //上面掉下来弹跳
        widgetFour = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 0, DEFAULT_OUT_TIME, true);
        widgetFour.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setCoordinate(0, -1f, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true)));
        widgetFour.setZView(0);

        //从左往右平移
        widgetFive = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 0, DEFAULT_OUT_TIME, true);
        widgetFive.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, -1f, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true).build());
        widgetFive.setZView(0);

        //从左往右平移
        widgetSix = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 0, DEFAULT_OUT_TIME, true);
        widgetSix.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, -1f, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true).build());
        widgetSix.setZView(0);
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
        widgetFour.drawFrame();
        widgetFive.drawFrame();
        widgetSix.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scaleX = width == height ? 3 : (width < height ? 3 : 3);
        float scaleY = width == height ? 3 : (width < height ? 3 : 3);
        float centerX = width == height ? -0.85f : (width < height ? -0.75f : -0.92f);
        float centerY = width == height ? 0.8f : (width < height ? 0.83f : 0.8f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleY);

        scaleX = width == height ? 3 : (width < height ? 3 : 3);
        scaleY = width == height ? 3 : (width < height ? 3 : 3);
        centerX = width == height ? 0.8f : (width < height ? 0.73f : 0.88f);
        centerY = width == height ? 0.8f : (width < height ? 0.83f : 0.8f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleY);

        scaleX = width == height ? 2f : (width < height ? 1.5f : 2f);
        scaleY = width == height ? 2f : (width < height ? 1.5f : 2f);
        centerX = width == height ? 0f : (width < height ? 0f : 0f);
        centerY = width == height ? -0.8f : (width < height ? -0.85f : -0.65f);
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleY);

        scaleX = width == height ? 0 : (width < height ? 2f : 0);
        scaleY = width == height ? 0 : (width < height ? 2f : 0);
        centerX = width == height ? 0f : (width < height ? 0f : 0f);
        centerY = width == height ? -0.6f : (width < height ? -0.7f : -0.6f);
        widgetFour.init(mimaps.get(3), width, height);
        widgetFour.adjustScaling(width, height,
                mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scaleX, scaleY);

        scaleX = width == height ? 0 : (width < height ? 2f : 0);
        scaleY = width == height ? 0 : (width < height ? 2f : 0);
        centerX = width == height ? 0f : (width < height ? 0f : 0f);
        centerY = width == height ? -0.75f : (width < height ? -0.75f : -0.75f);
        widgetFive.init(mimaps.get(4), width, height);
        widgetFive.adjustScaling(width, height,
                mimaps.get(4).getWidth(), mimaps.get(4).getHeight(), centerX, centerY, scaleX, scaleY);

        scaleX = width == height ? 0 : (width < height ? 2f : 0);
        scaleY = width == height ? 0 : (width < height ? 2f : 0);
        centerX = width == height ? 0f : (width < height ? 0f : 0f);
        centerY = width == height ? -0.55f : (width < height ? -0.65f : -0.55f);
        widgetSix.init(mimaps.get(4), width, height);
        widgetSix.adjustScaling(width, height,
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
        widgetSix.onDestroy();
    }
}
