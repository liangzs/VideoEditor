package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

/**
 * 加填一个半透明层
 */
public class HD33PreTreatment extends BasePreTreatment {
    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
//        index = 3;
        switch (index % 3) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday33_one_9_16" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday33_two_9_16" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday33_three_9_16" + SUFFIX));
                break;
            default:
        }
        return list;
    }

    @Override
    public List<Bitmap> getRatio169(int index) {
        List<Bitmap> list = new ArrayList<>();
//        index = 3;
        switch (index % 3) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday33_one_16_9" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday33_two_16_9" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday33_three_16_9" + SUFFIX));
                break;
            default:


        }
        return list;
    }

    @Override
    public List<Bitmap> getRatio11(int index) {
        List<Bitmap> list = new ArrayList<>();
//        index = 3;
        switch (index % 3) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday33_one_1_1" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday33_two_1_1" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday33_three_1_1" + SUFFIX));
                break;
            default:


        }
        return list;

    }
}
