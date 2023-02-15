package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.love;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class LoveSixteenPreTreatment extends BasePreTreatment {
    private final int MIPCOUNT = 5;

    @Override
    public Bitmap addFrame(PretreatConfig pretreatConfig) {
        if(pretreatConfig.isVideo()){
            return null;
        }
        int offsetBg1 = 0;
        int offsetBg2 = 0;
        int top = 0;
        Bitmap bitmapBg1;
        Bitmap bitmapBg2;
        switch (pretreatConfig.getIndex() % MIPCOUNT) {
            case 0:
                offsetBg1 = 0;
                offsetBg2 = 10;
                bitmapBg1 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love16_1_1");
                bitmapBg2 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love16_1_2a");
                break;
            case 1:
                offsetBg1 = 40;
                offsetBg2 = -10;
                top = 30;
                bitmapBg1 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love16_2_1");
                bitmapBg2 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love16_2_2");
//                bitmapBg2 = BitmapUtil.scaleImage(bitmapBg2, bitmapBg1.getWidth(), bitmapBg1.getHeight(), false);
                break;
            case 2:
                offsetBg1 = 0;
                offsetBg2 = 10;
                bitmapBg1 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love16_3_1");
                bitmapBg2 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love16_3_2");
                break;
            case 3:
                offsetBg1 = 0;
                offsetBg2 = 0;
                bitmapBg1 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love16_4_1");
                bitmapBg2 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love16_4_2");
                bitmapBg2 = BitmapUtil.scaleImage(bitmapBg2, bitmapBg1.getWidth() + 50, bitmapBg1.getHeight() + 50, false);
                break;
            case 4:
                offsetBg1 = 10;
                offsetBg2 = 0;
                bitmapBg1 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love16_5_1");
                bitmapBg2 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love16_5_2");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + pretreatConfig.getIndex() % MIPCOUNT);
        }

        int width = Math.max(bitmapBg1.getWidth(), bitmapBg2.getWidth());
        int height = Math.max(bitmapBg1.getHeight(), bitmapBg2.getHeight());
        Bitmap newBitmap = Bitmap.createBitmap(width + Math.max(offsetBg1, offsetBg2), height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Bitmap bitmap = BitmapUtil.scaleRatioByMatrix(BitmapFactory.decodeFile(pretreatConfig.getPath()),
                width, height);
        canvas.drawBitmap(bitmapBg1, offsetBg1 / 2, top, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(bitmap, offsetBg1 / 2, top, paint);
        bitmap.recycle();
        canvas.drawBitmap(bitmapBg2, offsetBg2 / 2, 0, null);
        bitmapBg1.recycle();
        bitmapBg2.recycle();
        return newBitmap;
    }


    @Override
    public int getMipmapsCount() {
        return super.getMipmapsCount();
    }

    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
        Resources resources = MediaSdk.getInstance().getResouce();
//        index = 4;
        switch (index % MIPCOUNT) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
        return list;
    }


    @Override
    public long dealDuration(int index) {
        return THEME_ONE_DURATION;
    }
}
