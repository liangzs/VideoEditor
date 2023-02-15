package com.ijoysoft.mediasdk.module.entity;

import com.ijoysoft.mediasdk.module.playControl.MediaConfig;
import com.ijoysoft.mediasdk.view.BackgroundType;

/**
 * 预处理参数集合
 */
public class PretreatConfig {
    private RatioType ratioType = RatioType.NONE;
    private String path;
    private int rotation;
    private int index;
    private @BackgroundType.Type
    int backgroundType;
    private int blurLevel;
    private int colorValue;
    private String customPath;
    //当前对象是否为视频
    private boolean isVideo;

    public PretreatConfig(String path, int rotation, int index) {
        this.path = path;
        this.rotation = rotation;
        this.index = index;
    }

    private PretreatConfig(PretreatConfig.Builder builder) {
        if (builder.ratioType != null) {
            this.ratioType = builder.ratioType;
        }
        this.path = builder.path;
        this.rotation = builder.rotation;
        this.index = builder.index;
        this.backgroundType = builder.backgroundType;
        this.blurLevel = builder.blurLevel;
        this.colorValue = builder.colorValue;
        this.customPath = builder.customPath;
    }

    public RatioType getRatioType() {
        return ratioType;
    }

    public void setRatioType(RatioType ratioType) {
        this.ratioType = ratioType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getBackgroundType() {
        return backgroundType;
    }

    public void setBackgroundType(int backgroundType) {
        this.backgroundType = backgroundType;
    }

    public int getBlurLevel() {
        return blurLevel;
    }

    public void setBlurLevel(int blurLevel) {
        this.blurLevel = blurLevel;
    }

    public int getColorValue() {
        return colorValue;
    }

    public void setColorValue(int colorValue) {
        this.colorValue = colorValue;
    }

    public String getCustomPath() {
        return customPath;
    }

    public void setCustomPath(String customPath) {
        this.customPath = customPath;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    /**
     * builder
     */
    public static class Builder {
        private RatioType ratioType;
        private String path;
        private int rotation;
        private int index;
        private int blurLevel;
        private int colorValue;
        private String customPath;
        private @BackgroundType.Type
        int backgroundType;
        boolean isVideo;


        public Builder setBackgroundType(int backgroundType) {
            this.backgroundType = backgroundType;
            return this;
        }

        public Builder setRatioType(RatioType ratioType) {
            this.ratioType = ratioType;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setRotation(int rotation) {
            this.rotation = rotation;
            return this;
        }

        public Builder setIndex(int index) {
            this.index = index;
            return this;
        }

        public Builder setBlurLevel(int blurLevel) {
            this.blurLevel = blurLevel;
            return this;
        }

        public Builder setColorValue(int colorValue) {
            this.colorValue = colorValue;
            return this;
        }

        public Builder setCustomPath(String customPath) {
            this.customPath = customPath;
            return this;
        }

        public boolean isVideo() {
            return isVideo;
        }

        public Builder setVideo(boolean video) {
            isVideo = video;
            return this;
        }

        public PretreatConfig build() {
            return new PretreatConfig(this);
        }
    }


}
