package com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction;

import android.opengl.Matrix;

import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.EvaluatorHelper;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAnimateAction;

/**
 * 气球左右摇摆，byebye姿势
 */
public class StayShakeAnimation extends BaseEvaluate implements IAnimateAction {
    /**
     * 根据悬停的类型，还有悬停时间，计算出的帧集合
     * 可能是存储角度，也有可能是存储偏移量
     */
    private float[] frames;
    //当前帧
    private int frameIndex;
    private float frameValue;
    private AnimateInfo.ORIENTATION waveType;

    private float coordX, coordY;

    public StayShakeAnimation(int duration, int width, int height, float coordX, float coordY) {
        this.duration = duration;
        frames = EvaluatorHelper.evaluateShakeStayAnimation(duration, 5, 5);
        frameCount = frames.length;
        Z_VIEW = 0;
        this.width = width;
        this.height = height;
        this.coordY = coordY;
        this.coordX = coordX;
    }

    public StayShakeAnimation(int duration, int width, int height, float coordX, float coordY,int period, float zView) {
        this.duration = duration;
        frames = EvaluatorHelper.evaluateShakeStayAnimation(duration, period, 5);
        frameCount = frames.length;
        Z_VIEW = zView;
        this.width = width;
        this.height = height;
        this.coordY = coordY;
        this.coordX = coordX;
    }


    /**
     * 因为纹理坐标和屏幕坐标是相反的，所以这里需要y值也取反
     *
     * @return
     */
    @Override
    public float[] draw() {
        if (frameIndex >= frameCount) {
            return transitionOM;
        }
        frameValue = frames[frameIndex];
        projectionMatrix = MatrixUtils.getOriginalMatrix();
        transitionOM = MatrixUtils.getOriginalMatrix();
//        Matrix.rotateM(transitionOM, 0, frameValue, 0f, 0f, 1f);
        rotateZ();
        Matrix.translateM(transitionOM, 0, -coordX, -coordY, Z_VIEW);
        Matrix.translateM(projectionMatrix, 0, coordX, coordY, 0);
        Matrix.multiplyMM(transitionOM, 0, projectionMatrix, 0, transitionOM, 0);
        frameIndex++;
        return transitionOM;
    }

    private void rotateZ() {
        float[] temp = MatrixUtils.getOriginalMatrix();
        Matrix.scaleM(temp, 0, height, width, 1);
        Matrix.multiplyMM(transitionOM, 0, transitionOM, 0, temp, 0);
        Matrix.rotateM(transitionOM, 0, frameValue, 0f, 0f, 1f);
        Matrix.invertM(temp, 0, temp, 0);
        Matrix.multiplyMM(transitionOM, 0, transitionOM, 0, temp, 0);
    }

    @Override
    public float[] prepare() {
        return null;
    }


    @Override
    public void seek(int duration) {

    }

    public void setRotateCoord(float x, float y) {
        this.coordX = x;
        this.coordY = coordY;
    }

}
