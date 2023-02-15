package com.ijoysoft.mediasdk.view;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.isTheme;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.AttributeSet;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.AudioMediaItem;
import com.ijoysoft.mediasdk.module.entity.BGInfo;
import com.ijoysoft.mediasdk.module.entity.DoodleItem;
import com.ijoysoft.mediasdk.module.entity.DurationInterval;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaMatrix;
import com.ijoysoft.mediasdk.module.entity.MediaType;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.entity.ResolutionType;
import com.ijoysoft.mediasdk.module.entity.VideoMediaItem;
import com.ijoysoft.mediasdk.module.opengl.InnerBorder;
import com.ijoysoft.mediasdk.module.opengl.particle.GlobalParticles;
import com.ijoysoft.mediasdk.module.opengl.particle.PAGNoBgParticle;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleDrawerManager;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeHelper;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter;
import com.ijoysoft.mediasdk.module.playControl.AudioPlayer;
import com.ijoysoft.mediasdk.module.playControl.IMediaCallback;
import com.ijoysoft.mediasdk.module.playControl.MediaConfig;
import com.ijoysoft.mediasdk.module.playControl.MediaRenderBus;
import com.ijoysoft.mediasdk.module.playControl.RecordPlayer;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import kotlin.Triple;
import kotlin.Unit;

/**
 * 图片播放用imageplayer，视频播放用VideoPlayer
 * <p>
 * 视频的前置transitionfilter跟视频一起播放
 * <p>
 * pre 选取好视频和图片时候，对视频和图片进行预处理，
 */
