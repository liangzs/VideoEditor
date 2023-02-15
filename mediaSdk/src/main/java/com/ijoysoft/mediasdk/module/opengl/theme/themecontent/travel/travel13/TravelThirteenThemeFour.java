package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel13;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * @ author       lfj
 * @ date         2020/10/28
 * @ description
 */
public class TravelThirteenThemeFour extends BaseBlurThemeExample {
    private BaseThemeExample widgetBorder;
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;

    public TravelThirteenThemeFour(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 3, 0.1f, 1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, -2f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.TOP));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 2f, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT));
        this.isNoZaxis = isNoZaxis;

        //border
        widgetBorder = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetBorder.setEnterAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, -2f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.TOP)));
        widgetBorder.setOutAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 2f, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT)));
        widgetBorder.setZView(0);
        //one 从右往左平移
        widgetOne = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 0, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(1f, 0, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true).build());
        widgetOne.setZView(0);
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());
        //two 上面掉下来弹跳
        widgetTwo = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 0, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setCoordinate(0, -1f, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true)));
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

        //右下角
        float scaleX = width == height ? 4f : (width < height ? 4f : 5f);
        float scaleY = width == height ? 4f : (width < height ? 4f : 5f);
        float centerX = width == height ? 0.8f : (width < height ? 0.75f : 0.85f);
        float centerY = width == height ? 0.86f : (width < height ? 0.93f : 0.8f);
        widgetOne.init(mimaps.get(1), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleY);

        //文案
        scaleX = width == height ? 2f : (width < height ? 1.5f : 3f);
        scaleY = width == height ? 2f : (width < height ? 1.5f : 3f);
        centerX = width == height ? 0f : (width < height ? 0f : 0f);
        centerY = width == height ? -0.82f : (width < height ? -0.8f : -0.85f);
        widgetTwo.init(mimaps.get(2), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleY);

        //左云
        scaleX = width == height ? 0 : (width < height ? 8f : 0);
        scaleY = width == height ? 0 : (width < height ? 8f : 0);
        centerX = width == height ? 0 : (width < height ? -0.8f : 0f);
        centerY = width == height ? 0 : (width < height ? -0.5f : 0f);
        widgetThree.init(mimaps.get(3), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scaleX, scaleY);

        //右云
        scaleX = width == height ? 0f : (width < height ? 8f : 0);
        scaleY = width == height ? 0f : (width < height ? 8f : 0);
        centerX = width == height ? 0 : (width < height ? 0.8f : 0f);
        centerY = width == height ? 0 : (width < height ? -0.7f : 0f);
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
