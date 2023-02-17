package com.qiusuo.videoeditor.common.bean;

import java.util.Objects;

/**
 * @ author       lfj
 * @ date         2020/9/5
 * @ description
 */
public class MusicEntity {
    /**
     * type : 0
     * path : /General/Bike Rides.mp3
     * name : Bike Rides.mp3
     * duration : 112000
     */


    private long id;//歌曲id
    private int type = -1;//分类
    private String path;//歌曲地址
    private String name;//歌曲名
    private int duration;//歌曲时间长度
    private long size;//歌曲所占空间大小
    private String localPath;
    private String copyRight;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getCopyRight() {
        return copyRight;
    }

    public void setCopyRight(String copyRight) {
        this.copyRight = copyRight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicEntity that = (MusicEntity) o;
        return Objects.equals(path, that.path) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, path, name, duration, size);
    }

}
