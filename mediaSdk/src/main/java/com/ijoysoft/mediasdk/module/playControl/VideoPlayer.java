package com.ijoysoft.mediasdk.module.playControl;

import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.EasyGlUtils;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaMatrix;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.entity.VideoMediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageBackgroundFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.VideoBackgroundFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.SlideGpuFilterGroup;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;

import javax.microedition.khronos.opengles.GL10;

/**
 * 视频播放，包含滤镜转场的纹理附加
 * 对于nofilter而已，重写了纹理的坐标值，使得其对原于纹理值进行垂直翻转，解决图像倒置的问题
 */
public class VideoPlayer implements IRender {
    private static final String TAG = "VideoPlayer";
    /**
     * 用于显示的变换矩阵
     */
    private SurfaceTexture surfaceTexture;
    /**
     * 视频源的背景
     */
    private VideoBackgroundFilter mVideoBackgrounFilter;

    private ImageBackgroundFilter mImageBackgroundFilter;
    /**
     * 旋转矩阵滤镜，撇除和前一帧的转场共同作用，这个矩阵滤镜只用到当前显示
     */
    // private AFilter mMatrixShow;

    /**
     * 滤镜组
     */
    private SlideGpuFilterGroup mAfilterGroup;
    /**
     * 创建离屏buffer
     */
    // 因为视频可能会用到前置的transitionfilter，所以把离屏通道设为2
    private int fTextureSize = 3;
    private int[] fFrame = new int[1];
    private int[] fTexture = new int[fTextureSize];
    /**
     * 用于视频旋转的参数
     */
    private int rotation;
    private int[] videoTexure = new int[1];

    private int currentVideoDuration;

    private VideoMediaItem videoMediaItem;
    private boolean isTransitionShow = true;
    private boolean isPreviewTransition;
    private int viewWidth;
    private int viewHeight;
    // opengl状态机的模型，所以纹理的值就一个int类型
    private int outputTexture;
    private boolean isResume;

    public VideoPlayer() {
        mVideoBackgrounFilter = new VideoBackgroundFilter();
        mAfilterGroup = new SlideGpuFilterGroup();
        // mMatrixShow = new OrientationNoFilter();
    }

    private void createVideoTexure() {
        GLES20.glGenTextures(1, videoTexure, 0);
        videoTexure = new int[1];
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, videoTexure[0]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, ConstantMediaSize.currentScreenWidth,
                ConstantMediaSize.currentScreenHeight, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        surfaceTexture = new SurfaceTexture(videoTexure[0]);
    }

    @Override
    public void onSurfaceCreated() {
        createVideoTexure();
        mVideoBackgrounFilter.create();
        mVideoBackgrounFilter.setTextureId(videoTexure[0]);
        mAfilterGroup.init();
        // mMatrixShow.create();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int viewWidth, int viewHeight) {
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        deleteFrameBuffer();
        GLES20.glGenFramebuffers(1, fFrame, 0);
        EasyGlUtils.genTexturesWithParameter(fTextureSize, fTexture, 0, GLES20.GL_RGBA, viewWidth, viewHeight);
        // fTexture = FBOManager.getInstance().fboTransactionNoRecyle(fTextureSize);
        // mMatrixShow.onSizeChanged(viewWidth, viewHeight);
        mAfilterGroup.onSizeChanged(viewWidth, viewHeight);
        // if (!isResume) {
        mVideoBackgrounFilter.onSizeChanged(viewWidth, viewHeight);
        // isResume = true;
        // }
    }

    private void deleteFrameBuffer() {
        GLES20.glDeleteFramebuffers(1, fFrame, 0);
        GLES20.glDeleteTextures(fTextureSize, fTexture, 0);
        for (int i = 0; i < fTextureSize; i++) {
            fTexture[i] = -1;
        }
    }

