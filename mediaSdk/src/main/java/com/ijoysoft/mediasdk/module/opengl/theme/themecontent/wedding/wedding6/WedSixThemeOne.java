package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding6;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * @ author       lfj
 * @ date         2020/10/27
 * @ description
 */
public class WedSixThemeOne extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    public WedSixThemeOne(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.2f, 1.2f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        //one 渐变
        widgetOne = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 2500, 0, DEFAULT_OUT_TIME, true);
        widgetOne.setZView(0);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(0, 1).build());

        //two 渐变
        widgetTwo = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 2500, 0, DEFAULT_OUT_TIME, true);
        widgetTwo.setZView(0);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(0, 1).build());
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit,mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        pos = width == height ? new float[]{
                -1f, -0.7f,
                -1f, -1f,
                1f, -0.7f,
                1f, -1f,
        } : (width < height ? new float[]{
                -1f, -0.8f,
                -1f, -1f,
                1f, -0.8f,
                1f, -1f,
        } : new float[]{
                -1f, -0.5f,
                -1f, -1f,
                1f, -0.5f,
                1f, -1f,
        });
        widgetOne.setVertex(pos);

        widgetTwo.init(mimaps.get(1), width, height);
        pos = width == height ? new float[]{
                -1f, 1f,
                -1f, 0.7f,
                1f, 1f,
                1f, 0.7f,
        } : (width < height ? new float[]{
                -1f, 1f,
                -1f, 0.8f,
                1f, 1f,
                1f, 0.8f,
        } : new float[]{
                -1f, 1f,
                -1f, 0.5f,
                1f, 1f,
                1f, 0.5f,
        });
        widgetTwo.setVertex(pos);

//        float scaleX = width == height ? 1.5f : (width < height ? 1.5f : 2.5f);
//        float scaleY = width == height ? 1.5f : (width < height ? 1.5f : 2.5f);
//        float centerX = width == height ? 0f : (width < height ? 0f : 0f);
//        float centerY = width == height ? 0.7f : (width < height ? 0.7f : 0.6f);
//        widgetThree.init(mimaps.get(2), width, height);
//        widgetThree.adjustScaling(width, height,
//                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleY);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }
}
