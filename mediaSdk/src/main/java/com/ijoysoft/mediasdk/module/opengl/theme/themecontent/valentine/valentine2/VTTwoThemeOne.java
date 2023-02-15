package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine2;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday16.particle.BDSixteenStarDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday16.particle.BDSixteenStarSystem;

import java.util.List;

/**
 * 缩小
 * 这里在悬停缩放时，添加滤镜
 */
public class VTTwoThemeOne extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BDSixteenStarDrawer bdSixteenDrawer;


    public VTTwoThemeOne(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.1f, 1.1f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.1f, 1.1f).setIsNoZaxis(true).build();
//        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(0.01f, 1f).setIsNoZaxis(true).setZView(0).build());
        widgetOne.getEnterAnimation().setIsSubAreaWidget(true, -0.85f, -0.8f);
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-0).setIsNoZaxis(true).setFade(1, 0).build());
        widgetOne.setZView(0);

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(0.01f, 1f).setIsNoZaxis(true).setZView(0).build());
        widgetTwo.getEnterAnimation().setIsSubAreaWidget(true, -0.6f, -0.9f);
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-0).setIsNoZaxis(true).setFade(1, 0).build());
        widgetTwo.setZView(0);

        //widget
        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-0).setOrientation(AnimateInfo.ORIENTATION.BOTTOM).
                setCoordinate(0, 2f, 0f, 0f).setEnterDelay(800).build());
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0f, 2f).build());

        bdSixteenDrawer = new BDSixteenStarDrawer(new BDSixteenStarSystem(BDSixteenStarSystem.StartType.HEART));
    }


    @Override
    public void drawLast() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
    }

    @Override
    public void drawWiget() {
        if (getStatus() != ActionStatus.ENTER && bdSixteenDrawer != null) {
            bdSixteenDrawer.onDrawFrame();
        }
    }


    @Override
    public void init(Bitmap bitmap, Bitmap bitmap1, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, bitmap1, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float scale = width < height ? 8 : (width == height ? 8 : 8);
        float centerX = width < height ? -0.85f : (width == height ? -0.85f : -0.7f);
        float centerY = width < height ? -0.8f : (width == height ? -0.7f : -0.7f);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
        widgetTwo.init(mimaps.get(0), width, height);
        scale = width < height ? 6 : (width == height ? 6 : 6);
        centerX = width < height ? -0.5f : (width == height ? -0.5f : -0.5f);
        centerY = width < height ? -0.9f : (width == height ? -0.8f : -0.8f);
        widgetTwo.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
        widgetThree.init(mimaps.get(1), width, height);
        scale = width < height ? 1.5f : (width == height ? 1.5f : 2);
        centerX = 0;
        centerY = width < height ? 0.9f : (width == height ? 0.85f : 0.8f);
        widgetThree.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);

        if (bdSixteenDrawer != null) {
            bdSixteenDrawer.init(mimaps.get(2));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
        bdSixteenDrawer.onDestroy();
    }

    @Override
    public float[] getPos() {
        return cube;
    }
}
