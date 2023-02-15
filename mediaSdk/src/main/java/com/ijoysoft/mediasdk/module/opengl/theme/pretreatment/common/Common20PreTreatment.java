package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.SUFFIX;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.Arrays;
import java.util.List;

/**
 * @author hayring
 * @date 2021/12/30  19:22
 */
public class Common20PreTreatment extends BaseTimePreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {

        return Arrays.asList(
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + SRCS[0] + SUFFIX),
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + SRCS[1] + SUFFIX),
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + SRCS[2] + SUFFIX),
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + SRCS[3] + SUFFIX),
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + SRCS[1] + SUFFIX),
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + SRCS[4] + SUFFIX),
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + SRCS[5] + SUFFIX),
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + SRCS[6] + SUFFIX),
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + SRCS[7] + SUFFIX),
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + SRCS[8] + SUFFIX)
        );
    }

    private static final String[] SRCS = new String[]{
            "/common20_crown", "/common20_flag", "/common20_left_balloon", "/commo20_right_balloon", "/common20_diamond"
            , "/common20_particle1", "/common20_particle2", "/common20_particle3", "/common20_particle4"
    };

    @Override
    public int getMipmapsCount() {
        return 5;
    }

    @Override
    public long dealDuration(int index) {
        return 2240;
    }

}
