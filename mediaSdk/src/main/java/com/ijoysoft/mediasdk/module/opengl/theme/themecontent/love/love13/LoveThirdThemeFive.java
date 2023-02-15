package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love13;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

/**
 * 左下角z轴翻滚进，右y轴翻转出
 */
public class LoveThirdThemeFive extends BaseThemeExample {


    public LoveThirdThemeFive(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.ROTATE_Z, width, height);
        stayAction.setZView(-2.5f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 2f, 0, 0).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 360).setWidthHeight(width, height).setZView(-2.5f).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 2f, 0f).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 90).setZView(-2.5f).build();
    }
}
