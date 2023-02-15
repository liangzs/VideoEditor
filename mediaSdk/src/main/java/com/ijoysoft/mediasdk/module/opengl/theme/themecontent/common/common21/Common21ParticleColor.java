package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common21;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticle.ICreateColor;

import java.util.Random;

/**
 * @author hayring
 * @date 2022/1/12  11:13
 */
public class Common21ParticleColor implements ICreateColor {

    Random random = new Random();

    @Override
    public float[] createColor() {
        return COLORS[random.nextInt(25)];
    }



    private static final float[][] COLORS = new float[][]{
            {1f, 102f/255f, 0f},
            {66f/255f, 170f/255f, 1f},
            {66f/255f, 132f/255f, 1f},
            {1f, 233f/255f, 66f/255f},
            {66f/255f, 1f, 189f/255f},
            {179f/255f, 1f, 66f/255f},
            {132f/255f, 66f/255f, 1f},
            {1f, 66f/255f, 208f/255f},
            {1f, 66f/255f, 138f/255f},
            {161f/255f, 1f, 66f/255f},
            {1f, 91f/255f, 66f/255f},
            {82f/255f, 1f, 66f/255f},
            {95f/255f, 92f/255f, 1f},
            {66f/255f, 1f, 208f/255f},
            {1f, 154f/255f, 39f/255f},
            {1f, 213f/255f, 66f/255f},
            {1f, 66f/255f, 88f/255f},
            {183f/255f, 66f/255f, 1f},
            {66f/255f, 1f, 126f/255f},
            {1f, 120f/255f, 66f/255f},
            {1f, 66f/255f, 157f/255f},
            {230f/255f, 66f/255f, 1f},
            {58f/255f, 153f/255f, 1f},
            {236f/255f, 1f, 66f/255f},
            {66f/255f, 1f, 98f/255f}
//            {0f, 0f, 0f}
    };
}
