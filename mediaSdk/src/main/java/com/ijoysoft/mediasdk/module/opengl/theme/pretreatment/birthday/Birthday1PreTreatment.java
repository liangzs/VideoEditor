package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.birthday;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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

public class Birthday1PreTreatment extends BasePreTreatment {
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
        int num = FRAME_BORDER;
        int width = bitmap.getWidth() + num;
        int height = bitmap.getHeight() + num;
        // 背图
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        // 生成白色的
        paint.setColor(Color.WHITE);
        canvas.drawBitmap(bitmap, num / 2, num / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        // 画正方形的
        canvas.drawRect(0, 0, width, height, paint);
        bitmap.recycle();
        return newBitmap;
    }


    private final int MIPCOUNT = 6;

    @Override
    public int getMipmapsCount() {
        return MIPCOUNT;
    }

    @Override
    public List<Bitmap> getRatio916(int index) {
//        index = 5;
        List<Bitmap> list = new ArrayList<>();
        Resources resources = MediaSdk.getInstance().getResouce();
        switch (index % MIPCOUNT) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_one_theme1_1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_one_theme1_2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_one_theme1_3"));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_one_theme2_1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_one_theme2_2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_one_theme2_3"));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_one_theme3_1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_one_theme3_2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_one_theme3_3"));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_one_theme4_1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_one_theme4_2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_one_theme4_3"));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_one_theme5_2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_one_theme5_1"));
                break;
            case 5:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_one_theme6_1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_one_theme6_2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_one_theme6_3"));
                break;
        }
        return list;
    }


}
