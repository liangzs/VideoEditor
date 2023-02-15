package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love16;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;


/**
 * 左上角z轴翻滚进，右下角z轴滚出
 */
public class LoveSixteenThemeFour extends BaseThemeExample {
    private BaseThemeExample widgetOne;

    public LoveSixteenThemeFour(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.SPRING_Y, LoveSixteenThemeManager.LOVESIXTEEN_ZVIEW);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, -2f, 0, 0).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 360).setZView(LoveSixteenThemeManager.LOVESIXTEEN_ZVIEW).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 2f, 2f).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 180).setZView(LoveSixteenThemeManager.LOVESIXTEEN_ZVIEW).setWidthHeight(width, height).build();
        setZView(LoveSixteenThemeManager.LOVESIXTEEN_ZVIEW);
    }


    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsWidget(true).
                setCoordinate(-2f, 0f, 0, 0).setFade(-0.5f, 1f).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsWidget(true).setCoordinate(0, 0f, 0, 2f).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).build());


    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        //one
        widgetOne.init(mimaps.get(0), width, height);
        float scale = width <= height ? 8 : 5;
        float centerX = -0.5f;
        float centerY = 0.6f;
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }

}
