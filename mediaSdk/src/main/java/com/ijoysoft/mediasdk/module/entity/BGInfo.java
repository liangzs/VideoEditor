package com.ijoysoft.mediasdk.module.entity;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.view.BackgroundType;

import java.io.Serializable;

/**
 * 背景设置
 */
public class BGInfo implements Serializable {
    private @BackgroundType.Type
    int backroundType;
    //自身模糊
    private int blurLevel;
    //颜色
    private int[] rgbs;
    //自定义背景
    private float zoomScale;

    private transient Bitmap customBitmap;

    public BGInfo() {
        backroundType = BackgroundType.SELF;
        blurLevel = 20;
    }

    public int getBackroundType() {
        return backroundType;
    }

    public void setBackroundType(int backroundType) {
        this.backroundType = backroundType;
    }

    public int getBlurLevel() {
        return blurLevel;
    }

    public void setBlurLevel(int blurLevel) {
        this.blurLevel = blurLevel;
    }

    public int[] getRgbs() {
        return rgbs;
    }

    public void setRgbs(int[] rgbs) {
        this.rgbs = rgbs;
    }

    public Bitmap getCustomBitmap() {
        return customBitmap;
    }

    public void setCustomBitmap(Bitmap customBitmap) {
        this.customBitmap = customBitmap;
    }

    public float getZoomScale() {
        return zoomScale;
    }

    public void setZoomScale(float zoomScale) {
        this.zoomScale = zoomScale;
    }
}
