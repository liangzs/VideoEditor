package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday2;

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
 * stay 缩小
 */
public class BirthDayTwoThemeThree extends BaseThemeExample {
    //挤压变形
    private BaseThemeExample widgetOne;
    private float[] origin = new float[8];

    private float widgetEneterCount;
    private float widgetEneterIndex;
    private float[] posOneOffset = new float[8];//计算控件出现动画

    public BirthDayTwoThemeThree(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.1f, 1.1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT).setScale(1.1f, 1.1f));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 0f, 2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -2f, 0f, 0f).
                setIsWidget(true).setIsNoZaxis(true).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0f, 2f).
                setIsWidget(true).setIsNoZaxis(true).build());
        widgetOne.setStayAction(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsWidget(true).setIsNoZaxis(true).build());
    }

    @Override
    public void drawWiget() {
        if (getStatus() == ActionStatus.STAY) {
            if (widgetEneterIndex < widgetEneterCount) {
                arrayAdd();
            } else if (widgetEneterIndex > widgetEneterCount && widgetEneterIndex < widgetEneterCount * 2) {
                arraySub();
            }
            widgetEneterIndex++;
            widgetOne.setVertex(origin);
        }
        widgetOne.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap bitmap1, List<Bitmap> list, int width, int height) {
        super.init(bitmap, bitmap1, list, width, height);
        //one
        widgetOne.init(list.get(0), width, height);
        float scale = width < height ? 2 : (width == height ? 1.5f : 1.5f);
        origin = widgetOne.adjustScaling(width, height / 2, list.get(0).getWidth(), list.get(0).getHeight(), AnimateInfo.ORIENTATION.BOTTOM, scale);
        evaluateShade();
    }

    /**
     * 除了底端y的坐标不变化之外，涉及顶点坐标有：1,3,4,5,7,8 （即 2、6两个坐标不做变化)
     * 1-2两个顶点是负x轴，34是正x轴
     */
    private void evaluateShade() {
        float[] end = new float[8];
        end[0] = origin[0] - 0.1f;
        end[1] = 1f;
        //end[1]为顶点
        end[2] = origin[2] - 0.1f;
        end[3] = origin[3] + 0.1f;
        end[4] = origin[4] + 0.1f;
        end[5] = 1f;
        //end[5]为1顶点
        end[6] = origin[6] + 0.1f;
        end[7] = origin[7] + 0.1f;
        widgetEneterCount = (500 * ConstantMediaSize.FPS / 1000);
        for (int i = 0; i < end.length; i++) {
            posOneOffset[i] = (end[i] - origin[i]) / widgetEneterCount;
        }
    }

    private void arrayAdd() {
        for (int i = 0; i < origin.length; i++) {
            origin[i] = origin[i] + posOneOffset[i];
        }
    }

    private void arraySub() {
        for (int i = 0; i < origin.length; i++) {
            origin[i] = origin[i] - posOneOffset[i];
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
    }
}
