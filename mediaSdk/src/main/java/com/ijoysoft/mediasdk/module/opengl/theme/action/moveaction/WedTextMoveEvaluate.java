package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

import android.opengl.Matrix;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;

/**
 * 文字的出现，旋转有加速减速效果,需要单独计算
 */
public class WedTextMoveEvaluate extends BaseEvaluate {
    private float[] frames;


    public WedTextMoveEvaluate(AnimationBuilder builder) {
        super(builder);
        frames = evaluateAnimateCoord(duration, builder.startConor, builder.endConor - builder.startConor);
    }


    @Override
    public float[] draw() {
        transitionOM = MatrixUtils.getOriginalMatrix();
        projectionMatrix = MatrixUtils.getOriginalMatrix();
        transitionX += offsetX;
        transitionY += offsetY;
        transitionScale += offsetScale;
        Matrix.translateM(transitionOM, 0, 0, 0, Z_VIEW);
        Matrix.scaleM(transitionOM, 0, transitionScale, transitionScale, 1f);
        frameIndex = frameIndex > frames.length - 1 ? frames.length - 1 : frameIndex;
        frameValue = frames[frameIndex];
        doRatate(transitionOM, frameValue);
        Matrix.translateM(transitionOM, 0, -coordX, -coordY, 0);
        Matrix.translateM(projectionMatrix, 0, coordX, coordY, 0);
        Matrix.multiplyMM(transitionOM, 0, projectionMatrix, 0, transitionOM, 0);
        frameIndex++;
        return transitionOM;
    }

    protected float[] doRatate(float[] om, float value) {
        if (rotationType == null) {
            Matrix.rotateM(om, 0, 0, 1f, 0f, 0f);
            return om;
        }
        switch (rotationType) {
            case NONE:
                Matrix.rotateM(om, 0, 0, 1f, 0f, 0f);
            case X_ANXIS:
                Matrix.rotateM(om, 0, value, 1f, 0f, 0f);
                break;
            case Y_ANXIS:
                Matrix.rotateM(om, 0, value, 0f, 1f, 0f);
                break;
            case Z_ANXIS:
//                temp = MatrixUtils.getOriginalMatrix();
//                Matrix.scaleM(temp, 0, height, width, 1);
//                Matrix.multiplyMM(om, 0, om, 0, temp, 0);
                Matrix.rotateM(om, 0, value, 0, 0, 1);
//                Matrix.invertM(temp, 0, temp, 0);
//                Matrix.multiplyMM(om, 0, om, 0, temp, 0);
                break;
            case X_ANXIS_RE:
                Matrix.rotateM(om, 0, value, -1f, 0f, 0f);
                break;
            case Y_ANXIS_RE:
                Matrix.rotateM(om, 0, value, 0f, -1f, 0f);
                break;
            case Z_ANXIS_RE:
//                temp = MatrixUtils.getOriginalMatrix();
//                Matrix.scaleM(temp, 0, height, width, 1);
//                Matrix.multiplyMM(om, 0, om, 0, temp, 0);
                Matrix.rotateM(om, 0, value, 0, 0, -1f);
//                Matrix.invertM(temp, 0, temp, 0);
//                Matrix.multiplyMM(om, 0, om, 0, temp, 0);
                break;
            case XY_ANXIS:
                Matrix.rotateM(om, 0, value, 1f, 1f, 0f);
                break;
            case XY_ANXIS_RE:
                Matrix.rotateM(om, 0, value, -1f, 1f, 0f);
                break;
        }
        return om;
    }


    /**
     * 计算动画数组
     * <p>
     * x < 0.5 ? 8 * x * x * x * x : 1 - pow(-2 * x + 2, 4) / 2;
     *
     * @return
     */
    public float[] evaluateAnimateCoord(long duration, float start, float angle) {
        int framesNum = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] frames = new float[framesNum];
        float frameValue = 0;
        float x;
        /**
         * 根据x在[0-1]计算的值，目前跟start和end关系不是太大对于上下悬停来说
         */
        for (int i = 0; i < framesNum; i++) {
            x = i * 1.0F / (framesNum - 1);
            frameValue = x < 0.5f ? 8f * x * x * x * x : (float) (1 - Math.pow(-2 * x + 2, 4) / 2f);
            frames[i] = start + frameValue * angle;
        }
        return frames;
    }

    public void setRatateCenter(float x, float y) {
        this.coordX = x;
        this.coordY = y;
    }
}
