package com.ijoysoft.mediasdk.module.entity;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class DurationInterval implements Serializable, Cloneable {
    private int startDuration;
    private int endDuration;
    private int interval;

    /**
     * 剪两头
     */
    private boolean modeCut = false;


    public DurationInterval() {

    }

    public boolean isInRange(int duration) {
        if (duration >= startDuration && duration <= endDuration) {
            return true;
        }
        return false;
    }

    public DurationInterval(int start, int end) {
        this.startDuration = start;
        this.endDuration = end;
        this.interval = end - start;
    }

    public int getStartDuration() {
        return startDuration;
    }

    public void setStartDuration(int startDuration) {
        this.startDuration = startDuration;
    }

    public int getEndDuration() {
        return endDuration;
    }

    public void setEndDuration(int endDuration) {
        this.endDuration = endDuration;
    }

    public void setDuration(int startDuration, int endDuration) {
        this.startDuration = startDuration;
        this.endDuration = endDuration;
    }

    public int getInterval() {
        if (isModeCut()) {
            return interval;
        } else {
            return (endDuration - startDuration);
        }
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof DurationInterval) {
            DurationInterval that = (DurationInterval) obj;
            return that.getEndDuration() == endDuration
                    && that.getStartDuration() == startDuration;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDuration, endDuration);
    }

    @Override
    public Object clone() {
        DurationInterval durationInterval = null;
        try {
            durationInterval = (DurationInterval) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return durationInterval;
    }

    @Override
    public String toString() {
        return "DurationInterval{" +
                "startDuration=" + startDuration +
                ", endDuration=" + endDuration +
                ", interval=" + interval +
                '}';
    }


    public boolean isModeCut() {
        return modeCut;
    }

    public void setModeCut(boolean modeCut) {
        this.modeCut = modeCut;
    }
}
