package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class Common11PreTreatment extends BaseTimePreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        List<Bitmap> list = new ArrayList<>();
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common11_particle1"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common11_particle4"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common11_particle5"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common11_particle6"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common11_particle7"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common11_particle8"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common11_particle9"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common11_particle10"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common11_particle11"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common11_particle12"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common11_particle13"));
        return list;
    }

    @Override
    public int getMipmapsCount() {
        return 7;
    }

    @Override
    public long dealDuration(int index) {
        switch (index % getMipmapsCount()) {
            case 0:
                return 1000;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return 800;
            case 6:
                return 1800;
        }
        return DURATION;
    }
}
