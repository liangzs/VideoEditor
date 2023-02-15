package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday10;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

public class BDTenThemeTwo extends BaseBlurThemeExample {

    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetBorder;

    public BDTenThemeTwo(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(3000, true, 2, 0.1f, 1);
        stayAction.setZView(0);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).
                setConors(AnimateInfo.ROTATION.Y_ANXIS, 270, 360).setIsNoZaxis(true).build();
        isNoZaxis = true;
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).build();

        widgetBorder = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetBorder.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setConors(AnimateInfo.ROTATION.Y_ANXIS, 270, 360).setIsNoZaxis(true).build());
        widgetBorder.setZView(0);

    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        widgetOne.setZView(-2.0f);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2f, 0f, 0f).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setZView(-2.0f).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, -2.5f, 0f).setZView(-2).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setZView(-2.0f).build());
    }

    @Override
    public void drawWiget() {
        widgetBorder.drawFrame();
        widgetOne.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        init(bitmap, width, height);
        widgetBorder.init(mimaps.get(1), width, height);
        widgetBorder.setVertex(pos);
        float scaleX = width < height ? 3.8f : (width == height ? 3.8f : 5.7f);
        float centerX = 0;
        float centerY = width < height ? 0.65f : (width == height ? 0.6f : 0.6f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetBorder.onDestroy();
    }

}
