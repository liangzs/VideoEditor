package com.ijoysoft.mediasdk.module.mediacodec;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.ConstantPath;
import com.ijoysoft.mediasdk.common.global.ThreadPoolManager;
import com.ijoysoft.mediasdk.common.global.ThreadPoolMaxThread;
import com.ijoysoft.mediasdk.common.utils.FileUtils;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.common.utils.TimeUtil;
import com.ijoysoft.mediasdk.module.entity.AudioInsertItem;
import com.ijoysoft.mediasdk.module.entity.AudioMediaItem;
import com.ijoysoft.mediasdk.module.entity.DoodleItem;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaType;
import com.ijoysoft.mediasdk.module.entity.PhotoMediaItem;
import com.ijoysoft.mediasdk.module.entity.VideoMediaItem;
import com.ijoysoft.mediasdk.module.ffmpeg.FFmpegCmd;
import com.ijoysoft.mediasdk.module.ffmpeg.SingleCmdListener;
import com.ijoysoft.mediasdk.module.ffmpeg.VideoEditManager;
import com.ijoysoft.mediasdk.module.playControl.MediaConfig;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.media.MediaExtractor.SEEK_TO_PREVIOUS_SYNC;
import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.ONE_BILLION;
import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.PHTOTO_FPS_TIME;

