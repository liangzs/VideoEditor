package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.valentine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
 * 根据图片的比例进行加边框，而不是根据显示区域的比例进行选择
 */
public class VTSevenPreTreatment extends BasePreTreatment {
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
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);

        //生成不规则图片
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        // 生成白色的
        if(pretreatConfig.getRotation()!=0) {
            Matrix m = new Matrix();
            m.postRotate(pretreatConfig.getRotation());
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        }
        canvas.drawBitmap(bitmap, 0, 0, paint);
        bitmap.recycle();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        float ratio = width / height;
        String res = ConstantMediaSize.themePath + "/vt_seven_pre_bg" + SUFFIX;

        if (ratio > 1.33f) {
            res = ConstantMediaSize.themePath + "/vt_seven_pre_bg169" + SUFFIX;

        }
        Bitmap resizeBmp = Bitmap.createScaledBitmap(BitmapUtil.decriptImage(res), width, height, true);
        canvas.drawBitmap(resizeBmp, 0, 0, paint);
        resizeBmp.recycle();
        switch (pretreatConfig.getIndex() % 2) {
            case 0:
                res = ConstantMediaSize.themePath + "/vt_seven_pre_bg1" + SUFFIX;
                break;
            case 1:
                res = ConstantMediaSize.themePath + "/vt_seven_pre_bg2" + SUFFIX;
                break;
        }
        if (ratio > 1.33f) {
            switch (pretreatConfig.getIndex() % 2) {
                case 0:
                    res = ConstantMediaSize.themePath + "/vt_seven_pre_bg169_1" + SUFFIX;
                    break;
                case 1:
                    res = ConstantMediaSize.themePath + "/vt_seven_pre_bg169_2" + SUFFIX;
                    break;
            }
        }
        resizeBmp = Bitmap.createScaledBitmap(BitmapUtil.decriptImage(res), width, height, true);
        canvas.drawBitmap(resizeBmp, 0, 0, null);
        resizeBmp.recycle();
        bitmap.recycle();
        return newBitmap;
    }


    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
        Resources resources = MediaSdk.getInstance().getResouce();
//        index = 2;
        switch (index % 5) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_seven_theme1_1" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_seven_theme2_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_seven_theme2_2" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_seven_theme3_1" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_seven_theme2_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_seven_theme2_2" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_seven_theme5_1" + SUFFIX));
                break;
        }
        return list;
    }
}
