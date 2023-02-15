package com.ijoysoft.mediasdk.module.playControl;

import com.ijoysoft.mediasdk.module.entity.MediaItem;

/**
 * 播放状态变化触发回调
 */
public interface IMediaCallback {
    //图片渲染触发
    void render();

    default void onPrepare() {
    }

    default void onStart() {
    }

    default void onPause() {
    }

    //视频走此回调
    void onComplet(int index);

    //图片走此回调
    void onComplet();

    default void onVideoChanged(MediaItem info) {
    }

    default void preTranComplete() {
    }
}
