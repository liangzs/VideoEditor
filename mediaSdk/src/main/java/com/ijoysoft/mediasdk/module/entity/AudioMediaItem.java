package com.ijoysoft.mediasdk.module.entity;

import java.io.Serializable;

/**
 * 音频播放元素，设置时间段，按照时间段进行播放
 */
public class AudioMediaItem implements Serializable ,Cloneable{
    private String projectId;
    private String path;
    private long duration;
    private long size;
    private String title;
    private DurationInterval durationInterval;
    //用于合成临时片段 seq_start
    private String murgePath;
    //对于物理时长小于逻辑时长，并且有差值 seq_start
    private String murgeOffsetPath;
    /**
     * 修改音量后的临时文件路径 seq_end
     */
    private String volumePath;

    /**
     * 0-1
     */
    private float volume;

    public AudioMediaItem(String path) {
        this.path = path;
        volume = 0.5f;
    }

    public AudioMediaItem() {
        volume = 0.5f;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public DurationInterval getDurationInterval() {
        return durationInterval;
    }

    public void setDurationInterval(DurationInterval durationInterval) {
        this.durationInterval = durationInterval;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMurgePath() {
        return murgePath;
    }

    public void setMurgePath(String murgePath) {
        this.murgePath = murgePath;
    }

    public String getMurgeOffsetPath() {
        return murgeOffsetPath;
    }

    public void setMurgeOffsetPath(String murgeOffsetPath) {
        this.murgeOffsetPath = murgeOffsetPath;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public String getVolumePath() {
        return volumePath;
    }

    public void setVolumePath(String volumePath) {
        this.volumePath = volumePath;
    }
    
    @Override
    public Object clone()  {
        AudioMediaItem audioMediaItem = null;
        try {
            audioMediaItem = (AudioMediaItem) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return audioMediaItem;
    }
    
    @Override
    public String toString() {
        return "AudioMediaItem{" +
                "projectId='" + projectId + '\'' +
                ", path='" + path + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", durationInterval=" + durationInterval +
                ", title='" + title + '\'' +
                ", murgePath='" + murgePath + '\'' +
                ", murgeOffsetPath='" + murgeOffsetPath + '\'' +
                ", volumePath='" + volumePath + '\'' +
                ", volume=" + volume +
                '}';
    }
}
