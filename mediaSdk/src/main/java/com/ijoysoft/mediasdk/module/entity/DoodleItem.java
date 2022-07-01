package com.ijoysoft.mediasdk.module.entity;

import java.io.Serializable;

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
     * x偏移量
     */
    private int x;

    //y偏移量
    private int y;

    //做的旋转动作
    private int angle;
    //显示周期
    private DurationInterval durationInterval;


    public DoodleItem(WaterMarkType type) {
        this.waterMarkType = type;
    }

    public DoodleItem() {

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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
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


    @Override
    public String toString() {
        return "DoodleItem{" +
                "projectId='" + projectId + '\'' +
                ", type='" + waterMarkType + '\'' +
                ", priority=" + priority +
                ", path='" + path + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", x=" + x +
                ", y=" + y +
                ", angle=" + angle +
                ", durationInterval=" + durationInterval +
                '}';
    }
}
