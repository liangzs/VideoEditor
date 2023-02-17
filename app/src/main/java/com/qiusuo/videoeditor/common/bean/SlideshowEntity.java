package com.qiusuo.videoeditor.common.bean;

import android.os.Build;

import com.ijoysoft.mediasdk.module.opengl.theme.ThemeEnum;
import com.qiusuo.videoeditor.BuildConfig;

import java.io.Serializable;
import java.util.Objects;

public class SlideshowEntity implements Serializable {
    private int index;
    private String name;
    private String path;
    private String zipPath;
    /**
     * 音乐文件开放出来，不采用hashcode的隐藏文件做法
     * 并且音频文件放置sd中，开发媒体数据
     */
    private String musicDownPath;
    private String musicLocalPath;
    private int size;
    private int musicDuration;
    private String resName;
    private String resRequestPath;
    private String resLocalPath;
    private String musicName;
    private ThemeEnum mThemeEnum;
    private int ThemeConstanType;

    /**
     * 在hot分类下的
     */
    private boolean isHot = false;


    public SlideshowEntity(String resName, String resRequestPath, ThemeEnum mThemeEnum) {
        this.resName = resName;
        this.resRequestPath = resRequestPath;
        this.mThemeEnum = mThemeEnum;
    }

    public SlideshowEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ThemeEnum getThemeEnum() {
        return mThemeEnum;
    }

    public void setThemeEnum(ThemeEnum themeEnum) {
        mThemeEnum = themeEnum;
    }


    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResRequestPath() {
        return resRequestPath;
    }

    public void setResRequestPath(String resRequestPath) {
        this.resRequestPath = resRequestPath;
    }

    public String getResLocalPath() {
        return resLocalPath;
    }

    public void setResLocalPath(String resLocalPath) {
        this.resLocalPath = resLocalPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getZipPath() {
        return zipPath;
    }

    public void setZipPath(String zipPath) {
        this.zipPath = zipPath;
    }

    public String getMusicDownPath() {
        if (BuildConfig.VERSION_CODE < Build.VERSION_CODES.M) {
            return musicDownPath.replace(" ", "%20");
        }
        return musicDownPath;
    }

    public void setMusicDownPath(String musicDownPath) {
        this.musicDownPath = musicDownPath;
    }

    public String getMusicLocalPath() {
        return musicLocalPath;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setMusicLocalPath(String musicLocalPath) {
        this.musicLocalPath = musicLocalPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlideshowEntity entity = (SlideshowEntity) o;
        return mThemeEnum == entity.mThemeEnum && index == entity.getIndex();
    }

    @Override
    public int hashCode() {
        return Objects.hash(mThemeEnum);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getMusicDuration() {
        return musicDuration;
    }

    public void setMusicDuration(int musicDuration) {
        this.musicDuration = musicDuration;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public int getThemeConstanType() {
        return ThemeConstanType;
    }

    public void setThemeConstanType(int themeConstanType) {
        ThemeConstanType = themeConstanType;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }
}