public class MediaPreviewView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private static final String TAG = "MediaPreviewView";
    private static final int FLUSH_RATE = 3;
    private List<MediaItem> mediaList = new ArrayList<>();
    private MediaPreviewCallback mediaPreviewCallback;
    private MediaPreviewChangeCallback mMediaPreviewChangeCallback;
    private AudioPlayer audioPlayer;
    private RecordPlayer recordPlayer;

    private int audioCount = 0; // 减少音频检测频率
    private int currentPosition;
    private int totalTime;
    private int curIndex;
    private MediaItem currentMediaItem;
    //渲染总线
    private MediaRenderBus renderBus;
    private int showWidth, showHeight;
    private int mWidth, mHeight;
    private int offsetX, offsetY;
    private ScreenShotCallback screenShotCallback;
    private boolean isPlaying, isTakeScreenShot, isMurging, isDataSource;
    private boolean isThemeChanging;//正在进行主题切中，做预处理状态
    //做一些动作时，不做progress的更新，因为出exoplayer视频播放时，做了seek(0,0)操作之后，progress值依然不是0的
    private boolean isNoProgressFrame;
    private MediaConfig mediaConfig;

    //针对seekbar这些操作，会产生很多渲染任务，在接入gthread前做一个缓存任务，同类型的只保留一次渲染
    private List<String> renderSingleTasks;


    private IMediaCallback mediaCallback = new IMediaCallback() {
        @Override
        public void render() {
            requestRender();
        }

        @Override
        public void onPrepare() {

        }

        @Override
        public void onStart() {

        }

        @Override
        public void onPause() {
            pause();
        }

        //视频回调,如果是走了seekto，那么curIndex的值已经是提前赋值了，
        //由于exoplayer的seekto操作会从新走这个回调，所以这个回调不是播放结束的回调，不应该再走这个方法
        @Override
        public void onComplet(int index) {
            playNextMedia();
        }

        @Override
        public void onComplet() {
            playNextMedia();
        }

        @Override
        public void onVideoChanged(MediaItem info) {
        }

        @Override
        public void preTranComplete() {
            isPlaying = false;
        }
    };


    public MediaPreviewView(Context context) {
        super(context, null);
    }

    public MediaPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 这里关键字很重要RENDERMODE_WHEN_DIRTY，如果去除glview会自动刷新
     */
    private void init() {
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);// when the surface is created, or when {@link #requestRender} is called.
        setPreserveEGLContextOnPause(false);// 按理说true更合适
        setCameraDistance(100);
        audioPlayer = new AudioPlayer();
        recordPlayer = new RecordPlayer();
        renderBus = new MediaRenderBus(mediaCallback);
        renderBus.setRenderTask(this::addRenderTask);
        mediaConfig = new MediaConfig.Builder().build();
        renderSingleTasks = new ArrayList<>();
    }

    /**
     * 添加渲染环境任务
     *
     * @return
     */
    private Unit addRenderTask(Runnable r) {
        LogUtils.v("MediaPreviewView", "add renderTask..");
        queueEvent(r);
        return null;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        LogUtils.i(TAG, "onSurfaceCreated....");
        if (currentMediaItem == null) {
            return;
        }
        renderBus.onSurfaceCreated();
    }

    // 控制比例显示
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        if (isMurging) {
            return;
        }
        ConstantMediaSize.canvasHeight = height;
        ConstantMediaSize.canvasWidth = width;
        LogUtils.i(TAG, "onSurfaceChanged:mWidth" + mWidth + ",mHeight:" + mHeight + ",showWidth:" + showWidth + ",showHeight:" + showHeight);
        if (currentMediaItem == null) {
            LogUtils.e(TAG, "onSurfaceChanged:currentMediaItem is null");
            return;
        }
        calcPreviewRatio(width, height);
        renderBus.onSurfaceChanged(offsetX, offsetY, showWidth, showHeight, width, height);
        if (mediaPreviewCallback instanceof MediaPreviewLayoutCallback) {
            ((MediaPreviewLayoutCallback) mediaPreviewCallback).mediaPreviewLayout();
        }
        if (isDataSource) {
            start();
            isDataSource = false;
        }
    }

    public void setMurging(boolean isMurging) {
        this.isMurging = isMurging;
    }

    // 每帧的渲染的时候，把该帧内容功能保存视频文件
    @Override
    public void onDrawFrame(GL10 gl) {
        if (currentMediaItem == null) {
            return;
        }
        currentPosition = getCurPosition();
        renderBus.onDrawFrame(currentPosition);
        audioCount++;
        // 音频播放
        if (audioCount % FLUSH_RATE == 0) {
            audioPlayer.ondrawFramePlay(currentPosition);
            recordPlayer.ondrawFramePlay(currentPosition);
            if (audioCount >= 3333) {
                audioCount = 0;
            }
        }
        if (isNoProgressFrame) {
            isNoProgressFrame = false;
            return;
        }
        if (mediaPreviewCallback != null) {
            mediaPreviewCallback.progress(currentPosition);
        }
        if (isTakeScreenShot && screenShotCallback != null) {
            Bitmap bitmap = createBitmapFromGLSurface(offsetX, offsetY, showWidth, showHeight, gl);
            screenShotCallback.result(bitmap);
            isTakeScreenShot = false;
        }
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
     * 当前media对象是否是视频
     *
     * @return
     */
    public boolean checkIsVideo() {
        if (currentMediaItem == null) {
            return false;
        }
        renderBus.locationRender(currentMediaItem);
        return false;
    }

    /**
     * 当前media对象是否是视频
     *
     * @return
     */
    public boolean isCurrentVideo() {
        if (currentMediaItem == null) {
            return false;
        }
        if (MediaType.VIDEO == currentMediaItem.getMediaType()) {
            return true;
        }
        return false;
    }

    /**
     * 播放结束，会回到第一帧
     *
     * @return
     * @date 只设置seek为0, 不重新requestrender，画面保持在最后
     */
    public void onFinishReset() {
        onFinishResetByProgress(0);
    }

    public void onFinishResetByProgress(int progress) {
        if (ObjectUtils.isEmpty(mediaList)) {
            return;
        }
        pause();
        //画面不回到首帧
        curIndex = 0;
        currentPosition = 0;
        currentMediaItem = mediaList.get(curIndex);
        renderBus.onFinishResetByProgress(progress);
        //检测视频是否为最后一个文件，如果视频是最后一个文件，播放结束时候，会走渲染回调，要取消这个回调
        if (mediaPreviewCallback != null) {
            mediaPreviewCallback.progress(0);
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
        audioPlayer.pause();
        recordPlayer.pause();
        renderBus.pause();
        if (mediaPreviewCallback != null) {
            mediaPreviewCallback.parse();
        }
    }

    /**
     * 还是视频的问题，如果存在大分辨率视频，比如2k以上视频文件
     * 当走onPuase的时候，释放exoplayer对象,然后再onresume重建对象
     */
    public void onVideoPause() {
//        if (existVideo()) {
//            videoRender.onVideoPause();
//        }
    }


    /**
     * start play video
     */
    public void start() {
        if (currentMediaItem == null) {
            return;
        }
        renderBus.start(mediaConfig.isAutoPlay());
        if (mediaConfig.isAutoPlay()) {
            isPlaying = true;
            audioPlayer.resume();
            recordPlayer.resume();
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
        audioPlayer.resume();
        recordPlayer.resume();
        renderBus.resume();
    }

    public void resumeAudio() {
        audioPlayer.resume();
        recordPlayer.resume();
    }

    /**
     * 播放结束
     */
    public void onDestroy() {
        renderBus.onDestroy();
        if (audioPlayer != null) {
            audioPlayer.onDestroy();
        }
        if (recordPlayer != null) {
            recordPlayer.onDestroy();
        }

        if (mediaList != null) {
            for (MediaItem mediaItem : mediaList) {
//                if (mediaItem.getAfilter() != null) {
//                    mediaItem.getAfilter().destroy();
//                }
                if (mediaItem.getTransitionFilter() != null) {
                    mediaItem.getTransitionFilter().onDestroy();
                }
            }
        }
        mediaPreviewCallback = null;
        mMediaPreviewChangeCallback = null;
        screenShotCallback = null;
        //清空回调
        mediaCallback = null;
    }

    public void onMainDestroy() {
//        renderBus.onMainDestroy();
    }


    /**
     * 是否转置编辑，即不预览翻转效果
     */
    public void setReverseEdit(boolean flag) {
        renderBus.videoReveasePlay(flag);
    }

    /**
     * 设置视频的播放地址,当视频的.getCurrentPosition>2的时候
     * 可以去除视频前置的transitionfilter
     */
    public void setDataSource(List<MediaItem> dataSource, List<DoodleItem> doodleItems, MediaConfig mediaConfig) {
        if (ObjectUtils.isEmpty(dataSource)) {
            return;
        }
        curIndex = 0;
        mediaList = dataSource;
        currentMediaItem = dataSource.get(0);
        renderBus.setDataSource(dataSource, doodleItems, mediaConfig);
        checkIsVideo();
        computeTotalTime();
        this.mediaConfig = mediaConfig;
        setAudioFadingPlay(mediaConfig.isFading());
        isDataSource = true;
    }

    /**
     * 设置控件素材
     */
    public void setWidgetDataSource(List<Bitmap> widgetDataSource) {
        renderBus.setWidgetDataSource(widgetDataSource);
    }


    /**
     * 对于子项的操作确认之后，要更新主项的数据
     *
     * @param dataSource
     */
    public void updateDataSource(List<MediaItem> dataSource) {
        if (ObjectUtils.isEmpty(dataSource)) {
            return;
        }
        //发现竟然出现了null的情况,只能再做一次循环过滤
        Iterator<MediaItem> iterator = dataSource.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == null) {
                iterator.remove();
            }
        }
        mediaList = dataSource;

        if (curIndex > mediaList.size() - 1) {
            curIndex = mediaList.size() - 1;
        }
        computeTotalTime();
        renderBus.updateDataSource(mediaList);
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
            audioPlayer.stop();
            recordPlayer.stop();
            if (mediaPreviewCallback != null) {
                mediaPreviewCallback.onFinish();
            }
            isPlaying = false;
            return;
        }
        switchPlayer();
    }

    public int getCurIndex() {
        return curIndex;
    }

    /**
     * 是否还有下一个文件播放
     *
     * @return
     */
    public boolean hasNext() {
        return curIndex < mediaList.size() - 1;
    }

    // 跳转到指定的时间点，只能跳到关键帧
    public void seekTo(int progress) {
        long duration = 0;
        for (int i = 0; i < mediaList.size(); i++) {
            duration += mediaList.get(i).getFinalDuration();
            if (duration > progress) {
                long ti = progress - (duration - mediaList.get(i).getFinalDuration());
                setSeekTo(i, (int) ti);
                break;
            }
        }
    }


    /**
     * 设置跳转位置
     * 如果有做切换，进行currentRender的切换
     *
     * @param i
     * @param ti
     */
    public void setSeekTo(int i, int ti) {
        renderBus.setSeekTo(i, ti, curIndex != i);
        if (curIndex != i) {// 同个media
            curIndex = i;
            currentMediaItem = mediaList.get(i);
        }
    }

    /**
     * seek触摸结束时进行跳转，只执行一次，主要是控制audioplayer
     * isRotation 当两个视频进行切换时，上下两个视频存在的角度不一样，则错出现旋转帧显示
     *
     * @param progress
     */
    public void seekToEnd(int progress) {
        renderBus.seekToEnd();
        long duration = 0;
        int i;
        for (i = 0; i < mediaList.size(); i++) {
            duration += mediaList.get(i).getFinalDuration();
            if (duration >= progress) {
                long ti = progress - (duration - mediaList.get(i).getFinalDuration());
                if (curIndex != i) {// 同个media
                    LogUtils.i(TAG, "seekToEnd:" + ti + ",i=" + i + ",curIndex=" + curIndex);
                    curIndex = i;
                    currentMediaItem = mediaList.get(i);
                }
                renderBus.setSeekTo(curIndex, (int) ti, currentMediaItem.isImage());
                break;
            }
        }
        try {
            audioPlayer.seekTo(progress);
            recordPlayer.seekTo(progress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 片段编辑
     *
     * @param curIndex
     * @param progress
     */
    public void seekToForce(int curIndex, int progress) {
        this.curIndex = curIndex;
        currentMediaItem = mediaList.get(curIndex);
        renderBus.setSeekTo(curIndex, progress, true);
        try {
            audioPlayer.seekTo(progress);
            recordPlayer.seekTo(progress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 音频设置进度
     *
     * @param progress
     */
    public void audioSeek(int progress) {
        try {
            audioPlayer.seekTo(progress);
            recordPlayer.seekTo(progress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void seekVideoIndex(int index) {
        if (index < 0 || index > mediaList.size() - 1) {
            return;
        }
        curIndex = index;
        currentMediaItem = mediaList.get(index);
    }

    public void seekToIndex(int index) {
        if (index < 0 || index > mediaList.size() - 1) {
            return;
        }
        curIndex = index;
        currentMediaItem = mediaList.get(index);
        renderBus.setSeekTo(curIndex, 0, true);
        int duration = 0;
        for (int i = 0; i < index; i++) {
            duration += mediaList.get(i).getFinalDuration();
        }
        audioPlayer.seekTo(duration);
        recordPlayer.seekTo(duration);
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
        renderBus.switchPlayer(new Triple(curIndex, getCurPosition(), false));
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
        int currentOffet = renderBus.getCurPosition();
        position += currentOffet;
        LogUtils.v("MediaPreviewView", "position:" + position + ",getCurPosition:" + currentOffet + ",curIndex:" + curIndex);
        return position;
    }

    /**
     * 计算总的时间，视频占多少时间，图片默认是4s时间
     */
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
     * 检查第一个媒体文件是否是视频
     *
     * @return
     */
    public boolean checkFirstIsVideo() {
        if (ObjectUtils.isEmpty(mediaList)) {
            return false;
        }
        return mediaList.get(0).isVideo();
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
     * totaltime
     *
     * @return
     */
    public int getTotalOriginTime() {
        int time = 0;
        for (MediaItem mediaItem : mediaList) {
            if (mediaItem.isVideo()) {
                time += mediaItem.getVideoOriginDuration();
            } else {
                time += mediaItem.getFinalDuration();
            }
        }
        return time;
    }


    /**
     * 播放暂停开关
     *
     * @return
     */
    public boolean tooglePlay() {
        if (isPlaying) {
            pause();
        } else {
            resume();
        }
        return isPlaying;
    }

    public void setMediaPreviewCallback(MediaPreviewCallback mediaPreviewCallback) {
        this.mediaPreviewCallback = mediaPreviewCallback;
    }

    public void setMediaPreviewCallback(MediaPreviewLayoutCallback mediaPreviewCallback) {
        this.mediaPreviewCallback = mediaPreviewCallback;
    }

    public interface MediaPreviewCallback {

        default void start() {
        }

        default void parse() {
        }

        default void progress(int position) {
        }

        void onFinish();

    }

    public interface MediaPreviewLayoutCallback extends MediaPreviewCallback {
        void mediaPreviewLayout();

    }

    public void setMediaPreviewChangeCallback(MediaPreviewChangeCallback mediaPreviewChangeCallback) {
        this.mMediaPreviewChangeCallback = mediaPreviewChangeCallback;
    }

    public interface MediaPreviewChangeCallback {
        void onChange(int index);
    }


    public interface ScreenShotCallback {
        void result(Bitmap bitmap);
    }

    public List<MediaItem> getDataSource() {
        return mediaList;
    }

    /**
     * 控制竖屏播放比例
     */
    public void setRatioNoRender(RatioType ratioType) {
        if (mediaConfig.getRatioType() == ratioType) {
            return;
        }
        mediaConfig.setRatioType(ratioType);
        ConstantMediaSize.ratioType = ratioType;
        calcPreviewRatio(mWidth, mHeight);

    }

    /**
     * 控制竖屏播放比例
     */
    public void setRatio(RatioType ratioType) {
        if (mediaConfig.getRatioType() == ratioType) {
            return;
        }
        boolean isplayer = isPlaying;
        pause();
        mediaConfig.setRatioType(ratioType);
        ConstantMediaSize.ratioType = ratioType;
        calcPreviewRatio(mWidth, mHeight);
        queueEvent(() -> {
            renderBus.onSurfaceChanged(offsetX, offsetY, showWidth, showHeight, mWidth, mHeight);
            renderBus.mediaPrepare();
            if (ThemeHelper.isChangeThemeReset(ConstantMediaSize.themeType)) {
                seekTo(0);
            }
            if (isplayer) {
                resume();
            } else {
                requestRender();
            }
        });
    }


    /**
     * 是否纯色
     * 十六进制color转rgb数组本别除255
     * <p>
     * 16进制color
     */
    public void setPureColor(BGInfo bgInfo, boolean isRender) {
        mediaConfig.setBGInfo(bgInfo);
        queueEvent(() -> {
            renderBus.setColorInfo(bgInfo);
            if (!isPlaying && isRender) {
                noTransitionDraw();
            }
        });
    }

    /**
     * 类似切换背景的时候，要预览实时效果，
     * 现在默认加上了转场，导致很多情况达不到实时预览
     */
    private void noTransitionDraw() {
        renderBus.noTransitionDraw();
    }

    /**
     * 预览元素片段
     *
     * @param index
     */
    public void previewMediaItem(int index) {
        if (index >= mediaList.size() || index < 0) {
            return;
        }
        curIndex = index;
        currentMediaItem = mediaList.get(index);
        renderBus.previewMediaItem(curIndex, !isPlaying);
        audioPlayer.seekTo(getCurPosition());
        recordPlayer.seekTo(getCurPosition());
    }

    /**
     * 播放转场
     * seekto前一片段的尾部，然后走正常的片段切换
     * 旧逻辑：如果preIndex是视频片段，则不进行seek，直接用黑屏过渡方案
     * 新逻辑：无论如何都seek
     */
    public void previewTransition(int index, TransitionFilter transitionFilter) {
        if (index < 1 /*|| mediaList.get(index - 1).getDuration() < ConstantMediaSize.TRANSITION_PREVIEW*/) {
            return;
        }
        mediaList.get(index).setTransitionFilter(transitionFilter);
        //seekto到上一个片段
        curIndex = index - 1;
        currentMediaItem = mediaList.get(curIndex);
        renderBus.previewTransition(curIndex);
        isPlaying = true;
    }

    /**
     * 预览滤镜效果
     */
    public void previewAfilter() {
        renderBus.previewAfilter();
        requestRenderNoprogress();
    }

    /**
     * 判断是否是两个视频再进行切换，如果是两个视频进行切换的话，
     *
     * @return
     */
    private boolean isTwoVideoSwitch(int curIndex, int lastIndex) {
        if (curIndex >= mediaList.size() || lastIndex >= mediaList.size()) {
            return false;
        }
        if (mediaList.get(curIndex).isVideo() && mediaList.get(lastIndex).isVideo()) {
            return true;
        }
        return false;
    }


    /**
     * 改变滤镜强度
     */
    public void setFilterStrength(int progress) {
        if (ObjectUtils.isEmpty(currentMediaItem.getAfilter())) {
            return;
        }
        renderSingleTasks.add(RenderSingleType.FILTER);
        currentMediaItem.getAfilter().setStrength((float) progress / 100f);
        requestRender();
    }


    /**
     * 移除转场预览状态
     */
    public void removeTransitionView() {
        renderBus.removeTransition();

    }

    public void setDoodle(List<DoodleItem> list) {
        renderBus.setDoodle(list);
    }

    /**
     * 每次做固定九十度的旋转操作
     */
    public void doMediaRotate() {
        int angle;
        if (currentMediaItem.getMediaMatrix() == null) {
            angle = 90;
            currentMediaItem.setMediaMatrix(new MediaMatrix());
        } else {
            angle = (currentMediaItem.getMediaMatrix().getAngle() + 90) % 360;
        }
        currentMediaItem.getMediaMatrix().setAngle(angle);
        renderBus.doMediaRotate();
        noTransitionDraw();
    }

    /**
     * 每次做固定九十度的旋转操作
     */
    public void doMediaMirror() {
        if (currentMediaItem.getMediaMatrix() == null) {
            currentMediaItem.setMediaMatrix(new MediaMatrix());
        }
        currentMediaItem.getMediaMatrix().setMirror(!currentMediaItem.getMediaMatrix().isMirror());
        renderBus.locationRender(currentMediaItem);
        noTransitionDraw();
    }

    /**
     * 对media进行缩放，平移操作
     *
     * @param scale
     * @param x
     * @param y     跟旋转一样，因为纹理坐标和屏幕坐标是相反的，作用y为相反值
     */
    public void setScaleTranlation(float scale, int x, int y, int originX, int originY) {
        renderBus.setScaleTranlation(scale, x, y, originX, originY);
    }


    /**
     * 获取当前glsurfaceview的截图
     *
     * @return
     */
    public void getScreenShot(ScreenShotCallback screenShotCallback) {
        this.screenShotCallback = screenShotCallback;
        isTakeScreenShot = true;
    }

    /**
     * 截图方法
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @param gl
     * @return
     */
    private Bitmap createBitmapFromGLSurface(int x, int y, int w, int h, GL10 gl) {
        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);
        try {
            gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            return null;
        }
        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_4444);
    }

    private boolean isResumePlay;

    /**
     * 从子页面回来
     */
    public void onResultResume(boolean isPlay) {
        curIndex = 0;
        isResumePlay = isPlay;
        currentMediaItem = mediaList.get(0);
        renderBus.seekToEnd();
        renderBus.setSeekTo(curIndex, 0, true, this::onResultResumePlay);
        if (isPlay && currentMediaItem.isImage()) {
            resume();
        }
        try {
            audioPlayer.seekTo(0);
            recordPlayer.seekTo(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 添加渲染环境任务
     *
     * @return
     */
    Unit onResultResumePlay() {
        if (isResumePlay) {
            resume();
        }
        return Unit.INSTANCE;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    public void seekAudio(int progress) {
        seekTo(progress);
        audioPlayer.seekTo(progress);
        recordPlayer.seekTo(progress);
    }

    /**
     * 加了生命周期，竟然把闪屏问题修复了
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 添加单个音频的情况，覆盖整个播放周期
     *
     * @param audio
     */
    public void setSingleAudio(AudioMediaItem audio) {
        if (ObjectUtils.isEmpty(audio)) {
            return;
        }
        if (ObjectUtils.isEmpty(audio.getDurationInterval()) || audio.getDurationInterval().getInterval() == 0) {
            audio.setDurationInterval(new DurationInterval(0, totalTime));
        }
        audioPlayer.setSingleAudio(audio);
    }


    /**
     * 添加多个音频情况
     * 并且计算外音跨域，或者原音内包含多个外音问题
     *
     * @param list
     */
    public void setMultiAudio(List<AudioMediaItem> list, List<AudioMediaItem> recordList) {
        if (!ObjectUtils.isEmpty(list)) {
            computeTotalTime();
            for (AudioMediaItem audioMediaItem : list) {
                if (ObjectUtils.isEmpty(audioMediaItem.getDurationInterval()) || audioMediaItem.getDurationInterval().getInterval() == 0) {
                    audioMediaItem.setDurationInterval(new DurationInterval(0, totalTime));
                }
            }
        }
        audioPlayer.setMultiAudio(list);
        recordPlayer.setMultiAudio(recordList);

    }

    public void updateMultiMusic(List<AudioMediaItem> list) {
        audioPlayer.updateMultiAudio(list);
    }

    public void updateRecord(List<AudioMediaItem> list) {
        recordPlayer.updateMultiAudio(list);
    }

    public void updateRecordList(List<AudioMediaItem> list) {
        recordPlayer.updateMultiAudio(list);
    }


    /**
     * 获取存储配置，后续可通过外部设置mediapreview的mediacofig
     *
     * @return
     */
    public MediaConfig getMediaConfig() {
        mediaConfig.setCurrentDuration(currentPosition);
        mediaConfig.setDuration(getTotalTime());
        mediaConfig.setAutoPlay(false);
        if (currentMediaItem != null) {
            mediaConfig.setProjectId(currentMediaItem.getProjectId());
        }
        return mediaConfig;
    }

    public void preViewRotate(boolean flag) {
        renderBus.preViewRotate(flag);
    }


    public void setAudioFadingPlay(boolean isFading) {
        if (totalTime > ConstantMediaSize.FADE_TIME) {
            audioPlayer.setFading(isFading);
            mediaConfig.setFading(isFading);
        }

    }


    public void setAudioPlayCallback(AudioPlayer.AudioCallback audioCallback) {
        audioPlayer.setAudioPlayCallback(audioCallback);
    }

    public boolean isThemeChanging() {
        return isThemeChanging;
    }

    public void setThemeChanging(boolean themeChanging) {
        isThemeChanging = themeChanging;
    }

    public interface ChangeThemeCallBack {
        void finish();

        void failed();
    }

    /**
     * 主题切换
     */
    public void changeTheme(List<MediaItem> dataSource, List<AudioMediaItem> list, List<AudioMediaItem> recordlist, ChangeThemeCallBack callBack) {
        changeTheme(dataSource, list, recordlist, callBack, true);
    }

    public void changeTheme(List<MediaItem> dataSource, List<AudioMediaItem> list, List<AudioMediaItem> recordlist, ChangeThemeCallBack callBack, boolean resume) {
        curIndex = 0;
        currentMediaItem = mediaList.get(0);
        updateDataSource(dataSource);
        renderBus.locationRender(currentMediaItem);
        postDelayed(() -> queueEvent(() -> {
            setMultiAudio(list, recordlist);
            try {
                renderBus.changeTheme(getMediaConfig());
                audioPlayer.seekTo(0);
                recordPlayer.seekTo(0);
                if (resume) {
                    resume();
                }
                LogUtils.i(TAG, "changeTheme->isThemeChanging->queueEvent:" + isThemeChanging);
                callBack.finish();
            } catch (Exception e) {
                e.printStackTrace();
                callBack.failed();
            } finally {
                isThemeChanging = false;
            }
        }), 200);
    }


    /**
     * 合成终止，窗口变化了
     */
    public void resetWindow() {
        ConstantMediaSize.canvasWidth = mWidth;
        ConstantMediaSize.canvasHeight = mHeight;
        ConstantMediaSize.showViewWidth = showWidth;
        ConstantMediaSize.showViewHeight = showHeight;
        ConstantMediaSize.offsetX = offsetX;
        ConstantMediaSize.offsetY = offsetY;
        LogUtils.i(TAG, "resetWindow:mWidth" + mWidth + ",mHeight:" + mHeight + ",showWidth:" + showWidth + ",showHeight:" + showHeight);
        requestRender();
    }

//    /**
//     * 根据mediaDrawer获取当前画面的缓存，用于视频和图片的转场
//     *
//     * @return
//     */
//    public GloableDrawer getmOpenglDrawer() {
//        return mOpenglDrawer;
//    }

    /**
     * 设置当前播放视频的音量
     *
     * @param volume
     */
    public void setVideoVolume(float volume) {
        for (MediaItem mediaItem : mediaList) {
            if (mediaItem instanceof VideoMediaItem) {
                ((VideoMediaItem) mediaItem).setVolume(volume);
            }
        }
        if (isCurrentVideo()) {
            renderBus.setVideoVolume(((VideoMediaItem) currentMediaItem).getPlayVolume());
        }
    }

    /**
     * 获取当前视频播放音量
     *
     * @return
     */
    public float getVideoVolume() {
        for (MediaItem mediaItem : mediaList) {
            if (mediaItem instanceof VideoMediaItem) {
                return ((VideoMediaItem) mediaItem).getVolume();
            }
        }
        return 0;
    }

    /**
     * 设置当前播放音乐的音量
     *
     * @param volume
     */
    public void setMusicVolume(float volume) {
        audioPlayer.setVolume(volume);
    }

    /**
     * 设置录音的音量
     *
     * @param volume
     */
    public void setRecordVolume(float volume) {
        recordPlayer.setVolume(volume);
    }

    public boolean isMusicContain() {
        return audioPlayer.isMusicContain();
    }

    /**
     * 获取当前外音的音量
     *
     * @return
     */
    public AudioMediaItem getCurrentAudio() {
        return audioPlayer.getCurrentAudio();
    }

    /**
     * 检查是否属于opengl转场，如果属于该转场则可以自定义mediaitem的转场方案
     *
     * @return
     */
    public boolean checkOpenglTransition() {
        return renderBus.checkSupportTransition();
    }


    /**
     * 设置当前视频播放速度
     *
     * @param speed
     */
    public void setVideoSpeed(float speed) {
        renderBus.setvideoSpeed(speed);
    }

    /**
     * 获取距离结束的时长
     *
     * @return
     */
    public long getEndOffset() {
        return getCurPosition() - totalTime;
    }


    public MediaItem getCurrentMediaItem() {
        return currentMediaItem;
    }


    /**
     * 设置全局粒子系统
     */
    public void changeGlobalParticle(GlobalParticles globalParticles, ParticleDrawerManager manager, PAGNoBgParticle pagNoBgParticle) {
        LogUtils.i("changeParticle", globalParticles.name());
        mediaConfig.setGlobalParticles(globalParticles);
        renderBus.changeGlobalParticle(manager, pagNoBgParticle);
    }


    /**
     * 设置边框
     */
    public void changeInnerBorder(InnerBorder innerBorder) {
        mediaConfig.setInnerBorder(innerBorder);
        renderBus.changeInnerBorder(innerBorder);
        requestRenderNoprogress();
    }


    public void pauseAudio() {
        audioPlayer.pause();
        recordPlayer.pause();
    }

    /**
     * 不更新progress的渲染
     */
    public void requestRenderNoprogress() {
        requestRender();
        isNoProgressFrame = true;
    }


    /**
     * 设置合成分辨率
     */
    public void setResolutionType(ResolutionType resolutionType) {
        mediaConfig.setResolutionType(resolutionType);
    }

    /**
     * 设置显示区域的大小
     */
    public void zoomViewPort(float zoomScale) {
        mediaConfig.getBGInfo().setZoomScale(zoomScale);
        if (renderSingleTasks.contains(RenderSingleType.ZOOM_SCALE)) {
            LogUtils.v("MediaPreviewView", "contains-zoomScale:" + zoomScale);
            return;
        }
        renderSingleTasks.add(RenderSingleType.ZOOM_SCALE);
        queueEvent(() -> {
            renderSingleTasks.remove(RenderSingleType.ZOOM_SCALE);
            renderBus.setZoomScale(mediaConfig.getBGInfo());
            requestRender();
        });
    }
}

