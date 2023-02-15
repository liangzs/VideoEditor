package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday6;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

public class BDSixThemeTwo extends BaseBlurThemeExample {

    private BaseThemeExample widgetOne;

    public BDSixThemeTwo(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(3000, true, 2, 0.1f, 1);
        stayAction.setZView(0);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).
                setConors(AnimateInfo.ROTATION.Y_ANXIS, 270, 360).setIsNoZaxis(true).build();
        isNoZaxis = true;
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).build();

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 2, 0, 0).setIsWidget(true).setIsNoZaxis(true).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).build());
        widgetOne.setZView(0);
    }


    @Override
    public void drawLast() {
        widgetOne.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        //one
        widgetOne.init(mimaps.get(0), width, height);
        //顶点坐标
        float pos[] = width == height ? new float[]{
                -0.9f, 0.92f,
                -0.9f, 0.79f,
                0.9f, 0.92f,
                0.9f, 0.79f,
        } : (width < height ? new float[]{
                -0.9f, 0.92f,
                -0.9f, 0.82f,
                0.9f, 0.92f,
                0.9f, 0.82f,
        } : new float[]{
                -0.9f, 0.95f,
                -0.9f, 0.75f,
                0.9f, 0.95f,
                0.9f, 0.75f,
        });
        widgetOne.setVertex(pos);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }
}
