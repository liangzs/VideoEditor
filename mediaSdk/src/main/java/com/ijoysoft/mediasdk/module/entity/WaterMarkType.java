package com.ijoysoft.mediasdk.module.entity;

public enum WaterMarkType {
    CHARACTER(0, "文字"), DOODLE(1, "涂鸦"), PICTURE(2, "贴图"), WATERMARK(3, "水印");

    private int id;
    private String detail;

    WaterMarkType(int id, String detail) {
        this.id = id;
        this.detail = detail;
    }
    public static WaterMarkType getType(int id) {
        switch (id) {
            case 0:
                return CHARACTER;
            case 1:
                return DOODLE;
            case 2:
                return PICTURE;
            case 3:
                return WATERMARK;
        }
        return CHARACTER;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
