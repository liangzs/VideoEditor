package com.qiusuo.videoeditor.module.select;

/**
 * 素材选择的排序
 */
public enum MediaShowType {
    LIST(0),
    GRID(1),
//
    ;

    private int type;

    MediaShowType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static MediaShowType getSortType(int type) {
        for (MediaShowType musicSortType : MediaShowType.values()) {
            if (type == musicSortType.type) {
                return musicSortType;
            }
        }
        return LIST;
    }

    public static MediaShowType valueOf(int type) {
        if (type == 0) {
            return LIST;
        } return GRID;
    }


}
