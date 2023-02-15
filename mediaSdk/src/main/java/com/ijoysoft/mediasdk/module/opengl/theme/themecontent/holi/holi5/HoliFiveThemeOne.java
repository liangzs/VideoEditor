package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi5;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 缩小
 * 这里在悬停缩放时，添加滤镜
 */
public class HoliFiveThemeOne extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;
    private BaseThemeExample widgetFive;


    public HoliFiveThemeOne(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.2f, 1.2f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setFade(0, 1).build());
        widgetOne.setZView(0);
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME + 500, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setZView(0);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setEnterDelay(500).setIsNoZaxis(true).setZView(0).
                setFade(0, 1).build());

        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME + 1000, 0, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setEnterDelay(1000).setIsNoZaxis(true).setZView(0).
                setFade(0, 1).build());

        widgetFour = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME, true);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT));
        widgetFour.setEnterAnimation(enterAnimation);
        widgetFive = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME, true);

    }


    @Override
    public void drawWiget() {
        widgetFive.drawFrame();
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
        widgetFour.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);

        float scale = width < height ? 4 : (width == height ? 4 : 4);
        float centerX = width < height ? -0.4f : (width == height ? -0.4f : -0.4f);
        float centerY = width < height ? 0.6f : (width == height ? 0.6f : 0.6f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);

        centerX = width < height ? -0.8f : (width == height ? -0.8f : -0.8f);
        centerY = width < height ? -0.2f : (width == height ? -0.2f : -0.2f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);

        centerX = width < height ? -0.7f : (width == height ? -0.7f : -0.7f);
        centerY = width < height ? -0.7f : (width == height ? -0.7f : -0.7f);
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scale, scale);

        scale = width < height ? 3 : (width == height ? 3 : 3);
        centerX = width < height ? 0.6f : (width == height ? 0.6f : 0.6f);
        centerY = width < height ? 0.8f : (width == height ? 0.8f : 0.8f);
        widgetFour.init(mimaps.get(3), width, height);
        widgetFour.adjustScaling(width, height,
                mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scale, scale);

        widgetFive.init(mimaps.get(4), width, height);
        widgetFive.setVertex(pos);
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
