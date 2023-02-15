package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveAction;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * {@link SpringYEnter 格式，，不累加，直接做位移值}
 */
public class EaseCustomNoOffset implements IMoveAction {

    public interface Action {
        float caculate(float x);
    }

    public EaseCustomNoOffset(Action action) {
        this.action = action;
    }

    private Action action;

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public float[] action(float duration, float start, float end) {
        int framesNum = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        float x;
        for (int i = 0; i < framesNum; i++) {
            x = i * 1.0F / (framesNum - 1);
            frames[i] = action.caculate(x);
        }
        LogUtils.v("", Arrays.toString(frames));
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
