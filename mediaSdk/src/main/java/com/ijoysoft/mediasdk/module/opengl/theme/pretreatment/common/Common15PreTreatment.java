package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.List;

public class Common15PreTreatment extends BaseTimePreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        return null;
    }

    @Override
    public int getMipmapsCount() {
        return 12;
    }

    @Override
    public long dealDuration(int index) {
//        index = 1;
        switch (index % getMipmapsCount()) {
            //左右移动
            case 0:
                return 800;
            case 6:
                return 1200;
            default:
                return 600;
        }
    }
}
