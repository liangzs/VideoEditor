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
 * stay 放大-缩小
 */
public class HDTwelveThemeFive extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;

    public HDTwelveThemeFive(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 2, 0.1f, 1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0f, 0f, 2, 0f).
                setOrientation(AnimateInfo.ORIENTATION.LEFT));
        this.isNoZaxis = isNoZaxis;

        widgetOne = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 1, 0f, 0f).setIsNoZaxis(true).build());
        widgetOne.setZView(0);
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());


    }

    @Override
    public void initWidget() {


    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        //文字
        widgetOne.init(mimaps.get(0), width, height);
        float scaleX = width < height ? 2f : (width == height ? 2f : 2);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth() / 2, mimaps.get(0).getHeight(), AnimateInfo.ORIENTATION.BOTTOM, scaleX);
    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }

}
