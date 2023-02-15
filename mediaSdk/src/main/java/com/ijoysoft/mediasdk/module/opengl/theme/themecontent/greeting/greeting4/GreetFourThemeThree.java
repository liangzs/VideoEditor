package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting4;

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
 * @ date         2020/10/16
 * @ description
 */
public class GreetFourThemeThree extends BaseBlurThemeExample {
    private BaseThemeExample widgetBorder;

    public GreetFourThemeThree(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.1f, 1.1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT).setScale(1.1f, 1.1f));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 0f, 2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;

        //border
        widgetBorder = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetBorder.setEnterAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT)));
        widgetBorder.setOutAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 0f, 2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM)));
        widgetBorder.setZView(0);
    }

    @Override
    public void drawWiget() {
        widgetBorder.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit,mimaps, width, height);
        //边框
        widgetBorder.init(mimaps.get(0), width, height);
        widgetBorder.setVertex(pos);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetBorder.onDestroy();
    }
}