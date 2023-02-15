package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common33;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticle;

import java.util.Random;

/**
 * 0.2  -0.8
 * 固定位置，缩放操作
 */
public class Common33Paticle implements IParticle.ICreateLocation {
    private Random random = new Random();


    @Override
    public float[] createLocation(int index) {
        return new float[]{2 * random.nextFloat() - 1f
                , randomY()
                , 0};
    }

    /**
     * -1  -0.8  10概率
     * -0.8  0.2  80
     * 0.2    0.6  10
     *
     * @return
     */
    private float randomY() {
        int value = random.nextInt(100);
        if (value < 10) {
            return -1f + random.nextFloat() * 0.8f;
        } else if (value < 20) {
            return 0.2f + random.nextFloat() * 0.4f;
        } else {
            return random.nextFloat() - 0.8f;
        }
    }

}
