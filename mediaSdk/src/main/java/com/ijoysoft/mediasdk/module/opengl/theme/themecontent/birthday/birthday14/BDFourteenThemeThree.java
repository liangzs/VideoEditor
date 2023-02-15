package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday14;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday14.particle.BDFourteenDrawerOne;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday14.particle.BDFourteenSystemOne;

import java.util.List;

/**
 * stay 缩小
 */
public class BDFourteenThemeThree extends BaseThemeExample {
    //挤压变形
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;

    private BDFourteenDrawerOne bdFourteenDrawer;

    public BDFourteenThemeThree(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.1f, 1.1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT).setScale(1.1f, 1.1f));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 0f, 2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;
//        starClusterDrawer = new StarClusterDrawer(new BDFiveStarSystemOne());
        bdFourteenDrawer = new BDFourteenDrawerOne(new BDFourteenSystemOne(3500));
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME * 2, 3000, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setFade(0, 1.0f).setIsWidget(true).setIsNoZaxis(true).build());
//        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).build());
        widgetOne.setZView(0);

        //widget
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.BOTTOM).
                setCoordinate(0, -1.0f, 0f, 0f).setIsWidget(true).setIsNoZaxis(true).setEnterDelay(1000).build());
        widgetTwo.setZView(0);

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.BOTTOM).
                setCoordinate(0, -1.0f, 0f, 0f).setIsWidget(true).setIsNoZaxis(true).setEnterDelay(1000).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(0, 0).build());
        widgetTwo.setZView(0);


        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME * 2, 0, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.BOTTOM).
                setCoordinate(0, 1.0f, 0f, 0f).setIsWidget(true).setIsNoZaxis(true).setEnterDelay(1000)));
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(0, 0).build());
        widgetThree.setZView(0);
    }

    @Override
    public void drawWiget() {
        widgetThree.drawFrame();
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        bdFourteenDrawer.onDrawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float[] pos = new float[]{
                -1, 1f,
                -1, 0.8f,
                1, 1f,
                1, 0.8f
        };
        widgetOne.setVertex(pos);

        widgetTwo.init(mimaps.get(1), width, height);
     pos = width < height ? new float[]{
                -1, -0.8f,
                -1, -1,
                1, -0.8f,
                1, -1
        } : (width == height ? new float[]{
                -1, -0.75f,
                -1, -1,
                1, -0.75f,
                1, -1
        } : new float[]{
                -1, -0.7f,
                -1, -1,
                1, -0.7f,
                1, -1
        });
        widgetTwo.setVertex(pos);

        widgetThree.init(mimaps.get(2), width, height);
        float centerX = 0.7f;
        float centerY = width < height ? 0.7f : (width == height ? 0.65f : 0.6f);
        float scale = width < height ? 4 : (width == height ? 4 : 3);
        widgetThree.adjustScaling(width, height, mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scale, scale);
        bdFourteenDrawer.init(mimaps.get(3));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
        if (bdFourteenDrawer != null) {
            bdFourteenDrawer.onDestroy();
        }
    }

    @Override
    public void adjustImageScaling(int width, int height) {
        adjustImageScalingStretch(width, height);
    }
}
