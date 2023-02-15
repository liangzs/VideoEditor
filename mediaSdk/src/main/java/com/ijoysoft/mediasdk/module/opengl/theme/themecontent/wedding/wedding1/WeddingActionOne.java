package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding1;

import android.graphics.Bitmap;
import android.opengl.Matrix;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.ThemeWedStayAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WeddingTextStayAnimation;

import java.util.List;

/**
 * 上进，上下悬停，左划出
 */
public class WeddingActionOne extends BaseThemeExample {
    private BaseThemeExample widgetOne;

    public WeddingActionOne(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new ThemeWedStayAnimation(3000, -10f, WeddingOneThemeManager.Z_VIEW);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -2.0f, 0, 0).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 0).setZView(WeddingOneThemeManager.Z_VIEW).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, -2.5f, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 180).setZView(WeddingOneThemeManager.Z_VIEW).build();
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }

    @Override
    public void initWidget() {
        super.initWidget();
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME);
        widgetOne.setStayAction(new WeddingTextStayAnimation(3000, 4, 0.2f, 1f));
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2.0f, 0, 0).
                setScale(0.1f, 1f).build());
        widgetOne.setOutAnimation(new AnimationBuilder(500).setCoordinate(0, 0, -2f, 0).build());
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scale = width < height ? 1.5f : (width == height ? 2f : 2f);
        float centerX = 0;
        float centerY = width <= height ? 1.0f :1.1f;
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
    }

    @Override
    protected void wedThemeSpecialDeal() {
        super.wedThemeSpecialDeal();
        Matrix.translateM(mViewMatrix, 0, 0f, -0.2f, 0);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }
}
