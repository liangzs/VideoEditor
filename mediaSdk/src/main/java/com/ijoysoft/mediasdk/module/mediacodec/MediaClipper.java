package com.ijoysoft.mediasdk.module.mediacodec;

import static android.media.MediaFormat.KEY_BIT_RATE;
import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.ONE_BILLION;
import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.PHTOTO_FPS_TIME;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.IntDef;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.ConstantPath;
import com.ijoysoft.mediasdk.common.global.ThreadPoolMaxThread;
import com.ijoysoft.mediasdk.common.utils.FileUtils;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.common.utils.TimeUtil;
import com.ijoysoft.mediasdk.module.entity.AudioInsertItem;
import com.ijoysoft.mediasdk.module.entity.AudioMediaItem;
import com.ijoysoft.mediasdk.module.entity.DoodleItem;
import com.ijoysoft.mediasdk.module.entity.DurationInterval;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaType;
import com.ijoysoft.mediasdk.module.entity.VideoMediaItem;
import com.ijoysoft.mediasdk.module.ffmpeg.VideoEditManager;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeHelper;
import com.ijoysoft.mediasdk.module.playControl.MediaConfig;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 视频的生成只能是纯音频了，生成视频文件后，再与混音后的音频进行结合。
 * 如果源是视频，那么就通过mediacodec解码视频，对视频进行推动，然后根据duration是否
 * 在两秒以内，那么就采用转场
 * 如果是图片那么久根据帧率的固定时间去渲染实现就好了
 *
 * @date 20220624 添加录音功能，录音单独处理一份和最终文件进行混音处理
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MediaClipper {
    private static final String TAG = "MediaClipper";
    private List<MediaItem> srcList;
    private List<DoodleItem> doodleList;
    private List<Bitmap> widgetMaps;
    private List<AudioMediaItem> audioList;
    private List<AudioMediaItem> recordList;
    private MediaCodec videoEncoder;
    private MediaExtractor mVideoExtractor;
    private MediaMuxer mMediaMuxer;
    private static ExecutorService executorService = Executors.newFixedThreadPool(1);
    private int muxVideoTrack = -1;
    private OutputSurface outputSurface = null;
    private InputSurface inputSurface = null;
    private Object lock = new Object();
    private static final int LOCK_TIME = 1500;
    private boolean muxStarted = false;
    private MediaConfig mediaConfig;
    private String silencePath;
    private String audioOutputPath;
    private String videoOutputPath;
    private String ouputPath;
    private long ptsOffset;// 这个是定义多个视轨直接的距离问题,用微妙做单位
    private long mediaPts;
    private boolean videoMurgeFlag, audioMurgeFlag, audiomurgeFail;
    private OnVideoCutFinishListener listener;// 重现优化进度方案
    // 视频的合成占80部分，音频占用20
    private float currentPositoin;
    private Handler handler;
    //是否为主题合成,因为主题合成音频会少很多操作,且没有视频元素

    // 初始化音视频解码器和编码器
    public MediaClipper(MediaConfig mediaConfig) {
        this.mediaConfig = mediaConfig;
        handler = new Handler(Looper.getMainLooper());
        try {
            // 解码器在 MediaFormat format = mVideoExtractor.getTrackFormat(j);这里进行自动匹配
            // videoDecoder = MediaCodec.createDecoderByType("video/avc");
            videoEncoder = MediaCodec.createEncoderByType("video/avc");
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoMurgeFlag = audioMurgeFlag = false;
    }

    /**
     * 如果不是主题合成，素材采用最原始的素材大小
     *
     * @param srcList
     * @param doodleItems
     * @param audioMediaItems
     */
    public void setDataSource(List<MediaItem> srcList, List<DoodleItem> doodleItems,
                              List<AudioMediaItem> audioMediaItems, List<AudioMediaItem> recordList, List<Bitmap> widgetMaps) {
        this.srcList = srcList;
        this.doodleList = doodleItems;
        this.audioList = audioMediaItems;
        this.recordList = recordList;
        this.widgetMaps = widgetMaps;
    }


    public void setOnVideoCutFinishListener(OnVideoCutFinishListener listener) {
        this.listener = listener;
    }

    /**
     * 合成视频
     * 后续版本建议加上低分辨率的输出，高分辨率输出提示输出失败，然后吐词用户使用低分辨率输出
     *
     * @throws IOException
     */
    public void murgeMedia(String outputPath) {
        murgeStatus = MURGING;
        ptsOffset = 0;
        this.ouputPath = outputPath;
        videoOutputPath = ConstantPath.createVideoOutputPath(mediaConfig.getProjectId());
        try {
            mMediaMuxer = new MediaMuxer(videoOutputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            if (executorService == null) {
                executorService = Executors.newFixedThreadPool(1);
            }
            executorService.execute(encoderRuner);
        } catch (IOException e) {
            e.printStackTrace();
        }
        murgeAudio();
    }

    private @MurgeStatus
    int murgeStatus;
    private Runnable encoderRuner = new Runnable() {
        @Override
        public void run() {
            LogUtils.i(TAG, "videoCliper..");
            initMediaCodec();// 初始化编解码，封装等信息
            for (int i = 0; i < srcList.size(); i++) {
                if (murgeStatus == CANCEL) {
                    break;
                }
                //开始编码
                startMediaCodec(videoEncoder, i, i == srcList.size() - 1);
                // 这里设置muxuer的视轨的距离值,即当前视频的最后一帧pts，当做下一个视频的第一帧pts
                ptsOffset += mediaPts;
                // 30帧,那么每帧是33.33ms，25帧则40ms
                ptsOffset += PHTOTO_FPS_TIME * 1000L;
            }
            ThreadPoolMaxThread.getInstance().execute(() -> release());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (murgeStatus == CANCEL) {
                return;
            }
            videoMurgeFlag = true;
            videoMarryAudio();
        }
    };

    /**
     * 计算当前进度
     *
     * @param currentUs
     */
    private void calcuProgress(long currentUs) {
        if (currentUs / 1000 - currentPositoin > 200) {// 300ms间距进行更新，不然太频繁
            currentPositoin = currentUs / 1000;
            if (listener != null) {
                int percent = (int) currentPositoin * 95 / mediaConfig.getDuration();
                if (percent < 1 || percent > 95) {
                    return;
                }
                listener.progress(percent);
            }
        }
    }

    /**
     * 初始化输出参数
     */
    private void initMediaCodec() {
        MediaFormat mediaFormat;
        remain = 0;
        mediaFormat = MediaFormat.createVideoFormat("video/avc", mediaConfig.getExportWidth(false)[0],
                mediaConfig.getExportWidth(false)[1]);
        // byte[] header_sps = {0, 0, 0, 1, 103, 100, 0, 31, -84, -76, 2, -128, 45, -56};
        // byte[] header_pps = {0, 0, 0, 1, 104, -18, 60, 97, 15, -1, -16, -121, -1, -8, 67, -1, -4, 33, -1, -2, 16, -1,
        // -1, 8, 127, -1, -64};
        // mediaFormat.setByteBuffer("csd-0", ByteBuffer.wrap(header_sps));
        // mediaFormat.setByteBuffer("csd-1", ByteBuffer.wrap(header_pps));
        // 设置视频的编码参数
        mediaFormat.setInteger(MediaFormat.KEY_BITRATE_MODE, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CBR);
        mediaFormat.setInteger(KEY_BIT_RATE, mediaConfig.getMatchRate());
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, ConstantMediaSize.FPS);
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);// 关键帧设为0，每帧都是关键帧
        try {
            videoEncoder.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            inputSurface = new InputSurface(videoEncoder.createInputSurface());
            // 解码过程，配合decoder进行滤镜添加
            // 建立egl，初始化GL环境，把屏幕的资源送到ecoder
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) {
                listener.error();
                murgeStatus = CANCEL;
            }
            return;
        }
        inputSurface.makeCurrent();
        try {
            videoEncoder.start();// 解码器启动
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtils.i(TAG, "initMediaCodec");
        outputSurface = new OutputSurface(doodleList, srcList, widgetMaps, mediaConfig);
    }


    /**
     * 图片编码,解码不需要，因为是直接通过离屏，即屏幕把数据swapper传给encoder的surface，然后encoder就会有数据过来了
     * <p>
     * 用毫秒进行运算把，转换成秒有时候损失太大
     */
    // 不够整除的余数保留下来，分给下次的图片进行显示（极少成多)
    // 主要是出现这种情况，比如选了10个以上的0.1s的图片，2.5显示的次数，这样偏差越来越大
    private int remain;

    private void startMediaCodec(MediaCodec encoder, int index, boolean isEnd) {
        int tranPosition = 0;
        MediaItem item = srcList.get(index);
        for (int i = 0; i < srcList.indexOf(item); i++) {
            tranPosition += srcList.get(i).getFinalDuration();
        }
        //片段切换
        outputSurface.mediaPrepare(index, tranPosition);
        //如果是视频片段，检测是否有加速设置
        int fpsCount = ((int) item.getVideoSpeedDuration() + remain) / PHTOTO_FPS_TIME;
        //根据主题类型，去除部分主题最后出场画面
        if (ConstantMediaSize.isTheme() && !ThemeHelper.isTimeTheme(ConstantMediaSize.themeType)) {
            LogUtils.i(TAG, "Is theme but not time theme, cut end offset");
            if (srcList.indexOf(item) == srcList.size() - 1) {

                fpsCount = fpsCount - ConstantMediaSize.themeType.getEndOffset() / PHTOTO_FPS_TIME;
            }
        }
        remain = ((int) item.getVideoSpeedDuration() + remain) % PHTOTO_FPS_TIME;
        for (int frameIndex = 0; frameIndex < fpsCount; frameIndex++) {
            if (murgeStatus == CANCEL) {
                return;
            }
            long presentationTimeNsec = computePresentationTimeNsec(frameIndex);
            try {
                drainEncoder(encoder, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaPts = presentationTimeNsec / 1000;
            outputSurface.drawImage((int) ((ptsOffset + mediaPts) / 1000), mediaPts);
            // Send it to the encoder.
            // 需要计算其时间戳，如果是图片的话就根据帧来计算时间就好了
            inputSurface.setPresentationTime(ptsOffset * 1000 + presentationTimeNsec);
            calcuProgress(ptsOffset + presentationTimeNsec / 1000);
            inputSurface.swapBuffers();
        }
        try {
            drainEncoder(encoder, isEnd);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 计算时间戳 fps=30帧
     *
     * @param frameIndex
     * @return
     */
    private long computePresentationTimeNsec(int frameIndex) {
        return frameIndex * ONE_BILLION / ConstantMediaSize.FPS;
    }

    /**
     * Extracts all pending data from the encoder and forwards it to the muxer.
     * <p>
     * If endOfStream is not set, this returns when there is no more data to drain.  If it
     * is set, we send EOS to the encoder, and then iterate until we see EOS on the output.
     * Calling this with endOfStream set should be done once, right before stopping the muxer.
     * <p>
     * We're just using the muxer to get a .mp4 file (instead of a raw H.264 stream).  We're
     * not recording audio.
     */
    private void drainEncoder(MediaCodec encoder, boolean endOfStream) {
        MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();
        final int TIMEOUT_USEC = 10000;
        if (endOfStream) {
            Log.d(TAG, "sending EOS to encoder");
            Log.i(TAG, "photo--signalEndOfInputStream............");
            encoder.signalEndOfInputStream();
        }

        ByteBuffer[] encoderOutputBuffers = encoder.getOutputBuffers();
        while (true) {
            if (murgeStatus == CANCEL) {
                break;
            }
            int encoderStatus = encoder.dequeueOutputBuffer(mBufferInfo, TIMEOUT_USEC);
            if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                // no output available yet
                if (!endOfStream) {
                    break; // out of while
                } else {
                    Log.d(TAG, "no output available, spinning to await EOS");
                }
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                // not expected for an encoder
                encoderOutputBuffers = encoder.getOutputBuffers();
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                // should happen before receiving buffers, and should only happen once
                MediaFormat newFormat = encoder.getOutputFormat();
                startMux(newFormat);
            } else if (encoderStatus < 0) {
                Log.w(TAG, "unexpected result from encoder.dequeueOutputBuffer: " + encoderStatus);
                // let's ignore it
            } else {
                ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
                if (encodedData == null) {
                    throw new RuntimeException("encoderOutputBuffer " + encoderStatus + " was null");
                }

                if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                    // The codec config data was pulled out and fed to the muxer when we got
                    // the INFO_OUTPUT_FORMAT_CHANGED status. Ignore it.
                    Log.d(TAG, "ignoring BUFFER_FLAG_CODEC_CONFIG");
                    mBufferInfo.size = 0;
                }
                if (mBufferInfo.size != 0) {
                    if (!muxStarted) {
                        synchronized (lock) {
                            if (!muxStarted) {
                                try {
                                    lock.wait(LOCK_TIME);
                                    if (!muxStarted) {
                                        cancelMurge();
                                        if (listener != null) {
                                            listener.error();
                                        }
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    encodedData.position(mBufferInfo.offset);
                    encodedData.limit(mBufferInfo.offset + mBufferInfo.size);
                    mMediaMuxer.writeSampleData(muxVideoTrack, encodedData, mBufferInfo);
                }
                encoder.releaseOutputBuffer(encoderStatus, false);
                if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    if (!endOfStream) {
                        Log.w(TAG, "reached end of stream unexpectedly");
                    } else {
                        Log.d(TAG, "end of stream reached");
                    }
                    break; // out of while
                }
            }
        }
    }

    /**
     * 当收到INFO_OUTPUT_FORMAT_CHANGED的指令后，设置mediaMuxer的值
     * 如果这个方法不可行，那么在遍历srclist之前设置这个muxer
     *
     * @param mediaFormat
     */
    private void startMux(MediaFormat mediaFormat) {
        Log.w(TAG, "startMux start");
        muxVideoTrack = mMediaMuxer.addTrack(mediaFormat);
        synchronized (lock) {
            if (muxVideoTrack != -1 && !muxStarted) {
                mMediaMuxer.start();
                muxStarted = true;
                lock.notify();
            }
        }
    }

    private void release() {
        try {
            if (mVideoExtractor != null) {
                mVideoExtractor.release();
                mVideoExtractor = null;
            }
            if (mMediaMuxer != null) {
                mMediaMuxer.stop();
                mMediaMuxer.release();
            }
            if (outputSurface != null) {
                outputSurface.release();
            }
            if (inputSurface != null) {
                inputSurface.release();
            }
            if (videoEncoder != null) {
                videoEncoder.stop();
                videoEncoder.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 音频合成
     * 前提：在多段音频的裁剪页面中，根据时间轴的调整变化值
     * 做保存是，如果裁剪小于逻辑长度，则进行物理合成，反之则再进行物理裁剪，并清遗留碎片
     * 如果是单段音频的话，则对此音频长度与总体项目长度进行对比进行裁剪，并清遗留碎片
     * <p>
     * 生成总时长的静音文件
     * 遍历media，对video中的原音进行抽取origin1,origin2
     * 根据音量调节结果，修改原音origin和外音music音量
     * 把片段音频插入静音文件,根据标志，是否存在淡入淡出音效
     * 静音文件与视频文件进行合成
     * 清除过程碎片数据
     */
    private Runnable audioRunable = new Runnable() {
        @Override
        public void run() {
            Log.i("FfmpegBackgroundService", "audioRunable....");
            while (FfmpegBackgroundHelper.getInstance().checkExistExtractAudio()) {
                if (murgeStatus == CANCEL) {
                    return;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (murgeStatus == CANCEL) {
                return;
            }
            // 修改音量,对于视频文件有前置条件，所以只能等待视频的音频抽取完毕之后才能做音量的修改，
            // 所以把有前置条件的执行放到音频抽取之后
            try {
                LogUtils.i(TAG, "changeAllVolume...");
                //生成volumepath路径
                List<String[]> volumeList = changeAllVolume(audioList, recordList, srcList);
                for (int i = 0; i < volumeList.size(); i++) {
                    FfmpegBackgroundHelper.getInstance()
                            .exeCuteTask(new BackroundTask(volumeList.get(i), FfmpegTaskType.COMMON_TASK));
                }
                // 音频（淡入功能）拼接最终音频文件,录音
                List<String[]> inserts = insertComposeAudio(audioList, recordList, srcList);

                for (int i = 0; i < inserts.size(); i++) {
                    FfmpegBackgroundHelper.getInstance()
                            .exeCuteTask(new BackroundTask(inserts.get(i), FfmpegTaskType.COMMON_TASK));
                }
            } catch (Exception e) {
                audioMurgeFlag = true;
                e.printStackTrace();
            }
            while (FfmpegBackgroundHelper.getInstance().getFfmpegStatus() == FfmpegStatus.EXECUTE_START) {
                if (murgeStatus == CANCEL) {
                    return;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (murgeStatus == CANCEL) {
                return;
            }
            Log.i(TAG, "合成结束");
            audioMurgeFlag = true;
            videoMarryAudio();
        }
    };


    private Runnable cutMultiAudioRunable = new Runnable() {
        @Override
        public void run() {
            while (FfmpegBackgroundHelper.getInstance().getFfmpegStatus() == FfmpegStatus.EXECUTE_START && murgeStatus != CANCEL) {
//                LogUtils.i(TAG, "wait..isCancel:" + isCancel);
            }
            if (murgeStatus == CANCEL) {
                return;
            }
            Log.i(TAG, "合成结束");
            audioMurgeFlag = true;
            videoMarryAudio();
        }
    };

    public void murgeAudio() {
        // 检查后台进程是否启动成功
        audiomurgeFail = false;
        FfmpegBackgroundHelper.getInstance().clearTasks();
        FfmpegBackgroundHelper.getInstance().restartService();
        silencePath = ConstantPath.createSilenceAudioPath(mediaConfig.getProjectId());
        FfmpegBackgroundHelper.getInstance().setCallback(() -> {
            if (listener != null) {
                listener.audioError();
            }
        });
        FfmpegBackgroundHelper.getInstance()
                .exeCuteTask(new BackroundTask(
                        VideoEditManager.createSilenceAudio(silencePath,
                                TimeUtil.getSecondOneDecimal(mediaConfig.getDuration()), null, false),
                        FfmpegTaskType.COMMON_TASK));

        // 对音频文件进行再裁剪
        dealAudioDuration(audioList);
        ThreadPoolMaxThread.getInstance().execute(audioRunable);
    }

    /**
     * 如果取消，则对该工程里面的存量数据进行清理，即对未重命名的文件都清理掉
     * 在执行过程中，不再对音频文件进行重命名了
     */
    public void cancelMurge() {
        long start = System.currentTimeMillis();
        Log.i(TAG, "cancelMurge`............");
        FfmpegBackgroundHelper.getInstance().clearCallback();
        handler.removeCallbacksAndMessages(null);
        murgeStatus = CANCEL;
        currentPositoin = 0;
        FfmpegBackgroundHelper.getInstance().clearTasks();
        LogUtils.v("MediaClipper", "cancelMurge1:" + (System.currentTimeMillis() - start));
        // 遍历清理产生的临时文件(再裁剪文件、音量文件、静音文件）
        List<String> paths = new ArrayList<>();
        if (audioList != null) {
            for (AudioMediaItem audioMediaItem : audioList) {
                paths.add(audioMediaItem.getMurgePath());
                paths.add(audioMediaItem.getVolumePath());
                paths.add(audioMediaItem.getMurgeOffsetPath());
            }
        }

        if (srcList != null) {
            for (MediaItem mediaItem : srcList) {
                if (mediaItem instanceof VideoMediaItem) {
                    paths.add(((VideoMediaItem) mediaItem).getVolumePath());
                }
            }
        }
        ThreadPoolMaxThread.getInstance().execute(() -> {
            clearShowedMedia(paths);
        });
        if (executorService != null) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    release();
                    LogUtils.v("MediaClipper", "cancelMurg3:" + (System.currentTimeMillis() - start));
                }
            });
            executorService.shutdown();
        }
        executorService = null;
        LogUtils.v("MediaClipper", "cancelMurg2:" + (System.currentTimeMillis() - start));
    }

    /**
     * 清理显示资源
     */
    public void clearShowedMedia(List<String> paths) {
        for (String path : paths) {
            FileUtils.delete(path);
        }
        FileUtils.delete(silencePath);
        FileUtils.delete(audioOutputPath);
        FileUtils.delete(videoOutputPath);
    }

    /**
     * 清理临时数据
     */
    public void clearTemp() {
        ThreadPoolMaxThread.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                for (AudioMediaItem audioMediaItem : audioList) {
                    FileUtils.delete(audioMediaItem.getMurgePath());
                    FileUtils.delete(audioMediaItem.getVolumePath());
                    FileUtils.delete(audioMediaItem.getMurgeOffsetPath());
                }
                for (MediaItem mediaItem : srcList) {
                    if (mediaItem instanceof VideoMediaItem) {
                        FileUtils.delete(((VideoMediaItem) mediaItem).getVolumePath());
                    }
                }
                FileUtils.delete(silencePath);
                FileUtils.delete(audioOutputPath);
                FileUtils.delete(videoOutputPath);
            }
        });
    }


    /**
     * 音视频最终合并,可喜可贺
     * 有些手机有一些奇葩错误，当且是纯图片时，没有音频时，合成会出错
     */
    private void videoMarryAudio() {
        if (audioMurgeFlag && videoMurgeFlag) {
            if (audiomurgeFail || FfmpegBackgroundHelper.getInstance().getFfmpegStatus() == FfmpegStatus.EXCUTE_ERROR) {
                try {
                    FileUtils.copy(videoOutputPath, ouputPath, false);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (listener != null) {
                        listener.onFinish();
                    }
                }
                return;
            }
            boolean hasVideo = false;
            for (MediaItem m : srcList) {
                if (m.getMediaType() == MediaType.VIDEO) {
                    hasVideo = true;
                    break;
                }
            }
            if (ObjectUtils.isEmpty(audioList) && !hasVideo) {
                FileUtils.renameFile(videoOutputPath, ouputPath);
                if (listener != null) {
                    listener.onFinish();
                }
                return;
            }
            // 发现有的用户没有视频生成
            if (!FileUtils.checkFileExistAndEmpty(videoOutputPath)) {
                if (listener != null) {
                    listener.error();
                    murgeStatus = CANCEL;
                }
                return;
            }
            // 检测最终的音频文件有没有生成
            if (!FileUtils.checkFileExistAndEmpty(audioOutputPath)) {
                try {
                    FileUtils.copy(videoOutputPath, ouputPath, false);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (listener != null) {
                        listener.onFinish();
                    }
                }
                return;
            }
            String tempOutpath = ConstantPath.createVideoOutputPath(mediaConfig.getProjectId());
            String[] command =
                    VideoEditManager.composeVideoAudio(videoOutputPath, audioOutputPath, tempOutpath, null, false);
            FfmpegBackgroundHelper.getInstance().exeCuteTaskProgress(new BackroundTask(command, mediaConfig.getDuration(), FfmpegTaskType.COMMON_TASK),
                    new FfmpegBackgroundHelper.SingleCommanCallback() {
                        @Override
                        public void finish() {
                            //回调后这里的环境为主线环境
                            LogUtils.v("videoMarryAudio", "Thread:" + Thread.currentThread().getName());
                            ThreadPoolMaxThread.getInstance().execute(new Runnable() {
                                @Override
                                public void run() {
                                    FileUtils.renameFile(tempOutpath, ouputPath);
                                    if (FileUtils.checkFileExist(ouputPath)) {
                                        if (listener != null) {
                                            listener.onFinish();
                                        }
                                    } else {
                                        error();
                                    }
                                }
                            });
                        }

                        @Override
                        public void progress(int progress) {
                            if (listener != null) {
                                listener.progress((95 + (int) (0.05f * progress)));
                            }
                        }

                        @Override
                        public void error() {
                            Log.i(TAG, "合成失败");
                            ThreadPoolMaxThread.getInstance().execute(new Runnable() {
                                @Override
                                public void run() {
                                    murgeFail();
                                    if (listener != null) {
                                        listener.error();
                                        murgeStatus = CANCEL;
                                    }
                                }
                            });
                            murgeStatus = CANCEL;
                        }
                    });
        }
    }

    /**
     * 清理数据，包括合成视频静音文件，音频空白文件，调音量渐变文件
     */
    private void murgeFail() {
        FileUtils.delete(silencePath);
        FileUtils.delete(audioOutputPath);
        FileUtils.delete(videoOutputPath);
        if (ObjectUtils.isEmpty(audioList)) {
            return;
        }
        for (AudioMediaItem audioMediaItem : audioList) {
            FileUtils.delete(audioMediaItem.getVolumePath());
        }
        if (ObjectUtils.isEmpty(srcList)) {
            return;
        }
        for (MediaItem mediaItem : srcList) {
            if (mediaItem instanceof VideoMediaItem) {
                if (((VideoMediaItem) mediaItem).getExtracAudioPath() == null
                        || ((VideoMediaItem) mediaItem).getExtracAudioPath().isEmpty()) {
                    continue;
                }
                // 这里检查是否有视频裁剪动作,目前video音频抽取没有做后缀rename
                if (!ObjectUtils.isEmpty(((VideoMediaItem) mediaItem).getVideoTrimAudioPath())) {
                    FileUtils.delete(((VideoMediaItem) mediaItem).getVideoTrimAudioPath());
                }
                FileUtils.delete(((VideoMediaItem) mediaItem).getExtracAudioPath());
                FileUtils.delete(((VideoMediaItem) mediaItem).getVolumePath());
            }
        }
    }

    /**
     * 处理每一个音频片段，如果物理时长大于逻辑时长，则进行裁剪
     * 如果物理时长小于逻辑时长，则进行裁剪合并
     * <p>
     * 针对很多音频片段合成时候，进行特殊处理，即把这个动作放
     * 远程服务区执行，当如果有新的音频选择时，进行清理动作。
     *
     * @TODO 2020/4/20 决定终止在选择音乐结束了对音频进行再裁剪，把这部分工作放到合成按钮的时候再做操作！
     * 另：不再对视频文件进行重命名私化
     */
    public void dealAudioDuration(List<AudioMediaItem> audioList) {
        if (audioList == null || audioList.isEmpty()) {
            return;
        }
        for (AudioMediaItem audio : audioList) {
            if (!FileUtils.checkFileExistAndEmpty(audio.getPath()) || audio.getDuration() == 0L) {
                continue;
            }
            if (audio.getDurationInterval() == null) {
                audio.setDurationInterval(new DurationInterval(0, mediaConfig.getDuration()));
            }
            if (Math.abs(audio.getDuration() - audio.getDurationInterval().getInterval()) < 500) {
                audio.setMurgePath(audio.getPath());
                continue;
            }
            if (audio.getDurationInterval().getInterval() < 100) {
                audio.setMurgePath(audio.getPath());
                continue;
            }
            int period = audio.getDurationInterval().getInterval();
            period = Math.min(period, mediaConfig.getDuration());
            if (audio.getDuration() > period) {
                String murgePath = ConstantPath.createAudioMurgePath(audio.getProjectId());
                audio.setMurgePath(murgePath);
                BackroundTask backroundTask = new BackroundTask(
                        VideoEditManager.cutAudio(audio.getPath(), murgePath, TimeUtil.getCutForamt(0),
                                TimeUtil.getCutForamt(period), null, false),
                        FfmpegTaskType.COMMON_TASK);
                FfmpegBackgroundHelper.getInstance().exeCuteTask(backroundTask);
            } else {
                int count = (int) (period / audio.getDuration());
                int offset = (int) (period % audio.getDuration());
                String murgePath = ConstantPath.createAudioMurgePath(audio.getProjectId());
                audio.setMurgePath(murgePath);
                // 差值大于100ms才进行切割
                String[] array = new String[count];
                for (int i = 0; i < array.length; i++) {
                    array[i] = audio.getPath();
                }
                if (offset >= 500) {
                    String murgeOffsetPath = ConstantPath.createAudioMurgePathOffset(audio.getProjectId());
                    audio.setMurgeOffsetPath(murgeOffsetPath);
                    BackroundTask backroundTask = new BackroundTask(
                            VideoEditManager.cutAudio(audio.getPath(), audio.getMurgeOffsetPath(),
                                    TimeUtil.getCutForamt(0), TimeUtil.getCutForamt(offset), null, false),
                            FfmpegTaskType.COMMON_TASK);
                    FfmpegBackgroundHelper.getInstance().exeCuteTask(backroundTask);
                    array = new String[count + 1];
                    for (int i = 0; i < array.length - 1; i++) {
                        array[i] = audio.getPath();
                    }
                    array[count] = audio.getMurgeOffsetPath();
                }
//                String[] command =
//                        VideoEditManager.videoMurgeFromFilelist(null, audio.getMurgePath(), false, false, null, array);
                List<String[]> list = VideoEditManager.composeMultiAudio(audio.getMurgePath(), null, false,
                        array);
                FfmpegBackgroundHelper.getInstance().exeCuteTask(list, null);
            }
        }
    }


    /**
     * 修改原音和外音的音量大小，在调整过程中，外音是主动的，原音是被动的，当调整seekbar
     * 修改两方的音量时，以外音为主，就是说原音有可能被两部分外音占有，当这种情况下，根据占有的时间戳，
     * 修改原音对应的音量大小，默认都是是半音0.5
     *
     * @// TODO: 2022/4/11 添加时间加速处理
     */
    private List<String[]> changeAllVolume(List<AudioMediaItem> audioMediaItems, List<AudioMediaItem> recordList, List<MediaItem> mediaItems) {
        List<String[]> cmdList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(audioMediaItems)) {
            for (AudioMediaItem audioMediaItem : audioMediaItems) {
                if (ObjectUtils.isEmpty(audioMediaItem.getMurgePath())) {
                    continue;
                }
                audioMediaItem.setVolumePath(ConstantPath.createAudioVolumePath(mediaConfig.getProjectId()));
                cmdList.add(VideoEditManager.changeAudioVolome(audioMediaItem.getMurgePath(), audioMediaItem.getVolume() / 100f,
                        audioMediaItem.getVolumePath(), null, false));
            }
        }
        if (!ObjectUtils.isEmpty(recordList)) {
            for (AudioMediaItem audioMediaItem : recordList) {
                audioMediaItem.setVolumePath(ConstantPath.createAudioVolumePath(mediaConfig.getProjectId()));
                cmdList.add(VideoEditManager.changeAudioVolome(audioMediaItem.getPath(), audioMediaItem.getVolume() / 100f,
                        audioMediaItem.getVolumePath(), null, false));
            }
        }

        if (!ObjectUtils.isEmpty(mediaItems)) {
            for (MediaItem mediaItem : mediaItems) {
                if (mediaItem instanceof VideoMediaItem) {
                    if (!FileUtils.checkFileExistAndEmpty(((VideoMediaItem) mediaItem).getExtracAudioPath())) {
                        continue;
                    }
                    // 这里检查是否有视频裁剪动作,目前video音频抽取没有做后缀rename
                    String sourcePath = ((VideoMediaItem) mediaItem).getExtracAudioPath();
                    //如果有视频裁剪，则取视频裁剪后的音频路径
                    if (!ObjectUtils.isEmpty(((VideoMediaItem) mediaItem).getVideoTrimAudioPath())) {
                        sourcePath = ((VideoMediaItem) mediaItem).getVideoTrimAudioPath();
                    }
                    //如果有视频加速，则对音频做一次一次加速处理，合成时做音频的加速处理
                    if (mediaItem.getSpeed() != 0) {
                        if (ObjectUtils.isEmpty(((VideoMediaItem) mediaItem).getSpeedPath())) {
                            ((VideoMediaItem) mediaItem).setSpeedPath(ConstantPath.createVideoSpeedPath(mediaItem.getProjectId()));
                        }
                        cmdList.add(VideoEditManager.adjustAudioSpeed(sourcePath, mediaItem.getSpeed(), ((VideoMediaItem) mediaItem).getSpeedPath(), false, null));
                        sourcePath = ((VideoMediaItem) mediaItem).getSpeedPath();
                    }
                    ((VideoMediaItem) mediaItem)
                            .setVolumePath(ConstantPath.createAudioVolumePath(mediaConfig.getProjectId()));
                    //现只做一次音量的改变
                    cmdList.add(VideoEditManager.changeAudioVolome(sourcePath, ((VideoMediaItem) mediaItem).getVolume() / 100f, ((VideoMediaItem) mediaItem).getVolumePath(),
                            null, false));
                }
            }
        }
        return cmdList;
    }


    /**
     * 最终合成，把所有的音频片段插入到静音文件中
     * 如果有淡入淡出标志，则进行淡入淡出处理
     *
     * @since v160 加入录音功能
     */
    private List<String[]> insertComposeAudio(List<AudioMediaItem> audioMediaItems, List<AudioMediaItem> recordList, List<MediaItem> mediaItems) {
        LogUtils.i(TAG, "insertComposeAudio...");
        audioOutputPath = ConstantPath.createAudioOutputPath(mediaConfig.getProjectId());
        List<AudioInsertItem> list = new ArrayList<>();
        if (!ObjectUtils.isEmpty(audioMediaItems)) {
            for (AudioMediaItem audioMediaItem : audioMediaItems) {
                if (ObjectUtils.isEmpty(audioMediaItem.getVolumePath())
                        || audioMediaItem.getDurationInterval() == null) {
                    continue;
                }
                list.add(new AudioInsertItem(
                        mediaConfig.isFading()
                                && audioMediaItem.getDurationInterval().getInterval() > ConstantMediaSize.FADE_TIME,
                        audioMediaItem.getDurationInterval().getStartDuration(),
                        audioMediaItem.getDurationInterval().getEndDuration(), audioMediaItem.getVolumePath()));
            }
        }

        int durationTemp = 0;
        if (!ObjectUtils.isEmpty(mediaItems)) {
            for (MediaItem mediaItem : mediaItems) {
                if (mediaItem.getMediaType() == MediaType.PHOTO) {
                    durationTemp += (int) mediaItem.getDuration();
                    continue;
                }
                if (ObjectUtils.isEmpty(((VideoMediaItem) mediaItem).getVolumePath())) {
                    durationTemp += (int) mediaItem.getVideoSpeedDuration();
                    continue;
                }

                list.add(new AudioInsertItem(false, durationTemp, (int) (durationTemp + mediaItem.getFinalDuration()),
                        ((VideoMediaItem) mediaItem).getVolumePath()));
                durationTemp += (int) mediaItem.getVideoSpeedDuration();
            }
        }

        //录音文件
        if (!ObjectUtils.isEmpty(recordList)) {
            for (AudioMediaItem audioMediaItem : recordList) {
                if (ObjectUtils.isEmpty(audioMediaItem.getVolumePath()) ||
                        audioMediaItem.getDurationInterval().getInterval() < ConstantMediaSize.MIN_RECORD) {
                    continue;
                }
                list.add(new AudioInsertItem(false,
                        audioMediaItem.getDurationInterval().getStartDuration(),
                        audioMediaItem.getDurationInterval().getEndDuration(), audioMediaItem.getVolumePath()));
            }
        }
        if (list.isEmpty()) {
            audioOutputPath = silencePath;
            return new ArrayList<>();
        }
        return VideoEditManager.inserMultiAudio(audioOutputPath, silencePath, list);
    }

    /**
     * 针对多文件不同视频，不同分辨率的文件，因为ffmpeg转化也慢，另外分辨率不同的话还有拉伸问题
     * 所以统一在这里进行生成
     *
     * @param commands
     */
    public void cutMultiVideo(List<String[]> commands, List<MediaItem> dataSource, final String resultPath) {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(1);
        }
        FfmpegBackgroundHelper.getInstance().restartService();
        srcList = dataSource;
        murgeStatus = MURGING;
        // 做好ffmpeg的准备工作
        if (listener != null) {
            listener.progress(1);
        }
        videoOutputPath = ConstantPath.createVideoOutputPath("trim");
        if (commands.size() > 0) {
            FfmpegBackgroundHelper.getInstance().exeCuteTask(commands,
                    new FfmpegBackgroundHelper.SingleCommanCallback() {
                        @Override
                        public void finish() {
                            ptsOffset = 0;
                            ouputPath = resultPath;

                            try {
                                mMediaMuxer = new MediaMuxer(videoOutputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                                executorService.execute(encoderRuner);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    cutMultiVideoOfAudio();
                                }
                            }, 300);
                        }

                        @Override
                        public void error() {
                            if (listener != null) {
                                listener.error();
                                murgeStatus = CANCEL;
                            }
                        }
                    });
        } else {
            ptsOffset = 0;
            ouputPath = resultPath;
            try {
                mMediaMuxer = new MediaMuxer(videoOutputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                executorService.execute(encoderRuner);
            } catch (IOException e) {
                e.printStackTrace();
            }
            cutMultiVideoOfAudio();
        }

    }

    /**
     * 视频裁剪，音频处理部分,针对这部分的音频，放到后台进行去处理，以免发生不测
     */
    private void cutMultiVideoOfAudio() {
        // 检查所有的文件是否是否是没有音频的状况，如果没有音频就不往下走了
        boolean checkall = false;
        for (int i = 0; i < srcList.size(); i++) {
            if (FileUtils.checkExistAudio(srcList.get(i).getPath())) {
                checkall = true;
                break;
            }
        }
        if (!checkall) {
            audiomurgeFail = true;
            audioMurgeFlag = true;
            videoMarryAudio();
            return;
        }

        // 抽取音频对音频进行拼接，视频合成之后和音频进行合并
        List<String[]> audioCommands = new ArrayList<>();
        final List<String> audiolist = new ArrayList<>();
        // 抽取音频的时候，因为文件中音频数据不定是MP3需要的音频，很多时候需要转码，这就有了问题
        for (int i = 0; i < srcList.size(); i++) {
            // 检测视频文件是否有音频，如果没有音频则生成静音文件（没有音频文件抽取也是报错的）
            String path = ConstantPath.createExtractVideo2Audio("trim", i + "");
            if (!FileUtils.checkExistAudio(srcList.get(i).getPath())) {
                audioCommands.add(VideoEditManager.createSilenceAudio(path,
                        TimeUtil.getSecondOneDecimal(srcList.get(i).getDuration()), null, false));
                audiolist.add(path);
                continue;
            }
            audioCommands.add(VideoEditManager.extractAudioMp3(srcList.get(i).getPath(), path, null, false));
            audiolist.add(path);
        }
        audioOutputPath = ConstantPath.createAudioOutputPath("trim");
        audioCommands.addAll(VideoEditManager.composeMultiAudio(audioOutputPath, null, false,
                audiolist.toArray(new String[audiolist.size()])));
        // 这里把这些指令集放到后台进程来执行
        // 检查后台进程是否启动成功
        FfmpegBackgroundHelper.getInstance().restartService();
        for (int i = 0; i < audioCommands.size(); i++) {
            FfmpegBackgroundHelper.getInstance()
                    .exeCuteTask(new BackroundTask(audioCommands.get(i), FfmpegTaskType.COMMON_TASK));
        }
        ThreadPoolMaxThread.getInstance().execute(cutMultiAudioRunable);
    }

    /**
     * 取消裁剪
     */
    public void cancelTrim() {
        FfmpegBackgroundHelper.getInstance().clearCallback();
        murgeStatus = CANCEL;
        currentPositoin = 0;
        FfmpegBackgroundHelper.getInstance().clearTasks();
        if (executorService != null) {
            executorService.shutdownNow();
        }
        executorService = null;
        release();
        FfmpegBackgroundHelper.getInstance().stopTaskAndTerminalService();
        FfmpegBackgroundHelper.getInstance().startService();
    }


    public void setMediaConfig(MediaConfig mediaConfig) {
        this.mediaConfig = mediaConfig;
    }

    public interface OnVideoCutFinishListener {
        void onFinish();

        void progress(int progress);

        void error();

        void audioError();
    }


    /**
     * 暂停合成,暂停合成是切断合成状态，保证ui主线程的调度
     */
    public void onPause() {
        try {
            if (mVideoExtractor != null) {
                mVideoExtractor.release();
                mVideoExtractor = null;
            }
            if (mMediaMuxer != null) {
                mMediaMuxer.stop();
                mMediaMuxer.release();
            }
            if (outputSurface != null) {
                outputSurface.release();
            }
            if (inputSurface != null) {
                inputSurface.release();
            }
            if (videoEncoder != null) {
                videoEncoder.stop();
                videoEncoder.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 合成状态
     */
    public static final int MURGING = 1;
    public static final int PAUSE = 2;
    public static final int CANCEL = 3;

    @IntDef({MURGING, PAUSE, CANCEL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MurgeStatus {
    }
}
