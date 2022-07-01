package com.ijoysoft.mediasdk.module.entity;

public class PhotoMediaItem extends MediaItem {
    private String title;
    private long size;

    public PhotoMediaItem(String path) {
        this.path = path;
    }

    public PhotoMediaItem() {
        mediaType = MediaType.PHOTO;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
