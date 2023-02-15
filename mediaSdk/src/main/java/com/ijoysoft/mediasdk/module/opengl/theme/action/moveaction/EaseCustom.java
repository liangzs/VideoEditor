package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveAction;

/**
 *
 */
public class EaseCustom implements IMoveAction {
    private boolean isTranslate;
    private boolean isRotation;

    public interface Action {
        float caculate(float x);
    }

    public EaseCustom(Action action) {
        this.action = action;
    }

    public EaseCustom setTranslate(boolean isTranslate) {
        this.isTranslate = isTranslate;
        return this;
    }

    public EaseCustom setRotation(boolean isRotation) {
        this.isRotation = isRotation;
        return this;
    }

    private Action action;

    public void setAction(Action action) {
        this.action = action;
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
            frameValue = x == 0 ? 0 : x > 1 ? 1 : action.caculate(x);
            frames[i] = start + (float) (frameValue * offset);
        }
        return frames;
    }

    @Override
    public boolean isTranslate() {
        return isTranslate;
    }

    @Override
    public boolean isRotate() {
        return false;
    }

    @Override
    public boolean isScale() {
        return false;
    }

    @Override
    public boolean isSkew() {
        return false;
    }


}
