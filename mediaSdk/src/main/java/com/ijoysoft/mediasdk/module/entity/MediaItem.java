package com.ijoysoft.mediasdk.module.entity;

import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter;

import java.io.Serializable;
import java.util.UUID;

/**
 * 媒体基类，记录视频，照片
 * 图片的源不再通过transitionfilter进行推动，图片源应该单独做一个纯afilter进行推动
 * 把transition和图片源进行分解
 */
public class MediaItem implements Serializable, Cloneable {
    private String projectId;
    protected String name;
    protected String path;
    protected MediaType mediaType;
    // 分辨率
    private int width;
    private int height;

    // 视频转的方向
    private int rotation;
    private long duration;

    private long tempDuration;
    private String firstFramePath;
    private String prepareFramePath;
    private String videolastFramePath;
    // 前一张纹理的角度
    private int preRotation;

    // 视频的最后一帧

    /**
     * 转场效果
     */
    private TransitionFilter transitionFilter;

    private GPUImageFilter afilter;
    // 当前播放进度
    public int currentSeek;

    // 旋转缩放平移
    private MediaMatrix mediaMatrix;

    /**
     * 对于视频裁剪，进行逻辑裁剪,放基类算了，放videoMediaItem转型太多
     * 最终放弃这个方案，采取物理裁剪方案，实时播放采取被裁剪后文件，该文件只记录值
     * 原因，mediaplayer没有实际播放完毕，状态不好控制，使得工作台播放不出来
     */
    private DurationInterval videoCutInterval;

    /**
     * 视频有裁剪记录后，对被裁结果进行覆盖
     */
    private String trimPath;
    private String equalId;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
        tempDuration = duration;
    }

    public void setDurationOnly(long duration) {
        this.duration = duration;
    }

    public int getCurrentSeek() {
        return currentSeek;
    }

    public void setCurrentSeek(int currentSeek) {
        this.currentSeek = currentSeek;
    }

    public TransitionFilter getTransitionFilter() {
        return transitionFilter;
    }

    public void setTransitionFilter(TransitionFilter transitionFilter) {
        this.transitionFilter = null;
        this.transitionFilter = transitionFilter;
    }

    public String getVideolastFramePath() {
        return videolastFramePath;
    }

    public void setVideolastFramePath(String videolastFramePath) {
        this.videolastFramePath = videolastFramePath;
    }

    public String getFirstFramePath() {
        return firstFramePath;
    }

    public void setFirstFramePath(String firstFramePath) {
        this.firstFramePath = firstFramePath;
    }

    public String getPrepareFramePath() {
        return prepareFramePath;
    }

    public void setPrepareFramePath(String prepareFramePath) {
        this.prepareFramePath = prepareFramePath;
    }

    public void setAfilter(GPUImageFilter afilter) {
        this.afilter = null;
        this.afilter = afilter;
    }

    public MediaItem() {
        equalId = UUID.randomUUID().toString();
    }

    public MediaItem(String path) {
        this.path = path;
        equalId = UUID.randomUUID().toString();
    }

    @Override
    public Object clone() {
        MediaItem mediaTemp = null;
        try {
            mediaTemp = (MediaItem) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return mediaTemp;
    }

    public MediaMatrix getMediaMatrix() {
        return mediaMatrix;
    }

    public void setMediaMatrix(MediaMatrix mediaMatrix) {
        this.mediaMatrix = mediaMatrix;
    }

    public DurationInterval getVideoCutInterval() {
        return videoCutInterval;
    }

    public void setVideoCutInterval(DurationInterval videoCutInterval) {
        this.videoCutInterval = videoCutInterval;
    }

    public String getTrimPath() {
        return trimPath;
    }

    public void setTrimPath(String trimPath) {
        this.trimPath = trimPath;
    }

    public int getPreRotation() {
        return preRotation;
    }

    public void setPreRotation(int preRotation) {
        this.preRotation = preRotation;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getEqualId() {
        return equalId;
    }

    public void setEqualId(String equalId) {
        this.equalId = equalId;
    }

    public long getTempDuration() {
        return tempDuration;
    }

    public void setTempDuration(long tempDuration) {
        this.tempDuration = tempDuration;
    }

    public GPUImageFilter getAfilter() {
        return afilter;
    }

    @Override
    public String toString() {
        return "MediaItem{" + "projectId='" + projectId + '\'' + ", name='" + name + '\'' + ", path='" + path + '\''
                + ", mediaType=" + mediaType + ", width=" + width + ", height=" + height + ", rotation=" + rotation
                + ", duration=" + duration + ", firstFrame=" + firstFramePath + ", prepareFrame=" + prepareFramePath
                + ", preRotation=" + preRotation + ", videolastFrame=" + videolastFramePath + ", transitionFilter="
                + transitionFilter + ", afilter=" + afilter + ", currentSeek=" + currentSeek + ", mediaMatrix="
                + mediaMatrix + ", videoCutInterval=" + videoCutInterval + ", trimPath='" + trimPath + '\''
                + ", equalId='" + equalId + '\'' + '}';
    }
}
