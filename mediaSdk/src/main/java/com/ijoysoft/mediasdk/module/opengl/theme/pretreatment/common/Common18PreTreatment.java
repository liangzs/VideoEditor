package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.SUFFIX;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.Collections;
import java.util.List;

public class Common18PreTreatment extends BaseTimePreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        return null;
    }

    @Override
    public int getMipmapsCount() {
        return 6;
    }

    @Override
    public long dealDuration(int index) {
//        index = 1;
        switch (index % getMipmapsCount()) {
            //左右移动
            case 0:
            case 1:
                return 2000;
            default:
                return 1600;
        }
    }


    public static final String[][] DYNAMIC_MIMAP_BITMAP_SOURCES = new String[][]{
            {"/common18_dynamic"},
    };

    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return DYNAMIC_MIMAP_BITMAP_SOURCES;
    }

    @Override
    public List<Bitmap> getMimapBitmaps(RatioType ratioType, int index) {
        if (index == 0) {
            return Collections.singletonList(
                    BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common18_title" + SUFFIX)
            );
        }
        return null;
    }
}
