package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding4;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;

/**
 * 一套组合，一个组合有三个显示元素，三者不一样的进场
 */
public class WeddingFourThemeOne extends BaseThemeExample {
    private final int endConorOffset = -20;
    private final int endConor = 340;

    public WeddingFourThemeOne(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_ENTER_TIME);
        stayAction = new AnimationBuilder(3000).setWidthHeight(width, height).
                setZView(WeddingFourThemeManager.Z_VIEW).setConors(AnimateInfo.ROTATION.Z_ANXIS, endConor, endConor).build();
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(WeddingFourThemeManager.Z_VIEW).setCoordinate(0, WeddingFourThemeManager.Z_VIEW, 0, 0).
                setConors(AnimateInfo.ROTATION.X_ANXIS, 0, 360).setZView(WeddingFourThemeManager.Z_VIEW).setBgConor(endConorOffset).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setWidthHeight(width, height).setZView(WeddingFourThemeManager.Z_VIEW).setConors(AnimateInfo.ROTATION.Z_ANXIS, endConor, endConor).build();
    }


    @Override
    public int getConor() {
        return endConorOffset;
    }


    @Override
    public void adjustImageScaling(int width, int height) {
        adjustImageScalingStretch(width, height);
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
