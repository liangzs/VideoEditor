package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine2;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayShakeAnimation;

import java.util.List;

/**
 * 缩小-放大-缩小
 */
public class VTTwoThemeFour extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    public VTTwoThemeFour(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 4, 0.1f, 1.0f);
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 2, 0, 0f).setIsNoZaxis(true).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).build());

        widgetTwo = new BaseThemeExample(totalTime, 0, 3500, DEFAULT_OUT_TIME, true);
        widgetTwo.setStayAction(new AnimationBuilder(3500).setZView(0).setIsNoZaxis(true).
                setCoordinate(0, 1.5f, 0f, -2.5f).build());
        widgetTwo.setZView(-0.0f);
    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap bitmap1, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, bitmap1, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float scale = width < height ? 3 : (width == height ? 2 : 2);
        float centerX = 0;
        float centerY = width < height ? 0.75f : (width == height ? 0.6f : 0.6f);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
        widgetOne.setStayAction(new StayShakeAnimation(2500, width, height, 0f, 1f));


        scale = width < height ? 5 : (width == height ? 3 : 3);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), -0.7f, 1.4f, scale, scale);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }

}