/**
 * 视频的生成只能是纯音频了，生成视频文件后，再与混音后的音频进行结合。
 * 如果源是视频，那么就通过mediacodec解码视频，对视频进行推动，然后根据duration是否
 * 在两秒以内，那么就采用转场
 * 如果是图片那么久根据帧率的固定时间去渲染实现就好了
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MediaClipper {
    private static final String TAG = "MediaClipper";
    final int TIMEOUT_USEC = 0;
    private List<MediaItem> srcList;
    private List<DoodleItem> doodleList;
    private List<AudioMediaItem> audioList;
    private MediaCodec videoDecoder;
    private MediaCodec videoEncoder;
    private MediaExtractor mVideoExtractor;
    private MediaMuxer mMediaMuxer;
    private static ExecutorService executorService = Executors.newFixedThreadPool(1);
    private int muxVideoTrack = -1;
    private int videoTrackIndex = -1;
    private OutputSurface outputSurface = null;
    private InputSurface inputSurface = null;
    private MediaFormat videoFormat;
    private long after;
    private Object lock = new Object();
    private boolean muxStarted = false;
    // 源文件列表
    private long videoCur;
    private long before;
    private MediaConfig mediaConfig;
    private String silencePath;
    private String audioOutputPath;
    private String videoOutputPath;
    private String ouputPath;
    private long ptsOffset;// 这个是定义多个视轨直接的距离问题,用微妙做单位
    private long mediaPts;
    private boolean isEnd;
    private boolean videoMurgeFlag, audioMurgeFlag;
    private OnVideoCutFinishListener listener;// 重现优化进度方案
    // 视频的合成占80部分，音频占用20
    private float currentPositoin;

    // 初始化音视频解码器和编码器
    public MediaClipper(MediaConfig mediaConfig) {
        this.mediaConfig = mediaConfig;
        try {
            // 解码器在 MediaFormat format = mVideoExtractor.getTrackFormat(j);这里进行自动匹配
            // videoDecoder = MediaCodec.createDecoderByType("video/avc");
            videoEncoder = MediaCodec.createEncoderByType("video/avc");
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoMurgeFlag = audioMurgeFlag = false;
    }

    public void setDataSource(List<MediaItem> srcList, List<DoodleItem> doodleItems,
                              List<AudioMediaItem> audioMediaItems) {
        this.srcList = srcList;
        this.doodleList = doodleItems;
        this.audioList = audioMediaItems;
    }

    public void setOnVideoCutFinishListener(OnVideoCutFinishListener listener) {
        this.listener = listener;
    }

    /**
     * 合成视频
     *
     * @throws IOException
     */
    public void murgeMedia(String outputPath) {
        isCancel = false;
        ptsOffset = 0;
        this.ouputPath = outputPath;
        videoOutputPath = ConstantPath.createVideoOutputPath(mediaConfig.getProjectId());
        try {
            mMediaMuxer = new MediaMuxer(videoOutputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            executorService.execute(videoCliper);
        } catch (IOException e) {
            e.printStackTrace();
        }
        murgeAudio();
    }

    private boolean isCancel;
    private Runnable videoCliper = new Runnable() {
        @Override
        public void run() {
            before = System.currentTimeMillis();
            initMediaCodec();// 初始化编解码，封装等信息
            for (int i = 0; i < srcList.size(); i++) {
                if (isCancel) {
                    break;
                }
                MediaItem item = srcList.get(i);
                isEnd = false;
                if (i == srcList.size() - 1) {
                    isEnd = true;
                }
                // 如果是视频文件，则做如下操作
                if (item instanceof VideoMediaItem) {
                    releaseDecoderResources();
                    String path = ObjectUtils.isEmpty(item.getTrimPath()) ? item.getPath() : item.getTrimPath();
                    // 选取编解码
                    LogUtils.e(TAG, "_____videoCliper------run--path:+" + path);
                    try {
                        mVideoExtractor = new MediaExtractor();
                        mVideoExtractor.setDataSource(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for (int j = 0; j < mVideoExtractor.getTrackCount(); j++) {
                        MediaFormat format = mVideoExtractor.getTrackFormat(j);
                        if (format.getString(MediaFormat.KEY_MIME).startsWith("video/")) {
                            videoTrackIndex = j;
                            videoFormat = format;
                            // 解码采用原有手机特定的匹配解码
                            String mime = format.getString(MediaFormat.KEY_MIME);
                            try {
                                videoDecoder = MediaCodec.createDecoderByType(mime);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                    if (videoDecoder != null) {
                        videoDecoder.stop();
                    }
                    try {
                        videoDecoder.configure(videoFormat, outputSurface.getSurface(), null, 0);
                        videoDecoder.start();// 解码器启动
                        mVideoExtractor.selectTrack(videoTrackIndex);
                        long firstVideoTime = mVideoExtractor.getSampleTime();
                        mVideoExtractor.seekTo(firstVideoTime, SEEK_TO_PREVIOUS_SYNC);

                        startVideoCodec(videoDecoder, videoEncoder, mVideoExtractor, inputSurface, outputSurface,
                                firstVideoTime, isEnd, item);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // 如果图片的视频则做如下操作
                if (item instanceof PhotoMediaItem) {
                    startPhotoCodec(videoEncoder, (PhotoMediaItem) item, isEnd);
                }
                // 这里设置muxuer的视轨的距离值,即当前视频的最后一帧pts，当做下一个视频的第一帧pts
                ptsOffset += mediaPts;
                // 30帧,那么每帧是33.33ms，25帧则40ms
                ptsOffset += PHTOTO_FPS_TIME * 1000;
            }
            if (isCancel) {
                release();
                return;
            }
            release();
            after = System.currentTimeMillis();
            LogUtils.i(TAG, "murVideo totalTime=" + (after - before));
            int sleepTime = 500;
            // if(Build.VERSION.SDK_INT <Build.VERSION_CODES.M){
            // sleepTime=2000;
            // }
            // if (after - before > 30000) {
            // sleepTime = 3000;
            // }
            // if(after - before > 120000){
            // sleepTime = 5000;
            // }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            videoMurgeFlag = true;
            videoMarryAudio();
        }
    };

    private void releaseDecoderResources() {
        // release everything we grabbed
        if (videoDecoder != null) {
            try {
                videoDecoder.stop();
                videoDecoder.release();
                videoDecoder = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (mVideoExtractor != null) {
            try {
                mVideoExtractor.release();
                mVideoExtractor = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 计算当前进度
     *
     * @param currentUs
     */
    private void calcuProgress(long currentUs) {
        if (currentUs / 1000 - currentPositoin > 200) {// 300ms间距进行更新，不然太频繁
            currentPositoin = currentUs / 1000;
            if (listener != null) {
                int percent = (int) currentPositoin * 98 / mediaConfig.getDuration();
                if (percent < 1 || percent > 100) {
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
        mediaFormat = MediaFormat.createVideoFormat("video/avc", mediaConfig.getExportWidth()[0],
                mediaConfig.getExportWidth()[1]);
        // byte[] header_sps = {0, 0, 0, 1, 103, 100, 0, 31, -84, -76, 2, -128, 45, -56};
        // byte[] header_pps = {0, 0, 0, 1, 104, -18, 60, 97, 15, -1, -16, -121, -1, -8, 67, -1, -4, 33, -1, -2, 16, -1,
        // -1, 8, 127, -1, -64};
        // mediaFormat.setByteBuffer("csd-0", ByteBuffer.wrap(header_sps));
        // mediaFormat.setByteBuffer("csd-1", ByteBuffer.wrap(header_pps));
        // 设置视频的编码参数
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, ConstantMediaSize.BIT_RATE);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, ConstantMediaSize.FPS);
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);// 关键帧
        videoEncoder.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        // 解码过程，配合decoder进行滤镜添加
        // 建立egl，初始化GL环境，把屏幕的资源送到ecoder
        inputSurface = new InputSurface(videoEncoder.createInputSurface());
        inputSurface.makeCurrent();
        videoEncoder.start();
        outputSurface = new OutputSurface(doodleList, srcList, mediaConfig);
    }

    /**
     * 将两个关键帧之间截取的部分重新编码
     *
     * @param decoder
     * @param encoder
     * @param extractor
     * @param inputSurface
     * @param outputSurface
     * @param firstSampleTime 视频第一帧的时间戳
     */
    private void startVideoCodec(MediaCodec decoder, MediaCodec encoder, MediaExtractor extractor,
                                 InputSurface inputSurface, OutputSurface outputSurface, long firstSampleTime, boolean end, MediaItem item) {
        outputSurface.mediaPrepare(item);
        ByteBuffer[] decoderInputBuffers = decoder.getInputBuffers();
        ByteBuffer[] encoderOutputBuffers = encoder.getOutputBuffers();
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        MediaCodec.BufferInfo outputInfo = new MediaCodec.BufferInfo();
        boolean done = false;// 用于判断整个编解码过程是否结束
        boolean inputDone = false;
        boolean decodeDone = false;
        while (!done && !isCancel) {
            if (!inputDone) {
                int inputIndex = decoder.dequeueInputBuffer(TIMEOUT_USEC);
                if (inputIndex >= 0) {
                    ByteBuffer inputBuffer = decoderInputBuffers[inputIndex];
                    inputBuffer.clear();
                    int readSampleData = extractor.readSampleData(inputBuffer, 0);
                    videoCur = extractor.getSampleTime() - firstSampleTime;// 当前已经截取的视频长度,当前position
                    if (readSampleData > 0) {
                        decoder.queueInputBuffer(inputIndex, 0, readSampleData, extractor.getSampleTime(), 0);
                        extractor.advance();
                    } else {
                        decoder.queueInputBuffer(inputIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                        inputDone = true;
                    }
                }
            }
            if (!decodeDone) {
                int index = decoder.dequeueOutputBuffer(info, TIMEOUT_USEC);
                if (index == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    // no output available yet
                } else if (index == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    // decoderOutputBuffers = decoder.getOutputBuffers();
                } else if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    // expected before first buffer of data
                    MediaFormat newFormat = decoder.getOutputFormat();
                } else if (index < 0) {
                } else {
                    boolean doRender = (info.size != 0 && info.presentationTimeUs - firstSampleTime > 0);
                    decoder.releaseOutputBuffer(index, doRender);
                    if (doRender) {
                        // This waits for the image and renders it after it arrives.
                        outputSurface.awaitNewImage();
                        LogUtils.i(TAG,
                                "video-videoCur--------->mediaPosition:"
                                        + ((int) (ptsOffset + info.presentationTimeUs) / 1000) + ",videoPosition:"
                                        + (int) info.presentationTimeUs / 1000);
                        outputSurface.drawImage(true, (int) ((ptsOffset + info.presentationTimeUs) / 1000),
                                (int) videoCur / 1000);
                        mediaPts = info.presentationTimeUs;
                        // Send it to the encoder.
                        LogUtils.i(TAG,
                                "video-setPresentationTime------" + (ptsOffset + info.presentationTimeUs) * 1000);
                        inputSurface.setPresentationTime((ptsOffset + info.presentationTimeUs) * 1000);
                        calcuProgress(ptsOffset + info.presentationTimeUs);
                        inputSurface.swapBuffers();
                    }
                    if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                        decodeDone = true;
                        if (end) {
                            LogUtils.i(TAG, "video--signalEndOfInputStream............");
                            encoder.signalEndOfInputStream();
                        }
                    }
                }
            }
            boolean encoderOutputAvailable = true;
            while (encoderOutputAvailable) {
                int encoderStatus = encoder.dequeueOutputBuffer(outputInfo, TIMEOUT_USEC);
                if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    // no output available yet
                    encoderOutputAvailable = false;
                } else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    encoderOutputBuffers = encoder.getOutputBuffers();
                } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    MediaFormat newFormat = encoder.getOutputFormat();
                    startMux(newFormat);
                } else if (encoderStatus < 0) {
                } else {
                    ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
                    done = (outputInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0;
                    if (done || decodeDone) {
                        encoderOutputAvailable = false;
                        done = true;
                    }
                    // Write the data to the output "file".
                    if (outputInfo.presentationTimeUs == 0 && !done) {
                        continue;
                    }
                    if (outputInfo.size != 0) {
                        encodedData.position(outputInfo.offset);
                        encodedData.limit(outputInfo.offset + outputInfo.size);
                        if (!muxStarted) {
                            synchronized (lock) {
                                if (!muxStarted) {
                                    try {
                                        lock.wait();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        Log.i(TAG, "writeSampleData:muxVideoTrack" + muxVideoTrack + ",outputInfo:" + outputInfo.size
                                + "," + outputInfo.offset);
                        mMediaMuxer.writeSampleData(muxVideoTrack, encodedData, outputInfo);
                    }
                    encoder.releaseOutputBuffer(encoderStatus, false);
                }
                if (encoderStatus != MediaCodec.INFO_TRY_AGAIN_LATER) {
                    // Continue attempts to drain output.
                    continue;
                }
            }
        }
    }

    /**
     * 图片编码,解码不需要，因为是直接通过离屏，即屏幕把数据swapper传给encoder的surface，然后encoder就会有数据过来了
     * <p>
     * 用毫秒进行运算把，转换成秒有时候损失太大
     */
    // 不够整除的余数保留下来，分给下次的图片进行显示（极少成多)
    // 主要是出现这种情况，比如选了10个以上的0.1s的图片，2.5显示的次数，这样偏差越来越大
    private int remain;

    private void startPhotoCodec(MediaCodec encoder, PhotoMediaItem item, boolean isEnd) {
        outputSurface.mediaPrepare(item);
        int fpsCount = ((int) item.getDuration() + remain) / PHTOTO_FPS_TIME;
        remain = ((int) item.getDuration() + remain) % PHTOTO_FPS_TIME;
        // BigDecimal.valueOf(item.getDuration()).divide(BigDecimal.valueOf(PHTOTO_FPS_TIME)).setScale(0,
        // BigDecimal.ROUND_HALF_UP)
        // .intValue();
        for (int frameIndex = 0; frameIndex < fpsCount; frameIndex++) {
            if (isCancel) {
                return;
            }
            long presentationTimeNsec = computePresentationTimeNsec(frameIndex);
            drainEncoder(encoder, false);
            mediaPts = presentationTimeNsec / 1000;
            LogUtils.i(TAG, "photo-videoCur--------->mediaPosition:" + ((int) (ptsOffset + mediaPts) / 1000)
                    + ",videoPosition:" + (int) mediaPts / 1000);
            LogUtils.i(TAG, "photo-setPresentationTime------" + (ptsOffset * 1000 + presentationTimeNsec));
            outputSurface.drawImage(false, (int) ((ptsOffset + mediaPts) / 1000), (int) (mediaPts / 1000));
            // Send it to the encoder.
            // 需要计算其时间戳，如果是图片的话就根据帧来计算时间就好了
            inputSurface.setPresentationTime(ptsOffset * 1000 + presentationTimeNsec);
            calcuProgress(ptsOffset + presentationTimeNsec / 1000);
            inputSurface.swapBuffers();
        }
        drainEncoder(encoder, isEnd);
    }

    /**
     * 计算时间戳 fps=30帧
     *
     * @param frameIndex
     * @return
     */
    private long computePresentationTimeNsec(int frameIndex) {
        return frameIndex * ONE_BILLION / ConstantMediaSize.PHTOTO_FPS;
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
                                    lock.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    // adjust the ByteBuffer values to match BufferInfo (not needed?)
                    encodedData.position(mBufferInfo.offset);
                    encodedData.limit(mBufferInfo.offset + mBufferInfo.size);

                    mMediaMuxer.writeSampleData(muxVideoTrack, encodedData, mBufferInfo);
                    Log.d(TAG, "writeSampleData:muxVideoTrack" + muxVideoTrack + ",sent " + mBufferInfo.size
                            + " bytes to muxer, ts=" + mBufferInfo.presentationTimeUs);
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
        muxVideoTrack = mMediaMuxer.addTrack(mediaFormat);
        synchronized (lock) {
            if (muxVideoTrack != -1 && !muxStarted) {
                mMediaMuxer.start();
                muxStarted = true;
                lock.notify();
            }
        }
    }

    private synchronized void release() {
        try {
            if (mVideoExtractor != null) {
                mVideoExtractor.release();
            }
            mMediaMuxer.stop();
            mMediaMuxer.release();
            if (outputSurface != null) {
                outputSurface.release();
            }
            if (inputSurface != null) {
                inputSurface.release();
            }
            if (videoDecoder != null) {
                videoDecoder.stop();
                videoDecoder.release();
            }
            videoEncoder.stop();
            videoEncoder.release();
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
    private SingleCmdListener audioCmdListener;
    private Runnable murRunable = new Runnable() {
        @Override
        public void run() {
            while (!FfmpegBackgroundHelper.getInstance().isFinish() && !isCancel) {
                LogUtils.i(TAG, "wait...");
            }
            if (!isCancel) {
                boolean flag = changeAllVolume(audioList, srcList, new SingleCmdListener() {
                    @Override
                    public void next() {
                        if (!isCancel) {
                            insertComposeAudio(audioList, srcList, audioCmdListener);
                        }
                    }
                });
                if (!flag) {
                    insertComposeAudio(audioList, srcList, audioCmdListener);
                }

            }
        }
    };

    public void murgeAudio() {
        srcList.toArray();
        createSilenceAudio(null);
        audioCmdListener = new SingleCmdListener() {
            @Override
            public void next() {
                Log.i(TAG, "合成结束");
                audioMurgeFlag = true;
                videoMarryAudio();
            }

            @Override
            public void error(String error) {
                super.error(error);
                Log.i(TAG, "合成失败");
            }
        };
        ThreadPoolManager.getThreadPool().execute(murRunable);
    }

    public void cancelMurge() {
        audioCmdListener = null;
        isCancel = true;
        currentPositoin = 0;
        ThreadPoolManager.getThreadPool().cancel(murRunable);
    }

    /**
     * 音视频最终合并,可喜可贺
     * 有些手机有一些奇葩错误，当且是纯图片时，没有音频时，合成会出错
     */
    private void videoMarryAudio() {
        if (audioMurgeFlag && videoMurgeFlag) {
            boolean hasVideo = false;
            for (MediaItem m : srcList) {
                if (m.getMediaType() == MediaType.VIDEO) {
                    hasVideo = true;
                }
            }
            if (ObjectUtils.isEmpty(audioList) && !hasVideo) {
                FileUtils.renameFile(videoOutputPath, ouputPath);
                if (listener != null) {
                    listener.onFinish();
                }
                return;
            }
            VideoEditManager.composeVideoAudio(videoOutputPath, audioOutputPath, ouputPath, new SingleCmdListener() {
                @Override
                public void next() {
                }

                @Override
                public void onStop(int result) {
                    super.onStop(result);
                    if (result < 300 && PhoneAdatarList.videoAudioMurgeList()) {
                        VideoEditManager.composeVideoAudio(videoOutputPath, audioOutputPath, ouputPath,
                                new SingleCmdListener() {
                                    @Override
                                    public void next() {
                                        if (listener != null) {
                                            listener.onFinish();
                                        }
                                    }
                                });
                    } else {
                        if (listener != null) {
                            listener.onFinish();
                        }
                    }
                }

                @Override
                public void error(String error) {
                    super.error(error);
                    Log.i(TAG, "合成失败");
                    murgeFail();
                    if (listener != null) {
                        listener.error();
                    }
                }
            });
        }
    }

    /**
     * 清理数据，包括合成视频静音文件，音频空白文件，调音量渐变文件
     */
    private void murgeFail() {
        FileUtils.delete(silencePath);
        for (AudioMediaItem audioMediaItem : audioList) {
            FileUtils.delete(audioMediaItem.getVolumePath());
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
        FileUtils.delete(audioOutputPath);
        FileUtils.delete(videoOutputPath);
    }

    /**
     * 视频添加音频
     *
     * @param videoPath
     * @param audioPath
     * @param outputPath
     */
    private void muxerAudio(String videoPath, String audioPath, String outputPath) {
        MediaExtractor mediaExtractor = new MediaExtractor();
        int audioIndex = -1;
        try {
            mediaExtractor.setDataSource(audioPath);
            int trackCount = mediaExtractor.getTrackCount();
            for (int i = 0; i < trackCount; i++) {
                MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
                if (trackFormat.getString(MediaFormat.KEY_MIME).startsWith("audio/")) {
                    audioIndex = i;
                }
            }
            mediaExtractor.selectTrack(audioIndex);
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(audioIndex);
            MediaMuxer mediaMuxer = new MediaMuxer(videoPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            int writeAudioIndex = mediaMuxer.addTrack(trackFormat);
            mediaMuxer.start();
            ByteBuffer byteBuffer = ByteBuffer.allocate(500 * 1024);
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

            long stampTime = 0;
            // 获取帧之间的间隔时间
            mediaExtractor.readSampleData(byteBuffer, 0);
            if (mediaExtractor.getSampleFlags() == MediaExtractor.SAMPLE_FLAG_SYNC) {
                mediaExtractor.advance();
            }
            mediaExtractor.readSampleData(byteBuffer, 0);
            long secondTime = mediaExtractor.getSampleTime();
            mediaExtractor.advance();
            mediaExtractor.readSampleData(byteBuffer, 0);
            long thirdTime = mediaExtractor.getSampleTime();
            stampTime = Math.abs(thirdTime - secondTime);
            mediaExtractor.unselectTrack(audioIndex);
            mediaExtractor.selectTrack(audioIndex);
            while (true) {
                int readSampleSize = mediaExtractor.readSampleData(byteBuffer, 0);
                if (readSampleSize < 0) {
                    break;
                }
                mediaExtractor.advance();
                bufferInfo.size = readSampleSize;
                bufferInfo.flags = mediaExtractor.getSampleFlags();
                bufferInfo.offset = 0;
                bufferInfo.presentationTimeUs += stampTime;
                calcuProgress(bufferInfo.presentationTimeUs / 1000);
                mediaMuxer.writeSampleData(writeAudioIndex, byteBuffer, bufferInfo);
            }
            mediaMuxer.stop();
            mediaMuxer.release();
            mediaExtractor.release();
            FileUtils.renameFile(videoPath, outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 针对很段的剪切音频，然后主题视频很长，这个短音频是循环播放的
     * 如果数量巨大用ffmpeg去拼接，会发生错误，所以同文件的拼接数量大于10时
     * 则采用android方法去合成音频
     *
     * @param audioList
     */
    public void multiAudioMurge(List<AudioMediaItem> audioList) {

    }

    /**
     * 音频视频合成
     *
     * @param videoPath
     * @param audioPath
     * @param output
     */
    public void combineVideoAudio(String videoPath, String audioPath, String output) {
        try {
            MediaExtractor videoExtractor = new MediaExtractor();
            videoExtractor.setDataSource(videoPath);
            MediaFormat videoFormat = null;
            int videoTrackIndex = -1;
            int videoTrackCount = videoExtractor.getTrackCount();
            for (int i = 0; i < videoTrackCount; i++) {
                videoFormat = videoExtractor.getTrackFormat(i);
                String mimeType = videoFormat.getString(MediaFormat.KEY_MIME);
                if (mimeType.startsWith("video/")) {
                    videoTrackIndex = i;
                    break;
                }
            }

            MediaExtractor audioExtractor = new MediaExtractor();
            audioExtractor.setDataSource(audioPath);
            MediaFormat audioFormat = null;
            int audioTrackIndex = -1;
            int audioTrackCount = audioExtractor.getTrackCount();
            for (int i = 0; i < audioTrackCount; i++) {
                audioFormat = audioExtractor.getTrackFormat(i);
                String mimeType = audioFormat.getString(MediaFormat.KEY_MIME);
                if (mimeType.startsWith("audio/")) {
                    audioTrackIndex = i;
                    break;
                }
            }

            videoExtractor.selectTrack(videoTrackIndex);
            audioExtractor.selectTrack(audioTrackIndex);

            MediaCodec.BufferInfo videoBufferInfo = new MediaCodec.BufferInfo();
            MediaCodec.BufferInfo audioBufferInfo = new MediaCodec.BufferInfo();

            MediaMuxer mediaMuxer = new MediaMuxer(output, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            int writeVideoTrackIndex = mediaMuxer.addTrack(videoFormat);
            int writeAudioTrackIndex = mediaMuxer.addTrack(audioFormat);
            mediaMuxer.start();
            ByteBuffer byteBuffer = ByteBuffer.allocate(500 * 1024);
            long sampleTime = 0;
            {
                videoExtractor.readSampleData(byteBuffer, 0);
                if (videoExtractor.getSampleFlags() == MediaExtractor.SAMPLE_FLAG_SYNC) {
                    videoExtractor.advance();
                }
                videoExtractor.readSampleData(byteBuffer, 0);
                long secondTime = videoExtractor.getSampleTime();
                videoExtractor.advance();
                long thirdTime = videoExtractor.getSampleTime();
                sampleTime = Math.abs(thirdTime - secondTime);
            }
            videoExtractor.unselectTrack(videoTrackIndex);
            videoExtractor.selectTrack(videoTrackIndex);
            currentPositoin = 0;
            while (true) {
                int readVideoSampleSize = videoExtractor.readSampleData(byteBuffer, 0);
                if (readVideoSampleSize < 0) {
                    break;
                }
                videoBufferInfo.size = readVideoSampleSize;
                videoBufferInfo.presentationTimeUs += sampleTime;
                calcuProgress(videoBufferInfo.presentationTimeUs / 1000);
                videoBufferInfo.offset = 0;
                videoBufferInfo.flags = videoExtractor.getSampleFlags();
                mediaMuxer.writeSampleData(writeVideoTrackIndex, byteBuffer, videoBufferInfo);

                videoExtractor.advance();
            }
            currentPositoin = 0;
            while (true) {
                int readAudioSampleSize = audioExtractor.readSampleData(byteBuffer, 0);
                if (readAudioSampleSize < 0) {
                    break;
                }

                audioBufferInfo.size = readAudioSampleSize;
                audioBufferInfo.presentationTimeUs += sampleTime;
                audioBufferInfo.offset = 0;
                audioBufferInfo.flags = videoExtractor.getSampleFlags();
                calcuProgress(videoBufferInfo.presentationTimeUs / 1000);
                mediaMuxer.writeSampleData(writeAudioTrackIndex, byteBuffer, audioBufferInfo);
                audioExtractor.advance();
            }

            mediaMuxer.stop();
            mediaMuxer.release();
            videoExtractor.release();
            audioExtractor.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改原音和外音的音量大小，在调整过程中，外音是主动的，原音是被动的，当调整seekbar
     * 修改两方的音量时，以外音为主，就是说原音有可能被两部分外音占有，当这种情况下，根据占有的时间戳，
     * 修改原音对应的音量大小，默认都是是半音0.5
     */
    private boolean changeAllVolume(List<AudioMediaItem> audioMediaItems, List<MediaItem> mediaItems,
                                    final SingleCmdListener listener) {
        List<String[]> cmdList = new ArrayList<>();
        final List<String> pathList = new ArrayList<>();
        List<String[]> checkExtractList = new ArrayList<>();
        for (AudioMediaItem audioMediaItem : audioMediaItems) {
            audioMediaItem.setVolumePath(ConstantPath.createAudioVolumePath(audioMediaItem.getMurgePath()));
            cmdList.add(VideoEditManager.changeAudioVolome(audioMediaItem.getMurgePath(), audioMediaItem.getVolume(),
                    audioMediaItem.getVolumePath(), null, false));
        }

        for (MediaItem mediaItem : mediaItems) {
            if (mediaItem instanceof VideoMediaItem) {
                if (ObjectUtils.isEmpty(((VideoMediaItem) mediaItem).getExtracAudioPath())) {
                    continue;
                }
                ((VideoMediaItem) mediaItem).setVolumePath(
                        ConstantPath.createAudioVolumePath(((VideoMediaItem) mediaItem).getExtracAudioPath()));
                // 如果文件不存在，则重新抽取
                if (!FileUtils.checkFileExist(((VideoMediaItem) mediaItem).getExtracAudioPath())) {
                    String extracPath = ((VideoMediaItem) mediaItem).getExtracAudioPath();
                    String tempFile = extracPath.substring(0, extracPath.indexOf("_ijoysoft"));
                    pathList.add(tempFile);
                    checkExtractList.add(VideoEditManager.extractAudioMp3(mediaItem.getPath(), tempFile, null, false));
                }
                // 这里检查是否有视频裁剪动作,目前video音频抽取没有做后缀rename
                String sourcePath = ObjectUtils.isEmpty(((VideoMediaItem) mediaItem).getVideoTrimAudioPath())
                        ? ((VideoMediaItem) mediaItem).getExtracAudioPath()
                        : ((VideoMediaItem) mediaItem).getVideoTrimAudioPath();
                String[] strarray =
                        VideoEditManager.changeMultiVolume(sourcePath, ((VideoMediaItem) mediaItem).getVolumePath(),
                                null, ((VideoMediaItem) mediaItem).getVolumeList(), false);
                cmdList.add(strarray);
            }
        }
        if (cmdList.isEmpty()) {
            return false;
        }
        if (checkExtractList.size() > 0) {
            final List<String[]> tempCmdList = cmdList;
            FFmpegCmd.getInstance().executeList(checkExtractList, new SingleCmdListener() {
                @Override
                public void next() {
                    for (String path : pathList) {
                        FileUtils.changePrivateFileName(path);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            FFmpegCmd.getInstance().executeList(tempCmdList, listener);
                        }
                    }, 300);
                }
            });
        } else {
            FFmpegCmd.getInstance().executeList(cmdList, listener);
        }
        return true;
    }

    /**
     * 创建静音文件
     */
    private void createSilenceAudio(SingleCmdListener listener) {
        silencePath = ConstantPath.createSilenceAudioPath(mediaConfig.getProjectId());
        VideoEditManager.createSilenceAudio(silencePath, TimeUtil.getSecondOneDecimal(mediaConfig.getDuration()),
                listener, true);
    }

    /**
     * 最终合成，把所有的音频片段插入到静音文件中
     * 如果有淡入淡出标志，则进行淡入淡出处理
     */
    private void insertComposeAudio(List<AudioMediaItem> audioMediaItems, List<MediaItem> mediaItems,
                                    SingleCmdListener listener) {
        audioOutputPath = ConstantPath.createAudioOutputPath(mediaConfig.getProjectId());
        List<AudioInsertItem> list = new ArrayList<>();
        for (AudioMediaItem audioMediaItem : audioMediaItems) {
            if (audioMediaItem.getVolumePath() == null || audioMediaItem.getVolumePath().isEmpty()) {
                continue;
            }
            list.add(new AudioInsertItem(
                    mediaConfig.isFading()
                            && audioMediaItem.getDurationInterval().getInterval() > ConstantMediaSize.FADE_TIME,
                    audioMediaItem.getDurationInterval().getStartDuration(),
                    audioMediaItem.getDurationInterval().getEndDuration(), audioMediaItem.getVolumePath()));
        }
        int durationTemp = 0;
        for (MediaItem mediaItem : mediaItems) {
            /*
             * java.lang.ClassCastException: com.ijoysoft.mediasdk.module.entity.PhotoMediaItem cannot be cast to
             * com.ijoysoft.mediasdk.module.entity.VideoMediaItem
             */
            if (mediaItem.getMediaType() == MediaType.PHOTO) {
                durationTemp += (int) mediaItem.getDuration();
                continue;
            }
            if (((VideoMediaItem) mediaItem).getVolumePath() == null
                    || ((VideoMediaItem) mediaItem).getVolumePath().isEmpty()) {
                durationTemp += (int) mediaItem.getTempDuration();
                continue;
            }
            if (mediaItem instanceof VideoMediaItem) {
                list.add(new AudioInsertItem(false, durationTemp, (int) (durationTemp + mediaItem.getTempDuration()),
                        ((VideoMediaItem) mediaItem).getVolumePath()));
            }
            durationTemp += (int) mediaItem.getTempDuration();
        }
        if (list.isEmpty()) {
            if (listener != null) {
                listener.next();
            }
            audioOutputPath = silencePath;
            return;
        }
        VideoEditManager.inserMultiAudio(audioOutputPath, silencePath, list, listener);
    }

    /**
     * 针对多文件不同视频，不同分辨率的文件，因为ffmpeg转化也慢，另外分辨率不同的话还有拉伸问题
     * 所以统一在这里进行生成
     *
     * @param commands
     */
    public void cutMultiVideo(List<String[]> commands, List<MediaItem> dataSource, final String resultPath) {
        srcList = dataSource;
        // 做好ffmpeg的准备工作
        listener.progress(1);
        videoOutputPath = ConstantPath.createVideoOutputPath("trim");
        if (commands.size() > 0) {
            FFmpegCmd.getInstance().executeList(commands, new SingleCmdListener() {
                @Override
                public void next() {
                    ptsOffset = 0;
                    ouputPath = resultPath;
                    try {
                        mMediaMuxer = new MediaMuxer(videoOutputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                        executorService.execute(videoCliper);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cutMultiVideoOfAudio();
                        }
                    }, 300);

                }
            });
        } else {
            ptsOffset = 0;
            ouputPath = resultPath;
            try {
                mMediaMuxer = new MediaMuxer(videoOutputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                executorService.execute(videoCliper);
            } catch (IOException e) {
                e.printStackTrace();
            }
            cutMultiVideoOfAudio();
        }

    }

    /**
     * 视频裁剪，音频处理部分
     */
    private void cutMultiVideoOfAudio() {
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
        audioCommands.add(VideoEditManager.composeMultiAudio(audioOutputPath, null, false,
                audiolist.toArray(new String[audiolist.size()])));
        FFmpegCmd.getInstance().executeList(audioCommands, new SingleCmdListener() {
            @Override
            public void next() {
                audioMurgeFlag = true;
                // if(Build.VERSION.SDK_INT <Build.VERSION_CODES.M){
                // new Handler().postDelayed(new Runnable() {
                // @Override
                // public void run() {
                // videoMarryAudio();
                // }
                // },1500);
                // }else{
                // videoMarryAudio();
                // }
                videoMarryAudio();
            }
        });
    }

    /**
     * 取消裁剪
     */
    public void cancelTrim() {
        isCancel = true;
        currentPositoin = 0;
    }

    private void audioConcatMurge(final String outPath, final String... path) {
        ThreadPoolMaxThread.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                AudioCodec.muxerAudio(outPath, srcList, path);
                audioMurgeFlag = true;
                videoMarryAudio();
            }
        });
    }

    private void videoCutProgress(long currentUs) {
        if (currentUs / 1000 - currentPositoin > 200) {// 300ms间距进行更新，不然太频繁
            currentPositoin = currentUs / 1000;
            if (listener != null) {
                listener.progress((int) (currentPositoin * 98 / mediaConfig.getDuration()));
            }
        }
    }

    public void setMediaConfig(MediaConfig mediaConfig) {
        this.mediaConfig = mediaConfig;
    }

    public interface OnVideoCutFinishListener {
        void onFinish();

        void progress(int progress);

        void error();
    }
}
