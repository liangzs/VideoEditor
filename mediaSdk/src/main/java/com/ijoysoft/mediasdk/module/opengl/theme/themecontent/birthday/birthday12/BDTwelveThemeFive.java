package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday12;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 放大-缩小
 */
public class BDTwelveThemeFive extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    private float[] originOne = new float[8];
    private float[] originTwo = new float[8];
    private int widgetEneterCount = (500 * ConstantMediaSize.FPS / 1000);
    private float[] posOneOffset = new float[8];//计算控件出现动画
    private float[] posTwoOffset = new float[8];//计算控件出现动画

    public BDTwelveThemeFive(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 2, 0.1f, 1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0f, 0f, 0, 2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;
//        starClusterDrawer = new StarClusterDrawer(new BDFiveStarSystemOne());
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 1f, 0f, 0f).
                setIsWidget(true).setIsNoZaxis(true).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0f, 1f).
                setIsWidget(true).setIsNoZaxis(true).build());
        widgetOne.setZView(0);


        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 1f, 0f, 0f).
                setIsWidget(true).setIsNoZaxis(true).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0f, 1f).
                setIsWidget(true).setIsNoZaxis(true).build());
        widgetTwo.setZView(0);

    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, null, mimaps, width, height);
        float scaleX = width < height ? 5 : (width == height ? 3 : 3);
        float centerX = width < height ? 0.7f : (width == height ? 0.7f : 0.8f);
        float centerY = width < height ? 0.85f : (width == height ? 0.8f : 0.8f);
        widgetOne.init(mimaps.get(0), width, height);
        originOne = widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 6 : (width == height ? 5 : 5);
        centerX = width < height ? 0.5f : (width == height ? 0.5f : 0.7f);
        centerY = width < height ? 0.94f : (width == height ? 0.84f : 0.85f);
        widgetTwo.init(mimaps.get(1), width, height);
        originTwo = widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleX);

        posOneOffset = evaluateShade(originOne);
        posTwoOffset = evaluateShade(originTwo);
    }

    @Override
    public void drawWiget() {
        if (getStatus() == ActionStatus.STAY) {
            if (frameIndex < stayCount + widgetEneterCount) {
                arrayAdd();
            } else if (frameIndex < stayCount + 2 * widgetEneterCount) {
                arraySub();
            } else if (frameIndex < stayCount + 3 * widgetEneterCount) {
                arrayAdd();
            } else if (frameIndex < stayCount + 4 * widgetEneterCount) {
                arraySub();
            } else if (frameIndex < stayCount + 5 * widgetEneterCount) {
                arrayAdd();
            } else if (frameIndex < stayCount + 6 * widgetEneterCount) {
                arraySub();
            }
            widgetOne.setVertex(originOne);
            widgetTwo.setVertex(originTwo);
        }
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }

    /**
     * 除了底端y的坐标不变化之外，涉及顶点坐标有：1,3,4,5,7,8 （即 2、6两个坐标不做变化)
     * 1-2两个顶点是负x轴，34是正x轴
     */
    private float[] evaluateShade(float[] origin) {
        float[] offset = new float[8];
        float[] end = new float[8];
        end[0] = origin[0] - 0.02f;
        end[1] = 1f;
        //end[1]为顶点
        end[2] = origin[2] - 0.02f;
        end[3] = origin[3] + 0.02f;
        end[4] = origin[4] + 0.02f;
        end[5] = 1f;
        //end[5]为1顶点
        end[6] = origin[6] + 0.02f;
        end[7] = origin[7] + 0.02f;
        for (int i = 0; i < end.length; i++) {
            offset[i] = (end[i] - origin[i]) / widgetEneterCount;
        }
        return offset;
    }

    private void arrayAdd() {
        for (int i = 0; i < originOne.length; i++) {
            originOne[i] = originOne[i] + posOneOffset[i];
        }
        for (int i = 0; i < originTwo.length; i++) {
            originTwo[i] = originTwo[i] + posTwoOffset[i];
        }
    }

    private void arraySub() {
        for (int i = 0; i < originOne.length; i++) {
            originOne[i] = originOne[i] - posOneOffset[i];
        }
        for (int i = 0; i < originTwo.length; i++) {
            originTwo[i] = originTwo[i] - posTwoOffset[i];
        }
    }

    @Override
    public void adjustImageScaling(int width, int height) {
        adjustImageScalingStretch(width, height);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }

}
