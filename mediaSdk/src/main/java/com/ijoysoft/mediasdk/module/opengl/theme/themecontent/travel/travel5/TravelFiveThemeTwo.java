package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel5;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 放大-缩小-放大
 */
public class TravelFiveThemeTwo extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;

    public TravelFiveThemeTwo(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, 0);
        stayAction = new StayScaleAnimation(2500, true, 3, 0.1f, 1f);
        this.isNoZaxis = isNoZaxis;
        setZView(0);


        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setZView(0);

        widgetTwo = new BaseThemeExample(totalTime - DEFAULT_ENTER_TIME, 3000, 0, DEFAULT_ENTER_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(3000).setIsNoZaxis(true)
                .setZView(0).setCoordinate(1, 0, 0, 0).build());
        widgetTwo.setZView(0);

        //three
        widgetThree = new BaseThemeExample(totalTime, 3000, 0, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new AnimationBuilder(3000).setZView(0).setIsNoZaxis(true).setCoordinate(-0.2f, 0, 0, 0).build());
        widgetThree.setZView(0.0f);
        //four
        widgetFour = new BaseThemeExample(totalTime, 3000, 0, DEFAULT_OUT_TIME, true);
        widgetFour.setEnterAnimation(new AnimationBuilder(3000).setZView(0).setIsNoZaxis(true).setCoordinate(-0.2f, 0, 0, 0)
                .build());
        widgetFour.setZView(0.0f);
    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetFour.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit,mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.setVertex(pos);
        widgetTwo.init(mimaps.get(1), width, height);
        float scaleW = width < height ? 4f : (width == height ? 5 : 5);
        float centerx = -0.4f;
        float centery = width < height ? -0.6f : -0.5f;
        widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(),
                mimaps.get(1).getHeight(), centerx, centery, scaleW, scaleW);
        //three
        float centerX = width <= height ? -0.7f : -0.7f;
        float centerY = width < height ? -0.4f : (width == height ? -0.4f : -0.4f);
        float scale = width < height ? 4f : (width == height ? 6 : 8);
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width, height, mimaps.get(2).getWidth(),
                mimaps.get(2).getHeight(), centerX, centerY, scale, scale);

        //four
        scale = width < height ? 6 : (width == height ? 8 : 10);
        centerX = width <= height ? 0.6f : 0.7f;
        centerY = width < height ? -0.65f : (width == height ? -0.6f : -0.55f);
        widgetFour.init(mimaps.get(2), width, height);
        widgetFour.adjustScaling(width, height, mimaps.get(2).getWidth(),
                mimaps.get(2).getHeight(), centerX, centerY, scale, scale);
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
