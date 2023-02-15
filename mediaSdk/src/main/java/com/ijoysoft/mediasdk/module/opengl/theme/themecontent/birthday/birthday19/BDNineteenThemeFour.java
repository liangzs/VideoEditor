package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday19;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;

import java.util.List;

/**
 * 左上角z轴翻滚进，右下角z轴滚出
 */
public class BDNineteenThemeFour extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private final int endConorOffset = 10;

    public BDNineteenThemeFour(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new AnimationBuilder(3000).setWidthHeight(width, height).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, endConorOffset, endConorOffset).setZView(BDNineteenThemeManager.Z_VIEW).build();
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(BDNineteenThemeManager.Z_VIEW).setCoordinate(0, BDNineteenThemeManager.Z_VIEW, 0, 0).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 360).setZView(BDNineteenThemeManager.Z_VIEW).setBgConor(endConorOffset).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setWidthHeight(width, height).setZView(BDNineteenThemeManager.Z_VIEW).setConors(AnimateInfo.ROTATION.Z_ANXIS, endConorOffset, endConorOffset).build();
    }


    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        widgetOne.setZView(-2.5f);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.5f).setFade(0, 1f).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(1, 0f).setZView(-2.5f).build());

    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scaleX = width < height ? 3 : (width == height ? 3 : 4);
        float centerX = width < height ? 0 : (width == height ? 0.3f : 0f);
        float centerY = width < height ? -0.85f : (width == height ? -0.78f : -0.7f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);
    }

    @Override
    public void adjustImageScaling(int width, int height) {
        adjustImageScalingStretch(width, height);
    }


    @Override
    public int getConor() {
        return endConorOffset;
    }

    @Override
    public float[] getPos() {
        return cube;
    }

    @Override
    public void onDestroy() {
        GLES20.glDeleteProgram(mProgram);
        widgetOne.onDestroy();
    }
}
