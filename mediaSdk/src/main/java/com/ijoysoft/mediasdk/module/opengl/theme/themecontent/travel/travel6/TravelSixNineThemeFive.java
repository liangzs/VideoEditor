package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel6;

import android.graphics.Bitmap;
import android.opengl.Matrix;

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
public class TravelSixNineThemeFive extends EffectFiveBaseExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    public TravelSixNineThemeFive(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 2000, 1000, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(2000, AnimateInfo.STAY.SPRING_Y, TravelSixThemeManager.DEFAUT_ZVIEW);
        stayNext = new AnimationBuilder(weightTime).setScale(1.0f, 1.1f).setZView(TravelSixThemeManager.DEFAUT_ZVIEW).build();
        enterAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(0f, -2f, 0f, 0).setZView(TravelSixThemeManager.DEFAUT_ZVIEW).build();
        outAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setScale(1.1f, 1.1f).
                setCoordinate(0f, 0f, 2f, 0f).setZView(TravelSixThemeManager.DEFAUT_ZVIEW));
        setZView(TravelSixThemeManager.DEFAUT_ZVIEW);
    }

    @Override
    public void initWidget() {
        //two
        widgetOne = new BaseThemeExample(totalTime, 1500, 3000, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(1500).setZView(0).setIsNoZaxis(true).setEnterDelay(1000)
                .setFade(0, 1).build());
        widgetOne.setZView(-0.0f);


        widgetTwo = new BaseThemeExample(totalTime, 2000, 3000, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(2000).setZView(0).setIsNoZaxis(true).setEnterDelay(1500)
                .setFade(0, 1).build());
        widgetTwo.setZView(-0.0f);


    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, width, height);
        float scale = width < height ? 4f : (width == height ? 4f : 3f);
        float centerX = width < height ? -0.6f : -0.6f;
        float centerY = width < height ? 0.7f : (width == height ? 0.7f : 0.6f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
        //three
        centerY = width < height ? 0.2f : (width == height ? 0 : -0.2f);
        widgetTwo.init(mimaps.get(0), width, height);
        widgetTwo.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);

    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }

    @Override
    protected void wedThemeSpecialDeal() {
        Matrix.translateM(mViewMatrix, 0, 0f, -0.1f, 0);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }

}
