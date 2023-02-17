package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine4;

import android.opengl.Matrix;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.ThemeWedStayAnimation;

/**
 * 上进，上下悬停，左划出
 */
public class VTFourThemeTwo extends BaseThemeExample {
    public VTFourThemeTwo(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new ThemeWedStayAnimation(3000, -10f);
        stayAction.setZView(VTFourThemeManager.CUSTOM_ZVIEW);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -2.5f, 0, 0).setZView(VTFourThemeManager.CUSTOM_ZVIEW).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 0).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 2.5f, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS, 0, 180).setZView(VTFourThemeManager.CUSTOM_ZVIEW).build();
    }

    @Override
    protected void wedThemeSpecialDeal() {
        super.wedThemeSpecialDeal();
        Matrix.translateM(mViewMatrix, 0, 0f, VTFourThemeManager.Z_AXIS, 0f);

    }
}