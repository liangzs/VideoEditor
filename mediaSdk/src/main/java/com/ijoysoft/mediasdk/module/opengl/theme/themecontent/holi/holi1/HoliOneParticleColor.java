package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi1;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticle.ICreateColor;

import java.util.Random;

public class HoliOneParticleColor implements ICreateColor {
    private Random random = new Random();

    @Override
    public float[] createColor() {
        return randomColor();
    }

    /**
     * 等概率生成颜色
     * 165,42,237
     * 255,185,25
     * 255,1,249
     * 46,190,188
     *
     * @return
     */
    private float[] randomColor() {
        int a = random.nextInt(100);
        float[] result = new float[3];
        if (a < 25) {
            result[0] = 165f / 255f;
            result[1] = 42f / 255f;
            result[2] = 237f / 255f;
        } else if (a < 50) {
            result[0] = 1f;
            result[1] = 185f / 255f;
            result[2] = 25f / 255f;
        } else if (a < 75) {
            result[0] = 1f;
            result[1] = 1f / 255f;
            result[2] = 249f / 255f;
        } else {
            result[0] = 46f / 255f;
            result[1] = 190f / 255f;
            result[2] = 188f / 255f;
        }
        return result;
    }
}
