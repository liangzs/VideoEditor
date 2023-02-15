package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common9;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticle.ICreateColor;

import java.util.Random;

/**
 * @author hayring
 * @date 2022/1/12  11:13
 */
public class Common9ParticleColor implements ICreateColor {

    Random random = new Random();

    @Override
    public float[] createColor() {
        return COLORS[random.nextInt(5)];
    }



    private static final float[][] COLORS = new float[][]{
            {1f, 92f/256f, 0f},
            {1f, 242f/256f, 171f/256f},
            {1f, 183f/256f, 0f},
            {168f/256f, 99f/256f, 59f/256f},
            {185f/256f, 185f/256f, 185f/256f}
//            {0f, 0f, 0f}
    };
}
