package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;

import java.util.ArrayList;
import java.util.List;

public class Common31PreTreatment extends Common5PreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
//        List<Bitmap> list = new ArrayList<>();
//        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common31_widget1"));
//        return list;
        return null;
    }


    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return new String[][]{
                {"/common31_gif"}
        };
    }


}
