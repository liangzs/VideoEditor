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
 * stay 放大-缩小
 */
public class BDFourteenThemeFive extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BDFourteenDrawerOne bdFourteenDrawer;

    public BDFourteenThemeFive(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 2, 0.1f, 1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT));

        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 2f, 0f).
                setOrientation(AnimateInfo.ORIENTATION.LEFT));


        this.isNoZaxis = isNoZaxis;
        bdFourteenDrawer = new BDFourteenDrawerOne(new BDFourteenSystemOne(3500));
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(0, 1f).setIsWidget(true).setIsNoZaxis(true).build());
        widgetOne.setZView(0);

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME * 2, 3000, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.BOTTOM).
                setCoordinate(0, 1.0f, 0f, 0f).setIsWidget(true).setIsNoZaxis(true).setEnterDelay(1000)));
        widgetTwo.setZView(0);

    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        //one
        widgetOne.init(mimaps.get(0), width, height);
        pos = new float[]{
                -1, 1,
                -1, 0.8f,
                1, 1,
                1, 0.8f,
        };
        widgetOne.setVertex(pos);

        widgetTwo.init(mimaps.get(1), width, height);
        pos = width < height ? new float[]{
                -1f, 0.8f,
                -1f, 0.6f,
                -0.5f, 0.8f,
                -0.5f, 0.6f
        } : (width == height ? new float[]{
                -1f, 0.8f,
                -1f, 0.5f,
                -0.5f, 0.8f,
                -0.5f, 0.5f
        } : new float[]{
                -1f, 0.8f,
                -1f, 0.4f,
                -0.7f, 0.8f,
                -0.7f, 0.4f
        });
        widgetTwo.setVertex(pos);
        bdFourteenDrawer.init(mimaps.get(2));
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        bdFourteenDrawer.onDrawFrame();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        if (bdFourteenDrawer != null) {
            bdFourteenDrawer.onDestroy();
        }
    }

    @Override
    public void adjustImageScaling(int width, int height) {
        adjustImageScalingStretch(width, height);
    }

}
