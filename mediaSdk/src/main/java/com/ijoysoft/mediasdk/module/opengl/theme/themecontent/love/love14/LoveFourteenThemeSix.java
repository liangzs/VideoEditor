package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love14;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleDefaultDrawer;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 缩小
 * 这里在悬停缩放时，添加滤镜
 */
public class LoveFourteenThemeSix extends BaseBlurThemeExample {
    private ParticleDefaultDrawer particleDefaultDrawer;

    public LoveFourteenThemeSix(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.2f, 1.2f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;

    }


    @Override
    public void drawWiget() {
        particleDefaultDrawer.onDrawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);

        float[][] coord = new float[][]{{-0.85f, 0.85f}, {0.85f, -0.85f}};
        int[] sizes = new int[coord.length];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = 500;
        }
        particleDefaultDrawer = (ParticleDefaultDrawer) new ParticleBuilder(ParticleType.FIX_COORD, mimaps.subList(0, 1)).
                setFixPointSize(20, sizes)
                .setFixParticlesCoord(coord).build();
        particleDefaultDrawer.onSurfaceCreated();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        particleDefaultDrawer.onDestroy();
    }
}