    @Override
    public void onDrawFrame() {
        surfaceTexture.updateTexImage();
        if (isPreviewTransition && currentVideoDuration > ConstantMediaSize.TRANSITION_DURATION) {
            isPreviewTransition = false;
            if (videoDrawerCallback != null) {
                videoDrawerCallback.onPause();
            }
            return;
        }
        mVideoBackgrounFilter.setTextureId(videoTexure[0]);
        if (videoMediaItem == null) {
            return;
        }
        if ((videoMediaItem.getTransitionFilter() != null
                && currentVideoDuration < ConstantMediaSize.TRANSITION_DURATION && isTransitionShow
                && videoMediaItem.getDuration() >= ConstantMediaSize.TRANSITION_DURATION)
                || (videoMediaItem.getTransitionFilter() != null && isPreviewTransition)) {
            mVideoBackgrounFilter.drawBackground();
            mAfilterGroup.onDrawFrame(mVideoBackgrounFilter.getOutputTexture());
            videoMediaItem.getTransitionFilter().setTextureId(mAfilterGroup.getOutputTexture());
            EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
            videoMediaItem.getTransitionFilter().draw();
            EasyGlUtils.unBindFrameBuffer();
            outputTexture = fTexture[0];
        } else {
            mVideoBackgrounFilter.drawBackground();
            mAfilterGroup.onDrawFrame(mVideoBackgrounFilter.getOutputTexture());
            outputTexture = mAfilterGroup.getOutputTexture();
        }
    }

