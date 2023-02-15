package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class HD24PreTreatment extends BaseTimePreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        List<Bitmap> list = new ArrayList<>();
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday24acton1_9_16"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday24acton2_9_16"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday24acton3_9_16"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday24acton1_16_9"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday24acton2_16_9"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday24acton3_16_9"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday24acton1_1_1"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday24acton2_1_1"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday24acton3_1_1"));
        return list;
    }

    @Override
    public int getMipmapsCount() {
        return 6;
    }

    @Override
    public long dealDuration(int index) {
        return 2000;
    }
}
