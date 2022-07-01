package com.ijoysoft.mediasdk.module.entity;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VideoMediaItem extends MediaItem {
    private long size;
    // 码率
    private int bitRate;
    private String extracAudioPath;
    // 保存修改音量后的路径
    private String volumePath;
    private List<AudioDuration> volumeList;
    private Map<Integer, Bitmap> thumbnails;
    private String videoTrimAudioPath;

    public VideoMediaItem() {
        mediaType = MediaType.VIDEO;
        volumeList = new ArrayList<>();

    }

    public void addAudioDuration(AudioDuration audioDuration) {
        volumeList.add(audioDuration);
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public ArrayList<AudioDuration> getVolumeList() {
        return (ArrayList<AudioDuration>) volumeList;
    }

    public void setVolumeList(List<AudioDuration> volumeList) {
        this.volumeList = volumeList;
    }

    public int getWidthNoRotation() {
        if (getRotation() == 90 || getRotation() == -90 || getRotation() == 270 || getRotation() == -270) {
            return getHeight();
        }
        return getWidth();
    }

    public int getHeightNoRotation() {
        if (getRotation() == 90 || getRotation() == -90 || getRotation() == 270 || getRotation() == -270) {
            return getWidth();
        }
        return getHeight();
    }

    public String getExtracAudioPath() {
        return extracAudioPath;
    }

    public void setExtracAudioPath(String extracAudioPath) {
        this.extracAudioPath = extracAudioPath;
    }

    public String getVolumePath() {
        return volumePath;
    }

    public void setVolumePath(String volumePath) {
        this.volumePath = volumePath;
    }

    public Map<Integer, Bitmap> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Map<Integer, Bitmap> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getVideoTrimAudioPath() {
        return videoTrimAudioPath;
    }

    public void setVideoTrimAudioPath(String videoTrimAudioPath) {
        this.videoTrimAudioPath = videoTrimAudioPath;
    }

   /* @Override
    public String toString() {
        return "VideoMediaItem{" + "size=" + size + ", bitRate=" + bitRate + ", extracAudioPath='" + extracAudioPath
                + '\'' + ", volumePath='" + volumePath + '\'' + ", volumeList=" + volumeList + ", thumbnails="
                + thumbnails + ", videoTrimAudioPath='" + videoTrimAudioPath + '\'' + '}';
    }*/
}
