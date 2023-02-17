package com.qiusuo.videoeditor.common.bean.type;


public enum MusicSortType {
    SORT_NAME(0),
    SORT_DATE(1),
    SORT_DURATION(2),
    SORT_REVERSE(3);

    private int type;

    MusicSortType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static MusicSortType getMusicSortType(int type) {
        for (MusicSortType musicSortType : MusicSortType.values()) {
            if (type == musicSortType.type) {
                return musicSortType;
            }
        }
        return SORT_NAME;
    }


}
