package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday3;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WaveAnimation;

import java.util.List;

/**
 * 右边波浪进
 */
public class BirthDayThreeThemeFour extends BDThreeBaseExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;

    public BirthDayThreeThemeFour(int totalTime) {
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
        Bitmap widgetBitmap = mimaps.get(0);
        widgetOne.init(widgetBitmap, width, height);
        float centerX = -0.7f;
        float centerY = -1.3f;
        float scale = width < height ? 6 : (width == height ? 4 : 3);
        widgetOne.adjustScaling(width, height, widgetBitmap.getWidth(), widgetBitmap.getHeight(), centerX, centerY, scale, scale);
        //two
        widgetBitmap = mimaps.get(1);
        widgetTwo.init(widgetBitmap, width, height);
        centerX = 0.7f;
        centerY = -1.3f;
        widgetTwo.adjustScaling(width, height, widgetBitmap.getWidth(), widgetBitmap.getHeight(), centerX, centerY, scale, scale);
        //three
        widgetBitmap = mimaps.get(2);
        widgetThree.init(widgetBitmap, width, height);
        centerX = 0;
        centerY = -1.3f;
        widgetThree.adjustScaling(width, height, widgetBitmap.getWidth(), widgetBitmap.getHeight(), centerX, centerY, scale, scale);

        widgetFour.init(mimaps.get(3), width, height);
        //顶点坐标
        float pos[] = width < height ? new float[]{
                -1.0f, -0.8f,
                -1.0f, -1.0f,
                0.4f, -0.8f,
                0.4f, -1.0f,
        } : (width == height ? new float[]{
                -1.0f, -0.7f,
                -1.0f, -1.0f,
                0.4f, -0.7f,
                0.4f, -1.0f,
        } : new float[]{
                -1.0f, -0.6f,
                -1.0f, -1.0f,
                -0.2f, -0.6f,
                -0.2f, -1.0f,
        });
        widgetFour.setVertex(pos);
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, totalTime, 0, 0);
        widgetOne.setEnterAnimation(new AnimationBuilder(totalTime).setIsWidget(true).
                setCoordinate(0, 2.5f, 0f, -1.0f).build());
        //two
        widgetTwo = new BaseThemeExample(totalTime, 2000, 0, totalTime - 2000);
        widgetTwo.setOutAnimation(new AnimationBuilder(totalTime - 2000).
                setCoordinate(0, 2.5f, 0f, -1.0f).setIsWidget(true).build());

        //three
        widgetThree = new BaseThemeExample(totalTime, 1500, 0, totalTime - 1500);
        widgetThree.setOutAnimation(new AnimationBuilder(totalTime - 1500).
                setCoordinate(0, 2.5f, 0f, -1.0f).setIsWidget(true).build());

        widgetFour = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetFour.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -1.0f, 0f, 0f).setIsNoZaxis(true).setZView(0).build());
        widgetFour.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setFade(1, 0).build());
        widgetFour.setZView(0);
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
    protected void drawFramePre() {
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

    @Override
    public String printOutString() {
        return this.getClass().getName() + ":getTexture:" + getTextureId() + ",wighet_one:" + widgetOne.getTexture()
                + ",widgetTwo:" + widgetTwo.getTexture() + ",widgetThree:" + widgetThree.getTexture() + ",widgetFour:" + widgetFour.getTexture();
    }
}
