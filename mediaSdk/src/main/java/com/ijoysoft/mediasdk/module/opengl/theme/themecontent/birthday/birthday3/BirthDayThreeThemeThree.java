package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday3;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WaveAnimation;

import java.util.List;

/**
 * 右边波浪进
 * 添加气球飞起粒子
 * 实际有三个气球
 * <p>
 * //TODO 换成控件方案
 */
public class BirthDayThreeThemeThree extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;

    private BaseThemeExample widgetFour;

    public BirthDayThreeThemeThree(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new AnimationBuilder(weightTime).
                setCoordinate(0f, -0.1f, 0f, 0).setZView(-2).build();
        enterAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(2f, 0f, 0, -0.1f).setZView(-2));
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(0f, 0f, 0f, 2).setZView(-2).build();
        setZView(-2);
//        bdThreeDrawerTwo = new BDThreeDrawerTwo(new BDThreeSystemTwo(3));
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, width, height);
        Bitmap widgetBitmap = mimaps.get(0);
        widgetOne.init(widgetBitmap, width, height);
        float centerX = -0.7f;
        float centerY = -1.3f;
        float scale = width < height ? 6 : 6;
        widgetOne.adjustScaling(width, height, widgetBitmap.getWidth(), widgetBitmap.getHeight(), centerX, centerY, scale, scale);
        //two
        widgetBitmap = mimaps.get(1);
        widgetTwo.init(widgetBitmap, width, height);
        centerX = 0.7f;
        centerY = -1.3f;
        widgetTwo.adjustScaling(width, height, widgetBitmap.getWidth(), widgetBitmap.getHeight(), centerX, centerY, scale, scale);
        //three
        widgetBitmap = mimaps.get(2);
        widgetThree.init(widgetBitmap, width, height);
        centerX = 0.8f;
        centerY = -1.3f;
        widgetThree.adjustScaling(width, height, widgetBitmap.getWidth(), widgetBitmap.getHeight(), centerX, centerY, scale, scale);

        widgetFour.init(mimaps.get(3), width, height);
        scale = width < height ? 2 : 2f;
        widgetFour.adjustScaling(width, height / 2, mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), AnimateInfo.ORIENTATION.BOTTOM, scale);
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, totalTime - 1000, 0, 0);
        widgetOne.setEnterAnimation(new AnimationBuilder(totalTime - 1000).setIsWidget(true).setCoordinate(0, 2f, 0f, -0.5f).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).build());
        //two
        widgetTwo = new BaseThemeExample(totalTime, totalTime - 1000, 0, 0);
        widgetTwo.setEnterAnimation(new AnimationBuilder(totalTime - 1000).setCoordinate(0, 2, 0f, -1f).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setIsWidget(true).build());

        //three
        widgetThree = new BaseThemeExample(totalTime, 2000, 0, totalTime);
        widgetThree.setOutAnimation(new AnimationBuilder(totalTime - 2000).setCoordinate(0, 2f, 0f, -0.5f).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setIsWidget(true).build());


        widgetFour = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetFour.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 1.0f, 0f, 0f).setIsNoZaxis(true).setZView(0).build());
        widgetFour.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0f, 1f).setIsNoZaxis(true).setZView(0).build());
        widgetFour.setZView(0);
    }


    @Override
    protected void drawFramePre() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
    }

    @Override
    public void drawWiget() {
        super.drawWiget();
        widgetFour.drawFrame();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
        widgetFour.onDestroy();
    }


    @Override
    public String printOutString() {
        return this.getClass().getName() + ":getTexture:" + getTextureId() + ",wighet_one:" + widgetOne.getTexture()
                + ",widgetTwo:" + widgetTwo.getTexture() + ",widgetThree:" + widgetThree.getTexture() + ",widgetFour:" + widgetFour.getTexture();
    }
}
