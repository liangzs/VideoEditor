package com.ijoysoft.mediasdk.module.opengl;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.ijoysoft.mediasdk.common.utils.LogUtils;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.TIME_DURATION;


/**
 * 处理手动转场的过程
 * 如果定时器是1s 25帧的周期，如果每帧的消耗超过40ms，那么其实会发生掉帧的
 */
public class TransitionThreadHandle {
    private static final String TAG = "TransitionThreadHandle";

    private static class SingleTonHoler {
        private static TransitionThreadHandle INSTANCE = new TransitionThreadHandle();
    }

    public static TransitionThreadHandle getInstance() {
        return SingleTonHoler.INSTANCE;
    }

    private HandlerThread handlerThread;
    private Handler handler;
    private int duration;
    private int currentDuration;
    private boolean isPlaying;
    private TransitionThreadCallBack transitionThreadCallBack;
    private static final int RENDER_CODE = 100;


    private TransitionThreadHandle() {
        handlerThread = new

                HandlerThread("imagePlayer");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                }
            }
        };
    }

    /**
     * 播放多久
     *
     * @param duration
     */
    public void play(int duration) {
        this.duration = duration;
        currentDuration = 0;
        startRender();
    }

    /**
     * 控制渲染状态
     */
    public void playPause() {
        isPlaying = false;
    }


    /**
     * 开始渲染
     */
    public void startRender() {
        if (!isPlaying) {
            return;
        }
        currentDuration += 40;
        if (currentDuration > TIME_DURATION) {
            playComplete();
            return;
        }
        LogUtils.i(TAG, "duration---->" + currentDuration);
        transitionThreadCallBack.render();
        handler.sendEmptyMessageDelayed(RENDER_CODE, 40);
    }


    /**
     * 播放结束
     */
    public void playComplete() {
        isPlaying = false;
        currentDuration = 0;
        transitionThreadCallBack.complete();
    }

    public void setTransitionThreadCallBack(TransitionThreadCallBack transitionThreadCallBack) {
        this.transitionThreadCallBack = transitionThreadCallBack;
    }

    /**
     * application中手动调用
     */
    public void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            handlerThread.quitSafely();
        }
    }

    public interface TransitionThreadCallBack {
        void render();

        void complete();
    }

}
