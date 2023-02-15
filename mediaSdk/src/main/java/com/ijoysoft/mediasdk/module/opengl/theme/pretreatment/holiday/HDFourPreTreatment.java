package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

/**
 * 加边框
 */
public class HDFourPreTreatment extends BasePreTreatment {
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
        Paint paint = new Paint();
        String res = ConstantMediaSize.themePath + "/hd_four_theme11_pre" + SUFFIX;
        switch (ConstantMediaSize.ratioType) {
            case _1_1:
                res = ConstantMediaSize.themePath + "/hd_four_theme11_pre" + SUFFIX;
                break;
            case _4_3:
            case _16_9:
                res = ConstantMediaSize.themePath + "/hd_four_theme169_pre" + SUFFIX;
                break;
            case _3_4:
            case _9_16:
                res = ConstantMediaSize.themePath + "/hd_four_theme916_pre" + SUFFIX;
                break;
        }
        Bitmap bitmapBg = BitmapUtil.decriptImage(res);
        Bitmap resizeBmp = Bitmap.createScaledBitmap(bitmapBg, width, height, true);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        canvas.drawBitmap(resizeBmp, 0, 0, paint);
        bitmapBg.recycle();
        resizeBmp.recycle();
        bitmap.recycle();
        res = ConstantMediaSize.themePath + "/hd_four_theme11_border" + SUFFIX;
        switch (ConstantMediaSize.ratioType) {
            case _1_1:
                res = ConstantMediaSize.themePath + "/hd_four_theme11_border" + SUFFIX;
                break;
            case _4_3:
            case _16_9:
                res = ConstantMediaSize.themePath + "/hd_four_theme169_border" + SUFFIX;
                break;
            case _3_4:
            case _9_16:
                res = ConstantMediaSize.themePath + "/hd_four_theme916_border" + SUFFIX;
                break;
        }
        Bitmap newBitmap1 = Bitmap.createBitmap(
                width + FRAME_BORDER, height + FRAME_BORDER, Bitmap.Config.ARGB_4444);
        Canvas canvas1 = new Canvas(newBitmap1);
        resizeBmp = Bitmap.createScaledBitmap(BitmapUtil.decriptImage(res), width + FRAME_BORDER, height + FRAME_BORDER, true);
        canvas1.drawBitmap(resizeBmp, 0, 0, null);
        canvas1.drawBitmap(newBitmap, FRAME_BORDER / 2, FRAME_BORDER / 2, null);
        resizeBmp.recycle();
        newBitmap.recycle();
        return newBitmap1;
    }

    @Override
    public boolean isNeedFrame() {
        return true;
    }

    private final int MIPCOUNT = 6;

    @Override
    public int getMipmapsCount() {
        return MIPCOUNT;
    }

    @Override
    public List<Bitmap> getRatio916(int index) {
//        index = 1;
        List<Bitmap> list = new ArrayList<>();
        switch (index % MIPCOUNT) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_four_theme1_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_four_theme1_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_four_theme1_3" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_four_theme2_1" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_four_theme3_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_four_theme3_2" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_four_theme4_1" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_four_theme5_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_four_theme5_2" + SUFFIX));
                break;
            case 5:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_four_theme6_1" + SUFFIX));
                break;
        }
        return list;
    }
}
