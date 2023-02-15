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
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class WeddingOnePreTreatment extends BasePreTreatment {
    @Override
    public int afterPreRotation() {
        return 0;
    }

    @Override
    public Bitmap addFrame(PretreatConfig pretreatConfig) {
        if(pretreatConfig.isVideo()){
            return null;
        }
        Bitmap bitmap = fitAndCutRatioBitmap(pretreatConfig);
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
        return ConcatPhoto(newBitmap);
    }

    /**
     * 拼接图片
     *
     * @param bit1
     */
    private static Bitmap ConcatPhoto(Bitmap bit1) {

        Bitmap bit2 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding_one_border" + ConstantMediaSize.SUFFIX);
        int width = bit1.getWidth();
        int bit2RealHeith = bit2.getHeight() * width / bit2.getWidth();
        int height = bit1.getHeight() + bit2RealHeith;

        float moveY = 0;
        //cut: 充值y轴绘制起始坐标，逻辑上从moveY开始绘制，moveY - 0 这个区间绘制的内容实际上被丢弃， 下方的moveX 同理相反，增加空白区域
        float cut = 0, add = 0;
        //剪切不需要的多于区域，让照片最大化
        if (ConstantMediaSize.ratioType.getRatioValue() > 1 && bit1.getWidth() > bit1.getHeight() /*&& ConstantMediaSize.ratioType.getRatioValue() > bit1.getWidth() / (float) bit1.getHeight()*/) {
            cut = moveY = (float) (-bit2RealHeith * 0.7);
            height += moveY;
            LogUtils.i("BDThreePreTreatment", "ConcatPhoto resize cut: " + cut);
        }
        if (ConstantMediaSize.ratioType.getRatioValue() < 1 /*&& bit1.getWidth() > bit1.getHeight() && ConstantMediaSize.ratioType.getRatioValue() > bit1.getWidth() / (float) bit1.getHeight()*/) {
            add  = 200 * (1f - ConstantMediaSize.ratioType.getRatioValue());
            width += 2 * add;
            LogUtils.i("BDThreePreTreatment", "ConcatPhoto resize add: " + add);
        }

        //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        Canvas canvas = new Canvas(bitmap);
        Bitmap resizeBmp = Bitmap.createScaledBitmap(bit2, width / 2, bit2RealHeith, true);
        canvas.drawBitmap(bit1, add, moveY + bit2RealHeith - FRAME_BORDER, null);
        canvas.drawBitmap(resizeBmp, width / 4, cut, null);
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
        switch (index % 5) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding_one_theme1_1" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding_one_theme3_1" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding_one_theme5_1" + SUFFIX));
                break;
        }
        return list;
    }
}
