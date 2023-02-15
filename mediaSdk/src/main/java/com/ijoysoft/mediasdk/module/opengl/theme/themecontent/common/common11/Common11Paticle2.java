package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common11;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticle;

import java.util.Random;

/**
 * 固定位置，缩放操作
 */
public class Common11Paticle2 implements IParticle.ICreateLocation, IParticle.ICreateTexture {
    private Random random = new Random();


    @Override
    public float[] createLocation(int index) {
        switch (index) {
            case 0:
                return new float[]{-0.7f, 0.8f, 0};
            case 1:
                return new float[]{0.7f, 0.85f, 0};
            case 2:
                return new float[]{-0.75f, 0.3f, 0};
            case 3:
                return new float[]{0.75f, -0.1f, 0};
            case 4:
                return new float[]{-0.8f, -0.3f, 0};
            case 5:
                return new float[]{-0.2f, -0.8f, 0};
            case 6:
                return new float[]{0.8f, -0.85f, 0};
        }
        return new float[3];
    }


    @Override
    public int createTexture(int index) {
        switch (index) {
            case 4:
                return 0;
            case 5:
                return 4;
            case 6:
                return 5;
            default:
                return index;
        }
    }
}
