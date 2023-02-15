package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common11;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticle;

import java.util.Random;

/**
 * 颜色集合
 * 235,243,155
 * 168,243,155
 * 155,241,243
 * 255,72,236
 * 255,112,34
 * 255,221,0
 */
public class Common11Paticle3 implements IParticle.ICreateColorMap {
    private Random random = new Random();
    private final int COLOR_COUNT = 6;
    //每5帧渲一次
    private final int FRAME = 5;


    private static final float[][] ARRAY = new float[][]{
            {235f / 255f, 243f / 255f, 155f / 255f},
            {168f / 255f, 243f / 255f, 155f / 255f},
            {155f / 255f, 241f / 255f, 243f / 255f},
            {1f, 72f / 255f, 236f / 255f},
            {1f, 112f / 255f, 34f / 255f},
            {1f, 221f / 255f, 0f}
    };

    @Override
    public float[] createColor() {
        int index = random.nextInt(COLOR_COUNT);
        return new float[]{ARRAY[index][0], ARRAY[index][1], ARRAY[index][2], index, FRAME};
    }

    @Override
    public float[] getColor(int index, int frame) {
        if (frame < 0) {
            int temp = (index + 1) % COLOR_COUNT;
            return new float[]{ARRAY[temp][0], ARRAY[temp][1], ARRAY[temp][2], temp, FRAME};
        }
        return null;
    }

}
