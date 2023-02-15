package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love13;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;

/**
 * 左y轴翻转进，左下角出z轴翻转出
 */
public class LoveThirdThemeTwo extends BaseThemeExample {


    public LoveThirdThemeTwo(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.ROTATE_Z, width, height);
        stayAction.setZView(-2.5f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0f, 0, 0).setZView(-2.5f).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 360).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, -2f, 2f).setZView(-2.5f).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 360).setWidthHeight(width, height).build();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
    }

}
