package com.qiusuo.videoeditor.common.bean;

public class ThemeDownEntity {
    private int index;
    private String name;
    private int size;
    private String path;
    private String musicName;
    private String musicPath;

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ThemeDownEntity{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", path='" + path + '\'' +
                ", musicName='" + musicName + '\'' +
                ", musicPath='" + musicPath + '\'' +
                '}';
    }
}
