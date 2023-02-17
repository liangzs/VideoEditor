package com.qiusuo.videoeditor.common.bean;

import com.ijoysoft.mediasdk.module.entity.DurationInterval;

import java.io.Serializable;

/**
 * Created by DELL on 2019/6/28.
 */
public class CutMusicItem implements Serializable {
    private String title;
    private long duration;
    private String path;
    private DurationInterval mDurationInterval;
    private float volume;
    private String originPath;
    private long originDuration;
    private long size;
    private long cutStart;
    private long cutEnd;

    public String getPath() {
        return path;
    }

    public CutMusicItem() {
    }

    public String getTitle() {
        return title;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public DurationInterval getDurationInterval() {
        return mDurationInterval;
    }

    public void setDurationInterval(DurationInterval durationInterval) {
        mDurationInterval = durationInterval;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }


    public String getOriginPath() {
        return originPath;
    }

    public void setOriginPath(String originPath) {
        this.originPath = originPath;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCutStart() {
        return cutStart;
    }

    public void setCutStart(long cutStart) {
        this.cutStart = cutStart;
    }

    public long getCutEnd() {
        return cutEnd;
    }

    public void setCutEnd(long cutEnd) {
        this.cutEnd = cutEnd;
    }

    public DurationInterval getmDurationInterval() {
        return mDurationInterval;
    }

    public void setmDurationInterval(DurationInterval mDurationInterval) {
        this.mDurationInterval = mDurationInterval;
    }

    public long getOriginDuration() {
        return originDuration;
    }

    public void setOriginDuration(long originDuration) {
        this.originDuration = originDuration;
    }

    @Override
    public String toString() {
        return "CutMusicItem{" +
                "title='" + title + '\'' +
                ", duration=" + duration +
                ", path='" + path + '\'' +
                ", mDurationInterval=" + mDurationInterval +
                ", volume=" + volume +
                '}';
    }
}
