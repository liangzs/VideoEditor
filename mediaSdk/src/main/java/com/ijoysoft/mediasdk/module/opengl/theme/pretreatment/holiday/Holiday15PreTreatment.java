package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common.Common15PreTreatment;

import java.util.ArrayList;
import java.util.List;

public class Holiday15PreTreatment extends Common15PreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        List<Bitmap> list = new ArrayList<>();
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday15_1"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday15_2"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday15_particle4"));
        return list;
    }

    @Override
    public long dealDuration(int index) {
        switch (index % getMipmapsCount()) {
            //左右移动
            case 6:
                return 1600;
            default:
                return 800;
        }
    }

    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return new String[][]{
                {"/holiday15_gif1"}
        };
    }


}
