package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveAction;

/**
 * y轴弹跳进场
 * 2^{-10x}\cdot\sin\left((x-0.2/4)*(2\pi)/0.2\right)
 * <p>
 * factor = 0.2
 * pow(2, -10 * x) * sin((x - factor / 4) * (2 * PI) / factor)
 * <p>
 * 默认是从-1进入，然后进行弹跳
 */
public class SpringYEnter implements IMoveAction {
    @Override
    public float[] action(float duration, float start, float end) {
        int framesNum = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        float x;
        for (int i = 0; i < framesNum; i++) {
            x = i * 1.0F / (framesNum - 1);
            frames[i] = (float) (Math.pow(2, -10 * x) * Math.sin((x - 0.05f) * -10 * Math.PI));
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
