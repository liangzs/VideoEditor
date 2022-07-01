package com.ijoysoft.mediasdk.module.opengl.filter;

import android.opengl.GLES20;
import android.util.Log;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.EasyGlUtils;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;

/**
 * 处理视频播放的背景问题，其本身的的textureid是视频帧纹理id
 * 经过fbo处理之后，开放textureid过去
 * 原先视频纹理是继承OesFilter的samplerExternalOES vTexture，现兼容sampler2D vTexture
 */

public class VideoBackgroundFilter extends OesFilter {
    private static final String TAG = "VideoBackgroundFilter";
    public static final int ROT_0 = 0;
    public static final int ROT_90 = 90;
    public static final int ROT_180 = 180;
    public static final int ROT_270 = 270;
    // private BackgroudFilter backgroudFilter;
    private GaussianBlurBackgroundFilter widthBlurFilter;
    private GaussianBlurBackgroundFilter heightBlurFilter;
    // FBO
    private int[] fFrame = new int[1];
    private int[] fTexture = new int[2];
    private boolean isPureBg;
    private AFilter pureColorFilter;
    private float[] rgba = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
    private MediaItem mediaItem;
    private int fTextureSize = 2;
    private float[] cube;
    private boolean isDoRotate;
    private int matrixAngle;
    private float[] mediaItemOM;

    public VideoBackgroundFilter() {
        super();
        // backgroudFilter = new BackgroudFilter();
        widthBlurFilter =
                GaussianBlurBackgroundFilter.initWithBlurRadiusInPixels(8).setTexelWidthOffset(2).setScale(false);
        heightBlurFilter =
                GaussianBlurBackgroundFilter.initWithBlurRadiusInPixels(8).setTexelHeightOffset(2).setScale(false);
        pureColorFilter = new AFilter() {
            @Override
            protected void onCreate() {

            }

            @Override
            public void onSizeChanged(int width, int height) {

            }

            @Override
            protected void onClear() {
                pureColorDraw();
            }
        };
    }

    /**
     * 绘传入的颜色背景
     */
    private void pureColorDraw() {
        GLES20.glClearColor(rgba[0], rgba[1], rgba[2], rgba[3]);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * 图片旋转时，出现拉伸情况
     *
     * @param r
     */
    public void doRotaion(int r) {
        matrixAngle = r;
        isDoRotate = false;
        if (r == 90 || r == 270) {
            isDoRotate = true;
        }
    }

    /**
     * 旋转视频操作
     *
     * @param rotation
     */
    public void setRotation(int rotation) {
        float[] coord;
        switch (rotation) {
        case ROT_0:
            // coord = new float[]{
            // 0.0f, 0.0f,
            // 0.0f, 1.0f,
            // 1.0f, 0.0f,
            // 1.0f, 1.0f,
            // };
            coord = new float[] { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, };

            break;
        case ROT_90:// 根据内容的旋转，然后再图纸作画，再反转角度重现得到坐标便可！
            // coord = new float[]{
            // 1.0f, 1.0f,
            // 0.0f, 1.0f,
            // 0.0f, 0.0f,
            // 1.0f, 0.0f
            // };
            coord = new float[] { 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f };

            break;
        case ROT_180:
            // coord = new float[]{
            // 1.0f, 1.0f,
            // 1.0f, 0.0f,
            // 0.0f, 1.0f,
            // 0.0f, 0.0f,
            // };
            coord = new float[] { 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, };
            break;
        case ROT_270:
            coord = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, };

            break;
        default:
            return;
        }
        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        pureColorFilter.create();
        // backgroudFilter.create();
        widthBlurFilter.create();
        heightBlurFilter.create();
    }

    @Override
    public void onSizeChanged(int width, int height) {
        pureColorFilter.setSize(width, height);
        // backgroudFilter.setSize(width, height);
        widthBlurFilter.onSizeChanged(width, height);
        heightBlurFilter.onSizeChanged(width, height);
        createFBo(width, height);
        adjustVideoScaling();
    }

