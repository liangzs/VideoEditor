package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.SUFFIX;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author hayring
 * @date 2021/12/30  19:22
 */
public class Common22PreTreatment extends Common8PreTreatment {


    @Override
    public List<Bitmap> createMimapBitmaps() {
        return Arrays.asList(
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common22_star1" + SUFFIX),
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common22_star2" + SUFFIX),
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common22_star3" + SUFFIX),
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common22_particle1" + SUFFIX),
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common22_particle2" + SUFFIX),
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common22_particle3" + SUFFIX),
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common22_title" + SUFFIX)

        );
    }

    @Override
    public int getMipmapsCount() {
        return 4;
    }


    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {

        return null;
    }
}
