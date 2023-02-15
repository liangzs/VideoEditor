package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.celebration;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

/**
 * 加填一个半透明层
 */
public class CDOnePreTreatment extends BasePreTreatment {
    @Override
    public List<Bitmap> getRatio169(int index) {
        List<Bitmap> list = new ArrayList<>();
        switch (index % 5) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_one_theme169_1_1" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_one_theme2_1" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_one_theme169_3_1" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_one_theme4_1" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_one_theme169_5_1" + SUFFIX));
                break;
        }
        return list;
    }

    @Override
    public List<Bitmap> getRatio11(int index) {
        List<Bitmap> list = new ArrayList<>();
        switch (index % 5) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_one_theme11_1_1" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_one_theme2_1" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_one_theme11_3_1" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_one_theme4_1" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_one_theme11_5_1" + SUFFIX));
                break;
        }
        return list;

    }

    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
        switch (index % 5) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_one_theme1_1" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_one_theme2_1" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_one_theme3_1" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_one_theme4_1" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_one_theme5_1" + SUFFIX));
                break;
        }
        return list;
    }
}
