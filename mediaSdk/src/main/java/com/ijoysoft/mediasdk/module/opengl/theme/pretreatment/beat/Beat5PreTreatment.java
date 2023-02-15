package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.beat;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class Beat5PreTreatment extends BasePreTreatment {

    private final int MIPCOUNT = 6;

    @Override
    public int getMipmapsCount() {
        return MIPCOUNT;
    }

    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
        Resources resources = MediaSdk.getInstance().getResouce();
        switch (index % MIPCOUNT) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_five_theme1_1"));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_five_theme2_1"));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_five_theme3_1"));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_five_theme4_1"));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_five_theme5_1"));
                break;
            case 5:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_five_theme5_1"));
                break;
        }
        return list;
    }

}