    private void createFBo(int width, int height) {
        // 创建离屏渲染
        deleteFrameBuffer();
        GLES20.glGenFramebuffers(1, fFrame, 0);
        // 创建离屏纹理
        genTextures(width, height);
        // fTexture = FBOManager.getInstance().fboTransactionNoRecyle(fTextureSize);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    // 生成Textures
    private void genTextures(int width, int height) {
        GLES20.glGenTextures(fTextureSize, fTexture, 0);
        Log.i("test","VideoBackgroud->genTextures:"+fTexture[0]+","+fTexture[1]);

        for (int i = 0; i < fTextureSize; i++) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture[i]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE, null);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        }
    }

    private void deleteFrameBuffer() {
        GLES20.glDeleteFramebuffers(1, fFrame, 0);
        GLES20.glDeleteTextures(fTextureSize, fTexture, 0);
        for (int i = 0; i < fTextureSize; i++) {
            fTexture[i] = -1;
        }
    }

    public void setIsPureColor(boolean isPureColor, int[] value) {
        this.isPureBg = isPureColor;
        rgba[0] = ((float) value[0] / 255f);
        rgba[1] = ((float) value[1] / 255f);
        rgba[2] = ((float) value[2] / 255f);
        rgba[3] = ((float) value[3] / 255f);
    }

    /**
     * 显示video的比例
     * MatrixUtils.rotate(SM, 90);和调顶点位置相通
     */
    private void adjustVideoScaling() {
        float outputWidth = ConstantMediaSize.showViewWidth;
        float outputHeight = ConstantMediaSize.showViewHeight;
        if (mediaItem != null) {
            int framewidth = mediaItem.getWidth();
            int frameheight = mediaItem.getHeight();
            LogUtils.d(TAG, "matrixAngle:" + matrixAngle + ",mediaItem.getRotation():" + mediaItem.getRotation());
            int rotation = (matrixAngle + mediaItem.getRotation()) % 360;
            if ((rotation == 90 || rotation == 270)) {
                framewidth = mediaItem.getHeight();
                frameheight = mediaItem.getWidth();
            }
            float ratio1 = outputWidth / framewidth;
            float ratio2 = outputHeight / frameheight;
            float ratioMax = Math.min(ratio1, ratio2);
            // 居中后图片显示的大小
            float imageWidthNew = Math.round(framewidth * ratioMax);
            float imageHeightNew = Math.round(frameheight * ratioMax);

            // 图片被拉伸的比例,以及显示比例时的偏移量
            // float ratioWidth = outputWidth / imageWidthNew;
            // float ratioHeight = outputHeight / imageHeightNew;
            float ratioWidth = outputWidth / imageWidthNew;
            float ratioHeight = outputHeight / imageHeightNew;
            LogUtils.i(TAG,
                    "outputWidth:" + outputWidth + ",outputHeight:" + outputHeight + ",imageWidthNew:" + imageWidthNew
                            + ",imageHeightNew:" + imageHeightNew + ",framewidth:" + framewidth + ",frameheight:"
                            + frameheight);
            LogUtils.i(TAG, "isDoRotate:" + isDoRotate + ",ratioWidth:" + ratioWidth + ",ratioHeight:" + ratioHeight);
            if (isDoRotate) {// 这个回路好难理解啊，根据结果推到没有旋转前的状态
                if (imageWidthNew > imageHeightNew) {
                    ratioWidth = ratioHeight;
                    ratioHeight = 1;
                    LogUtils.i(TAG, "ratioWidth > ratioHeight---tisDoRotate:" + isDoRotate + ",ratioWidth:" + ratioWidth
                            + ",ratioHeight:" + ratioHeight);
                } else {
                    ratioHeight = ratioWidth;
                    ratioWidth = 1;
                    LogUtils.i(TAG, "ratioWidth < ratioHeigh----tisDoRotate:" + isDoRotate + ",ratioWidth:" + ratioWidth
                            + ",ratioHeight:" + ratioHeight);
                }
            }
            // 根据拉伸比例还原顶点
            cube = new float[] { pos[0] / ratioWidth, pos[1] / ratioHeight, pos[2] / ratioWidth, pos[3] / ratioHeight,
                    pos[4] / ratioWidth, pos[5] / ratioHeight, pos[6] / ratioWidth, pos[7] / ratioHeight, };
            mVerBuffer.clear();
            mVerBuffer.put(cube).position(0);
        }
    }

    /**
     * 这里只负责做旋转的动作，包括旋转后的顶点动态变化，以及纹理的旋转
     * 缩放和平移放在videodrawer的matrixshow去管理，记得先旋转后缩放
     * @param currentMediaItem
     */
    public void setMaxtrix(MediaItem currentMediaItem) {
        mediaItemOM = MatrixUtils.getOriginalMatrix();
        setMatrix(mediaItemOM);
        heightBlurFilter.setMatrix(mediaItemOM);
        doRotaion(0);
        if (currentMediaItem.getMediaMatrix() != null) {
            doRotaion(currentMediaItem.getMediaMatrix().getAngle());
            MatrixUtils.rotate(mediaItemOM, currentMediaItem.getMediaMatrix().getAngle());
            setMatrix(mediaItemOM);
            heightBlurFilter.setMatrix(mediaItemOM);
        }
    }

    /**
     * 绘画背景，对外提供接口，因为视频是GL_TEXTURE_EXTERNAL_OES，需转化
     * GL_TEXTURE_EXTERNAL_OES 转GL_TEXTURE_2D
     */
    public void drawBackground() {
        // 黑色背景
        GLES20.glViewport(0, 0, ConstantMediaSize.currentScreenWidth, ConstantMediaSize.currentScreenHeight);
        GLES20.glClearColor(0.114f, 0.114f, 0.114f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // 显示区背景
        GLES20.glViewport(0, 0, ConstantMediaSize.showViewWidth, ConstantMediaSize.showViewHeight);
        if (!isPureBg) {
            EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
            mVerBuffer.clear();
            mVerBuffer.put(pos).position(0);
            draw();// GL_TEXTURE_EXTERNAL_OES 转GL_TEXTURE_2D
            EasyGlUtils.unBindFrameBuffer();
            // 模糊width方向
            widthBlurFilter.setTextureId(fTexture[0]);
            EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[1]);
            widthBlurFilter.draw();
            EasyGlUtils.unBindFrameBuffer();
            // 模糊height方向
            heightBlurFilter.setTextureId(fTexture[1]);
            EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
            heightBlurFilter.draw();
            // backgroudFilter.setTextureId(fTexture[0]);
            // backgroudFilter.draw();
            mVerBuffer.clear();
            mVerBuffer.put(cube).position(0);
            draw();// 绘画视频的帧
            EasyGlUtils.unBindFrameBuffer();
        } else {
            EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
            pureColorFilter.draw();
            draw();// 绘画视频的帧
            EasyGlUtils.unBindFrameBuffer();
        }
    }

    /**
     * 获取fbo的渲染纹理
     *
     * @return
     */
    public int getOutputTexture() {
        // if (!isPureBg) {
        // return fTexture[1];
        // }
        return fTexture[0];
    }

    /**
     * 视频帧的显示
     */
    public void setVideoMediaItem(MediaItem mediaItem) {
        this.mediaItem = mediaItem;
    }

    /**
     * 视频帧的显示及更新
     */
    public void setVideoFrameRatioUpdate(MediaItem mediaItem) {
        this.mediaItem = mediaItem;
        adjustVideoScaling();
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

}
