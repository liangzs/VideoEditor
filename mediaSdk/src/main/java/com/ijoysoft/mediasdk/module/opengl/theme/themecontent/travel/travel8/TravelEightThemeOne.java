package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel8;

import android.graphics.Bitmap;
import android.opengl.Matrix;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.ThemeWedStayAnimation;

import java.util.List;

/**
 * 上进，上下悬停，左划出
 */
public class TravelEightThemeOne extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    public TravelEightThemeOne(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new ThemeWedStayAnimation(3000, -10f);
        stayAction.setZView(TravelEightThemeManager.DEFAUT_ZVIEW);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -2.0f, 0, 0).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 0).setZView(TravelEightThemeManager.DEFAUT_ZVIEW).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, -2.5f, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 180).setZView(TravelEightThemeManager.DEFAUT_ZVIEW).build();
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.BOTTOM).
                setCoordinate(0, 1.0f, 0f, 0f).setEnterDelay(500).setIsNoZaxis(true).setZView(0)));
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());
        widgetOne.setZView(-0.0f);


        widgetTwo = new BaseThemeExample(totalTime - DEFAULT_ENTER_TIME, 3000, 0, DEFAULT_ENTER_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(3000).setIsNoZaxis(true)
                .setZView(0).setCoordinate(-1, 0, 0, 0).build());
        widgetTwo.setZView(0);
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float centerX = 0;
        float centerY = width < height ? 0.85f : 0.85f;
        float scale = width < height ? 2f : 2;
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(),
                mimaps.get(0).getHeight(), centerX, centerY, scale, scale);

        scale = width < height ? 4f : (width == height ? 5 : 5);
        centerX = width <= height ? 0.6f : 0.7f;
        centerY = width < height ? -0.7f : -0.6f;
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(),
                mimaps.get(1).getHeight(), centerX, centerY, scale, scale);
    }

    @Override
    protected void wedThemeSpecialDeal() {
        super.wedThemeSpecialDeal();
        Matrix.translateM(mViewMatrix, 0, 0f, TravelEightThemeManager.ZVIEW_OFFSET, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }
}
