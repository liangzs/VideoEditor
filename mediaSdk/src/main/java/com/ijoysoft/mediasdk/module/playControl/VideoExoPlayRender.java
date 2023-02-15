package com.ijoysoft.mediasdk.module.playControl;

import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.BGInfo;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaMatrix;
import com.ijoysoft.mediasdk.module.entity.MediaType;
import com.ijoysoft.mediasdk.module.entity.VideoMediaItem;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.view.MediaPreviewView;
import com.ijoysoft.mediasdk.view.RenderSingleType;

import java.util.ArrayList;
import java.util.List;


/**
 * 采用exoplayer对视频源进行拉取
 *
 * @apiNote 确保videolist的索引和整体medialist的索引切换
 * 转场方案和滤镜方案，在做进行绘画，对视频片段元素进行移动，添加，删除，都需要重新进行ijk的初始化动作
 * <p>
 * 补充surfaceTexture，视频的切换采用爽纹理通道，这样视频在切换的时候，双纹理通道一起运行
 * 用转场替换纹理
 */
public class VideoExoPlayRender implements IMediaRender, ExoPlayerSingle.PlayCallback {
    private static final String TAG = "VideoMediaRender";
    private VideoPlayer mVideoPlayer;
    private Surface surface;
    private IMediaCallback mIMediaCallback;
    private MediaItem currentMediaItem;
    //    private MediaPreviewView mGLSurfaceView;
    private int currentPosition;
    private int currentVideoIndex;
    private List<MediaItem> medialist;
    private List<VideoMediaItem> videoLists;
    private SurfaceTexture surfaceTexture;
    private boolean finishReset, isCompleteRender;
    //原始片段播放
    private boolean originPlay;


    @Override
    public void onSurfaceCreated() {
        mVideoPlayer.onSurfaceCreated();
        surfaceTexture = mVideoPlayer.getSurfaceTexture();
        // 这句很重要，是告诉surface转gl过程中，有帧来了
        surfaceTexture.setOnFrameAvailableListener(surfaceTexture -> {
//            LogUtils.i(TAG, "onFrameAvailable:" + IjkExoMediaPlayer.getInstance().getCurrentPosition());
            if (finishReset) {
                LogUtils.i(TAG, "onFrameAvailable...finishReset:" + finishReset);
                finishReset = false;
                return;
            }
            if (isCompleteRender) {
                ExoPlayerSingle.INSTANCE.getHandler().postDelayed(() -> {
                    if (mIMediaCallback != null) {
                        mIMediaCallback.render();
                    }
                }, 400);
                isCompleteRender = false;
                return;
            }
            if (mIMediaCallback != null) {
                mIMediaCallback.render();
            }
        });
        surface = new Surface(surfaceTexture);
        ExoPlayerSingle.INSTANCE.setSurface(surface);
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        LogUtils.i(TAG, "onSurfaceChanged...");
        mVideoPlayer.onSurfaceChanged(0, 0, width, height, screenWidth, screenHeight);
        mVideoPlayer.onVideoChanged(currentMediaItem);
    }

    @Override
    public void onDrawFrame() {
        mVideoPlayer.setCurrentVideoDuration(currentPosition);
        mVideoPlayer.onDrawFrameStatus();
    }

    @Override
    public ActionStatus onDrawFrameStatus() {
        mVideoPlayer.setCurrentVideoDuration(currentPosition);
        return mVideoPlayer.onDrawFrameStatus();
    }

    @Override
    public void init(MediaRenderBus bus, IMediaCallback callback) {
        mVideoPlayer = new VideoPlayer();
        this.mIMediaCallback = callback;
        ExoPlayerSingle.INSTANCE.setPlayCallback(() -> {
            if (mIMediaCallback != null) {
                mIMediaCallback.onComplet();
            }
        });
    }

    @Override
    public void start(boolean autoPlayer) {
        updateTransitionAndAfilterVideo();
        ExoPlayerSingle.INSTANCE.setSurface(surface);
        if (autoPlayer) {
            ExoPlayerSingle.INSTANCE.start();
            updateVideoPlayeInfo();
        }
    }


    @Override
    public void pause() {
        ExoPlayerSingle.INSTANCE.pause();
    }

    @Override
    public void resume() {
        finishReset = false;
        ExoPlayerSingle.INSTANCE.setSurface(surface);
        ExoPlayerSingle.INSTANCE.start();
//            mVideoPlayer.setTransitionShow(true);
        updateVideoPlayeInfo();
        if (mIMediaCallback != null) {
            mIMediaCallback.render();
        }
    }

