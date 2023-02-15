package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday1;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;

import static com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday1.BDOneThemeManager.BD_ONE_ZVIEW;

/**
 * 左上角z轴翻滚进，右下角z轴滚出
 */
public class BirthDayActionFour extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;

    public BirthDayActionFour(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.SPRING_Y, BD_ONE_ZVIEW);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, -2f, 0, 0).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 360).setZView(BD_ONE_ZVIEW).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 2f, 2f).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 180).setZView(BD_ONE_ZVIEW).setWidthHeight(width, height).build();
        setZView(BD_ONE_ZVIEW);
    }


    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsWidget(true).
                setCoordinate(-2f, 0f, 0, 0).setFade(-0.5f, 1f).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsWidget(true).setCoordinate(0, 0f, 0, 2f).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).build());
        //two
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(-0.5f, 1f).setCoordinate(2f, 0f, 0, 0).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setIsWidget(true).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsWidget(true).setCoordinate(0, 0f, 0, 2f).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).build());
        widgetOne.setZView(-2);
        widgetTwo.setZView(-2);
        //three
        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(-0.5f, 1f).
                setCoordinate(0f, -1f, 0f, -0f).setIsNoZaxis(true).setZView(0).build());
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setFade(1.0f, 0).build());
        widgetThree.setZView(0);


    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        //one
        widgetOne.init(mimaps.get(0), width, height);
        float scale = width <= height ? 8 : 5;
        float centerX = -0.5f;
        float centerY = 0.6f;
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
        //two
        widgetTwo.init(mimaps.get(1), width, height);
        centerX = 0.5f;
        centerY = 0.6f;
        widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);
        //three
        widgetThree.init(mimaps.get(2), width, height);
        //顶点坐标
        float pos[] = width < height ? new float[]{
                -1.0f, -0.8f,
                -1.0f, -1.0f,
                1.0f, -0.8f,
                1.0f, -1.0f,
        } : (width == height ? new float[]{
                -1.0f, -0.7f,
                -1.0f, -1.0f,
                1.0f, -0.7f,
                1.0f, -1.0f,
        } : new float[]{
                -0.6f, -0.7f,
                -0.6f, -1.0f,
                0.6f, -0.7f,
                0.6f, -1.0f,
        });
        widgetThree.setVertex(pos);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
    }

}