    /**
     * 对视频的转场做准备工作，转场前赋值滤镜
     * playPrepare
     */
    public void playPrepare(MediaItem preMediaItem) {
        MediaMatrix mediaMatrix = preMediaItem == null ? null : preMediaItem.getMediaMatrix();
        // 黑色背景
        GLES20.glViewport(0, 0, ConstantMediaSize.currentScreenWidth, ConstantMediaSize.currentScreenHeight);
        GLES20.glClearColor(0.114f, 0.114f, 0.114f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // 显示区背景
        GLES20.glViewport(0, 0, ConstantMediaSize.showViewWidth, ConstantMediaSize.showViewHeight);
        // 转场需要的前一帧
        if (videoMediaItem.getTransitionFilter() != null) {
            if (mImageBackgroundFilter == null) {
                mImageBackgroundFilter = new ImageBackgroundFilter();
                mImageBackgroundFilter.create();
            }

            // int cornor = preMediaItem.getRotation();
            int cornor = 0;
            if (preMediaItem != null && preMediaItem.getMediaMatrix() != null) {
                cornor = (cornor + preMediaItem.getMediaMatrix().getAngle() + 360) % 360;
            }
            // 当前mediaitem做旋转
            if (videoMediaItem.getMediaMatrix() != null) {
                cornor = (cornor - videoMediaItem.getMediaMatrix().getAngle() + 360) % 360;
            }
            mImageBackgroundFilter.setBitmapPath(videoMediaItem.getPrepareFramePath(), cornor);
            mImageBackgroundFilter.initTexture();
            Log.i("test", "videoplayer->playPrepare->mImageBackgroundFilter.initTexture():" + mImageBackgroundFilter.getTextureId());

            mImageBackgroundFilter.setMaxtrix(mediaMatrix);
            float[] matrix = mImageBackgroundFilter.getMatrix();
            mImageBackgroundFilter.onSizeChanged(viewWidth, viewHeight);
            mImageBackgroundFilter.drawBackgrondPrepare();
            mImageBackgroundFilter.setMatrix(getScaleTranlation(matrix, mediaMatrix));
            EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[1]);
            mImageBackgroundFilter.drawBackground();
            EasyGlUtils.unBindFrameBuffer();
            if (preMediaItem != null) {
                mAfilterGroup.changeFilter(preMediaItem.getAfilter());
            }
            EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[2]);
            mAfilterGroup.onDrawOnlyFrame(fTexture[1]);
            EasyGlUtils.unBindFrameBuffer();
            videoMediaItem.getTransitionFilter().create();
            videoMediaItem.getTransitionFilter().onSizeChanged(viewWidth, viewHeight);
            videoMediaItem.getTransitionFilter().setPreviewTextureId(fTexture[2]);
            // mMatrixShow.setMatrix(MatrixUtils.getOriginalMatrix());
            if (videoMediaItem.getAfilter() != null) {
                mAfilterGroup.changeFilter(videoMediaItem.getAfilter());
            }
        }
    }

    public SurfaceTexture getSurfaceTexture() {
        return surfaceTexture;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
        if (mVideoBackgrounFilter != null) {
            mVideoBackgrounFilter.setRotation(this.rotation);
        }
    }

    public void changeFilter(MagicFilterType type) {
        mAfilterGroup.changeFilter(type);
    }

    public void changeFilter(GPUImageFilter gpuImageFilter) {
        mAfilterGroup.changeFilter(gpuImageFilter);
    }

    /**
     * 是否纯色,视频的别净土的渲染放在videodrawer去操作，
     *
     * @param flag
     */
    public void setPureColor(boolean flag, int[] rgba) {
        mVideoBackgrounFilter.setIsPureColor(flag, rgba);
    }

    /**
     * 给背景显示设置当前显示尺寸
     */
    public void setVideoMediaItem(MediaItem mediaItem) {
        mVideoBackgrounFilter.setVideoMediaItem(mediaItem);
    }

    /**
     * 给背景显示设置当前显示尺寸
     */
    public void setVideoFrameRatioUpdate(MediaItem mediaItem) {
        videoMediaItem = (VideoMediaItem) mediaItem;
        mVideoBackgrounFilter.setMaxtrix(mediaItem);
        mVideoBackgrounFilter.setVideoFrameRatioUpdate(mediaItem);
        float[] matrix = mVideoBackgrounFilter.getMatrix();
        mVideoBackgrounFilter.setMatrix(getScaleTranlation(matrix, mediaItem.getMediaMatrix()));
    }

    /**
     * 设置旋转角度
     *
     * @param angle
     */
    public void setRotate(int angle, RatioType ratioType) {
        if (ratioType != RatioType._1_1) {
            mVideoBackgrounFilter.doRotaion(angle);
        }
        // MatrixUtils.rotate(mediaItemOM, -angle);
        // mShow.setMatrix(mediaItemOM);
    }

    /**
     * 当预览元素片段的时候，直接显示第一帧图
     *
     * @param transitionShow
     */
    public void setTransitionShow(boolean transitionShow) {
        isTransitionShow = transitionShow;
    }

    /**
     * 是否是预览转场效果,如果是预览转场效果，转场播放结束后设置isplaying为false
     */
    public void isPreviewTransition(boolean isPreviewTransition) {
        this.isPreviewTransition = isPreviewTransition;
    }

    public interface VideoDrawerCallback {
        void onPause();
    }

    private VideoDrawerCallback videoDrawerCallback;

    public void setVideoDrawerCallback(VideoDrawerCallback videoDrawerCallback) {
        this.videoDrawerCallback = videoDrawerCallback;
    }

    public void onDestroy() {
        if (mVideoBackgrounFilter != null) {
            mVideoBackgrounFilter.onDestroy();
        }
        if (mImageBackgroundFilter != null) {
            mImageBackgroundFilter.onDestroy();

        }
        if (mAfilterGroup != null) {
            mAfilterGroup.onDestroy();
        }
        deleteFrameBuffer();
    }

    public void setCurrentVideoDuration(int currentVideoDuration) {
        this.currentVideoDuration = currentVideoDuration;
    }

    public void setVideoMedia(VideoMediaItem media) {
        this.videoMediaItem = media;
    }

    /**
     * 对media进行缩放，平移操作
     */
    public float[] getScaleTranlation(float[] matrix, MediaMatrix mediaMatrix) {
        if (mediaMatrix == null) {
            return matrix;
        }
        if (mediaMatrix.getScale() != 0 || mediaMatrix.getOffsetX() != 0 || mediaMatrix.getOffsetY() != 0) {
            float scale = mediaMatrix.getScale();
            int x = mediaMatrix.getOffsetX();
            int y = mediaMatrix.getOffsetY();
            MatrixUtils.scale(matrix, scale, scale);
            float tranx = (float) x / (float) ConstantMediaSize.showViewWidth;
            float trany = (float) y / (float) ConstantMediaSize.showViewHeight;
            LogUtils.i(TAG, "x:" + x + ",y:" + y + ",scale:" + scale + ",tranx:" + tranx + "," + trany);
            Matrix.translateM(matrix, 0, tranx, trany, 0f);
        }
        return matrix;
    }

    public void onVideoChanged(MediaItem info) {
        setRotation(info.getRotation());
    }

    public int getOutputTexture() {
        return outputTexture;
    }
}
