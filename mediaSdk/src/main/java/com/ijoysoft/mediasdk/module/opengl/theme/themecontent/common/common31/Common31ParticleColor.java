package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common31;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticle.ICreateColor;

import java.util.Random;

public class Common31ParticleColor implements ICreateColor {
    private Random random = new Random();

    @Override
    public float[] createColor() {
        return randomColor();
    }

    /**
     * 等概率生成颜色
     * 9574f1
     * f95789
     * c7f94d
     * 9ff78a
     * d14a46
     * e6c59f
     *
     * @return
     */
    private static final float[][] ARRAY = new float[][]{
            {149f / 255f, 116f / 255f, 241f / 255f},
            {249f / 255f, 87f / 255f, 137f / 255f},
            {199f / 255f, 249f / 255f, 77f / 255f},
            {159f / 255f, 247f / 255f, 138f / 255f},
            {209f / 255f, 74f / 255f, 70f / 255f},
            {230f / 255f, 197f / 255f, 159f / 255f}
    };

    private float[] randomColor() {
        return ARRAY[random.nextInt(6)];
    }
}
