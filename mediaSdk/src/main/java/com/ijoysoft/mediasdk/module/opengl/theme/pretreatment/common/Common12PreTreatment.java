package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.List;

public class Common12PreTreatment extends BaseTimePreTreatment {
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
//        index = 29;
        switch (index % getMipmapsCount()) {
            //左右移动
            case 0:
                return 600;
            case 1:
                return 400;
            case 3:
            case 14:
            case 15:
            case 16:
            case 2:
                return 1200;
            //缩放
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                return 400;
            case 13:
                //缩放平移
                return 800;
            case 17:
                //左右移动，旋转
                return 800;
            case 26:
                return 800;
            case 27:
                return 1200;
            case 28:
                return 1500;
            case 29:
                return 2000;
            default:
                return 600;
        }
    }
}
