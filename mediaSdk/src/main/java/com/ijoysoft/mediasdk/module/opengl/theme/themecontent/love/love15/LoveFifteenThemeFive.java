package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love15;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleDefaultDrawer;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 缩小-放大-缩小
 */
public class LoveFifteenThemeFive extends BaseBlurThemeExample {
    private ParticleDefaultDrawer particleDefaultDrawer;

    public LoveFifteenThemeFive(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 3, 0.1f, 1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2, 0, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 0, -2f).
                setOrientation(AnimateInfo.ORIENTATION.TOP));
        this.isNoZaxis = isNoZaxis;


    }


    @Override
    public void drawWiget() {
        particleDefaultDrawer.onDrawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);

        float[][] coord = new float[][]{{-0.8f, 0.8f}, {0.8f, -0.8f}};
        particleDefaultDrawer = (ParticleDefaultDrawer) new ParticleBuilder(ParticleType.FIX_COORD, mimaps.subList(0, 1)).
                setFixPointSize(new int[]{360, 360})
                .setFixParticlesCoord(coord).build();
        particleDefaultDrawer.onSurfaceCreated();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        particleDefaultDrawer.onDestroy();
    }

}
