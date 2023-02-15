package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love11;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleDefaultDrawer;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 放大-缩小
 */
public class LoveElevenThemeFive extends BaseBlurThemeExample {
    private ParticleDefaultDrawer particleDefaultDrawer;

    public LoveElevenThemeFive(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 2, 0.2f, 1f);
        this.isNoZaxis = isNoZaxis;
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);

        particleDefaultDrawer = (ParticleDefaultDrawer) new ParticleBuilder(ParticleType.HOVER, mimaps.subList(0, 1)).setParticleCount(8)
                .setPointSize(3, 40).setSelfRotate(true).setLifeTime(5, 3).build();
        particleDefaultDrawer.onSurfaceCreated();
    }


    @Override
    public void drawWiget() {
        particleDefaultDrawer.onDrawFrame();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        particleDefaultDrawer.onDestroy();
    }


}
