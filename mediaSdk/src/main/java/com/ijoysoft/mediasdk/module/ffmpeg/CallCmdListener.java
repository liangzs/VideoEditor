package com.ijoysoft.mediasdk.module.ffmpeg;

public interface CallCmdListener {
    default void onStart() {
    }

    void onStop(int result);

    default void onNext() {
    }

    default void onInnerFinish() {
    }

    default void onProgress(int progress) {
    }

    default void error(String error) {
    }
}
