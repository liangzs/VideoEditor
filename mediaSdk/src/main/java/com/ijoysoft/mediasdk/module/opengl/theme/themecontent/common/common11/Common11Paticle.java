package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common11;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticle;

import java.util.Random;

/**
 * 初始位置全屏，方向为2种，一种向上，一种向下
 */
public class Common11Paticle implements IParticle.ICreateLocation, IParticle.ICreateDirector, IParticle.ICreateAccelerate {
    private Random random = new Random();

    @Override
    public float[] createDirector() {
        return new float[]{0, (random.nextFloat() - 0.5f) * 0.2f, 0};
    }

    @Override
    public float[] createLocation(int index) {
        return new float[]{random.nextFloat() * 2 - 1f, random.nextFloat() * 2 - 1f, 0};
    }

    @Override
    public float createAccelerate() {
        return random.nextFloat() < 0.5 ? 1f : -1f;
    }
}
