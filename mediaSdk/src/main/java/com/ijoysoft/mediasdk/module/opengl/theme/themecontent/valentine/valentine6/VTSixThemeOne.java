package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine6;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine6.particle.VTSixDrawerTwo;

import java.util.List;

/**
 * 缩小
 * 这里在悬停缩放时，添加滤镜
 */
public class VTSixThemeOne extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private VTSixDrawerTwo particleDrawer;

    public VTSixThemeOne(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.1f, 1.1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2.5f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT).setScale(1.1f, 1.1f));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0, -2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;

        //widget
        widgetOne = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.BOTTOM).
                setCoordinate(0, 1.0f, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true)));

        widgetOne.setZView(0);
        //two
        widgetTwo = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).
                setCoordinate(-2, 0f, 0f, 0f).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());
        widgetTwo.setZView(-0.0f);
        particleDrawer = new VTSixDrawerTwo();

    }


    @Override
    public void drawLast() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        particleDrawer.onDrawFrame();
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
        float scale = width < height ? 3 : (width == height ? 3f : 3f);
        float centerX = width < height ? 0.5f : (width == height ? 0.5f : 0.5f);
        float centerY = width < height ? 0.65f : (width == height ? 0.6f : 0.5f);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
        widgetTwo.init(mimaps.get(1), width, height);
        scale = width < height ? 2.3f : (width == height ? 2.5f : 4f);
        centerX = width < height ? -0.5f : (width == height ? -0.5f : -0.5f);
        centerY = width < height ? 0.7f : (width == height ? 0.6f : 0.5f);
        widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);
        particleDrawer.init(mimaps.get(2));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        particleDrawer.onDestroy();
    }


}
