package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday6;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 缩小-放大-缩小
 */
public class HDSixThemeFour extends BaseBlurThemeExample {

    private BaseThemeExample widgetOne;

    public HDSixThemeFour(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 4, 0.1f, 1.0f);
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 1, 0, 0).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setZView(0);

    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit,mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float[] pos = width < height ? new float[]{
                -0.8f, 1.0f,
                -0.8f, 0.8f,
                1.0f, 1.0f,
                1.0f, 0.8f,
        } : (width == height ? new float[]{
                -0.8f, 1.0f,
                -0.8f, 0.7f,
                1.0f, 1.0f,
                1.0f, 0.7f,
        } : new float[]{
                -0.0f, 1.0f,
                -0.0f, 0.7f,
                1.0f, 1.0f,
                1.0f, 0.7f,
        });
        widgetOne.setVertex(pos);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }


}
