package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine5;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine5.particle.VTFiveDrawerTwo;

import java.util.List;

/**
 * 缩小-放大-缩小
 */
public class VTFiveThemeSix extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private VTFiveDrawerTwo vtFiveDrawer;

    public VTFiveThemeSix(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 1, 0.2f, 1.0f);
        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime - 500, DEFAULT_ENTER_TIME, 1500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -1f, 0, 0).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-0).setIsNoZaxis(true).setFade(1, 0f).build());
        widgetOne.setZView(0);
        vtFiveDrawer = new VTFiveDrawerTwo();
    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        vtFiveDrawer.onDrawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit,mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float[] pos = width < height ? new float[]{
                -1f, -0.8f,
                -1f, -1f,
                1f, -0.8f,
                1f, -1f,
        } : (width == height ? new float[]{
                -1f, -0.65f,
                -1f, -1f,
                1f, -0.65f,
                1f, -1f,
        } : new float[]{
                -1f, -0.65f,
                -1f, -1f,
                1f, -0.65f,
                1f, -1f,
        });
        widgetOne.setVertex(pos);
        vtFiveDrawer.init(mimaps.subList(1, mimaps.size()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        vtFiveDrawer.onDestroy();
    }
}
