package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding1;

import android.graphics.Bitmap;
import android.opengl.Matrix;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.WedTextMoveEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.ThemeWedStayAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WeddingTextStayAddRotate;

import java.util.List;

/**
 * 上进，上下悬停，左划出
 */
public class WeddingActionFive extends BaseThemeExample {
    private BaseThemeExample widgetOne;

    public WeddingActionFive(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new ThemeWedStayAnimation(3000, -10f, WeddingOneThemeManager.Z_VIEW);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, -2.0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 0).setZView(WeddingOneThemeManager.Z_VIEW).build();
        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(1, 0).setZView(WeddingOneThemeManager.Z_VIEW).build();


        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME);
        widgetOne.setStayAction(new WeddingTextStayAddRotate(1600));
        widgetOne.setEnterAnimation(new WedTextMoveEvaluate(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setScale(0.1f, 1f).setWidthHeight(width, height)));
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME)
                .setFade(1f, 0.0f).build());
        float centerY = width <= height ? 0.9f : 1f;
        ((WedTextMoveEvaluate) widgetOne.getEnterAnimation()).setRatateCenter(0, centerY);
        widgetOne.getOutAnimation().setIsSubAreaWidget(true, 0, centerY);
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scale = width < height ? 1.5f : (width == height ? 2f : 2f);
        float centerX = 0;
        float centerY = width <= height ? 0.9f : 1f;
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
    }

    @Override
    protected void wedThemeSpecialDeal() {
        super.wedThemeSpecialDeal();
        Matrix.translateM(mViewMatrix, 0, 0f, -0.2f, 0);
    }    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }
}
