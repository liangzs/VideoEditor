package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;


import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.SUFFIX;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//public class Common16PreTreatment extends DownloadedPreTreatmentTemplate {
//
//
//    private static final String[][] SRCS = new String[][]{
//            {"/common16_pink_camera", "/common16_blue_camera"},
//            {"/common16_pink_camera", "/common16_blue_camera"},
//            {"/common16_gift"},
//            {"/common16_gift"},
//    };
//
//
//
//
//    @Override
//    protected String[][] getSources() {
//        return SRCS;
//    }
//
//    /**
//     * Opengl转场时间
//     * @return {@link com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment#THEME_TWO_DURATION}
//     */
//    @Override
//    public int createDuration() {
//        return THEME_TWO_DURATION;
//    }
//
//}
public class Common16PreTreatment  extends BaseTimePreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        return getMimapBitmaps(RatioType._9_16, 0);
    }

    @Override
    public int getMipmapsCount() {
        return 15;
    }

    @Override
    public long dealDuration(int index) {
        if (index == 0) {
            return 1920;
        }
//        if (index == 7) {
//            return 3500;
//        }
        return 480;
    }

        private static final String[] SRCS = new String[]{
            "/common16_pink_camera", "/common16_blue_camera", "/common16_gift"
    };

    @Override
    public List<Bitmap> getMimapBitmaps(RatioType ratioType, int index) {
        if (index == 0) {
            return Arrays.asList(
                    BitmapUtil.decriptImage(ConstantMediaSize.themePath + SRCS[0] + SUFFIX),
                    BitmapUtil.decriptImage(ConstantMediaSize.themePath + SRCS[1] + SUFFIX),
                    BitmapUtil.decriptImage(ConstantMediaSize.themePath + SRCS[2] + SUFFIX)
            );
        }
        return null;
    }
}