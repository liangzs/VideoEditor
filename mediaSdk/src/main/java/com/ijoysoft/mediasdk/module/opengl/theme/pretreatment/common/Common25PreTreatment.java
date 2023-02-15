package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.SUFFIX;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author hayring
 * @date 2022/1/18  16:32
 */
public class Common25PreTreatment extends Common15PreTreatment{
    @Override
    public long dealDuration(int index) {
        return 2*super.dealDuration(index);
    }


    @Override
    public List<Bitmap> createMimapBitmaps() {
            return Arrays.asList(
                    BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common25_emoticon1" + SUFFIX),
                    BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common25_emoticon2" + SUFFIX),
                    BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common25_emoticon3" + SUFFIX),
                    BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common25_emoticon3" + SUFFIX),
                    BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common25_emoticon2" + SUFFIX),
                    BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common25_emoticon1" + SUFFIX)
            );
    }

    @Override
    public List<Bitmap> getMimapBitmaps(RatioType ratioType, int index) {
        if (index == 0) {
            return Collections.singletonList(
                    BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common25_title" + SUFFIX)
            );
        }
        return null;
    }
}
