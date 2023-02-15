package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday18;

import android.graphics.Bitmap;
import android.opengl.Matrix;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WaveAnimation;

import java.util.List;

/**
 * 上进，上下悬停，放大，左划出
 */
public class BirthDayEightteenThemeOne extends BDEightteenBaseExample {
    private BaseThemeExample widgetOne;


    public BirthDayEightteenThemeOne(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 2000, 1000, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(2000, AnimateInfo.STAY.SPRING_Y, -2);
        stayNext = new AnimationBuilder(weightTime).setScale(1.0f, 1.2f).setZView(-2).build();
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -2.5f, 0, 0).setZView(-2).build();
        outAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(0, 0f, -2f, 0).setZView(-2).setScale(1.2f, 1.2f));
        setZView(-2);
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 1.5f, 0f, 0f).setZView(-3).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-3).setCoordinate(0, 0f, -2.5f, 0f).build());
    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }

    @Override
    protected void drawStayNextLater() {
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        init(bitmap, width, height);

        float scaleX = width < height ? 1.2f : (width == height ? 1.5f : 2);
        float centerX = 0;
        float centerY = width < height ? 0.98f : (width == height ? 0.88f : 0.8f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);
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
