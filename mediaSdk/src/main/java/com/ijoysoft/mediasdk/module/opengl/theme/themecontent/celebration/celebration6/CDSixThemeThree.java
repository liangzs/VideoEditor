package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration6;

import android.graphics.Bitmap;
import android.opengl.Matrix;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;

/**
 * 右上角x轴翻滚进，x反相下方出
 */
public class CDSixThemeThree extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private CDSixDrawer mCDSixDrawer;

    public CDSixThemeThree(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, -2f, 0, 0).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 360).build();
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.ROTATE_X);
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 0f, 2f).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 180).build();
        mCDSixDrawer = new CDSixDrawer(CDSixSystem.TWO);
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME)
                .setIsNoZaxis(true).setZView(0).setCoordinate(0, -1, 0, 0).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setFade(1, 0).build());
        widgetOne.setZView(0);


    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        mCDSixDrawer.onDrawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scaleX = width < height ? 3 : (width == height ? 4 : 6);
        float centerX = 0;
        float centerY = width < height ? -0.85f : (width == height ? -0.85f : -0.85f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(),
                mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);
        mCDSixDrawer.init(mimaps.get(1));
    }


    @Override
    protected void wedThemeSpecialDeal() {
        Matrix.translateM(mViewMatrix, 0, 0f, 0.1f, 0f);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        mCDSixDrawer.onDestroy();
    }
}
