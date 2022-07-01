package com.ijoysoft.mediasdk.module.entity;

import java.io.Serializable;

public class AudioDuration implements Serializable, Cloneable {
    private int start;
    private int end;
    private float volume;

    public AudioDuration(int start, int end, float volume) {
        this.start = start;
        this.end = end;
        this.volume = volume;
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

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }


    public int getInterval() {
        return end - start;
    }
}
