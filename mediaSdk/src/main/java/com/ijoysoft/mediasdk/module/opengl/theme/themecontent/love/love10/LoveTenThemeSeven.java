package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love10;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 放大-缩小
 */
public class LoveTenThemeSeven extends BaseBlurThemeExample {

    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    public LoveTenThemeSeven(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 2, 0.2f, 1f);
        this.isNoZaxis = isNoZaxis;

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).
                setScale(0.01f, 1).setStartX(-1).setStartY(-1f).build());

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).
                setScale(0.01f, 1).setStartX(1).setStartY(1f).build());
    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height / 2, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(),
                AnimateInfo.ORIENTATION.LEFT_TOP, 1.5f);

        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height / 2, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(),
                AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 1.5f);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }


}
