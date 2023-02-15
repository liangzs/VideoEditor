package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.module.entity.RatioType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hayring
 * @date 2021/11/17  9:35
 * @description 本地图片读取
 */
public abstract class LocalPreTreatmentTemplate extends BasePreTreatment {
    public static final String END_SUFFIX = ".local";

    @Override
    public int afterPreRotation() {
        return 0;
    }


    /**
     * 获取某一action的素材数组
     *
     * @param index action index
     * @return
     */
    protected int[] getSource(int index) {
        return getSources()[index % getSources().length];
    }

    /**
     * 获取所有素材数组
     *
     * @return sources
     */
    protected abstract int[][] getSources();

    @Override
    public int getMipmapsCount() {
        return getSources().length;
    }


    /**
     * @param ratioType 比例类型
     * @param index     第几个图片，可以未取模
     * @return bitmaps
     */
    @Override
    public List<Bitmap> getMimapBitmaps(RatioType ratioType, int index) {
        this.mRatioType = ratioType;
        index = index % getMipmapsCount();
        int[] res;
        switch (ratioType) {
            case _1_1:
                res = getSources_1_1(index);
                break;
            case _3_4:
                res = getSources_3_4(index);
                break;
            case _4_3:
                res = getSources_4_3(index);
                break;
            case _16_9:
                res = getSources_16_9(index);
                break;
            case NONE:
                res = new int[0];
                break;
            default:
                res = getSource(index);
        }
        return getMipMapsByResource(res);
    }

    /**
     * 从src数组中获取bitmap
     *
     * @param res resourceId数组
     * @return bitmaps
     */
    public List<Bitmap> getMipMapsByResource(int[] res) {
        List<Bitmap> list = new ArrayList<>();
        Resources resources = MediaSdk.getInstance().getResouce();
        for (int resource : res) {
            list.add(BitmapFactory.decodeResource(resources, resource));
        }
        return list;
    }

    /**
     * 获取1-1素材数组
     *
     * @param index 第几个action
     * @return 默认返回9-16的数组
     */
    protected int[] getSources_1_1(int index) {
        return getSource(index);
    }

    /**
     * 获取16-9素材数组
     *
     * @param index 第几个action
     * @return 默认返回9-16的数组
     */
    protected int[] getSources_16_9(int index) {
        return getSource(index);
    }

    /**
     * 获取4-3素材数组
     *
     * @param index 第几个action
     * @return 默认返回16-9的数组
     */
    protected int[] getSources_4_3(int index) {
        return getSources_16_9(index);
    }

    /**
     * 获取3-4素材数组
     *
     * @param index 第几个action
     * @return 默认返回9-16的数组
     */
    protected int[] getSources_3_4(int index) {
        return getSource(index);
    }


    @Override
    public List<Bitmap> getRatio11(int index) {
        return getMimapBitmaps(RatioType._1_1, index);
    }

    @Override
    public List<Bitmap> getRatio169(int index) {
        return getMimapBitmaps(RatioType._16_9, index);
    }

    @Override
    public List<Bitmap> getRatio916(int index) {
        return getMimapBitmaps(RatioType._9_16, index);
    }

    @Override
    public List<Bitmap> getRatio43(int index) {
        return getMimapBitmaps(RatioType._4_3, index);
    }

    @Override
    public List<Bitmap> getRatio34(int index) {
        return getMimapBitmaps(RatioType._3_4, index);
    }


}
