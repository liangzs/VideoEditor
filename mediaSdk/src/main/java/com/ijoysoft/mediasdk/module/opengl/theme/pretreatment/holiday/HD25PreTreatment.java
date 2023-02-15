package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.SUFFIX;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HD25PreTreatment extends BaseTimePreTreatment {


    @Override
    public int getMipmapsCount() {
        return 7;
    }

    @Override
    public List<Bitmap> createMimapBitmaps() {
        return Collections.singletonList(
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday25_particle")
        );
    }



    @Override
    public long dealDuration(int index) {
        switch (index % getMipmapsCount()) {
            case 0:
                return 1000;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return 800;
            case 6:
                return 1800;
        }
        return DURATION;
    }

    final String[][] dynamicMipmapBitmapSources = new String[][]{{"/holiday25_dynamic"}};

    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return dynamicMipmapBitmapSources;
    }
}
