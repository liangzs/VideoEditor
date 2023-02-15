package com.ijoysoft.mediasdk.module.playControl;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.BGInfo;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaType;
import com.ijoysoft.mediasdk.module.entity.VideoMediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.VideoBackgroundFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.SlideGpuFilterGroup;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

/**
 * 视频播放，包含滤镜转场的纹理附加
 * 对于nofilter而已，重写了纹理的坐标值，使得其对原于纹理值进行垂直翻转，解决图像倒置的问题
 */
public class VideoRichRender implements IRender {
    private static final String TAG = "VideoPlayer";
    //视频源的背景
    private VideoBackgroundFilter mVideoBackgrounFilter;
    // 滤镜组
    private SlideGpuFilterGroup mAfilterGroup;
    // 用于视频旋转的参数
    private int rotation;
    private int currentVideoDuration;
    private VideoMediaItem videoMediaItem;
    private boolean isPreviewTransition;
    // opengl状态机的模型，所以纹理的值就一个int类型
    private int outputTexture;
    //视频源纹理
    private int inputTexture;

    public VideoRichRender() {
        mVideoBackgrounFilter = new VideoBackgroundFilter();
        mAfilterGroup = new SlideGpuFilterGroup();
        // mMatrixShow = new OrientationNoFilter();
    }

    /**
     * 设置视频源纹理
     *
     * @param inputTexture
     */
    public void setInputTexture(int inputTexture) {
        this.inputTexture = inputTexture;
        mVideoBackgrounFilter.setTextureId(inputTexture);
    }

    @Override
    public void onSurfaceCreated() {
        mVideoBackgrounFilter.create();
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
        if (isPreviewTransition && currentVideoDuration > (videoMediaItem.getPagDuration() != 0 ? (int) videoMediaItem.getPagDuration() : ConstantMediaSize.TRANSITION_DURATION)) {
            isPreviewTransition = false;
            if (transitionCallback != null) {
                transitionCallback.onPause();
            }
            return ActionStatus.STAY;
        }
        if (videoMediaItem == null) {
            return ActionStatus.STAY;
        }
        LogUtils.v("VideoPlayer", "currentVideoDuration:" + currentVideoDuration);
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
