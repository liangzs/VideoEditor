package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.travel;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据图片的比例进行加边框，而不是根据显示区域的比例进行选择
 */
public class TravelThreePreTreatment extends BasePreTreatment {
    @Override
    public List<Bitmap> getRatio916(int index) {
//        index = 4;
        List<Bitmap> list = new ArrayList<>();
        switch (index % 5) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme916_1_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme1_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme1_3" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme916_2_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme2_2" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme916_3_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme3_2" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme916_4_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme4_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme1_3" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme916_5_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme5_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme2_3" + SUFFIX));
                break;
        }
        return list;
    }

    @Override
    public List<Bitmap> getRatio11(int index) {
//        index = 4;
        List<Bitmap> list = new ArrayList<>();
        switch (index % 5) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme11_1_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme1_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme1_3" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme11_2_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme2_2" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme11_3_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme3_2" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme11_4_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme4_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme1_3" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme11_5_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme5_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme2_3" + SUFFIX));
                break;
        }
        return list;
    }

    @Override
    public List<Bitmap> getRatio169(int index) {
//        index = 4;
        List<Bitmap> list = new ArrayList<>();
        switch (index % 5) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme169_1_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme1_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme1_3" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme169_2_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme2_2" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme169_3_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme3_2" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme169_4_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme4_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme1_3" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme169_5_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme5_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_three_theme2_3" + SUFFIX));
                break;
        }
        return list;
    }
}
