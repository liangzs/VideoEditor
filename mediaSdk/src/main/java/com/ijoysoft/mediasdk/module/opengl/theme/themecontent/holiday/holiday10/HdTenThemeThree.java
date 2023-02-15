package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday10;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 缩小
 */
public class HdTenThemeThree extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetThree;

    public HdTenThemeThree(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, 0);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.1f, 1.1f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.1f, 1.1f).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 1, 0, 0).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setZView(0);

        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetThree.setZView(0);

    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetThree.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetThree.setVertex(pos);
        widgetThree.init(mimaps.get(1), width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float pos[] = width < height ? new float[]{
                -1f, 1f,
                -1f, 0.65f,
                1f, 1,
                1f, 0.65f,
        } : (width == height ? new float[]{
                -1f, 1f,
                -1f, 0.3f,
                1f, 1,
                1f, 0.3f,
        } : new float[]{
                -1f, 1f,
                -1f, 0.3f,
                1f, 1,
                1f, 0.3f,
        });
        widgetOne.setVertex(pos);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetThree.onDestroy();
    }
}
