package com.ijoysoft.mediasdk.module.entity;

/**
 * 合成需要的辅助类
 */
public class FFmpegListProgress {
    private String path;
    private int totalTime;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public FFmpegListProgress(String path, int totalTime) {
        this.path = path;
        this.totalTime = totalTime;
    }

    public FFmpegListProgress(int totalTime) {
        this.totalTime = totalTime;
    }
}
