package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love14;

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
public class LoveFourteenThemeFour extends BaseBlurThemeExample {
    private ParticleDefaultDrawer particleDefaultDrawer;

    public LoveFourteenThemeFour(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 2, 0.2f, 1f);
        this.isNoZaxis = isNoZaxis;
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float[][] coord = new float[][]{{-0.85f, -0.85f}, {-0.85f, 0}, {0.85f, -0.85f}
                , {-0.85f, 0.85f}, {0.85f, 0}, {0.85f, 0.85f}};
        int[] sizes = new int[coord.length];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = 360;
        }
        particleDefaultDrawer = (ParticleDefaultDrawer) new ParticleBuilder(ParticleType.FIX_COORD, mimaps.subList(0, 1)).
                setFixPointSize(10, sizes)
                .setFixParticlesCoord(coord).build();
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
