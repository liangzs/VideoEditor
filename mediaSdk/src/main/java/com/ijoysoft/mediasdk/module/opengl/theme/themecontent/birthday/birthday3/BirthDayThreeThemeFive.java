package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday3;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WaveAnimation;

import java.util.List;

/**
 * 右边波浪进
 */
public class BirthDayThreeThemeFive extends BDThreeBaseExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    public BirthDayThreeThemeFive(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2000, 1000, DEFAULT_OUT_TIME);
        stayAction = new AnimationBuilder(2000).setScale(1.1f, 1.0f).setZView(-2).build();
        stayNext = new StayAnimation(weightTime, AnimateInfo.STAY.SPRING_Y, -2, true);
        enterAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setScale(1.1f, 1.1f).
                setCoordinate(-2f, 0f, 0f, 0f).setZView(-2));
        //从0.2开始是因为staynext的最后计算是0.2
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(0f, 0.2f, 0f, 2f).setZView(-2).build();
        setZView(-2);


        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME * 2, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.BOTTOM).
                setCoordinate(0, 1.0f, 0f, 0f).setEnterDelay(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true)));
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-3).setCoordinate(0, 0, 0, 1f).build());
        widgetOne.setZView(0);

        widgetTwo = new BaseThemeExample(totalTime - 1000, 800, 3000, 800, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(800).setScale(0.1f, 1f).setIsNoZaxis(true).setZView(0).build());
        float centerY = width < height ? 0.7f : 0.5f;
        widgetTwo.getEnterAnimation().setIsSubAreaWidget(true, 0.5f, centerY);
        widgetTwo.setOutAnimation(new AnimationBuilder(800).setZView(0).setIsNoZaxis(true).setCoordinate(0, 0, 0, 1f).build());
        widgetTwo.setZView(0);
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        init(bitmap, width, height);
        widgetOne.init(mimaps.get(0), width, height);

        float scale = width < height ? 3 : (width == height ? 4 : 3f);
        float centerX = width < height ? -0.65f : (width == height ? -0.5f : -0.7f);
        float centerY = width < height ? 0.8f : 0.6f;
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
        //two
        widgetTwo.init(mimaps.get(1), width, height);
        scale = width < height ? 3 : (width == height ? 3 : 2f);
        centerX = width < height ? 0.5f : 0.5f;
        centerY = width < height ? 0.7f : (width == height ? 0.6f : 0.5f);
        widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);
    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }

    @Override
    public String printOutString() {
        return this.getClass().getName() + ":getTexture:" + getTextureId() + ",wighet_one:" + widgetOne.getTexture()
                + ",widgetTwo:" + widgetTwo.getTexture();
    }
}
