package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine15;

import android.graphics.Bitmap;
import android.opengl.Matrix;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WaveAnimation;

import java.util.List;

/**
 * 右边波浪进
 */
public class VTFifteenThemeTwo extends BaseThemeExample {

    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;

    public VTFifteenThemeTwo(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        float zview = width < height ? -2.5f : -2;
        stayAction = new StayScaleAnimation(weightTime, false, 2, 0.1f, 1.1f);
        stayAction.setZView(zview);
        enterAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(2.5f, 0f, 0, 0).setZView(zview).setScale(1.1f, 1.1f));
        outAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(0f, 0f, -2.5f, 0).setZView(zview).setScale(1.1f, 1.1f));
        setZView(zview);

    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, 500, totalTime - 500, 0);
        widgetOne.setStayAction(new AnimationBuilder(totalTime).setZView(-2.0f).
                setCoordinate(0, 2.5f, 0f, -1.5f).build());
        widgetOne.setZView(-2.0f);

        widgetTwo = new BaseThemeExample(totalTime, 2000, 0, totalTime - 2000);
        widgetTwo.setOutAnimation(new AnimationBuilder(totalTime - 1000).
                setCoordinate(0, 2.5f, 0f, -1.5f).setZView(-2.0f).build());
        widgetTwo.setZView(-2.0f);

        //three
        widgetThree = new BaseThemeExample(totalTime, 800, 3000, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new AnimationBuilder(800).setCoordinate(0, 1.5f, 0f, 0f).
                setZView(0).setIsNoZaxis(true).build());
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());
        widgetThree.setZView(0);
        //four
        widgetFour = new BaseThemeExample(totalTime, 2000, 3000, DEFAULT_OUT_TIME, true);
        widgetFour.setEnterAnimation(new AnimationBuilder(1200).setEnterDelay(800).
                setCoordinate(0, 1.5f, 0f, 0f).setZView(0).setIsNoZaxis(true).build());
        widgetFour.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());
        widgetFour.setZView(0);

    }

    @Override
    public void drawWiget() {
        widgetTwo.drawFrame();
        widgetFour.drawFrame();
        widgetThree.drawFrame();
        widgetOne.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        init(bitmap, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        int devideX = width < height ? 1 : 2;
        int devideY = width < height ? 2 : 1;
        float centerX = 0.7f;
        float centerY = -1.4f;
        float scaleX = width < height ? 4 : 8;
        float scaleY = width < height ? 8 : 4;
        widgetOne.adjustScaling(width / devideX, height / devideY, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleY);

        widgetTwo.init(mimaps.get(1), width, height);
        centerX = -0.7f;
        centerY = -1.4f;
        scaleX = width < height ? 2 : 4;
        scaleY = width < height ? 4 : 2;
        widgetTwo.adjustScaling(width / devideX, height / devideY, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleY);

        widgetThree.init(mimaps.get(2), width, height);
        float[] pos = new float[]{
                0.4f, 1.0f,
                0.4f, 0.7f,
                1.0f, 1.0f,
                1.0f, 0.7f,
        };
        if (width > height) {
            pos = new float[]{
                    0.6f, 1.0f,
                    0.6f, 0.4f,
                    1.0f, 1.0f,
                    1.0f, 0.4f,
            };
        }
        widgetThree.setVertex(pos);

        widgetFour.init(mimaps.get(3), width, height);
        pos = new float[]{
                0.6f, 0.8f,
                0.6f, -0.7f,
                1.0f, 0.7f,
                1.0f, -0.7f,
        };
        if (width > height) {
            pos = new float[]{
                    0.7f, 0.8f,
                    0.7f, -1f,
                    1.0f, 0.7f,
                    1.0f, -1f,
            };
        }
        widgetFour.setVertex(pos);
    }


    @Override
    protected void wedThemeSpecialDeal() {
        Matrix.translateM(mViewMatrix, 0, 0f, -0.1f, 0f);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetThree.onDestroy();
        widgetTwo.onDestroy();
        widgetFour.onDestroy();
    }
}
