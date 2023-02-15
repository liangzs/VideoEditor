package com.ijoysoft.mediasdk.module.entity;

/**
 * {@link RateQualityType} 这个类进行取medium质量的码率
 * 然后，帧率以30帧为准，不同帧的值动态计算*该帧率和30的比值
 * _1080p_(0, "1080p", 17203200, 12902400, 8601600),
 * _720p_(1, "720p", 7680000, 5734400, 3891200),
 * _640p_(2, "640p", 6041600, 4608000, 3072000),
 * _480p_(3, "480p", 3481600, 2560000, 1843200),
 * _320p_(4, "320p", 1536000, 1126140, 819200);
 */
public enum ResolutionType {

    _480p_(0, 480, 850, "480p", 3481600),
    _720p_(1, 720, 1280, "720p", 5734400),
    _1080p_(2, 1080, 1920, "1080p", 8601600),
    _2k_4k_(3, 1080, 1920, "2k/4k", 17203200);

    private int index;
    private int width;
    private int height;
    private String name;
    private int rateBit;

    ResolutionType(int index, int width, int height, String name, int rateBit) {
        this.index = index;
        this.width = width;
        this.height = height;
        this.name = name;
        this.rateBit = rateBit;
    }


    public int getIndex() {
        return index;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static ResolutionType getResolution(int index) {
        for (ResolutionType resolutionType : ResolutionType.values()) {
            if (resolutionType.index == index) {
                return resolutionType;
            }
        }
        return _720p_;
    }

    public static ResolutionType getResolutionByName(String name) {
        for (ResolutionType resolutionType : ResolutionType.values()) {
            if (resolutionType.name().equals(name)) {
                return resolutionType;
            }
        }
        return _720p_;
    }

    public String getName() {
        return name;
    }

    public long getRateBit() {
        return rateBit;
    }

    /**
     * @param frameType 用次帧率跟基础帧率25进行对比
     * @return
     */
    public int getMatchRate(FrameType frameType) {
        if (frameType == null) {
            return rateBit;
        }
        return (int) (rateBit * frameType.getMatchFrameRatio());
    }

    /**
     * 根据宽度或者窄边获取清晰度
     * @param realWidth
     * @return
     */
    public static ResolutionType getResolutionByWidth(int realWidth) {
        if (realWidth > 1080) {
            return _2k_4k_;
        }
        if (realWidth > 720) {
            return _1080p_;
        }
        if (realWidth > 480) {
            return _720p_;
        }
        return _480p_;
    }



    /**
     * 最小的输出清晰度
     * 内存值最大
     * @return
     */
    public static RateQualityType getMinCompressQuality(ResolutionType resolutionType) {
        switch (resolutionType) {
            case _480p_: return RateQualityType._360p_;
            case _720p_: return RateQualityType._640p_;
            case _1080p_: return RateQualityType._720p_;
            case _2k_4k_: return RateQualityType._1080p_;
        }
        return RateQualityType._360p_;
    }


    /**
     * 最小的输出清晰度
     * 内存值最大
     * @return
     */
    public RateQualityType getMinCompressQuality() {
        return getMinCompressQuality(this);
    }

}
