package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi10;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 缩小
 */
public class HoliTenThemeThree extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;


    public HoliTenThemeThree(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 1, 0.2f, 1f);
        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).
                setCoordinate(0, -1f, 0, 0).build());

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setFade(0, 1).build());
    }


    @Override
    public void drawLast() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float vertex[] = width < height ? new float[]{
                -1f, -0.8f,
                -1f, -1f,
                0.4f, -0.8f,
                0.4f, -1f,
        } : (width == height ? new float[]{
                -1f, -0.8f,
                -1f, -1f,
                0.4f, -0.8f,
                0.4f, -1f,
        } : new float[]{
                -1f, -0.8f,
                -1f, -1f,
                0.4f, -0.8f,
                0.4f, -1f,
        });
        widgetOne.setVertex(vertex);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScalingFixX(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), AnimateInfo.ORIENTATION.BOTTOM);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }


}
