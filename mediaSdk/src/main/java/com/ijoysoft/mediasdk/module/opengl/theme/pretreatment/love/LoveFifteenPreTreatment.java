package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.love;

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

public class LoveFifteenPreTreatment extends BasePreTreatment {

    private final int MIPCOUNT = 7;

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
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_fifteen_border"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_fifteen_theme1_1"));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_fifteen_theme2_1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_fifteen_theme2_2"));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_fifteen_border"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_fifteen_theme3_1"));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_fifteen_border"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_fifteen_theme2_1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_fifteen_theme2_2"));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_fifteen_theme5_1"));
                break;
            case 5:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_fifteen_border"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_fifteen_theme3_1"));
                break;
            case 6:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_fifteen_border"));
                break;
        }
        return list;
    }

    @Override
    public int createDuration() {
        return THEME_TWO_DURATION;
    }
}
