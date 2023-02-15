package com.ijoysoft.mediasdk.module.mediacodec;

/**
 * 如果是通用任务，则backgroundTask的String[] 存储的是cmd命令，除外则存储的是参数
 */
public enum FfmpegTaskType {
    TERMINAL_TASK(0, "终止任务"), EXTRACT_AUDIO(1, "视频音源抽取"),
    COMMON_TASK(2, "通用任务"), LIST_TASK(3, "列表任务"),
    CRASH(4, "异常崩溃"),PROGRESS_TASK(5, "进度任务"),
    TERMINAL_AND_KILL(6, "停止任务和进程");
    private int id;
    private String name;

    FfmpegTaskType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static FfmpegTaskType getType(int id) {
        for (FfmpegTaskType ffmpegTaskType : FfmpegTaskType.values()) {
            if (ffmpegTaskType.getId() == id) {
                return ffmpegTaskType;
            }
        }
        return null;
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
