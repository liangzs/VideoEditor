package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love1;

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
 * @ author       lfj
 * @ date         2020/10/20
 * @ description
 */
public class LoveOneThemeFive extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;

    private float[] originTwo = new float[8];
    private float[] originThree = new float[8];
    private int widgetEneterCount = (500 * ConstantMediaSize.FPS / 1000);
    private float[] posTwoOffset = new float[8];//计算控件出现动画
    private float[] posThreeOffset = new float[8];//计算控件出现动画
//    private LoveOneLoveDrawer loveOneLoveDrawer;

    public LoveOneThemeFive(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 2, 0.1f, 1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 2f, 0f).
                setOrientation(AnimateInfo.ORIENTATION.LEFT));
        this.isNoZaxis = isNoZaxis;

        //one
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setZView(0);
        //two
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetTwo.setZView(-2.5f);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.5f).setCoordinate(-1, 0f, 0f, 0f)
                .build());
        //three
        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetThree.setZView(-2.5f);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.5f).setCoordinate(1, 0f, 0f, 0f)
                .build());
//        loveOneLoveDrawer = new LoveOneLoveDrawer();
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
//        loveOneLoveDrawer.onDrawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);

        widgetOne.init(mimaps.get(0), width, height);
        //顶点坐标
        pos = width == height ? new float[]{
                -1f, 1f,
                -1f, 0.7f,
                1f, 1f,
                1f, 0.7f,
        } : (width < height ? new float[]{
                -1f, 1f,
                -1f, 0.7f,
                1f, 1f,
                1f, 0.7f,
        } : new float[]{
                -1f, 1f,
                -1f, 0.7f,
                1f, 1f,
                1f, 0.7f,
        });
        widgetOne.setVertex(pos);

        float scaleX = width == height ? 5f : (width < height ? 5f : 3f);
        float scaleY = width == height ? 5f : (width < height ? 5f : 3f);
        float centerX = width == height ? -0.2f : (width < height ? -0.2f : -0.3f);
        float centerY = width == height ? 0.8f : (width < height ? 0.7f : 0.7f);
        widgetTwo.init(mimaps.get(1), width, height);
        originTwo = widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(),
                mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleY);

        scaleX = width == height ? 5f : (width < height ? 5f : 3f);
        scaleY = width == height ? 5f : (width < height ? 5f : 3f);
        centerX = width == height ? 0.4f : (width < height ? 0.4f : 0.5f);
        centerY = width == height ? 0.6f : (width < height ? 0.5f : 0.5f);
        widgetThree.init(mimaps.get(2), width, height);
        originThree = widgetThree.adjustScaling(width, height, mimaps.get(2).getWidth(),
                mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleY);

        posTwoOffset = evaluateShade(originTwo);
        posThreeOffset = evaluateShade(originThree);
//        loveOneLoveDrawer.init(mimaps.subList(1, 2));
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
    }

}
