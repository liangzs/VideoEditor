package com.ijoysoft.mediasdk.module.mediacodec;

import android.os.Build;
import android.os.HandlerThread;
import androidx.annotation.RequiresApi;
import android.view.Surface;

import java.io.File;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.ONE_BILLION;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class PhotoItemClipper extends HandlerThread {
    int width;
    //    private FullFrameRect mFullScreen;
    int height;
    int bitRate;
    File outputFile;
    ProgressListener listener;
    private EglCore mEglCore;
    private WindowSurface mWindowSurface;
    private VideoEncoderCore mVideoEncoder;
    private long[] timeSections;
    private boolean stop = false;

    private PhotoItemClipper() {
        super("MovieEngine-thread");
    }


    private void makeMovie() {
        //不断绘制。
        boolean isCompleted = false;
        try {
            //初始化GL环境
            mEglCore = new EglCore(null, EglCore.FLAG_RECORDABLE);

            mVideoEncoder = new VideoEncoderCore(width, height, bitRate, outputFile);
            Surface encoderInputSurface = mVideoEncoder.getInputSurface();
            mWindowSurface = new WindowSurface(mEglCore, encoderInputSurface, true);
            mWindowSurface.makeCurrent();
//
//            mFullScreen = new FullFrameRect(
//                    new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_2D));

            //绘制
//            计算时长
            long totalDuration = 0;
//            timeSections = new long[movieMakers.size()];
//            for (int i = 0; i < movieMakers.size(); i++) {
//                MovieMaker movieMaker = movieMakers.get(i);
//                timeSections[i] = totalDuration;
//                totalDuration += movieMaker.getDurationAsNano();
//            }
            if (listener != null) {
            }
            long tempTime = 0;
            int frameIndex = 0;
            while (tempTime <= totalDuration + ONE_BILLION / 30) {
                mVideoEncoder.drainEncoder(false);
                generateFrame(tempTime);
                long presentationTimeNsec = computePresentationTimeNsec(frameIndex);
                submitFrame(presentationTimeNsec);
                updateProgress(tempTime, totalDuration);
                frameIndex++;
                tempTime = presentationTimeNsec;
                if (stop) {
                    break;
                }
            }
            System.out.println("total frames =" + (frameIndex));
            //finish
            mVideoEncoder.drainEncoder(true);
            isCompleted = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //结束
            try {
                releaseEncoder();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isCompleted && listener != null) {
            }
        }

    }

    private void updateProgress(final long tempTime, final long totalDuration) {
        if (listener != null) {
        }
    }

    private void submitFrame(long presentationTimeNsec) {
        mWindowSurface.setPresentationTime(presentationTimeNsec);
        mWindowSurface.swapBuffers();
    }

    //fps 30
    private long computePresentationTimeNsec(int frameIndex) {
        final long ONE_BILLION = 1000000000;
        return frameIndex * ONE_BILLION / 30;
    }

    private void generateFrame(long tempTime) {
        int movieIndex = 0;
        boolean find = false;
        for (int i = 0; i < timeSections.length; i++) {
            if (i + 1 < timeSections.length && tempTime >= timeSections[i] && tempTime < timeSections[i + 1]) {
                find = true;
                movieIndex = i;
                break;
            }
        }
        if (!find) {
            movieIndex = timeSections.length - 1;
        }
        long curTime = tempTime - timeSections[movieIndex];
//        MovieMaker movieMaker = movieMakers.get(movieIndex);
//        movieMaker.generateFrame(curTime);
    }

    private void releaseEncoder() {
        mVideoEncoder.release();
        if (mWindowSurface != null) {
            mWindowSurface.release();
            mWindowSurface = null;
        }
//        if (mFullScreen != null) {
//            mFullScreen.release(false);
//            mFullScreen = null;
//        }

        if (mEglCore != null) {
            mEglCore.release();
            mEglCore = null;
        }
    }

    @Override
    public boolean quit() {
        stop = true;
        return super.quit();
    }

    public interface ProgressListener {

        void onStart();

        void onCompleted(String path);

        void onProgress(long current, long totalDuration);

    }

}
