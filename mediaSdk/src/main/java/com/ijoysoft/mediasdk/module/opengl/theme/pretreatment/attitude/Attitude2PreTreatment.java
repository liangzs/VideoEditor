package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.attitude;

import android.content.res.Resources;
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
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

import java.util.ArrayList;
import java.util.List;

public class Attitude2PreTreatment extends DownloadedPreTreatmentTemplate {

    @Override
    public Bitmap addFrame(PretreatConfig pretreatConfig) {
        if(pretreatConfig.isVideo()){
            return null;
        }
        long start = System.currentTimeMillis();
        Bitmap bitmap = scaleBitmap(pretreatConfig);
        int num = FRAME_BORDER;
        int width = bitmap.getWidth() + num;
        int height = bitmap.getHeight() + num;
        LogUtils.i("test", "width:" + width + ",height:" + height + ",");
        if (pretreatConfig.getRotation() == 90 || pretreatConfig.getRotation() == 270) {
            height = bitmap.getWidth() + num;
            width = bitmap.getHeight() + num;
        }
        // 背图
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        // 生成白色的
        paint.setColor(Color.WHITE);
        if (pretreatConfig.getRotation() != 0) {
            Matrix m = new Matrix();
            m.postRotate(pretreatConfig.getRotation());
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        }
        canvas.drawBitmap(bitmap, num / 2, num / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        // 画正方形的
        canvas.drawRoundRect(0, 0, width, height,10f, 10f, paint);
        bitmap.recycle();
        //将频繁打印的日志设置为-verbose级别
        Log.v("addBitmapFrame:", "time:" + (System.currentTimeMillis() - start));
        if (pretreatConfig.getIndex() % 2 == 0) {
            return addFrameExtra(newBitmap, pretreatConfig.getIndex());
        }
        if (pretreatConfig.getIndex() % 6 == 1) {
            return addEmoji(newBitmap);
        }
        return newBitmap;
    }


    public Bitmap addFrameExtra(Bitmap bit, int index) {
        Bitmap bit2;
        index = (index % 6) / 2;
        if (index == 0) {
            bit2 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude2_top1" + SUFFIX);
        } else if (index == 1) {
            bit2 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude2_top3" + SUFFIX);
        } else {
            bit2 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude2_top5" + SUFFIX);
        }
        int width = bit.getWidth();
        int bit2RealWidth = (width * 8) / 10;
        int bit2RealHeight = bit2.getHeight() * bit2RealWidth / bit2.getWidth();
        int height = bit.getHeight() + bit2RealHeight / 2;
        //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        Canvas canvas = new Canvas(bitmap);
        Bitmap resizeBmp = Bitmap.createScaledBitmap(bit2, bit2RealWidth, bit2RealHeight, true);
        canvas.drawBitmap(bit, 0, bit2RealHeight / 2f, null);
        canvas.drawBitmap(resizeBmp, (width - bit2RealWidth) / 2f, 0, null);
        //将canvas传递进去并设置其边框
        bit.recycle();
        bit2.recycle();
        return bitmap;
    }

    public Bitmap addEmoji(Bitmap bit) {
        Bitmap bit2 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude2_top2" + SUFFIX);
        int width = bit.getWidth() + 2 * FRAME_BORDER;
        int bit2Realwidth = width / 3;
        int height = bit2.getHeight() * bit2Realwidth / bit2.getWidth();
        //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度emoji缩放后，实际的高度
        Bitmap bitmap1 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        Canvas canvas = new Canvas(bitmap1);
        Bitmap scaledBit2 = Bitmap.createScaledBitmap(bit2, bit2Realwidth, height, true);
        canvas.drawBitmap(scaledBit2, width - bit2Realwidth, 0, null);
        bit2.recycle();
        scaledBit2.recycle();

        Bitmap bitmap = Bitmap.createBitmap(width, bit.getHeight() + height, Bitmap.Config.ARGB_4444);
        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        Canvas canvas2 = new Canvas(bitmap);
        canvas2.drawBitmap(bit, 0, height / 2f, null);
        canvas2.drawBitmap(bitmap1, 0, 0, null);
        bitmap1.recycle();
        return bitmap;
    }



    private static final String[][] SRCS = new String[][]{
            {"/attitude2_center1"},
            {"/attitude2_center2"},
            {"/attitude2_center3"},
            {"/attitude2_center4"},
            {"/attitude2_center5"},
            {"/attitude2_center6"},
    };


    @Override
    protected String[][] getSources() {
        return SRCS;
    }

}
