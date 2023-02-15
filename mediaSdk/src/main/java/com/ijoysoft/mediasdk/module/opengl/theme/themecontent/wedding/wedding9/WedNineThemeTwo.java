package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding9;

import android.graphics.Bitmap;
import android.opengl.Matrix;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;

/**
 * 左y轴翻转进，左下角出z轴翻转出
 */
public class WedNineThemeTwo extends BaseThemeExample {
    private BaseThemeExample widgetOne;

    public WedNineThemeTwo(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.ROTATE_Z, width, height);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 360).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, -2f, 2f).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 360).setWidthHeight(width, height).build();
    }


    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, 2200, 0, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setCoordinate(0, -1.0f, 0f, 0f).setEnterDelay(1000).setIsNoZaxis(true).setZView(0)));
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setFade(1, 0).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setZView(0);
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scaleX = width < height ? 1.5f : (width == height ? 2 : 2);
        float centerX = 0;
        float centerY = width < height ? -0.9f : (width == height ? -0.9f : -0.9f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);
    }

    @Override
    protected void wedThemeSpecialDeal() {
        Matrix.translateM(mViewMatrix, 0, 0f, 0.1f, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }
}
