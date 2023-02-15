package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday7;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday14.particle.BDFourteenDrawerOne;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday14.particle.BDFourteenSystemOne;

import java.util.List;

/**
 * 左上角z轴翻滚进，右下角z轴滚出
 */
public class HDSevenThemeFour extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;

    private float[] originTwo = new float[8];
    private float[] originThree = new float[8];
    private int widgetEneterCount = (500 * ConstantMediaSize.FPS / 1000);
    private float[] posTwoOffset = new float[8];//计算控件出现动画
    private float[] posThreeOffset = new float[8];//计算控件出现动画
    private BDFourteenDrawerOne mBDFourteenDrawerOne;

    public HDSevenThemeFour(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.SPRING_Y);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, -2f, 0, 0).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 360).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 2f, 2f).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 180).setWidthHeight(width, height).build();
        mBDFourteenDrawerOne = new BDFourteenDrawerOne(new BDFourteenSystemOne(3500));
    }


    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetOne.setZView(0);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(-0, 1f, 0f, 0f)
                .build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0f, 1f)
                .setIsNoZaxis(true).setZView(0).build());
        //two
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetTwo.setZView(-2.5f);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.5f).setCoordinate(-1, 0f, 0f, 0f)
                .build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(1, 0f, 0f, 2f)
                .setZView(-2.5f).build());
        //three
        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetThree.setZView(-2.5f);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.5f).setCoordinate(1, 0f, 0f, 0f)
                .build());
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(1, 0f, 0f, 0f)
                .setZView(-2.5f).build());

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
            widgetThree.setVertex(originThree);
        }
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
        mBDFourteenDrawerOne.onDrawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scaleX = width <= height ? 1.5f : 2f;
        float scaleY = width <= height ? 1.5f : 2f;
        float centerX = width <= height ? 0.1f : 0.4f;
        float centerY = width <= height ? 0.8f : 0.7f;
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(),
                mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleY);
        //two
        scaleX = 5f;
        scaleY = 5f;
        centerX = width <= height ? -0.8f : -0.9f;
        centerY = width <= height ? 0.8f : 0.7f;
        widgetTwo.init(mimaps.get(1), width, height);
        originTwo = widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(),
                mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleY);
        centerX = width < height ? 0.8f : 0.85f;
        centerY = width < height ? -0.8f : -0.7f;
        scaleX = 5f;
        scaleY = 5f;
        widgetThree.init(mimaps.get(2), width, height);
        originThree = widgetThree.adjustScaling(width, height, mimaps.get(2).getWidth(),
                mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleY);

        posTwoOffset = evaluateShade(originTwo);
        posThreeOffset = evaluateShade(originThree);
        mBDFourteenDrawerOne.init(mimaps.get(3));

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
        for (int i = 0; i < originTwo.length; i++) {
            originTwo[i] = originTwo[i] + posTwoOffset[i];
        }
        for (int i = 0; i < originThree.length; i++) {
            originThree[i] = originThree[i] + posThreeOffset[i];
        }
    }

    private void arraySub() {
        for (int i = 0; i < originTwo.length; i++) {
            originTwo[i] = originTwo[i] - posTwoOffset[i];
        }
        for (int i = 0; i < originThree.length; i++) {
            originThree[i] = originThree[i] - posThreeOffset[i];
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
        mBDFourteenDrawerOne.onDestroy();
    }
}
