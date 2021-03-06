package com.ijoysoft.mediasdk.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.ThreadPoolMaxThread;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.ColorUtil;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.AudioDuration;
import com.ijoysoft.mediasdk.module.entity.AudioMediaItem;
import com.ijoysoft.mediasdk.module.entity.DoodleItem;
import com.ijoysoft.mediasdk.module.entity.DurationInterval;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaMatrix;
import com.ijoysoft.mediasdk.module.entity.MediaType;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.entity.VideoMediaItem;
import com.ijoysoft.mediasdk.module.mediacodec.PhoneAdatarList;
import com.ijoysoft.mediasdk.module.opengl.MediaDrawer;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;
import com.ijoysoft.mediasdk.module.playControl.AudioPlayer;
import com.ijoysoft.mediasdk.module.playControl.ImagePlayer;
import com.ijoysoft.mediasdk.module.playControl.MediaConfig;
import com.ijoysoft.mediasdk.module.playControl.MediaPlayerWrapper;
import com.ijoysoft.mediasdk.module.playControl.MediaSeriesFrameCallBack;
import com.ijoysoft.mediasdk.module.playControl.VideoPlayer;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.THUMTAIL_HEIGHT;
import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.THUMTAIL_WIDTH;

/**
 * ???????????????imageplayer??????????????????VideoPlayer
 * <p>
 * ???????????????transitionfilter?????????????????????
 * <p>
 * pre ?????????????????????????????????????????????????????????????????????
 */
