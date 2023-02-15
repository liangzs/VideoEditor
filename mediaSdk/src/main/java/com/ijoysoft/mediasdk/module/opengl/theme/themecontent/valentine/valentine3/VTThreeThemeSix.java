package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine3;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WaveAnimation;

import java.util.List;

/**
 * 右边波浪进
 */
public class VTThreeThemeSix extends BaseThemeExample {

    private BaseThemeExample widgetOne;

    public VTThreeThemeSix(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(3000, false, 2, 0.1f, 1.1f);
        stayAction.setZView(-3);
        enterAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(0f, -2.5f, 0f, 0).setScale(1.1f, 1.1f).build();
        outAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(0f, 0f, 0f, 2.5f));

    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        init(bitmap, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        //顶点坐标
        widgetOne.init(mimaps.get(0), width, height);
        float centerX = width <= height ? 0 : 0.5f;
        float scale = width < height ? 1.5f : (width == height ? 1.5f : 1.5f);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), AnimateInfo.ORIENTATION.BOTTOM, centerX, scale);
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(0, 1.5f, 0f, 0f).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(0, 0f, 0f, 1.5f).build());
        widgetOne.setZView(0);
    }


    @Override
    protected void wedThemeSpecialDeal() {
        super.wedThemeSpecialDeal();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }
}
