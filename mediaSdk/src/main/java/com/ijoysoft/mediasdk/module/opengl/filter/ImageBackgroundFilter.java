package com.ijoysoft.mediasdk.module.opengl.filter;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.EasyGlUtils;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.entity.MediaMatrix;

/**
 * 图片层的背景渲染，逻辑从imageplayer中抽出
 * 目的是把背景封装，暴露纹理接口，即外部调用的时候只需传入纹理id便可
 */
public class ImageBackgroundFilter extends ImageOriginFilter {
    // private BackgroudFilter backgroudFilter;
    private PureColorFilter pureColorFilter;
    private GaussianBlurBackgroundFilter widthBlurFilter;
    private GaussianBlurBackgroundFilter heightBlurFilter;
    // 是否为纯色背景
    private boolean isPureBg;
    float[] rgba = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
    private float[] mediaItemOM;
    /**
     * 创建缓存对象，离屏buffer
     */
    private int[] fFrame = new int[1];
    private int fTextureSize = 1;
    private int[] fTexture = new int[fTextureSize];

    public ImageBackgroundFilter() {
        super();
        // backgroudFilter = new BackgroudFilter();
        widthBlurFilter =
                GaussianBlurBackgroundFilter.initWithBlurRadiusInPixels(8).setTexelWidthOffset(2).setScale(false);
        heightBlurFilter =
                GaussianBlurBackgroundFilter.initWithBlurRadiusInPixels(8).setTexelHeightOffset(2).setScale(false);
        pureColorFilter = new PureColorFilter();
    }

    /**
     * 处理旋转角度
     *
     * @param rotation
     */
    @Override
    public void setRotation(int rotation) {
        super.setRotation(rotation);
        // backgroudFilter.setRotation(rotation);
    }

    @Override
    public void doRotaion(int r) {
        super.doRotaion(r);
    }

    /**
     * 这里只负责做旋转的动作，包括旋转后的顶点动态变化，以及纹理的旋转
     * 缩放和平移放在videodrawer的matrixshow去管理，记得先旋转后缩放
     * @param mediaMatrix
     */
    public void setMaxtrix(MediaMatrix mediaMatrix) {
        mediaItemOM = MatrixUtils.getOriginalMatrix();
        setMatrix(mediaItemOM);
        heightBlurFilter.setMatrix(mediaItemOM);
        doRotaion(0);
        if (mediaMatrix != null) {
            doRotaion(mediaMatrix.getAngle());
            MatrixUtils.rotate(mediaItemOM, mediaMatrix.getAngle());
            setMatrix(mediaItemOM);
            heightBlurFilter.setMatrix(mediaItemOM);
        }
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        // backgroudFilter.create();
        widthBlurFilter.create();
        heightBlurFilter.create();
        pureColorFilter.create();
    }

    @Override
    public void onSizeChanged(int width, int height) {
        createFBo(width, height);
        super.onSizeChanged(width, height);
        // backgroudFilter.setSize(width, height);
        widthBlurFilter.onSizeChanged(width, height);
        heightBlurFilter.onSizeChanged(width, height);
        pureColorFilter.setSize(width, height);
    }

    private void createFBo(int width, int height) {
        // 创建离屏渲染
//        deleteFrameBuffer();
        GLES20.glGenFramebuffers(1, fFrame, 0);
        // 创建离屏纹理
        genTextures(width, height);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    // 生成Textures
    private void genTextures(int width, int height) {
        GLES20.glGenTextures(fTextureSize, fTexture, 0);
        for (int i = 0; i < fTextureSize; i++) {
             GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture[i]);
//            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, fTexture[i]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE, null);
             GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
             GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
             GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
             GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
//            GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
//            GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
//            GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
//            GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        }
    }

    public void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            LogUtils.e(TAG, op + ": glError " + error);
//            throw new RuntimeException(op + ": glError " + error);
        }
    }

    private void deleteFrameBuffer() {
        GLES20.glDeleteFramebuffers(1, fFrame, 0);
        GLES20.glDeleteTextures(fTextureSize, fTexture, 0);
        for (int i = 0; i < fTextureSize; i++) {
            fTexture[i] = -1;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (widthBlurFilter != null) {
            widthBlurFilter.onDestroy();
        }
        if (heightBlurFilter != null) {
            heightBlurFilter.onDestroy();
        }
        if (pureColorFilter != null) {
            pureColorFilter.onDestroy();
        }
        deleteFrameBuffer();
    }

    public void setIsPureColor(boolean isPureColor, int[] value) {
        // if (backgroudFilter != null && isPureColor) {
        // backgroudFilter.setTextureId(0);
        // }
        this.isPureBg = isPureColor;
        rgba[0] = ((float) value[0] / 255f);
        rgba[1] = ((float) value[1] / 255f);
        rgba[2] = ((float) value[2] / 255f);
        rgba[3] = ((float) value[3] / 255f);
        pureColorFilter.setIsPureColor(rgba);
    }

    public void drawBackgrondPrepare() {
        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
        widthBlurFilter.setTextureId(getTextureId());
        widthBlurFilter.draw();
        EasyGlUtils.unBindFrameBuffer();
    }

    /**
     * 绘画背景图
     */
    public void drawBackground() {
        if (isPureBg) {
            pureColorFilter.draw();
        } else {
            heightBlurFilter.setTextureId(fTexture[0]);
            heightBlurFilter.setRotation(getBitmapAngle());
            heightBlurFilter.draw();
        }
        draw();
    }

}
