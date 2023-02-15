package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holi;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class HoliFivePreTreatment extends BasePreTreatment {

    private final int MIPCOUNT = 5;

    @Override
    public int getMipmapsCount() {
        return MIPCOUNT;
    }

    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
        switch (index % MIPCOUNT) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_five_theme1_1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_five_theme1_2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_five_theme1_3"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_five_theme1_4"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_five_theme1_5"));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_five_theme2_1"));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_five_theme3_1"));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_five_theme4_1"));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_five_theme5_1"));
                break;
        }
        return list;
    }

}
