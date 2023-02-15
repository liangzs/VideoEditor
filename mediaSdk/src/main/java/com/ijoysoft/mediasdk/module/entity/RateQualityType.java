package com.ijoysoft.mediasdk.module.entity;

/**
 * 导出时，是以width为主，根据选择比例计算出其高是多少
 */
public enum RateQualityType {
    _2160p_(0, "2160p", 68812800, 51609600, 34406400, 2160),
    _1440p_(1, "1440p", 30583466, 22937600, 15291733, 1440),
    _1080p_(2, "1080p", 17203200, 12902400, 8601600, 1080),
    _720p_(3, "720p", 7680000, 5734400, 3891200, 720),
    _640p_(4, "640p", 6041600, 4608000, 3072000, 640),
    _540p_(5, "540p", 4301100, 3280500,  2187000, 540),
    _480p_(6, "480p", 3481600, 2560000, 1843200, 480),
    _360p_(7, "360p", 1958400, 1440000, 1036800, 360),
    _240p_(8, "240p", 870400, 640000, 460800, 240)


    //枚举结束分号
    ;

    private int hightRate;
    private int mediumRate;
    private int lowRate;
    private String name;
    private int index;

    /**
     * 对应宽度
     */
    private int width;

    RateQualityType(int index, String name, int hight, int medium, int low, int width) {
        this.hightRate = hight;
        this.mediumRate = medium;
        this.lowRate = low;
        this.name = name;
        this.index = index;
        this.width = width;
    }


    public int getHightRate() {
        return hightRate;
    }

    public void setHightRate(int hightRate) {
        this.hightRate = hightRate;
    }

    public int getMediumRate() {
        return mediumRate;
    }

    public void setMediumRate(int mediumRate) {
        this.mediumRate = mediumRate;
    }

    public int getLowRate() {
        return lowRate;
    }

    public void setLowRate(int lowRate) {
        this.lowRate = lowRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public int getMatchRate(int index) {
        int rate = mediumRate;
        switch (index) {
            case 0:
                rate = hightRate;
                break;
            case 1:
                rate = mediumRate;
                break;
            case 2:
                rate = lowRate;
                break;
        }
        return rate;
    }

    public static RateQualityType getIndex(int index) {
        return RateQualityType.values()[index];
    }





    /**
     * 根据宽度或者窄边获取清晰度
     * @param realWidth
     * @return
     */
    public static RateQualityType getResolutionByWidth(int realWidth) {
        for (RateQualityType type : RateQualityType.values()) {
            if (realWidth > type.width) {
                return type.getCeilType();
            }
        }
        return _240p_;
    }

    /**
     * 取上一个清晰度
     * @return
     */
    public RateQualityType getCeilType() {
        if (index == 0) {
            return this;
        }
        return getIndex(index-1);
    }

    /**
     * 取下一个清晰度
     * @return
     */
    public RateQualityType getFloorType() {
        if (index == 8) {
            return this;
        }
        return getIndex(index+1);
    }


}
