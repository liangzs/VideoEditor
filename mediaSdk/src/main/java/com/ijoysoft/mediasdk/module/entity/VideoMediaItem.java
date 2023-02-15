package com.ijoysoft.mediasdk.module.entity;

import android.graphics.Bitmap;
import android.util.ArrayMap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;

import java.util.Map;

public class VideoMediaItem extends MediaItem {
    private long size;
    // 码率
    private int bitRate;
    private String extracAudioPath;
    // 保存修改音量后的路径
    private String volumePath;
    /**
     * 现改变策略，一个视频内的音量只为一段，
     * 原视频音量跨了很多间断，需要赋值很多音量
     */
    //0-200
    private float volume;
    private Map<Integer, Bitmap> thumbnails;
    private String videoTrimAudioPath;
    private String extractTaskId;//音频抽取任务id
    //视频变速的路径
    private String speedPath;
    private String reversePath;

    public VideoMediaItem() {
        super();
        mediaType = MediaType.VIDEO;
        volume = 100f;
        thumbnails = new ArrayMap<>();
    }

    public float getPlayVolume() {
        return volume / ConstantMediaSize.MAX_VOLUME;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }


    public String getExtracAudioPath() {
        return extracAudioPath;
    }

    public void setExtracAudioPath(String extracAudioPath) {
        this.extracAudioPath = extracAudioPath;
    }

    public String getVolumePath() {
        return volumePath;
    }

    public float getVolume() {
        return volume;
    }


    public void setVolumePath(String volumePath) {
        this.volumePath = volumePath;
    }

    public Map<Integer, Bitmap> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Map<Integer, Bitmap> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getVideoTrimAudioPath() {
        return videoTrimAudioPath;
    }

    public void setVideoTrimAudioPath(String videoTrimAudioPath) {
        this.videoTrimAudioPath = videoTrimAudioPath;
    }

    public String getExtractTaskId() {
        return extractTaskId;
    }

    public void setExtractTaskId(String extractTaskId) {
        this.extractTaskId = extractTaskId;
    }

    public String getSpeedPath() {
        return speedPath;
    }

    public void setSpeedPath(String speedPath) {
        this.speedPath = speedPath;
    }


    public String getReversePath() {
        return reversePath;
    }

    public void setReversePath(String reversePath) {
        this.reversePath = reversePath;
    }

    /**
     * 获取最终的路径
     *
     * @return
     */
    public String getFinalPath() {
        String path = ObjectUtils.isEmpty(getTrimPath()) ? getPath() : getTrimPath();
        return ObjectUtils.isEmpty(getReversePath()) ? path : getReversePath();
    }

    public String getFinalPath(boolean origin) {
        if (!origin) {
            return getFinalPath();
        }
        return getPath();
    }

}
