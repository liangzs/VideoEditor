package com.ijoysoft.mediasdk.module.entity;

import java.io.Serializable;

/**
 * 记录media的旋转、缩放、平移，最终转化成matrix进行操作
 * 图片的缩放平移由graphics.Matrix向opengl.Matrix进行过度
 */
public class MediaMatrix implements Serializable, Cloneable {
    private int angle;
    private float scale = 1.0f;
    private int offsetX;
    private int offsetY;
    private int originX;
    private int originY;
    /**
     * photoview控件在做旋转时，也会做缩放动作，但是和opengl的缩放不同，
     * opengl的旋转scale是1的，但是控件进行旋转的时候，它的缩放值是会发生变化的
     * 这就导致两者进行转化的时候，去除之间的缩放差值.
     */
    private float basicScale = 1f;


    public MediaMatrix() {
    }

    public MediaMatrix(int angle) {
        this.angle = angle;
    }

    public MediaMatrix(float scale, int offsetX, int offsetY) {
        this.scale = scale;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public MediaMatrix(float scale, int offsetX, int offsetY, int originX, int originY) {
        this.scale = scale;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.originX = originX;
        this.originY = originY;
    }

    public MediaMatrix(int angle, float scale, int offsetX, int offsetY) {
        this.angle = angle;
        this.scale = scale;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getOriginX() {
        return originX;
    }

    public void setOriginX(int originX) {
        this.originX = originX;
    }

    public int getOriginY() {
        return originY;
    }

    public void setOriginY(int originY) {
        this.originY = originY;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        MediaMatrix mediaMatrix = (MediaMatrix) super.clone();
        return mediaMatrix;
    }

    public float getBasicScale() {
        return basicScale;
    }

    public void setBasicScale(float basicScale) {
        this.basicScale = basicScale;
    }
}
