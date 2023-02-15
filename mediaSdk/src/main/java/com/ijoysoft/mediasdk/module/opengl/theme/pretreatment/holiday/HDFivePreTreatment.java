package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

/**
 * 加填一个半透明层
 */
public class HDFivePreTreatment extends BasePreTreatment {
    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_five_theme916_border" + SUFFIX));
        list.addAll(getRatio(index));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_five_theme916_bat" + SUFFIX));
        return list;
    }

    @Override
    public List<Bitmap> getRatio169(int index) {
        List<Bitmap> list = new ArrayList<>();
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_five_theme169_border" + SUFFIX));
        list.addAll(getRatio(index));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_five_theme169_bat" + SUFFIX));
        return list;
    }

    @Override
    public List<Bitmap> getRatio11(int index) {
        List<Bitmap> list = new ArrayList<>();
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_five_theme11_border" + SUFFIX));
        list.addAll(getRatio(index));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_five_theme11_bat" + SUFFIX));
        return list;
    }

    /**
     * @return
     */
    private List<Bitmap> getRatio(int index) {
        List<Bitmap> list = new ArrayList<>();
//        index = 4;
        switch (index % 5) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_five_theme1_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_five_theme1_2" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_five_theme2_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_five_theme2_2" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_five_theme3_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_five_theme3_2" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_five_theme4_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_five_theme4_2" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_five_theme5_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_five_theme5_2" + SUFFIX));
                break;
        }
        return list;
    }

}
