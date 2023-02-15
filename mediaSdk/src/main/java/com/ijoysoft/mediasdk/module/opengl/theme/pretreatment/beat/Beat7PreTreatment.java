package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.beat;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class Beat7PreTreatment extends BaseTimePreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        List<Bitmap> list = new ArrayList<>();
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_seven_theme1"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_seven_theme2"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_seven_theme3"));
        return list;
    }

    @Override
    public long dealDuration(int index) {
        return super.dealDuration(index);
    }
}
