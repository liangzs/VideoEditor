package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.birthday;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

/**
 * 加填一个半透明层
 */
public class BDSevenPreTreatment extends BasePreTreatment {
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
        long start = System.currentTimeMillis();
        int num = FRAME_BORDER * 2;
        int width = bitmap.getWidth() + num;
        int height = (int) (bitmap.getHeight() + num);
        // 背图
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        // 生成白色的
        paint.setColor(Color.WHITE);
        canvas.drawBitmap(bitmap, num / 2, num / 2, paint);
        bitmap.recycle();
        bitmap = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_seven_pre_bg" + SUFFIX);
        Bitmap resizeBmp = Bitmap.createScaledBitmap(bitmap, width, height, true);
        canvas.drawBitmap(resizeBmp, 0, 0, paint);
        bitmap.recycle();
        resizeBmp.recycle();
        Log.i("addBitmapFrame:", "time:" + (System.currentTimeMillis() - start));
        return ConcatPhoto(newBitmap);
    }


    /**
     * 拼接图片
     *
     * @param bit1
     */
    private Bitmap ConcatPhoto(Bitmap bit1) {
        Bitmap bit2 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_seven_pre_bg1" + SUFFIX);
        int width = bit1.getWidth();
        int bit2RealHeith = (int) (FRAME_BORDER * 4f);
        int bit2RealWidth = bit2RealHeith * bit2.getWidth() / bit2.getHeight();
        int height = bit1.getHeight() + bit2RealHeith;
        //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        Canvas canvas = new Canvas(bitmap);
        Bitmap resizeBmp = Bitmap.createScaledBitmap(bit2, bit2RealWidth, bit2RealHeith, true);
        canvas.drawBitmap(bit1, 0, 2 * FRAME_BORDER, null);
        canvas.drawBitmap(resizeBmp, (width - bit2RealWidth) / 2, 0, null);
        //将canvas传递进去并设置其边框
        bit1.recycle();
        bit2.recycle();
        return bitmap;
    }

    private final int MIPCOUNT = 5;

    @Override
    public int getMipmapsCount() {
        return MIPCOUNT;
    }

    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
        Resources resources = MediaSdk.getInstance().getResouce();
//        index = 4;
        switch (index % MIPCOUNT) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_seven_theme1_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_seven_theme1_2" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_seven_theme2_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_seven_theme2_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_seven_particle1" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_seven_theme3_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_seven_theme3_2" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_seven_theme4_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_seven_theme4_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_seven_theme4_3" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_seven_theme5_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_seven_theme5_2" + SUFFIX));
                break;
        }
        return list;
    }
}
