package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.SUFFIX;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Common5PreTreatment extends BaseTimePreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        return null;
    }

    @Override
    public int getMipmapsCount() {
        return 15;
    }

    @Override
    public long dealDuration(int index) {
        if (index%15 == 0) {
            return 1920;
        }
        return 480;
    }


//    public static final String[][] DYNAMIC_MIMAP_BITMAP_SOURCES = new String[][]{
//            {"/common5_dynamic"},
//    };
//
//    @Override
//    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
//        return DYNAMIC_MIMAP_BITMAP_SOURCES;
//    }
//
//    @Override
//    public List<Bitmap> getMimapBitmaps(RatioType ratioType, int index) {
//        if (index == 0) {
//            return Collections.singletonList(
//                    BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common5_flag" + SUFFIX)
//            );
//        }
//        return null;
//    }
}
