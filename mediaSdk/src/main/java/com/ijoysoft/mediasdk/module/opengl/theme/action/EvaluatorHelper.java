package com.ijoysoft.mediasdk.module.opengl.theme.action;

import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.ActionBlurType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseCustom;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseCustomNoOffset;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInCubic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInMultiple;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutCubic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutMultiple;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.Easings;

import java.util.ArrayList;
import java.util.List;

/**
 * 动画悬停非线性估值器
 */
public class EvaluatorHelper {


    /**
     * 上下浮动
     */
    public static ValueAnimator topBottomMove(int duration, float start, float end, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(animatorUpdateListener);
        return animator;
    }

    /**
     * 计算动画数组
     * 计算动画时，x，y两个值都需要归一化成0-1范围
     *
     * @return
     */
    public static float[] evaluateAnimateCoord(int duration, float start, float end, AnimateInfo.STAY stayType) {
        if (ObjectUtils.isEmpty(stayType)) {
            return new float[0];
        }
        int framesNum = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        float frameValue = 0;
        float x;
        switch (stayType) {
            /**
             * 根据x在[0-1]计算的值，目前跟start和end关系不是太大对于上下悬停来说
             * pow(2, -3 * (x + 0.5)) * sin(x * 3.14 / 0.2) * -0.3
             */
            case SPRING_Y:
                for (int i = 0; i < framesNum; i++) {
                    x = i * 1.0F / (framesNum - 1);
                    frameValue = (float) (Math.pow(2, -3 * (x + 0.5)) * Math.sin(x * Math.PI / 0.2f) * 0.3f);
                    frames[i] = frameValue;
                }
                return frames;
            case ROTATE_X:
                for (int i = 0; i < framesNum; i++) {
                    x = i * 1.0F / (framesNum - 1);
                    frameValue = (float) (Math.pow(2, -3 * (x + 0.5)) * Math.sin(x * Math.PI / 0.2f) * 10f);
                    frames[i] = frameValue;
                }
                return frames;
            case ROTATE_Y:
                for (int i = 0; i < framesNum; i++) {
                    x = i * 1.0F / (framesNum - 1);
                    frameValue = (float) (Math.pow(2, -3 * (x + 0.5)) * Math.sin(x * Math.PI / 0.2f) * 10f);
                    frames[i] = frameValue;
                }
                return frames;
            case ROTATE_Z:
                for (int i = 0; i < framesNum; i++) {
                    x = i * 1.0F / (framesNum - 1);
                    frameValue = (float) (Math.pow(2, -3 * (x + 0.5)) * Math.sin(x * Math.PI / 0.2f) * 10f);
                    frames[i] = frameValue;
                }
                return frames;
            case ROTATE_XY:
                for (int i = 0; i < framesNum; i++) {
                    x = i * 1.0F / (framesNum - 1);
                    frameValue = (float) (Math.pow(2, -3 * (x + 0.5)) * Math.sin(x * Math.PI / 0.2f) * 10f);
                    frames[i] = frameValue;
                }
                return frames;
            case ROTATE_XY_RE:
                for (int i = 0; i < framesNum; i++) {
                    x = i * 1.0F / (framesNum - 1);
                    frameValue = (float) (Math.pow(2, -3 * (x + 0.5)) * Math.sin(x * Math.PI / 0.2f) * 10f);
                    frames[i] = frameValue;
                }
                return frames;
        }
        return frames;
    }


    /**
     * 计算动画数组
     * 计算动画时，x，y两个值都需要归一化成0-1范围
     * pow(2, -3 * (x + 0.6)) * sin((x+0.1) * 3.14 / 0.2) * -0.3
     *
     * @return
     */
    public static float[] evaluateSringYReverse(int duration) {
        int framesNum = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        float frameValue = 0;
        float x;
        for (int i = 0; i < framesNum; i++) {
            x = i * 1.0F / (framesNum - 1);
            frameValue = (float) (Math.pow(2, -3 * (x + 0.6)) * Math.sin((x + 0.1) * Math.PI / 0.2f) * 0.25f);
            frames[i] = frameValue;
        }
        return frames;
    }


