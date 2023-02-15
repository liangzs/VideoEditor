package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday13;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;

/**
 * 左上角z轴翻滚进，右下角z轴滚出
 */
public class BDThirteenThemeFour extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;

    public BDThirteenThemeFour(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.SPRING_Y);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, -2f, 0, 0).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 360).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 2f, 2f).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 180).setWidthHeight(width, height).build();
    }


    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setCoordinate(0, 1.0f, 0f, 0f).setZView(-2.4f)));
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.4f).setCoordinate(0, 0, 0, 2f).build());

        //two
        widgetTwo = new BaseThemeExample(totalTime, 1700, 0, DEFAULT_OUT_TIME);
        widgetTwo.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setEnterDelay(500).setCoordinate(0, 1.0f, 0f, 0f).setZView(-2.4f)));
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.4f).setCoordinate(0, 0f, 0, 2f).build());
        //three
        widgetThree = new BaseThemeExample(totalTime, 2200, 0, DEFAULT_OUT_TIME);
        widgetThree.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setEnterDelay(1000).setCoordinate(0, 1.0f, 0f, 0f).setZView(-2.4f)));
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.4f).setCoordinate(0, 0f, 0, 2f).build());

        widgetFour = new BaseThemeExample(totalTime, 2200, 0, DEFAULT_OUT_TIME);
        widgetFour.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setEnterDelay(1500).setCoordinate(0, 1.0f, 0f, 0f).setZView(-2.4f)));
        widgetFour.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.4f).setCoordinate(0, 0f, 0, 2f).build());

        widgetOne.setZView(-2.4f);
        widgetTwo.setZView(-2.4f);
        widgetThree.setZView(-2.4f);
        widgetFour.setZView(-2.4f);

    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
        widgetFour.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        //one
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.setVertex(adjustScalingSpecial((int) (width / 2f), (int) (height / 4f), mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), -0.6f, 4, 4));
        //two
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.setVertex(adjustScalingSpecial((int) (width / 2f), (int) (height / 4f), mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), -0.2f, 4, 4));

        //three
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.setVertex(adjustScalingSpecial((int) (width / 2f), (int) (height / 4f), mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), 0.2f, 4, 4));
        //four
        widgetFour.init(mimaps.get(3), width, height);
        widgetFour.setVertex(adjustScalingSpecial((int) (width / 2f), (int) (height / 4f), mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), 0.7f, 3, 3));
    }

    public float[] adjustScalingSpecial(int showWidth, int showHeight, int framewidth, int frameheight, float centerX, float scaleX, float scaleY) {
        float outputWidth = showWidth;
        float outputHeight = showHeight;
        float ratio1 = outputWidth / framewidth;
        float ratio2 = outputHeight / frameheight;
        float ratioMax = Math.min(ratio1, ratio2);
        // 居中后图片显示的大小
        int imageWidthNew = Math.round(framewidth * ratioMax);
        int imageHeightNew = Math.round(frameheight * ratioMax);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = imageWidthNew / outputWidth;
        float ratioHeight = imageHeightNew / outputHeight;
        // 根据拉伸比例还原顶点
        float[] cube = new float[]{centerX - ratioWidth / scaleX, 1f, centerX - ratioWidth / scaleX, 1 - ratioHeight / scaleY,
                centerX + ratioWidth / scaleX, 1, centerX + ratioWidth / scaleX, 1 - ratioHeight / scaleY};
        return cube;
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
