package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel5;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 缩小-放大-缩小
 */
public class TravelFiveThemeFour extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;

    public TravelFiveThemeFour(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 4, 0.1f, 1.0f);
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setZView(0);

        //two
        widgetTwo = new BaseThemeExample(totalTime, 500, totalTime - 500, 0);
        widgetTwo.setStayAction(new AnimationBuilder(totalTime - 500).setZView(-2.0f).
                setCoordinate(0, 2.5f, 0f, -1.5f).build());
        widgetTwo.setZView(-2.0f);


        widgetThree = new BaseThemeExample(totalTime, 800, 0, totalTime - 800);
        widgetThree.setOutAnimation(new AnimationBuilder(totalTime - 800).
                setCoordinate(0, 2.5f, 0f, -1.5f).setZView(-2.0f).build());
        widgetThree.setZView(-2.0f);

    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit,mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.setVertex(pos);

        widgetTwo.init(mimaps.get(1), width, height);
        float centerX = -0.7f;
        float centerY = -1.4f;
        float scale = width < height ? 5 : 3;
        widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);

        widgetThree.init(mimaps.get(2), width, height);
        centerX = 0.7f;
        centerY = -1.4f;
        widgetThree.adjustScaling(width, height, mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scale, scale);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
    }
}
