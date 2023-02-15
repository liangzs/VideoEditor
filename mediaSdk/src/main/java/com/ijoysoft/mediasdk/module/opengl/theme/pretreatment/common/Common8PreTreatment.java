package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hayring
 * @date 2021/12/30  19:22
 */
public class Common8PreTreatment extends BaseTimePreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        return null;
    }

    @Override
    public int getMipmapsCount() {
        return 5;
    }

    @Override
    public long dealDuration(int index) {
        return 2240;
    }

}