    /**
     * 悬停动画，后面再根据具体类型再进行扩展
     *
     * @param duration
     * @param start
     * @param end      pow(2, -3 * (x + 0.5)) * sin(2*x * 3.14) * 0.6
     * @return
     */
    public static float[] evaluateWidgetCoord(int duration, float start, float end) {
        int framesNum = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        float frameValue = 0;
        float x;
        for (int i = 0; i < framesNum; i++) {
            x = i * 1.0F / (framesNum - 1);
            frameValue = (float) (Math.pow(2, -3 * (x + 0.5)) * Math.sin(2 * x * Math.PI) * 0.3);
            frames[i] = frameValue;
        }
        return frames;
    }

    /**
     * 目前根据glsl的mix函数，值域范围取0-1
     *
     * @param duration
     * @return
     */
    public static float[] evaluateWidgetMixProgress(int duration) {
        int framesNum = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        float ave = 1f / framesNum;
        for (int i = 0; i < framesNum; i++) {
            frames[i] = ave * (i + 1f);
        }
        return frames;
    }

    /**
     * 计算动画数组
     * 计算动画时，x，y两个值都需要归一化成0-1范围
     * cycles=5
     *
     * @return
     */
    public static float[] evaluateShakeStayAnimation(int duration, int period, int angle) {
        int framesNum = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        float frameValue;
        float x;
        for (int i = 0; i < framesNum; i++) {
            x = i * 1.0F / (framesNum - 1);
            frameValue = (float) (Math.sin(period * Math.PI * x) * angle);
            frames[i] = frameValue;
        }
        return frames;
    }

    /**
     * 波浪动画组
     * 计算动画时，x，y两个值都需要归一化成0-1范围
     * cycles=5
     *
     * @return
     */
    public static float[] evaluateWaveAnimation(int duration, int period, float scale) {
        int framesNum = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        float frameValue;
        float x;
        for (int i = 0; i < framesNum; i++) {
            x = i * 1.0F / (framesNum - 1);
            frameValue = (float) (Math.sin(period * Math.PI * x) * scale);
            frames[i] = frameValue;
        }
        return frames;
    }


    /**
     * 模糊左进左出
     *
     * @return
     */
    public static List<BaseEvaluate> moveBLurLeft2Left(long currentDuration, long duration, int width, int height) {
        List<BaseEvaluate> list = new ArrayList<>();
        list.add(new AnimationBuilder((int) (currentDuration * 0.4f), width, height, true)
                .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 5, 0)
                .setRotaCenter(0, -0.7f)
                .setMoveAction(new EaseOutCubic(true, false, true))
                .setConors(AnimateInfo.ROTATION.Z_ANXIS, 2, 1)
                .setRotateAction(new EaseCustom(x -> Easings.smoothStepDelay(x)))
                .setCoordinate(-0.6f, 0, 0f, 0).build());

