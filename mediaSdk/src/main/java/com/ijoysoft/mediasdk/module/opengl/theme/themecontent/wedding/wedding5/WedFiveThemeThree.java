package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding5;

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
 * @ date         2020/10/26
 * @ description
 */
public class WedFiveThemeThree extends BaseBlurThemeExample {
    private BaseThemeExample widgetBorder;
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;

    public WedFiveThemeThree(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.1f, 1.1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT).setScale(1.1f, 1.1f));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 0f, 2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;

        //border
        widgetBorder = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetBorder.setEnterAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT)));
        widgetBorder.setOutAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 0f, 2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM)));
        widgetBorder.setZView(0);

        //one
        float centerX = width == height ? 0f : (width < height ? 0f : 0f);
        float centerY = width == height ? -0.85f : (width < height ? -0.75f : -0.8f);
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setZView(0);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setScale(0.01f, 1f).build());
        widgetOne.getEnterAnimation().setIsSubAreaWidget(true, centerX, centerY);

        //two
        //上面掉下来弹跳
        widgetTwo = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 0, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setCoordinate(0, -1f, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true)));
        widgetTwo.setZView(0);
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());

        //three
        //上面掉下来弹跳
        widgetThree = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 0, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setCoordinate(0, -1f, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true)));
        widgetThree.setZView(0);
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());

    }

    @Override
    public void drawWiget() {
        widgetBorder.drawFrame();
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit,mimaps, width, height);

        widgetBorder.init(mimaps.get(0), width, height);
        widgetBorder.setVertex(pos);

        float scaleX = width == height ? 1.3f : (width < height ? 1f : 3f);
        float scaleY = width == height ? 1.3f : (width < height ? 1f : 3f);
        float centerX = width == height ? 0f : (width < height ? 0f : 0f);
        float centerY = width == height ? -0.85f : (width < height ? -0.75f : -0.8f);
        widgetOne.init(mimaps.get(1), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleY);

        scaleX = width == height ? 10f : (width < height ? 5f : 5f);
        scaleY = width == height ? 10f : (width < height ? 5f : 5f);
        centerX = width == height ? 0.93f : (width < height ? 0.88f : 0.93f);
        centerY = width == height ? 0.8f : (width < height ? 0.89f : 0.6f);
        widgetTwo.init(mimaps.get(2), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleY);

        scaleX = width == height ? 10f : (width < height ? 5f : 5f);
        scaleY = width == height ? 10f : (width < height ? 5f : 5f);
        centerX = width == height ? 0.75f : (width < height ? 0.68f : 0.75f);
        centerY = width == height ? 0.9f : (width < height ? 0.94f : 0.85f);
        widgetThree.init(mimaps.get(3), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scaleX, scaleY);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
        widgetBorder.onDestroy();
    }
}

