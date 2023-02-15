package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby3;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby3.particle.BabyThreeCloudDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby3.particle.BabyThreeCloudSystem;

import java.util.List;

/**
 * stay 放大-缩小-放大
 */
public class BabyThreeThemeTwo extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetBorder;
    private BabyThreeCloudDrawer mBabyThreeCloudDrawer;

    public BabyThreeThemeTwo(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 3, 0.1f, 1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.TOP));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, -2f, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT).setScale(1.1f, 1.1f));
        this.isNoZaxis = isNoZaxis;

        widgetBorder = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetBorder.setEnterAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.TOP)));
        widgetBorder.setOutAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, -2f, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT)));
        widgetBorder.setZView(0);
        mBabyThreeCloudDrawer = new BabyThreeCloudDrawer(new BabyThreeCloudSystem(BabyThreeCloudSystem.ONE));
    }


    @Override
    public void drawWiget() {
        widgetBorder.drawFrame();
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        mBabyThreeCloudDrawer.onDrawFrame();
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime - DEFAULT_ENTER_TIME, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.BOTTOM).
                setCoordinate(0, -1.0f, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true)));
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());
        widgetOne.setZView(-0.0f);

        widgetTwo = new BaseThemeExample(totalTime - DEFAULT_ENTER_TIME, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setCoordinate(-1, 0, 0, 0)
                .build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());
        widgetTwo.setZView(-0.0f);
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        //文字
        widgetOne.init(mimaps.get(1), width, height);
        float scale = width < height ? 2f : (width == height ? 3 : 4);
        widgetOne.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), 0, -0.8f, scale, scale);

        widgetTwo.init(mimaps.get(2), width, height);
        int devideX = width < height ? 1 : 2;
        int devideY = width < height ? 2 : 1;
        float centerx = -0.0f;
        float centery = 0.8f;
        float scaleX = width < height ? 3 : 6;
        float scaleY = width < height ? 6 : 3;
        widgetTwo.adjustScaling(width / devideX, height / devideY, mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerx, centery, scaleX, scaleY);
        //边框
        widgetBorder.init(mimaps.get(0), width, height);
        pos = new float[]{
                -1.0f, 1.0f,
                -1.0f, -1.0f,
                1.0f, 1.0f,
                1.0f, -1.0f,
        };
        widgetBorder.setVertex(pos);
        mBabyThreeCloudDrawer.init(mimaps.get(3));
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetBorder.onDestroy();
        mBabyThreeCloudDrawer.onDestroy();
    }

}
