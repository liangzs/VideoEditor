package com.ijoysoft.mediasdk.module.entity;

import java.io.Serializable;

public class DurationInterval implements Serializable, Cloneable {
    private int startDuration;
    private int endDuration;
    private int interval;


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

    public int getInterval() {
        return (endDuration - startDuration);
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    @Override
    public Object clone()  {
        DurationInterval durationInterval =null;
        try {
            durationInterval= (DurationInterval) super.clone();
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
}
