package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.wedding;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

/**
 * @ author       lfj
 * @ date         2020/10/24
 * @ description
 */
public class WeddingNinePreTreatment extends BasePreTreatment {
    @Override
    public int afterPreRotation() {
        return 0;
    }

    @Override
    public Bitmap addFrame(PretreatConfig pretreatConfig) {
        if(pretreatConfig.isVideo()){
            return null;
        }
        Bitmap bitmap = fitRatioBitmap( pretreatConfig);
        int num = FRAME_BORDER / 4;
        int width = bitmap.getWidth() + num;
        int height = bitmap.getHeight() + num;
        // 背图
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        // 生成白色的
        int color = Color.BLACK;
        paint.setColor(color);
        canvas.drawBitmap(bitmap, num / 2, num / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        // 画正方形的
        canvas.drawRect(0, 0, width, height, paint);
        bitmap.recycle();
        return newBitmap;
    }

    private final int MIPCOUNT = 4;

    @Override
    public int getMipmapsCount() {
        return MIPCOUNT;
    }

    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
        Resources resources = MediaSdk.getInstance().getResouce();
//        index = 3;
        switch (index % MIPCOUNT) {
            case 0:
                if (ConstantMediaSize.ratioType == RatioType._16_9 || ConstantMediaSize.ratioType == RatioType._4_3) {
                    list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wed_nine_theme169_1_1" + SUFFIX));
                } else {
                    list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wed_nine_theme1_1" + SUFFIX));
                }
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wed_nine_theme1_2" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wed_nine_theme2_1" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wed_nine_theme3_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wed_nine_theme1_2" + SUFFIX));
                break;
            case 3:
                if (ConstantMediaSize.ratioType == RatioType._16_9 || ConstantMediaSize.ratioType == RatioType._4_3) {
                    list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wed_nine_theme169_1_1" + SUFFIX));
                } else {
                    list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wed_nine_theme1_1" + SUFFIX));
                }
                break;
        }
        return list;
    }

}