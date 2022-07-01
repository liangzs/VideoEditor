package com.ijoysoft.mediasdk.module.playControl;

public interface IRender {
    void onSurfaceCreated();

    void onSurfaceChanged(int offsetX, int offsetY, int width, int height);

    void onDrawFrame();
}
