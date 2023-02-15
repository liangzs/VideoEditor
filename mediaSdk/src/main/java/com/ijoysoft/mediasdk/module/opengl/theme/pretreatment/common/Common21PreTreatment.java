package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.List;

import static com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.LocalPreTreatmentTemplate.END_SUFFIX;

public class Common21PreTreatment extends BaseTimePreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        return null;
    }

    @Override
    public int getMipmapsCount() {
        return 6;
    }

    @Override
    public long dealDuration(int index) {
        return 1640;
    }

//    private static final String[][] GIF = new String[][]{
//            {"/common21_dynamic"}
//    };

    private static final String[][] GIF = new String[][]{
            {R.mipmap.common21_dynamic + END_SUFFIX}
    };

    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return GIF;
    }
}
