package com.ijoysoft.mediasdk.view;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.isTheme;

import android.content.Context;
import android.opengl.GLES20;
import android.os.Build;
import android.util.AttributeSet;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.NoFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeHelper;
import com.ijoysoft.mediasdk.module.playControl.IMediaCallback;
import com.ijoysoft.mediasdk.module.playControl.MediaConfig;
import com.ijoysoft.mediasdk.module.playControl.VideoExoPlayRender;
import com.ijoysoft.mediasdk.view.teture.BaseGLTextureView;
import com.ijoysoft.mediasdk.view.teture.GLViewRenderer;

import java.util.ArrayList;
import java.util.List;


/**
 * From init to run: onSizeChange --> onSurfaceTextureAvailable --> createGLThread --> createSurface --> onSurfaceCreated --> onSurfaceChanged
 * From pause to run: onResume --> createSurface --> onSurfaceChanged
 * From stop to run: onResume --> onSurfaceTextureAvailable --> createGLThread --> createSurface  --> onSurfaceCreated --> onSurfaceChanged
 * 换成tetureview看看效果
 */
public class VideoGLTextureView extends BaseGLTextureView implements GLViewRenderer {
    private static final String TAG = "VideoGLTextureView";
    private int showWidth, showHeight;
    private int mWidth, mHeight;
    private int offsetX, offsetY;
    private VideoExoPlayRender videoRender;
    //配置信息
    private MediaConfig mediaConfig;
    private List<MediaItem> mediaList = new ArrayList<>();
    private int curIndex, currentPosition, totalTime;
    private MediaItem currentMediaItem;
    private boolean isPlaying;
    private MediaPreviewView.MediaPreviewCallback mediaPreviewCallback;
    private MediaPreviewView.MediaPreviewChangeCallback mMediaPreviewChangeCallback;

    //渲染最终对象
    private AFilter mShowFilter;

    public VideoGLTextureView(Context context) {
        super(context);
    }

