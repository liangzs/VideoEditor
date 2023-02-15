package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love3;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * @ author       lfj
 * @ date         2020/10/21
 * @ description
 */
public class LoveThreeThemeTwo extends BaseBlurThemeExample {
    private BaseThemeExample widgetBorder;
    private BaseThemeExample widgetOne;

    private LoveThreePetalDrawer loveThreePetalDrawer;

    public LoveThreeThemeTwo(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(3000, true, 2, 0.1f, 1);
        stayAction.setZView(0);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).
                setConors(AnimateInfo.ROTATION.Y_ANXIS, 270, 360).setIsNoZaxis(true).build();
        this.isNoZaxis = true;
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).build();

        loveThreePetalDrawer = new LoveThreePetalDrawer();
        //border
        widgetBorder = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetBorder.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setConors(AnimateInfo.ROTATION.Y_ANXIS, 270, 360).setIsNoZaxis(true).build());
        widgetBorder.setZView(0);
    }

    @Override
    public void initWidget() {
        //one
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setFade(0, 1).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setFade(1, 0).build());
        widgetOne.setZView(0);
    }

    @Override
    public void drawWiget() {
        widgetBorder.drawFrame();
        widgetOne.drawFrame();
        loveThreePetalDrawer.onDrawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        //边框
        widgetBorder.init(mimaps.get(0), width, height);
        widgetBorder.setVertex(pos);

        //右下角
        float scaleX = width == height ? 4f : (width < height ? 4f : 4f);
        float scaleY = width == height ? 4f : (width < height ? 4f : 4f);
        float centerX = width == height ? 0.7f : (width < height ? 0.68f : 0.8f);
        float centerY = width == height ? 0.7f : (width < height ? 0.82f : 0.7f);
        widgetOne.init(mimaps.get(1), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleY);

        loveThreePetalDrawer.init(mimaps.subList(2, 6));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetBorder.onDestroy();
    }

}
