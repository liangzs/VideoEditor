package com.ijoysoft.mediasdk.module.entity;

/**
 * 清晰度，导出时，是以width为主，根据选择比例计算出其高是多少
 */

@Deprecated // TODO: 2022/1/19  导出页面改换
public enum DefinitionType {

    _1080p_(1080, 1920), _720p_(720, 1280),
    _640p_(640, 1140), _480p_(480, 850), _320p_(320, 570);

    private int width;
    private int height;

    DefinitionType(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
