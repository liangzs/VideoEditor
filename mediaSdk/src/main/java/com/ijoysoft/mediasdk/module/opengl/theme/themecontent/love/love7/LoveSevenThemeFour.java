package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love7;

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
 * @ date         2020/10/23
 * @ description
 */
public class LoveSevenThemeFour extends BaseBlurThemeExample {
    private BaseThemeExample widgetBorder;

    public LoveSevenThemeFour(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 3, 0.1f, 1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, -2f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.TOP));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 2f, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT));
        this.isNoZaxis = isNoZaxis;

        widgetBorder = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetBorder.setEnterAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, -2f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.TOP)));
        widgetBorder.setOutAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 2f, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT)));
        widgetBorder.setZView(0);
    }

    @Override
    public void drawWiget() {
        widgetBorder.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetBorder.init(mimaps.get(0), width, height);
        //顶点坐标
        pos = width == height ? new float[]{
                -1f, 1f,
                -1f, 0.6f,
                1f, 1f,
                1f, 0.6f,
        } : (width < height ? new float[]{
                -1f, 1f,
                -1f, 0.7f,
                1f, 1f,
                1f, 0.7f,
        } : new float[]{
                -1f, 1f,
                -1f, 0.5f,
                1f, 1f,
                1f, 0.5f,
        });
        widgetBorder.setVertex(pos);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetBorder.onDestroy();
    }
}
