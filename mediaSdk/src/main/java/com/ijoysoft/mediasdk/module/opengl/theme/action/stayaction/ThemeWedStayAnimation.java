package com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction;

import android.opengl.Matrix;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAnimateAction;

/**
 * 结婚主题，悬停沿X轴转动幅度很大，且统一
 */
public class ThemeWedStayAnimation extends BaseEvaluate implements IAnimateAction {
    /**
     * 根据悬停的类型，还有悬停时间，计算出的帧集合
     * 可能是存储角度，也有可能是存储偏移量
     */
    private float[] frames;
    //当前帧
    private int frameIndex;
    private float frameValue;


    public ThemeWedStayAnimation(int duration, float fractor) {
        this.duration = duration;
        frames = evaluateAnimateCoord(duration, 3, fractor);
        frameCount = frames.length;
    }

    public ThemeWedStayAnimation(int duration, float fractor, float zview) {
        this.duration = duration;
        frames = evaluateAnimateCoord(duration, 3, fractor);
        frameCount = frames.length;
        setZView(zview);
    }


    @Override
    public float[] draw() {
        if (frameIndex >= frameCount) {
            return transitionOM;
        }
        frameValue = frames[frameIndex];
        transitionOM = MatrixUtils.getOriginalMatrix();
        Matrix.translateM(transitionOM, 0, 0, 0, Z_VIEW);
        Matrix.rotateM(transitionOM, 0, frameValue, 1f, 0f, 0f);
        frameIndex++;
        return transitionOM;
    }

    @Override
    public float[] prepare() {
        return null;
    }


    @Override
    public void seek(int duration) {

    }


    /**
     * 计算动画数组
     * <p>
     * sin(2*x*PI)*0.1
     *
     * @return
     */
    public float[] evaluateAnimateCoord(long duration, float period, float factor) {
        int framesNum = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        float frameValue = 0;
        float x;
        /**
         * 根据x在[0-1]计算的值，目前跟start和end关系不是太大对于上下悬停来说
         */
        for (int i = 0; i < framesNum; i++) {
            x = i * 1.0F / (framesNum - 1);
            frameValue = (float) Math.sin(period * Math.PI * x) * factor;
            frames[i] = frameValue;
        }
        return frames;
    }

}
