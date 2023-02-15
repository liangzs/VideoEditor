package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.SUFFIX;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hayring
 * @date 2021/11/17  9:35
 * @description 下载好图片读取
 */
public abstract class DownloadedPreTreatmentTemplate extends BasePreTreatment {

    @Override
    public int afterPreRotation() {
        return 0;
    }


    /**
     * 获取某一action的素材数组
     * @param index action index
     * @return
     */
    protected String[] getSource(int index) {
        return getSources()[index];
    }

    /**
     * 获取所有素材数组
     * @return sources
     */
    protected abstract String[][] getSources();

    @Override
    public int getMipmapsCount() {
        return getSources().length;
    }


    /**
     *
     * @param ratioType 比例类型
     * @param index 第几个图片，可以未取模
     * @return bitmaps
     */
    @Override
    public List<Bitmap> getMimapBitmaps(RatioType ratioType, int index) {
        this.mRatioType = ratioType;
        index = index % getMipmapsCount();
        String[] res;
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
                res = new String[0];
                break;
            default:
                res = getSource(index);
        }
        return getMipMapsByResource(res);
    }

    /**
     * 从src数组中获取bitmap
     * @param res resourceId数组
     * @return bitmaps
     */
    public List<Bitmap> getMipMapsByResource(String[] res) {
        List<Bitmap> list = new ArrayList<>();
        for (String resource : res) {
            list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + resource + SUFFIX));

        }
        return list;
    }

    /**
     * 获取1-1素材数组
     * @param index 第几个action
     * @return 默认返回9-16的数组
     */
    protected String[] getSources_1_1(int index) {
        return getSource(index);
    }

    /**
     * 获取16-9素材数组
     * @param index 第几个action
     * @return 默认返回9-16的数组
     */
    protected String[] getSources_16_9(int index) {
        return getSource(index);
    }
    /**
     * 获取4-3素材数组
     * @param index 第几个action
     * @return 默认返回16-9的数组
     */
    protected String[] getSources_4_3(int index) {
        return getSources_16_9(index);
    }

    /**
     * 获取3-4素材数组
     * @param index 第几个action
     * @return 默认返回9-16的数组
     */
    protected String[] getSources_3_4(int index) {
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


//    /**
//     * 加载gif
//     * @param ratioType 比例（暂时不用）
//     * @param index 下标
//     * @return
//     */
//    @Override
//    public List<List<GifDecoder.GifFrame>> getDynamicMimapBitmaps(RatioType ratioType, int index) {
//        String[][] gifSource;
//        if ((gifSource = getDynamicSources()) == null || gifSource[index] == null || gifSource[index].length == 0) {
//            return null;
//        }
//        List<List<GifDecoder.GifFrame>> result = new ArrayList<>();
//        for (String source : gifSource[index]) {
//            GifDecoder mGifDecoder = new GifDecoder();
//            InputStream inputStream = null;
//            try {
//                inputStream = new FileInputStream(ConstantMediaSize.themePath + source + SUFFIX);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            //这里面的方法会对inpustream进行close
//            mGifDecoder.read(inputStream);
//            List<GifDecoder.GifFrame> frames = mGifDecoder.getFrames();
//            for (int i = 0; i < frames.size(); i++) {
//                if (frames.get(i).delay == 0) {
//                    frames.get(i).delay = frames.get(i-1).delay;
//                }
//            }
//            result.add(frames);
//        }
//        return result;
//    }


}
