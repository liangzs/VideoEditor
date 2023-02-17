package com.qiusuo.videoeditor.common.bean;

/**
 * 将滤镜分组
 * Created by luotangkang on 2020/12/7.
 */
public class FilterEntity {
    private int nameId;
    private String nameTop;
    private int drawableId;
    private int color;
    private int type;

    public FilterEntity(int nameId, String nameTop, int drawableId, int color, int type) {
        this.nameId = nameId;
        this.nameTop = nameTop;
        this.drawableId = drawableId;
        this.color = color;
        this.type = type;
    }

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }

    public String getNameTop() {
        return nameTop;
    }

    public void setNameTop(String nameTop) {
        this.nameTop = nameTop;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
