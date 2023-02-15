package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday9;

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
public class HDNineThemeOne extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;


    public HDNineThemeOne(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.2f, 1.2f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setZView(0);

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME, true);
        widgetTwo.setZView(0);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setFade(0, 1).build());


        widgetThree = new BaseThemeExample(totalTime, 2200, 0, DEFAULT_OUT_TIME);
        widgetThree.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setEnterDelay(1000).setCoordinate(0, -1.0f, 0f, 0f).setZView(-2.4f)));
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setFade(1, 0).setZView(-2.4f).build());
        widgetThree.setZView(-2.4f);

        widgetFour = new BaseThemeExample(totalTime, 2700, 0, DEFAULT_OUT_TIME);
        widgetFour.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setEnterDelay(1500).setCoordinate(0, -1.0f, 0f, 0f).setZView(-2.4f)));
        widgetFour.setZView(-2.4f);
        widgetFour.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setFade(1, 0).setZView(-2.4f).build());

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

        float scale = width < height ? 2 : 1.5f;
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height / 2, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), AnimateInfo.ORIENTATION.BOTTOM, scale);

        scale = width < height ? 4f : 2f;
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width, height / 2, mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), AnimateInfo.ORIENTATION.RIGHT_TOP, scale);
        scale = width < height ? 3f : 1.5f;
        widgetFour.init(mimaps.get(3), width, height);
        float centerX = 0.7f;
        widgetFour.adjustScaling(width, height, mimaps.get(3).getWidth(),
                mimaps.get(3).getHeight(), AnimateInfo.ORIENTATION.TOP, centerX, scale);
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
