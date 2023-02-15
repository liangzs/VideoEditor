package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.valentine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class VTTwelvePreTreatment extends BasePreTreatment {
    @Override
    public int afterPreRotation() {
        return 0;
    }
    @Override
    public Bitmap addFrame(PretreatConfig pretreatConfig) {
        if(pretreatConfig.isVideo()){
            return null;
        }
        Bitmap bitmap = scaleBitmap(pretreatConfig);
        float conor = 0;
        switch (pretreatConfig.getIndex() % 5) {
            case 0:
                conor = -3f;
                break;
            case 1:
                conor = 3f;
                break;
            case 2:
                conor = 2f;
                break;
            case 3:
                conor = 3f;
                break;
            case 4:
                conor = -3f;
                break;
        }
        Bitmap bit2 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme_border" + SUFFIX);

        // 背图
        if (pretreatConfig.getRotation() != 0) {
            Matrix m = new Matrix();
            m.postRotate(pretreatConfig.getRotation());
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        }
        int originW = bitmap.getWidth();
        Matrix m = new Matrix();
        m.postRotate(conor);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        bitmap.recycle();
        bitmap = Bitmap.createScaledBitmap(bit2, originW, height, true);
        canvas.drawBitmap(bitmap, (width - originW) / 2, 0, null);
        bitmap.recycle();
        bit2.recycle();
        return newBitmap;
    }

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
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme1_4" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme1_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme1_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme1_3" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme2_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme1_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme1_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme2_1" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme3_1" + SUFFIX));
                if (mRatioType == RatioType._16_9) {
                    list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme169_2_2" + SUFFIX));
                } else {
                    list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme3_2" + SUFFIX));
                }
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme1_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme2_1" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme4_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme1_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme1_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme2_1" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme5_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme1_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme1_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme1_3" + SUFFIX));
                break;
            case 5:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme6_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme1_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme1_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_twele_theme1_3" + SUFFIX));
                break;
        }
        return list;
    }
}
