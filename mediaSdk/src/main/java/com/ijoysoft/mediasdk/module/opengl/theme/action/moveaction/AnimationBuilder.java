package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveAction;

public class AnimationBuilder {
    public int duration;
    public int conor;

    public float startX, startY, endX, endY;

    /**
     * 错切角
     */
    public float startSkewX, startSkewY, endSkewX, endSkewY;
    public float startConor, endConor;
    public AnimateInfo.ROTATION rotationType;
    public AnimateInfo.ORIENTATION orientationType;

    public int width, height;
    public boolean isWidget;
    public float startFade, endFade;

    public boolean isNoZaxis;
    public float startScale, endScale;
    public float scaleCenterX, scaleCenterY;
    public float zView;
    public int enterDelay;
    public float bgConor;
    //动感模糊blurRange做值渐变，blurCornor只做值传递
    public float startBlurRange, endBlurRange;
    //旋转中心点
    public float rotateCenterX;
    public float rotateCenterY;
    //动画曲线，加减速
    public IMoveAction moveAction;
    public IMoveAction moveActionX;
    //旋转，区分和移动加速的动画速度
    public IMoveAction rotateAction;
    //缩放，区分和移动加速的动画速度
    public IMoveAction scaleAction;
    public IMoveAction skewAction;
    public ActionBlurType blurType = ActionBlurType.NONE;
    //y轴弹簧效果
    public boolean isSpringY;
    public boolean isSpringX;
    //自定义通用字段
    public float cusStart, cusEnd, cusStart2, cusEnd2;
    //自定义动画轨迹
    public IMoveAction cusAction, cusAction2;

    /**
     * 连续性
     */
    public boolean adjective = true;

    /**
     * 只对x轴缩放
     */
    public boolean onlyScaleX;

    /**
     * 只对y轴缩放
     */
    public boolean onlyScaleY;

    public boolean log = false;


    public AnimationBuilder() {

    }

    public AnimationBuilder(int duration) {
        this.duration = duration;
    }

    public AnimationBuilder(int duration, int width, int height, boolean isNoZaxis) {
        this.duration = duration;
        this.width = width;
        this.height = height;
        this.isNoZaxis = isNoZaxis;
    }

    public AnimationBuilder setCoordinate(float startX, float startY, float endX, float endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        return this;
    }

    /**
     * degree
     * @param startSkewX 起始错切x角
     * @param startSkewY 起始错切y角
     * @param endSkewX 结束错切x角
     * @param endSkewY 结束错切y角
     * @return this
     */
    public AnimationBuilder setSkew(float startSkewX, float startSkewY, float endSkewX, float endSkewY) {
        this.startSkewX = startSkewX;
        this.startSkewY = startSkewY;
        this.endSkewX = endSkewX;
        this.endSkewY = endSkewY;
        return this;
    }

    public AnimationBuilder setConors(AnimateInfo.ROTATION rotationType, float startConor, float endConor) {
        this.rotationType = rotationType;
        this.startConor = startConor;
        this.endConor = endConor;
        return this;
    }

    public AnimationBuilder setRotation(AnimateInfo.ROTATION rotation) {
        this.rotationType = rotation;
        return this;
    }

