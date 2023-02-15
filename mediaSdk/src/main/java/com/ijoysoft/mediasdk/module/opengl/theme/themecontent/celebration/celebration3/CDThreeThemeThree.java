package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration3;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;

/**
 * 右上角x轴翻滚进，x反相下方出
 */
public class CDThreeThemeThree extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    public CDThreeThemeThree(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, -2f, 0, 0).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 360).build();
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.ROTATE_X);
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 0f, 2f).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 180).build();
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 1, 0f, 0f).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0, 0, 1f).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setZView(0);
        //two
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, -1, 0f, 0f).setIsNoZaxis(true).setZView(0).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0, 0, -1f).setIsNoZaxis(true).setZView(0).build());
        widgetTwo.setZView(0);
        ;


    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scale = width < height ? 2f : (width == height ? 1.5f : 1.2f);
        float centerX = width < height ? 0.6f : 0.5f;
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), AnimateInfo.ORIENTATION.BOTTOM, centerX, scale);
        //two
        scale = width < height ? 4f : (width == height ? 6 : 6);
        centerX = -0.4f;
        float centerY = -0.8f;
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }
}
