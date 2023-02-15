package com.ijoysoft.mediasdk.module.playControl;

import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.entity.BGInfo;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaType;
import com.ijoysoft.mediasdk.module.entity.VideoMediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.VideoBackgroundFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.SlideGpuFilterGroup;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

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

//    private ImageBackgroundFilter mImageBackgroundFilter;
    /**
     * 旋转矩阵滤镜，撇除和前一帧的转场共同作用，这个矩阵滤镜只用到当前显示
     */
    // private AFilter mMatrixShow;

    /**
     * 滤镜组
     */
    private SlideGpuFilterGroup mAfilterGroup;
    /**
     * 用于视频旋转的参数
     */
    private int rotation;
    private int[] videoTexure = new int[1];

    private int currentVideoDuration;

    private VideoMediaItem videoMediaItem;
    private boolean isPreviewTransition;
    // opengl状态机的模型，所以纹理的值就一个int类型
    private int outputTexture;

    public VideoPlayer() {
        mVideoBackgrounFilter = new VideoBackgroundFilter();
        mAfilterGroup = new SlideGpuFilterGroup();
        // mMatrixShow = new OrientationNoFilter();
    }

    private void createVideoTexure() {
        GLES20.glGenTextures(1, videoTexure, 0);
        videoTexure = new int[1];
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, videoTexure[0]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, 256, 2, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        surfaceTexture = new SurfaceTexture(videoTexure[0]);
    }

    private void deleteFrameBuffer() {
        GLES20.glDeleteTextures(1, videoTexure, 0);
    }

    /**
     * 重置视频split播放
     */
    public void videoRest() {
        deleteFrameBuffer();
        createVideoTexure();
        mVideoBackgrounFilter.setTextureId(videoTexure[0]);
    }

    @Override
    public void onSurfaceCreated() {
        createVideoTexure();
        mVideoBackgrounFilter.create();
        mVideoBackgrounFilter.setTextureId(videoTexure[0]);
        mAfilterGroup.init();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int viewWidth, int viewHeight, int screenWidth, int screenHeight) {
        mAfilterGroup.onSizeChanged(viewWidth, viewHeight);
        mVideoBackgrounFilter.onSurfaceChanged(offsetX, offsetY, viewWidth, viewHeight, screenWidth, screenHeight);
    }


    @Override
    public void onDrawFrame() {
    }

    @Override
    public ActionStatus onDrawFrameStatus() {
        try {
            surfaceTexture.updateTexImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isPreviewTransition && currentVideoDuration > (videoMediaItem.getPagDuration() != 0 ? (int) videoMediaItem.getPagDuration() : ConstantMediaSize.TRANSITION_DURATION)) {
            isPreviewTransition = false;
            if (transitionCallback != null) {
                transitionCallback.onPause();
            }
            return ActionStatus.STAY;
        }
        mVideoBackgrounFilter.setTextureId(videoTexure[0]);
        if (videoMediaItem == null) {
            return ActionStatus.STAY;
        }
        if ((videoMediaItem.getTransitionFilter() != null &&
                currentVideoDuration < ConstantMediaSize.TRANSITION_DURATION || isPreviewTransition)) {
            mVideoBackgrounFilter.drawBackground();
            mAfilterGroup.onDrawFrame(mVideoBackgrounFilter.getOutputTexture());
            outputTexture = mAfilterGroup.getOutputTexture();
            return ActionStatus.ENTER;
        }

        mVideoBackgrounFilter.drawBackground();
        mAfilterGroup.onDrawFrame(mVideoBackgrounFilter.getOutputTexture());
        outputTexture = mAfilterGroup.getOutputTexture();
        return ActionStatus.STAY;
    }

    public void setFilterStrength(float progress) {
        if (mAfilterGroup != null) {
            mAfilterGroup.setStrenth(progress);
        }
    }


    private boolean checkTransitionExist() {
        if (videoMediaItem.getTransitionFilter() == null ||
                videoMediaItem.getTransitionFilter().getTransitionType() == TransitionType.NONE) {
            return false;
        }
        return true;
    }

    /**
     * 对视频的转场做准备工作，转场前赋值滤镜
     * 这里假设有转场，转场上帧从当前缓存获取
     * playPrepare
     */
    public void playPrepare() {
        if (!checkTransitionExist()) {
            return;
        }
        currentVideoDuration = 0;
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

    public void changeFilter(GPUImageFilter gpuImageFilter) {
        mAfilterGroup.changeFilter(gpuImageFilter);
    }

    /**
     * 是否纯色,视频的别净土的渲染放在videodrawer去操作，
     */
    public void setPureColor(BGInfo bgInfo) {
        mVideoBackgrounFilter.setIsPureColor(bgInfo);
    }

    /**
     * 给背景显示设置当前显示尺寸
     */
    public void setVideoMediaItem(MediaItem mediaItem) {
        videoMediaItem = (VideoMediaItem) mediaItem;
        mVideoBackgrounFilter.setVideoMediaItem(mediaItem);
    }

    /**
     * 给背景显示设置当前显示尺寸
     */
    public void setVideoFrameRatioUpdate(MediaItem mediaItem) {
        if (mediaItem.getMediaType() != MediaType.VIDEO) {
            return;
        }
        videoMediaItem = (VideoMediaItem) mediaItem;
        mVideoBackgrounFilter.setVideoFrameRatioUpdate(mediaItem);
    }


    /**
     * 是否是预览转场效果,如果是预览转场效果，转场播放结束后设置isplaying为false
     */
    public void isPreviewTransition(boolean isPreviewTransition) {
        this.isPreviewTransition = isPreviewTransition;
    }

    public interface TransitionCallback {
        void onPause();
    }

    private TransitionCallback transitionCallback;

    public void setTransitionCallback(TransitionCallback transitionCallback) {
        this.transitionCallback = transitionCallback;
    }

    public void onDestroy() {
        if (mVideoBackgrounFilter != null) {
            mVideoBackgrounFilter.onDestroy();
        }
        if (mAfilterGroup != null) {
            mAfilterGroup.onDestroy();
        }
        if (surfaceTexture != null) {
            surfaceTexture.release();
        }
    }

    public void setCurrentVideoDuration(int currentVideoDuration) {
        this.currentVideoDuration = currentVideoDuration;
    }

    public void setVideoMedia(VideoMediaItem media) {
        this.videoMediaItem = media;
    }


    public void onVideoChanged(MediaItem info) {
        setRotation(info.getRotation());
    }

    public int getOutputTexture() {
        return outputTexture;
    }
}
