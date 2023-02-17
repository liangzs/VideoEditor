package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love10;

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
public class LoveTenThemeSix extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;

    public LoveTenThemeSix(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.2f, 1.2f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setCoordinate(0, -1f, 0f, 0f).setIsNoZaxis(true)));
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME + 500, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setCoordinate(0, -1f, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true)));
        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME + 1000, 2500, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setCoordinate(0, -1f, 0f, 0f).setEnterDelay(1000).setIsNoZaxis(true)));
        widgetFour = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME + 1000, 2500, DEFAULT_OUT_TIME, true);
        widgetFour.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME + 1000).setScale(0.01f, 1)
                .setStartX(0).setStartY(1).setIsNoZaxis(true).build());
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
        super.init(bitmap, tempBit, mimaps, width, height);

        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(),
                AnimateInfo.ORIENTATION.TOP, -0.6f, 2);

        widgetTwo.init(mimaps.get(0), width, height);
        widgetTwo.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(),
                0, -0.8f, 4, 4);

        widgetThree.init(mimaps.get(0), width, height);
        widgetThree.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(),
                AnimateInfo.ORIENTATION.TOP, 0.6f, 2);

        widgetFour.init(mimaps.get(1), width, height);
        widgetFour.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(),
                AnimateInfo.ORIENTATION.BOTTOM, 0, 1.5f);
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