package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat4;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticle.ICreateColor;

import java.util.Random;

public class Beat4ParticleColor implements ICreateColor {
    private Random random = new Random();

    @Override
    public float[] createColor() {
        return randomColor();
    }

    /**
     * 等概率生成颜色
     * 33% 19,254,1
     * 33% 255,36,242
     * 33% 240,255,4
     *
     * @return
     */
    private float[] randomColor() {
        int a = random.nextInt(100);
        float[] result = new float[3];
        if (a < 33) {
            result[0] = 19f / 255f;
            result[1] = 254f / 255f;
            result[2] = 1f;
        } else if (a < 66) {
            result[0] = 1f;
            result[1] = 36f / 255f;
            result[2] = 242f / 255f;
        } else {
            result[0] = 240f / 255f;
            result[1] = 1f;
            result[2] = 4f / 255f;
        }
        return result;
    }
}
