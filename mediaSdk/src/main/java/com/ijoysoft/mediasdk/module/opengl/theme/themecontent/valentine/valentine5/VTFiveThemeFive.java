package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine5;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 放大-缩小
 */
public class VTFiveThemeFive extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;


    public VTFiveThemeFive(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 2, 0.2f, 1f);

//        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 0f, 0, 0).
//                setOrientation(AnimateInfo.ORIENTATION.LEFT));
//
//        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0f, 0f, 0, 2f).
//                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;
        setZView(0);
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime - 500, DEFAULT_ENTER_TIME, 1500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 1f, 0, 0).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-0).setIsNoZaxis(true).setFade(1, 0f).build());
        widgetOne.setZView(0);

    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit,mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float[] pos = width < height ? new float[]{
                -1f, 1f,
                -1f, 0.8f,
                1f, 1f,
                1f, 0.8f
        } : (width == height ? new float[]{
                -1f, 1f,
                -1f, 0.65f,
                1f, 1f,
                1f, 0.65f
        } : new float[]{
                -1f, 1f,
                -1f, 0.65f,
                1f, 1f,
                1f, 0.65f
        });
        widgetOne.setVertex(pos);
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }


}
