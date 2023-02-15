package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine9;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 缩小-放大-缩小
 */
public class VTNineThemeFour extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private int widgetEneterCount = (500 * ConstantMediaSize.FPS / 1000);
    private float[] originTwo = new float[8];
    private float[] posOffsetTwo = new float[8];//计算控件出现动画

    public VTNineThemeFour(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.2f, 1.2f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f).setIsNoZaxis(true).setZView(0).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setZView(0);


        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        widgetTwo.setZView(-2.5f);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.5f).setFade(0, 1f).build());
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
            widgetTwo.setVertex(originTwo);
        }
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit,mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.setVertex(pos);

        widgetTwo.init(mimaps.get(1), width, height);
        float scale = width < height ? 4 : (width == height ? 5 : 4);
        float centerX = width < height ? 0.0f : 0.1f;
        float centerY = width < height ? 0.8f : (width == height ? 0.8f : 0.7f);
        originTwo = widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);
        posOffsetTwo = evaluateShade(originTwo);
    }


//    @Override
//    public void adjustImageScaling(int width, int height) {
//        adjustImageScalingStretch(width, height);
//    }

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
        for (int i = 0; i < 8; i++) {
            originTwo[i] = originTwo[i] + posOffsetTwo[i];
        }
    }

    private void arraySub() {
        for (int i = 0; i < 8; i++) {
            originTwo[i] = originTwo[i] - posOffsetTwo[i];
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }
}
