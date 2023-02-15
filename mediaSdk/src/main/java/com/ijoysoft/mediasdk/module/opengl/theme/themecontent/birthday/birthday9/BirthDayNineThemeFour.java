package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday9;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.EffectFiveBaseExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WaveAnimation;

import java.util.List;

/**
 * 右边波浪进
 */
public class BirthDayNineThemeFour extends EffectFiveBaseExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    public BirthDayNineThemeFour(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 2000, 1000, DEFAULT_OUT_TIME);
        stayAction = new AnimationBuilder(2000).setScale(1.1f, 1.0f).setZView(-2).build();
        stayNext = new StayAnimation(weightTime, AnimateInfo.STAY.SPRING_Y, -2, true);
        enterAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setScale(1.1f, 1.1f).
                setCoordinate(-2f, 0f, 0f, 0f).setZView(-2));
        //从0.2开始是因为staynext的最后计算是0.2
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(0f, 0.2f, 0f, 2f).setZView(-2).build();
        setZView(-2);
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        init(bitmap, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float scale = width < height ? 1f : (width == height ? 1.0f : 1.5f);
        float centerY = 0.8f;
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), 0, centerY, scale, scale);
        widgetTwo.init(mimaps.get(1), width, height);
        //顶点坐标
        pos = width <= height ? new float[]{
                0.2f, -0.85f,
                0.2f, -1.05f,
                1.05f, -0.85f,
                1.05f, -1.05f,
        } : new float[]{
                0.4f, -0.75f,
                0.4f, -1.05f,
                1.05f, -0.75f,
                1.05f, -1.05f,
        };
        widgetTwo.setVertex(pos);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 1.0f, 0f, 0f).setEnterDelay(DEFAULT_ENTER_TIME).setZView(-2.5f).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(2.5f).setCoordinate(0, 0, 0, 1f).build());
        widgetOne.setZView(-2.5f);

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_ENTER_TIME);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -1f, 0, -0f).setZView(-2.5f).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.5f).setCoordinate(0, 0, 0, -1f).build());
        widgetTwo.setZView(-2.5f);
    }

    @Override
    public void drawWiget() {
        super.drawWiget();
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }
}
