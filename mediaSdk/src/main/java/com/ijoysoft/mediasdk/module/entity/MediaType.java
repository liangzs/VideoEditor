package com.ijoysoft.mediasdk.module.entity;

public enum MediaType {
    VIDEO(1, "视频"), PHOTO(2, "照片"), AUDIO(3, "音频");

    private int id;
    private String detail;

    MediaType(int id, String detail) {
        this.id = id;
        this.detail = detail;
    }

    public static MediaType getMediaType(int id) {
        switch (id) {
            case 1:
                return VIDEO;
            case 2:
                return PHOTO;
            case 3:
                return AUDIO;
        }
        return VIDEO;
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