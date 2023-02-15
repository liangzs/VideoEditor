package com.ijoysoft.mediasdk.module.opengl.theme.action;

import android.opengl.Matrix;
import android.util.Log;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.common.utils.TimeUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.ActionBlurType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;

import java.util.Arrays;

/**
 * 动画基类，默认是匀速运动，如果是加速运动则在具体实现中复写
 *
 * @TODO 如果需要自旋转，需要传入width和height
 */
public class BaseEvaluate implements IAnimateAction {
    /**
     * 视角拉高
     */
    public float Z_VIEW = -3f;
    protected float[] transitionOM = new float[16];
    //由模型矩阵进行旋转操作，因为旋转只绕中心点去旋转，然后平移操作由世界坐标去完成
    protected float[] projectionMatrix = new float[16];
    protected float offsetX, offsetY, offsetConor, offsetFade, offsetCus, offsetSkewX, offsetSkewY;
    protected float endX, endY, endScale, endSkewX, endSkewY;
    protected boolean isSpringY;
    protected float transitionX, transitionY, transitionConor, transitonRange;
    protected float conor;
    protected float skewX;
    protected float skewY;
    protected float bgConor, bgConorOffset;
    protected float endConor;
    protected float fade;
    protected float[] temp = MatrixUtils.getOriginalMatrix();
    protected int width, height;

    protected float transitionScale = 1f;
    protected float startScale, startX, startY, startConor, startFade, startBlurRange, startSkewX, startSkewY;
    protected float offsetScale, offsetBlurRange;
    //总帧数
    protected int frameCount;

    protected float frameValue;
    //当前帧
    protected int frameIndex;
    /**
     * 时长，单位是ms
     */
    protected long duration;

    protected AnimateInfo.ROTATION rotationType;

    protected boolean isNoZaxis;

    protected boolean isWidgetRotate;
    protected float coordX, coordY;

    private int delayCount;
    private float rotateCenterX, rotateCenterY;
    //动画曲线
    protected IMoveAction moveAction;
    //用frames集合替代index线性递增
    private float[] transitionXFrames;
    private float[] transitionYFrames;
    private float[] rotateFrames;
    private float[] scaleFrames;
    private float[] skewXFrames;
    private float[] skewYFrames;

    //自定义通用字段的线性非线性值
    private float customValue, customValue2;
    private float[] customFrames, customFrames2;
    //动感模糊和径向模糊
    private ActionBlurType blurType;

    /**
     * 连续的
     */
    private boolean adjective = true;


    private boolean log = false;

    public BaseEvaluate() {

    }


    /**
     * 只对x轴缩放
     */
    protected boolean onlyScaleX;

    /**
     * 只对y轴缩放
     */
    protected boolean onlyScaleY;


    /**
     * 通过builder构造对象
     *
     * @param builder
     */
    public BaseEvaluate(AnimationBuilder builder) {
        transitionConor = builder.endConor - builder.startConor;
        rotationType = builder.rotationType;
        duration = builder.duration;
        frameCount = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);

        //设置MaxValue时表示具体值要根据图片测量后得知
        //使用offsetX存储endX作为标记，具体值等待后期修复
        //修复目的是使控件刚刚好在屏幕边缘外，使动画全程都能看见控件，而不是空间在屏幕外做无意义的动画
        //y轴同理
        if (builder.startX != Float.MAX_VALUE && builder.startX != Float.MIN_VALUE
                && builder.endX != Float.MAX_VALUE && builder.endX != Float.MIN_VALUE) {
            //这里移动和旋转都是线性，所以，只需要求一个平均值就好了
            transitionX = this.startX = builder.startX;
            offsetX = (builder.endX - builder.startX) / (1f * frameCount);
        } else {
            startX = builder.startX;
            offsetX = builder.endX;
        }
        //y轴
        if (builder.startY != Float.MAX_VALUE && builder.startY != Float.MIN_VALUE
                && builder.endY != Float.MAX_VALUE && builder.endY != Float.MIN_VALUE) {
            //这里移动和旋转都是线性，所以，只需要求一个平均值就好了
            transitionY = this.startY = builder.startY;
            offsetY = (builder.endY - builder.startY) / (1f * frameCount);
        } else {
            startY = builder.startY;
            offsetY = builder.endY;
        }
        endX = builder.endX;
        endY = builder.endY;
        endScale = builder.endScale;
        endSkewX = builder.endSkewX;
        endSkewY = builder.endSkewY;
        isSpringY = builder.isSpringY;