    public AnimationBuilder setWidthHeight(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public AnimationBuilder setConor(int conor) {
        this.conor = conor;
        return this;
    }

    public AnimationBuilder setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public AnimationBuilder setStartX(float startX) {
        this.startX = startX;
        return this;
    }

    public AnimationBuilder setStartY(float startY) {
        this.startY = startY;
        return this;
    }

    public AnimationBuilder setEndX(float endX) {
        this.endX = endX;
        return this;
    }

    public AnimationBuilder setEndY(float endY) {
        this.endY = endY;
        return this;
    }

    public AnimationBuilder setStartConor(int startConor) {
        this.startConor = startConor;
        return this;
    }

    public AnimationBuilder setEndConor(int endConor) {
        this.endConor = endConor;
        return this;
    }

    /**
     * 设置z轴转动的时候，记得设置width，height值
     * 以为通过宽高找到中心店进行旋转的
     *
     * @param rotationType
     * @return
     */
    public AnimationBuilder setRotationType(AnimateInfo.ROTATION rotationType) {
        this.rotationType = rotationType;
        return this;
    }

    public AnimationBuilder setIsWidget(boolean widget) {
        isWidget = widget;
        return this;
    }

    public AnimationBuilder setFade(float startFade, float endFade) {
        this.startFade = startFade;
        this.endFade = endFade;
        return this;
    }

    public AnimationBuilder setIsNoZaxis(boolean noZaxis) {
        isNoZaxis = noZaxis;
        return this;
    }

    public AnimationBuilder setOrientation(AnimateInfo.ORIENTATION orientation) {
        orientationType = orientation;
        return this;
    }

    public AnimationBuilder setScale(float startScale, float endScale) {
        this.startScale = startScale;
        this.endScale = endScale;
        return this;
    }

    public AnimationBuilder setScale(float startScale, float endScale, float scaleCenterX, float scaleCenterY) {
        this.startScale = startScale;
        this.endScale = endScale;
        this.scaleCenterX = scaleCenterX;
        this.scaleCenterY = scaleCenterY;
        return this;
    }

    public AnimationBuilder setZView(float zView) {
        this.zView = zView;
        return this;
    }

    public AnimationBuilder setEnterDelay(int duration) {
        this.enterDelay = duration;
        return this;
    }

    public AnimationBuilder setBgConor(float bgConor) {
        this.bgConor = bgConor;
        return this;
    }

    public AnimationBuilder setOnlyScaleX(boolean onlyScaleX) {
        this.onlyScaleX = onlyScaleX;
        return this;
    }

    public AnimationBuilder setOnlyScaleY(boolean onlyScaleY) {
        this.onlyScaleY = onlyScaleY;
        return this;
    }

    public AnimationBuilder setIsSprintY(boolean isSpringY) {
        this.isSpringY = isSpringY;
        return this;
    }

    public AnimationBuilder setIsSprintX(boolean isSpringX) {
        this.isSpringX = isSpringX;
        return this;
    }

    public AnimationBuilder setMoveBlurRange(@FloatRange(from = 0, to = 20) float startBlurRange, @FloatRange(from = 0, to = 20) float endBlurRange) {
        this.startBlurRange = startBlurRange;
        this.endBlurRange = endBlurRange;
        this.blurType = ActionBlurType.MOVE_BLUR;
        return this;
    }

    public AnimationBuilder setMoveBlurRange(ActionBlurType blurType, @FloatRange(from = 0, to = 20) float startBlurRange, @FloatRange(from = 0, to = 20) float endBlurRange) {
        this.startBlurRange = startBlurRange;
        this.endBlurRange = endBlurRange;
        this.blurType = blurType;
        if (blurType == ActionBlurType.RATIO_BLUR) {
            this.startBlurRange = startBlurRange / 20;
            this.endBlurRange = endBlurRange / 20;
        }
        return this;
    }

    /**
     * 径向模糊
     *
     * @param startBlurRange
     * @param endBlurRange
     * @param zoomIn         放大则是扩散，缩小则是内缩模糊
     * @return
     */
    public AnimationBuilder setRatioBlurRange(@FloatRange(from = 0, to = 10) float startBlurRange, @FloatRange(from = 0, to = 10) float endBlurRange, boolean zoomOut) {
        this.startBlurRange = startBlurRange;
        this.endBlurRange = endBlurRange;
        blurType = ActionBlurType.RATIO_BLUR;
        float flag = zoomOut ? -1f : 1f;
        this.startBlurRange = startBlurRange / 10 * flag;
        this.endBlurRange = endBlurRange / 10 * flag;
        return this;
    }


    public AnimationBuilder setMoveAction(IMoveAction moveAction) {
        this.moveAction = moveAction;
        return this;
    }

    public AnimationBuilder setMoveActionX(IMoveAction moveAction) {
        this.moveActionX = moveAction;
        return this;
    }

    public AnimationBuilder setRotateAction(IMoveAction iRotateAcion) {
        this.rotateAction = iRotateAcion;
        return this;
    }

    public AnimationBuilder setScaleAction(IMoveAction scaleAction) {
        this.scaleAction = scaleAction;
        return this;
    }

    public AnimationBuilder setSkewAction(IMoveAction skewAction) {
        this.skewAction = skewAction;
        return this;
    }

    /**
     * 设置z轴选中时，设置的旋转点中心，现在默认是0.5，0.5纹理的中心作为旋转的点
     *
     * @param rotateCenterX
     * @param rotateCenterY
     * @return
     */
    public AnimationBuilder setRotaCenter(float rotateCenterX, float rotateCenterY) {
        this.rotateCenterX = rotateCenterX;
        this.rotateCenterY = rotateCenterY;
        return this;
    }

    /**
     * 自定义通用的字段
     *
     * @param cusStart
     * @param cusEnd
     * @return
     */
    public AnimationBuilder setCusStartEnd(float cusStart, float cusEnd) {
        this.cusStart = cusStart;
        this.cusEnd = cusEnd;
        return this;
    }

    /**
     * 自定义通用的字段
     *
     * @return
     */
    public AnimationBuilder setCusStartEnd2(float cusStart2, float cusEnd2) {
        this.cusStart2 = cusStart2;
        this.cusEnd2 = cusEnd2;
        return this;
    }

    /**
     * 自定义字段的动画
     *
     * @param iMoveAction
     * @return
     */
    public AnimationBuilder setCusAction(IMoveAction iMoveAction) {
        this.cusAction = iMoveAction;
        return this;
    }

    /**
     * 自定义字段的动画
     *
     * @param iMoveAction
     * @return
     */
    public AnimationBuilder setCusAction2(IMoveAction iMoveAction) {
        this.cusAction2 = iMoveAction;
        return this;
    }


    public BaseEvaluate build() {
        return new BaseEvaluate(this);
    }

    public AnimationBuilder setAdjective(boolean adjective) {
        this.adjective = adjective;
        return this;
    }

    public AnimationBuilder setStartXY(float startX, float startY) {
        this.startX = startX;
        this.startY = startY;
        return this;
    }

    public AnimationBuilder setEndXY(float endX, float endY) {
        this.endX = endX;
        this.endY = endY;
        return this;
    }

    public AnimationBuilder setStayPos(float[] pos) {
        return setStartEndXY(pos[0], pos[1], pos[0], pos[1]);
    }

    public AnimationBuilder setStartEndXY(float startX, float startY, float endX, float endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        return this;
    }


    public AnimationBuilder setLog(boolean log) {
        this.log = log;
        return this;
    }



}
