package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel5;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 缩小
 */
public class TravelFiveThemeFive extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    //眼镜
    private BaseThemeExample widgetTwo;
    //树木
    private BaseThemeExample widgetThree;
    //朵云
    private BaseThemeExample widgetFour;
    private float[] origin = new float[8];
    private int widgetEneterCount = (500 * ConstantMediaSize.FPS / 1000);
    private float[] posOffset = new float[8];//计算控件出现动画

    public TravelFiveThemeFive(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, 0);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.1f, 1.1f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.1f, 1.1f).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);


        float centerX = -1f;
        float centerY = 1f;
        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetThree.setZView(0);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setScale(0.01f, 1f).build());
        widgetThree.getEnterAnimation().setIsSubAreaWidget(true, centerX, centerY);
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setZView(0);

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetTwo.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true)
                .setZView(0).setOrientation(AnimateInfo.ORIENTATION.TOP).setCoordinate(0, -1, 0, 0)));
        widgetTwo.setZView(0);


        //three
        widgetFour = new BaseThemeExample(totalTime, 3000, 0, DEFAULT_OUT_TIME, true);
        widgetFour.setEnterAnimation(new AnimationBuilder(3000).setZView(0).setIsNoZaxis(true).setCoordinate(-0.2f, 0, 0, 0).build());
        widgetFour.setZView(0.0f);

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
        widgetThree.drawFrame();
        widgetFour.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit,mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.setVertex(pos);
        widgetTwo.init(mimaps.get(1), width, height);
        float centerX = width < height ? 0.7f : (width == height ? 0.6f : 0.8f);
        float centerY = width < height ? -0.7f : -0.7f;
        float scale = width < height ? 4f : (width == height ? 5 : 4);
        origin = widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(),
                mimaps.get(1).getHeight(), centerX, centerY, scale, scale);
        posOffset = evaluateShade(origin);

        scale = width < height ? 3f : (width == height ? 3 : 2);
        centerX = width < height ? -0.7f : (width == height ? -0.75f : -0.8f);
        centerY = width < height ? 0.85f : (width == height ? 0.8f : 0.7f);
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scale, scale);

        //云
        centerX = width <= height ? -0.7f : -0.7f;
        scale = width < height ? 6 : (width == height ? 8 : 10);
        centerY = width < height ? -0.75f : (width == height ? -0.7f : -0.65f);
        widgetFour.init(mimaps.get(3), width, height);
        widgetFour.adjustScaling(width, height, mimaps.get(3).getWidth(),
                mimaps.get(3).getHeight(), centerX, centerY, scale, scale);

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
