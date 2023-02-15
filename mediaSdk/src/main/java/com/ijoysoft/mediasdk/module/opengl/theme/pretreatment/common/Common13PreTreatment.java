package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.List;

public class Common13PreTreatment extends BaseTimePreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        return null;
    }

    @Override
    public int getMipmapsCount() {
        return 30;
    }

    @Override
    public long dealDuration(int index) {
        switch (index % getMipmapsCount()) {
            //左右移动
            case 0:
            case 4:
                return 600;
            case 1:
                return 400;
            case 2:
            case 3:
            case 9:
            case 17:
                return 1200;
            case 10:
            case 11:
                return 2000;
            default:
                return 600;
        }
    }
}
