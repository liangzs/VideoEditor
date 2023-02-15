package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine14;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 放大-缩小
 */
public class VTFourteenThemeFive extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;


    public VTFourteenThemeFive(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 2, 0.2f, 1f);
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setZView(0);
        float centerX = width < height ? -0.7f : -0.8f;
        float centerY = width < height ? 0.8f : 0.7f;
        ;
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setZView(0);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setScale(0.01f, 1f).build());
        widgetTwo.getEnterAnimation().setIsSubAreaWidget(true, centerX, centerY);
        //three
        centerX = width < height ? 0.8f : 0.8f;
        centerY = width < height ? -0.8f : -0.7f;
        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetThree.setZView(0);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setScale(0.01f, 1f).build());
        widgetThree.getEnterAnimation().setIsSubAreaWidget(true, centerX, centerY);
        //four-text
        widgetFour = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetFour.setZView(0);
        widgetFour.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(2, 0, 0, 0).build());
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.setVertex(pos);

        int devideX = width < height ? 1 : 2;
        int devideY = width < height ? 2 : 1;
        float scaleX = width < height ? 5 : 10;
        float scaleY = width < height ? 10 : 5;
        float centerX = width < height ? -0.7f : -0.8f;
        float centerY = width < height ? 0.8f : 0.7f;
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width / devideX, height / devideY,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleY);
        centerX = width < height ? 0.8f : 0.8f;
        centerY = width < height ? -0.8f : -0.7f;

        scaleX = width < height ? 3 : 6;
        scaleY = width < height ? 6 : 3;
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width / devideX, height / devideY,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleY);

        widgetFour.init(mimaps.get(3), width, height);
        float[] pos = width < height ? new float[]{
                -0.4f, 0.95f,
                -0.4f, 0.8f,
                1.0f, 0.95f,
                1.0f, 0.8f,
        } : (width == height ? new float[]{
                -0.4f, 0.95f,
                -0.4f, 0.75f,
                1.0f, 0.95f,
                1.0f, 0.75f,
        } : new float[]{
                0.0f, 0.95f,
                0.0f, 0.7f,
                1.0f, 0.95f,
                1.0f, 0.7f,
        });
        widgetFour.setVertex(pos);
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
        widgetFour.drawFrame();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
        widgetFour.onDestroy();
    }
}
