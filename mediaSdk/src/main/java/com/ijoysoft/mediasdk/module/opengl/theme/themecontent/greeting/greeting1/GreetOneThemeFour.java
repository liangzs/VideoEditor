package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting1;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * @ author       lfj
 * @ date         2020/10/14
 * @ description
 */
public class GreetOneThemeFour extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;

    public GreetOneThemeFour(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.2f, 1.2f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setZView(0);

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setZView(0);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(-1f, 0, 0, 0).build());

        if (width < height) {//9:16时多一个控件
            //three
            widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
            widgetThree.setZView(0);
            widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                    setCoordinate(-1f, 0, 0, 0).build());
        }
        //four
        widgetFour = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetFour.setZView(0);
        widgetFour.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(1f, 0, 0, 0).build());
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        if (widgetThree != null) {
            widgetThree.drawFrame();
        }
        widgetFour.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.setVertex(pos);

        //图片
        float scaleX = width == height ? 6 : (width < height ? 4 : 5);
        float scaleY = width == height ? 6 : (width < height ? 4 : 5);
        float centerX = width == height ? 0.65f : (width < height ? 0.8f : -0.4f);
        float centerY = width == height ? 0.5f : (width < height ? 0.9f : 0.7f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleY);

        float[] pos;
        //
        if (widgetThree != null) {
            widgetThree.init(mimaps.get(2), width, height);
            //顶点坐标
            pos = width == height ? new float[]{
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
            } : (width < height ? new float[]{
                    -0.5f, 0.8f,
                    -0.5f, 0.6f,
                    0.5f, 0.8f,
                    0.5f, 0.6f,
            } : new float[]{
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
            });
            widgetThree.setVertex(pos);
        }
        //
        widgetFour.init(mimaps.get(3), width, height);
        //顶点坐标
        pos = width == height ? new float[]{
                -0.4f, 0.9f,
                -0.4f, 0.8f,
                0.4f, 0.9f,
                0.4f, 0.8f,
        } : (width < height ? new float[]{
                -0.35f, 0.75f,
                -0.35f, 0.65f,
                0.35f, 0.75f,
                0.35f, 0.65f,
        } : new float[]{
                -0.2f, 0.95f,
                -0.2f, 0.8f,
                0.4f, 0.95f,
                0.4f, 0.8f,
        });
        widgetFour.setVertex(pos);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        if (widgetThree != null) {
            widgetThree.onDestroy();
        }
        widgetFour.onDestroy();
    }

}
