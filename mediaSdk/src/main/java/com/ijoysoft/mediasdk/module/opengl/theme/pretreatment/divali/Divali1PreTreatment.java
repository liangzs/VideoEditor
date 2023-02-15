package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.divali;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class Divali1PreTreatment extends BaseTimePreTreatment {
    @Override
    public List<Bitmap> createMimapBitmaps() {
//        List<Bitmap> list = new ArrayList<>();
//        Resources resources = MediaSdk.getInstance().getResouce();
//        list.add(BitmapFactory.decodeResource(resources, R.mipmap.divali_one_theme1));
//        list.add(BitmapFactory.decodeResource(resources, R.mipmap.divali_one_theme2));
//        list.add(BitmapFactory.decodeResource(resources, R.mipmap.divali_one_theme3));
//        list.add(BitmapFactory.decodeResource(resources, R.mipmap.divali_one_theme4));
//        return list;
        return null;
    }

    @Override
    public long dealDuration(int index) {
        return super.dealDuration(index);
    }
}
