package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveAction;

import java.util.Arrays;
import java.util.Collections;

/**
 * 幂曲线(0-1)斜率
 * @author hayring
 * @date 2021/12/29  17:24
 */
public class PowerFunction implements IMoveAction {

    /**
     * 幂次
     */
    int pow;

    /**
     * 是否位移
     */
    private final boolean transition;

    /**
     * 是否缩放
     */
    private final boolean scale;

    /**
     * 是否旋转
     */
    private final boolean rotate;


    /**
     * 是否错切
     */
    private boolean skew;

    /**
     * 进场
     */
    boolean in = false;


    /**
     * @param transition 是否位移
     * @param scale 是否缩放
     * @param rotate 是否旋转
     */
    public PowerFunction(int pow, boolean transition, boolean scale, boolean rotate) {
        this.pow = pow;
        this.transition = transition;
        this.scale = scale;
        this.rotate = rotate;
    }

    /**
     * @param transition 是否位移
     * @param scale 是否缩放
     * @param rotate 是否旋转
     * @param skew 是否错切
     */
    public PowerFunction(boolean transition, boolean scale, boolean rotate, boolean skew, int pow) {
        this.pow = pow;
        this.transition = transition;
        this.skew = skew;
        this.scale = scale;
        this.rotate = rotate;
    }

    /**
     * @param transition 是否位移
     * @param scale 是否缩放
     * @param rotate 是否旋转
     * @param skew 是否错切
     * @param in 是否进入
     */
    public PowerFunction(int pow, boolean transition, boolean scale, boolean rotate, boolean skew, boolean in) {
        this.pow = pow;
        this.transition = transition;
        this.skew = skew;
        this.scale = scale;
        this.rotate = rotate;
        this.in = in;
    }

    /**
     * @param transition 是否位移
     * @param scale 是否缩放
     * @param rotate 是否旋转
     * @param in 是否进入
     */
    public PowerFunction(int pow, boolean transition, boolean scale, boolean rotate, boolean in) {
        this.pow = pow;
        this.transition = transition;
        this.scale = scale;
        this.rotate = rotate;
        this.in = in;
    }



    @Override
    public float[] action(float duration, float start, float end) {
        int framesNum =(int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        float offset = end - start;
        float x;
        for (int i = 0; i < framesNum; i++) {
            x = i * 1.0F / (framesNum - 1);
            frames[i] = x == 0 ? 0f : (float) (x > 1 ? 1 : Math.pow(x, pow));
        }

        if (in) {
            int i = 0;
            int j = frames.length - 1;
            float tmp;
            while (i < j) {
                tmp = frames[i];
                frames[i++] = frames[j];
                frames[j--] = tmp;
            }
            offset = - offset;
            for (i = 0; i < framesNum; i++) {
                frames[i] = end + frames[i] * offset;
            }
        } else {
            for (int i = 0; i < framesNum; i++) {
                frames[i] = start + frames[i] * offset;
            }
        }
        LogUtils.v(getClass().getSimpleName(), Arrays.toString(frames));
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
