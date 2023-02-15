package com.ijoysoft.mediasdk.module.opengl.theme.action;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.EasyGlUtils;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.GaussianBlurBackgroundFilter;

/**
 * 模糊背景
 *
 * @TODO 先注释颜色入口，后续有可能放开背景定义
 */
public class BaseTimeBlurThemeExample extends BaseTimeThemeExample {
    //    private PureColorFilter pureColorFilter;
    private GaussianBlurBackgroundFilter widthBlurFilter;
    private GaussianBlurBackgroundFilter heightBlurFilter;
    // 是否为纯色背景
    private int blurLevel;//模糊度(1-20)
    /**
     * 创建缓存对象，离屏buffer
     */
    private int[] fFrame = new int[1];
    private int fTextureSize = 1;
    private int[] fTexture = new int[fTextureSize];


    private float offsetSigma;


    public BaseTimeBlurThemeExample() {
        super();
        blurLevel = 15;
        offsetSigma = 0.9f;
        widthBlurFilter =
                GaussianBlurBackgroundFilter.initWithBlurRadiusInPixels(blurLevel).setTexelWidthOffset(offsetSigma).setScale(true);
        heightBlurFilter =
                GaussianBlurBackgroundFilter.initWithBlurRadiusInPixels(blurLevel).setTexelHeightOffset(offsetSigma).setScale(true);
//        pureColorFilter = new PureColorFilter();
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


    @Override
    public void drawPrepare(MediaItem mediaItem) {
        super.drawPrepare(mediaItem);
        //根据mediaitem中的图形不一致进行等比例缩放
        if (isVideoRender) {
            widthBlurFilter.adjustImageScalingStretch(videoWidth, videoHeight,
                    bitmapAngle, width, height);
        } else {
            if (bitmap != null) {
                widthBlurFilter.adjustImageScalingStretch(bitmap.getWidth(), bitmap.getHeight(),
                        bitmapAngle, width, height);
            }
        }
        drawBackgrondPrepare();

    }

    /**
     * 这里一个很重要的点就是经过    setTextureId(fTexture[1]);之后是整个glview是一张纹理了
     */
    public void drawBackgrondPrepare() {
        LogUtils.v("BaseTimeBlurThemeExample", "drawBackgrondPrepare:originTexture:" + originTexture + ",fTexture[0]:" + fTexture[0]);
        if (originTexture != 0) {
            setTextureId(originTexture);
        }
        if (isVideoRender) {
            if (videoDataSourcePlay.getTextureId() == -1) {
                return;
            }
            EasyGlUtils.bindFrameTexture(videoFrame[0], videoFteture[0]);
            videoDataSourcePlay.draw();
            EasyGlUtils.unBindFrameBuffer();
            setTextureId(videoFteture[0]);
        }
        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
        widthBlurFilter.setTextureId(getTextureId());
        widthBlurFilter.draw();
        EasyGlUtils.unBindFrameBuffer();
        heightBlurFilter.setTextureId(fTexture[0]);
        heightBlurFilter.setRotation(bitmapAngle);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        widthBlurFilter.create();
        heightBlurFilter.create();
    }

    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
        createFBo(width, height);
        widthBlurFilter.onSizeChanged(width, height);
        heightBlurFilter.onSizeChanged(width, height);
//        if (bitmap != null) {
//            drawBackgrondPrepare();
//        }
//        pureColorFilter.setSize(width, height);

    }

    private void createFBo(int width, int height) {
        // 创建离屏渲染
        deleteFrameBuffer();
        GLES20.glGenFramebuffers(1, fFrame, 0);
        // 创建离屏纹理
        genTextures(width, height);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    // 生成Textures
    private void genTextures(int width, int height) {
        GLES20.glGenTextures(fTextureSize, fTexture, 0);
        if (fTexture[0] == -1) {
//            deleteFrameBuffer();
            GLES20.glGenTextures(fTextureSize, fTexture, 0);
        }
        for (int i = 0; i < fTextureSize; i++) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture[i]);
            // GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, fTexture[i]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE, null);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
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
        GLES20.glDeleteProgram(mProgram);
        GLES20.glDeleteTextures(1, new int[]{originTexture}, 0);
        originTexture = -1;
        if (widthBlurFilter != null) {
            widthBlurFilter.onDestroy();
        }
        if (heightBlurFilter != null) {
            heightBlurFilter.onDestroy();
        }
//        if (pureColorFilter != null) {
//            pureColorFilter.onDestroy();
//        }
        deleteFrameBuffer();
    }


    @Override
    public void draw() {
        drawBackground();
    }

    /**
     * 绘画背景图
     */
    public void drawBackground() {
        LogUtils.v("BaseTimeBlurThemeExample-drawBackground", "getteture:" + getTexture() + ",fTexture[0]:" + fTexture[0] + ",isVideoRender:" + isVideoRender);
        drawBlur();
        super.draw();
    }

    /**
     * 这是预渲染，就是在renderbus帧缓存之外的提前渲染
     *
     * @param videoTeture
     */
    @Override
    public void drawVideoFrame(int videoTeture) {
        if (isVideoRender) {
            videoDataSourcePlay.setTextureId(videoTeture);
        } else {
            LogUtils.v("BaseTimeBlurThemeExample", "originTexture:" + originTexture);
            setTextureId(originTexture);
        }
        drawBackgrondPrepare();
        GLES20.glViewport(0, 0, width, height);//mediadrawer的原因，导致这里背景的预渲染需要重置窗口
    }

    /**
     * 渲染高斯模糊
     */
    public void drawBlur() {
        heightBlurFilter.setTextureId(fTexture[0]);
        heightBlurFilter.setRotation(getBitmapAngle());
        heightBlurFilter.draw();
    }

}
