package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveAction;

import java.util.Arrays;

/**
 * @author hayring
 * @date 2021/12/30  14:53
 */
public class InverseProportionalFunction implements IMoveAction {



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

    private final double a = 1/(5*(1-((5-Math.sqrt(45d))/10)));

    private final double b = (5-Math.sqrt(45d))/10;

    @Override
    public float[] action(float duration, float start, float end) {
        int framesNum =(int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        double frameValue;
        float offset = end - start;
        float x;
        for (int i = 0; i < framesNum; i++) {
            x = i * 1.0F / (framesNum - 1);
            frames[i] = x == 0 ? 1f : (float) (x > 1 ? 0 : 1-1/(5*(x+a))-b);
        }
        for (int i = 0; i < framesNum; i++) {
            frames[i] = start + frames[i] * offset;
        }
        LogUtils.v(getClass().getSimpleName(), Arrays.toString(frames));
        return frames;
    }





    public InverseProportionalFunction(boolean transition, boolean scale, boolean rotate, boolean skew) {
        this.transition = transition;
        this.scale = scale;
        this.rotate = rotate;
        this.skew = skew;
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
