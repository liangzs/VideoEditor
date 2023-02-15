//package com.ijoysoft.mediasdk.module.playControl;
//
//import android.util.Log;
//import android.view.Surface;
//
//import com.ijoysoft.mediasdk.common.global.MediaSdk;
//import com.ijoysoft.mediasdk.common.utils.LogUtils;
//import com.ijoysoft.mediasdk.module.entity.MediaItem;
//import com.ijoysoft.mediasdk.module.entity.VideoMediaItem;
//
//import java.util.List;
//
//import tv.danmaku.ijk.media.player.AbstractMediaPlayer;
//import tv.danmaku.ijk.media.player.IMediaPlayer;
//import tv.danmaku.ijk.media.player.exo.IjkExoMediaPlayer;
//
///**
// * 此播放器兼容ffmpeg 的ijkplayer和原生的exopalyer
// *
// * @version 20210317
// * 重构播放器未exoplayer，采用ConcatenatingMediaSource的方案
// */
//public class MediaPlayerWrapper
//        implements IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnPreparedListener {
//    private List<MediaItem> mSrcList; // video src list
//    private Surface surface;
//    private IMediaCallback mCallback;
//    private static final String TAG = "MediaPlayerWrapper";
//
//
//    private MediaPlayerWrapper() {
//    }
//
//    public void setOnCompletionListener(IMediaCallback callback) {
//        this.mCallback = null;
//        this.mCallback = callback;
//    }
//
//    /**
//     * 视频跳转到指定位置
//     *
//     * @param time·
//     */
//    public void seekTo(int time) {
////        if (mCurMediaPlayer != null) {
////            try {
////                mCurMediaPlayer.seekTo(time);
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////
////        }
//        if (ijkExoMediaPlayer != null) {
//            try {
//                ijkExoMediaPlayer.seekTo(time);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//    /**
//     * 遍历选中文件的，获取其媒体信息（主要是视频文件）
//     */
//    public void setDataSource(List<MediaItem> dataSource, boolean trimPrevew) {
//        mSrcList = dataSource;
//        createContatMediaPlayer();
//    }
//
//
//    public void setSourceUpdate(List<MediaItem> dataSource) {
//        release();
//        setDataSource(dataSource, false);
//        this.mSrcList = dataSource;
//
//    }
//
//    public void setSourceUpdateOriginPath(List<MediaItem> dataSource) {
//        release();
//        setDataSource(dataSource, false);
//        this.mSrcList = dataSource;
//
//    }
//
//    public boolean changeCurrentMediaplayer(int index) {
////        if (ObjectUtils.isEmpty(mPlayerMap) || ObjectUtils.isEmpty(mSrcList)) {
////            return false;
////        }
////        if (mPlayerMap.get(mSrcList.get(index).getEqualId()) != mCurMediaPlayer) {
////            switchPlayer(mSrcList.get(index));
////            return true;
////        }
//        return false;
//    }
//
//    /**
//     * 更换文件播放路径
//     */
//    public void changePath(MediaItem mediaItem) {
////        if (mCurMediaPlayer == null) {
////            return;
////        }
////        try {
////            mCurMediaPlayer = createMediaPlayer((VideoMediaItem) mediaItem, false);
////            mCurMediaPlayer.setSurface(surface);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//
//    }
//
//
//    public void setSurface(Surface surface) {
//        this.surface = surface;
//    }
//
//
//    public void prepare(MediaItem mediaItem) {
//        if (mSrcList == null || mSrcList.isEmpty()) {
//            return;
//        }
//        if (mCallback != null && mediaItem instanceof VideoMediaItem) {
//            mCallback.onVideoChanged(mediaItem);
//        }
//        if (mCallback != null) {
//            mCallback.onPrepare();
//        }
//    }
//
//    public void start(int seekStart) {
//        ijkExoMediaPlayer.start();
//
//    }
//
//    public void bindSurface() {
//        if (ijkExoMediaPlayer != null) {
//            ijkExoMediaPlayer.setSurface(surface);
//        }
//
//    }
//
//    public void pause() {
//        if (ijkExoMediaPlayer != null) {
//            ijkExoMediaPlayer.pause();
//        }
//    }
//
//    public boolean isPlaying() {
//        return ijkExoMediaPlayer != null && ijkExoMediaPlayer.isPlaying();
//    }
//
//    @Override
//    public void onCompletion(IMediaPlayer mp) {
//        LogUtils.i(TAG, "onCompletion...");
////        mCurMediaPlayer.setSurface(null);
//        if (mCallback != null) {
//            // if (mCallback != null && mp.isPlaying()) { 三星手机出问题，&& mp.isPlaying()去掉，但是不保证其他手机会跳。真麻烦
//            mCallback.onComplet(mp);
//        }
//
//    }
//
//    public void stop() {
//        if (ijkExoMediaPlayer != null) {
//            ijkExoMediaPlayer.stop();
//        }
//    }
//
//    public void release() {
//        if (ijkExoMediaPlayer != null) {
//            ijkExoMediaPlayer.release();
//        }
//    }
//
//    @Override
//    public boolean onError(IMediaPlayer mp, int what, int extra) {
//        Log.i("mediaplayer", "error:" + what);
//        return true;
//    }
//
//    @Override
//    public void onPrepared(IMediaPlayer mp) {
//    }
//
//    /**
//     * 确认是第几个视频文件的音量
//     *
//     * @param volume
//     */
//    public void setVolume(float volume) {
//
//    }
//
//    public void wholeSeekTo(MediaItem mediaItem, int ti) {
//
//    }
//
//    /**
//     * 添加播放器
//     *
//     * @param mediaItem
//     */
//    public void addMediaPlayer(MediaItem mediaItem) {
////        createMediaPlayer((VideoMediaItem) mediaItem, false);
//    }
//
//    /**
//     * 视频播放切换
//     */
//    public void switchPlayer(MediaItem mediaItem) {
//    }
//
//    /**
//     * 清除子页面中mediaplayer对surface的占有
//     */
//    public void clearMediaPlayerStatus() {
//        try {
//            if (ijkExoMediaPlayer != null) {
//                ijkExoMediaPlayer.setSurface(null);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    /**
//     * 获取当前播放文件的播放位置点
//     *
//     * @return
//     */
//    public int getCurrentPosition() {
//        if (ijkExoMediaPlayer != null) {
//            return (int) ijkExoMediaPlayer.getCurrentPosition();
//        }
//        return 0;
//    }
//
//
//    private static class SingleTonHoler {
//        private static MediaPlayerWrapper INSTANCE = new MediaPlayerWrapper();
//    }
//
//    public static MediaPlayerWrapper getInstance() {
//        return SingleTonHoler.INSTANCE;
//    }
//
//
//    /**
//     * 如果视频超过1080p及以下，则默认使用ijkplayer进行播放，超过这个范围则使用exoplayer进行播放
//     * 后续再根据情况进行优化
//     *
//     * @return
//     */
//    public AbstractMediaPlayer createMediaPlayer(VideoMediaItem mediaItem, boolean trimPreview) {
//        return null;
//    }
//
//
//    private IjkExoMediaPlayer ijkExoMediaPlayer;
//
//    /**
//     * 尝试使用exoplayer的concat连续播放功能
//     */
//    public void createContatMediaPlayer() {
//        ijkExoMediaPlayer = new IjkExoMediaPlayer(MediaSdk.getInstance().getContext());
//        ijkExoMediaPlayer.setLooping(false);
//        ijkExoMediaPlayer.setDataSource(mSrcList);
//        ijkExoMediaPlayer.setOnCompletionListener(this);
//        ijkExoMediaPlayer.setOnErrorListener(this);
//        ijkExoMediaPlayer.setOnPreparedListener(this);
//        ijkExoMediaPlayer.setSurface(surface);
//        ijkExoMediaPlayer.prepareAsync();
//    }
//
//    public void onDestory() {
//        if (ijkExoMediaPlayer != null) {
//            ijkExoMediaPlayer.release();
//        }
//    }
//
//
//}
