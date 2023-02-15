package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi9;

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
public class HoliNineThemeOne extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;


    public HoliNineThemeOne(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 1, 0.1f, 1.0f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT).setScale(1f, 1f));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0, -2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;


        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT)));
        widgetOne.setZView(0);
        widgetOne.setOutAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0, -2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM)));


        //two
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -1.0f, 0f, 0f).setIsNoZaxis(true).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).setFade(1, 0).build());

        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(0, 1).setIsNoZaxis(true).build());
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).setFade(1, 0).build());

    }

    @Override
    public void drawFramePreview() {
        super.drawFramePreview();
        widgetOne.drawFramePreview();
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
        //文字
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.setVertex(pos);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScalingFixX(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(),
                AnimateInfo.ORIENTATION.TOP);

        widgetThree.init(mimaps.get(2), width, height);
        float centerX = -0.3f;
        float centerY = width < height ? 0.75f : 0.6f;
        float scale = width < height ? 1.5f : 1.5f;
        widgetThree.adjustScaling(width, height, mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scale, scale);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
    }
}