public class MediaPreviewView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private static final String TAG = "MediaPreviewView";
    private MediaPlayerWrapper mMediaPlayer;
    private MediaDrawer mOpenglDrawer;
    private VideoPlayer mVideoPlayer;
    private ImagePlayer mImagePlayer;
    private List<MediaItem> playList = new ArrayList<>();
    private int curIndex;
    private MediaItem currentMediaItem;
    // ??????????????????????????????????????????????????????????????????????????????????????????.???????????????????????????
    private boolean isSwitching;
    private int totalTime;
    private boolean isLooper;
    private boolean isPlaying;
    private MediaPreviewCallback mediaPreviewCallback;
    private MediaPreviewChangeCallback mMediaPreviewChangeCallback;
    private AudioPlayer audioPlayer;
    private int audioCount = 0; // ????????????????????????
    private int currentPosition;
    private RatioType ratioType;
    private int showWidth;
    private int showHeight;
    private int screenW;
    private int offsetX, offsetY;
    private boolean isAutoPlay;
    private boolean isMainControl;
    private ScreenShotCallback screenShotCallback;
    private boolean isTakeScreenShot;
    private Handler handler = new Handler();
    private String rgbaHexString;
    private Surface surface;
    private boolean isMusicFading;
    private boolean isClipTrimResume;
    private boolean isResuming;
    /**
     * ??????????????????
     */
    private MediaPlayerWrapper.IMediaCallback videoCallback = new MediaPlayerWrapper.IMediaCallback() {
        @Override
        public void onVideoPrepare() {

        }

        @Override
        public void onVideoStart() {
        }

        @Override
        public void onVideoPause() {
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            playNextMedia();
        }

        @Override
        public void onVideoChanged(final MediaItem info) {
            queueEvent(new Runnable() {
                @Override
                public void run() {
                    mVideoPlayer.onVideoChanged(info);
                }
            });
        }
    };

    // ??????????????????
    private ImagePlayer.ImageCallback imageCallback = new ImagePlayer.ImageCallback() {
        @Override
        public void start() {

        }

        @Override
        public void onComplete() {
            playNextMedia();
        }

        @Override
        public void parse() {

        }

        @Override
        public void render() {
            post(new Runnable() {
                @Override
                public void run() {
                    requestRender();
                }
            });
        }
    };

    private VideoPlayer.VideoDrawerCallback videoDrawerCallback = new VideoPlayer.VideoDrawerCallback() {
        @Override
        public void onPause() {
            pause();
        }
    };

    public MediaPreviewView(Context context) {
        super(context, null);
    }

    public MediaPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);// when the surface is created, or when {@link #requestRender} is called.
        setPreserveEGLContextOnPause(false);// ?????????true?????????
        setCameraDistance(100);
        mOpenglDrawer = new MediaDrawer();
        mVideoPlayer = new VideoPlayer();
        mVideoPlayer.setVideoDrawerCallback(videoDrawerCallback);

        // ?????????Drawer???VideoPlayer
        mMediaPlayer = MediaPlayerWrapper.getInstance();
        mMediaPlayer.setOnCompletionListener(videoCallback);
        mImagePlayer = new ImagePlayer(imageCallback);
        audioPlayer = new AudioPlayer();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        LogUtils.i(TAG, "onSurfaceCreated....");
        if (currentMediaItem == null) {
            return;
        }
        mOpenglDrawer.onSurfaceCreated();
        mVideoPlayer.onSurfaceCreated();
        SurfaceTexture surfaceTexture = mVideoPlayer.getSurfaceTexture();
        // ???????????????????????????surface???gl????????????????????????
        surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                requestRender();
            }
        });
        surface = new Surface(surfaceTexture);
        mMediaPlayer.setSurface(surface);
        mImagePlayer.onSurfaceCreated();
        mMediaPlayer.prepare();
        if (isAutoPlay) {
            start();
        } else {
            if (isCurrentVideo()) {
                updateTransitionAndAfilterVideo();
                mMediaPlayer.bindSurface();
                mMediaPlayer.seekTo(0);
                if (PhoneAdatarList.checkAlwayAutoPlayVideo()) {
                    mMediaPlayer.start(0);
                }
            }
        }
    }

    // ??????????????????
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        LogUtils.i(TAG, "onSurfaceChanged:" + width + "," + height);
        ConstantMediaSize.currentScreenHeight = height;
        ConstantMediaSize.currentScreenWidth = width;
        calcPreviewRatio(width, height);
        if (currentMediaItem == null) {
            return;
        }
        LogUtils.i(TAG,
                "showWidth:" + showWidth + ",showHeight:" + showHeight + ",offsetX:" + offsetX + ",offsetY:" + offsetY);
        /**
         * ???????????????????????????????????????????????????????????????????????????
         */
        // if (isMainControl) {
        // FBOManager.getInstance().createFBo(showWidth, showHeight);
        // isMainControl = false;
        // }
        mOpenglDrawer.onSurfaceChanged(offsetX, offsetY, showWidth, showHeight);
        mImagePlayer.onSurfaceChanged(offsetX, offsetY, showWidth, showHeight);
        mVideoPlayer.onSurfaceChanged(offsetX, offsetY, showWidth, showHeight);
        screenW = width;
        if (!isAutoPlay && !isCurrentVideo()) {
            mImagePlayer.setCurrentMediaItem(currentMediaItem);
            mImagePlayer.playPrepare();
        }
        if (mediaPreviewCallback instanceof MediaPreviewLayoutCallback) {
            ((MediaPreviewLayoutCallback) mediaPreviewCallback).mediaPreviewLayout();
        }
    }

    // ??????????????????????????????????????????????????????????????????
    @Override
    public void onDrawFrame(GL10 gl) {
        if (currentMediaItem == null) {
            return;
        }
        currentPosition = getCurPosition();
        // LogUtils.i(TAG, "currentPosition:" + currentPosition + ",ID:" + currentMediaItem.getEqualId());
        if (!isCurrentVideo()) {
            mImagePlayer.onDrawFrame();
            mOpenglDrawer.setInputTexture(mImagePlayer.getOutputTexture());
        } else {
            mVideoPlayer.setCurrentVideoDuration(getCurVideoPosition());
            mVideoPlayer.onDrawFrame();
            mOpenglDrawer.setInputTexture(mVideoPlayer.getOutputTexture());
        }
        mOpenglDrawer.setCurrentDuration(currentPosition);
        mOpenglDrawer.onDrawFrame();
        audioCount++;
        // ????????????
        if (audioCount % 3 == 0) {
            audioPlayer.ondrawFramePlay(currentPosition);
            if (audioCount >= 3333) {
                audioCount = 0;
            }
        }
        if (mediaPreviewCallback != null)
            mediaPreviewCallback.progress(currentPosition);

        if (isTakeScreenShot) {
            isTakeScreenShot = false;
            if (screenShotCallback != null) {
                Bitmap bitmap = createBitmapFromGLSurface(offsetX, offsetY, showWidth, showHeight, gl);
                screenShotCallback.result(bitmap);
            }
        }
        // LogUtils.i(TAG, "onDrawFrame:" + "end.....");
    }

    /**
     * ???????????????????????????????????????(16:9),??????(9:16),??????(1:1)
     * ???????????????????????????4:3???1:1??????????????????
     * ?????????????????????????????????9:16??????????????????16:9????????????
     */
    public void calcPreviewRatio(int screenWidth, int screenHeight) {
        if (ratioType == null) {
            ratioType = RatioType._1_1;
        }
        switch (ratioType) {
            case NONE:
                showWidth = showHeight = screenHeight;
                offsetX = offsetY = 0;
                break;
            case _1_1:
                showWidth = showHeight = screenHeight;
                offsetX = offsetY = 0;
                break;
            case _9_16:
                showHeight = screenHeight;
                showWidth = (int) (0.5625 * showHeight);
                offsetX = (screenWidth - showWidth) / 2;
                offsetY = 0;
                break;
            case _16_9:
                showWidth = screenWidth;
                showHeight = (int) (0.5625 * showWidth);
                offsetY = (screenWidth - showHeight) / 2;
                offsetX = 0;
                break;
            case _3_4:
                showHeight = screenHeight;
                showWidth = (int) (0.75 * showHeight);
                offsetX = (screenWidth - showWidth) / 2;
                offsetY = 0;
                break;
            case _4_3:
                showWidth = screenWidth;
                showHeight = (int) (0.75 * showWidth);
                offsetY = (screenWidth - showHeight) / 2;
                offsetX = 0;
                break;
        }
        ConstantMediaSize.showViewWidth = showWidth;
        ConstantMediaSize.showViewHeight = showHeight;
        ConstantMediaSize.offsetX = offsetX;
        ConstantMediaSize.offsetY = offsetY;
    }

    /**
     * ??????media?????????????????????
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
     * ????????????
     *
     * @param
     */
    private void startPlayImage() {
        mImagePlayer.setCurrentMediaItem(currentMediaItem);
        queueEvent(new Runnable() {
            @Override
            public void run() {
                long l = System.currentTimeMillis();
                mImagePlayer.playPrepare();
//                Log.i("test", "one:" + (System.currentTimeMillis() - l));
                mVideoPlayer.changeFilter(currentMediaItem.getAfilter());
                isSwitching = false;
                mImagePlayer.playResume();
            }
        });
    }

    /**
     * isPlaying now
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * ????????????
     */
    public void pause() {
        isPlaying = false;
        if (currentMediaItem == null) {
            return;
        }
        audioPlayer.pause();
        if (mediaPreviewCallback != null) {
            mediaPreviewCallback.parse();
        }
        if (isCurrentVideo()) {
            mMediaPlayer.pause();
            return;
        } else {
            mImagePlayer.playPause();
        }
    }

    /**
     * start play video
     */
    public void start() {
        isPlaying = true;
        if (currentMediaItem == null) {
            return;
        }
        if (isCurrentVideo()) {
            updateTransitionAndAfilterVideo();
            mMediaPlayer.start(0);
        } else {
            startPlayImage();
        }
        audioPlayer.resume();
        if (mediaPreviewCallback != null) {
            mediaPreviewCallback.start();
        }
    }

    public void resume() {
        if (currentMediaItem == null) {
            return;
        }
        isPlaying = true;
        audioPlayer.resume();
        if (isCurrentVideo()) {
            mMediaPlayer.start(0);
        } else {
            mImagePlayer.playResume();
        }
    }

    public void trimResume() {
        if (currentMediaItem == null) {
            return;
        }
        isPlaying = true;
        audioPlayer.resume();
        if (isCurrentVideo()) {
            mMediaPlayer.start(mMediaPlayer.getCurrentPosition());
        }
    }

    /**
     * ????????????
     */
    public void onDestroy() {
        if (currentMediaItem == null) {
            return;
        }
        mImagePlayer.onDestroy();
        audioPlayer.onDestroy();
        mOpenglDrawer.onDestroy();
        mVideoPlayer.onDestroy();
        mediaPreviewCallback = null;
        if (playList != null) {
            for (MediaItem mediaItem : playList) {
                if (mediaItem.getAfilter() != null) {
                    mediaItem.getAfilter().destroy();
                }
                if (mediaItem.getTransitionFilter() != null) {
                    mediaItem.getTransitionFilter().onDestroy();
                }
            }
        }

        // mMediaPreviewChangeCallback = null;
        // mMediaPlayer.pause();
        // FBOManager.getInstance().onDestroy();
    }

    public void onMainDestroy() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }

    /**
     * ???????????????????????????,????????????.getCurrentPosition>2?????????
     * ???????????????????????????transitionfilter
     */
    public void setDataSource(List<MediaItem> dataSource, List<DoodleItem> doodleItems, boolean justPlay,
                              MediaConfig mediaConfig, boolean changePath) {
        if (dataSource == null) {
            return;
        }
        isAutoPlay = justPlay;
        curIndex = 0;
        playList = dataSource;
        for (int i = 0; i < dataSource.size(); i++) {
            if (i == 0) {
                currentMediaItem = dataSource.get(i);
                if (mOnChangeCurItemListener != null) {
                    mOnChangeCurItemListener.onChangeCurItem(currentMediaItem);
                }
                mImagePlayer.setDataSource(playList, currentMediaItem);
                if (currentMediaItem instanceof VideoMediaItem) {
                    mVideoPlayer.setVideoMediaItem(currentMediaItem);
                    LogUtils.i(TAG, "mOpenglDrawer.setVideoFrameRatio:" + currentMediaItem.getWidth() + ","
                            + currentMediaItem.getHeight());
                }
            }
        }
        if (changePath) {
            mMediaPlayer.setDataSource(dataSource);
        } else {
            mMediaPlayer.setOriginDataSource(dataSource);
        }
        mOpenglDrawer.setDoodle(doodleItems);
        computeTotalTime();
        if (mediaConfig != null) {
            if (mediaConfig.getRatioType() != null) {
                ratioType = mediaConfig.getRatioType();
            }
            setAudioFadingPlay(mediaConfig.isFading());
            if (!ObjectUtils.isEmpty(mediaConfig.getRgba())) {
                setPureColor(true, mediaConfig.getRgba());
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param dataSource
     * @param doodleItems
     * @param justPlay
     * @param mediaConfig
     * @param changePath
     * @param isMainControl
     */
    public void setDataSource(List<MediaItem> dataSource, List<DoodleItem> doodleItems, boolean justPlay,
                              MediaConfig mediaConfig, boolean changePath, boolean isMainControl) {
        setDataSource(dataSource, doodleItems, justPlay, mediaConfig, changePath);
        this.isMainControl = isMainControl;
    }

    public void setClipDataSource(List<MediaItem> dataSource) {
        mMediaPlayer.setDataSourceUpdate(dataSource);
    }

    /**
     * ????????????
     *
     * @param mediaItem
     */
    public void previewClipMediaItem(MediaItem mediaItem) {
        mImagePlayer.isPreviewTransition(false);
        mImagePlayer.setTransitionShow(false);
        mVideoPlayer.isPreviewTransition(false);
        mVideoPlayer.setTransitionShow(false);
        curIndex = 0;
        currentMediaItem = mediaItem;
        playList.clear();
        playList.add(mediaItem);
        computeTotalTime();
        if (currentMediaItem.getMediaType() == MediaType.VIDEO) {
            mVideoPlayer.setRotation(currentMediaItem.getRotation());
            mMediaPlayer.wholeSeekTo(currentMediaItem, 0);
            updateTransitionAndAfilterVideo();
            requestRender();
        } else {
            // ????????????
            updateTransitionAndAfilterImage();
            requestRender();
        }
    }

    /**
     * ??????????????????mediaplayer???surface?????????
     */
    public void clearMediaPlayerStatus() {
        mMediaPlayer.clearMediaPlayerStatus();
        mMediaPlayer.setOnCompletionListener(null);
    }

    /**
     * ????????????????????????????????????????????????????????????
     *
     * @param dataSource
     */
    public void updateDataSource(List<MediaItem> dataSource) {
        if (dataSource == null || dataSource.size() == 0) {
            return;
        }
        playList = dataSource;
        // curIndex = 0;
        // currentMediaItem = dataSource.get(curIndex);
        mImagePlayer.setDataSource(playList, currentMediaItem);
        if (isClipTrimResume) {
            mMediaPlayer.setSourceUpdate(playList);
            isClipTrimResume = false;
        } else {
            mMediaPlayer.setDataSourceUpdate(playList);
        }
        mMediaPlayer.setOnCompletionListener(videoCallback);
    }

    public boolean isClipTrimResume() {
        return isClipTrimResume;
    }

    public void setClipTrimResume(boolean clipTrimResume) {
        isClipTrimResume = clipTrimResume;
    }

    /**
     * ??????????????????????????????
     */
    public void playNextMedia() {
        Log.i(TAG, "playNextMedia");
        if (isResuming) {
            return;
        }
        if (!hasNext()) {
            if (isLooper) {
                curIndex = 0;
                playNextMedia();
                return;
            }
            audioPlayer.stop();
            if (mediaPreviewCallback != null) {
                mediaPreviewCallback.onFinish();
            }
            isPlaying = false;
            return;
        }
        curIndex++;
        switchPlayer();
    }

    public int getCurIndex() {
        return curIndex;
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    public boolean hasNext() {
        if (curIndex >= playList.size() - 1) {
            return false;
        }
        return true;
    }

    // ???????????????????????????????????????????????????
    public void seekTo(int progress) {
        long duration = 0;
        for (int i = 0; i < playList.size(); i++) {
            duration += playList.get(i).getTempDuration();
            if (duration > progress) {
                long ti = progress - (duration - playList.get(i).getTempDuration());
                LogUtils.i(TAG, "currentIndex:" + curIndex + ",index:" + i + ",ti:" + ti + ", progress==" + progress);
                setSeekTo(i, (int) ti);
                break;
            }
        }
    }

    // ???????????????????????????????????????????????????
    public void seekToIndex(int index) {
        setSeekTo(index, 0);
    }

    /**
     * seek???????????????????????????????????????????????????????????????audioplayer
     *
     * @param progress
     */
    public void seekToEnd(int progress) {
        long duration = 0;
        for (int i = 0; i < playList.size(); i++) {
            duration += playList.get(i).getTempDuration();
            if (duration > progress) {
                long ti = progress - (duration - playList.get(i).getTempDuration());
                LogUtils.i(TAG, "ti - progress:" + ti);
                setSeekTo(i, (int) ti);
                break;
            }
        }
        audioPlayer.seekTo(progress);
    }

    /**
     * ??????????????????
     *
     * @param i
     * @param ti
     */
    private void setSeekTo(int i, final int ti) {
        if (currentMediaItem == null) {
            return;
        }
        if (mMediaPlayer.isPlaying() || mImagePlayer.isPlaying()) {
            pause();
        }
        LogUtils.i(TAG, "setSeekTo:ti:" + ti);
        if (curIndex == i) {// ??????media
            if (currentMediaItem.getMediaType() == MediaType.VIDEO) {
                mMediaPlayer.seekTo(ti);
                LogUtils.i(TAG,
                        "getCurrentPosition:" + mMediaPlayer.getCurrentPosition() + "," + mMediaPlayer.toString());
            } else {
                mImagePlayer.seekTo(ti);
            }
        } else {// ??????media
            LogUtils.i(TAG, "setSeekTo:" + ti + ",i=" + i + ",curIndex=" + curIndex);
            curIndex = i;
            currentMediaItem = playList.get(i);
            if (currentMediaItem.getMediaType() == MediaType.VIDEO) {
                updateTransitionAndAfilterVideo();
                mMediaPlayer.wholeSeekTo(currentMediaItem, ti);
                if (mVideoPlayer.getSurfaceTexture() != null) {
                    queueEvent(new Runnable() {
                        @Override
                        public void run() {
                            mVideoPlayer.getSurfaceTexture().updateTexImage();
                        }
                    });
                }
                // requestRender();
            } else {
                mImagePlayer.seekTo(ti);
                // ?????????????????????
                updateTransitionAndAfilterImage();
            }
        }
    }

    /**
     * ??????????????????????????????????????????????????????
     * ????????????????????????????????????????????????????????????
     */
    private void updateTransitionAndAfilterImage() {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (isCurrentVideo()) {
                    if (currentMediaItem.getAfilter() != null) {
                        mVideoPlayer.changeFilter(currentMediaItem.getAfilter());
                    }
                } else {
                    mImagePlayer.setCurrentMediaItem(currentMediaItem);
                    mImagePlayer.playPrepare();
                }
            }
        });
    }

    private void updateTransitionAndAfilterVideo() {
        if (currentMediaItem.getMediaType() != MediaType.VIDEO) {
            return;
        }
        queueEvent(new Runnable() {
            @Override
            public void run() {
                mVideoPlayer.setVideoFrameRatioUpdate(currentMediaItem);
                if (currentMediaItem instanceof VideoMediaItem) { // TODO: 2019/7/29 ????????????????????? ???
                    MediaItem preMediaItem = getPreMediaItem();
                    mVideoPlayer.playPrepare(preMediaItem);
                    if (currentMediaItem.getAfilter() != null) {
                        mVideoPlayer.changeFilter(currentMediaItem.getAfilter());
                    } else {
                        mVideoPlayer.changeFilter(MagicFilterType.NONE);
                    }
                }

            }
        });
    }

    private MediaItem getPreMediaItem() {
        int index = 0;
        for (int i = 0; i < playList.size(); i++) {
            if (playList.get(i) == currentMediaItem) {
                index = i - 1;
                break;
            }
        }
        if (index != -1) {
            return playList.get(index);
        }
        return null;
    }

    /**
     * ???????????????????????????
     *
     * @param
     */
    private void switchPlayer() {
        LogUtils.i(TAG, "switchPlayer-->index:" + curIndex);
        currentMediaItem = playList.get(curIndex);
        if (mMediaPreviewChangeCallback != null) {
            mMediaPreviewChangeCallback.onChange(curIndex);
        }
        LogUtils.i(TAG, "switchPlayer-->equid:" + currentMediaItem.getEqualId());
        if (isCurrentVideo()) {
            isSwitching = false;
            mMediaPlayer.switchPlayer(currentMediaItem);
            updateTransitionAndAfilterVideo();
            queueEvent(new Runnable() {
                @Override
                public void run() {
                    mVideoPlayer.getSurfaceTexture().updateTexImage();
                }
            });
            // requestRender();
        } else {
            startPlayImage();
        }
    }

    /**
     * ???????????????????????? ms
     *
     * @return
     */
    public int getCurPosition() {
        int position = 0;
        // TODO: 2019/7/16
        if (playList.isEmpty()) {
            return 0;
        }
        for (int i = 0; i < curIndex; i++) {
            position += playList.get(i).getTempDuration(); // TODO ????????????
        }
        // LogUtils.i(TAG, "getCurPosition->position-start:" + position);
        int currentOffet = currentMediaItem.getMediaType() == MediaType.VIDEO ? mMediaPlayer.getCurrentPosition()
                : mImagePlayer.getCurrentPostion();
        // LogUtils.i(TAG, "getCurPosition->currentOffet:" + currentOffet);
        position += currentOffet;
        // LogUtils.i(TAG, "getCurPosition->position:" + position);
        return position;
    }

    public int getCurVideoPosition() {
        if (isCurrentVideo()) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * ?????????????????????????????????
     *
     * @param curIndex
     * @return
     */
    public int getCurVideoDuration(int curIndex) {
        return (int) playList.get(curIndex).getTempDuration();
    }

    /**
     * ????????????????????????????????????????????????????????????4s??????
     */
    private void computeTotalTime() {
        for (MediaItem mediaItem : playList) {
            totalTime += mediaItem.getTempDuration();
        }
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
     * ??????????????????????????????
     *
     * @return
     */
    public int getUpateTotalTime() {
        int tempTotalTime = 0;
        for (MediaItem mediaItem : playList) {
            tempTotalTime += mediaItem.getTempDuration();
        }
        totalTime = tempTotalTime;
        return totalTime;
    }

    /**
     * ????????????????????????
     *
     * @param isLooper
     */
    public void setLooper(boolean isLooper) {
        this.isLooper = isLooper;
    }

    /**
     * ??????????????????
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

        void start();

        void parse();

        void progress(int position);// ms

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

    public interface MediaRecordCallback {
        void progress(int progress);

        void finish();
    }

    public interface ScreenShotCallback {
        void result(Bitmap bitmap);
    }

    private MediaRecordCallback mediaRecordCallback;

    public MediaRecordCallback getMediaRecordCallback() {
        return mediaRecordCallback;
    }

    public void setMediaRecordCallback(MediaRecordCallback mediaRecordCallback) {
        this.mediaRecordCallback = mediaRecordCallback;
    }

    public List<MediaItem> getDataSource() {
        return playList;
    }

    /**
     * ????????????????????????
     */
    public void setRatio(RatioType ratioType) {
        LogUtils.i(TAG, "setRatio--" + ratioType.name());
        this.ratioType = ratioType;
        calcPreviewRatio(screenW, screenW);
        queueEvent(new Runnable() {
            @Override
            public void run() {
                mOpenglDrawer.onSurfaceChanged(offsetX, offsetY, showWidth, showHeight);
                mImagePlayer.onSurfaceChanged(offsetX, offsetY, showWidth, showHeight);
                mImagePlayer.playPrepare();
                mVideoPlayer.onSurfaceChanged(offsetX, offsetY, showWidth, showHeight);
                if (!isPlaying) {
                    requestRender();
                }
            }
        });
    }

    /**
     * ????????????????????????
     */
    public void setRatioLajiPhone(RatioType ratioType, int w, int h) {
        this.ratioType = ratioType;
        calcPreviewRatio(w, h);
        queueEvent(new Runnable() {
            @Override
            public void run() {
                mOpenglDrawer.onSurfaceChanged(offsetX, offsetY, showWidth, showHeight);
                mImagePlayer.onSurfaceChanged(offsetX, offsetY, showWidth, showHeight);
                mImagePlayer.playPrepare();
            }
        });
    }

    /**
     * ????????????
     * ????????????color???rgb???????????????255
     *
     * @param flag
     * @param hexColor 16??????color
     */
    public void setPureColor(boolean flag, String hexColor) {
        hexColor = hexColor == null ? "#FFFFFFFF" : hexColor;
        if (hexColor.length() == 7) {
            hexColor = hexColor.replace("#", "#FF");
        }
        LogUtils.i(TAG, hexColor);
        try {
            int[] rgba = ColorUtil.hex2Rgb(hexColor);
            mImagePlayer.setIsPureColor(flag, rgba);
            mVideoPlayer.setPureColor(flag, rgba);
            this.rgbaHexString = "";
            if (flag) {
                this.rgbaHexString = hexColor;
            }
            if (!isCurrentVideo()) {
                queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        mImagePlayer.playPrepare();
                        requestRender();
                    }
                });
            }

            if (!isPlaying && isCurrentVideo()) {
                if (isCurrentVideo()) {
                    requestRender();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ??????????????????
     *
     * @param index
     */
    public void previewMediaItem(int index) {
        mImagePlayer.isPreviewTransition(false);
        mImagePlayer.setTransitionShow(false);
        mVideoPlayer.isPreviewTransition(false);
        mVideoPlayer.setTransitionShow(false);
        setSeekTo(index, 0);
        // ???????????????????????????????????????????????????????????????????????????queueEvent????????????????????????????????????
        // ???????????????????????????????????????????????????requestRender??????????????????????????????
        if (isCurrentVideo()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestRender();
                }
            }, 200);

        }
    }

    public void previewMediaItem(int index, boolean preview) {
        mImagePlayer.isPreviewTransition(preview);
        mImagePlayer.setTransitionShow(preview);
        mVideoPlayer.isPreviewTransition(preview);
        mVideoPlayer.setTransitionShow(preview);
        setSeekTo(index, 0);
    }

    /**
     * ????????????
     */
    public void previewTransition(int index, TransitionFilter transitionFilter) {
        if (index < 1) {
            return;
        }
        LogUtils.i(TAG, "previewTransition:" + index);
        curIndex = index;
        currentMediaItem = playList.get(index);
        if (transitionFilter == null || transitionFilter.getTransitionType() == TransitionType.NONE
                || currentMediaItem.getDuration() < ConstantMediaSize.TRANSITION_DURATION) {
            return;
        }
        currentMediaItem.setTransitionFilter(transitionFilter);
        if (isCurrentVideo()) {
            queueEvent(new Runnable() {
                @Override
                public void run() {
                    mVideoPlayer.setVideoMedia((VideoMediaItem) currentMediaItem);
                    MediaItem preMediaItem = getPreMediaItem();
                    mVideoPlayer.playPrepare(preMediaItem);
                    mVideoPlayer.isPreviewTransition(true);
                    mVideoPlayer.setCurrentVideoDuration(0);
                }
            });
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
            mMediaPlayer.seekTo(0);
            mMediaPlayer.start(0);
        } else {
            mImagePlayer.isPreviewTransition(true);
            mImagePlayer.resetStart();
            startPlayImage();
        }
    }

    public void playWholeTransition() {
        mVideoPlayer.isPreviewTransition(false);
        mImagePlayer.isPreviewTransition(false);
        mImagePlayer.setTransitionShow(true);
        mVideoPlayer.setTransitionShow(true);
        setSeekTo(curIndex, 0);
        resume();
    }

    /**
     * ??????????????????
     */
    public void previewAfilter() {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (isCurrentVideo()) {
                    mVideoPlayer.changeFilter(currentMediaItem.getAfilter());
                } else {
                    mImagePlayer.playPrepare();
                }
                requestRender();
            }
        });
    }

    /**
     * ???????????????????????????
     */
    public void getMediaDataPerSecondFrame(final MediaSeriesFrameCallBack callBack) {
        if (totalTime < 1000) {
            callBack.callback(0, BitmapUtil.scaleImage(BitmapFactory.decodeFile(playList.get(0).getFirstFramePath()),
                    THUMTAIL_WIDTH, THUMTAIL_HEIGHT));
            return;
        }
        final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        ThreadPoolMaxThread.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                int remainMs = 0;
                int index = 0;
                int frameSize;
                int photoDuration;
                for (int i = 0; i < playList.size(); i++) {
                    LogUtils.d(TAG, "getMediaDataPerSecondFrame----" + i);
                    long start = System.currentTimeMillis();
                    if (i >= playList.size()) {
                        break;
                    }
                    // if (i == 0) {
                    // callBack.callback(0, playList.get(0).getFirstFrame());
                    // index++;
                    // continue;
                    // }
                    if (playList.get(i).getMediaType() == MediaType.PHOTO) {
                        photoDuration = (int) (playList.get(i).getTempDuration() + remainMs);
                        frameSize = photoDuration / 1000;
                        if (photoDuration % 1000 != 0) {
                            remainMs = photoDuration % 1000;
                        } else {
                            remainMs = 0;
                        }
                        for (int j = 0; j < frameSize; j++) {
                            Bitmap originBitmap = BitmapFactory.decodeFile(playList.get(i).getFirstFramePath());
                            int width = originBitmap.getWidth();
                            int height = originBitmap.getHeight();
                            if (width != 0 && height != 0) {
                                bitmap = BitmapUtil.rotateBitmap(
                                        BitmapUtil.centerSquareScaleBitmap(originBitmap, THUMTAIL_WIDTH),
                                        playList.get(i).getRotation());
                            } else {
                                bitmap = BitmapUtil.rotateBitmap(
                                        BitmapUtil.scaleImage(originBitmap, THUMTAIL_WIDTH, THUMTAIL_HEIGHT),
                                        playList.get(i).getRotation());
                            }
                            callBack.callback(index, bitmap);
                            index++;
                        }
                    } else {
                        frameSize = (int) (playList.get(i).getTempDuration() / 1000);
                        if (playList.get(i).getTempDuration() % 1000 != 0) {
                            remainMs += (int) (playList.get(i).getTempDuration() % 1000);
                        }
                        for (int j = 0; j < frameSize; j++) {
                            if (((VideoMediaItem) playList.get(i)).getThumbnails() != null
                                    && ((VideoMediaItem) playList.get(i)).getThumbnails().get(index) != null
                                    && ObjectUtils.isEmpty(playList.get(i).getTrimPath())) {
                                callBack.callback(index, ((VideoMediaItem) playList.get(i)).getThumbnails().get(j));
                                bitmap = ((VideoMediaItem) playList.get(i)).getThumbnails().get(j);
                                index++;
                                continue;
                            }
                            String path = ObjectUtils.isEmpty(playList.get(i).getTrimPath()) ? playList.get(i).getPath()
                                    : playList.get(i).getTrimPath();
                            try {
                                retriever.setDataSource(path);
                                Bitmap videoOriginBitmap = retriever.getFrameAtTime(j * 1000000);
                                if (videoOriginBitmap == null) {
                                    index++;
                                    continue;
                                }
                                int width = videoOriginBitmap.getWidth();
                                int height = videoOriginBitmap.getHeight();
                                if (width != 0 && height != 0) {
                                    bitmap = BitmapUtil.rotateBitmap(
                                            BitmapUtil.centerSquareScaleBitmap(videoOriginBitmap, THUMTAIL_WIDTH),
                                            playList.get(i).getRotation());
                                } else {
                                    bitmap = BitmapUtil.rotateBitmap(
                                            BitmapUtil.scaleImage(videoOriginBitmap, THUMTAIL_WIDTH, THUMTAIL_HEIGHT),
                                            playList.get(i).getRotation());
                                }
                                callBack.callback(index, bitmap);
                                index++;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        LogUtils.i(TAG, "PerSecondFrame-comsume:" + (System.currentTimeMillis() - start));
                    }
                }
                retriever.release();
                if (remainMs / 1000 > 0) {
                    for (int i = 0; i < remainMs / 1000; i++) {
                        callBack.callback(index, bitmap);
                        index++;
                    }
                }
            }
        });
    }

    // private int getIndexBySecond(int duration) {
    //
    // }

    /**
     * ????????????????????????????????????
     *
     * @param callBack
     */
    public void getCoverFrameAndDuration(MediaSeriesFrameCallBack callBack) {
        Bitmap bitmap = BitmapUtil.scaleImage(BitmapFactory.decodeFile(playList.get(0).getFirstFramePath()),
                THUMTAIL_WIDTH, THUMTAIL_HEIGHT);
        callBack.callback(getTotalTime(), BitmapUtil.rotateBitmap(bitmap, playList.get(0).getRotation()));
    }

    /**
     * ???????????????????????????
     *
     * @param callBack
     * @return
     */
    public void getVideoFrameSpecifySecond(final MediaSeriesFrameCallBack callBack) {
        if (!(currentMediaItem instanceof VideoMediaItem)) {
            LogUtils.e(TAG, "current media is not video,can't get video frame");
            return;
        }
        final int second = currentPosition / 1000;
        final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(currentMediaItem.getPath());
        } catch (Exception e) {
            retriever.release();
            return;
        }
        ThreadPoolMaxThread.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap;
                // ?????????
                if (((VideoMediaItem) currentMediaItem).getThumbnails() != null) {
                    callBack.callback(second, ((VideoMediaItem) currentMediaItem).getThumbnails().get(second));
                }
                bitmap = retriever.getFrameAtTime(second * 1000000);
                callBack.callback(second, bitmap);
                retriever.release();
            }
        });
    }

    /**
     * ??????????????????????????????10?????????
     * ???????????????????????????
     * ?????????????????????????????????????????????10s?????????
     *
     * @param callBack
     */
    public void getVideoTrimFrame(final MediaSeriesFrameCallBack callBack) {
        if (totalTime < 10000 && playList.size() > 0) {
            getMediaDataPerSecondFrame(callBack);
            return;
        }
        final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        final long[] frames = new long[10];
        if (totalTime <= 10000) {
            for (int i = 0; i < totalTime / 1000; i++) {
                frames[i] = i;
            }
        } else {
            frames[9] = totalTime / 1000;
            int timeTrim = (totalTime - 2000) / 8000;
            for (int i = 1; i < 9; i++) {
                frames[i] = i * timeTrim;
            }
        }
        LogUtils.i("test", "frames:" + Arrays.toString(frames));
        // ??????????????????
        if (playList.size() == 1) {
            try {
                retriever.setDataSource(currentMediaItem.getPath());
            } catch (Exception e) {
                retriever.release();
                return;
            }
            ThreadPoolMaxThread.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap;
                    for (int i = 0; i < frames.length; i++) {
                        if (i > 0 && frames[i] == 0) {
                            continue;
                        }
                        // bitmap = BitmapUtil.scaleImage(retriever.getFrameAtTime(frames[i] * 1000000), 150,
                        // THUMTAIL_HEIGHT);
                        bitmap = BitmapUtil.aspectScaleBitmap(retriever.getFrameAtTime(frames[i] * 1000000), 150, 200);
                        if (bitmap != null) {
//                            LogUtils.i("test", "i:" + i + ",bitmap-width:" + bitmap.getWidth() + ", height=="
//                                    + bitmap.getHeight());
                            callBack.callback(i, bitmap);
                        }
                    }
                    retriever.release();
                }
            });
            return;
        }
        // ??????????????????
        ThreadPoolMaxThread.getInstance().execute(new Runnable() {

            @Override
            public void run() {
                long duration = 0;
                int currentDuration;
                int index = 0;
                Bitmap bitmap;
                for (int i = 0; i < playList.size(); i++) {
                    try {
                        retriever.setDataSource(playList.get(i).getPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    currentDuration = (int) duration / 1000;
                    duration += playList.get(i).getDuration();
                    LogUtils.i("test", "----currentDuration:" + currentDuration + "," + duration);
                    while (index < 10 && frames[index] <= duration / 1000) {
                        if (index > 0 && frames[index] == 0) {
                            break;
                        }
                        LogUtils.i("test", "current:" + (frames[index] - currentDuration) * 1000000);
                        // bitmap = BitmapUtil.scaleImage(
                        // retriever.getFrameAtTime((frames[index] - currentDuration) * 1000000), THUMTAIL_WIDTH,
                        // THUMTAIL_HEIGHT);
                        bitmap = BitmapUtil.aspectScaleBitmap(
                                retriever.getFrameAtTime((frames[index] - currentDuration) * 1000000), 150, 200);
                        if (bitmap != null) {
                            callBack.callback(index, bitmap);
                        }
                        index++;
                    }
                }
                retriever.release();
            }

        });
    }

    public void setDoodle(List<DoodleItem> list) {
        mOpenglDrawer.setDoodle(list);
    }

    /**
     * ?????????????????????????????????
     */
    public void updateDoodle(final int index, final DoodleItem doodleItem) {

        queueEvent(new Runnable() {
            @Override
            public void run() {
                mOpenglDrawer.updateDoodle(index, doodleItem);
            }
        });
    }

    /**
     * ???????????????????????????????????????
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
        // mImagePlayer.doRotaion(angle);
        // // ?????????????????????
        // if (currentMediaItem.getMediaType() == MediaType.VIDEO) {
        // updateTransitionAndAfilterVideo();
        // } else {
        // updateTransitionAndAfilterImage();
        // }
        // requestRender();
    }

    /**
     * ???media???????????????????????????
     *
     * @param scale
     * @param x
     * @param y     ????????????????????????????????????????????????????????????????????????y????????????
     */
    public void setScaleTranlation(float scale, int x, int y, int originX, int originY) {
        // ????????????matrix
        if (currentMediaItem.getMediaMatrix() == null) {
            currentMediaItem.setMediaMatrix(new MediaMatrix(scale, x, y, originX, originY));
        } else {
            currentMediaItem.getMediaMatrix().setScale(scale);
            currentMediaItem.getMediaMatrix().setOffsetX(x);
            currentMediaItem.getMediaMatrix().setOffsetY(y);
            currentMediaItem.getMediaMatrix().setOriginX(originX);
            currentMediaItem.getMediaMatrix().setOriginY(originY);
        }
        if (isCurrentVideo()) {
            updateTransitionAndAfilterVideo();
        } else {
            updateTransitionAndAfilterImage();
        }
        requestRender();
    }

    /**
     * mediaItem???????????????????????????????????????
     */
    // public void mediaMatrixUpdate() {
    // if (currentMediaItem.getMediaMatrix() != null) {
    // if (currentMediaItem.getMediaMatrix().getScale() != 0 || currentMediaItem.getMediaMatrix().getOffsetX() != 0
    // || currentMediaItem.getMediaMatrix().getOffsetY() != 0) {
    // mOpenglDrawer.setScaleTranlation(currentMediaItem.getMediaMatrix().getScale(),
    // currentMediaItem.getMediaMatrix().getOffsetX(), currentMediaItem.getMediaMatrix().getOffsetY());
    // return;
    // }
    // }
    // mOpenglDrawer.setScaleTranlation(1, 0, 0);
    // }

    /**
     * ???????????????????????????,????????????????????????
     */
    public void trimVideoPreview() {
        curIndex = 0;
        if (!ObjectUtils.isEmpty(currentMediaItem.getTrimPath())) {
            mMediaPlayer.changePath(currentMediaItem.getTrimPath());
        }
        if (currentMediaItem.getVideoCutInterval() == null) {
            currentMediaItem.setVideoCutInterval(new DurationInterval(0, (int) currentMediaItem.getTempDuration()));
        }
        int delay = 600;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        updateTransitionAndAfilterVideo();
                    }
                });
            }
        }, delay);
    }

    /**
     * ????????????glsurfaceview?????????
     *
     * @return
     */
    public void getScreenShot(ScreenShotCallback screenShotCallback) {
        this.screenShotCallback = screenShotCallback;
        isTakeScreenShot = true;
    }

    /**
     * ????????????
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
        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }

    /**
     * ?????????pause?????????onresume????????????glsurfaceview???????????????
     */
    public void onResumePlay() {
        if (ObjectUtils.isEmpty(playList)) {
            return;
        }
        isResuming = true;
        curIndex = 0;
        currentMediaItem = playList.get(0);
        mImagePlayer.setCurrentMediaItem(currentMediaItem);
        audioPlayer.seekTo(0);
        mMediaPlayer.setSurface(surface);
        mMediaPlayer.setOnCompletionListener(videoCallback);
        int delay = 300;
        if (isCurrentVideo()) {
            mMediaPlayer.playReset(currentMediaItem);
            delay = 600;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        if (isCurrentVideo()) {
                            if (currentMediaItem.getAfilter() != null) {
                                mVideoPlayer.changeFilter(currentMediaItem.getAfilter());
                            }
                            updateTransitionAndAfilterVideo();
                            mMediaPlayer.changeCurrentMediaplayer(0);
                            mMediaPlayer.wholeSeekTo(currentMediaItem, 0);
                        } else {
                            seekTo(0);
                            mImagePlayer.playPrepare();
                        }
                        resume();
                        isResuming = false;
                    }
                });
            }
        }, delay);
    }

    public void seekAudio(int progress) {
        seekTo(progress);
        audioPlayer.seekTo(progress);
    }

    public void onResume() {
        mMediaPlayer.setSurface(surface);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        mImagePlayer.playPrepare();
                        if (currentMediaItem.getAfilter() != null) {
                            mVideoPlayer.changeFilter(currentMediaItem.getAfilter());
                        }
                    }
                });
                requestRender();
            }
        }, 300);
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param audio
     */
    public void setSingleAudio(AudioMediaItem audio) {
        audioPlayer.setSingleAudio(audio);
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param audio
     */
    public void updateSingleAudio(AudioMediaItem audio) {
        for (int i = 0; i < playList.size(); i++) {
            if (playList.get(i).getMediaType() == MediaType.VIDEO) {
                VideoMediaItem videoMediaItem = ((VideoMediaItem) playList.get(i));
                videoMediaItem.getVolumeList().clear();
                videoMediaItem.getVolumeList()
                        .add(new AudioDuration(0, (int) videoMediaItem.getTempDuration(), 1 - audio.getVolume()));
            }
        }
    }

    public void deleteSingleAudio() {
        audioPlayer.deleteSingleAudio();
    }

    /**
     * ????????????????????????
     * ??????????????????????????????????????????????????????????????????
     *
     * @param list
     */
    public void setMultiAudio(List<AudioMediaItem> list) {
        audioPlayer.setMultiAudio(list);
        int durationTemp = 0;
        // ????????????
        for (int i = 0; i < playList.size(); i++) {
            if (playList.get(i).getMediaType() == MediaType.VIDEO) {
                checkOriginMusicCross(list, (VideoMediaItem) playList.get(i), durationTemp);
            }
            durationTemp += playList.get(i).getTempDuration();
        }
    }

    public void updateMultiMusic(List<AudioMediaItem> list) {
        audioPlayer.updateMultiAudio(list);
    }

    /**
     * ????????????????????????????????????
     * ??????????????????????????????????????????????????????
     *
     * @param list
     */
    private void checkOriginMusicCross(List<AudioMediaItem> list, VideoMediaItem mediaItem, int currentStart) {
        AudioMediaItem currentAudio;
        int end = currentStart + (int) mediaItem.getTempDuration();
        int audioStart, audioEnd;
        mediaItem.getVolumeList().clear();
        for (int i = 0; i < list.size(); i++) {
            currentAudio = list.get(i);
            audioStart = currentAudio.getDurationInterval().getStartDuration();
            audioEnd = currentAudio.getDurationInterval().getEndDuration();
            if (audioStart < currentStart && audioEnd > currentStart && audioEnd < end) {
                mediaItem.addAudioDuration(new AudioDuration(currentStart, audioEnd, currentAudio.getVolume()));
            } else if (audioStart > currentStart && audioEnd < end) {
                mediaItem.addAudioDuration(new AudioDuration(audioStart, audioEnd, currentAudio.getVolume()));
            } else if (audioStart > currentStart && audioEnd > end) {
                mediaItem.addAudioDuration(new AudioDuration(audioStart, end, currentAudio.getVolume()));
            } else if (audioStart < currentStart && audioEnd > end) {
                mediaItem.addAudioDuration(new AudioDuration(currentStart, end, currentAudio.getVolume()));
            }
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param origin
     * @param music
     */
    public void ChangeOrignAndMusicVolume(float origin, float music) {
        mMediaPlayer.setVolume(origin);
        audioPlayer.setVolume(music);
    }

    /**
     * ??????????????????????????????
     *
     * @param videoVolume
     * @param musicVolum
     */
    public void setVolume(float videoVolume, float musicVolum) {
        mMediaPlayer.setVolume(videoVolume / 100.0f);
        audioPlayer.setVolume(musicVolum / 100.0f);
    }

    /**
     * ????????????????????????????????????????????????mediapreview???mediacofig
     *
     * @return
     */
    public MediaConfig getMediaConfig() {
        MediaConfig.Builder builder = new MediaConfig.Builder().setRatioType(ratioType).setRgba(rgbaHexString)
                .setDuration(getTotalTime()).setCurrentDuration(currentPosition).setFading(isMusicFading);
        if (currentMediaItem != null) {
            builder.setProjectId(currentMediaItem.getProjectId());
        }
        return builder.build();
    }

    public void preViewRotate(boolean flag) {
        mOpenglDrawer.preViewRotate(flag);
    }

    public void setOnChangeCurItemListener(OnChangeCurItemListener onChangeCurItemListener) {
        mOnChangeCurItemListener = onChangeCurItemListener;
    }

    private OnChangeCurItemListener mOnChangeCurItemListener;

    public interface OnChangeCurItemListener {
        void onChangeCurItem(MediaItem curMediaItem);
    }

    public void setAudioFadingPlay(boolean isFading) {
        isMusicFading = isFading;
        audioPlayer.setFading(isFading);
    }

    public void videoCopy(MediaItem item) {
        mMediaPlayer.addMediaPlayer(item);
    }

    public void resetCurrentIndex() {
        curIndex = -1;
    }

    public void setAudioPlayCallback(AudioPlayer.AudioCallback audioCallback) {
        audioPlayer.setAudioPlayCallback(audioCallback);
    }

}
