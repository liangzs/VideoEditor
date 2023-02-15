package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine15;

import android.graphics.Bitmap;
import android.opengl.Matrix;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WaveAnimation;

import java.util.List;

/**
 * 右边波浪进
 */
public class VTFifteenThemeFour extends VTFifteenBaseExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;

    private float[] origin = new float[8];
    private float widgetEneterCount;
    private float widgetEneterIndex;
    private float widgetStayCount;
    private float[] posOneOffset = new float[8];//计算控件出现动画
    private float[] posEnterOffset = new float[8];//计算控件出现动画

    public VTFifteenThemeFour(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2000, 1000, DEFAULT_OUT_TIME);
        float zview = width < height ? -2.5f : -2;
        stayAction = new AnimationBuilder(2000).setScale(1.1f, 1.0f).setZView(zview).build();
        stayNext = new StayAnimation(weightTime, AnimateInfo.STAY.SPRING_Y, zview, true);
        enterAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.1f, 1.1f).
                setCoordinate(-2.5f, 0f, 0f, 0f).setZView(zview));
        //从0.2开始是因为staynext的最后计算是0.2
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(0f, 0.2f, 0f, 2.5f).setZView(zview).build();
        setZView(zview);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        widgetOne = new BaseThemeExample(totalTime, 500, totalTime - 500, 0);
        widgetOne.setStayAction(new AnimationBuilder(totalTime).setZView(-2.0f).
                setCoordinate(0, 2.5f, 0f, -1.5f).build());
        widgetOne.setZView(-2.0f);

        widgetTwo = new BaseThemeExample(totalTime, 2000, 0, totalTime - 2000);
        widgetTwo.setOutAnimation(new AnimationBuilder(totalTime - 1000).
                setCoordinate(0, 2.5f, 0f, -1.5f).setZView(-2.0f).build());
        widgetTwo.setZView(-2.0f);

        //弹跳压缩效果
        widgetThree = new BaseThemeExample(totalTime, 1000, 3000, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new AnimationBuilder(1000).setCoordinate(0, -2f, 0f, 0f).
                setIsWidget(true).setIsNoZaxis(true).build());
        widgetThree.setStayAction(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsWidget(true).setIsNoZaxis(true).build());

    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        init(bitmap, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        int devideX = width < height ? 1 : 2;
        int devideY = width < height ? 2 : 1;
        float centerX = 0.7f;
        float centerY = -1.4f;
        float scaleX = width < height ? 4 : 8;
        float scaleY = width < height ? 8 : 4;
        widgetOne.adjustScaling(width / devideX, height / devideY, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleY);

        widgetTwo.init(mimaps.get(1), width, height);
        centerX = -0.7f;
        centerY = -1.4f;
        scaleX = width < height ? 4 : 8;
        scaleY = width < height ? 8 : 4;
        widgetTwo.adjustScaling(width / devideX, height / devideY, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleY);

        //three
        float scale = width < height ? 3 : 4;
        widgetThree.init(mimaps.get(2), width, height);
        origin = widgetThree.adjustScaling(width, height, mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), AnimateInfo.ORIENTATION.BOTTOM, 0, scale);
        evaluateShade();
    }


    /**
     * arraySub(true);是arraySub(false)的两倍速度
     */
    @Override
    public void drawWiget() {
        super.drawWiget();
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        if (frameIndex > stayCount - widgetEneterCount && frameIndex < stayCount + widgetEneterCount) {
            arrayAdd(true);
        } else {
            if (getStatus() != ActionStatus.ENTER) {
                if (widgetEneterIndex < 0.6 * widgetStayCount) {
                    for (int i = 0; i < origin.length; i++) {
                        origin[i] = origin[i] - posEnterOffset[i] * 2;
                    }
                } else if (widgetEneterIndex > 0.6f * widgetStayCount && widgetEneterIndex < widgetStayCount * 1.5f) {
                    arrayAdd(false);
                } else if (widgetEneterIndex > widgetStayCount * 1.5 && widgetEneterIndex < widgetStayCount * 2.0f) {
                    arraySub(false);
                }
                widgetEneterIndex++;
            }
        }
        widgetThree.setVertex(origin);
        widgetThree.drawFrame();
    }

    /**
     * 除了底端y的坐标不变化之外，涉及顶点坐标有：1,3,4,5,7,8 （即 2、6两个坐标不做变化)
     * 1-2两个顶点是负x轴，34是正x轴
     */
    private void evaluateShade() {
        float[] end = new float[8];
        end[0] = origin[0] - 0.05f;
        end[1] = 1f;
        //end[1]为顶点
        end[2] = origin[2] - 0.05f;
        end[3] = origin[3] + 0.05f;
        end[4] = origin[4] + 0.05f;
        end[5] = 1f;
        //end[5]为1顶点
        end[6] = origin[6] + 0.05f;
        end[7] = origin[7] + 0.05f;
        widgetEneterCount = (500 * ConstantMediaSize.FPS / 1000);
        widgetStayCount = (1000 * ConstantMediaSize.FPS / 1000);
        for (int i = 0; i < end.length; i++) {
            posEnterOffset[i] = (end[i] - origin[i]) / widgetEneterCount;
        }

        end[0] = origin[0] - 0.04f;
        end[1] = 1f;
        //end[1]为顶点
        end[2] = origin[2] - 0.04f;
        end[3] = origin[3] + 0.04f;
        end[4] = origin[4] + 0.04f;
        end[5] = 1f;
        //end[5]为1顶点
        end[6] = origin[6] + 0.04f;
        end[7] = origin[7] + 0.04f;
        for (int i = 0; i < end.length; i++) {
            posOneOffset[i] = (end[i] - origin[i]) / widgetStayCount;
        }
    }

    private void arrayAdd(boolean isEnter) {
        if (isEnter) {
            for (int i = 0; i < origin.length; i++) {
                origin[i] = origin[i] + posEnterOffset[i];
            }
        } else {
            for (int i = 0; i < origin.length; i++) {
                origin[i] = origin[i] + posOneOffset[i];
            }
        }

    }

    private void arraySub(boolean isEnter) {
        if (isEnter) {
            for (int i = 0; i < origin.length; i++) {
                origin[i] = origin[i] - posEnterOffset[i];
            }
        } else {
            for (int i = 0; i < origin.length; i++) {
                origin[i] = origin[i] - posOneOffset[i];
            }
        }
    }

    @Override
    protected void wedThemeSpecialDeal() {
        Matrix.translateM(mViewMatrix, 0, 0f, -0.1f, 0f);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
    }
}
