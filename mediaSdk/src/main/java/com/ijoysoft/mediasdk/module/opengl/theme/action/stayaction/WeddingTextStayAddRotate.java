package com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction;

import android.opengl.Matrix;
import android.util.Log;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAnimateAction;

import java.util.Arrays;

/**
 * 弹性左右拉伸+翻转
 * 对半分
 */
public class WeddingTextStayAddRotate extends BaseEvaluate implements IAnimateAction {
    /**
     * 根据悬停的类型，还有悬停时间，计算出的帧集合
     * 可能是存储角度，也有可能是存储偏移量
     */
    private float[] frames;
    private int[] indexs;

    public WeddingTextStayAddRotate(int duration) {
        frames = evaluateAnimateCoord(duration, 1, 0.3f, 1f);
        frameCount = frames.length;
        indexs = new int[2];
        indexs[0] = frameCount / 2;
        indexs[1] = frameCount / 2 + frameCount / 4;
    }


    @Override
    public float[] draw() {
        Log.i("test1", "index:" + frameIndex + ",count:" + frameCount + ",transitionOM" + Arrays.toString(transitionOM));
        if (frameIndex >= frameCount) {
            transitionOM = MatrixUtils.getOriginalMatrix();
            Matrix.translateM(transitionOM, 0, 0, 0, Z_VIEW);
            return transitionOM;
        }
        frameValue = frames[frameIndex];
        transitionOM = MatrixUtils.getOriginalMatrix();
        Matrix.translateM(transitionOM, 0, 0, 0, Z_VIEW);
        if (frameIndex < indexs[0]) {
            //如果有视角，则先平移，再缩放
            MatrixUtils.scale(transitionOM, frameValue, 1f);
        } else if (frameIndex >= indexs[0] && frameIndex < indexs[1]) {
            Matrix.rotateM(transitionOM, 0, frameValue, 0f, 1f, 0f);
        } else {
            Matrix.rotateM(transitionOM, 0, frameValue + 180, 0f, 1f, 0f);
//            Matrix.rotateM(transitionOM, 0, frameValue + 270, 0f, 1f, 0f);
        }
        frameIndex++;
        return transitionOM;
    }


    @Override
    public void seek(int duration) {

    }


    /**
     * 计算动画数组
     * <p>
     * sin( cycles * PI * -x)*0.2+1
     * 对半分，一半伸缩，一半旋转
     * 旋转只转180，所以旋转90度时，基数270，最终旋转完是360
     *
     * @return
     */
    public float[] evaluateAnimateCoord(long duration, float period, float factor, float begin) {
        int framesNum = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        int start = framesNum / 2;
        float[] frames = new float[framesNum];
        float frameValue = 0;
        float x;
        /**
         * 根据x在[0-1]计算的值，目前跟start和end关系不是太大对于上下悬停来说
         */
        float zoomInFirst = 1f;
        for (int i = 0; i < start; i++) {
            x = i * 1.0F / (start - 1);
            frameValue = (float) Math.abs(Math.sin(period * Math.PI * x)) * factor * zoomInFirst + begin;
            frames[i] = frameValue;
        }
        frameValue = 180f / start;
        for (int i = 0; i < start; i++) {
            frames[start + i] = frameValue * (i + 1);
        }
        return frames;
    }
}
