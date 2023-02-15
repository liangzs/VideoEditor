package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday11;

import android.graphics.Bitmap;
import android.opengl.Matrix;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.ThemeWedStayAnimation;

import java.util.List;

/**
 * 小控件做三次周期运动，0.5s半周期，1s一周期，悬停有3s时间
 */
public class BDElevenThemeFour extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;
    private BaseThemeExample widgetFive;
    private BaseThemeExample widgetSix;
    private BDElevenDrawer bdElevenDrawer;

    public BDElevenThemeFour(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new ThemeWedStayAnimation(3000, -5f);
        stayAction.setZView(-2.5f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 0.0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 180, 0).
                setZView(-2.5f).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 0, -2.5f).setZView(-2.5f).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 0).build();
        setZView(-2.5f);
        bdElevenDrawer = new BDElevenDrawer();
    }

    @Override
    public void drawFrame() {
        super.drawFrame();
        bdElevenDrawer.onDrawFrame();
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetOne.setZView(-2.5f);
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetTwo.setZView(-2.5f);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.5f).
                setFade(0, 1).build());
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.5f).
                setFade(0, 1).build());

        widgetThree = new BaseThemeExample(totalTime, totalTime, 0, 0);
        widgetThree.setEnterAnimation(new AnimationBuilder(totalTime).setZView(-2.5f).
                setCoordinate(0, 2.5f, 0f, -1.0f).build());
        widgetThree.setZView(-2.5f);

        //two
        widgetFour = new BaseThemeExample(totalTime, 1000, 0, totalTime - 800);
        widgetFour.setOutAnimation(new AnimationBuilder(totalTime - 1000).
                setCoordinate(0, 2.5f, 0f, -1.0f).setZView(-2.5f).build());
        widgetFour.setZView(-2.5f);

        widgetFive = new BaseThemeExample(totalTime, 2000, 0, totalTime - 1600);
        widgetFive.setOutAnimation(new AnimationBuilder(totalTime - 1000).
                setCoordinate(0, 2.5f, 0f, -1.0f).setZView(-2.5f).build());
        widgetFive.setZView(-2.5f);

        widgetSix = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetSix.setZView(-2.5f);
        widgetSix.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2f, 0f, 0f).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setZView(-2.5f).build());
        widgetSix.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0f, 2f).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setZView(-2.5f).build());
    }

    @Override
    public void drawWiget() {
        widgetSix.drawFrame();
    }

    @Override
    protected void drawFramePre() {
        if (getStatus() == ActionStatus.STAY) {
            widgetOne.drawFrame();
            widgetTwo.drawFrame();
        }
        widgetThree.drawFrame();
        widgetFour.drawFrame();
        widgetFive.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, null, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float[] pos = new float[]{
                -1f, 0f,
                -1f, -1f,
                1f, 0f,
                1f, -1f
        };
        widgetOne.setVertex(pos);

        widgetTwo.init(mimaps.get(1), width, height);
        pos = new float[]{
                -1f, 1f,
                -1f, 0.5f,
                1f, 1f,
                1f, 0.5f
        };
        widgetTwo.setVertex(pos);

        float scaleX = width < height ? 5 : (width == height ? 5 : 5);
        float centerX = width < height ? -0.8f : (width == height ? -0.8f : -0.8f);
        float centerY = width < height ? -1.4f : (width == height ? -1.4f : -1.4f);
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 5 : (width == height ? 5 : 5);
        centerX = width < height ? -0.8f : (width == height ? -0.8f : -0.8f);
        centerY = width < height ? -1.4f : (width == height ? -1.4f : -1.4f);
        widgetFour.init(mimaps.get(3), width, height);
        widgetFour.adjustScaling(width, height,
                mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 5 : (width == height ? 5 : 5);
        centerX = width < height ? 0.8f : (width == height ? 0.8f : 0.8f);
        centerY = width < height ? -1.4f : (width == height ? -1.4f : -1.4f);
        widgetFive.init(mimaps.get(4), width, height);
        widgetFive.adjustScaling(width, height,
                mimaps.get(4).getWidth(), mimaps.get(4).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 2 : (width == height ? 2 : 3);
        centerX = 0;
        centerY = width < height ? 0.95f : (width == height ? 0.95f : 0.95f);
        widgetSix.init(mimaps.get(5), width, height);
        widgetSix.adjustScaling(width, height,
                mimaps.get(5).getWidth(), mimaps.get(5).getHeight(), centerX, centerY, scaleX, scaleX);

        bdElevenDrawer.init(mimaps.get(6));
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
        widgetFour.onDestroy();
        widgetFive.onDestroy();
        widgetSix.onDestroy();
        bdElevenDrawer.onDestroy();
    }
}
