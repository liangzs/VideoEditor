package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class HD22PreTreatment extends BaseTimePreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        List<Bitmap> list = new ArrayList<>();
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday22_bottom"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday22_title"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday22_top"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday22_eye0"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday22_eye1"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday22_eye2"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday22_eye3"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday22_spider"));
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
