package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration5;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;

/**
 * 左y轴翻转进，左下角出z轴翻转出
 */
public class CDFiveThemeTwo extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    private float[] origin = new float[8];
    private int widgetEneterCount = (500 * ConstantMediaSize.FPS / 1000);
    private float[] posOffset = new float[8];//计算控件出现动画
    private CDFiveDrawer mCDFiveDrawer;

    public CDFiveThemeTwo(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.ROTATE_Z, width, height);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 360).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, -2f, 2f).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 360).setWidthHeight(width, height).build();
        mCDFiveDrawer = new CDFiveDrawer();
    }


    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(-1, 0, 0f, 0f).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setFade(1, 0).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setZView(0);
        //two
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 1, 0f, 0f).setIsNoZaxis(true).setZView(0).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setFade(1, 0).setIsNoZaxis(true).setZView(0).build());
        widgetTwo.setZView(0);
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
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        mCDFiveDrawer.onDrawFrame();

    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scale = width < height ? 3f : (width == height ? 4f : 4);
        float centerX = -0.5f;
        float centerY = 0.35f;
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
        //two
        scale = width < height ? 3f : (width == height ? 4f : 4);
        centerX = 0.7f;
        centerY = width <= height ? 0.8f : 0.7f;
        widgetTwo.init(mimaps.get(1), width, height);
        origin = widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);
        posOffset = evaluateShade(origin);
        mCDFiveDrawer.init(mimaps.subList(2, 5));
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
        mCDFiveDrawer.onDestroy();
    }
}
