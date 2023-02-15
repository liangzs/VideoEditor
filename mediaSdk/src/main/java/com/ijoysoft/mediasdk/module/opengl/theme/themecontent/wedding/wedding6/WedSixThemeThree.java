package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding6;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * @ author       lfj
 * @ date         2020/10/27
 * @ description
 */
public class WedSixThemeThree extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;
    private BaseThemeExample widgetFive;

    public WedSixThemeThree(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 1, 0.2f, 1f);
        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f).setIsNoZaxis(true).build();
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

        //three 放大
        float centerX = width == height ? 0f : (width < height ? 0f : -0.5f);
        float centerY = width == height ? -0.8f : (width < height ? -0.9f : -0.8f);
        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetThree.setZView(0);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setScale(0.01f, 1f).build());
        widgetThree.getEnterAnimation().setIsSubAreaWidget(true, centerX, centerY);

        //four 放大
        centerX = width == height ? 0.8f : (width < height ? 0.8f : 0.8f);
        centerY = width == height ? -0.8f : (width < height ? -0.9f : -0.8f);
        widgetFour = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetFour.setZView(0);
        widgetFour.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setScale(0.01f, 1f).build());
        widgetFour.getEnterAnimation().setIsSubAreaWidget(true, centerX, centerY);

        //five
        //上面掉下来弹跳
        widgetFive = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 0, DEFAULT_OUT_TIME, true);
        widgetFive.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setCoordinate(0, -1f, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true)));
        widgetFive.setZView(0);
        widgetFive.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
        widgetFour.drawFrame();
        widgetFive.drawFrame();
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

        float scaleX = width == height ? 3f : (width < height ? 3f : 3f);
        float scaleY = width == height ? 3f : (width < height ? 3f : 3f);
        float centerX = width == height ? 0f : (width < height ? 0f : -0.5f);
        float centerY = width == height ? -0.8f : (width < height ? -0.9f : -0.8f);
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleY);

        scaleX = width == height ? 5f : (width < height ? 5f : 3f);
        scaleY = width == height ? 5f : (width < height ? 5f : 3f);
        centerX = width == height ? 0.8f : (width < height ? 0.8f : 0.8f);
        centerY = width == height ? -0.8f : (width < height ? -0.9f : -0.8f);
        widgetFour.init(mimaps.get(3), width, height);
        widgetFour.adjustScaling(width, height,
                mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scaleX, scaleY);

        scaleX = width == height ? 2f : (width < height ? 1.5f : 2.5f);
        scaleY = width == height ? 2f : (width < height ? 1.5f : 2.5f);
        centerX = width == height ? 0f : (width < height ? 0f : 0f);
        centerY = width == height ? 0.9f : (width < height ? 0.9f : 0.8f);
        widgetFive.init(mimaps.get(4), width, height);
        widgetFive.adjustScaling(width, height,
                mimaps.get(4).getWidth(), mimaps.get(4).getHeight(), centerX, centerY, scaleX, scaleY);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
        widgetFour.onDestroy();
        widgetFive.onDestroy();
    }
}
