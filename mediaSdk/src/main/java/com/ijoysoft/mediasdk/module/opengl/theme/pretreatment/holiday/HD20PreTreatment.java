package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class HD20PreTreatment extends BaseTimePreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        List<Bitmap> list = new ArrayList<>();
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday20_weight"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday20_particle"));
        return list;
    }

    @Override
    public int getMipmapsCount() {
        return 1;
    }

    @Override
    public long dealDuration(int index) {
        return 2000;
    }
}
