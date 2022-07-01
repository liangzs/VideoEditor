package com.ijoysoft.mediasdk.module.ffmpeg;

public abstract class SingleCmdListener implements CallCmdListener {
    @Override
    public void onStart() {

    }

    @Override
    public void onStop(int result) {

    }

    @Override
    public void onNext() {
        next();
    }


    @Override
    public void onProgress(int position) {

    }

    @Override
    public void error(String error) {

    }

    public abstract void next();

}
