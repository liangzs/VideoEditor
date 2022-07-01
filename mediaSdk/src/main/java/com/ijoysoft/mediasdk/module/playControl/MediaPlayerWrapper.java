package com.ijoysoft.mediasdk.module.playControl;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Surface;

import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MediaPlayerWrapper
        implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
    // 不通过index去定位mediaplayer了，应该会发生两个mediaitem间的切换，暂用mediaitem的内容地址做key值做
    private MediaPlayer mCurMediaPlayer; // current player
    // private List<MediaPlayer> mPlayerList; //player list
    private Map<String, MediaPlayer> mPlayerMap;
    private List<MediaItem> mSrcList; // video src list
    private Surface surface;
    private IMediaCallback mCallback;

    private MediaPlayerWrapper() {
        mPlayerMap = new HashMap<>();
    }

    public void setOnCompletionListener(IMediaCallback callback) {
        this.mCallback = null;
        this.mCallback = callback;
    }

    /**
     * 视频跳转到指定位置
     *
     * @param time
     */
    public void seekTo(int time) {
        if (mCurMediaPlayer != null) {
            mCurMediaPlayer.seekTo(time);
        }
    }

    /**
     * 遍历选中文件的，获取其媒体信息（主要是视频文件）
     */
    public void setDataSource(List<MediaItem> dataSource) {
        mSrcList = dataSource;
        try {
            for (int i = 0; i < mSrcList.size(); i++) {
                if (mSrcList.get(i).getMediaType() != MediaType.VIDEO) {
                    continue;
                }
                if (mPlayerMap.get(mSrcList.get(i).getEqualId()) == null) {
                    Log.i("MediaPlayer", " new MediaPlayer()");
                    MediaPlayer player = new MediaPlayer();
                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    player.setOnCompletionListener(this);
                    player.setOnErrorListener(this);
                    player.setOnPreparedListener(this);
                    String path = ObjectUtils.isEmpty(mSrcList.get(i).getTrimPath()) ? mSrcList.get(i).getPath()
                            : mSrcList.get(i).getTrimPath();
                    player.setDataSource(path);
                    player.prepare();
                    mPlayerMap.put(mSrcList.get(i).getEqualId(), player);
                } else {
                    MediaPlayer mediaPlayer = mPlayerMap.get(mSrcList.get(i).getEqualId());
                    mediaPlayer.setSurface(null);
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setOnCompletionListener(this);
                    mediaPlayer.setOnErrorListener(this);
                    mediaPlayer.setOnPreparedListener(this);
                    String path = ObjectUtils.isEmpty(mSrcList.get(i).getTrimPath()) ? mSrcList.get(i).getPath()
                            : mSrcList.get(i).getTrimPath();
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepare();
                    // mediaPlayer.prepareAsync();用这个，三星有的手机竟然会有问题,
                }
                if (i == 0) {
                    Log.d("set-mCurMediaPlayer", "mCurMediaPlayer");
                    mCurMediaPlayer = mPlayerMap.get(mSrcList.get(0).getEqualId());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 遍历选中文件的，获取其媒体信息（主要是视频文件）
     */
    public void setOriginDataSource(List<MediaItem> dataSource) {
        mSrcList = dataSource;
        for (int i = 0; i < mSrcList.size(); i++) {
            if (mSrcList.get(i).getMediaType() != MediaType.VIDEO) {
                continue;
            }
            if (mPlayerMap.get(mSrcList.get(i).getEqualId()) == null) {
                Log.i("MediaPlayer", " new MediaPlayer()");
                MediaPlayer player = new MediaPlayer();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setOnCompletionListener(this);
                player.setOnErrorListener(this);
                player.setOnPreparedListener(this);
                String path = mSrcList.get(i).getPath();
                try {
                    player.setDataSource(path);
                    player.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mPlayerMap.put(mSrcList.get(i).getEqualId(), player);
            } else {
                MediaPlayer mediaPlayer = mPlayerMap.get(mSrcList.get(i).getEqualId());
                mediaPlayer.setSurface(null);
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(this);
                mediaPlayer.setOnErrorListener(this);
                mediaPlayer.setOnPreparedListener(this);
                String path = mSrcList.get(i).getPath();
                try {
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (i == 0) {
                mCurMediaPlayer = mPlayerMap.get(mSrcList.get(0).getEqualId());
            }
        }
    }

    /**
     * 视频文件的数量
     *
     * @return
     */
    public boolean checkUpdateVideo(List<MediaItem> dataSource) {
        int count = 0;
        for (int i = 0; i < dataSource.size(); i++) {
            if (dataSource.get(i).getMediaType() == MediaType.VIDEO) {
                count++;
                if (mPlayerMap.get(dataSource.get(i).getEqualId()) == null) {
                    return true;
                }
            }
        }
        if (count != mPlayerMap.size()) {
            return true;
        }
        return false;
    }

    /**
     * 仅做数据的更新
     *
     * @param dataSource
     */
    public void setDataSourceUpdate(List<MediaItem> dataSource) {
        if (checkUpdateVideo(dataSource)) {
            release();
            setDataSource(dataSource);
        }
        this.mSrcList = dataSource;
    }

    public void setSourceUpdate(List<MediaItem> dataSource) {
        release();
        setDataSource(dataSource);
        this.mSrcList = dataSource;

    }

    public boolean changeCurrentMediaplayer(int index) {
        Log.d("Mediaplayer", "(mPlayerMap.get(mSrcList.get(index).getEqualId())=="
                + mPlayerMap.get(mSrcList.get(index).getEqualId()));
        Log.d("Mediaplayer", "mCurMediaPlayer===" + mCurMediaPlayer);
        if (mPlayerMap.get(mSrcList.get(index).getEqualId()) != mCurMediaPlayer) {
            switchPlayer(mSrcList.get(index));
            return true;
        }
        return false;
    }

    /**
     * 更换文件播放路径
     *
     * @param path
     */
    public void changePath(String path) {
        mCurMediaPlayer.setSurface(null);
        mCurMediaPlayer.stop();
        mCurMediaPlayer.reset();
        try {
            mCurMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mCurMediaPlayer.setOnCompletionListener(this);
            mCurMediaPlayer.setOnErrorListener(this);
            mCurMediaPlayer.setOnPreparedListener(this);
            mCurMediaPlayer.setDataSource(path);
            mCurMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCurMediaPlayer.setSurface(surface);
    }

    public List<MediaItem> getVideoInfo() {
        return mSrcList;
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
    }

    /**
     * 对二级页面回到主页面进行重置不放，也就是说对mediaplayer进行全新的重置（仅对首个文件进行设置）
     * 出现这样一个情况，就是当主页面播放到非首个文件的时候，进入二级页面然后返回（此时的状态是二级页面是首个文件在播放）
     * 这时候主页面需要播放首个文件，但是首个文件的mediaplayer的状态确是stop的状态，一直都找不到原因，只能如此下策
     */
    public void playReset(MediaItem mediaItem) {
        mCurMediaPlayer = mPlayerMap.get(mediaItem.getEqualId());
        mCurMediaPlayer.setSurface(null);
        mCurMediaPlayer.stop();
        mCurMediaPlayer.reset();
        mCurMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mCurMediaPlayer.setOnCompletionListener(this);
        mCurMediaPlayer.setOnErrorListener(this);
        mCurMediaPlayer.setOnPreparedListener(this);
        String path = ObjectUtils.isEmpty(mediaItem.getTrimPath()) ? mediaItem.getPath() : mediaItem.getTrimPath();
        try {
            mCurMediaPlayer.setDataSource(path);
            mCurMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCurMediaPlayer.setSurface(surface);
    }

    public void prepare() {
        if (mSrcList == null || mSrcList.isEmpty()) {
            return;
        }
        if (mCallback != null) {
            mCallback.onVideoChanged(mSrcList.get(0));
        }
        if (mCallback != null) {
            mCallback.onVideoPrepare();
        }
    }

    public void start(int seekStart) {
        if (mCurMediaPlayer == null) {
            return;
        }
        mCurMediaPlayer.setSurface(surface);
        if (seekStart > 0) {// seekSart这个不是说是为了seekbar，只针对于逻辑裁剪的播放
            mCurMediaPlayer.seekTo(seekStart);
            return;
        }
        mCurMediaPlayer.start();
        if (mCallback != null) {
            mCallback.onVideoStart();
        }
    }

    public void bindSurface() {
        if (mCurMediaPlayer != null) {
            mCurMediaPlayer.setSurface(surface);
        }
    }

    public void pause() {
        if (mCurMediaPlayer != null && mCurMediaPlayer.isPlaying()) {
            mCurMediaPlayer.pause();
        }
        if (mCallback != null) {
            mCallback.onVideoPause();
        }
    }

    public boolean isPlaying() {
        if (mCurMediaPlayer != null) {
            return mCurMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i("test", "complete");
        if (mCallback != null) {
            // if (mCallback != null && mp.isPlaying()) { 三星手机出问题，&& mp.isPlaying()去掉，但是不保证其他手机会跳。真麻烦
            mCallback.onCompletion(mp);
        }
    }

    public void stop() {
        if (mPlayerMap == null) {
            return;
        }
        Set<Map.Entry<String, MediaPlayer>> entries = mPlayerMap.entrySet();
        Iterator<Map.Entry<String, MediaPlayer>> iteratorMap = entries.iterator();
        while (iteratorMap.hasNext()) {
            Map.Entry<String, MediaPlayer> next = iteratorMap.next();
            if (next.getValue() != null) {
                next.getValue().stop();
            }
        }
    }

    public void release() {
        if (mPlayerMap == null) {
            return;
        }
        Set<Map.Entry<String, MediaPlayer>> entries = mPlayerMap.entrySet();
        Iterator<Map.Entry<String, MediaPlayer>> iteratorMap = entries.iterator();
        while (iteratorMap.hasNext()) {
            Map.Entry<String, MediaPlayer> next = iteratorMap.next();
            if (next.getValue() != null) {
                next.getValue().setSurface(null);
                next.getValue().release();
            }
        }
        mCurMediaPlayer = null;
        mPlayerMap.clear();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.i("mediaplayer", "error:" + what);
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
    }

    public void setVolume(float volume) {
        Log.d("setVolume", "setVolume===" + volume);
        Set<Map.Entry<String, MediaPlayer>> entries = mPlayerMap.entrySet();
        Iterator<Map.Entry<String, MediaPlayer>> iteratorMap = entries.iterator();
        while (iteratorMap.hasNext()) {
            Map.Entry<String, MediaPlayer> next = iteratorMap.next();
            if (next.getValue() != null) {
                next.getValue().setVolume(volume, volume);
            }
        }

    }

    /**
     * 跳到整体seek中的指定位置
     *
     * @param ti
     */
    public void wholeSeekTo(MediaItem mediaItem, int ti) {
        if (mCurMediaPlayer != null) {
            mCurMediaPlayer.setSurface(null);
            mCurMediaPlayer.seekTo(0);
            if (mCurMediaPlayer.isPlaying()) {
                pause();
            }
        }
        if (mCallback != null) {
            mCallback.onVideoChanged(mediaItem);
            mCallback.onVideoPause();
        }
        if (mCurMediaPlayer != mPlayerMap.get(mediaItem.getEqualId())
                && mPlayerMap.get(mediaItem.getEqualId()) != null) {
            mPlayerMap.get(mediaItem.getEqualId()).setSurface(surface);
        }
        mCurMediaPlayer = mPlayerMap.get(mediaItem.getEqualId());
        if (mCurMediaPlayer == null) {
            return;
        }
        mCurMediaPlayer.seekTo(ti);
    }

    public void addMediaPlayer(MediaItem mediaItem) {
        MediaPlayer player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.setOnPreparedListener(this);
        try {
            player.setDataSource(mediaItem.getTrimPath() == null ? mediaItem.getPath() : mediaItem.getTrimPath());
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayerMap.put(mediaItem.getEqualId(), player);
    }

    /**
     * 视频播放切换
     */
    public void switchPlayer(MediaItem mediaItem) {
        if (mCurMediaPlayer != null) {
            mCurMediaPlayer.setSurface(null);
            mCurMediaPlayer.seekTo(0);
            if (mCurMediaPlayer.isPlaying()) {
                pause();
            }
        }
        if (mCallback != null) {
            mCallback.onVideoChanged(mediaItem);
        }
        try {
            mCurMediaPlayer = mPlayerMap.get(mediaItem.getEqualId());
            mCurMediaPlayer.setSurface(surface);
            mCurMediaPlayer.start();

        } catch (Exception e) {
            e.printStackTrace();
            // Log.e("MediaPlayerWrapper", new Gson().toJson(e));
        }
    }

    /**
     * 清除子页面中mediaplayer对surface的占有
     */
    public void clearMediaPlayerStatus() {
        if (mCurMediaPlayer != null) {
            mCurMediaPlayer.setSurface(null);
        }
    }

    /**
     * 视频播放切换
     */
    public void switchPlayerPreview(MediaItem mediaItem) {
        if (mCurMediaPlayer != null) {
            mCurMediaPlayer.setSurface(null);
        }
        if (mCallback != null) {
            mCallback.onVideoChanged(mediaItem);
        }
        mCurMediaPlayer = mPlayerMap.get(mediaItem.getEqualId());
        mCurMediaPlayer.setSurface(surface);
        mCurMediaPlayer.seekTo(0);
    }

    /**
     * 获取当前播放文件的播放位置点
     *
     * @return
     */
    public int getCurrentPosition() {

        if (mCurMediaPlayer == null) {
            return 0;
        }
        // Log.i("test","mCurMediaPlayer.getCurrentPosition()"+mCurMediaPlayer.toString()+"---"+mCurMediaPlayer.getCurrentPosition()+"");
        return mCurMediaPlayer.getCurrentPosition();
    }

    public interface IMediaCallback {
        /**
         * callback when all the player prepared
         */
        void onVideoPrepare();

        /**
         * callback when player start
         */
        void onVideoStart();

        /**
         * callback when player pause
         */
        void onVideoPause();

        /**
         * callback when all the videos have been played
         *
         * @param mp
         */
        void onCompletion(MediaPlayer mp);

        /**
         * callback when video changed
         *
         * @param info
         */
        void onVideoChanged(MediaItem info);
    }

    private static class SingleTonHoler {
        private static MediaPlayerWrapper INSTANCE = new MediaPlayerWrapper();
    }

    public static MediaPlayerWrapper getInstance() {
        return SingleTonHoler.INSTANCE;
    }

}
