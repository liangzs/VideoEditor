package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.List;

/**
 * @author hayring
 * @date 2021/12/30  19:22
 */
public class Common6PreTreatment extends BaseTimePreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        return null;
    }

    @Override
    public int getMipmapsCount() {
        return 9;
    }

    @Override
    public long dealDuration(int index) {
        if (index%9 == 0) {
            return 3400;
        }
        return 800;
    }


}
