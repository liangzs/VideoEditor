package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hayring
 * @date 2021/12/30  19:22
 */
public class Common7PreTreatment extends BaseTimePreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        List<Bitmap> list = new ArrayList<>();
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common7_particle1"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common7_particle2"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common7_particle3"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common7_particle4"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common7_particle5"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common7_particle6"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common7_particle7"));
        return list;
    }

    @Override
    public int getMipmapsCount() {
        return 11;
    }

    @Override
    public long dealDuration(int index) {
        index %=11;
        switch (index) {
            case 2:
                return 1960;
            case 7:
            case 8:
            case 9:
                return 440;
            case 10:
                return 880;
            default:
                return 840;
        }
    }

}
