package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;

import java.util.ArrayList;
import java.util.List;

public class Common30PreTreatment extends Common15PreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        List<Bitmap> list = new ArrayList<>();
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common30_particle1"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common30_particle2"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common30_particle3"));
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
                {"/common30_gif1", "/common30_gif2", "/common30_gif3"}
        };
    }


}
