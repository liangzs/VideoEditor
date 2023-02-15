package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration7;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;

/**
 * 上进，上下悬停，左划出
 */
public class CDSevenThemeOne extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    public CDSevenThemeOne(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.SPRING_Y);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -2.5f, 0, 0).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 360).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, -2.5f, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 360).build();


        float centerX = width < height ? -0.9f : -0.9f;
        float centerY = 0.9f;
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(0.1f, 1f).setIsNoZaxis(true).setZView(0).build());
        widgetOne.getEnterAnimation().setIsSubAreaWidget(true, centerX, centerY);
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(0, 0, -1, 0).build());
        widgetOne.setZView(0);
        //two
        centerX = width < height ? 0.9f : 0.9f;
        centerY = -0.9f;
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(0.1f, 1f).setIsNoZaxis(true).setZView(0).build());
        widgetTwo.getEnterAnimation().setIsSubAreaWidget(true, centerX, centerY);
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(0, 0, 1, 0).build());
        widgetTwo.setZView(0);
    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scale = width < height ? 2f : (width == height ? 2 : 2);
        float centerX = width < height ? -0.9f : -0.9f;
        float centerY = 0.9f;
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
        //two
        centerX = width < height ? 0.9f : 0.9f;
        centerY = -0.9f;
        widgetTwo.init(mimaps.get(0), width, height);
        widgetTwo.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }

}
