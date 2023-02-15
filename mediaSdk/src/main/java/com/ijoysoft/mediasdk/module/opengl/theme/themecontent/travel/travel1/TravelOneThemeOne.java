package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel1;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;

import java.util.List;

/**
 * 上进，上下悬停，左划出
 */
public class TravelOneThemeOne extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;


    public TravelOneThemeOne(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new AnimationBuilder(3000).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 10, 10).setWidthHeight(width, height).build();
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -2.5f, 0, 0).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 370).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, -2.5f, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 10, 360).build();
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setZView(0);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(-1f, 0, 0, 0).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(0, 0, -1, 0).build());
        //two
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setZView(0);
        widgetTwo.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(0, 1, 0, 0).setOrientation(AnimateInfo.ORIENTATION.BOTTOM)));
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(0, 0, 0, 1).build());
        //three
        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetThree.setZView(0);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(1f, 0, 0, 0).build());
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(0, 0, 1, 0).build());

        //four
        widgetFour = new BaseThemeExample(totalTime, 1500, 2500, DEFAULT_OUT_TIME);
        widgetFour.setEnterAnimation(new AnimationBuilder(1500).setEnterDelay(1000)
                .setFade(0, 1).build());
        widgetFour.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).
                setFade(1, 0).build());
    }

    @Override
    public void drawWiget() {
        widgetFour.drawFrame();
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float centerX = width < height ? -0.65f : (width == height ? -0.75f : -0.65f);
        float centerY = width < height ? -0.7f : (width == height ? -0.65f : -0.6f);
        float scaleX = width < height ? 4 : (width == height ? 4 : 4);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);

        //two
        centerX = width < height ? 0.7f : (width == height ? 0.8f : 0.85f);
        centerY = width < height ? 0.9f : (width == height ? 0.8f : 0.7f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleX);
        //three
        centerX = width < height ? 0.7f : (width == height ? 0.8f : 0.85f);
        centerY = width < height ? -0.8f : (width == height ? -0.8f : -0.7f);
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleX);

        //four
        centerX = cube[2] + 0.2f;
        centerY = cube[3];
        widgetFour.init(mimaps.get(3), width, height);
        widgetFour.adjustScaling(width, height,
                mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scaleX, scaleX);
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
