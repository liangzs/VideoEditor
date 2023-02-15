package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common.Common15PreTreatment;

import java.util.ArrayList;
import java.util.List;

public class HD21PreTreatment extends Common15PreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        List<Bitmap> list = new ArrayList<>();
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday21_blood"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday21_hand"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday21_left_hand"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday21_net"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday21_right_hand"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday21_particle"));
        return list;
    }

    @Override
    public long dealDuration(int index) {
        return 800;
    }

    @Override
    public int getMipmapsCount() {
        return 6;
    }




}
