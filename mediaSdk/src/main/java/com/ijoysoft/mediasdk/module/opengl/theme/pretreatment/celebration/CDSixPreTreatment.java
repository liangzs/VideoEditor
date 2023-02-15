package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.celebration;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据图片的比例进行加边框，而不是根据显示区域的比例进行选择
 */
public class CDSixPreTreatment extends BasePreTreatment {
    @Override
    public int afterPreRotation() {
        return 0;
    }

    @Override
    public Bitmap addFrame(PretreatConfig pretreatConfig) {
        if(pretreatConfig.isVideo()){
            return null;
        }
        Bitmap bitmap = fitRatioBitmap(pretreatConfig);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // 背图
        Bitmap newBitmap = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);

        float ratio = width / height;
        String res;
        if (ratio <= 0.75f) {
            res = ConstantMediaSize.themePath + "/cd_six_border916" + SUFFIX;
        } else if (ratio > 1.33f) {
            res = ConstantMediaSize.themePath + "/cd_six_border169" + SUFFIX;
        } else {
            res = ConstantMediaSize.themePath + "/cd_six_border11" + SUFFIX;
        }
        Bitmap resizeBmp = Bitmap.createScaledBitmap(BitmapUtil.decriptImage(res), width, height, true);

        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawBitmap(resizeBmp, 0, 0, null);
        resizeBmp.recycle();
        bitmap.recycle();
        return newBitmap;
    }

    @Override
    public boolean isNeedFrame() {
        return true;
    }

    @Override
    public List<Bitmap> getRatio916(int index) {
//        index = 2;
        List<Bitmap> list = new ArrayList<>();
        switch (index % 5) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_six_theme1_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_six_theme1_2" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_six_theme2_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_six_particle1" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_six_theme3_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_six_particle2" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_six_theme4_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_six_theme4_2" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_six_theme5_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_six_theme5_2" + SUFFIX));
                break;
        }
        return list;
    }
}
