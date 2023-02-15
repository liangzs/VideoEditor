package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.birthday;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class BDSixteenPreTreatment extends BasePreTreatment {
    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
        Resources resources = MediaSdk.getInstance().getResouce();
//        index = 4;
        switch (index % 5) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme1_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme1_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme1_5" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme1_4" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme1_3" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme2_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme2_4" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme2_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme2_3" + SUFFIX));

                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme3_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme3_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme1_5" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme1_4" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme4_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme4_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme4_3" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme2_3" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme2_2" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme5_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme5_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme1_3" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme1_5" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_theme1_4" + SUFFIX));
                break;
        }
        list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_sixteen_shape" + SUFFIX));
        return list;
    }

}
