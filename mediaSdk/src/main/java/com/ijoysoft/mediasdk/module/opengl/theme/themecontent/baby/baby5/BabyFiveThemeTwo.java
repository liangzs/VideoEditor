package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby5;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 放大-缩小-放大
 */
public class BabyFiveThemeTwo extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    public BabyFiveThemeTwo(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, totalTime - DEFAULT_ENTER_TIME, 0);
        stayAction = new StayScaleAnimation(totalTime
                - DEFAULT_ENTER_TIME, true, 2, 0.2f, 1f);
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, 0, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(0, 1, 0, 0).build());
        widgetOne.setZView(0);


        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setZView(0);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(0, -1, 0, 0).build());
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
        float pos[] = width <= height ? new float[]{
                -1.0f, 1.0f,
                -1.0f, 0.8f,
                1.0f, 1.0f,
                1.0f, 0.8f,
        } : new float[]{
                -1.0f, 1.0f,
                -1.0f, 0.7f,
                1.0f, 1.0f,
                1.0f, 0.7f,
        };
        widgetOne.setVertex(pos);
        widgetTwo.init(mimaps.get(1), width, height);
        //顶点坐标
        pos = width <= height ? new float[]{
                -1.0f, -0.65f,
                -1.0f, -1.0f,
                1.0f, -0.65f,
                1.0f, -1.0f,
        } : new float[]{
                -1.0f, -0.3f,
                -1.0f, -1.0f,
                -0.2f, -0.3f,
                -0.2f, -1.0f,
        };
        widgetTwo.setVertex(pos);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }
}
