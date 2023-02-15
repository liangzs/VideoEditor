package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel9;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;

import java.util.List;

/**
 * 一套组合，一个组合有三个显示元素，三者不一样的进场
 */
public class TravelNineThemeOne extends BaseThemeExample {
    private final int endConorOffset = -20;
    private final int endConor = 340;
    private BaseThemeExample widgetOne;

    public TravelNineThemeOne(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_ENTER_TIME);
        stayAction = new AnimationBuilder(3000).setWidthHeight(width, height).
                setZView(TravelNineThemeManager.Z_VIEW).setConors(AnimateInfo.ROTATION.Z_ANXIS, endConor, endConor).build();
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(TravelNineThemeManager.Z_VIEW).setCoordinate(0, TravelNineThemeManager.Z_VIEW, 0, 0).
                setConors(AnimateInfo.ROTATION.X_ANXIS, 0, 360).setZView(TravelNineThemeManager.Z_VIEW).setBgConor(endConorOffset).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setWidthHeight(width, height).setZView(TravelNineThemeManager.Z_VIEW).setConors(AnimateInfo.ROTATION.Z_ANXIS, endConor, endConor).build();
    }


    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime - DEFAULT_ENTER_TIME, DEFAULT_ENTER_TIME, 3000, DEFAULT_ENTER_TIME, true);
        widgetOne.setZView(0);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(0f, -1, 0, 0).setOrientation(AnimateInfo.ORIENTATION.TOP)));
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(0, 0, 0, -1).build());
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float centerX = width < height ? 0.65f : (width == height ? 0.7f : 0.65f);
        float centerY = width < height ? -0.7f : (width == height ? -0.65f : -0.6f);
        float scaleX = width < height ? 4 : (width == height ? 4 : 4);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);
    }

    @Override
    public void adjustImageScaling(int width, int height) {
        adjustImageScalingStretch(width, height);
    }

    @Override
    public float[] getPos() {
        return cube;
    }

    @Override
    public int getConor() {
        return endConorOffset;
    }

    @Override
    public void onDestroy() {
        GLES20.glDeleteProgram(mProgram);
        widgetOne.onDestroy();
    }
}
