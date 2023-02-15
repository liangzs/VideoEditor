package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine11;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 缩小
 */
public class VTElevenThemeThree extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetBorder;

    //心缩放
    private float[] originOne = new float[8];
    private int widgetEneterCount = (500 * ConstantMediaSize.FPS / 1000);
    private float[] posOneOffset = new float[8];//计算控件出现动画

    public VTElevenThemeThree(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.1f, 1.1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT).setScale(1.1f, 1.1f));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 0f, 2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;

        widgetBorder = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetBorder.setEnterAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT)));
        widgetBorder.setOutAnimation(new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 0f, 2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM)));
        widgetBorder.setZView(0);
    }

    @Override
    public void initWidget() {
        //widget
        float move = -1.0f;
        if (ConstantMediaSize.ratioType == RatioType._16_9) {
            move = 1.0f;
        }

        //widget
        widgetOne = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, 1700, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.BOTTOM).
                setCoordinate(0, move, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true)));
        widgetOne.setZView(0);
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());


        widgetTwo = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setZView(-0);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(0f, 1f)
                .build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());

        widgetThree = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetThree.setZView(0);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setCoordinate(0, 1f, 0f, 0f)
                .build());
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());

    }


    @Override
    public void drawWiget() {
        widgetBorder.drawFrame();
        if (getStatus() != ActionStatus.ENTER) {
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
            widgetTwo.setVertex(originOne);
        }
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float[] pos = width < height ? new float[]{
                -1.0f, -0.75f,
                -1.0f, -1.0f,
                1.0f, -0.75f,
                1.0f, -1.0f
        } : (width == height ? new float[]{
                -1.0f, -0.65f,
                -1.0f, -1.0f,
                1.0f, -0.65f,
                1.0f, -1.0f
        } : new float[]{
                -1.0f, 1.0f,
                -1.0f, 0.6f,
                1.0f, 1.0f,
                1.0f, 0.6f,
        });

        widgetOne.setVertex(pos);
        widgetTwo.init(mimaps.get(1), width, height);
        //顶点坐标
        float centerX = -0.8f;
        float centerY = -0.75f;
        originOne = widgetTwo.adjustScaling((int) (width / 2f), (int) (height / 4), mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, 5, 10);
        posOneOffset = evaluateShade(originOne);

        widgetThree.init(mimaps.get(2), width, height);
        centerX = 0.7f;
        centerY = 0.8f;
        widgetThree.adjustScaling((int) (width / 2f), (int) (height / 4), mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, 4, 8);
        //边框
        widgetBorder.init(mimaps.get(3), width, height);
        pos = new float[]{
                -1.0f, 1.0f,
                -1.0f, -1.0f,
                1.0f, 1.0f,
                1.0f, -1.0f,
        };
        widgetBorder.setVertex(pos);
    }

//    @Override
//    public void adjustImageScaling(int width, int height) {
//        adjustImageScalingStretch(width, height);
//    }


    /**
     * 心缩放
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
        for (int i = 0; i < originOne.length; i++) {
            originOne[i] = originOne[i] + posOneOffset[i];
        }
    }

    private void arraySub() {
        for (int i = 0; i < originOne.length; i++) {
            originOne[i] = originOne[i] - posOneOffset[i];
        }
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
