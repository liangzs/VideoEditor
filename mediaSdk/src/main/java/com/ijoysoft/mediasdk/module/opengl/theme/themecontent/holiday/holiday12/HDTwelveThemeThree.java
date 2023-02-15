package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday12;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 缩小
 */
public class HDTwelveThemeThree extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;

    public HDTwelveThemeThree(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 1, 0.1f, 1.0f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 0f, 2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;
    }

    @Override
    public void initWidget() {

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 1, 0f, 0f).setIsNoZaxis(true).build());
        widgetOne.setZView(0);
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());

    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        pos = width < height ? new float[]{
                0.2f, 1f,
                0.2f, 0.6f,
                1f, 1f,
                1f, 0.6f,
        } : (width == height ? new float[]{
                0.2f, 1f,
                0.2f, 0.4f,
                1f, 1f,
                1f, 0.4f,
        } : new float[]{
                0.5f, 1f,
                0.5f, 0.2f,
                1f, 1f,
                1f, 0.2f,
        });
        widgetOne.setVertex(pos);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }
}
