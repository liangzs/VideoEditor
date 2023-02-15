package com.ijoysoft.mediasdk.module.entity;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * 涂鸦管理类
 */
public class DoodleItem implements Serializable {
    private String projectId;
    /**
     * 0代表文字，1代表涂鸦,2 贴图(后续),3水印
     */
    private WaterMarkType waterMarkType;
    /**
     * 优先级，设置该值后，重新排列顺序
     */
    private int priority;

    private String path;


    private String doodleSrcPath; //涂鸦原始路径

    private int width;
    private int height;
    /**
     * 记录涂鸦的归一化值，所以当比例进行切换,记录原来的显示区域
     */
    private float basew;
    private float baseH;
    private float tranx;
    private float trany;
    private float angle;
    private boolean gif;

    //显示周期
    private DurationInterval durationInterval;

    private int resourceId;//直接从apk中读取图片资源


    public DoodleItem(WaterMarkType type) {
        this.waterMarkType = type;
    }

    public DoodleItem() {
    }

    public void setRectBound(float baseW, float baseH, float tranX, float tranY, int width, int height, float angle) {
        this.basew = baseW;
        this.baseH = baseH;
        this.tranx = tranX;
        this.trany = tranY;
        if (angle < 0) {
            angle += 360f;
        }
        this.angle = angle;
        double sinAngle = Math.sin(Math.toRadians(Math.abs(angle)));
        double cosAngle = Math.cos(Math.toRadians(Math.abs(angle)));

        this.width = (int) (cosAngle * width + sinAngle * height);
        this.height = (int) (sinAngle * width + cosAngle * height);
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public DurationInterval getDurationInterval() {

        return durationInterval;
    }

    public void setDurationInterval(DurationInterval durationInterval) {
        this.durationInterval = durationInterval;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public WaterMarkType getWaterMarkType() {
        return waterMarkType;
    }

    public void setWaterMarkType(WaterMarkType waterMarkType) {
        this.waterMarkType = waterMarkType;
    }

    public String getDoodleSrcPath() {
        return doodleSrcPath;
    }

    public void setDoodleSrcPath(String doodleSrcPath) {
        this.doodleSrcPath = doodleSrcPath;
    }


    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }


    @Override
    public String toString() {
        return "DoodleItem{" +
                ", projectId='" + projectId + '\'' +
                ", waterMarkType=" + waterMarkType +
                ", priority=" + priority +
                ", path='" + path + '\'' +
                ", doodleSrcPath='" + doodleSrcPath + '\'' +
                ", baseW=" + basew +
                ", tranX=" + tranx +
                ", tranY=" + trany +
                ", width=" + width +
                ", height=" + height +
                ", angle=" + angle +
                ", durationInterval=" + durationInterval +
                ", resourceId=" + resourceId +
                '}';
    }

    public float getTranX() {
        return tranx;
    }

    public void setTranX(float tranX) {
        this.tranx = tranX;
    }

    public float getTranY() {
        return trany;
    }

    public void setTranY(float tranY) {
        this.trany = tranY;
    }

    public int getWidth() {
        return width;
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

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getBaseW() {
        return basew;
    }

    public void setBaseW(float baseW) {
        this.basew = baseW;
    }

    public float getBasew() {
        return basew;
    }

    public void setBasew(float basew) {
        this.basew = basew;
    }

    public float getTranx() {
        return tranx;
    }

    public void setTranx(float tranx) {
        this.tranx = tranx;
    }

    public float getTrany() {
        return trany;
    }

    public void setTrany(float trany) {
        this.trany = trany;
    }

    public boolean getGif() {
        return gif;
    }

    public void setGif(boolean gif) {
        this.gif = gif;
    }

    public float getBaseH() {
        return baseH;
    }

    public void setBaseH(float baseH) {
        this.baseH = baseH;
    }
}