        skewX = this.startSkewX = builder.startSkewX;
        offsetSkewX = (builder.endSkewX - builder.startSkewX) / (1f * frameCount);
        skewY = this.startSkewY = builder.startSkewY;
        offsetSkewY = (builder.endSkewY - builder.startSkewY) / (1f * frameCount);


        if (onlyScaleX ^ onlyScaleY) {
            onlyScaleX = onlyScaleY = false;
        } else {
            onlyScaleX = builder.onlyScaleX;
            onlyScaleY = builder.onlyScaleY;
        }
        offsetFade = (builder.endFade - builder.startFade) / (1f * frameCount);
        offsetConor = (builder.endConor - builder.startConor) / (1f * frameCount);
        offsetCus = (builder.cusEnd - builder.cusStart) / (1f * frameCount);
        fade = this.startFade = builder.startFade;
        conor = this.startConor = builder.startConor;
        customValue = builder.cusStart;
        this.bgConor = builder.bgConor;
        bgConorOffset = builder.bgConor / (1f * frameCount);
        endConor = builder.endConor;
        this.width = builder.width;
        this.height = builder.height;
        if (builder.zView != 0) {
            Z_VIEW = builder.zView;
        }
        if (builder.isWidget) {
            Z_VIEW = -2f;
        }
        if (builder.isNoZaxis) {
            Z_VIEW = 0;
        }
        this.transitionScale = this.startScale = builder.startScale;
        transitionScale = transitionScale <= 0 ? 1f : transitionScale;
        transitonRange = this.startBlurRange = builder.startBlurRange;
        this.offsetScale = (builder.endScale - builder.startScale) / (1f * frameCount);
        this.offsetBlurRange = (builder.endBlurRange - builder.startBlurRange) / (1f * frameCount);
        delayCount = builder.enterDelay * ConstantMediaSize.FPS / 1000;
        this.rotateCenterX = builder.rotateCenterX;
        this.rotateCenterY = builder.rotateCenterY;
        if (builder.scaleCenterY != 0 || builder.scaleCenterX != 0) {
            isWidgetRotate = true;
            this.coordX = builder.scaleCenterX;
            this.coordY = builder.scaleCenterY;
        }
        this.blurType = builder.blurType;
        //动态动画
        if (builder.moveAction != null) {
            this.moveAction = builder.moveAction;
            if (moveAction.isTranslate()) {
                transitionXFrames = moveAction.action(duration, builder.startX, builder.endX);
                transitionYFrames = moveAction.action(duration, builder.startY, builder.endY);
            }
            if (moveAction.isRotate()) {
                rotateFrames = moveAction.action(duration, builder.startConor, builder.endConor);
            }
            if (moveAction.isScale()) {
                scaleFrames = moveAction.action(duration, builder.startScale, builder.endScale);
            }
            if (moveAction.isSkew()) {
                skewXFrames = moveAction.action(duration, builder.startSkewX, builder.endSkewX);
                skewYFrames = moveAction.action(duration, builder.startSkewY, builder.endSkewY);
            }
            if (builder.isSpringY) {
                transitionYFrames = moveAction.action(duration, builder.startY, builder.endY);
            }
        }
        if (builder.rotateAction != null) {
            rotateFrames = builder.rotateAction.action(duration, builder.startConor, builder.endConor);
        }
        if (builder.scaleAction != null) {
            scaleFrames = builder.scaleAction.action(duration, builder.startScale, builder.endScale);
        }
        if (builder.isSpringX && builder.moveActionX != null) {
            transitionXFrames = builder.moveActionX.action(duration, builder.startX, builder.endX);
        }
        if (builder.cusAction != null) {
            customFrames = builder.cusAction.action(duration, builder.cusStart, builder.cusEnd);
        }
        if (builder.cusAction2 != null) {
            customFrames2 = builder.cusAction2.action(duration, builder.cusStart2, builder.cusEnd2);
        }
        if (builder.skewAction != null) {
            skewXFrames = builder.skewAction.action(duration, builder.startSkewX, builder.endSkewX);
            skewYFrames = builder.skewAction.action(duration, builder.startSkewY, builder.endSkewY);
        }
        adjective = builder.adjective;
        log = builder.log;
    }


    @Override
    public float[] draw() {
        if (log) {
            LogUtils.v("BaseEvaluate_draw_" + BaseEvaluate.this.hashCode(), "draw");
        }
        if (frameIndex >= frameCount + delayCount) {
//            LogUtils.w(getClass().getSimpleName(), "FrameIndex Over Flow, frameIndex:" + frameIndex + " frameCount:" + frameCount);
            return transitionOM;
        }
        if (frameIndex < delayCount) {
            frameIndex++;
            return transitionOM;
        }
        drawBefore();

        frameIndex++;
        transitionOM = MatrixUtils.getOriginalMatrix();
        if (isWidgetRotate) {
            Matrix.scaleM(transitionOM, 0, transitionScale, transitionScale, 1);
            projectionMatrix = MatrixUtils.getOriginalMatrix();
            Matrix.translateM(transitionOM, 0, -coordX, -coordY, 0);
            Matrix.translateM(projectionMatrix, 0, coordX, coordY, 0);
            Matrix.scaleM(projectionMatrix, 0, transitionScale, transitionScale, 1);
            Matrix.multiplyMM(transitionOM, 0, projectionMatrix, 0, transitionOM, 0);
            doRatate();
            return transitionOM;
        }
        Matrix.translateM(transitionOM, 0, transitionX, transitionY, Z_VIEW);
        if (onlyScaleX) {
            Matrix.scaleM(transitionOM, 0, transitionScale, 1f, 1);
        } else if (onlyScaleY) {
            Matrix.scaleM(transitionOM, 0, 1f, transitionScale, 1);
        } else {
            Matrix.scaleM(transitionOM, 0, transitionScale, transitionScale, 1);
        }
        if (log) {
            LogUtils.v("BaseEvaluate_Matrix_" + hashCode(), "matrix: " + MatrixUtils.matrixToString(transitionOM, 0));
        }

        skewZAxisM(transitionOM, 0, skewX, skewY);

        doRatate();
        return transitionOM;
    }

    protected void drawBefore() {
        if (frameIndex < frameCount + delayCount) {
            transitionX += offsetX;
            transitionY += offsetY;
            skewX += offsetSkewX;
            skewY += offsetSkewY;
            conor += offsetConor;
            customValue += offsetCus;
            transitionScale += offsetScale;
            bgConorOffset += bgConorOffset;
            transitonRange += offsetBlurRange;
            if (transitionXFrames != null) {
                transitionX = transitionXFrames[frameIndex];
            }
            if (transitionYFrames != null) {
                transitionY = transitionYFrames[frameIndex];
            }
            if (rotateFrames != null) {
                conor = rotateFrames[frameIndex];
            }
            if (scaleFrames != null) {
                transitionScale = scaleFrames[frameIndex];
            }
            if (customFrames != null) {
                customValue = customFrames[frameIndex];
            }
            if (customFrames2 != null) {
                customValue2 = customFrames2[frameIndex];
            }
            if (skewXFrames != null) {
                skewX = skewXFrames[frameIndex];
            }
            if (skewYFrames != null) {
                skewY = skewYFrames[frameIndex];
            }

            LogUtils.v("", "Index:" + frameIndex + ",Y:" + transitionY);
            //将频繁输出的日志设置为-verbose级别
//            LogUtils.v("", "transitionScale:" + transitionScale);
        }
    }


    @Override
    public float drawAlpha() {
        fade += offsetFade;
        fade = fade < 0 ? 0 : fade;
        fade = fade > 1 ? 1 : fade;
        return fade;
    }

    public float getBlurRange() {
        //LogUtils.v(getClass().getSimpleName() + "getBlurRange", "transitonRange:" + transitonRange + " frameindex:" +frameIndex);
        return transitonRange;
    }

    public int getBlurType() {
        if (blurType == null) {
            return ActionBlurType.NONE.getType();
        }
        return blurType.getType();
    }


    protected void doRatate() {
        if (rotationType == null) {
            Matrix.rotateM(transitionOM, 0, 0, 1f, 0f, 0f);
            return;
        }
        doBgConor();
        switch (rotationType) {
            case NONE:
                Matrix.rotateM(transitionOM, 0, 0, 1f, 0f, 0f);
            case X_ANXIS:
                Matrix.rotateM(transitionOM, 0, conor, 1f, 0f, 0f);
                break;
            case Y_ANXIS:
                Matrix.rotateM(transitionOM, 0, conor, 0f, 1f, 0f);
                break;
            case Z_ANXIS:
                rotateZview(1f);
                break;
            case X_ANXIS_RE:
                Matrix.rotateM(transitionOM, 0, conor, -1f, 0f, 0f);
                break;
            case Y_ANXIS_RE:
                Matrix.rotateM(transitionOM, 0, conor, 0f, -1f, 0f);
                break;
            case Z_ANXIS_RE:
                rotateZview(-1f);
                break;
            case XY_ANXIS:
                Matrix.rotateM(transitionOM, 0, conor, 1f, 1f, 0f);
                break;
            case XY_ANXIS_RE:
                Matrix.rotateM(transitionOM, 0, conor, -1f, 1f, 0f);
                break;
        }
    }

    private void rotateZview(float zDirect) {
//        LogUtils.v(getClass().getSimpleName(), "conor: " + conor);
        temp = MatrixUtils.getOriginalMatrix();
        Matrix.scaleM(temp, 0, height, width, 1);
        if (rotateCenterY != 0 || rotateCenterX != 0) {
            Matrix.translateM(transitionOM, 0, rotateCenterX, rotateCenterY, 0);
        }
        Matrix.multiplyMM(transitionOM, 0, transitionOM, 0, temp, 0);
        Matrix.rotateM(transitionOM, 0, conor, 0f, 0f, zDirect);
        Matrix.invertM(temp, 0, temp, 0);
        Matrix.multiplyMM(transitionOM, 0, transitionOM, 0, temp, 0);
        if (rotateCenterY != 0 || rotateCenterX != 0) {
            Matrix.translateM(transitionOM, 0, -rotateCenterX, -rotateCenterY, 0);
        }
    }



    /**
     * 如果背景是有转动的，那么就需要做两个的旋转，主旋转和背景旋转
     * 背景旋转是绕z轴进行旋转
     */
    private void doBgConor() {
        if (bgConor != 0) {
            temp = MatrixUtils.getOriginalMatrix();
            Matrix.scaleM(temp, 0, height, width, 1);
            Matrix.multiplyMM(transitionOM, 0, transitionOM, 0, temp, 0);
            Matrix.rotateM(transitionOM, 0, bgConor, 0f, 0f, 1f);
            Matrix.invertM(temp, 0, temp, 0);
            Matrix.multiplyMM(transitionOM, 0, transitionOM, 0, temp, 0);
        }
    }

    @Override
    public float[] prepare() {
        reset();
        transitionOM = MatrixUtils.getOriginalMatrix();
        Matrix.translateM(transitionOM, 0, transitionX, transitionY, Z_VIEW);
        doRatate();
        return transitionOM;
    }

    public void reset() {
        if (log) {
            LogUtils.w("BaseEvaluate draw" + hashCode(), "reset");
        }
        frameIndex = 0;
        transitionX = this.startX;
        transitionY = this.startY;
        skewX = startSkewX;
        skewY = startSkewY;
        fade = this.startFade;
        conor = this.startConor;
        transitionScale = this.startScale;
        transitionScale = transitionScale <= 0 ? 1f : transitionScale;
        transitonRange = this.startBlurRange;
        customValue = 0;
        skewX = startSkewX;
        skewY = startSkewY;
    }

    @Override
    public void seek(int duration) {

    }

    public boolean checkAlphaDraw() {
        return offsetFade != 0;
    }


    public int getFrameCount() {
        return frameCount;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public float getFrameValue() {
        return frameValue;
    }

    public float getTransitionX() {
        return transitionX;
    }

    public float getTransitionY() {
        return transitionY;
    }

    public void setTransitionX(float transitionX) {
        this.transitionX = transitionX;
    }

    public void setTransitionY(float transitionY) {
        this.transitionY = transitionY;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public float getConor() {
        return conor;
    }

    public AnimateInfo.ROTATION getRotationType() {
        return rotationType;
    }

    public float getTransitionScale() {
        return transitionScale;
    }

    public float getCustomValue() {
        return customValue;
    }

    public float getCustomValue2() {
        return customValue2;
    }

    /**
     * 控件模块存在于指定的小区域，进行旋转操作的确用的是整体的0，0坐标，所以需要转化
     *
     * @param isWidget
     * @param coordX
     * @param coordY
     */
    public void setIsSubAreaWidget(boolean isWidget, float coordX, float coordY) {
        this.isWidgetRotate = isWidget;
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public void setWidgetRotate(boolean isWidget, float coordX, float coordY) {
        this.isWidgetRotate = isWidget;
        this.coordX = coordX;
        this.coordY = coordY;
    }


    public void setZView(float zView) {
        this.Z_VIEW = zView;
    }


    /**
     * 克隆
     *
     * @return 新BaseEvalute
     * @see BaseEvaluate#BaseEvaluate()
     */
    @Override
    public BaseEvaluate clone() {
        BaseEvaluate baseEvaluate = new BaseEvaluate();
        baseEvaluate.transitionConor = transitionConor;
        baseEvaluate.rotationType = rotationType;
        baseEvaluate.duration = duration;
        baseEvaluate.frameCount = frameCount;


        baseEvaluate.transitionX = transitionX;
        baseEvaluate.offsetX = offsetX;
        baseEvaluate.startX = startX;
        baseEvaluate.transitionY = transitionY;
        baseEvaluate.startY = startY;
        baseEvaluate.offsetY = offsetY;


        baseEvaluate.offsetFade = offsetFade;
        baseEvaluate.offsetConor = offsetConor;


        baseEvaluate.onlyScaleX = onlyScaleX;
        baseEvaluate.onlyScaleY = onlyScaleY;


        baseEvaluate.fade = fade;
        baseEvaluate.conor = conor;
        baseEvaluate.bgConor = bgConor;
        baseEvaluate.bgConorOffset = bgConorOffset;
        baseEvaluate.endConor = endConor;
        baseEvaluate.width = width;
        baseEvaluate.height = height;
        baseEvaluate.Z_VIEW = Z_VIEW;
        baseEvaluate.transitionScale = this.transitionScale;
        baseEvaluate.transitonRange = this.transitonRange;
        baseEvaluate.offsetScale = offsetScale;
        baseEvaluate.delayCount = delayCount;
        baseEvaluate.skewY = skewY;
        baseEvaluate.skewX = skewX;
        baseEvaluate.offsetSkewY = offsetSkewY;
        baseEvaluate.offsetSkewX = offsetSkewX;
        baseEvaluate.startSkewY = startSkewY;
        baseEvaluate.startSkewX = startSkewX;

        baseEvaluate.transitionYFrames = transitionYFrames;
        baseEvaluate.transitionXFrames = transitionXFrames;
        baseEvaluate.scaleFrames = scaleFrames;
        baseEvaluate.rotateFrames = rotateFrames;
        baseEvaluate.skewXFrames = skewXFrames;
        baseEvaluate.skewYFrames = skewYFrames;
        baseEvaluate.customFrames = customFrames;
        baseEvaluate.customFrames2 = customFrames2;

        baseEvaluate.endSkewX = endSkewX;
        baseEvaluate.endSkewY = endSkewY;
        baseEvaluate.endX = endX;
        baseEvaluate.endY = endY;
        baseEvaluate.endScale = endScale;
        baseEvaluate.isSpringY = isSpringY;


        return baseEvaluate;
    }


    /**
     * 修复使控件正好在屏幕边缘之外
     * if满足，则修复进出场
     * if不满足，则修复自身缩放位置
     *
     * @param cube 长度为8的顶点数组
     */
    public void fixEnterOutAnimationStartEndAtScreenEdge(float[] cube) {
        boolean xFixed = false;
        boolean yFixed = false;
        //从右边进场
        if (startX == Float.MAX_VALUE) {
            if (0f == offsetScale) {
                startX = 1 - cube[0];
                LogUtils.v(getClass().getSimpleName(), "startX fixed. width:" + width);
            } else {
                //修复
                startX = (cube[0] + cube[4]) / 2;
            }
            xFixed = true;
        }
        //从左边进场
        if (startX == Float.MIN_VALUE) {
            if (0f == offsetScale) {
                startX = -1 - cube[4];
                LogUtils.v(getClass().getSimpleName(), "startX fixed. width:" + width);
            } else {
                startX = (cube[0] + cube[4]) / 2;
            }
            xFixed = true;
        }
        //从右边出场
        if (offsetX == Float.MAX_VALUE) {
            if (0f == offsetScale) {
                offsetX = 1 - cube[0];
                LogUtils.v(getClass().getSimpleName(), "endX fixed. width:" + width);
            } else {
                offsetX = (cube[0] + cube[4]) / 2;
            }
            xFixed = true;
        }
        //从左边出场
        if (offsetX == Float.MIN_VALUE) {
            if (0f == offsetScale) {
                offsetX = -1 - cube[4];
                LogUtils.v(getClass().getSimpleName(), "endX fixed. width:" + width);
            } else {
                offsetX = (cube[0] + cube[4]) / 2;
            }
            xFixed = true;
        }
        //从下边进场
        if (startY == Float.MAX_VALUE) {
            if (0f == offsetScale) {
                startY = 1 - cube[3];
                LogUtils.v(getClass().getSimpleName(), "startY fixed. height:" + height);
            } else {
                startY = (cube[1] + cube[3]) / 2;
            }
            yFixed = true;
        }
        if (startY == Float.MIN_VALUE) {
            if (0f == offsetScale) {
                startY = -1 - cube[1];
                LogUtils.v(getClass().getSimpleName(), "startY fixed. height:" + height);
            } else {
                startY = (cube[1] + cube[3]) / 2;
            }
            yFixed = true;
        }
        if (offsetY == Float.MAX_VALUE) {
            if (0f == offsetScale) {
                offsetY = 1 - cube[3];
                LogUtils.v(getClass().getSimpleName(), "endY fixed. height:" + height);
            } else {
                offsetY = (cube[1] + cube[3]) / 2;
            }
            yFixed = true;
        }
        if (offsetY == Float.MIN_VALUE) {
            if (0f == offsetScale) {
                offsetY = -1 - cube[1];
                LogUtils.v(getClass().getSimpleName(), "endY fixed. height:" + height);
            } else {
                offsetY = (cube[1] + cube[3]) / 2;
            }
            yFixed = true;
        }

        if (xFixed) {
//            LogUtils.v(getClass().getSimpleName(), hashCode() + " cube:" + Arrays.toString(cube));
//            LogUtils.v(getClass().getSimpleName(), hashCode() + " Xfixed, transitionX = " +this.startX + " endX: " + offsetX);
            if (0f == offsetScale) {
                //修复少绘制一帧的情况
                offsetX = (offsetX - startX) / (frameCount - 2);
                this.startX = transitionX = startX - offsetX;
            } else {
                //修复放大缩小定位
                offsetX = (offsetX - startX) / frameCount;
                transitionX = startX;
            }
        }
        if (yFixed) {
//            LogUtils.v(getClass().getSimpleName(), hashCode() + " cube:" + Arrays.toString(cube));
//            LogUtils.v(getClass().getSimpleName(), hashCode() + " Yfixed, transitionY = " +this.startY+ " endY " + offsetY);
            if (0f == offsetScale) {
                //修复少绘制一帧的情况
                offsetY = (offsetY - startY) / (frameCount - 2);
                this.startY = transitionY = startY - offsetY;
            } else {
                //修复放大缩小定位
                offsetY = (offsetY - startY) / frameCount;
                transitionY = startY;
            }
        }
    }

    /**
     * 拼接上个动画状态
     *
     * @param baseEvaluate
     */
    public void composeEvaluate(BaseEvaluate baseEvaluate) {
        if (adjective) {
            this.transitionX = baseEvaluate.getTransitionX();
            this.transitionY = baseEvaluate.getTransitionY();
            this.conor = baseEvaluate.getConor();
            this.transitionScale = baseEvaluate.getTransitionScale();
            this.rotateCenterX = baseEvaluate.rotateCenterX;
            this.rotateCenterY = baseEvaluate.rotateCenterY;
            if (baseEvaluate.getRotationType() != null) {
                this.rotationType = baseEvaluate.getRotationType();
            }
            LogUtils.v("BaseEvaluate", toString());
        }
    }


    public String getTransitions() {
        return "  startX:" + startX + "  startY:" + startY +
                "  transitionX:" + transitionX + "  transitionY:" + transitionY;
    }


    /**
     * 后期设置, 参数可为null， 若为null则表示不变
     *
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public void setCoordinatesLazy(Float startX, Float startY, Float endX, Float endY) {
        if (startX != null || endX != null) {
            offsetX = this.startX + frameCount * offsetX;
            if (startX != null) {
                this.startX = startX;
            }
            if (endX != null) {
                this.offsetX = endX;
            }
            offsetX = (offsetX - this.startY) / frameCount;
            transitionX = this.startX + offsetX * frameIndex;
        }
        if (startY != null || endY != null) {
            offsetY = this.startY + frameCount * offsetY;
            if (startY != null) {
                this.startY = startY;
            }
            if (endY != null) {
                this.offsetY = endY;
            }
            offsetY = (offsetY - this.startY) / frameCount;
            transitionY = this.startY + offsetY * frameIndex;
        }
    }

    /**
     * 临时操作矩阵
     * Temporary memory for operations that need temporary matrix data.
     */
    private final static float[] sTemp = new float[32];

    /**
     * 对矩阵进行错切变化
     * @param m 源矩阵
     * @param mOffset 矩阵起始下标
     * @param sitaX x错切角
     * @param sitaY y错切角
     */
    public static void skewZAxisM(float[] m, int mOffset, float sitaX, float sitaY) {
        setZAxisSkewM(sTemp, 0, sitaX, sitaY);
        Matrix.multiplyMM(sTemp, 16, m, mOffset, sTemp, 0);
        System.arraycopy(sTemp, 16, m, mOffset, 16);
        //LogUtils.v(BaseEvaluate.class.getSimpleName(), "zAxis skew matrix:\n" + MatrixUtils.matrixToString(m, 0));
    }

    /**
     * 设置矩阵为纯错切变化矩阵
     * @param m 源矩阵
     * @param mOffset 矩阵起始下标
     * @param sitaX x错切角
     * @param sitaY y错切角
     */
    public static void setZAxisSkewM(float[] m, int mOffset, float sitaX, float sitaY) {
        Arrays.fill(m, mOffset, mOffset + 16, 0);
        m[mOffset] = 1;
        m[mOffset + 5] = 1;
        m[mOffset + 10] = 1;
        m[mOffset + 15] = 1;
        sitaX = ( sitaX % 90 );
        sitaY = ( sitaY % 90 );
        sitaX *= (float) (Math.PI / 180.0f);
        sitaY *= (float) (Math.PI / 180.0f);
        float tanAx = (float) Math.tan(sitaX);
        float tanAy = (float) Math.tan(sitaY);
        m[mOffset + 1] = tanAy;
        m[mOffset + 4] = tanAx;
    }




    @Override
    public String toString() {
        return "BaseEvaluate{" +
                "Z_VIEW=" + Z_VIEW +
                ", transitionOM=" + Arrays.toString(transitionOM) +
                ", projectionMatrix=" + Arrays.toString(projectionMatrix) +
                ", offsetX=" + offsetX +
                ", offsetY=" + offsetY +
                ", offsetConor=" + offsetConor +
                ", offsetFade=" + offsetFade +
                ", offsetCus=" + offsetCus +
                ", transitionX=" + transitionX +
                ", transitionY=" + transitionY +
                ", transitionConor=" + transitionConor +
                ", transitonRange=" + transitonRange +
                ", conor=" + conor +
                ", bgConor=" + bgConor +
                ", bgConorOffset=" + bgConorOffset +
                ", endConor=" + endConor +
                ", fade=" + fade +
                ", temp=" + Arrays.toString(temp) +
                ", width=" + width +
                ", height=" + height +
                ", transitionScale=" + transitionScale +
                ", startScale=" + startScale +
                ", startX=" + startX +
                ", startY=" + startY +
                ", startConor=" + startConor +
                ", startFade=" + startFade +
                ", startBlurRange=" + startBlurRange +
                ", offsetScale=" + offsetScale +
                ", offsetBlurRange=" + offsetBlurRange +
                ", frameCount=" + frameCount +
                ", frameValue=" + frameValue +
                ", frameIndex=" + frameIndex +
                ", duration=" + duration +
                ", rotationType=" + rotationType +
                ", isNoZaxis=" + isNoZaxis +
                ", isWidgetRotate=" + isWidgetRotate +
                ", coordX=" + coordX +
                ", coordY=" + coordY +
                ", delayCount=" + delayCount +
                ", rotateCenterX=" + rotateCenterX +
                ", rotateCenterY=" + rotateCenterY +
                ", moveAction=" + moveAction +
                ", transitionXFrames=" + Arrays.toString(transitionXFrames) +
                ", transitionYFrames=" + Arrays.toString(transitionYFrames) +
                ", rotateFrames=" + Arrays.toString(rotateFrames) +
                ", scaleFrames=" + Arrays.toString(scaleFrames) +
                ", customValue=" + customValue +
                ", customFrames=" + Arrays.toString(customFrames) +
                ", blurType=" + blurType +
                ", onlyScaleX=" + onlyScaleX +
                ", onlyScaleY=" + onlyScaleY +
                ", adjective=" + adjective +
                '}';
    }


    public void setMoveAction(IMoveAction moveAction) {
        this.moveAction = moveAction;
        if (moveAction.isTranslate()) {
            transitionXFrames = moveAction.action(duration, startX, endX);
            transitionYFrames = moveAction.action(duration, startY, endY);
        }
        if (moveAction.isRotate()) {
            rotateFrames = moveAction.action(duration, startConor, endConor);
        }
        if (moveAction.isScale()) {
            scaleFrames = moveAction.action(duration, startScale, endScale);
        }
        if (moveAction.isSkew()) {
            skewXFrames = moveAction.action(duration, startSkewX, endSkewX);
            skewYFrames = moveAction.action(duration, startSkewY, endSkewY);
        }
        if (isSpringY) {
            transitionYFrames = moveAction.action(duration, startY, endY);
        }
    }

    public void setScaleAction(IMoveAction moveAction) {
        if (moveAction.isScale()) {
            scaleFrames = moveAction.action(duration, startScale, endScale);
        } else {
            scaleFrames = null;
        }
    }

    public void setRotateAction(IMoveAction moveAction) {
        if (moveAction.isRotate()) {
            rotateFrames = moveAction.action(duration, startConor, endConor);
        } else {
            rotateFrames = null;
        }
    }

    public void setSkewAction(IMoveAction moveAction) {
        if (moveAction.isSkew()) {
            skewXFrames = moveAction.action(duration, startSkewX, endSkewX);
            skewYFrames = moveAction.action(duration, startSkewY, endSkewY);
        } else {
            skewXFrames = null;
            skewYFrames = null;
        }
    }


    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public void setAdjective(boolean adjective) {
        this.adjective = adjective;
    }
}
