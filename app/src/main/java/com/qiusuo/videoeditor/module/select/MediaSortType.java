package com.qiusuo.videoeditor.module.select;

/**
 * 素材选择的排序
 */
public enum MediaSortType {
    SORT_DATE(0),
    SORT_NAME(1),
    SORT_SIZE(2)
//
    ;

    private int type;

    MediaSortType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static MediaSortType getSortType(int type) {
        for (MediaSortType musicSortType : MediaSortType.values()) {
            if (type == musicSortType.type) {
                return musicSortType;
            }
        }
        return SORT_DATE;
    }


}
