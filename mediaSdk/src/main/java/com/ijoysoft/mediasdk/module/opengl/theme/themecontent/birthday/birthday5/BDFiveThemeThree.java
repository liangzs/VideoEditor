package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday5;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday5.startcluster.BDFiveStarSystemOne;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday5.startcluster.StarClusterDrawer;

import java.util.List;

/**
 * stay 缩小
 */
public class BDFiveThemeThree extends BaseThemeExample {
    //挤压变形
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    private float[] ballonYs;
    private float[] ballonXs;
    private float ballonCenter;
    private int ballonCount;
    private StarClusterDrawer starClusterDrawer;
    private int width, height;

    public BDFiveThemeThree(int totalTime, int width, int height, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.1f, 1.1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT).setScale(1.1f, 1.1f));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 0f, 2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;
        starClusterDrawer = new StarClusterDrawer(new BDFiveStarSystemOne());
        this.width = width;
        this.height = height;
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME * 2, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME * 2).setFade(1, 0).
                setIsWidget(true).setIsNoZaxis(true).build());
        widgetOne.setZView(0);

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME)
                .setCoordinate(0, 1f, 0f, 0f).setFade(0, 1f).setIsWidget(true).setIsNoZaxis(true).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsWidget(true).setIsNoZaxis(true).setCoordinate(
                0, 0, 0, 1).build());
        widgetTwo.setZView(0);
    }

    @Override
    public void drawWiget() {
        if (frameIndex < ballonCount) {
            ballonCenter = ballonYs[frameIndex];
            float[] temp = width < height ? new float[]{
                    ballonXs[frameIndex] - 0.3f, ballonCenter + 0.2f,
                    ballonXs[frameIndex] - 0.3f, ballonCenter - 0.2f,
                    ballonXs[frameIndex] + 0.3f, ballonCenter + 0.2f,
                    ballonXs[frameIndex] + 0.3f, ballonCenter - 0.2f
            } : (width == height ? new float[]{
                    ballonXs[frameIndex] - 0.3f, ballonCenter + 0.3f,
                    ballonXs[frameIndex] - 0.3f, ballonCenter - 0.3f,
                    ballonXs[frameIndex] + 0.3f, ballonCenter + 0.3f,
                    ballonXs[frameIndex] + 0.3f, ballonCenter - 0.3f
            } : new float[]{
                    ballonXs[frameIndex] - 0.2f, ballonCenter + 0.3f,
                    ballonXs[frameIndex] - 0.2f, ballonCenter - 0.3f,
                    ballonXs[frameIndex] + 0.2f, ballonCenter + 0.3f,
                    ballonXs[frameIndex] + 0.2f, ballonCenter - 0.3f
            });
            widgetOne.setVertex(temp);
        }
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        starClusterDrawer.onDrawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        //one
        float scaleX = width < height ? 2 : (width == height ? 3 : 2);
        float centerX = width < height ? 0.5f : (width == height ? 0.5f : 0.6f);
        float centerY = width < height ? -0.85f : (width == height ? -0.8f : -0.8f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 4 : (width == height ? 3 : 2);
        centerX = width < height ? -0.7f : (width == height ? -0.8f : -0.8f);
        centerY = width < height ? 0.8f : (width == height ? 0.7f : 0.5f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleX);

        evaluateShade();
    }

    /**
     * 计算气球的滑动轨迹
     */
    private void evaluateShade() {
        ballonCount = DEFAULT_ENTER_TIME * 2 * ConstantMediaSize.FPS / 1000;
        ballonYs = new float[ballonCount];
        ballonXs = new float[ballonCount];
        float frameValue = 0;
        float x;
        for (int i = 0; i < ballonCount; i++) {
            x = i * 1.0F / (ballonCount - 1);
            frameValue = (float) Math.sqrt(1 - Math.pow(x - 1, 2)) * -2 + 1.1f;
            ballonYs[i] = frameValue;
        }
        for (int i = 0; i < ballonCount; i++) {
            ballonXs[i] = i * 2.0F / (ballonCount - 1) - 1.1f;
        }
    }

    @Override
    public void adjustImageScaling(int width, int height) {
        adjustImageScalingStretch(width, height);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        starClusterDrawer.onDestroy();
    }
}
