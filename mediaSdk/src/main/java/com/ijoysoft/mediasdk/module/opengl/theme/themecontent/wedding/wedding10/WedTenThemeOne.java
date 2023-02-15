package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding10;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 缩小
 * 这里在悬停缩放时，添加滤镜
 */
public class WedTenThemeOne extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;//文字

    public WedTenThemeOne(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.2f, 1.2f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setZView(0);

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setZView(0);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).
                setZView(0).setCoordinate(-1, 0, 0, 0).build());

        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetThree.setZView(0);
        widgetThree.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).
                setZView(0).setCoordinate(0, 1, 0, 0).setOrientation(AnimateInfo.ORIENTATION.BOTTOM)));
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.setVertex(pos);

        widgetTwo.init(mimaps.get(1), width, height);
        float[] pos = width < height ? new float[]{
                -1f, -0.8f,
                -1f, -1f,
                -0.6f, -0.8f,
                -0.6f, -1f,

        } : (width == height ? new float[]{
                -1f, -0.7f,
                -1f, -1f,
                -0.65f, -0.7f,
                -0.65f, -1f,

        } : new float[]{
                -1f, -0.55f,
                -1f, -1f,
                -0.6f, -0.55f,
                -0.6f, -1f,

        });
        widgetTwo.setVertex(pos);

        pos = width < height ? new float[]{
                -1f, 0.98f,
                -1f, 0.7f,
                1f, 0.98f,
                1f, 0.7f,

        } : (width == height ? new float[]{
                -1f, 0.98f,
                -1f, 0.6f,
                1f, 0.98f,
                1f, 0.6f,

        } : new float[]{
                0, 0.98f,
                0, 0.55f,
                1f, 0.98f,
                1f, 0.55f,

        });
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.setVertex(pos);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
    }

}
