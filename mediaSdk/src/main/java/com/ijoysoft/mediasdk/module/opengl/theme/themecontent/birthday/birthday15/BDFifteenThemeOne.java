package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday15;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 缩小
 * 这里在悬停缩放时，添加滤镜
 */
public class BDFifteenThemeOne extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;

    //蛋糕缩放动画
    private float[] origin = new float[8];
    private int widgetEneterCount = (500 * ConstantMediaSize.FPS / 1000);
    private float[] posOffset = new float[8];//计算控件出现动画

    public BDFifteenThemeOne(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(3000, false, 1, 0.1f, 1.1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT).setScale(1.1f, 1.1f));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0, -2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;

        //widget
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setFade(0, 0.8f).setIsWidget(true).setIsNoZaxis(true).build());
//        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).build());
        widgetOne.setZView(0);

        //widget
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(0, 1).setIsWidget(true).setIsNoZaxis(true).build());
        widgetTwo.setZView(0);


        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 1.0f, 0f, 0f).setIsWidget(true).setIsNoZaxis(true).build());
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 0.0f, 0f, 1f).setIsWidget(true).setIsNoZaxis(true).build());
        widgetThree.setZView(0);

        widgetFour = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetFour.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, -1.0f, 0f, 0f).setIsWidget(true).setIsNoZaxis(true).build());
        widgetFour.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 0.0f, 0f, -1f).setIsWidget(true).setIsNoZaxis(true).build());
        widgetFour.setZView(0);

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
            widgetTwo.setVertex(origin);
        }
        widgetThree.drawFrame();
        widgetFour.drawFrame();
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }


    //需要放在这里，因为需要用useprogram 这里注意的是颜色值在[0,1]之间这里注意的是颜色值在[0,1]之间
    //暖色的颜色。是加强R/G来完成。  float[] warmFilterColorData = {0.1f, 0.1f, 0.0f};
    //冷色系的调整。简单的就是增加B的分量float[] coolFilterColorData = {0.0f, 0.0f, 0.1f};
    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);

        widgetThree.init(mimaps.get(0), width, height);
        pos = new float[]{
                -1, 1,
                -1, 0.8f,
                1, 1,
                1, 0.8f
        };
        widgetThree.setVertex(pos);

        widgetFour.init(mimaps.get(0), width, height);
        pos = new float[]{
                -1f, -0.8f,
                -1, -1,
                1, -0.8f,
                1, -1f
        };
        widgetFour.setVertex(pos);

        widgetOne.init(mimaps.get(1), width, height);
        float[] pos = new float[]{
                -1, 1f,
                -1, -1f,
                1, 1f,
                1, -1f
        };
        widgetOne.setVertex(pos);

        float scaleX = width < height ? 5 : (width == height ? 5 : 5);
        float centerX = 0;
        float centerY = width < height ? 0.8f : (width == height ? 0.8f : 0.8f);
        widgetTwo.init(mimaps.get(2), width, height);
        origin = widgetTwo.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleX);

        posOffset = evaluateShade(origin);
    }


    /**
     * 除了底端y的坐标不变化之外，涉及顶点坐标有：1,3,4,5,7,8 （即 2、6两个坐标不做变化)
     * 1-2两个顶点是负x轴，34是正x轴
     */
    private float[] evaluateShade(float[] origin) {
        float[] offset = new float[8];
        float[] end = new float[8];
        end[0] = origin[0] - 0.02f;
        end[1] = origin[1] + 0.02f;
        //end[1]为顶点
        end[2] = origin[2] - 0.02f;
        end[3] = origin[3] - 0.02f;

        end[4] = origin[4] + 0.02f;
        end[5] = origin[5] + 0.02f;
        //end[5]为1顶点
        end[6] = origin[6] + 0.02f;
        end[7] = origin[7] - 0.02f;
        for (int i = 0; i < end.length; i++) {
            offset[i] = (end[i] - origin[i]) / widgetEneterCount;
        }
        return offset;
    }

    private void arrayAdd() {
        for (int i = 0; i < origin.length; i++) {
            origin[i] = origin[i] + posOffset[i];
        }
    }

    private void arraySub() {
        for (int i = 0; i < origin.length; i++) {
            origin[i] = origin[i] - posOffset[i];
        }
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
