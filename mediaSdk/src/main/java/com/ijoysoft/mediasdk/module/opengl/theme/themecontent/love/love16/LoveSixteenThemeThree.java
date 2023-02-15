package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love16;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;


/**
 * 右上角x轴翻滚进，x反相下方出
 */
public class LoveSixteenThemeThree extends BaseThemeExample {
    private BaseThemeExample widgetOne;

    public LoveSixteenThemeThree(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, -2f, 0, 0).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 360).setZView(LoveSixteenThemeManager.LOVESIXTEEN_ZVIEW).build();
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.ROTATE_X, LoveSixteenThemeManager.LOVESIXTEEN_ZVIEW);
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 0f, 2f).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 180).setZView(LoveSixteenThemeManager.LOVESIXTEEN_ZVIEW).build();
        setZView(LoveSixteenThemeManager.LOVESIXTEEN_ZVIEW);
    }


    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsWidget(true).setCoordinate(-1, 0, 0, 0).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setIsWidget(true).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsWidget(true).setCoordinate(0, 0, -1, 0).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setIsWidget(true).build());
        widgetOne.setZView(-2);

    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float centerX = -0.5f;
        float centerY = 0.6f;
        float scale = width <= height ? 8 : 6;
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }

}
