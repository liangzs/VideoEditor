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
 * 右边波浪进
 */
public class BirthDayEightteenThemeFour extends BDEightteenBaseExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;

    public BirthDayEightteenThemeFour(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 2000, 1000, DEFAULT_OUT_TIME);
        stayAction = new AnimationBuilder(2000).setScale(1.1f, 1.0f).setZView(-2).build();
        stayNext = new StayAnimation(weightTime, AnimateInfo.STAY.SPRING_Y, -2, true);
        enterAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setScale(1.1f, 1.1f).
                setCoordinate(-2f, 0f, 0f, 0f).setZView(-2));
        //从0.2开始是因为staynext的最后计算是0.2
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(0f, 0.2f, 0f, 2f).setZView(-2).build();
        setZView(-2);
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        init(bitmap, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float centerX = -0.7f;
        float centerY = -1.4f;
        widgetOne.adjustScaling((int) (width), (int) (height / 2), mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, 2, 4);

        widgetTwo.init(mimaps.get(1), width, height);
        centerX = -0.8f;
        centerY = -1.4f;
        widgetTwo.adjustScaling((int) (width), (int) (height / 2), mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, 2, 4);

        widgetThree.init(mimaps.get(2), width, height);
        centerX = 0.8f;
        centerY = -1.4f;
        widgetThree.adjustScaling((int) (width), (int) (height / 2), mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, 2, 4);
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, 0, 2000, DEFAULT_OUT_TIME, true);
        widgetOne.setStayAction(new AnimationBuilder(totalTime).setZView(0).setIsNoZaxis(true).
                setCoordinate(0, 2.5f, 0f, -1.5f).build());
        widgetOne.setZView(-0.0f);

        widgetTwo = new BaseThemeExample(totalTime, 1200, 2000, DEFAULT_OUT_TIME, true);
        widgetTwo.setStayAction(new AnimationBuilder(totalTime).setZView(0).setIsNoZaxis(true).
                setCoordinate(0, 2.5f, 0f, -1.5f).build());
        widgetTwo.setZView(-0.0f);

        widgetThree = new BaseThemeExample(totalTime, 1700, 2000, DEFAULT_OUT_TIME, true);
        widgetThree.setStayAction(new AnimationBuilder(totalTime).setZView(0).setIsNoZaxis(true).
                setCoordinate(0, 2.5f, 0f, -1.5f).build());
        widgetThree.setZView(-0.0f);
    }


    @Override
    protected void drawFramePre() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
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
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
    }
}
