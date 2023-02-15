package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine6;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine6.particle.VTSixDrawerTwo;

import java.util.List;

/**
 * stay 缩小
 */
public class VTSixThemeThree extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private VTSixDrawerTwo particleDrawer;


    public VTSixThemeThree(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.1f, 1.1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT).setScale(1.1f, 1.1f));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 0f, 2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;
        particleDrawer = new VTSixDrawerTwo();
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 2000, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.BOTTOM).
                setCoordinate(0, 1.0f, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true)));
//        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).build());
        widgetOne.setZView(0);


        widgetTwo = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).
                setCoordinate(2, 0f, 0f, 0f).build());
        widgetTwo.setZView(-0.0f);

        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).
                setIsNoZaxis(true).setCoordinate(0, 0, 0, 2).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).
                setIsNoZaxis(true).setCoordinate(0, 0, 2, 0).build());

    }

    @Override
    public void drawLast() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        particleDrawer.onDrawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);

        widgetOne.init(mimaps.get(0), width, height);
        float scale = width < height ? 3 : (width == height ? 3f : 3f);
        float centerX = width < height ? -0.5f : (width == height ? -0.5f : -0.5f);
        float centerY = width < height ? 0.65f : (width == height ? 0.6f : 0.5f);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
        widgetTwo.init(mimaps.get(1), width, height);
        scale = width < height ? 2f : (width == height ? 2f : 2.5f);
        centerX = width < height ? 0.3f : (width == height ? 0.3f : 0.5f);
        centerY = width < height ? 0.7f : (width == height ? 0.6f : 0.5f);
        widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);

        particleDrawer.init(mimaps.get(2));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        particleDrawer.onDestroy();
    }

//    @Override
//    public void adjustImageScaling(int width, int height) {
//        adjustImageScalingStretch(width, height);
//    }

}
