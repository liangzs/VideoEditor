package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveAction;

public class WaveOut implements IMoveAction {
    @Override
    public float[] action(float duration, float start, float end) {
        int framesNum = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        double frameValue;
        float offset = end - start;
        float x;
        for (int i = 0; i < framesNum; i++) {
            x = i * 1.0F / (framesNum - 1);
            frameValue = x == 0 ? 0 : x > 1 ? 1 : Math.sin(Math.PI * x);
            frames[i] = (float) frameValue;
        }
        return frames;
    }

    @Override
    public boolean isTranslate() {
        return false;
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
