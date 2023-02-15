package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday18;

import android.graphics.Bitmap;
import android.opengl.Matrix;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WaveAnimation;

import java.util.List;

/**
 * 右边波浪进
 */
public class BirthDayEightteenThemeTwo extends BaseThemeExample {

    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;

    public BirthDayEightteenThemeTwo(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(3000, false, 2, 0.1f, 1.1f);
        stayAction.setZView(-2);
        enterAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(2f, 0f, 0, 0).setZView(-2).setScale(1.1f, 1.1f));
        outAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(0f, 0f, -2f, 0).setZView(-2).setScale(1.1f, 1.1f));
        setZView(-2);

    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_ENTER_TIME, DEFAULT_ENTER_TIME);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.5f).
                setCoordinate(0, 1f, 0f, 0f).build());
        widgetOne.setZView(-2.5f);
        widgetOne.setOutAnimation(new AnimationBuilder(0).setZView(0).build());

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_ENTER_TIME);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setFade(0, 1).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(0).setZView(0).build());

        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_ENTER_TIME);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setFade(0, 1).build());
        widgetThree.setOutAnimation(new AnimationBuilder(0).setZView(0).build());
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        init(bitmap, width, height);

        float scaleX = width < height ? 1.5f : (width == height ? 2 : 3);
        float centerX = 0;
        float centerY = width < height ? 0.85f : (width == height ? 0.75f : 0.75f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 6 : (width == height ? 7 : 6);
        centerX = width < height ? -0.9f : (width == height ? -0.85f : -0.8f);
        centerY = width < height ? -1f : (width == height ? -0.85f : -0.6f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 5 : (width == height ? 6 : 5);
        centerX = width < height ? 0.85f : (width == height ? 0.85f : 0.7f);
        centerY = width < height ? -0.9f : (width == height ? -0.8f : -0.6f);
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleX);
    }

    @Override
    protected void wedThemeSpecialDeal() {
        super.wedThemeSpecialDeal();
        Matrix.translateM(mViewMatrix, 0, 0f, -0.2f, 0);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
    }
}
