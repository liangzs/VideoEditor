package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class Common10PreTreatment extends BaseTimePreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        return null;
    }

    @Override
    public int getMipmapsCount() {
        return 5;
    }

    @Override
    public long dealDuration(int index) {
        switch (index % getMipmapsCount()) {
            case 0:
                return 600;
            case 1:
            case 2:
            case 3:
                return 480;
            case 4:
                return 2400;
        }
        return DURATION;
    }
}
