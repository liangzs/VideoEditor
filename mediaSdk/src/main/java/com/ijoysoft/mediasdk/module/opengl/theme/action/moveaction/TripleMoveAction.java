package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveAction;

/**
 * @author hayring
 * @date 2021/12/30  11:07
 */
public class TripleMoveAction implements IMoveAction {


    enum Status {
        /**
         * 位移
         */
        TRANSITION,
        /**
         * 旋转
         */
        ROTATE,
        /**
         * 缩放
         */
        SCALE,

        /**
         * 错切
         */
        SKEW
    }

    private Status currentStatus = Status.TRANSITION;

    @Override
    public float[] action(float duration, float start, float end) {
        switch (currentStatus) {
            case TRANSITION:
                return actionTransition(duration, start, end);
            case SCALE:
                return actionScale(duration, start, end);
            case ROTATE:
                return actionRotate(duration, start, end);
            case SKEW:
                return actionSkew(duration, start, end);
            default: throw new IllegalStateException("out of enum");
        }
    }

    @Override
    public boolean isTranslate() {
        currentStatus = Status.TRANSITION;
        return true;
    }

    @Override
    public boolean isRotate() {
        currentStatus = Status.ROTATE;
        return true;
    }

    @Override
    public boolean isScale() {
        currentStatus = Status.SCALE;
        return true;
    }

    @Override
    public boolean isSkew() {
        currentStatus = Status.SKEW;
        return true;
    }

    /**
     * 位移操作
     * @param duration 时间
     * @param start 初始值
     * @param end 结束值
     * @return 帧
     */
    public float[] actionTransition(float duration, float start, float end) {
        return actionLinear(duration, start, end);
    }

    /**
     * 缩放操作
     * @param duration 时间
     * @param start 初始值
     * @param end 结束值
     * @return 帧
     */
    public float[] actionScale(float duration, float start, float end) {
        return actionLinear(duration, start, end);
    }

    /**
     * 旋转操作
     * @param duration 时间
     * @param start 初始值
     * @param end 结束值
     * @return 帧
     */
    public float[] actionRotate(float duration, float start, float end) {
        return actionLinear(duration, start, end);
    }

    /**
     * 错切操作
     * @param duration 时间
     * @param start 初始值
     * @param end 结束值
     * @return 帧
     */
    public float[] actionSkew(float duration, float start, float end) {
        return actionLinear(duration, start, end);
    }


    /**
     * 默认操作：线性变化
     * @param duration 时间
     * @param start 初始值
     * @param end 结束值
     * @return 帧
     */
    protected static float[] actionLinear(float duration, float start, float end) {
        int framesNum = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        double frameValue;
        float offset = end - start;
        float x;
        for (int i = 0; i < framesNum; i++) {
            x = i * 1.0F / (framesNum - 1);
            frameValue = x == 0 ? 0 : x > 1 ? 1 : x;
            frames[i] = start + (float) (frameValue * offset);
        }
        return frames;
    }

}
