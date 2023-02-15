package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby2;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayShakeAnimation;

import java.util.List;

/**
 * 缩小
 * 这里在悬停缩放时，添加滤镜
 */
public class BabyTwoThemeOne extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;

    public BabyTwoThemeOne(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.2f, 1.2f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setZView(0);

        float location = width < height ? -1 : 1;
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setZView(0);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(0, location, 0, 0).build());
        //three
        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetThree.setZView(0);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(-1f, 0, 0, 0).build());
        widgetThree.setStayAction(new StayShakeAnimation(2500, width, height, -1f, 1f));
        //four-text
        widgetFour = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetFour.setZView(0);
        widgetFour.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(1, 0, 0, 0).build());
        widgetFour.setStayAction(new StayShakeAnimation(2500, width, height, 1f, 1f));

    }

    @Override
    public void drawWiget() {
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

        int devideX = width < height ? 1 : 2;
        int devideY = width < height ? 2 : 1;
        float scaleX = width < height ? 2 : 2;
        float scaleY = width < height ? 4 : 1;
        float centerX = 0;
        float centerY = width < height ? -0.8f : 0.8f;
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width / devideX, height / devideY,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleY);

        //两个树枝
        widgetThree.init(mimaps.get(2), width, height);
        //顶点坐标
        float pos[] = width < height ? new float[]{
                -1.0f, 1.0f,
                -1.0f, 0.7f,
                -0.5f, 1.0f,
                -0.5f, 0.7f,
        } : new float[]{
                -1.0f, 1.0f,
                -1.0f, 0.4f,
                -0.7f, 1.0f,
                -0.7f, 0.4f,
        };

        widgetThree.setVertex(pos);

        widgetFour.init(mimaps.get(3), width, height);
        pos = width < height ? new float[]{
                0.5f, 1.0f,
                0.5f, 0.7f,
                1.0f, 1.0f,
                1.0f, 0.7f,
        } : new float[]{
                0.7f, 1.0f,
                0.7f, 0.4f,
                1.0f, 1.0f,
                1.0f, 0.4f,
        };
        widgetFour.setVertex(pos);
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
