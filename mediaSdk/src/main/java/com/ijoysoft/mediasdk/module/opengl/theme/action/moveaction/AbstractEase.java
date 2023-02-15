package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveAction;

import java.util.Arrays;

/**
 * 抽象动画
 * @author hayring
 * @date 2021/12/30  17:37
 */
public abstract class AbstractEase implements IMoveAction {
    private boolean transition;
    private boolean scale;
    private boolean rotate;
    private boolean skew;
    private int count;




    public AbstractEase(boolean transition, boolean scale, boolean rotate, boolean skew) {
        this.transition = transition;
        this.scale = scale;
        this.rotate = rotate;
        this.skew = skew;
    }

    @Override
    public float[] action(float duration, float start, float end) {
        int framesNum =(int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        double frameValue;
        float offset = end - start;
        float x;
        for (int i = 0; i < framesNum; i++) {
            x = i * 1.0F / (framesNum - 1);
            frameValue = x == 0 ? 0 : x > 1 ? 1 : function(x);
            frames[i] = start + (float) (frameValue * offset);
        }
        LogUtils.v(getClass().getSimpleName(), "frames: " + Arrays.toString(frames));
        return frames;
    }


    /**
     * 具体实现函数
     * @param x x
     * @return y
     *
     */
    public abstract double function(double x);


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
