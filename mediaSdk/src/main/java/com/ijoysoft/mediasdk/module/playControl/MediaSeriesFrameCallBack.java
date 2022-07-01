package com.ijoysoft.mediasdk.module.playControl;

import android.graphics.Bitmap;

public interface MediaSeriesFrameCallBack {
    /**
     * @param second 秒
     * @param bitmap 帧图
     */
    void callback(int second, Bitmap bitmap);
}
