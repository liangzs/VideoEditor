package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday2;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 放大
 */
public class BirthDayTwoThemeSeven extends BaseThemeExample {
    private BaseThemeExample widgetOne;


    public BirthDayTwoThemeSeven(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 1, 0.1f, 1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 2f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 0f, 2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM).setScale(1.1f, 1.1f));
        this.isNoZaxis = isNoZaxis;
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, 2500, 0, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(2500).setCoordinate(0, 1f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM).setZView(0).setIsNoZaxis(true)));
        widgetOne.setZView(0);
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).setZView(0).setFade(1, 0).build());

    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap bitmap1, List<Bitmap> list, int width, int height) {
        super.init(bitmap, bitmap1, list, width, height);
        widgetOne.init(list.get(0), width, height);
        float scale = width < height ? 1.5f : 2f;
        float centerX = width < height ? -0.3f : (width == height ? -0.4f : -0.4f);
        float centerY = width < height ? 0.8f : (width == height ? 0.8f : 0.7f);
        widgetOne.adjustScaling(width, height, list.get(0).getWidth(), list.get(0).getHeight(), centerX, centerY, scale, scale);
    }

    @Override
    public void adjustImageScaling(int width, int height) {
        adjustImageScalingStretch(width, height);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }
}
