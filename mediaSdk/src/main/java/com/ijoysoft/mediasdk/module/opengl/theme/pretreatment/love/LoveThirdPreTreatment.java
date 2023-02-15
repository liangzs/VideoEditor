package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.love;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class LoveThirdPreTreatment extends BasePreTreatment {
    private final int MIPCOUNT = 7;

    @Override
    public int getMipmapsCount() {
        return MIPCOUNT;
    }

    @Override
    public int afterPreRotation() {
        return 0;
    }

    @Override
    public Bitmap addFrame(PretreatConfig pretreatConfig) {
        // 背图
        if(pretreatConfig.isVideo()){
            return null;
        }
        Bitmap bitmapBg = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "love_third_pre1");
        Bitmap newBitmap = Bitmap.createBitmap(bitmapBg.getWidth(), bitmapBg.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Bitmap bitmap = BitmapUtil.scaleRatioByMatrix(BitmapFactory.decodeFile(pretreatConfig.getPath()),
                bitmapBg.getWidth(), bitmapBg.getHeight());
        canvas.drawBitmap(bitmapBg, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        float offsetX = (bitmapBg.getWidth() - bitmap.getWidth()) / 2f;
        float offsetY = (bitmapBg.getHeight() - bitmap.getHeight()) / 2f;
        offsetX = offsetX < 0 ? 0 : offsetX;
        offsetY = offsetY < 0 ? 0 : offsetY;
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
        bitmap.recycle();
        bitmapBg.recycle();
        return newBitmap;
    }


    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
        Resources resources = MediaSdk.getInstance().getResouce();
        switch (index % MIPCOUNT) {
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "love_third_theme4_1"));
                break;
            case 5:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "love_third_theme6_1"));
                break;
        }
        return list;
    }

    @Override
    public int createDuration() {
        return THEME_TWO_DURATION;
    }
}
