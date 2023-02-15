package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.birthday;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class BDFourteenPreTreatment extends BasePreTreatment {
    private final int MIPCOUNT = 5;

    @Override
    public int getMipmapsCount() {
        return MIPCOUNT;
    }

    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
//        index = 0;
        switch (index % MIPCOUNT) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_text_bg" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_theme1_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_theme1_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_particle1" + SUFFIX));
                break;
            case 1:
                if (ConstantMediaSize.ratioType == RatioType._16_9 || ConstantMediaSize.ratioType == RatioType._4_3) {
                    list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_theme169_2_1" + SUFFIX));
                } else {
                    list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_theme2_1" + SUFFIX));
                }
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_theme2_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_particle3" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_text_bg" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_theme3_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_theme3_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_particle1" + SUFFIX));
                break;
            case 3:
                if (ConstantMediaSize.ratioType == RatioType._16_9 || ConstantMediaSize.ratioType == RatioType._4_3) {
                    list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_theme169_4_1" + SUFFIX));
                } else {
                    list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_theme4_1" + SUFFIX));
                }
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_theme4_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_particle3" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_text_bg" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_theme5_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_fourteen_particle1" + SUFFIX));
                break;
        }
        return list;
    }
}
