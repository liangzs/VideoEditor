package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine13;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;

/**
 * 上进，上下悬停，左划出
 */
public class VTThirteenThemeOne extends BaseThemeExample {
    private BaseThemeExample widgetOne;

    public VTThirteenThemeOne(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.SPRING_Y);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -2.5f, 0, 0).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 360).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, -2.5f, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 360).build();
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        widgetOne.setZView(0);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(0, 1, 0, 0f).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(0, 0f, -1.5f, 0).build());
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        //顶点坐标
        float pos[] = width < height ? new float[]{
                -0.9f, 0.95f,
                -0.9f, 0.8f,
                0.9f, 0.95f,
                0.9f, 0.8f,
        } : (width == height ? new float[]{
                -0.7f, 0.95f,
                -0.7f, 0.8f,
                0.7f, 0.95f,
                0.7f, 0.8f,
        } : new float[]{
                -0.5f, 0.95f,
                -0.5f, 0.75f,
                0.5f, 0.95f,
                0.5f, 0.75f,
        });
        widgetOne.setVertex(pos);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }
}
