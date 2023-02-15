package com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction;

import android.opengl.Matrix;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAnimateAction;

/**
 * 弹性左右拉伸,x轴进行动画拉伸
 */
public class WeddingTextStayAnimation extends BaseEvaluate implements IAnimateAction {
    /**
     * 根据悬停的类型，还有悬停时间，计算出的帧集合
     * 可能是存储角度，也有可能是存储偏移量
     */
    private float[] frames;


    private float period;
    private float factor;

    public WeddingTextStayAnimation(int duration, float p, float scaleFactor, float begin) {
        this.duration = duration;
        this.period = p * 0.5f;
        factor = scaleFactor;
        frames = evaluateAnimateCoord(duration, period, factor, begin);
        frameCount = frames.length;
    }


    @Override
    public float[] draw() {
        if (frameIndex >= frameCount) {
            frameIndex = frameCount - 1;
        }
        frameValue = frames[frameIndex];
        transitionOM = MatrixUtils.getOriginalMatrix();
        //如果有视角，则先平移，再缩放
        Matrix.translateM(transitionOM, 0, 0, 0, Z_VIEW);
        MatrixUtils.scale(transitionOM, frameValue, 1f);
//        Matrix.scaleM(transitionOM, 0, frameValue, frameValue, Z_VIEW);
        frameIndex++;
        return transitionOM;
    }

    @Override
    public float[] prepare() {
        transitionX = this.startX;
        transitionY = this.startY;
        fade = this.startFade;
        conor = this.startConor;
        transitionScale = this.startScale;
        return null;
    }


    @Override
    public void seek(int duration) {

    }


    /**
     * 计算动画数组
     * <p>
     * sin( cycles * PI * -x)*0.2+1
     *
     * @return
     */
    public float[] evaluateAnimateCoord(long duration, float period, float factor, float begin) {
        int framesNum =(int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        float frameValue = 0;
        float x;
        /**
         * 根据x在[0-1]计算的值，目前跟start和end关系不是太大对于上下悬停来说
         */
        float zoomInFirst = 1f;
        for (int i = 0; i < framesNum; i++) {
            x = i * 1.0F / (framesNum - 1);
            frameValue = (float) Math.abs(Math.sin(period * Math.PI * x)) * factor * zoomInFirst + begin;
            frames[i] = frameValue;
        }
        return frames;
    }
}
