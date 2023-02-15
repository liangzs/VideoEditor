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
 * 缩小
 * 这里在悬停缩放时，添加滤镜
 */
public class HDTwelveThemeOne extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;

    public HDTwelveThemeOne(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 1, 0.1f, 1.0f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT).setScale(1f, 1f));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0, -2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(1, 0, 0f, 0f).setIsNoZaxis(true).build());
        widgetOne.setZView(0);
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());


    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }


    //需要放在这里，因为需要用useprogram 这里注意的是颜色值在[0,1]之间这里注意的是颜色值在[0,1]之间
    //暖色的颜色。是加强R/G来完成。  float[] warmFilterColorData = {0.1f, 0.1f, 0.0f};
    //冷色系的调整。简单的就是增加B的分量float[] coolFilterColorData = {0.0f, 0.0f, 0.1f};
    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        pos = width < height ? new float[]{
                0.2f, 1f,
                0.2f, 0.7f,
                1f, 1f,
                1f, 0.7f,
        } : (width == height ? new float[]{
                0.2f, 1f,
                0.2f, 0.5f,
                1f, 1f,
                1f, 0.5f,
        } : new float[]{
                0.5f, 1f,
                0.5f, 0.5f,
                1f, 1f,
                1f, 0.5f,
        });
        widgetOne.setVertex(pos);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }
}
