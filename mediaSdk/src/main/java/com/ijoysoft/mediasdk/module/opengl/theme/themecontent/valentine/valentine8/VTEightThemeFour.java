package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine8;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday16.particle.BDSixteenStarDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday16.particle.BDSixteenStarSystem;

import java.util.List;

/**
 * 缩小-放大-缩小
 */
public class VTEightThemeFour extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BDSixteenStarDrawer bdSixteenDrawer;

    public VTEightThemeFour(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.2f, 1.2f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f).setIsNoZaxis(true).setZView(0).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 1500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2, 0f, 0, 0).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setZView(0);
        bdSixteenDrawer = new BDSixteenStarDrawer(new BDSixteenStarSystem(BDSixteenStarSystem.StartType.LINE));
    }


    @Override
    protected void onDrawArraysAfter() {
        super.onDrawArraysAfter();
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        if (getStatus() != ActionStatus.ENTER) {
            bdSixteenDrawer.onDrawFrame();
        }
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float scale = width < height ? 1.6f : (width == height ? 1.6f : 1.0f);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), AnimateInfo.ORIENTATION.BOTTOM, 0, scale);
        bdSixteenDrawer.init(mimaps.get(1));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        bdSixteenDrawer.onDestroy();
    }
}
