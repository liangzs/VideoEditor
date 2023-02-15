package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat2;

import android.graphics.RenderNode;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticle.ICreateColor;

import java.util.Random;

public class Beat2ParticleColor implements ICreateColor {
    private Random random = new Random();

    @Override
    public float[] createColor() {
        return randomColor();
    }

    /**
     * 等概率生成颜色
     * 00FFFB  33% 0,255,251
     * FA00FE  33% 250,0,254
     * ffffff  33%
     *
     * @return
     */
    private float[] randomColor() {
        int a = random.nextInt(100);
        float[] result = new float[3];
        if (a < 33) {
            result[0] = 0;
            result[1] = 1;
            result[2] = 251f / 255f;
        } else if (a < 66) {
            result[0] = 250f / 255f;
            result[1] = 0;
            result[2] = 1f;
        } else {
            result[0] = 1f;
            result[1] = 1f;
            result[2] = 1f;
        }
        return result;
    }
}
