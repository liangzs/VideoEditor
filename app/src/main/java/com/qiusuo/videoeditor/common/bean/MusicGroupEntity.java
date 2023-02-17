package com.qiusuo.videoeditor.common.bean;


import java.util.ArrayList;
import java.util.List;

public class MusicGroupEntity {
    private List<MusicEntity> general;
    private List<MusicEntity> happy;
    private List<MusicEntity> love;
    private List<MusicEntity> soft;
    private List<MusicEntity> children;
    private List<MusicEntity> birthday;
    private List<MusicEntity> dynamic;
    private List<MusicEntity> epic;

    public List<MusicEntity> getGeneral() {
        return general;
    }

    public void setGeneral(List<MusicEntity> general) {
        this.general = general;
    }

    public List<MusicEntity> getHappy() {
        return happy;
    }

    public void setHappy(List<MusicEntity> happy) {
        this.happy = happy;
    }

    public List<MusicEntity> getLove() {
        return love;
    }

    public void setLove(List<MusicEntity> love) {
        this.love = love;
    }

    public List<MusicEntity> getSoft() {
        return soft;
    }

    public void setSoft(List<MusicEntity> soft) {
        this.soft = soft;
    }

    public List<MusicEntity> getChildren() {
        return children;
    }

    public void setChildren(List<MusicEntity> children) {
        this.children = children;
    }

    public List<MusicEntity> getBirthday() {
        return birthday;
    }

    public void setBirthday(List<MusicEntity> birthday) {
        this.birthday = birthday;
    }


    public List<MusicEntity> getAllList() {
        List<MusicEntity> list = new ArrayList<>();
        if (general != null) {
            list.addAll(general);
        }
        if (happy != null) {
            list.addAll(happy);
        }
        if (love != null) {
            list.addAll(love);
        }
        if (soft != null) {
            list.addAll(soft);
        }
        if (children != null) {
            list.addAll(children);
        }
        if (birthday != null) {
            list.addAll(birthday);
        }
        if (dynamic != null) {
            list.addAll(dynamic);
        }
        if (epic != null) {
            list.addAll(epic);
        }
        return list;
    }

    public void setDynamic(List<MusicEntity> dynamic) {
        this.dynamic = dynamic;
    }

    public void setEpic(List<MusicEntity> epic) {
        this.epic = epic;
    }
}


