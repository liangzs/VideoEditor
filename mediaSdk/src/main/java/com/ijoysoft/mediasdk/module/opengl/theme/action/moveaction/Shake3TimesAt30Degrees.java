package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveAction;
import com.ijoysoft.mediasdk.module.opengl.theme.action.MoveAnimation;

/**
 * @author hayring
 * @date 2021/12/29  15:08
 */
public class Shake3TimesAt30Degrees implements IMoveAction {

    private boolean transition;
    private boolean scale;
    private boolean rotate;
    private boolean skew;


    public Shake3TimesAt30Degrees(boolean transition, boolean scale, boolean rotate) {
        this.transition = transition;
        this.scale = scale;
        this.rotate = rotate;
    }

    public Shake3TimesAt30Degrees(boolean transition, boolean scale, boolean rotate, boolean skew) {
        this.transition = transition;
        this.scale = scale;
        this.rotate = rotate;
        this.skew = skew;
    }

    @Override
    public float[] action(float duration, float start, float end) {
        int framesNum =(int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        double deltaX = 3f / framesNum;
        double x = -3 - deltaX;
        for (int i = 0; i < framesNum; i++) {
            x += deltaX;
            frames[i] = (float) (0.2 * x * Math.cos(4 * x) * 360f / (2 * Math.PI));
        }
        frames[framesNum - 1] = 0f;
        return frames;
    }

    @Override
    public boolean isTranslate() {
        return transition;
    }

    @Override
    public boolean isScale() {
        return scale;
    }

    @Override
    public boolean isRotate() {
        return rotate;
    }

    @Override
    public boolean isSkew() {
        return skew;
    }
}
