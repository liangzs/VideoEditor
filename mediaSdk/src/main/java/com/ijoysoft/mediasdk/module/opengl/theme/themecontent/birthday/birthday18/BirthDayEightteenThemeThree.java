package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday18;

import android.graphics.Bitmap;
import android.opengl.Matrix;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WaveAnimation;

import java.util.List;

/**
 * 右边波浪进
 * 添加气球飞起粒子
 * 实际有三个气球
 * <p>
 * //TODO 换成控件方案
 */
public class BirthDayEightteenThemeThree extends BaseThemeExample {
    private BaseThemeExample widgetOne;

    public BirthDayEightteenThemeThree(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new AnimationBuilder(3000).
                setCoordinate(0f, -0.1f, 0f, 0).setZView(-2).build();
        enterAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(2f, 0f, 0, -0.1f).setZView(-2));
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(0f, 0f, 2f, 0).setZView(-2).build();
        setZView(-2);
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, width, height);
        float scaleX = width < height ? 1.2f : (width == height ? 1.5f : 2);
        float centerX = 0;
        float centerY = width < height ? 0.98f : (width == height ? 0.88f : 0.8f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 1.5f, 0f, 0f).setZView(-3).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-3).setCoordinate(0, 0f, 0f, 1.5f).build());
    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }

    @Override
    protected void wedThemeSpecialDeal() {
        super.wedThemeSpecialDeal();
        Matrix.translateM(mViewMatrix, 0, 0f, -0.2f, 0);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }

}
