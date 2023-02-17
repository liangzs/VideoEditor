package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.valentine;

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

public class VTOnePreTreatment extends BasePreTreatment {
    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
        Resources resources = MediaSdk.getInstance().getResouce();
//        index = 4;
        switch (index % 5) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_one_theme1_2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_one_theme1_1"));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_one_theme2_3"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_one_theme2_1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_one_theme2_2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_one_particle6"));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_one_theme3_1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_one_theme3_2"));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_one_theme4_1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_one_particle6"));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_one_theme5_1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_one_theme5_2"));
                break;
        }
        return list;
    }

}