package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love11;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleDefaultDrawer;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 缩小
 */
public class LoveElevenThemeThree extends BaseBlurThemeExample {
    private ParticleDefaultDrawer particleDefaultDrawer;


    public LoveElevenThemeThree(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 1, 0.2f, 1f);
        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

    }


    @Override
    public void drawWiget() {
        if (particleDefaultDrawer != null) {
            particleDefaultDrawer.onDrawFrame();
        }
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float[][] coord = new float[][]{{0, 0.8f}};
        particleDefaultDrawer = (ParticleDefaultDrawer) new ParticleBuilder(ParticleType.FIX_COORD, mimaps.subList(0, 1)).
                setFixPointSize(new int[]{360})
                .setFixParticlesCoord(coord).build();
        particleDefaultDrawer.onSurfaceCreated();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (particleDefaultDrawer != null) {
            particleDefaultDrawer.onDestroy();
        }
    }


}
