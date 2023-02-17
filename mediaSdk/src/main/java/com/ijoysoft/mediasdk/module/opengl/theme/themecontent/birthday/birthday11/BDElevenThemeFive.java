package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday11;

import android.graphics.Bitmap;
import android.opengl.Matrix;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.ThemeWedStayAnimation;

import java.util.List;

public class BDElevenThemeFive extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;

    public BDElevenThemeFive(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new ThemeWedStayAnimation(3000, -5f);
        stayAction.setZView(-2.5f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 0.0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 180, 0).
                setZView(-2.5f).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 0, 2.5f).setZView(-2.5f).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 0).build();
        setZView(-2.5f);
    }


    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, totalTime, 0, 0);
        widgetOne.setEnterAnimation(new AnimationBuilder(totalTime).setZView(-2.5f).
                setCoordinate(0, 2.5f, 0f, -1.0f).build());
        widgetOne.setZView(-2.5f);

        //two
        widgetTwo = new BaseThemeExample(totalTime, 1500, 0, totalTime);
        widgetTwo.setOutAnimation(new AnimationBuilder(totalTime).
                setCoordinate(0, 2.5f, 0f, -1.0f).setZView(-2.5f).build());
        widgetTwo.setZView(-2.5f);

        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetThree.setZView(-2.5f);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2f, 0f, 0f).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setZView(-2.5f).build());
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2f, 0f, 0f).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setZView(-2.5f).build());
    }

    @Override
    public void drawWiget() {
        widgetThree.drawFrame();
    }

    @Override
    protected void drawFramePre() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, null, mimaps, width, height);

        float scaleX = width < height ? 5 : (width == height ? 5 : 5);
        float centerX = width < height ? -0.8f : (width == height ? -0.8f : -0.8f);
        float centerY = width < height ? -1.4f : (width == height ? -1.4f : -1.4f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 5 : (width == height ? 5 : 5);
        centerX = width < height ? 0.8f : (width == height ? 0.8f : 0.8f);
        centerY = width < height ? -1.4f : (width == height ? -1.4f : -1.4f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 2 : (width == height ? 2 : 3);
        centerX = 0;
        centerY = width < height ? 0.95f : (width == height ? 0.95f : 0.95f);
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleX);
    }


    @Override
    protected void wedThemeSpecialDeal() {
        super.wedThemeSpecialDeal();
        Matrix.translateM(mViewMatrix, 0, 0f, -0.02f, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
    }
}