package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel13;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * @ author       lfj
 * @ date         2020/10/28
 * @ description
 */
public class TravelThirteenThemeOne extends BaseBlurThemeExample {
    private BaseThemeExample widgetBorder;
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;

    public TravelThirteenThemeOne(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 1, 0.1f, 1.0f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT).setScale(1f, 1f));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0, -2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;

        //border
        widgetBorder = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetBorder.setEnterAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT)));
        widgetBorder.setOutAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0, -2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM)));
        widgetBorder.setZView(0);
        //one 放大
        float centerX = width == height ? -0.78f : (width < height ? -0.6f : -0.8f);
        float centerY = width == height ? 0.73f : (width < height ? 0.73f : 0.63f);
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setZView(0);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setScale(0.01f, 1f).build());
        widgetOne.getEnterAnimation().setIsSubAreaWidget(true, centerX, centerY);
        //two 从左往右平移
        widgetTwo = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 0, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(-1f, 0, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true).build());
        widgetTwo.setZView(0);
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());
        //three 渐变
        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(0, 1).setIsNoZaxis(true).build());
        widgetThree.setZView(0);
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());
        //four 渐变
        widgetFour = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME, true);
        widgetFour.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(0, 1).setIsNoZaxis(true).build());
        widgetFour.setZView(0);
        widgetFour.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());

    }

    @Override
    public void drawWiget() {
        widgetBorder.drawFrame();
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
        widgetFour.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit,mimaps, width, height);
        //边框
        widgetBorder.init(mimaps.get(0), width, height);
        widgetBorder.setVertex(pos);

        //雕像
        float scaleX = width == height ? 4f : (width < height ? 4f : 3f);
        float scaleY = width == height ? 4f : (width < height ? 4f : 3f);
        float centerX = width == height ? -0.78f : (width < height ? -0.6f : -0.8f);
        float centerY = width == height ? 0.73f : (width < height ? 0.73f : 0.63f);
        widgetOne.init(mimaps.get(1), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleY);

        //飞机
        scaleX = width == height ? 6f : (width < height ? 6f : 8f);
        scaleY = width == height ? 6f : (width < height ? 6f : 8f);
        centerX = width == height ? 0.77f : (width < height ? 0.8f : 0.85f);
        centerY = width == height ? -0.72f : (width < height ? -0.8f : -0.72f);
        widgetTwo.init(mimaps.get(2), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleY);

        //左云
        scaleX = width == height ? 10f : (width < height ? 8f : 0);
        scaleY = width == height ? 10f : (width < height ? 8f : 0);
        centerX = width == height ? -0.82f : (width < height ? -0.8f : 0f);
        centerY = width == height ? -0.7f : (width < height ? -0.75f : 0f);
        widgetThree.init(mimaps.get(3), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scaleX, scaleY);

        //右云
        scaleX = width == height ? 10f : (width < height ? 8f : 0);
        scaleY = width == height ? 10f : (width < height ? 8f : 0);
        centerX = width == height ? 0.82f : (width < height ? 0.8f : 0f);
        centerY = width == height ? -0.5f : (width < height ? -0.4f : 0f);
        widgetFour.init(mimaps.get(3), width, height);
        widgetFour.adjustScaling(width, height,
                mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scaleX, scaleY);
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
