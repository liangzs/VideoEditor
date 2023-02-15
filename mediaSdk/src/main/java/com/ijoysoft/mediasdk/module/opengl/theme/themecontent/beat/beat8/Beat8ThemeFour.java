package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat8;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 缩小-放大-缩小
 */
public class Beat8ThemeFour extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;

    public Beat8ThemeFour(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 3, 0.1f, 1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, -2f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.TOP));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 2f, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT));
        this.isNoZaxis = isNoZaxis;


        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0f, -2f, 0, 0).setIsNoZaxis(true).
                setOrientation(AnimateInfo.ORIENTATION.TOP)));
        widgetOne.setOutAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0f, 0f, 2f, 0).setIsNoZaxis(true).
                setOrientation(AnimateInfo.ORIENTATION.LEFT)));

    }

    /**
     * 复写这里的方法，传入滤镜
     */
    @Override
    public void drawFrame() {
        super.drawFrame();
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        //文字
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScalingFixX(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), AnimateInfo.ORIENTATION.BOTTOM);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }
}
