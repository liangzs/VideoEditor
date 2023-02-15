package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love13;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;

/**
 * 右上角x轴翻滚进，x反相下方出
 */
public class LoveThirdThemeSeven extends BaseThemeExample {

    public LoveThirdThemeSeven(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, -2f, 0, 0).setZView(-2.5f).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 360).build();
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.ROTATE_X);
        stayAction.setZView(-2.5f);
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 0f, 2f).setZView(-2.5f).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 180).build();

    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
    }
}
