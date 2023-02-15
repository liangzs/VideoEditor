package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.valentine;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class VTElevenPreTreatment extends BasePreTreatment {

    private final int MIPCOUNT = 6;

    @Override
    public int getMipmapsCount() {
        return MIPCOUNT;
    }
    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
        Resources resources = MediaSdk.getInstance().getResouce();
//        index = 5;
        switch (index % MIPCOUNT) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme1_4" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme1_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme1_3" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme2_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme2_2" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme3_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme3_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme3_3" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme4_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme2_2" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme5_1" + SUFFIX));
                break;
            case 5:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme6_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme3_2" + SUFFIX));
                break;
        }
        switch (index % 3) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme_border1_1" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme_border2_1" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme_border3_1" + SUFFIX));
                break;
        }
        return list;
    }

    @Override
    public List<Bitmap> getRatio169(int index) {
        List<Bitmap> list = new ArrayList<>();
        Resources resources = MediaSdk.getInstance().getResouce();
//        index = 5;
        switch (index % MIPCOUNT) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme169_1_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme1_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme1_3" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme169_2_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme2_2" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme169_3_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme3_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme3_3" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme169_4_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme2_2" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme169_5_1" + SUFFIX));
                break;
            case 5:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme169_6_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme3_2" + SUFFIX));
                break;
        }
        switch (index % 3) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme_border1_1" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme_border2_1" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eleven_theme_border3_1" + SUFFIX));
                break;
        }
        return list;
    }
}
