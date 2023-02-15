package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

import android.opengl.Matrix;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;

/**
 * 进入弹跳一下马上停止,主要用于控件
 */
public class EnterEaseOutElastic extends BaseEvaluate {
    /**
     * 根据悬停的类型，还有悬停时间，计算出的帧集合
     * 可能是存储角度，也有可能是存储偏移量
     */
    private float[] frames;
    //当前帧
    private int frameIndex;
    private float frameValue;
    private AnimateInfo.ORIENTATION orientationType;
    private float originValue;
    private int delayCount;

    public EnterEaseOutElastic(AnimationBuilder animationBuilder) {
        super(animationBuilder);
        transitionX = animationBuilder.endX - animationBuilder.startX;
        transitionY = animationBuilder.endY - animationBuilder.startY;
        this.orientationType = animationBuilder.orientationType;
        switch (orientationType) {
            case TOP:
            case BOTTOM:
                originValue = animationBuilder.startY;
                frames = evaluateAnimateCoord(duration, transitionY);
                break;
            case LEFT:
            case RIGHT:
                originValue = animationBuilder.startX;
                frames = evaluateAnimateCoord(duration, transitionX);
                break;

        }
//        Z_VIEW = 0;
        Z_VIEW = animationBuilder.zView;
        frameCount = frames.length;
        delayCount = animationBuilder.enterDelay * ConstantMediaSize.FPS / 1000;
    }


    @Override
    public float[] draw() {
        if (frameIndex >= frameCount + delayCount) {
            return transitionOM;
        }
        if (frameIndex < delayCount) {
            frameIndex++;
            return transitionOM;
        }

        frameValue = frames[frameIndex - delayCount];
        transitionOM = MatrixUtils.getOriginalMatrix();
        switch (orientationType) {
            case TOP:
            case BOTTOM:
                Matrix.translateM(transitionOM, 0, 0, originValue + frameValue, Z_VIEW);
                break;
            case LEFT:
            case RIGHT:
                Matrix.translateM(transitionOM, 0, originValue + frameValue, 0, Z_VIEW);
                break;

        }
        if (transitionScale != 0) {
            Matrix.scaleM(transitionOM, 0, transitionScale, transitionScale, Z_VIEW);
        }
//        Log.i("test1", "transtionY:" + transitionY + "," + Arrays.toString(frames));
        frameIndex++;
        return transitionOM;
    }

    @Override
    public float[] prepare() {
        transitionOM = MatrixUtils.getOriginalMatrix();
        Matrix.translateM(transitionOM, 0, transitionX, transitionY, Z_VIEW);
        return transitionOM;
    }


    @Override
    public void seek(int duration) {

    }


    /**
     * 计算动画数组
     *
     * @return
     */
    public float[] evaluateAnimateCoord(long duration, float translate) {
        int framesNum = ((int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f));
        double c4 = (2 * Math.PI) / 3;
        float[] frames = new float[framesNum];
        double frameValue = 0;
        float x;
        /**
         */
        for (int i = 0; i < framesNum; i++) {
            x = i * 1.0F / (framesNum - 1);
            frameValue = x == 0 ? 0 : x == 1 ? 1 : Math.pow(2, -16 * x) * Math.sin((x * 10 - 0.75) * c4) + 1;
            frames[i] = (float) (frameValue * translate);
        }
        return frames;
    }

    public float[] getFrames() {
        return frames;
    }

    @Override
    public void reset() {
        frameIndex = 0;
    }
}
