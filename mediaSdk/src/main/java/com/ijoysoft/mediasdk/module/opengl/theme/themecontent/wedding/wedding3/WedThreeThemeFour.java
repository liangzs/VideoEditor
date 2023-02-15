package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding3;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayShakeAnimation;

import java.util.List;

/**
 * @ author       lfj
 * @ date         2020/10/26
 * @ description
 */
public class WedThreeThemeFour extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private WedThreeDrawer wedThreeDrawer;

    public WedThreeThemeFour(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 3, 0.1f, 1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, -2f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.TOP));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 2f, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT));
        this.isNoZaxis = isNoZaxis;

        //渐变
        widgetOne = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 0, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(0, 1).build());
        widgetOne.setZView(0);
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());

        //平移后摇摆
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 1, 0f, 0f).setIsNoZaxis(true).build());
        widgetTwo.setZView(0);
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());
        float centerX = width == height ? 0f : (width < height ? 0f : 0f);
        float centerY = width == height ? 0.75f : (width < height ? 0.82f : 0.7f);
        widgetTwo.setStayAction(new StayShakeAnimation(2500, width, height, centerX, centerY));

        wedThreeDrawer = new WedThreeDrawer();
    }

    @Override
    public void drawWiget() {
        wedThreeDrawer.onDrawFrame();
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);

        float scaleX = width == height ? 1.5f : (width < height ? 1.5f : 2);
        float scaleY = width == height ? 1.5f : (width < height ? 1.5f : 2);
        float centerX = width == height ? -0.4f : (width < height ? -0.35f : -0.5f);
        float centerY = width == height ? -0.65f : (width < height ? -0.8f : -0.55f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleY);

        scaleX = width == height ? 1.5f : (width < height ? 1.2f : 2f);
        scaleY = width == height ? 1.5f : (width < height ? 1.2f : 2f);
        centerX = width == height ? 0f : (width < height ? 0f : 0f);
        centerY = width == height ? 0.75f : (width < height ? 0.82f : 0.7f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleY);


        wedThreeDrawer.init(mimaps.subList(2, 3));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        wedThreeDrawer.onDestroy();
    }
}

