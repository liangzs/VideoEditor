package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love3;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
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
public class LoveThreeThemeOne extends BaseBlurThemeExample {
    private BaseThemeExample widgetBorder;
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;

    public LoveThreeThemeOne(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(3000, true, 2, 0.1f, 1.0f);
        stayAction.setZView(-2.5f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(1f, 0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 270, 360).setZView(-2.5f).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setZView(-2.5f).build();

        widgetBorder = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetBorder.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(1f, 0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 270, 360).setScale(1.04f, 1.04f).setZView(-2.5f).build());
        widgetBorder.setStayAction(new AnimationBuilder(3000).setIsNoZaxis(true).setZView(0).build());
        widgetBorder.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).setZView(0).build());

    }

    @Override
    public void initWidget() {
        //one
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setFade(0, 1).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setFade(1, 0).build());
        widgetOne.setZView(0);
        //two
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setFade(0, 1).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setFade(1, 0).build());
        widgetTwo.setZView(0);
        //three
        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setFade(0, 1).build());
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setFade(1, 0).build());
        widgetThree.setZView(0);
    }

    @Override
    public void drawWiget() {
        if (getStatus() == ActionStatus.ENTER) {
            widgetBorder.setNoZaxis(false);
        } else {
            widgetBorder.setNoZaxis(true);
        }
        widgetBorder.drawFrame();
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        //边框
        widgetBorder.init(mimaps.get(0), width, height);
        widgetBorder.setVertex(pos);

        //上
//        widgetOne.init(mimaps.get(1), width, height);
//        //顶点坐标
//        pos = width == height ? new float[]{
//                -1f, -0.3f,
//                -1f, -1f,
//                1f, -0.3f,
//                1f, -1f,
//        } : (width < height ? new float[]{
//                -1f, -0.3f,
//                -1f, -1f,
//                1f, -0.3f,
//                1f, -1f,
//        } : new float[]{
//                -1f, -0.1f,
//                -1f, -1f,
//                1f, -0.1f,
//                1f, -1f,
//        });
//        widgetOne.setVertex(pos);

        //上
        float scaleX = width == height ? 1f : (width < height ? 1f : 1f);
        float scaleY = width == height ? 1f : (width < height ? 1f : 1f);
        float centerX = width == height ? 0f : (width < height ? 0f : 0f);
        float centerY = width == height ? -0.55f : (width < height ? -0.6f : -0.4f);
        widgetOne.init(mimaps.get(1), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleY);

        //左下角
        scaleX = width == height ? 4f : (width < height ? 4f : 4f);
        scaleY = width == height ? 4f : (width < height ? 4f : 4f);
        centerX = width == height ? -0.95f : (width < height ? -0.9f : -0.9f);
        centerY = width == height ? 0.85f : (width < height ? 0.92f : 0.8f);
        widgetTwo.init(mimaps.get(2), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleY);

        //右下角
        scaleX = width == height ? 4f : (width < height ? 4f : 4f);
        scaleY = width == height ? 4f : (width < height ? 4f : 4f);
        centerX = width == height ? 0.95f : (width < height ? 0.9f : 0.9f);
        centerY = width == height ? 0.85f : (width < height ? 0.92f : 0.8f);
        widgetThree.init(mimaps.get(3), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scaleX, scaleY);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
        widgetBorder.onDestroy();
    }

}