package com.qiusuo.videoeditor.common.bean;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.ColorUtil;
import com.ijoysoft.mediasdk.module.entity.BGInfo;
import com.ijoysoft.mediasdk.view.BackgroundType;

import java.io.Serializable;

/**
 * sp存储背景信息
 */
public class SpBgInfo implements Serializable {
    private int backgrondType;
    private int colorValue;
    private int blurLevel;
    //如果是gallery,groupIndex赋值为-1
    private int groupIndex;
    private String customPath;
    private float zoomScale;

    private transient Bitmap blurBitmap;

    public int getBackgrondType() {
        return backgrondType;
    }

    public void setBackgrondType(int backgrondType) {
        this.backgrondType = backgrondType;
    }

    public int getColorValue() {
        return colorValue;
    }

    public void setColorValue(int colorValue) {
        this.colorValue = colorValue;
    }

    public int getBlurLevel() {
//        if (blurLevel == 0) {
//            return ConstantMediaSize.DEFAULT_BG_BLUR;
//        }
        return blurLevel;
    }

    public void setBlurLevel(int blurLevel) {
        this.blurLevel = blurLevel;
    }

    public String getCustomPath() {
        return customPath;
    }

    public void setCustomPath(String customPath) {
        this.customPath = customPath;
    }

    public int getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
    }

    public Bitmap getBlurBitmap() {
        return blurBitmap;
    }

    public void setBlurBitmap(Bitmap blurBitmap) {
        this.blurBitmap = blurBitmap;
    }

    public static SpBgInfo defaultBgInfo() {
        SpBgInfo spBgInfo = new SpBgInfo();
        spBgInfo.setBackgrondType(BackgroundType.SELF);
        spBgInfo.setBlurLevel(ConstantMediaSize.DEFAULT_BG_BLUR);
        return spBgInfo;
    }

    public static BGInfo getBGInfo(SpBgInfo spBgInfo) {
        BGInfo bgInfo = new BGInfo();
        bgInfo.setBackroundType(spBgInfo.getBackgrondType());
        bgInfo.setBlurLevel(spBgInfo.getBlurLevel());
        bgInfo.setCustomBitmap(spBgInfo.getBlurBitmap());
        String hexColor = "#FF000000";
        if (spBgInfo.getColorValue() != 0) {
            hexColor = "#" + Integer.toHexString(spBgInfo.getColorValue());
        }

        if (hexColor.length() == 7) {
            hexColor = hexColor.replace("#", "#FF");
        }
        int[] rgba = ColorUtil.hex2Rgb(hexColor);
        bgInfo.setRgbs(rgba);
        return bgInfo;
    }

    public float getZoomScale() {
        return zoomScale;
    }

    public void setZoomScale(float zoomScale) {
        this.zoomScale = zoomScale;
    }
}

