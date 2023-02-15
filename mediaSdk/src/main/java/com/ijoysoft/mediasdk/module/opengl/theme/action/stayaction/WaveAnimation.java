package com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction;

import android.opengl.Matrix;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.EvaluatorHelper;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAnimateAction;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;

/**
 * 波浪进出动画
 */
public class WaveAnimation extends BaseEvaluate implements IAnimateAction {
    /**
     * 根据悬停的类型，还有悬停时间，计算出的帧集合
     * 可能是存储角度，也有可能是存储偏移量
     */
    private float[] frames;
    //当前帧
    private int frameIndex;
    private float frameValue;

    public WaveAnimation(AnimationBuilder builder) {
        super(builder);
        float scale = 0.06f;
        switch (ConstantMediaSize.ratioType) {
            case _4_3:
            case _16_9:
                scale = 0.12f;
                break;
        }
        frames = EvaluatorHelper.evaluateWaveAnimation((int) duration, 3, scale);
        frameCount = frames.length;
    }


    /**
     * 因为纹理坐标和屏幕坐标是相反的，所以这里需要y值也取反
     *
     * @return
     */
    @Override
    public float[] draw() {
        frameIndex++;
        if (frameIndex < frameCount) {
            transitionX += offsetX;
            transitionY += offsetY;
            conor += offsetConor;
            transitionScale += offsetScale;
        }
        transitionOM = MatrixUtils.getOriginalMatrix();
        Matrix.translateM(transitionOM, 0, transitionX, transitionY, Z_VIEW);
        Matrix.scaleM(transitionOM, 0, transitionScale, transitionScale, 1);
        doRatate();
        if (frameIndex < frames.length) {
            frameValue = frames[frameIndex];
        }
        Matrix.translateM(transitionOM, 0, 0, frameValue, 0);
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
