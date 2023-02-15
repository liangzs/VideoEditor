package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday2;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 缩小
 */
public class HDTwoThemeThree extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;


    public HDTwoThemeThree(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 1, 0.2f, 1f);
        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(0, -1, 0, 0).build());
        widgetOne.setZView(0);

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME, true);
        widgetTwo.setZView(0);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setFade(0, 1).build());


    }


    @Override
    public void drawWiget() {

        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit,mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float centerx = 0;
        float centery = width < height ? -0.9f : -0.85f;
        float scale = width < height ? 1.5f : 2;
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerx, centery, scale, scale);

        widgetTwo.init(mimaps.get(1), width, height);
        pos = width < height ? new float[]{
                -1f, 1,
                -1f, 0.55f,
                1f, 1,
                1f, 0.55f,
        } : (width == height ? new float[]{
                -1f, 1f,
                -1f, 0.5f,
                1f, 1f,
                1f, 0.5f,
        } : new float[]{
                -1f, 1f,
                -1f, 0.1f,
                1f, 1f,
                1f, 0.1f,
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
