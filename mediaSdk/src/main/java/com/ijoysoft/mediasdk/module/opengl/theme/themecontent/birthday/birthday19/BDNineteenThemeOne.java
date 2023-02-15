package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday19;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;

import java.util.List;

/**
 * 一套组合，一个组合有三个显示元素，三者不一样的进场
 */
public class BDNineteenThemeOne extends BaseThemeExample {
    //前方背景片段,如果后续重复主题多在移到父类
    private final int endConor = 340;
    private final int endConorOffset = -20;
    private float[] originOne = new float[8];
    private int widgetEneterCount = (500 * ConstantMediaSize.FPS / 1000);
    private float[] posOneOffset = new float[8];//计算控件出现动画
    private BaseThemeExample widgetOne;

    public BDNineteenThemeOne(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_ENTER_TIME);
        stayAction = new AnimationBuilder(3000).setWidthHeight(width, height).
                setZView(BDNineteenThemeManager.Z_VIEW).setConors(AnimateInfo.ROTATION.Z_ANXIS, endConor, endConor).build();
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(BDNineteenThemeManager.Z_VIEW).setCoordinate(0, BDNineteenThemeManager.Z_VIEW, 0, 0).
                setConors(AnimateInfo.ROTATION.X_ANXIS, 0, 360).setZView(BDNineteenThemeManager.Z_VIEW).setBgConor(endConorOffset).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setWidthHeight(width, height).setZView(BDNineteenThemeManager.Z_VIEW).setConors(AnimateInfo.ROTATION.Z_ANXIS, endConor, endConor).build();
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, 500, true);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.BOTTOM).
                setCoordinate(0, 1.0f, 0f, 0f).setIsWidget(true).setIsNoZaxis(true)));
        widgetOne.setOutAnimation(new AnimationBuilder(500).setFade(1, 0).setIsWidget(true).setIsNoZaxis(true).
                build());
        widgetOne.setZView(0);
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
            }
            widgetOne.setVertex(originOne);
        }
        widgetOne.drawFrame();
    }

    @Override
    protected void drawFramePre() {
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);

        float scaleX = width < height ? 2.5f : (width == height ? 3f : 2.5f);
        float centerX = 0;
        float centerY = width < height ? 0.75f : (width == height ? 0.7f : 0.65f);
        widgetOne.init(mimaps.get(0), width, height);
        originOne = widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);

        posOneOffset = evaluateShade(originOne);
    }

    @Override
    public int getConor() {
        return endConorOffset;
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
    }

    private void arraySub() {
        for (int i = 0; i < originOne.length; i++) {
            originOne[i] = originOne[i] - posOneOffset[i];
        }
    }

    @Override
    public void adjustImageScaling(int width, int height) {
        adjustImageScalingStretch(width, height);
    }

    @Override
    public float[] getPos() {
        return cube;
    }

    @Override
    public void onDestroy() {
        GLES20.glDeleteProgram(mProgram);
        widgetOne.onDestroy();
    }
}
