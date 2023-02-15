package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common.Common15PreTreatment;

import java.util.ArrayList;
import java.util.List;

public class Holiday16PreTreatment extends Common15PreTreatment {

    @Override
    public int getMipmapsCount() {
        return 30;
    }

    @Override
    public long dealDuration(int index) {
        switch (index % getMipmapsCount()) {
            //左右移动
            case 0:
            case 4:
                return 600;
            case 1:
                return 400;
            case 2:
            case 3:
            case 9:
            case 17:
                return 1200;
            case 10:
            case 11:
                return 2000;
            default:
                return 600;
        }
    }

    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return new String[][]{
                {"/holiday16_gif1"}
        };
    }


}
