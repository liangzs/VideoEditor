package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday9;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 放大-缩小
 */
public class HDNineThemeFive extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;


    public HDNineThemeFive(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 2, 0.2f, 1f);
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, 0, 2000, DEFAULT_OUT_TIME * 2, true);
        widgetOne.setStayAction(new AnimationBuilder(totalTime).setZView(0).setIsNoZaxis(true).
                setCoordinate(0, 2.5f, 0f, -1.5f).build());
        widgetOne.setZView(-0.0f);

        widgetTwo = new BaseThemeExample(totalTime, 500, 2000, DEFAULT_OUT_TIME * 2, true);
        widgetTwo.setStayAction(new AnimationBuilder(totalTime).setZView(0).setIsNoZaxis(true).
                setCoordinate(0, 2.5f, 0f, -1.5f).build());
        widgetTwo.setZView(-0.0f);

        widgetThree = new BaseThemeExample(totalTime, 1000, 2000, DEFAULT_OUT_TIME * 2, true);
        widgetThree.setStayAction(new AnimationBuilder(totalTime).setZView(0).setIsNoZaxis(true).
                setCoordinate(0, 2.5f, 0f, -1.5f).build());
        widgetThree.setZView(-0.0f);

    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit,mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float centerX = -0.8f;
        float centerY = -1.4f;
        float scale = 3;
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);

        widgetTwo.init(mimaps.get(1), width, height);
        centerX = -0.6f;
        centerY = -1.4f;
        widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);

        widgetThree.init(mimaps.get(2), width, height);
        centerX = 0.7f;
        centerY = -1.4f;
        widgetThree.adjustScaling(width, height, mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scale, scale);
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
    }


}