        if (duration > currentDuration) {
            list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                    .build());
        }
        list.add(new AnimationBuilder((int) (currentDuration * 0.6f), width, height, true)
                .setRotaCenter(0, -0.7f)
                .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 1, -1)
                .setRotateAction(new EaseOutCubic())
                .setMoveAction(new EaseInCubic(true, false, false))
                .setCoordinate(0.0f, 0, -2f, 0).build());

        return list;
    }

    /**
     * 右进右出，动感模糊
     *
     * @param duration
     * @param width
     * @param height
     * @return
     */
    public static List<BaseEvaluate> moveBlurRight2Right(long currentDuration, long duration, int width, int height) {
        List<BaseEvaluate> list = new ArrayList<>();
        list.add(new AnimationBuilder((int) (currentDuration * 0.4f), width, height, true)
                .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 5, 0)
                .setMoveAction(new EaseOutCubic(true, false, true))
                .setRotaCenter(0, -0.7f)
                .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 2, 1)
                .setRotateAction(new EaseCustom(x -> Easings.smoothStepDelay(x)))
                .setCoordinate(0.6f, 0, 0f, 0).build());
        if (duration > currentDuration) {
            list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                    .build());
        }
        list.add(new AnimationBuilder((int) (currentDuration * 0.6f), width, height, true)
                .setRotaCenter(0, -0.7f)
                .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 1, -1)
                .setRotateAction(new EaseOutCubic())
                .setMoveAction(new EaseInCubic(true, false, false))
                .setCoordinate(0.0f, 0, 2f, 0).build());
        return list;
    }


    /**
     * 径向模糊缩小进场，缩小出场
     *
     * @param duration
     * @param width
     * @param height
     * @return
     */
    public static List<BaseEvaluate> zoomOut2ZoomOut(long duration, int width, int height) {
        List<BaseEvaluate> list = new ArrayList<>();
        list.add(new AnimationBuilder((int) (duration * 0.5), width, height, true)
                .setScaleAction(new EaseOutCubic())
                .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 5, 0)
                .setScale(2f, 1.1f)
                .build());
        list.add(new AnimationBuilder((int) (duration * 0.1), width, height, true)
                .build());
        list.add(new AnimationBuilder((int) (duration * 0.4), width, height, true)
                .setScaleAction(new EaseInCubic())
                .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 0, 1)
                .setScale(1.1f, 0.8f)
                .build());
        return list;
    }

    /**
     * 径向模糊缩小进场，放大出场
     *
     * @param duration
     * @param width
     * @param height
     * @return
     */
    public static List<BaseEvaluate> zoomOut2ZoomIn(long duration, int width, int height) {
        List<BaseEvaluate> list = new ArrayList<>();
        list.add(new AnimationBuilder((int) (duration * 0.5), width, height, true)
                .setScaleAction(new EaseOutCubic())
                .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 5, 0)
                .setScale(1.8f, 1.0f)
                .build());
        list.add(new AnimationBuilder((int) (duration * 0.1), width, height, true)
                .build());
        list.add(new AnimationBuilder((int) (duration * 0.4), width, height, true)
                .setScaleAction(new EaseInCubic())
                .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 0, 1)
                .setScale(1.0f, 1.8f)
                .build());
        return list;
    }

    /**
     * 缩小上移放大
     *
     * @return
     */
    public static List<BaseEvaluate> zoomOut2MoveTop2ZoomIn(long currentDuration, long duration, int width, int height) {
        List<BaseEvaluate> list = new ArrayList<>();
        //动感模糊缩小，向上平移，动感模糊放大
        list.add(new AnimationBuilder((int) (currentDuration * 0.3f), width, height, true)
                .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 3, 0)
                .setCoordinate(0, 0.1f, 0, 0.1f)
                .setMoveAction(new EaseOutMultiple(5, false, true, false))
                .setScale(1.7f, 1f).build());
        list.add(new AnimationBuilder((int) (duration - currentDuration * 0.6f), width, height, true)
                .setMoveAction(new EaseCustom(x -> Easings.easeInOutSine(x)))
                .setCoordinate(0, 0.1f, 0, 0.0f).build());
        list.add(new AnimationBuilder((int) (currentDuration * 0.3f), width, height, true)
                .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 0, 3)
                .setMoveAction(new EaseInMultiple(5, false, true, false))
                .setScale(1.0f, 1.7f).build());
        return list;
    }

    /**
     * 缩小，微无规则散动，再缩小
     *
     * @return
     */
    public static List<BaseEvaluate> zoomOut2MoveFree2ZoomOut(long currentDuration, long duration, int width, int height) {
        List<BaseEvaluate> list = new ArrayList<>();
        //动感模糊缩小，向上平移，动感模糊放大
        list.add(new AnimationBuilder((int) (currentDuration * 0.3), width, height, true)
                .setScaleAction(new EaseOutCubic())
                .setRatioBlurRange(2, 0, true)
                .setScale(1.7f, 1.2f)
                .setStartConor(2)
                .setRotateAction(new EaseCustom(x -> Easings.smoothStepDelay(x)))
                .setRotaCenter(-0.4f, 0f)
                .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 2, 1.5f)
                .build());
        if (duration > currentDuration) {
            list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                    .build());
        }
        list.add(new AnimationBuilder((int) (currentDuration * 0.5), width, height, true)
                .setScaleAction(new EaseInCubic())
                .setRotaCenter(-0.1f, -0.7f)
                .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 1.5f, 1f)
                .setCoordinate(0, 0, -0.05f, 0.01f)
                .setScale(1.2f, 0.8f)
                .build());
        return list;
    }

    /**
     * //缩小弹簧进,模糊放大出
     *
     * @return
     */
    public static List<BaseEvaluate> zoomOutSpring2ZoomOut(long currentDuration, long duration, int width, int height) {
        List<BaseEvaluate> list = new ArrayList<>();
        //动感模糊缩小，向上平移，动感模糊放大
        list.add(new AnimationBuilder((int) (currentDuration * 0.7), width, height, true)
                .setScale(1.2f, 1.0f)
                .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                .setRotateAction(new EaseCustomNoOffset(x -> Easings.easeSpring(x, 20)))
                .build());
        if (duration > currentDuration) {
            list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                    .build());
        }
        list.add(new AnimationBuilder((int) (currentDuration * 0.3), width, height, true)
                .setScale(1.0f, 1.8f)
                .setRatioBlurRange(0, 2, false)
                .setScaleAction(new EaseInCubic())
                .build());
        return list;
    }

    /**
     * //右下角弹动，带微角度，角度为右下角
     *
     * @return
     */
    public static List<BaseEvaluate> Bottom2SpringYBycornorRight(long currentDuration, long duration, int width, int height) {
        List<BaseEvaluate> list = new ArrayList<>();
        list.add(new AnimationBuilder((int) (currentDuration), width, height, true)
                .setRotaCenter(0.8f, 0.8f)
                .setRotateAction(new EaseCustomNoOffset(x -> Easings.easeSpringCommon15Conor(x, 2)))
                .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                .setIsSprintY(true)
                .setMoveAction(new EaseCustomNoOffset(x -> Easings.easeSpringCommon15(x, 0.1f, -1)))
                .setMoveActionX(new EaseCustomNoOffset(x -> Easings.easeSpringCommon15X(x, -1)))
                .setIsSprintX(true)
                //模糊
                .setCusAction(new EaseOutMultiple(5))
                .setCusStartEnd(10, 0)
                .setCusAction2(new EaseOutMultiple(5))
                .setCusStartEnd2(90, 45)
                .build());
        if (duration > currentDuration) {
            list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                    .build());
        }
        return list;
    }

    /**
     * //左下角弹动
     *
     * @return
     */
    public static List<BaseEvaluate> Bottom2SpringYBycornorLeft(long currentDuration, long duration, int width, int height) {
        List<BaseEvaluate> list = new ArrayList<>();
        list.add(new AnimationBuilder((int) (currentDuration), width, height, true)
                .setRotaCenter(0.8f, 0.8f)
                .setRotateAction(new EaseCustomNoOffset(x -> Easings.easeSpringCommon15Conor(x, 2)))
                .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                .setIsSprintY(true)
                .setMoveAction(new EaseCustomNoOffset(x -> Easings.easeSpringCommon15(x, 0.1f, 1)))
                .setMoveActionX(new EaseCustomNoOffset(x -> Easings.easeSpringCommon15X(x, 1)))
                .setIsSprintX(true)
                //模糊
                .setCusAction(new EaseOutMultiple(5))
                .setCusStartEnd(10, 0)
                .setCusAction2(new EaseOutMultiple(5))
                .setCusStartEnd2(90, 45)
                .build());

        if (duration > currentDuration) {
            list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                    .build());
        }
        return list;
    }


    /**
     * //中下点弹簧旋转以及模糊渐变
     *
     * @return
     */
    public static List<BaseEvaluate> springYBycornorCenter(float center, long currentDuration, long duration, int width, int height) {
        List<BaseEvaluate> list = new ArrayList<>();
        list.add(new AnimationBuilder((int) (currentDuration * 0.5f), width, height, true)
                .setRotaCenter(0f, center)
                .setRotateAction(new EaseCustomNoOffset(x -> Easings.easeSpringchristmas5(x, 20)))
                .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                //模糊,通过cutomValue传递
                .setCusAction(new EaseOutMultiple(5))
                .setCusStartEnd(10, 0)
                .setCusAction2(new EaseOutMultiple(5))
                .setCusStartEnd2(90, 45)
                .build());
        list.add(new AnimationBuilder((int) (duration - currentDuration * 0.5f), width, height, true)
                .build());
        return list;
    }

    /**
     * 只做简单的旋转
     *
     * @param center
     * @param currentDuration
     * @param duration
     * @param width
     * @param height
     * @return
     */
    public static List<BaseEvaluate> springYBycornorCenterNormal(float center, long currentDuration, long duration, int width, int height) {
        List<BaseEvaluate> list = new ArrayList<>();
        list.add(new AnimationBuilder((int) (currentDuration * 0.5f), width, height, true)
                .setRotaCenter(0f, center)
                .setRotateAction(new EaseCustomNoOffset(x -> Easings.easeSpringchristmas5(x, 20)))
                .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                //模糊,通过cutomValue传递
                .setCusAction(new EaseOutMultiple(5))
                .setCusStartEnd(10, 0)
                .setCusAction2(new EaseOutMultiple(5))
                .setCusStartEnd2(90, 45)
                .build());
        list.add(new AnimationBuilder((int) (duration - currentDuration * 0.5f), width, height, true)
                .build());
        return list;
    }

    /**
     * //中下点弹簧旋转以及模糊渐变
     *
     * @return
     */
    public static List<BaseEvaluate> springXBycornorCenter(float center, long currentDuration, long duration, int width, int height) {
        List<BaseEvaluate> list = new ArrayList<>();
        list.add(new AnimationBuilder((int) (currentDuration * 0.5f), width, height, true)
                .setRotaCenter(center, 0f)
                .setRotateAction(new EaseCustomNoOffset(x -> Easings.easeSpringchristmas5(x, 20)))
                .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                //模糊,通过cutomValue传递
                .setCusAction(new EaseOutMultiple(5))
                .setCusStartEnd(10, 0)
                .setCusAction2(new EaseOutMultiple(5))
                .setCusStartEnd2(90, 45)
                .build());
        list.add(new AnimationBuilder((int) (duration - currentDuration * 0.5f), width, height, true)
                .build());
        return list;
    }

    /**
     * //中下点弹簧旋转以及模糊渐变
     *
     * @return
     */
    public static List<BaseEvaluate> springXBycornorCenterNomal(float center, long currentDuration, long duration, int width, int height) {
        List<BaseEvaluate> list = new ArrayList<>();
        list.add(new AnimationBuilder((int) (currentDuration * 0.5f), width, height, true)
                .setRotaCenter(center, 0f)
                .setRotateAction(new EaseCustomNoOffset(x -> Easings.easeSpringFood0(x, 20)))
                .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                //模糊,通过cutomValue传递
                .setCusAction(new EaseOutMultiple(5))
                .setCusStartEnd(10, 0)
                .setCusAction2(new EaseOutMultiple(5))
                .setCusStartEnd2(90, 45)
                .build());
        list.add(new AnimationBuilder((int) (duration - currentDuration * 0.5f), width, height, true)
                .build());
        return list;
    }


}
