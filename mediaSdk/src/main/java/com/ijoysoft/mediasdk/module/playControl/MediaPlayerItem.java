package com.ijoysoft.mediasdk.module.playControl;

import android.media.MediaPlayer;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;

/**
 * 视频播放和图片播放
 * 视频播放采用mediaplayer，图片采用opengl一帧帧绘画
 */
public class MediaPlayerItem {
    private MediaPlayer mediaPlayer;
    private MediaItem item;
    private GPUImageFilter filter;


    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public MediaItem getItem() {
        return item;
    }

    public void setItem(MediaItem item) {
        this.item = item;
    }

    public GPUImageFilter getFilter() {
        return filter;
    }

    public void setFilter(GPUImageFilter filter) {
        this.filter = filter;
    }

    public void start() {
        if (item.getMediaType() == MediaType.VIDEO) {
            mediaPlayer.start();
        } else {

        }
    }

    public void seekTo(int seek) {

    }

    public boolean isPlaying() {
        return false;
    }


    public boolean isVideoPlay() {
        return item.getMediaType() == MediaType.VIDEO;
    }
}