    public VideoGLTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoGLTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        super.init();
        videoRender = new VideoExoPlayRender();
        mShowFilter = new NoFilter();
        setRenderer(this);
        videoRender.init(null, new IMediaCallback() {
            @Override
            public void render() {
                requestRender();
            }

            @Override
            public void onComplet(int index) {
                playNextMedia();
            }

            @Override
            public void onComplet() {
                playNextMedia();
            }
        });
    }

    @Override
    public void onSurfaceCreated() {
        LogUtils.v(TAG, "onSurfaceCreated: ");
        videoRender.onSurfaceCreated();
        mShowFilter.create();
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        LogUtils.v(TAG, "onSurfaceChanged: ");
        this.mWidth = width;
        this.mHeight = height;
        calcPreviewRatio(width, height);
        videoRender.onSurfaceChanged(offsetX, offsetY, showWidth, showHeight, width, height);
        mShowFilter.onSizeChanged(width, height);
    }

    @Override
    public void onDrawFrame() {
        currentPosition = getCurPosition();
        videoRender.onDrawFrame();
        mShowFilter.setTextureId(videoRender.getOutputTexture());
        GLES20.glViewport(offsetX, offsetY, showWidth, showHeight);
        mShowFilter.draw();
        if (mediaPreviewCallback != null) {
            mediaPreviewCallback.progress(currentPosition);
        }
    }

    public void setDataSource(List<MediaItem> dataSource, MediaConfig mediaConfig) {
        if (ObjectUtils.isEmpty(dataSource)) {
            return;
        }
        curIndex = 0;
        mediaList = dataSource;
        currentMediaItem = dataSource.get(0);
        this.mediaConfig = mediaConfig;
        videoRender.setDataSource(dataSource, mediaConfig);
        videoRender.setCurrentMediaItem(currentMediaItem);
        computeTotalTime();
    }

    /**
     * 计算显示区域的宽高比，宽屏(16:9),竖屏(9:16),方块(1:1)
     * 其实如果是图片比是4:3用1:1的展示较合适
     * 如果是图片带角度其实是9:16这样的，采取16:9的方式了
     */
    public void calcPreviewRatio(int screenWidth, int screenHeight) {
        RatioType ratioType = mediaConfig.getRatioType() != null ? mediaConfig.getRatioType() : RatioType._9_16;
        float screenRatio = screenWidth * 1f / screenHeight;
        float showRatio = ratioType.getRatioValue();
        if (screenRatio > showRatio) {
            showHeight = screenHeight;
            showWidth = (int) (showHeight * showRatio);
            offsetX = (screenWidth - showWidth) / 2;
            offsetY = 0;
        } else {
            showWidth = screenWidth;
            showHeight = (int) (showWidth / showRatio);
            offsetX = 0;
            offsetY = (screenHeight - showHeight) / 2;
        }
        ConstantMediaSize.showViewWidth = showWidth;
        ConstantMediaSize.showViewHeight = showHeight;
        ConstantMediaSize.offsetX = offsetX;
        ConstantMediaSize.offsetY = offsetY;
    }

    /**
     * 默认滑动
     *
     * @param progress
     */
    public void seekTo(int progress) {
        long duration = 0;
        for (int i = 0; i < mediaList.size(); i++) {
            duration += mediaList.get(i).getFinalDuration();
            if (duration > progress) {
                long ti = progress - (duration - mediaList.get(i).getFinalDuration());
                setSeekTo(i, (int) ti, false);
                break;
            }
        }
    }

    private void setSeekTo(int i, int ti, boolean foceRender) {
        if (curIndex != i) {// 同个media
            curIndex = i;
            currentMediaItem = mediaList.get(i);
            videoRender.setCurrentMediaItem(currentMediaItem);
        }
        videoRender.setSeekTo(i, ti, foceRender);
    }

    /**
     * 强制刷新滑动
     *
     * @param progress
     */
    public void seekToEnd(int progress) {
        long duration = 0;
        for (int i = 0; i < mediaList.size(); i++) {
            duration += mediaList.get(i).getFinalDuration();
            if (duration > progress) {
                long ti = progress - (duration - mediaList.get(i).getFinalDuration());
                setSeekTo(i, (int) ti, true);
                break;
            }
        }
    }

    /**
     * isPlaying now
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * 播放暂停
     */
    public void pause() {
        isPlaying = false;
        videoRender.pause();
        if (mediaPreviewCallback != null) {
            mediaPreviewCallback.parse();
        }
    }

    public void start() {
        if (currentMediaItem == null) {
            return;
        }
        videoRender.start(mediaConfig.isAutoPlay());
        if (mediaConfig.isAutoPlay()) {
            isPlaying = true;
            if (mediaPreviewCallback != null) {
                mediaPreviewCallback.start();
            }
        }
    }

    public void resume() {
        if (currentMediaItem == null) {
            return;
        }
        isPlaying = true;
        videoRender.resume();
    }

    /**
     * 一个单元文件播放完毕
     */
    public void playNextMedia() {
        LogUtils.i(TAG, "playNextMedia");
        if (!isPlaying) {
            return;
        }
        if (!hasNext()) {
            if (mediaPreviewCallback != null) {
                mediaPreviewCallback.onFinish();
            }
            isPlaying = false;
            return;
        }
        switchPlayer();
    }

    /**
     * 切换下一个播放文件
     *
     * @param
     */
    private void switchPlayer() {
        //分开两个动作
        curIndex++;
        if (curIndex >= mediaList.size()) {
            return;
        }
        currentMediaItem = mediaList.get(curIndex);
        if (mMediaPreviewChangeCallback != null) {
            mMediaPreviewChangeCallback.onChange(curIndex);
        }
        videoRender.setCurrentMediaItem(currentMediaItem);
        videoRender.switchPlayer(curIndex);
    }

    /**
     * 播放暂停开关
     *
     * @returnw35ste
     */
    public boolean tooglePlay() {
        if (isPlaying) {
            pause();
        } else {
            resume();
        }
        return isPlaying;
    }

    /**
     * 是否还有下一个文件播放
     *
     * @return
     */
    public boolean hasNext() {
        return curIndex < mediaList.size() - 1;
    }

    public void setMediaPreviewChangeCallback(MediaPreviewView.MediaPreviewChangeCallback mediaPreviewChangeCallback) {
        this.mMediaPreviewChangeCallback = mediaPreviewChangeCallback;
    }

    public void setMediaPreviewCallback(MediaPreviewView.MediaPreviewCallback mediaPreviewCallback) {
        this.mediaPreviewCallback = mediaPreviewCallback;
    }

    public void setMediaPreviewCallback(MediaPreviewView.MediaPreviewLayoutCallback mediaPreviewCallback) {
        this.mediaPreviewCallback = mediaPreviewCallback;
    }

    public MediaItem getCurrentMediaItem() {
        return currentMediaItem;
    }

    public long computeTotalTime() {
        totalTime = 0;
        for (MediaItem mediaItem : mediaList) {
            totalTime += mediaItem.getFinalDuration();
        }
        if (isTheme() && !ThemeHelper.isTimeTheme(ConstantMediaSize.themeType)) {
            totalTime = totalTime - ConstantMediaSize.themeType.getEndOffset();
        }
        return totalTime;
    }

    /**
     * totaltime
     *
     * @return
     */
    public int getTotalTime() {
        return totalTime;
    }

    /**
     * 播放结束时补充
     *
     * @param progress
     */
    public void onFinishResetByProgress(int progress) {
        pause();
        //画面不回到首帧
        curIndex = 0;
        currentPosition = 0;
        currentMediaItem = mediaList.get(curIndex);
        videoRender.finishReset();
        if (mediaPreviewCallback != null) {
            mediaPreviewCallback.progress(0);
        }
        seekTo(progress);
    }

    public void onFinishReset() {
        onFinishResetByProgress(0);
    }

    /**
     * 销毁资源
     */
    public void onDestroy() {
        if (mediaList != null) {
            for (MediaItem mediaItem : mediaList) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    if (mediaItem.getAfilter() != null) {
                        mediaItem.getAfilter().destroy();
                    }
                }
                if (mediaItem.getTransitionFilter() != null) {
                    mediaItem.getTransitionFilter().onDestroy();
                }
            }
        }
        videoRender.onDestroy();
        mediaPreviewCallback = null;
        mMediaPreviewChangeCallback = null;
        if (mShowFilter != null) {
            mShowFilter.onDestroy();
        }
    }

    /**
     * 获取当前播放位置 ms
     *
     * @return
     */
    public int getCurPosition() {
        if (mediaList.isEmpty()) {
            return 0;
        }
        int position = 0;
        for (int i = 0; i < curIndex; i++) {
            position += mediaList.get(i).getFinalDuration(); // TODO 修改時間
        }
        int currentOffet = videoRender.getCurPosition();
        position += currentOffet;
        LogUtils.v("MediaPreviewView", "position:" + position + ",getCurPosition:" + currentOffet + ",curIndex:" + curIndex);
        return position;
    }


}
