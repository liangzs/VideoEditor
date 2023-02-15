package com.ijoysoft.mediasdk.module.opengl.theme.action.widgetaction;

import android.opengl.Matrix;

import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.EvaluatorHelper;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAnimateAction;

/**
 * 入场动画结束时，切换到悬停动画的开始
 * frame帧数根据传入的时间按25f/s进行计算,1000ms 30f 那么大概是40ms一帧
 */
public class widgetOneAnimation extends BaseEvaluate implements IAnimateAction {
    /**
     * 根据悬停的类型，还有悬停时间，计算出的帧集合
     * 可能是存储角度，也有可能是存储偏移量
     */
    private float[] frames;
    //当前帧
    private int frameIndex;
    private float frameValue;

    public widgetOneAnimation(int duration, int width, int height) {
        this.duration = duration;
        frames = EvaluatorHelper.evaluateWidgetCoord(duration, 0, 0);
        frameCount = frames.length;
        this.width = width;
        this.height = height;
    }


    @Override
    public float[] draw() {
        if (frameIndex >= frameCount) {
            return transitionOM;
        }
        frameValue = frames[frameIndex];
        transitionOM = MatrixUtils.getOriginalMatrix();
        Matrix.translateM(transitionOM, 0, 0f, frameValue, Z_VIEW);
        Matrix.rotateM(transitionOM, 0, 0, 1f, 0f, 0f);
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
