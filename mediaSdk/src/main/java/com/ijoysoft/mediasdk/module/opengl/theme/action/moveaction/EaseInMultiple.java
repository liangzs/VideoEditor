package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveAction;

/**
 * 3立方曲线，由慢到快，适合出场
 * * x^count
 */
public class EaseInMultiple implements IMoveAction {
    private boolean transition;
    private boolean scale;
    private boolean rotate;
    private boolean skew;
    private int count;

    /**
     * @param transition
     * @param scale
     * @param rotate
     */
    public EaseInMultiple(int count, boolean transition, boolean scale, boolean rotate) {
        this.transition = transition;
        this.scale = scale;
        this.rotate = rotate;
        this.count = count;
    }

    public EaseInMultiple(boolean transition, boolean scale, boolean rotate, boolean skew) {
        this.transition = transition;
        this.scale = scale;
        this.rotate = rotate;
        this.skew = skew;
    }

    public EaseInMultiple(int count) {
        this.count = count;
    }


    @Override
    public float[] action(float duration, float start, float end) {
        int framesNum = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        double frameValue;
        float offset = end - start;
        float x;
        for (int i = 0; i < framesNum; i++) {
            x = i * 1.0F / (framesNum - 1);
            frameValue = x == 0 ? 0 : x > 1 ? 1 : Math.pow(x, count);
            frames[i] = start + (float) (frameValue * offset);
        }
        return frames;
    }


    @Override
    public boolean isTranslate() {
        return transition;
    }

    @Override
    public boolean isRotate() {
        return rotate;
    }

    @Override
    public boolean isScale() {
        return scale;
    }

    @Override
    public boolean isSkew() {
        return skew;
    }
}
