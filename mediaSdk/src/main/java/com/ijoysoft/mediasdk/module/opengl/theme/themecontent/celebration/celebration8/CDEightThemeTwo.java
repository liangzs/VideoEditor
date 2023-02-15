package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration8;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 放大-缩小-放大
 */
public class CDEightThemeTwo extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    public CDEightThemeTwo(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, 0);
        stayAction = new StayScaleAnimation(2500, true, 3, 0.1f, 1f);
        this.isNoZaxis = isNoZaxis;
        setZView(0);


        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setZView(0);

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setFade(0, 1f).build());
        widgetTwo.setZView(0);
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
        widgetOne.setVertex(pos);
        widgetTwo.init(mimaps.get(1), width, height);
        //文字
        float centerx = 0;
        float centery = width < height ? 0.95f : 0.9f;
        float scaleW = width < height ? 3f : (width == height ? 3 : 4);
        float scaleH = width < height ? 6f : (width == height ? 3 : 2);
        int devideX = width < height ? 1 : (width == height ? 1 : 2);
        int devideY = width < height ? 2 : 1;
        widgetTwo.adjustScaling(width / devideX, height / devideY, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerx, centery, scaleW, scaleH);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }


}
