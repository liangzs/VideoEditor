package com.ijoysoft.mediasdk.module.mediacodec;

import androidx.annotation.Nullable;

import com.ijoysoft.mediasdk.common.utils.ObjectUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

public class BackroundTask implements Serializable {
    private String[] commands;

    private String id;

    private FfmpegTaskType ffmpegTaskType;
    private int duration;

    public BackroundTask(String[] commands, FfmpegTaskType taskType) {
        this.id = UUID.randomUUID().toString();
        this.commands = commands;
        this.ffmpegTaskType = taskType;
    }

    public BackroundTask(String[] commands, int duration, FfmpegTaskType taskType) {
        this.id = UUID.randomUUID().toString();
        this.commands = commands;
        this.duration = duration;
        this.ffmpegTaskType = taskType;
    }

    public BackroundTask(String id) {
        if (id != null) {
            this.id = id;
            return;
        }
        id = UUID.randomUUID().toString();
    }

    public String[] getCommands() {
        return commands;
    }

    public void setCommands(String[] commands) {
        this.commands = commands;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FfmpegTaskType getFfmpegTaskType() {
        return ffmpegTaskType;
    }

    public void setFfmpegTaskType(FfmpegTaskType ffmpegTaskType) {
        this.ffmpegTaskType = ffmpegTaskType;
    }

    @Override
    public String toString() {
        return "BackroundTask{" +
                "commands=" + Arrays.toString(commands) +
                ", id='" + id + '\'' +
                ", ffmpegTaskType=" + ffmpegTaskType +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (ObjectUtils.isEmpty(id)) {
            return false;
        }
        if (ObjectUtils.isEmpty(obj) || ObjectUtils.isEmpty(((BackroundTask) obj).id)) {
            return false;
        }
        if (this.id.equals(((BackroundTask) obj).id)) {
            return true;
        }
        return false;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
