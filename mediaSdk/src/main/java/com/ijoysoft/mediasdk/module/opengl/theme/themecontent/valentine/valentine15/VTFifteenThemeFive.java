package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine15;

import android.graphics.Bitmap;
import android.opengl.Matrix;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WaveAnimation;

import java.util.List;

/**
 * 右边波浪进
 */
public class VTFifteenThemeFive extends VTFifteenBaseExample {
    private BaseThemeExample widgetOne;
    private VTFifteenDrawer mVTFifteenDrawer;

    public VTFifteenThemeFive(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2000, 1000, DEFAULT_OUT_TIME);
        float zview = width < height ? -2.5f : -2;
        stayAction = new StayAnimation(2000, AnimateInfo.STAY.SPRING_Y, zview);
        stayNext = new AnimationBuilder(weightTime).setScale(1.0f, 1.1f).setZView(zview).build();
        enterAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(0f, -2.5f, 0f, 0).setZView(zview).build();
        outAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setScale(1.1f, 1.1f).
                setCoordinate(0f, 0f, 0f, 2.5f).setZView(zview));
        setZView(zview);
        mVTFifteenDrawer = new VTFifteenDrawer();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        //顶点坐标
        float pos[] = width < height ? new float[]{
                -0.7f, 0.95f,
                -0.7f, 0.88f,
                0.7f, 0.95f,
                0.7f, 0.88f,
        } : (width == height ? new float[]{
                -0.55f, 0.95f,
                -0.55f, 0.88f,
                0.55f, 0.95f,
                0.55f, 0.88f,
        } : new float[]{
                -0.4f, 0.95f,
                -0.4f, 0.8f,
                0.4f, 0.95f,
                0.4f, 0.8f,
        });
        widgetOne.setVertex(pos);
        mVTFifteenDrawer.init(mimaps.subList(1, 5));

    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 1.5f, 0f, 0f).setZView(0).setIsNoZaxis(true).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setCoordinate(0, 0f, 0f, 1.5f).build());
        widgetOne.setZView(0);

    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        mVTFifteenDrawer.onDrawFrame();
    }

    @Override
    protected void wedThemeSpecialDeal() {
        Matrix.translateM(mViewMatrix, 0, 0f, -0.2f, 0f);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        if (mVTFifteenDrawer != null) {
            mVTFifteenDrawer.onDestroy();
        }
    }
}
