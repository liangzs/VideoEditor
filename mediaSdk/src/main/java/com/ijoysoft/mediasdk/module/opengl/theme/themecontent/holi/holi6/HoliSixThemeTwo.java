package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi6;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 放大-缩小-放大
 */
public class HoliSixThemeTwo extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;


    public HoliSixThemeTwo(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 2, 0.2f, 1f);
        this.isNoZaxis = isNoZaxis;

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setFade(0, 1).build());

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setCoordinate(0, 1.0f, 0f, 0f)));
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setFade(1, 0).setIsNoZaxis(true).build());
    }


    @Override
    public void drawWiget() {
        super.drawWiget();
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.setVertex(pos);
        float scale = width < height ? 4 : (width == height ? 4 : 4);
        float centerX = 0;
        float centerY = width < height ? 0.8f : (width == height ? 0.8f : 0.8f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }

}
