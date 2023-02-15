package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday1;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;

import static com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday1.BDOneThemeManager.BD_ONE_ZVIEW;

/**
 * 上进，上下悬停，左划出
 */
public class BirthDayActionOne extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;

    public BirthDayActionOne(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(totalTime - DEFAULT_ENTER_TIME - DEFAULT_OUT_TIME, AnimateInfo.STAY.SPRING_Y);
        stayAction.setZView(BD_ONE_ZVIEW);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -2.5f, 0, 0).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 360).setZView(BD_ONE_ZVIEW).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, -2.5f, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 360).setZView(BD_ONE_ZVIEW).build();
        setZView(BD_ONE_ZVIEW);
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(-1, 0, 0f, 0f).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(0, 0f, -1, 0f).build());
        widgetOne.setZView(0);

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(1, 0, 0f, 0f).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(0, 0f, 1, 0f).build());
        widgetTwo.setZView(0);

        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setFade(0f, 1f).build());
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setFade(1, 0).build());
        widgetThree.setZView(0);

    }

    @Override
    public void drawWiget() {
        widgetThree.drawFrame();
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);

        float scale = width < height ? 3 : (width == height ? 3 : 3);
        float centerX = width < height ? -0.7f : (width == height ? -0.7f : -0.7f);
        float centerY = width < height ? 0.7f : (width == height ? 0.7f : 0.6f);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
        //two
        widgetTwo.init(mimaps.get(1), width, height);
        centerX = width < height ? 0.7f : (width == height ? 0.7f : 0.7f);
        widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);

        //three
        widgetThree.init(mimaps.get(2), width, height);
        scale = width < height ? 2 : (width == height ? 2 : 2);
        centerX = 0;
        centerY = width < height ? 0.8f : (width == height ? 0.8f : 0.8f);
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
