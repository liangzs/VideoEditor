package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.SUFFIX;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Common19PreTreatment extends BaseTimePreTreatment {


    @Override
    public int getMipmapsCount() {
        return 7;
    }

    @Override
    public List<Bitmap> createMimapBitmaps() {
        return null;
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

    @Override
    public List<Bitmap> getMimapBitmaps(RatioType ratioType, int index) {
        if (index == 0) {
            return Collections.singletonList(
                    BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common19_title" + SUFFIX)
            );
        }
        return null;
    }
}
