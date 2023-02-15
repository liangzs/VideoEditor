
package com.ijoysoft.mediasdk.module.playControl;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.media.MediaPlayer;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.AudioMediaItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 音频控制类,与视频播放类中的mediaplayer存在交互
 * 暂且视频播放类持有一个mediaplayer，也能控制音量，合成的时候把音频anix混入视频中
 * 如果效果不好替代方案为把视频中的音频进行抽取，然后和现有音频混音合并后再与视频进行合成
 * <p>
 * 创建一个单线程线程池
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
    private AudioMediaItem currentAudio;
    private boolean isFadeFinish;
    private Handler handler = new Handler(Looper.getMainLooper());
    private ValueAnimator animator;
    private boolean isDestory;
    // activity走onresume过程中，glsurface会重走过程，所以暂停状态下audioplayer也会播放
    // 加入此标志，使得resume,pause,start控制此状态
    private boolean enablePlay;
    boolean isMusicContain;// progress是否超出播放范围
    private LoudnessEnhancer loudnessEnhancer;
    private ExecutorService audioExcutor;
    private volatile boolean recreate;


    public AudioPlayer() {
        audioList = new ArrayList<>();
        audioExcutor = Executors.newSingleThreadExecutor();
    }

    public void updateMultiAudio(List<AudioMediaItem> list) {
        stop();
        audioList.clear();
        audioList.addAll(list);
    }

    /**
     * 设置多音频播放
     *
     * @param list
     */
    public void setMultiAudio(List<AudioMediaItem> list) {
        audioList.clear();
        if (list != null) {
            audioList.addAll(list);
        }
        if (ObjectUtils.isEmpty(audioList)) {
            stop();
        }
    }

    /**
     * 设置单音频播放
     *
     * @param audio
     */
    public void setSingleAudio(AudioMediaItem audio) {
        audioList.clear();
        audioList.add(audio);
        recreate = true;
    }

    /**
     * 控制音量
     */
    public void setVolume(float volume) {
        if (currentAudio != null) {
            currentAudio.setVolume(volume);
        }
        if (!ObjectUtils.isEmpty(audioList)) {
            for (AudioMediaItem audioMediaItem : audioList) {
                audioMediaItem.setVolume(volume);
            }
        }
        if (audioMediaPlayer != null) {
            addRunWork(() -> {
                try {
                    if (volume < 100) {
                        audioMediaPlayer.setVolume(volume / ConstantMediaSize.MAX_VOLUME, volume / ConstantMediaSize.MAX_VOLUME);
                        if (loudnessEnhancer != null) {
                            loudnessEnhancer.setEnabled(false);
                        }
                    } else {
                        audioMediaPlayer.setVolume(1f, 1f);
                        if (loudnessEnhancer != null) {
                            loudnessEnhancer.setEnabled(true);
                            loudnessEnhancer.setTargetGain((int) (2 * (volume - 100)));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
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
        if (currentAudio != null && audioMediaPlayer != null) {
            addRunWork(() -> {
                try {
                    audioMediaPlayer.start();
                    audioMediaPlayer.setVolume(currentAudio.getPlayVolume(), currentAudio.getPlayVolume());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
    }

    /**
     * 开始播放-淡入
     */
    public void start() {
        if (audioList.isEmpty() || ObjectUtils.isEmpty(currentAudio)) {
            return;
        }
        addRunWork(() -> {
            isFadeFinish = false;
            createAudioPlayer();
        });

        if (audioCallback != null) {
            audioCallback.start(currentAudio);
        }
    }

    /**
     * 创建音乐播放
     */
    private void createAudioPlayer() {
        if (currentAudio == null) {
            return;
        }
        if (audioMediaPlayer != null) {
            audioMediaPlayer.stop();
            audioMediaPlayer.release();
        }
        try {
            audioMediaPlayer = new MediaPlayer();
            audioMediaPlayer.setDataSource(currentAudio.getPath());
            audioMediaPlayer.setLooping(true);
            audioMediaPlayer.setOnErrorListener(this);
            audioMediaPlayer.setOnPreparedListener(this);
            audioMediaPlayer.prepareAsync();
            if (loudnessEnhancer != null) {
                loudnessEnhancer.release();
                loudnessEnhancer = null;
            }
            if (currentAudio.getPlayVolume() > 1) {
                loudnessEnhancer = new LoudnessEnhancer(audioMediaPlayer.getAudioSessionId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 开始播放
     */
    public void resume() {
        enablePlay = true;
        LogUtils.i(TAG, "resume");
        if (!audioList.isEmpty()) {
            addRunWork(() -> {
                try {
                    if (audioMediaPlayer != null && !audioMediaPlayer.isPlaying() && isMusicContain) {
                        audioMediaPlayer.start();
                        LogUtils.i(TAG, "resume1");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }

    /**
     * 暂停播放
     */
    public void pause() {
        enablePlay = false;
        addRunWork(() -> {
            try {
                if (audioMediaPlayer != null && audioMediaPlayer.isPlaying()) {
                    audioMediaPlayer.pause();
                }
                handler.post(() -> {
                    if (animator != null && animator.isRunning()) {
                        animator.cancel();
                        isFadeFinish = false;
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

    /**
     * 整个工作台播放结束，音频需要重新播放，则音频需要依赖时间去做播放动作
     */
    public void stop() {
        LogUtils.i(TAG, "stop");
        recreate = true;
        enablePlay = false;
        if (audioMediaPlayer != null) {
            addRunWork(() -> {
                try {
                    audioMediaPlayer.pause();
                    audioMediaPlayer.seekTo(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
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
        if (ObjectUtils.isEmpty(audioList)) {
            return;
        }
        isMusicContain = false;
        for (AudioMediaItem audio : audioList) {
            if (audio.getDurationInterval() == null) {
                continue;
            }
            if (currentPostion >= audio.getDurationInterval().getStartDuration()
                    && currentPostion <= audio.getDurationInterval().getEndDuration()) {
                if ((currentAudio != audio || recreate) && enablePlay) {
                    currentAudio = audio;
                    recreate = false;
                    LogUtils.i(TAG, "ondrawFramePlay->start");
                    start();
                }
                isMusicContain = true;
                if (!isFadeFinish && isFading) {
                    if (currentPostion < audio.getDurationInterval().getStartDuration() + fadeInTime) {//淡入
                        isFadeFinish = true;
                        LogUtils.i(TAG, "audioFadeIn,currentPostion:" + currentPostion);
                        audioFadeIn();
                        return;
                    }
                    if (currentPostion > (audio.getDurationInterval().getEndDuration() - fadeOutTime)) {// 淡出
                        isFadeFinish = true;
                        audioFadeOut();
                        LogUtils.i(TAG, "audioFadeOut,currentPostion" + currentPostion);
                    }

                }
            }
        }
        try {
            if (!isMusicContain && audioMediaPlayer != null && audioMediaPlayer.isPlaying()) {
                audioMediaPlayer.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 跳转到音频指定位置
     */
    public void seekTo(int progress) {
        isMusicContain = false;
        for (AudioMediaItem audio : audioList) {
            if (progress < 0) {
                progress = 0;
            }
            //防止除以0崩溃
            if (audio.getDurationInterval() == null || 0 == (int) audio.getDuration()) {
                continue;
            }
            if (progress >= audio.getDurationInterval().getStartDuration()
                    && progress <= audio.getDurationInterval().getEndDuration()) {
                int duration = (int) audio.getDuration();
                int time = progress > duration ? progress % duration : progress;
                if (currentAudio != audio || recreate) {
                    currentAudio = audio;
                    recreate = false;
                    addRunWork(() -> {
                        seekPlay(time);
                    });

                } else {
                    if (audioMediaPlayer == null) {
                        return;
                    }
                    int finalProgress = time;
                    addRunWork(() -> {
                        audioMediaPlayer.seekTo(finalProgress);
                    });
                }
                isMusicContain = true;
            }
        }
        if (!isMusicContain) {
            pause();
            recreate = true;
        }
    }

    /**
     * 活动seekbar滚动条是，播放到指定位置
     *
     * @param progress
     */
    public void seekPlay(int progress) {
        if (currentAudio == null) {
            return;
        }
        isFadeFinish = false;
        handler.post(() -> {
            if (animator != null && animator.isRunning()) {
                animator.cancel();
                animator.end();
            }
        });
        addRunWork(() -> createAudioplayer(progress));
    }

    /**
     * 创建player
     *
     * @param progress
     */
    private void createAudioplayer(final int progress) {
        try {
            if (currentAudio == null) {
                return;
            }
            if (audioMediaPlayer == null) {
                audioMediaPlayer = new MediaPlayer();
                audioMediaPlayer.setDataSource(currentAudio.getPath());
                audioMediaPlayer.setOnErrorListener(this);
                audioMediaPlayer.setLooping(true);
                audioMediaPlayer.setOnPreparedListener(mediaPlayer -> {
                    if (currentAudio == null) {
                        return;
                    }
                    mediaPlayer.setVolume(currentAudio.getPlayVolume(), currentAudio.getPlayVolume());
                    if (progress > 0) {
                        audioMediaPlayer.seekTo(progress);
                    }
                    //经常设置没有效果
                    if (enablePlay) {
                        mediaPlayer.start();
                    }
                });
                audioMediaPlayer.prepareAsync();
                if (loudnessEnhancer != null) {
                    loudnessEnhancer.release();
                    loudnessEnhancer = null;
                }
                if (currentAudio.getPlayVolume() > 1) {
                    loudnessEnhancer = new LoudnessEnhancer(audioMediaPlayer.getAudioSessionId());
                }
            } else {
                audioMediaPlayer.stop();
                audioMediaPlayer.reset();
                audioMediaPlayer.setLooping(true);
                audioMediaPlayer.setOnErrorListener(this);
                audioMediaPlayer.setDataSource(currentAudio.getPath());
                audioMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        try {
                            if (currentAudio == null || audioMediaPlayer == null) {
                                return;
                            }
                            audioMediaPlayer.setVolume(currentAudio.getPlayVolume(), currentAudio.getPlayVolume());
                            if (progress > 0) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    audioMediaPlayer.seekTo(progress, MediaPlayer.SEEK_CLOSEST);
                                } else {
                                    audioMediaPlayer.seekTo(progress);
                                }
                            }
                            if (enablePlay) {
                                audioMediaPlayer.start();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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
        handler.post(() -> {
            if (animator != null && animator.isRunning()) {
                animator.cancel();
                animator.end();
            }
        });
        animator = ValueAnimator.ofFloat(currentAudio.getPlayVolume(), 0);
        animator.setDuration(fadeOutTime);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> {
            float volume = (float) animation.getAnimatedValue();
            try {
                if (audioMediaPlayer != null) {
                    audioMediaPlayer.setVolume(volume, volume);
                }
            } catch (Exception e) {
                if (animation != null) {
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
                isFadeFinish = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                try {
                    if (audioMediaPlayer != null && currentAudio != null) {
                        audioMediaPlayer.setVolume(currentAudio.getPlayVolume(), currentAudio.getPlayVolume());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        handler.post(() -> {
            if (animator != null) {
                animator.start();
            }
        });
    }

    /**
     * 淡入效果，音量大小从0变到当前设置音量
     */
    public void audioFadeIn() {
        handler.post(() -> {
            if (animator != null && animator.isRunning()) {
                animator.cancel();
                animator.end();
            }
        });
        animator = ValueAnimator.ofFloat(0, currentAudio.getPlayVolume());
        animator.setDuration(fadeInTime);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float volume = (float) animation.getAnimatedValue();
                try {
                    if (audioMediaPlayer != null) {
                        audioMediaPlayer.setVolume(volume, volume);
                    }
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
                isFadeFinish = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                try {
                    if (audioMediaPlayer != null && currentAudio != null) {
                        audioMediaPlayer.setVolume(currentAudio.getPlayVolume(), currentAudio.getPlayVolume());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        handler.post(() -> {
            if (animator != null) {
                animator.start();
            }
        });

    }

    public AudioMediaItem getCurrentAudio() {
        return currentAudio;
    }

    public boolean isFading() {
        return isFading;
    }

    public boolean isPlaying() {
        if (audioMediaPlayer == null) {
            return false;
        }
        return audioMediaPlayer.isPlaying();
    }

    public boolean isMusicContain() {
        return isMusicContain;
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
        addRunWork(() -> {
            recreate = true;
            try {
                if (audioMediaPlayer != null) {
                    audioMediaPlayer.stop();
                    audioMediaPlayer.release();
                    audioMediaPlayer = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        audioExcutor.shutdown();
    }

    /**
     * {@link #onDestroy()} 调用完这个方法之后，还是发现关闭广告时，触发了线程的执行，所以导致了RejectedExecutionException
     */
    private void addRunWork(Runnable runnable) {
        if (!audioExcutor.isShutdown()) {
            audioExcutor.execute(runnable);
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
