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

public class HoliThreePreTreatment extends BasePreTreatment {

    private final int MIPCOUNT = 4;

    @Override
    public int getMipmapsCount() {
        return MIPCOUNT;
    }

    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
        Resources resources = MediaSdk.getInstance().getResouce();
        switch (index % MIPCOUNT) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_three_theme1_1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_three_theme1_2"));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_three_theme2_1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_three_theme2_2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_three_theme2_3"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_three_theme2_4"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_three_theme2_5"));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_three_theme3_1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_three_theme3_3"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_three_theme3_2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_three_theme3_4"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_three_theme3_5"));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_three_theme4_2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_three_theme4_1"));
                break;
        }
        return list;
    }

}
