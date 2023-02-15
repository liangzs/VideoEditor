package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common32;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticle.ICreateColor;

import java.util.Random;

public class Common32ParticleColor implements ICreateColor {
    private Random random = new Random();
    private static final float[][] ARRAY = new float[][]{
            {148f / 255f, 207f / 255f, 254f / 255f},
            {254f / 255f, 69f / 255f, 97f / 255f},
            {255f / 255f, 250f / 255f, 207f / 255f},
            {242f / 255f, 132f / 255f, 116f / 255f},
            {217f / 255f, 255f / 255f, 230f / 255f},
            {207f / 255f, 78f / 255f, 254f / 255f}
    };

    @Override
    public float[] createColor() {
        return randomColor();
    }

    /**
     * 等概率生成颜色
     * 148,207,254
     * 254,69,97
     * 255,250,207
     * 242,132,116
     * 217,255,230
     * 207,78,254
     *
     * @return
     */
    private float[] randomColor() {
        return ARRAY[random.nextInt(6)];
    }
}
