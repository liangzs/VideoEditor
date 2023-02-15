package com.ijoysoft.mediasdk.module.playControl;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;

import com.ijoysoft.mediasdk.module.entity.BGInfo;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;
import com.ijoysoft.mediasdk.view.BackgroundType;
import com.ijoysoft.mediasdk.view.MediaPreviewView;

import java.util.List;

/**
 * mediapreview开放接口，去除视频和照片if else 一堆操作
 */
public interface IMediaRender extends IRender {

    default void init(MediaRenderBus bus, IMediaCallback callback) {
    }

    void start(boolean autoPlay);

    void pause();

    void resume();

    void onDestroy();


    default void setDataSource(List<MediaItem> dataSource, boolean originPathPlay, boolean noSpeedPlay) {
    }

    default void setDataSource(List<MediaItem> dataSource,MediaConfig mediaConfig ) {
    }


    //和previewClipMediaItem看后面能不能合并
    void previewMediaItem(int index);

    //这个和previewMediaItem很大相似处，考虑合并
    void previewAfilter();

    default void switchPlayer(int curIndex) {
    }

    default void setSeekTo(int i, int ti, boolean forceRender) {
    }


    default void setZoomScale(BGInfo bgInfo) {
    }

    default void finishReset() {

    }

    void seekToEnd();

    default void setRatio() {
    }

    void setPureColor(BGInfo bgInfo);

    default void doMediaRotate() {
    }

    default void doMediaMirror() {
    }

    default int getOutputTexture() {
        return 0;
    }

    int getCurPosition();

    default void setCurrentMediaItem(MediaItem mediaItem) {
    }

    default void setCurrenPostion(int currenPostion) {
    }

    void setFilterStrength(float progress, List<String> renderSingleList);

    //image的转场处理逻辑放到，baseThemeExample和BlurBaseThemeExample中，视频的转场前帧提取当前缓存帧作输入
    default void previewTransition(boolean flag) {
    }

    default void previewTransitionPreViedeo(boolean flag) {
    }

    default void removeTransition() {
    }

    void setScaleTranlation(float scale, int x, int y, int originX, int originY);

    default boolean checkSupportTransition() {
        return false;
    }

    default void setClipPreview(boolean flag) {
    }

    default void setReverse(boolean flag) {
    }

    //photo-----------------------

    default void setWidgetDataSource(List<Bitmap> list) {
    }

    //保留颜色和背景模糊的设置
    default void changeTheme(MediaConfig mediaConfig) {
    }

    /************************video************************/
    default void onVideoPause() {
    }

    default void videoReset() {
    }

    default void switchNotVideo() {
    }

    default void setVolume(float volume) {
    }

    default void updateTransitionAndAfilterVideo() {
    }

    default void setvideoSpeed(float speed) {
    }

    /**
     * 用于单例的清理，比如视频的播放等
     */
    default void onMainDestroy() {

    }

    /**
     * 主题播放结束时做一些重置
     */
    default void onFinish() {

    }


    /*新增前后帧纹理*/
    int getPreTexture();

    int getNextTexture();
}
