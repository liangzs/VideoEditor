package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.wedding;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

/**
 * @ author       lfj
 * @ date         2020/10/24
 * @ description
 */
public class WeddingFourPreTreatment extends BasePreTreatment {
    @Override
    public int afterPreRotation() {
        return 0;
    }

    @Override
    public Bitmap addFrame(PretreatConfig pretreatConfig) {
        if(pretreatConfig.isVideo()){
            return null;
        }
        Bitmap bitmap = fitAndCutRatioBitmap( pretreatConfig);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 背图
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        bitmap = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding_four_theme11_broder_1" + SUFFIX);

        Bitmap resizeBmp = Bitmap.createScaledBitmap(bitmap, width, height, true);
        canvas.drawBitmap(resizeBmp, 0, 0, paint);
        bitmap.recycle();
        resizeBmp.recycle();
        return newBitmap;
    }

    private final int MIPCOUNT = 0;

    @Override
    public int getMipmapsCount() {
        return MIPCOUNT;
    }

}
