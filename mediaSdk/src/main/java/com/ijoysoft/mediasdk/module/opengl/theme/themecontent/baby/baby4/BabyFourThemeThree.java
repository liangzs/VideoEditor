package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby4;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 缩小
 */
public class BabyFourThemeThree extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    public BabyFourThemeThree(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 1, 0.1f, 1.0f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 0f, 2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;


    }

    @Override
    public void initWidget() {

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, -1, 0f, 0f).setIsNoZaxis(true).build());
        widgetOne.setZView(0);
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());

        widgetTwo = new BaseThemeExample(totalTime - 2500, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.BOTTOM).
                setCoordinate(0, 1.0f, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true)));
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());
        widgetTwo.setZView(-0.0f);


    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        //文字
        widgetOne.init(mimaps.get(0), width, height);
        float[] pos = width < height ? new float[]{
                -0.75f, -0.8f,
                -0.75f, -0.95f,
                0.75f, -0.8f,
                0.75f, -0.95f
        } : (width == height ? new float[]{
                -0.75f, -0.75f,
                -0.75f, -1.0f,
                0.75f, -0.75f,
                0.75f, -1.0f
        } : new float[]{
                -0.5f, -0.75f,
                -0.5f, -1.0f,
                0.5f, -0.75f,
                0.5f, -1.0f,
        });
        widgetOne.setVertex(pos);
        float centerx = 0;
        float centery = width < height ? 0.75f : 0.6f;
        float scaleW = width < height ? 2 : 4;
        float scaleH = width < height ? 4 : 2;
        int devideX = width < height ? 1 : 2;
        int devideY = width < height ? 2 : 1;
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width / devideX, height / devideY, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerx, centery, scaleW, scaleH);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }

}
