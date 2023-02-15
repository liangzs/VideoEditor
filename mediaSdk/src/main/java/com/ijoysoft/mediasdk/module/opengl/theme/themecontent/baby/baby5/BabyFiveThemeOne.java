package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby5;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 缩小
 * 这里在悬停缩放时，添加滤镜
 */
public class BabyFiveThemeOne extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    public BabyFiveThemeOne(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME,  DEFAULT_ENTER_TIME, 0);
        stayAction = new StayScaleAnimation( DEFAULT_ENTER_TIME, false, 1, 0.2f, 1.2f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(0, -1, 0, 0).build());
        widgetOne.setZView(0);

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setZView(0);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(-1, 0, 0, 0).build());

    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit,mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        //顶点坐标
        float pos[] = width < height ? new float[]{
                -1.0f, -0.8f,
                -1.0f, -1.0f,
                1.0f, -0.8f,
                1.0f, -1.0f,
        } : (width == height ? new float[]{
                -1.0f, -0.75f,
                -1.0f, -1.0f,
                1.0f, -0.75f,
                1.0f, -1.0f,
        } : new float[]{
                -1.0f, -0.7f,
                -1.0f, -1.0f,
                1.0f, -0.7f,
                1.0f, -1.0f,
        });
        widgetOne.setVertex(pos);
        int devideX = width <= height ? 1 : 2;
        int devideY = width <= height ? 2 : 1;
        float scaleX = width <= height ? 2 : 4;
        float scaleY = width <= height ? 4 : 2;
        float centerX = width <= height ? -0.5f : -0.7f;
        float centerY = width <= height ? 0.8f : 0.6f;
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width / devideX, height / devideY,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleY);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }


}