    /**
     * 更新音量，变速等视频信息
     */
    private void updateVideoPlayeInfo() {
        if (currentMediaItem == null) {
            return;
        }
        float volume = ((VideoMediaItem) currentMediaItem).getPlayVolume();
        ExoPlayerSingle.INSTANCE.setVolume(volume);
        if (originPlay) {
            ExoPlayerSingle.INSTANCE.setSpeed(1);
            return;
        }
        float speed = currentMediaItem.getSpeed();
        speed = speed == 0 ? 1 : speed;
        ExoPlayerSingle.INSTANCE.setSpeed(speed);
    }


    /**
     * 因为视频的播放作为一个单例，所以最后清理时，记得一定要把数据清理，解绑
     */
    @Override
    public void onDestroy() {
        surface = null;
        mVideoPlayer.onDestroy();
        mIMediaCallback = null;
        ExoPlayerSingle.INSTANCE.setPlayCallback(null);
        onMainDestroy();
    }

    @Override
    public void setDataSource(List<MediaItem> dataSource, MediaConfig mediaConfig) {
        setDataSource(dataSource, false);
    }

    @Override
    public void onMainDestroy() {
        ExoPlayerSingle.INSTANCE.stop();
        ExoPlayerSingle.INSTANCE.release();
        //清理隐式调用
        ExoPlayerSingle.INSTANCE.removeListener();
    }

    @Override
    public int getPreTexture() {
        return 0;
    }

    @Override
    public int getNextTexture() {
        return 0;
    }

    @Override
    public void onVideoPause() {
        ExoPlayerSingle.INSTANCE.stop();
        ExoPlayerSingle.INSTANCE.release();

    }

    @Override
    public void videoReset() {
        surfaceTexture = mVideoPlayer.getSurfaceTexture();
        // 这句很重要，是告诉surface转gl过程中，有帧来了
        surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                if (mIMediaCallback != null) {
                    mIMediaCallback.render();
                }
            }
        });
        surface = new Surface(surfaceTexture);
        ExoPlayerSingle.INSTANCE.setSurface(surface);
    }

    @Override
    public void setDataSource(List<MediaItem> dataSource, boolean originPathPlay, boolean noSpeedPlay) {
        setDataSource(dataSource, originPathPlay);
    }

    public void setDataSource(List<MediaItem> dataSource, boolean originPath) {
        originPlay = originPath;
        filterVideoItem(dataSource);
        createContatMediaPlayer(originPath);
        this.medialist = dataSource;
        if (!videoLists.isEmpty()) {
            mVideoPlayer.setVideoMediaItem(videoLists.get(0));
        }
        if (currentVideoIndex > medialist.size() - 1) {
            currentVideoIndex = medialist.size() - 1;
        }
    }


    /**
     * 过滤出视频频片段
     */
    private void filterVideoItem(List<MediaItem> srcList) {
        videoLists = new ArrayList<>();
        if (ObjectUtils.isEmpty(srcList)) {
            return;
        }
        for (MediaItem mediaItem : srcList) {
            if (mediaItem == null) {
                continue;
            }
            if (mediaItem.getMediaType() == MediaType.VIDEO) {
                videoLists.add((VideoMediaItem) mediaItem);
            }
        }
    }


    /**
     * 通过currentVideoIndex进行seekto预览
     *
     * @param index
     */
    @Override
    public void previewMediaItem(int index) {
        currentVideoIndex = getVideoIndex(index);
        updateTransitionAndAfilterVideo();
//        mVideoPlayer.setTransitionShow(false);
        currentPosition = 0;
        ExoPlayerSingle.INSTANCE.setSurface(surface);
        ExoPlayerSingle.INSTANCE.seekTo(currentVideoIndex, 0);

        //替换为updateTexImage方案，只有新的数据来到了，才进行刷新，不然直接刷新会显示上一片段的素材数据
//        mGLSurfaceView.queueEvent(() -> {
//            try {
//                if (mVideoPlayer != null && mVideoPlayer.getSurfaceTexture() != null) {
//                    mVideoPlayer.getSurfaceTexture().updateTexImage();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
    }

    @Override
    public void previewTransition(boolean flag) {
        mVideoPlayer.isPreviewTransition(flag);
    }

    @Override
    public void previewTransitionPreViedeo(boolean flag) {
        mVideoPlayer.isPreviewTransition(flag);
        currentVideoIndex = getVideoIndex();
        ExoPlayerSingle.INSTANCE.seekTo(currentVideoIndex, 0);
        mVideoPlayer.onVideoChanged(currentMediaItem);
        mVideoPlayer.setVideoFrameRatioUpdate(currentMediaItem);
        mVideoPlayer.playPrepare();
        mVideoPlayer.setCurrentVideoDuration(0);
        currentPosition = 0;
        mIMediaCallback.render();
        ExoPlayerSingle.INSTANCE.start();
        updateVideoPlayeInfo();
    }

    @Override
    public void switchPlayer(int curIndex) {
        LogUtils.v("VideoMediaRender", "params:" + "curIndex:" + curIndex);

        currentPosition = 0;
        updateTransitionAndAfilterVideo();
        updateVideoPlayeInfo();
        //重置为0重头播放
        ExoPlayerSingle.INSTANCE.seekTo(getVideoIndex(curIndex), 0);
        ExoPlayerSingle.INSTANCE.setSurface(surface);
//            IjkExoMediaPlayer.getInstance().checkSwitchPlay();
//        mGLSurfaceView.requestRender();
    }


    private volatile boolean seeking;

    /**
     * 从新计算index在videolist中的索引
     *
     * @param i
     * @param ti
     * @param forceRender
     */
    @Override
    public void setSeekTo(int i, int ti, boolean forceRender) {
        seekImple(i, ti, forceRender);
    }

    @Override
    public void setZoomScale(BGInfo bgInfo) {
        //记录scaleZoom的值
        mVideoPlayer.setPureColor(bgInfo);
    }

