package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday9;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WaveAnimation;

import java.util.List;

/**
 * 右边波浪进
 */
public class BirthDayNineThemeTwo extends BaseThemeExample {

    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;

    public BirthDayNineThemeTwo(int totalTime) {
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
        widgetOne = new BaseThemeExample(totalTime, 500, totalTime - 500, 0);
        widgetOne.setStayAction(new AnimationBuilder(totalTime).setZView(-2.0f).
                setCoordinate(0, 2.5f, 0f, -1.5f).build());
        widgetOne.setZView(-2.0f);


        widgetTwo = new BaseThemeExample(totalTime, 2000, 0, totalTime - 2000);
        widgetTwo.setOutAnimation(new AnimationBuilder(totalTime - 1000).
                setCoordinate(0, 2.5f, 0f, -1.5f).setZView(-2.0f).build());
        widgetTwo.setZView(-2.0f);

        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        widgetThree.setZView(-2.0f);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2f, 0f, 0f).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setZView(-2.0f).build());
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, -2.5f, 0f).setZView(-2).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setZView(-2.0f).build());
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
        float scaleX = width < height ? 3 : (width == height ? 3 : 3);
        float centerX = width < height ? -0.7f : (width == height ? -0.7f : -0.7f);
        float centerY = width < height ? -1.4f : (width == height ? -1.4f : -1.4f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 3 : (width == height ? 3 : 3);
        centerX = width < height ? 0.7f : (width == height ? 0.7f : 0.7f);
        centerY = width < height ? -1.4f : (width == height ? -1.4f : -1.4f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 2 : (width == height ? 2 : 3);
        centerX = 0;
        centerY = width < height ? 0.7f : (width == height ? 0.65f : 0.65f);
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleX);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
    }
}
