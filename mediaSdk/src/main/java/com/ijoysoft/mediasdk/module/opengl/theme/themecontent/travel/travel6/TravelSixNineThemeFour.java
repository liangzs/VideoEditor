package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel6;

import android.graphics.Bitmap;
import android.opengl.Matrix;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.EffectFiveBaseExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WaveAnimation;

import java.util.List;

/**
 * 右边波浪进
 */
public class TravelSixNineThemeFour extends EffectFiveBaseExample {
    private BaseThemeExample widgetOne;

    public TravelSixNineThemeFour(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 2000, 1000, DEFAULT_OUT_TIME);
        stayAction = new AnimationBuilder(2000).setScale(1.1f, 1.0f).setZView(TravelSixThemeManager.DEFAUT_ZVIEW).build();
        stayNext = new StayAnimation(weightTime, AnimateInfo.STAY.SPRING_Y, TravelSixThemeManager.DEFAUT_ZVIEW, true);
        enterAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setScale(1.1f, 1.1f).
                setCoordinate(-2f, 0f, 0f, 0f).setZView(TravelSixThemeManager.DEFAUT_ZVIEW));
        //从0.2开始是因为staynext的最后计算是0.2
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(0f, 0.2f, 0f, 2f).setZView(TravelSixThemeManager.DEFAUT_ZVIEW).build();
        setZView(TravelSixThemeManager.DEFAUT_ZVIEW);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        widgetOne = new BaseThemeExample(totalTime, 3000, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(3000).
                setCoordinate(0, 1, 0f, 0f).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setIsNoZaxis(true).setZView(0)));
        widgetOne.setZView(0);

    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        init(bitmap, width, height);
        float scale = width < height ? 2 : (width == height ? 3 : 3);
        float centerX = -0.5f;
        float centerY = 0.7f;
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
    }


    @Override
    public void drawWiget() {
        super.drawWiget();
        widgetOne.drawFrame();
    }

    @Override
    protected void wedThemeSpecialDeal() {
        Matrix.translateM(mViewMatrix, 0, 0f, -0.1f, 0);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }
}
