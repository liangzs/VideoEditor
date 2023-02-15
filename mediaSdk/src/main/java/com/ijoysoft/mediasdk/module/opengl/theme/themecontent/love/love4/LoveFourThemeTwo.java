package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love4;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * @ author       lfj
 * @ date         2020/10/22
 * @ description
 */
public class LoveFourThemeTwo extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;

    public LoveFourThemeTwo(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 3, 0.1f, 1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.TOP));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, -2f, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT).setScale(1.1f, 1.1f));
        this.isNoZaxis = isNoZaxis;
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 2f, 0f, 0f).setIsNoZaxis(true).build());
        widgetOne.setZView(0);
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());
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
        pos = width == height ? new float[]{
                -1f, 1f,
                -1f, 0.65f,
                1f, 1f,
                1f, 0.65f,
        } : (width < height ? new float[]{
                -1f, 1f,
                -1f, 0.8f,
                1f, 1f,
                1f, 0.8f,
        } : new float[]{
                -1f, 1f,
                -1f, 0.8f,
                1f, 1f,
                1f, 0.8f,
        });
        widgetOne.setVertex(pos);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }
}

