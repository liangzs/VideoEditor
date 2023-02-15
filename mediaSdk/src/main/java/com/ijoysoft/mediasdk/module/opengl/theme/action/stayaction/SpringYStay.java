package com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAnimateAction;

/**
 * Y轴悬停实现，暂且多态化垂直悬停，如果后面有多态的垂直悬停再改造
 */
public class SpringYStay implements IAnimateAction {
    //悬停时间
    private int duration;


    private int frameIndex;//当前执行都的帧数
    private float frameValue;

    private int framesNum;//总共帧数
    private float[] frames;

    private float peakValue;//(峰值）临界值

    /**
     * 核查动画时，检查动画有多少个波段然后根据波段去计算峰值
     * 目前[0-1]中，函数(Math.pow(2, -3 * (x + 0.5)) * Math.sin(3 * x * Math.PI / factor)) 有5个波段
     *
     * @param duration
     */
    public SpringYStay(int duration) {
        framesNum = (int) Math.ceil(duration * ConstantMediaSize.FPS/1000f);
        frames = new float[framesNum - 1];
        peakValue = 5;
        for (int i = 0; i < framesNum; i++) {
            frameValue = MoveAlgorithm(i * 1.0F / (framesNum - 1));
            frames[i] = frameValue;
            if (frameValue > peakValue && frameValue < 0) {
                frames[i] = -1f * frameValue;
            }
            peakValue = frameValue;
        }
    }

    public static final float factor = 0.2f;

    private float MoveAlgorithm(float x) {
        return (float) (Math.pow(2, -3 * (x + 0.5)) * Math.sin(3 * x * Math.PI / factor) * -0.2f);
    }


    public float[] evaluate() {
        return new float[0];
    }

    @Override
    public float[] draw() {
        return new float[0];
    }

    @Override
    public float drawAlpha() {
        return 0;
    }

    @Override
    public float[] prepare() {
        return new float[0];
    }


    @Override
    public void seek(int duration) {

    }
}
