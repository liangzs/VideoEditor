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

public class Beat1PreTreatment extends BaseTimePreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
        List<Bitmap> list = new ArrayList<>();
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_one_theme2_1"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_one_theme1_1"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_one_theme3_1"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_one_theme4_1"));
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_one_theme5_1"));
        return list;
    }

    @Override
    public long dealDuration(int index) {
        return super.dealDuration(index);
    }
}
