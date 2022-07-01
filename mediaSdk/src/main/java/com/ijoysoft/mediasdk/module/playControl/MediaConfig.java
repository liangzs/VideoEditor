package com.ijoysoft.mediasdk.module.playControl;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.entity.RatioType;

import java.io.Serializable;

/**
 * 参数配置
 */
public class MediaConfig implements Serializable {
    private RatioType ratioType;
    private String rgba;
    private int duration;
    private String projectId;
    private boolean isFading;
    private int currentDuration;

    public RatioType getRatioType() {
        return ratioType;
    }

    public void setRatioType(RatioType ratioType) {
        this.ratioType = ratioType;
    }

    public String getRgba() {
        return rgba;
    }

    public void setRgba(String rgba) {
        this.rgba = rgba;
    }

    public boolean isFading() {
        return isFading;
    }

    public void setFading(boolean fading) {
        isFading = fading;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCurrentDuration() {
        return currentDuration;
    }

    public void setCurrentDuration(int currentDuration) {
        this.currentDuration = currentDuration;
    }

    private MediaConfig(Builder builder) {
        this.ratioType = builder.ratioType;
        this.rgba = builder.rgba;
        this.duration = builder.duration;
        this.projectId = builder.projectId;
        this.isFading = builder.isFading;
        this.currentDuration = builder.currentDuration;
    }

    /**
     * 获取导出的width
     * 默认1:1
     *
     * @return
     */
    public int[] getExportWidth() {
        if (ratioType != null) {
            switch (ratioType) {
                case _1_1:
                    return ConstantMediaSize.HD_1_1;
                case _3_4:
                    return ConstantMediaSize.HD_3_4;
                case _4_3:
                    return ConstantMediaSize.HD_4_3;
                case _16_9:
                    return ConstantMediaSize.HD_16_9;
                case _9_16:
                    return ConstantMediaSize.HD_9_16;
            }
        }
        return ConstantMediaSize.HD_1_1;
    }


    @Override
    public String toString() {
        return "MediaConfig{" +
                "ratioType=" + ratioType +
                ", rgba=" + rgba +
                '}';
    }

    /**
     * builder
     */
    public static class Builder {
        private RatioType ratioType;
        private String rgba;
        private int duration;
        private String projectId;
        private boolean isFading;
        private int currentDuration;

        public Builder() {

        }

        public Builder setRatioType(RatioType ratioType) {
            this.ratioType = ratioType;
            return this;
        }

        public Builder setRgba(String rgba) {
            this.rgba = rgba;
            return this;
        }


        public Builder setCurrentDuration(int currentDuration) {
            this.currentDuration = currentDuration;
            return this;
        }


        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder setProjectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder setFading(boolean fading) {
            isFading = fading;
            return this;
        }

        public MediaConfig build() {
            return new MediaConfig(this);
        }
    }


}
