package com.ijoysoft.mediasdk.module.playControl;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.entity.AudioMediaItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 音频控制类,与视频播放类中的mediaplayer存在交互
 * 暂且视频播放类持有一个mediaplayer，也能控制音量，合成的时候把音频anix混入视频中
 * 如果效果不好替代方案为把视频中的音频进行抽取，然后和现有音频混音合并后再与视频进行合成
 */
public class AudioPlayer
        implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
    private static final String TAG = "AudioPlayer";
    // 淡入，如果音频片段小于3000ms，那么淡入淡出设置为1s，截音频的时候限制长度
    private int fadeInTime = ConstantMediaSize.FADE_IN_TIME;
    // 淡出
    private int fadeOutTime = ConstantMediaSize.FADE_OUT_TIME;
    private List<AudioMediaItem> audioList;
    private boolean isFading;
    // 播放器保留一个是因为，在时间轴上只允许一个背景音乐
    private MediaPlayer audioMediaPlayer;
    // 当前音量,记录用户调整的音量值，合成的时候还需该值进行合成
    private AudioFadeCallback audioFadeCallback;
    private AudioMediaItem currentAudio;
    private boolean isFadeOutFinish;
    private Handler handler = new Handler(Looper.getMainLooper());
    private ValueAnimator animator;
    private boolean isDestory;
    // activity走onresume过程中，glsurface会重走过程，所以暂停状态下audioplayer也会播放
    // 加入此标志，使得resume,pause,start控制此状态
    private boolean enablePlay;
    private boolean isInit;
    private boolean isBelongDuration;

    public AudioPlayer() {
        audioList = new ArrayList<>();
    }

    /**
     * 设置多音频播放
     *
     * @param list
     */
    public void setMultiAudio(List<AudioMediaItem> list) {
        isInit = true;
        audioList.clear();
        audioList.addAll(list);
    }

    public void updateMultiAudio(List<AudioMediaItem> list) {
        stop();
        enablePlay = true;
        isInit = true;
        audioList.clear();
        audioList.addAll(list);
    }

    /**
     * 设置单音频播放
     *
     * @param audio
     */
    public void setSingleAudio(AudioMediaItem audio) {
        isInit = true;
        audioList.clear();
        audioList.add(audio);
        currentAudio = audio;
    }

    /* 删除单段音乐 */
    public void deleteSingleAudio() {
        if (audioList.size() > 0) {
            audioList.remove(0);
        }

        if (currentAudio != null) {
            currentAudio = null;
        }
        if (audioMediaPlayer != null) {
            audioMediaPlayer.stop();
        }
    }

    /**
     * 控制音量
     */
    public void setVolume(float volume) {
        if (currentAudio != null) {
            currentAudio.setVolume(volume);
        }

        if (audioMediaPlayer != null) {
            audioMediaPlayer.setVolume(volume, volume);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, "audioPlayer---onError:what-" + what + ",");
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.i(TAG, "onprepare-finish");
        audioMediaPlayer.start();
        if (currentAudio != null) {
            audioMediaPlayer.setVolume(currentAudio.getVolume(), currentAudio.getVolume());
        }
        if (isFading) {
            audioFadeIn();
        }
    }

    /**
     * 开始播放-淡入
     */
    public void start() throws IOException {
        if (audioList.isEmpty()) {
            return;
        }
        isFadeOutFinish = false;
        if (audioMediaPlayer == null) {
            Log.i(TAG, "audioPlayer-start:audioMediaPlayer == null");
            audioMediaPlayer = new MediaPlayer();
            audioMediaPlayer.setLooping(true);
            audioMediaPlayer.setOnErrorListener(this);
            audioMediaPlayer.setOnPreparedListener(this);
            audioMediaPlayer.setDataSource(currentAudio.getPath());
            audioMediaPlayer.prepareAsync();
        } else {
            Log.i(TAG, "audioPlayer-start:audioMediaPlayer != null");
            audioMediaPlayer.stop();
            audioMediaPlayer.reset();
            audioMediaPlayer.setDataSource(currentAudio.getPath());
            audioMediaPlayer.setLooping(true);
            audioMediaPlayer.setOnErrorListener(this);
            audioMediaPlayer.setOnPreparedListener(this);
            audioMediaPlayer.prepareAsync();
        }
        if (audioCallback != null) {
            audioCallback.start(currentAudio);
        }
    }

    /**
     * 开始播放
     */
    public void resume() {
        enablePlay = true;
        if (audioMediaPlayer != null && !audioMediaPlayer.isPlaying()) {
            audioMediaPlayer.start();
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        enablePlay = false;
        try {
            if (audioMediaPlayer != null && audioMediaPlayer.isPlaying()) {
                audioMediaPlayer.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 整个工作台播放结束，音频需要重新播放，则音频需要依赖时间去做播放动作
     */
    public void stop() {
        isInit = true;
        enablePlay = false;
        try {
            if (audioMediaPlayer != null) {
                if (audioMediaPlayer.isPlaying())
                    audioMediaPlayer.stop();
                audioMediaPlayer.reset();
                audioMediaPlayer.release();
                audioMediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 检查是否有多个音频片段
     * 该方法在播放seekbarChange进行回调
     * 精确到100ms吧
     */
    public void ondrawFramePlay(int currentPostion) {
        if (isDestory || !enablePlay) {
            return;
        }
        if (audioList.isEmpty()) {
            return;
        }
        isBelongDuration = false;
        for (AudioMediaItem audio : audioList) {
            if (currentPostion >= audio.getDurationInterval().getStartDuration()
                    && currentPostion <= audio.getDurationInterval().getEndDuration()) {
                Log.i(TAG,
                        "currentPostion:" + currentPostion + ",audioItem:"
                                + audio.getDurationInterval().getStartDuration() + ","
                                + audio.getDurationInterval().getEndDuration());
                if ((currentAudio != audio || isInit) && enablePlay) {
                    isInit = false;
                    currentAudio = audio;
                    try {
                        start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                isBelongDuration = true;
                if (currentPostion > (audio.getDurationInterval().getEndDuration() - fadeOutTime) && !isFadeOutFinish) {// 淡出
                    isFadeOutFinish = true;
                    if (isFading) {
                        Log.i(TAG, "audioFadeOut:" + currentPostion);
                        audioFadeOut();
                    }
                }
            }
        }
        if (!isBelongDuration && audioMediaPlayer != null && audioMediaPlayer.isPlaying() && !isFading) {
            audioMediaPlayer.pause();
        }
    }

    /**
     * 跳转到音频指定位置
     */
    public void seekTo(int progress) {
        currentAudio = null;
        for (AudioMediaItem audio : audioList) {
            if (progress < 0) {
                progress = 0;
            }
            if (progress >= audio.getDurationInterval().getStartDuration()
                    && progress <= audio.getDurationInterval().getEndDuration()) {
                int duration = (int) audio.getDuration();
                int time = progress > duration ? progress % duration : progress;
                if (currentAudio != audio) {
                    currentAudio = audio;
                    seekPlay(time);
                } else {
                    if (audioMediaPlayer == null) {
                        return;
                    }
                    audioMediaPlayer.seekTo(time);
                }
            }
        }
    }

    /**
     * 活动seekbar滚动条是，播放到指定位置
     *
     * @param progress
     */
    public void seekPlay(final int progress) {
        try {
            if (audioMediaPlayer == null) {
                audioMediaPlayer = new MediaPlayer();
                audioMediaPlayer.setDataSource(currentAudio.getPath());
                audioMediaPlayer.setOnErrorListener(this);
                audioMediaPlayer.setLooping(true);
                audioMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.setVolume(currentAudio.getVolume(), currentAudio.getVolume());
                        mediaPlayer.seekTo(progress);
                        if (enablePlay) {
                            mediaPlayer.start();
                        }
                    }
                });
                audioMediaPlayer.prepareAsync();
            } else {
                audioMediaPlayer.stop();
                audioMediaPlayer.reset();
                audioMediaPlayer.setLooping(true);
                audioMediaPlayer.setOnErrorListener(this);
                audioMediaPlayer.setDataSource(currentAudio.getPath());
                audioMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.setVolume(currentAudio.getVolume(), currentAudio.getVolume());
                        mediaPlayer.seekTo(progress);
                        if (enablePlay) {
                            mediaPlayer.start();
                        }
                    }
                });
                audioMediaPlayer.prepareAsync();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 淡出效果，音量大小从当前音量减到0
     */
    public void audioFadeOut() {
        animator = ValueAnimator.ofFloat(currentAudio.getVolume(), 0);
        animator.setDuration(fadeOutTime);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float volume = (float) animation.getAnimatedValue();
                try {
                    audioMediaPlayer.setVolume(volume, volume);
                } catch (Exception e) {
                    animation.cancel();
                }
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                try {
                    // audioMediaPlayer.stop();
                    // audioMediaPlayer.release();
                    // audioMediaPlayer = null;
                } catch (Exception e) {
                    animation.cancel();
                }
                if (audioFadeCallback != null) {
                    audioFadeCallback.onComplete();
                }
                isFadeOutFinish = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                try {
                    audioMediaPlayer.setVolume(currentAudio.getVolume(), currentAudio.getVolume());
                    // audioMediaPlayer.stop();
                    // audioMediaPlayer.release();
                    // audioMediaPlayer = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        handler.post(new Runnable() {
            @Override
            public void run() {
                animator.start();
            }
        });
    }

    /**
     * 淡入效果，音量大小从0变到当前设置音量
     */
    public void audioFadeIn() {
        animator = ValueAnimator.ofFloat(0, currentAudio.getVolume());
        animator.setDuration(fadeInTime);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float volume = (float) animation.getAnimatedValue();
                try {
                    audioMediaPlayer.setVolume(volume, volume);
                } catch (Exception e) {
                    animation.cancel();
                }
            }
        });
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (audioMediaPlayer != null && currentAudio != null) {
                    audioMediaPlayer.setVolume(currentAudio.getVolume(), currentAudio.getVolume());
                    if (audioFadeCallback != null) {
                        audioFadeCallback.onComplete();
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                try {
                    audioMediaPlayer.setVolume(currentAudio.getVolume(), currentAudio.getVolume());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        handler.post(new Runnable() {
            @Override
            public void run() {
                animator.start();
            }
        });

    }

    public boolean isFading() {
        return isFading;
    }

    /**
     * 开放入口控制是否有淡入效果
     *
     * @param fading
     */
    public void setFading(boolean fading) {
        isFading = fading;
    }

    public interface AudioFadeCallback {
        void onComplete();

    }

    public void onDestroy() {
        isDestory = true;
        try {
            if (audioMediaPlayer != null) {
                audioMediaPlayer.stop();
                audioMediaPlayer.release();
                audioMediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAudioPlayCallback(AudioCallback audioCallback) {
        this.audioCallback = audioCallback;
    }

    private AudioCallback audioCallback;

    public interface AudioCallback {
        void start(AudioMediaItem audioMediaItem);

        void onComplete();

    }
}
