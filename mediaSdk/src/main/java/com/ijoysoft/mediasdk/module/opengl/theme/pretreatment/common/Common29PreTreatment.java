package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;

import java.util.ArrayList;
import java.util.List;

public class Common29PreTreatment extends Common6PreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        List<Bitmap> list = new ArrayList<>();
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common29_particle1"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common29_wiget1"));
        return list;
    }


    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return new String[][]{
                {"/common29_gif"},
        };
    }


}
