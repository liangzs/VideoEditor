package com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction;

import android.opengl.Matrix;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAnimateAction;

/**
 * 1、线性变化
 * 片段1
 * ①放大
 * ②缩小
 * 片段2
 * 放大-缩小
 * 缩小-放大
 * 片段3
 * 放大-缩小-放大
 * 缩小-放大-缩小
 * 片段4
 * 片段数量5
 */
public class StayScaleAnimation extends BaseEvaluate implements IAnimateAction {
    /**
     * 根据悬停的类型，还有悬停时间，计算出的帧集合
     * 可能是存储角度，也有可能是存储偏移量
     */
    private float[] frames;


    //是否放大 0缩小 1放大
    private boolean zoomIn;
    private float period;
    private float factor;
    private float begin;

    public StayScaleAnimation(int duration, boolean zoomIn, float p, float scaleFactor, float begin) {
        Z_VIEW = 0;
        this.duration = duration;
        this.zoomIn = zoomIn;
        this.period = p * 0.5f;
        factor = scaleFactor;
        frames = evaluateAnimateCoord(duration, period, zoomIn, factor, begin);
        frameCount = frames.length;
        this.begin = begin;
    }

    @Override
    public float[] draw() {
        if (frameIndex >= frameCount) {
            return transitionOM;
        }
        frameValue = frames[frameIndex];
        transitionOM = MatrixUtils.getOriginalMatrix();
        Matrix.translateM(transitionOM, 0, 0, 0, Z_VIEW);
        MatrixUtils.scale(transitionOM, frameValue, frameValue);
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
     *
     * @param isZoonIn 是否放大
     *                 Math.abs(sin( cycles * PI * -x))*0.2+1
     * @return
     */
    public float[] evaluateAnimateCoord(long duration, float period, boolean isZoonIn, float factor, float begin) {
        int framesNum =(int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        float frameValue = 0;
        float x;
        /**
         * 根据x在[0-1]计算的值，目前跟start和end关系不是太大对于上下悬停来说
         */
        float zoomInFirst = isZoonIn ? 1f : -1f;
        for (int i = 0; i < framesNum; i++) {
            x = i * 1.0F / (framesNum - 1);
            frameValue = (float) Math.abs(Math.sin(period * Math.PI * x)) * factor * zoomInFirst + begin;
            frames[i] = frameValue;
        }
        return frames;
    }
}
