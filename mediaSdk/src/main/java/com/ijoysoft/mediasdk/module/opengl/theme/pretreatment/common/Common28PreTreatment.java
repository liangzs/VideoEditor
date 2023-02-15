package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.LocalPreTreatmentTemplate.END_SUFFIX;

public class Common28PreTreatment extends Common11PreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        return Arrays.asList(
                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common28_particle1),
                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common28_particle2),
                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common28_particle3),
                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common28_particle4),
                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common28_particle5),
                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common28_particle6)
        );
    }

    @Override
    public long dealDuration(int index) {
        switch (index % getMipmapsCount()) {
            case 6:
                return 1800;
            default:
                return 1000;
        }
    }


    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return new String[][]{
                {R.mipmap.common28_gif + END_SUFFIX}
        };
    }


}