//    @Override
//    public void drawNoTransition() {
//        mVideoPlayer.setTransitionShow(false);
//    }

    @Override
    public void finishReset() {
        finishReset = true;
    }

    private void seekImple(int i, int ti, boolean forceRender) {
//        if (seeking) {
//            return;
//        }
        boolean haRotation = false;
        int videoIndex = getVideoIndex(i);
        if (i >= medialist.size() || videoLists.isEmpty() || videoIndex == -1 || currentVideoIndex >= videoLists.size()) {
            return;
        }
        LogUtils.i(TAG, "i:" + i + ",ti:" + ti + ",videoIndex:" + videoIndex + ",currentVideoIndex:" + currentVideoIndex);
        if (currentVideoIndex != videoIndex || forceRender) {
            currentMediaItem = videoLists.get(videoIndex);
            seeking = true;
            updateTransitionAndAfilterVideo();
            if (currentVideoIndex != -1) {
                haRotation = videoLists.get(currentVideoIndex).getRotation() != videoLists.get(videoIndex).getRotation();
            }
        }
        currentVideoIndex = videoIndex;
        //如果视频有进行加速和减速情况的，要进行还原
        currentPosition = ti;
        if (currentMediaItem != null && currentMediaItem.getSpeed() != 0) {
            currentPosition *= currentMediaItem.getSpeed();
        }
        if (videoIndex == 0 && videoLists.size() == 1) {
            ExoPlayerSingle.INSTANCE.seekTo(currentPosition);
        } else {
            ExoPlayerSingle.INSTANCE.seekTo(videoIndex, currentPosition);
        }
    }


    @Override
    public void seekToEnd() {
//        mGLSurfaceView.queueEvent(() -> {
//            try {
//                mVideoPlayer.getSurfaceTexture().updateTexImage();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
    }


    /**
     * 更改显示区域，滤镜，转场，都在这里完成
     */
    @Override
    public void updateTransitionAndAfilterVideo() {
        mVideoPlayer.setVideoFrameRatioUpdate(currentMediaItem);
        mVideoPlayer.onVideoChanged(currentMediaItem);
//        mGLSurfaceView.queueEvent(() -> {
//            Log.i(TAG, "updateTransitionAndAfilterVideo........");
//            mVideoPlayer.onVideoChanged(currentMediaItem);
//            mVideoPlayer.setVideoFrameRatioUpdate(currentMediaItem);
//            mVideoPlayer.playPrepare(mGLSurfaceView.getmOpenglDrawer(), false);
//            if (currentMediaItem.getAfilter() != null
//                    && currentMediaItem.getAfilter().getmFilterType() != MagicFilterType.NONE) {
//                mVideoPlayer.changeFilter(currentMediaItem.getAfilter());
//            } else {
//                mVideoPlayer.changeFilter(new GPUImageFilter());
//            }
//            seeking = false;
//        });
    }

    @Override
    public boolean checkSupportTransition() {
        return true;
    }

    @Override
    public void switchNotVideo() {
        currentPosition = 0;
        if (ExoPlayerSingle.INSTANCE.isPlaying()) {
            ExoPlayerSingle.INSTANCE.pause();
        }
    }

    @Override
    public void setFilterStrength(float progress, List<String> renderSingleTasks) {
//        mGLSurfaceView.queueEvent(() -> {
//            mVideoPlayer.setFilterStrength(progress);
//            mIMediaCallback.render();
//            renderSingleTasks.remove(RenderSingleType.FILTER);
//        });
    }


    @Override
    public void setRatio() {
    }


    @Override
    public void setPureColor(BGInfo bgInfo) {
//        mGLSurfaceView.queueEvent(new Runnable() {
//            @Override
//            public void run() {
//                mVideoPlayer.setPureColor(bgInfo);
//            }
//        });
    }


    @Override
    public void setScaleTranlation(float scale, int x, int y, int originX, int originY) {
        // 需要重置matrix
        if (currentMediaItem.getMediaMatrix() == null) {
            currentMediaItem.setMediaMatrix(new MediaMatrix(scale, x, y, originX, originY));
        } else {
            currentMediaItem.getMediaMatrix().setScale(scale);
            currentMediaItem.getMediaMatrix().setOffsetX(x);
            currentMediaItem.getMediaMatrix().setOffsetY(y);
            currentMediaItem.getMediaMatrix().setOriginX(originX);
            currentMediaItem.getMediaMatrix().setOriginY(originY);
        }
        updateTransitionAndAfilterVideo();
        if (mIMediaCallback != null) {
            mIMediaCallback.render();
        }
    }


    @Override
    public void previewAfilter() {
        mVideoPlayer.changeFilter(currentMediaItem.getAfilter());
    }


    @Override
    public void doMediaRotate() {
        updateTransitionAndAfilterVideo();
    }

    @Override
    public void doMediaMirror() {
        updateTransitionAndAfilterVideo();
    }


    @Override
    public int getOutputTexture() {
        return mVideoPlayer.getOutputTexture();
    }

    @Override
    public int getCurPosition() {
        ExoPlayerSingle.INSTANCE.getHandler().post(() -> currentPosition = (int) ExoPlayerSingle.INSTANCE.getMExoPlayer().getCurrentPosition());
        return currentPosition;
    }

    @Override
    public void setCurrentMediaItem(MediaItem mediaItem) {
        this.currentMediaItem = mediaItem;
    }

    @Override
    public void setCurrenPostion(int currenPostion) {
        this.currentPosition = currenPostion;
    }


    @Override
    public void setClipPreview(boolean flag) {

    }


    @Override
    public void removeTransition() {
        mVideoPlayer.isPreviewTransition(false);
    }


    @Override
    public void setVolume(float volume) {
        LogUtils.v("VideoMediaRender", "volume:" + volume);
        ExoPlayerSingle.INSTANCE.setVolume(volume);
    }


    /**
     * 尝试使用exoplayer的concat连续播放功能
     */
    public void createContatMediaPlayer(boolean useOriPath) {
        if (ObjectUtils.isEmpty(videoLists)) {
            return;
        }
        ExoPlayerSingle.INSTANCE.initPlay();
        ExoPlayerSingle.INSTANCE.stop();
        if (surface != null) {
            ExoPlayerSingle.INSTANCE.setSurface(surface);
        }
        ExoPlayerSingle.INSTANCE.setDataSource(videoLists, useOriPath);
    }


    /**
     * 转化器
     *
     * @return
     */
    public int getMediaIndex(int videoIndex) {
        if (ObjectUtils.isEmpty(videoLists) || ObjectUtils.isEmpty(medialist)) {
            return 0;
        }
        return medialist.indexOf(videoLists.get(videoIndex));
    }

    public int getVideoIndex(int mediaIndex) {
        if (ObjectUtils.isEmpty(videoLists) || ObjectUtils.isEmpty(medialist) || mediaIndex >= medialist.size()) {
            return -1;
        }
        return videoLists.indexOf(medialist.get(mediaIndex));
    }

    public int getVideoIndex() {
        if (currentMediaItem == null) {
            return 0;
        }
        return videoLists.indexOf(currentMediaItem);
    }

    @Override
    public void setvideoSpeed(float speed) {
        ExoPlayerSingle.INSTANCE.setSpeed(speed);
    }

    @Override
    public void setReverse(boolean reverse) {
    }


    /**
     * 重置当前进度
     */
    public void resetCurrentClipProgress() {
        currentPosition = 0;
        ExoPlayerSingle.INSTANCE.seekTo(currentVideoIndex, 0);
    }

    public void onCompletion() {
        currentPosition = 0;
        isCompleteRender = true;
//        mIMediaCallback.onComplet(getMediaIndex(currentVideoIndex));
    }

}
