
package com.ijoysoft.mediasdk.module.playControl;

import android.media.MediaPlayer;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

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
 * {seek {@link AudioPlayer}}
 * <p>
 * 录音文件的播放控制
 */
public class RecordPlayer
        implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
    private static final String TAG = "RecordPlayer";
    private List<AudioMediaItem> audioList;
    private MediaPlayer audioMediaPlayer;
    // 当前音量,记录用户调整的音量值，合成的时候还需该值进行合成
    private AudioMediaItem currentAudio;
    private boolean isDestory;
    // activity走onresume过程中，glsurface会重走过程，所以暂停状态下audioplayer也会播放
    // 加入此标志，使得resume,pause,start控制此状态
    private boolean enablePlay;
    boolean isMusicContain;// progress是否超出播放范围
    private ExecutorService audioExcutor;
    private LoudnessEnhancer loudnessEnhancer;
    private volatile boolean recreate;

    public RecordPlayer() {
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
        audioList.addAll(list);
        if (ObjectUtils.isEmpty(audioList)) {
            stop();
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
//        LogUtils.i(TAG, "resume");
        addRunWork(() -> {
            try {
                if (audioMediaPlayer != null && !audioMediaPlayer.isPlaying() && isMusicContain) {
                    audioMediaPlayer.start();
//                    LogUtils.i(TAG, "resume1");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


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
                if ((currentAudio != audio||recreate) && enablePlay) {
                    currentAudio = audio;
                    recreate=false;
                    LogUtils.i(TAG, "ondrawFramePlay->start");
                    start();
                }
                isMusicContain = true;
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
            if (audio.getDurationInterval() == null) {
                continue;
            }
            if (progress >= audio.getDurationInterval().getStartDuration()
                    && progress <= audio.getDurationInterval().getEndDuration()) {
                int duration = (int) audio.getDuration();
                int time = progress > duration ? progress % duration : progress;
                if (currentAudio != audio||recreate) {
                    currentAudio = audio;
                    recreate=false;
                    addRunWork(() -> {
                        seekPlay(time);
                    });

                } else {
                    if (audioMediaPlayer == null) {
                        return;
                    }
                    int finalProgress = progress;
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
        addRunWork(new Runnable() {
            @Override
            public void run() {
                createAudioplayer(progress);
            }
        });
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
                            if (currentAudio == null) {
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


    public AudioMediaItem getCurrentAudio() {
        return currentAudio;
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
}
