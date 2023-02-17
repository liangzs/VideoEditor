package com.qiusuo.videoeditor.common.bean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.lang.ref.SoftReference;

public class MediaSet implements Serializable {
    public int bucketId;
    public String name;
    public int count;
    public int photoCount;
    public int videoCount;
    public String coverpath;


    public MediaSet(int bucketId, String name, int count, String coverpath, boolean isImage) {
        this.bucketId = bucketId;
        if (name == null && coverpath.startsWith("/storage/emulated/0/")) {
            this.name = "0";
        } else {
            this.name = name;
        }
        this.count = count;
        this.coverpath = coverpath;
        if (isImage) {
            photoCount = count;
        } else {
            videoCount = count;
        }
    }

    public MediaSet() {

    }

    public SoftReference<Drawable> mThumbCache;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MediaSet other = (MediaSet) obj;
        return bucketId == other.bucketId;
    }


    public int getBucketId() {
        return bucketId;
    }

    public void setBucketId(int bucketId) {
        this.bucketId = bucketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCoverpath() {
        return coverpath;
    }

    public void setCoverpath(String coverpath) {
        this.coverpath = coverpath;
    }

}
