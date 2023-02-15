package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.module.entity.RatioType;

import java.util.Arrays;
import java.util.List;

import static com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.LocalPreTreatmentTemplate.END_SUFFIX;

public class Common27PreTreatment extends Common10PreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        return Arrays.asList(
                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common27_particle1),
                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common27_particle2),
                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common27_particle3)
        );
    }

    @Override
    public int getMipmapsCount() {
        return 5;
    }


    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return new String[][]{
                {R.mipmap.common27_gif + END_SUFFIX}
        };
    }


}
