package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine1;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 缩小
 * 这里在悬停缩放时，添加滤镜
 */
public class VTOneThemeOne extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;


    public VTOneThemeOne(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.1f, 1.1f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.1f, 1.1f).setIsNoZaxis(true).build();
//        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);
        //widget
        widgetOne = new BaseThemeExample(totalTime, 2000, 2500, DEFAULT_OUT_TIME);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-3).setOrientation(AnimateInfo.ORIENTATION.LEFT).
                setCoordinate(-2, 0.0f, 0f, 0f).setEnterDelay(800)));
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, -2f, 0f).build());
        //widget
        widgetTwo = new BaseThemeExample(totalTime, 2000, 2500, DEFAULT_OUT_TIME);
        widgetTwo.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-3).setOrientation(AnimateInfo.ORIENTATION.LEFT).
                setCoordinate(2, 0f, 0f, 0f).setEnterDelay(800)));
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 2f, 0f).build());
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);

        float scale = width < height ? 2f : (width == height ? 2.5f : 3f);
        float centerX = width < height ? -0.5f : (width == height ? -0.7f : -0.8f);
        float centerY = width < height ? 0.8f : (width == height ? 0.8f : 0.85f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);

        widgetTwo.init(mimaps.get(1), width, height);
        scale = width < height ? 3f : (width == height ? 3 : 2);

        centerX = width < height ? 0.7f : (width == height ? 0.8f : 0.85f);
        centerY = width < height ? 0.8f : (width == height ? 0.8f : 0.8f);
        widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }

}
