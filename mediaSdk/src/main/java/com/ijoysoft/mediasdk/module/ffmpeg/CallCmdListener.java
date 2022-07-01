package com.ijoysoft.mediasdk.module.ffmpeg;

public interface CallCmdListener {
    void onStart();

    void onStop(int result);

    void onNext();

    void onProgress(int progress);

    void error(String error);
}
