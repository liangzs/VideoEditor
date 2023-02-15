package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding4;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;

import java.util.List;

/**
 * 左上角z轴翻滚进，右下角z轴滚出
 */
public class WeddingFourThemeFour extends BaseThemeExample {
    private final int endConorOffset = 10;

    public WeddingFourThemeFour(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new AnimationBuilder(3000).setWidthHeight(width, height).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, endConorOffset, endConorOffset).setZView(WeddingFourThemeManager.Z_VIEW).build();
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(WeddingFourThemeManager.Z_VIEW).setCoordinate(0, WeddingFourThemeManager.Z_VIEW, 0, 0).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 360).setZView(WeddingFourThemeManager.Z_VIEW).setBgConor(endConorOffset).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setWidthHeight(width, height).setZView(WeddingFourThemeManager.Z_VIEW).setConors(AnimateInfo.ROTATION.Z_ANXIS, endConorOffset, endConorOffset).build();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
    }

    @Override
    public void adjustImageScaling(int width, int height) {
        adjustImageScalingStretch(width, height);
    }


    @Override
    public int getConor() {
        return endConorOffset;
    }

    @Override
    public float[] getPos() {
        return cube;
    }

    @Override
    public void onDestroy() {
        GLES20.glDeleteProgram(mProgram);
    }
}
