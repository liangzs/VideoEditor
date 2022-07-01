package com.ijoysoft.mediasdk.module.entity;

/**
 * 用于音频的插入
 */
public class AudioInsertItem {
    private boolean isFade;
    private int start;
    private int end;
    private String path;

    public AudioInsertItem(boolean isFade, int start, int end, String path) {
        this.isFade = isFade;
        this.start = start;
        this.end = end;
        this.path = path;
    }

    public AudioInsertItem() {
    }

    public boolean isFade() {
        return isFade;
    }

    public void setFade(boolean fade) {
        isFade = fade;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
