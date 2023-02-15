package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday9;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.EffectFiveBaseExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WaveAnimation;

import java.util.List;

/**
 * 右边波浪进
 */
public class BirthDayNineThemeFive extends EffectFiveBaseExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;

    public BirthDayNineThemeFive(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 2000, 1000, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(1800, AnimateInfo.STAY.SPRING_Y, -2);
        stayNext = new AnimationBuilder(weightTime).setScale(1.0f, 1.1f).setZView(-2).build();
        enterAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(0f, -2f, 0f, 0).setZView(-2).build();
        outAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setScale(1.1f, 1.1f).
                setCoordinate(0f, 0f, 2f, 0f).setZView(-2));
        setZView(-2);
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, width, height);
        float scaleX = width < height ? 3 : (width == height ? 3 : 2);
        float centerX = width < height ? -0.7f : (width == height ? -0.7f : -0.7f);
        float centerY = width < height ? -1.4f : (width == height ? -1.4f : -1.4f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 3 : (width == height ? 3 : 2);
        centerX = width < height ? 0.7f : (width == height ? 0.7f : 0.7f);
        centerY = width < height ? -1.5f : (width == height ? -1.5f : -1.5f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 3 : (width == height ? 3 : 2);
        centerX = width < height ? 0.7f : (width == height ? 0.7f : 0.7f);
        centerY = width < height ? -1.5f : (width == height ? -1.5f : -1.5f);
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 2 : (width == height ? 2 : 3);
        centerX = 0;
        centerY = width < height ? 0.7f : (width == height ? 0.65f : 0.65f);
        widgetFour.init(mimaps.get(3), width, height);
        widgetFour.adjustScaling(width, height,
                mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scaleX, scaleX);
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, totalTime, 0, 0);
        widgetOne.setEnterAnimation(new AnimationBuilder(totalTime).setIsWidget(true).
                setCoordinate(0, 2.5f, 0f, -1.0f).build());

        //two
        widgetTwo = new BaseThemeExample(totalTime, 1000, 0, totalTime - 1000);
        widgetTwo.setOutAnimation(new AnimationBuilder(totalTime - 1000).
                setCoordinate(0, 2.5f, 0f, -1.0f).setIsWidget(true).build());

        //three
        widgetThree = new BaseThemeExample(totalTime, 2000, 0, totalTime - 2000);
        widgetThree.setOutAnimation(new AnimationBuilder(totalTime - 1000).
                setCoordinate(0, 2.5f, 0f, -1.0f).setIsWidget(true).build());
        widgetOne.setZView(-2f);
        widgetTwo.setZView(-2f);
        widgetThree.setZView(-2f);

        widgetFour = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        widgetFour.setZView(-2.0f);
        widgetFour.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2f, 0f, 0f).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setZView(-2.0f).build());
        widgetFour.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 2f, 0f).setZView(-2).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setZView(-2.0f).build());

    }

    public float[] adjustScalingSpecial(int showWidth, int showHeight, int framewidth, int frameheight, float centerX, float centerY) {
        float outputWidth = showWidth;
        float outputHeight = showHeight;
        float ratio1 = outputWidth / framewidth;
        float ratio2 = outputHeight / frameheight;
        float ratioMax = Math.min(ratio1, ratio2);
        // 居中后图片显示的大小
        int imageWidthNew = Math.round(framewidth * ratioMax);
        int imageHeightNew = Math.round(frameheight * ratioMax);
        //频繁打印的日志放至-verbose级别中
        LogUtils.v("adjustScaling", "outputWidth:" + outputWidth + ",outputHeight:" + outputHeight + ",imageWidthNew:"
                + imageWidthNew + ",imageHeightNew:" + imageHeightNew);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = imageWidthNew / outputWidth;
        float ratioHeight = imageHeightNew / outputHeight;
        // 根据拉伸比例还原顶点
        float[] temp = new float[]{centerX - ratioWidth / 2, centerY + ratioHeight / 4, centerX - ratioWidth / 2, centerY - ratioHeight / 4,
                centerX + ratioWidth / 2, centerY + ratioHeight / 4, centerX + ratioWidth / 2, centerY - ratioHeight / 4};
        return temp;
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
        widgetFour.drawFrame();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
        widgetFour.onDestroy();
    }

}
