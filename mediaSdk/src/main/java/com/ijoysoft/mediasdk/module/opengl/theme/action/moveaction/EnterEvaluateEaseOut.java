package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

import android.opengl.Matrix;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;

/**
 * 入场动画结束时，切换到悬停动画的开始
 * frame帧数根据传入的时间按25f/s进行计算,1000ms 30f 那么大概是40ms一帧
 *
 * 主题二快速进场
 */
public class EnterEvaluateEaseOut extends BaseEvaluate {
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

    public EnterEvaluateEaseOut(AnimationBuilder animationBuilder) {
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
        Z_VIEW = 0;
        frameCount = frames.length;

    }


    @Override
    public float[] draw() {
        if (frameIndex >= frameCount) {
            return transitionOM;
        }

        frameValue = frames[frameIndex];
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
     * 1 - pow(1 - x, 5)
     *
     * @return
     */
    public float[] evaluateAnimateCoord(long duration, float translate) {
        int framesNum = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        float frameValue = 0;
        float x;
        /**
         * 1 - pow(1 - x, 5)
         *
         * sqrt(1 - pow(x - 1, 2))
         */
        for (int i = 0; i < framesNum; i++) {
            x = i * 1.0F / (framesNum - 1);
            frameValue = (float) Math.sqrt(1 - Math.pow(x - 1, 2));
            frames[i] = frameValue * translate;
        }
        return frames;
    }


    public float[] getFrames() {
        return frames;
    }

}
