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
public class HDNinePreTreatment extends BasePreTreatment {
    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
//        index = 3;
        switch (index % 5) {
            case 0:
                if (ConstantMediaSize.ratioType == RatioType._9_16 || ConstantMediaSize.ratioType == RatioType._3_4) {
                    list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_nine_theme1_1" + SUFFIX));
                } else {
                    list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_nine_theme11_1_1" + SUFFIX));
                }
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_nine_theme1_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_nine_theme1_3" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_nine_theme1_4" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_nine_theme2_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_nine_theme2_2" + SUFFIX));
                break;
            case 2:
                if (ConstantMediaSize.ratioType == RatioType._9_16 || ConstantMediaSize.ratioType == RatioType._3_4) {
                    list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_nine_theme1_1" + SUFFIX));
                } else {
                    list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_nine_theme11_1_1" + SUFFIX));
                }
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_nine_theme3_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_nine_theme3_3" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_nine_theme4_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_nine_theme4_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_nine_theme4_3" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_nine_theme5_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_nine_theme5_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_nine_theme5_3" + SUFFIX));
                break;
        }
        return list;
    }


}
