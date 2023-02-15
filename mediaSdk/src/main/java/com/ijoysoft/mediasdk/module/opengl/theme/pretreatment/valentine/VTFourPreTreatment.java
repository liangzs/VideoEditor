package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.valentine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

public class VTFourPreTreatment extends BasePreTreatment {
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
        if (bitmap == null) {
            return null;
        }
        int num = FRAME_BORDER;
        int bitwidth = bitmap.getWidth();
        int bitheight = bitmap.getHeight();
        String textRes;
        switch (pretreatConfig.getIndex() % 5) {
            case 0:
                textRes = "/vt_four_theme1_1";
                break;
            case 1:
                textRes = "/vt_four_theme2_1";
                break;
            case 2:
                textRes = "/vt_four_theme3_1";
                break;
            case 3:
                textRes = "/vt_four_theme4_1";
                break;
            case 4:
                textRes = "/vt_four_theme5_1";
                break;
            default:
                textRes = "/vt_four_theme1_1";
                break;
        }


        Bitmap bit2 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + textRes);
        int textSize = 10;
        switch (pretreatConfig.getRatioType()) {
            case _1_1:
            case _4_3:
            case _16_9:
                textSize = 15;
                break;
        }
        int bit2RealWidth = bitwidth - textSize * num;
        int bit2RealHeith = bit2RealWidth * bit2.getHeight() / bit2.getWidth();
        int width = bitwidth + num;
        int height = bitheight + 2 * num + bit2RealHeith;
        // 背图
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        //画文字
        int offsetXTwo = 0, offsetyTwo = 0;
        int offsetX = 0, offsety = 0;
        switch (pretreatConfig.getIndex() % 5) {
            case 0:
            case 2:
                offsetX = num / 2;
                offsety = num / 2;
                offsetXTwo = (width - bit2RealWidth) / 2;
                offsetyTwo = bitheight + num * 5 / 4;
                break;
            case 1:
            case 3:
            case 4:
                offsetX = num / 2;
                offsety = height - bitheight - num / 2;
                offsetXTwo = (width - bit2RealWidth) / 2;
                offsetyTwo = num;
                break;
        }
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);
        canvas.drawBitmap(bitmap, offsetX, offsety, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        // 画正方形的
        canvas.drawRect(0, 0, width, height, paint);
        Bitmap resizeBmp = Bitmap.createScaledBitmap(bit2, bit2RealWidth, bit2RealHeith, true);
        canvas.drawBitmap(resizeBmp, offsetXTwo, offsetyTwo, null);
        bitmap.recycle();
        bit2.recycle();
        resizeBmp.recycle();
        return ConcatPhoto(newBitmap);
    }

    /**
     * 拼接图片
     *
     * @param bit1
     */
    private Bitmap ConcatPhoto(Bitmap bit1) {
        Bitmap bit2 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_four_pre_bg");
        int width = bit1.getWidth();
        int height = bit1.getHeight() + bit2.getHeight() / 2;
        //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bit1, 0, bit2.getHeight() / 2, null);
        canvas.drawBitmap(bit2, (width - bit2.getWidth()) / 2, 0, null);
        //将canvas传递进去并设置其边框
        bit1.recycle();
        bit2.recycle();
        return bitmap;
    }

}
