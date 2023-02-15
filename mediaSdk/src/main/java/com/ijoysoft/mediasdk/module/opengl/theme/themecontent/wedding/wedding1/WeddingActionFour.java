package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding1;

import android.opengl.Matrix;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.ThemeWedStayAnimation;

/**
 * 上进，上下悬停，左划出
 */
public class WeddingActionFour extends BaseThemeExample {

    public WeddingActionFour(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new ThemeWedStayAnimation(3000, -10f, WeddingOneThemeManager.Z_VIEW);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 0.0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 180, 0).setZView(WeddingOneThemeManager.Z_VIEW).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 2.5f, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 90).setZView(WeddingOneThemeManager.Z_VIEW).build();
    }

    @Override
    protected void wedThemeSpecialDeal() {
        super.wedThemeSpecialDeal();
        Matrix.translateM(mViewMatrix, 0, 0f, -0.2f, 0);
    }
}
