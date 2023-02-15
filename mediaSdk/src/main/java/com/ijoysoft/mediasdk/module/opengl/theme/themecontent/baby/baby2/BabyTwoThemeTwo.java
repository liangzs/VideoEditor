package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby2;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayShakeAnimation;

import java.util.List;

/**
 * stay 放大-缩小-放大
 */
public class BabyTwoThemeTwo extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;

    public BabyTwoThemeTwo(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 2, 0.2f, 1f);
//        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(0, 1).setIsNoZaxis(true).setZView(0).build();
//        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.1f, 1.1f).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setZView(0);


        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setZView(0);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(0, 1, 0, 0).build());
        //three
        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetThree.setZView(0);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).
                setZView(0).setCoordinate(-1, 0, 0, 0).build());
        //four-text
        widgetFour = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetFour.setZView(0);
        widgetFour.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(0, -1, 0, 0).build());
        float centerX = width < height ? 0.7f : 0.8f;
        float centerY = width < height ? -0.9f : -0.8f;
        widgetFour.setStayAction(new StayShakeAnimation(2500, width, height, centerX, centerY, 5, 0));
    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
        widgetFour.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit,mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.setVertex(pos);

        int devideX = width < height ? 1 : 2;
        int devideY = width < height ? 2 : 1;
        float scaleX = width < height ? 2 : 4;
        float scaleY = width < height ? 4 : 2;
        float centerX = width < height ? 0.3f : 0f;
        float centerY = width < height ? 0.8f : 0.7f;
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width / devideX, height / devideY,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleY);
        centerX = width < height ? -0.5f : -0.6f;
        centerY = width < height ? 0.7f : 0.5f;
        scaleX = width < height ? 2 : 4;
        scaleY = width < height ? 4 : 2;
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width / devideX, height / devideY,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleY);

        widgetFour.init(mimaps.get(3), width, height);
        centerX = width < height ? 0.7f : 0.8f;
        centerY = width < height ? -0.9f : -0.8f;
        scaleX = width < height ? 5 : 10;
        scaleY = width < height ? 12 : 5;
        widgetFour.adjustScaling(width / devideX, height / devideY,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleY);
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
