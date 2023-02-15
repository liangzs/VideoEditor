package com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction;

import android.opengl.Matrix;

import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.EvaluatorHelper;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAnimateAction;

/**
 * 入场动画结束时，切换到悬停动画的开始
 * frame帧数根据传入的时间按25f/s进行计算,1000ms 30f 那么大概是40ms一帧
 */
public class StayAnimation extends BaseEvaluate implements IAnimateAction {
    private AnimateInfo.STAY stayType;
    /**
     * 根据悬停的类型，还有悬停时间，计算出的帧集合
     * 可能是存储角度，也有可能是存储偏移量
     */
    private float[] frames;
    //当前帧
    private int frameIndex;
    private float frameValue;
    private boolean isReverse;

    public StayAnimation(int duration, AnimateInfo.STAY stayType) {
        this.duration = duration;
        this.stayType = stayType;
        frames = EvaluatorHelper.evaluateAnimateCoord(duration, 0, 0, stayType);
        frameCount = frames.length;
    }

    public StayAnimation(int duration, AnimateInfo.STAY stayType, float zView) {
        this.duration = duration;
        this.stayType = stayType;
        frames = EvaluatorHelper.evaluateAnimateCoord(duration, 0, 0, stayType);
        frameCount = frames.length;
        Z_VIEW = zView;
    }

    public StayAnimation(int duration, AnimateInfo.STAY stayType, float zView, boolean isReverse) {
        this.duration = duration;
        this.stayType = stayType;
        this.isReverse = isReverse;
        if (isReverse) {
            frames = EvaluatorHelper.evaluateSringYReverse(duration);
        } else {
            frames = EvaluatorHelper.evaluateAnimateCoord(duration, 0, 0, stayType);
        }
        frameCount = frames.length;
        Z_VIEW = zView;
    }

    public StayAnimation(int duration, AnimateInfo.STAY stayType, int width, int height) {
        this.duration = duration;
        this.stayType = stayType;
        frames = EvaluatorHelper.evaluateAnimateCoord(duration, 0, 0, stayType);
        frameCount = frames.length;
        this.width = width;
        this.height = height;
    }

    public StayAnimation(int duration, AnimateInfo.STAY stayType, int width, int height, float zView) {
        this.duration = duration;
        this.stayType = stayType;
        frames = EvaluatorHelper.evaluateAnimateCoord(duration, 0, 0, stayType);
        frameCount = frames.length;
        this.width = width;
        this.height = height;
        this.Z_VIEW = zView;
    }


    @Override
    public float[] draw() {
        if (frameIndex >= frameCount) {
            transitionOM = MatrixUtils.getOriginalMatrix();
            Matrix.translateM(transitionOM, 0, 0, 0, Z_VIEW);
            return transitionOM;
        }
        frameValue = frames[frameIndex];
        if (isReverse) {
            frameValue = frames[frameCount - frameIndex - 1];
        }
        transitionOM = MatrixUtils.getOriginalMatrix();
        Matrix.translateM(transitionOM, 0, 0, 0, Z_VIEW);
        switch (stayType) {
            case ROTATE_X:
                Matrix.rotateM(transitionOM, 0, frameValue, 1f, 0f, 0f);
                break;
            case ROTATE_Y:
                Matrix.rotateM(transitionOM, 0, frameValue, 0f, 1f, 0f);
                break;
            case ROTATE_Z:
                float[] temp = MatrixUtils.getOriginalMatrix();
                Matrix.scaleM(temp, 0, height, width, 1);
                Matrix.multiplyMM(transitionOM, 0, transitionOM, 0, temp, 0);
                Matrix.rotateM(transitionOM, 0, frameValue, 0f, 0f, 1f);
                Matrix.invertM(temp, 0, temp, 0);
                Matrix.multiplyMM(transitionOM, 0, transitionOM, 0, temp, 0);
                break;
            case SPRING_Y:
                Matrix.translateM(transitionOM, 0, 0f, frameValue, 0);
//                Matrix.rotateM(transitionOM, 0, 0, 1f, 0f, 0f);
                break;
            case ROTATE_XY:
                Matrix.rotateM(transitionOM, 0, frameValue, 1f, 1f, 0f);
                break;
            case ROTATE_XY_RE:
                Matrix.rotateM(transitionOM, 0, frameValue, -1f, 1f, 0f);
                break;
        }
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

}
