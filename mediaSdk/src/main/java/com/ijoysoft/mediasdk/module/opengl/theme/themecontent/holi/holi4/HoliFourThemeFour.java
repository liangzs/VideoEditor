package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi4;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 缩小-放大-缩小
 */
public class HoliFourThemeFour extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;


    public HoliFourThemeFour(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.2f, 1.2f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f).setIsNoZaxis(true).setZView(0).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setZView(0);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setStartX(0).setStartY(0.8f).setIsNoZaxis(true).setZView(0).setScale(0.01f, 1f).build());


    }


    @Override
    public void drawWiget() {
        super.drawWiget();
        widgetOne.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scale = width < height ? 1.2f : (width == height ? 1.2f : 1.2f);
        float centerX = 0;
        float centerY = width < height ? 0.8f : (width == height ? 0.8f : 0.8f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }

}
