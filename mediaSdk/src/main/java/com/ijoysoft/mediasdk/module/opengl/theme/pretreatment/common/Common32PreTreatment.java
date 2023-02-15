package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Common32PreTreatment extends Common14PreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        return Collections.singletonList(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common32_particle1"));
    }


    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return new String[][]{
                {"/common32_gif1"}, {}, {"/common32_gif1"}, {"/common32_gif3"}, {"/common32_gif2"}, {"/common32_gif3"}
        };
    }


}
