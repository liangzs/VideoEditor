package com.ijoysoft.mediasdk.module.mediacodec;

public enum FfmpegStatus {
    EXCUTE_IDLE(0, "准备状态"),EXECUTE_START(1, "执行状态"), EXCUTE_ERROR(2, "异常");
    private int id;
    private String name;

    FfmpegStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
