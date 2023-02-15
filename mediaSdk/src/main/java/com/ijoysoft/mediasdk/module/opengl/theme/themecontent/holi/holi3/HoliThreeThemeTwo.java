package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi3;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 放大-缩小-放大
 */
public class HoliThreeThemeTwo extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;
    private BaseThemeExample widgetFive;

    public HoliThreeThemeTwo(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 3, 0.1f, 1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.TOP));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, -2f, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT).setScale(1.1f, 1.1f));
        this.isNoZaxis = isNoZaxis;
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetThree.drawFrame();
        widgetTwo.drawFrame();
        widgetFour.drawFrame();
        widgetFive.drawFrame();
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, 1200, 0, DEFAULT_OUT_TIME);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setCoordinate(0, 1.0f, 0f, 0f).setZView(-2.4f)));
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setFade(1, 0).setZView(-2.4f).build());
        widgetOne.setZView(-2.4f);
        //two
        widgetTwo = new BaseThemeExample(totalTime, 2000, 0, DEFAULT_OUT_TIME);
        widgetTwo.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setEnterDelay(800).setCoordinate(0, 1.0f, 0f, 0f).setZView(-2.4f)));
        widgetTwo.setZView(-2.4f);
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setFade(1, 0).setZView(-2.4f).build());
        //three
        widgetThree = new BaseThemeExample(totalTime, 2800, 0, DEFAULT_OUT_TIME);
        widgetThree.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setEnterDelay(1600).setCoordinate(0, 1.0f, 0f, 0f).setZView(-2.4f)));
        widgetThree.setZView(-2.4f);
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setFade(1, 0).setZView(-2.4f).build());

        widgetFour = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME, true);
        widgetFour.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(4, 0, 0, 0).setIsNoZaxis(true).build());
        widgetFour.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setFade(1, 0).setIsNoZaxis(true).build());

        widgetFive = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME + 500, 0, DEFAULT_OUT_TIME, true);
        widgetFive.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setEnterDelay(500).setCoordinate(3, 0, 0, 0).setIsNoZaxis(true).build());
        widgetFive.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setFade(1, 0).setIsNoZaxis(true).build());
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scale = width < height ? 3 : (width == height ? 3 : 3);
        float centerX = width < height ? -0.4f : (width == height ? -0.5f : -0.5f);
        float centerY = width < height ? 0.7f : (width == height ? 0.8f : 0.8f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
        scale = width < height ? 3.5f : (width == height ? 3 : 3);
        centerX = 0;
        centerY = width < height ? 0.8f : (width == height ? 0.8f : 0.8f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);
        centerX = width < height ? 0.35f : (width == height ? 0.5f : 0.5f);
        centerY = width < height ? 0.7f : (width == height ? 0.8f : 0.8f);
        scale = width < height ? 3.2f : (width == height ? 3 : 3);
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scale, scale);

        scale = width < height ? 1.2f : (width == height ? 1.5f : 1.5f);
        widgetFour.init(mimaps.get(3), width, height);
        centerX = width < height ? 0.9f : (width == height ? 0.8f : 0.8f);
        centerY = width < height ? -0.8f : (width == height ? -0.8f : -0.8f);
        widgetFour.adjustScaling(width, height,
                mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scale, scale);

        widgetFive.init(mimaps.get(4), width, height);
        centerX = width < height ? 0.85f : (width == height ? 0.8f : 0.8f);
        centerY = 0.4f;
        widgetFive.adjustScaling(width, height,
                mimaps.get(4).getWidth(), mimaps.get(4).getHeight(), centerX, centerY, scale, scale);

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
