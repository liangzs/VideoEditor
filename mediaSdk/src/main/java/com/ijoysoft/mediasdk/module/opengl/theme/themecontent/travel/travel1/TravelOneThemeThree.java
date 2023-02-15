package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel1;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;

import java.util.List;

/**
 * 右上角x轴翻滚进，x反相下方出
 */
public class TravelOneThemeThree extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;

    public TravelOneThemeThree(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, -2f, 0, 0).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 370).setWidthHeight(width, height).build();
        stayAction = new AnimationBuilder(3000).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 10, 10).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 0f, 2f).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 180).build();
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME*2, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setZView(0);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME*2).setIsNoZaxis(true).setZView(0).
                setCoordinate(0, -1, 0, 0).setOrientation(AnimateInfo.ORIENTATION.TOP).setEnterDelay(DEFAULT_ENTER_TIME)));
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(0, 0, 0, -1).build());

        //two
        widgetTwo = new BaseThemeExample(totalTime, 1500, 2500, DEFAULT_OUT_TIME);
        widgetTwo.setEnterAnimation(new AnimationBuilder(1500).setEnterDelay(1000)
                .setFade(0, 1).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).
                setFade(1, 0).build());
        //three
        widgetThree = new BaseThemeExample(totalTime, 1500, 2500, DEFAULT_OUT_TIME);
        widgetThree.setEnterAnimation(new AnimationBuilder(1500).setEnterDelay(1000)
                .setFade(0, 1).build());
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).
                setFade(1, 0).build());

    }

    @Override
    public void drawWiget() {
        widgetThree.drawFrame();
        widgetTwo.drawFrame();
        widgetOne.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scaleX = width < height ? 4f : (width == height ? 4f : 3f);
        float centerX = 0.5f;
        float centerY = width < height ? -0.8f : (width == height ? -0.7f : -0.65f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);


        scaleX = width < height ? 4 : (width == height ? 4 : 4);
        //two
        centerX = width <= height ? cube[2] + 0.3f : cube[2] + 0.1f;
        centerY = cube[3];
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleX);
        //three
        centerX = width <= height ? cube[4] - 0.3f : cube[4] - 0.1f;
        centerY = cube[5];
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleX);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
    }


}
