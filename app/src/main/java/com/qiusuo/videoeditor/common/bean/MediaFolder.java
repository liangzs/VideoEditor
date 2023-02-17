package com.qiusuo.videoeditor.common.bean;

import com.qiusuo.videoeditor.util.TimeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 存储相册内的数据
 */
public class MediaFolder implements Serializable {

    private int bucketId = -1;

    private List<String> allDates;
    private List<String> videoDates;
    private List<String> photoDates;

    private Map<String, List<MediaEntity>> allMap;
    private Map<String, List<MediaEntity>> photoMap;
    private Map<String, List<MediaEntity>> videoMap;


    public List<String> getAllDates() {
        return allDates;
    }

    public void setAllDates(List<String> allDates) {
        this.allDates = allDates;
    }

    public List<String> getVideoDates() {
        return videoDates;
    }

    public void setVideoDates(List<String> videoDates) {
        this.videoDates = videoDates;
    }

    public List<String> getPhotoDates() {
        return photoDates;
    }

    public void setPhotoDates(List<String> photoDates) {
        this.photoDates = photoDates;
    }

    public Map<String, List<MediaEntity>> getAllMap() {
        return allMap;
    }

    public void setAllMap(Map<String, List<MediaEntity>> allMap) {
        this.allMap = allMap;
    }

    public Map<String, List<MediaEntity>> getPhotoMap() {
        return photoMap;
    }

    public void setPhotoMap(Map<String, List<MediaEntity>> photoMap) {
        this.photoMap = photoMap;
    }

    public Map<String, List<MediaEntity>> getVideoMap() {
        return videoMap;
    }

    public void setVideoMap(Map<String, List<MediaEntity>> videoMap) {
        this.videoMap = videoMap;
    }


    /**
     * 添加一个新的媒体文件
     *
     * @param mediaEntity
     */
    public void addLatestMedia(MediaEntity mediaEntity) {
        String date = TimeUtils.format(mediaEntity.dateAdd * 1000L, TimeUtils.Date_PATTER);
        if (!allDates.contains(date)) {
            allDates.add(0, date);
            allMap.put(date, new ArrayList<>());
            if (mediaEntity.type == MediaEntity.TYPE_VIDEO) {
                videoDates.add(0, date);
                videoMap.put(date, new ArrayList<>());
            } else {
                photoDates.add(0, date);
                photoMap.put(date, new ArrayList<>());
            }
        }
        allMap.get(date).add(0, mediaEntity);
        if (mediaEntity.type == MediaEntity.TYPE_VIDEO) {
            videoMap.get(date).add(0, mediaEntity);
        } else {
            photoMap.get(date).add(0, mediaEntity);
        }

    }

    public int getBucketId() {
        return bucketId;
    }

    public void setBucketId(int bucketId) {
        this.bucketId = bucketId;
    }
}
