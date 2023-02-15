package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.birthday;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class BDThreePreTreatment extends BasePreTreatment {
    @Override
    public int afterPreRotation() {
        return 0;
    }

    @Override
    public Bitmap addFrame(PretreatConfig pretreatConfig) {
        if(pretreatConfig.isVideo()){
            return null;
        }
        long start = System.currentTimeMillis();
        Bitmap bitmap = fitAndCutRatioBitmap(pretreatConfig);
        int num = FRAME_BORDER * 2;
        int width = bitmap.getWidth() + num;
        int height = bitmap.getHeight() + num;
        int conor = 5;
        switch (pretreatConfig.getIndex() % 5) {
            case 0:
            case 1:
                conor = 6;
                break;
            case 2:
            case 3:
                conor = -3;
                break;
            case 4:
                conor = -6;
                break;
            default:
                break;
        }
        // 背图
//        float z = (float) Math.sqrt(width * width + height * height);
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        // 生成白色的
        canvas.drawBitmap(bitmap, num / 2, num / 2, paint);
        bitmap.recycle();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        bitmap = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_three_border" + SUFFIX);
        Bitmap resizeBmp = Bitmap.createScaledBitmap(bitmap, width, height, true);
        canvas.drawBitmap(resizeBmp, 0, 0, paint);
        resizeBmp.recycle();
        bitmap.recycle();
        Matrix m = new Matrix();
        m.postRotate(conor);
        bitmap = Bitmap.createBitmap(newBitmap, 0, 0, newBitmap.getWidth(), newBitmap.getHeight(), m, true);
        newBitmap.recycle();
        Log.i("addBitmapFrame:", "time:" + (System.currentTimeMillis() - start)
                + ",W:" + width + ",H:" + height + ",resizeBmp.w:" + resizeBmp.getWidth() + ",resizeBmp.h:" + resizeBmp.getHeight() +
                ",nw:" + bitmap.getWidth() + ",nh:" + bitmap.getHeight());

        return ConcatPhoto(bitmap, pretreatConfig.getIndex());
    }


    /**
     * 拼接图片
     *
     * @param bit1
     */
    /**
     * 拼接图片
     *
     * @param bit1
     */
    private Bitmap ConcatPhoto(Bitmap bit1, int index) {
        Bitmap bit2 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_three_head" + SUFFIX);
        int width = bit1.getWidth();
        int bit2RealHeith = bit2.getHeight() * width / bit2.getWidth();
        int height = bit1.getHeight() + bit2RealHeith;
        //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        float moveX = 0, moveY = 0;
        //cut: 充值y轴绘制起始坐标，逻辑上从moveY开始绘制，moveY - 0 这个区间绘制的内容实际上被丢弃， 下方的moveX 同理相反，增加空白区域
        float cut = 0, add = 0;
        //剪切不需要的多于区域，让照片最大化
        if (ConstantMediaSize.ratioType.getRatioValue() > 1 && bit1.getWidth() > bit1.getHeight() /*&& ConstantMediaSize.ratioType.getRatioValue() > bit1.getWidth() / (float) bit1.getHeight()*/) {
            cut = moveY = -200 * (ConstantMediaSize.ratioType.getRatioValue() - 1f);
            height += 2* moveY;
            LogUtils.i("BDThreePreTreatment", "ConcatPhoto resize cut: " + cut);
        }
        if (ConstantMediaSize.ratioType.getRatioValue() < 1 /*&& bit1.getWidth() > bit1.getHeight() && ConstantMediaSize.ratioType.getRatioValue() > bit1.getWidth() / (float) bit1.getHeight()*/) {
            add = moveX = 200 * (1f - ConstantMediaSize.ratioType.getRatioValue());
            width += 2 * moveX;
            LogUtils.i("BDThreePreTreatment", "ConcatPhoto resize add: " + add);
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        int num = FRAME_BORDER * 2;
        switch (index % 5) {
            case 0:
            case 1:
//                conor = 6;
//                moveX = 0;
                moveY += bit2RealHeith * 0.6f;
                break;
            case 2:
            case 3:
                moveX += -1.6f * num;
                moveY += bit2RealHeith * 0.7f;
//                conor = -3;
                break;
            case 4:
//                conor = -6;
                moveX += -1.5f * num;
                moveY += bit2RealHeith * 0.6f;
                break;
            default:
                break;
        }

        Canvas canvas = new Canvas(bitmap);
        Bitmap resizeBmp = Bitmap.createScaledBitmap(bit2, (int) (width * 1.2f), bit2RealHeith, true);
        canvas.drawBitmap(bit1, add, moveY, null);
        canvas.drawBitmap(resizeBmp, moveX, cut , null);
        //将canvas传递进去并设置其边框
        bit1.recycle();
        bit2.recycle();
        return bitmap;
    }


    @Override
    public List<Bitmap> getRatio916(int index) {
//        index = 4;
        List<Bitmap> list = new ArrayList<>();
        Resources resources = MediaSdk.getInstance().getResouce();
        switch (index % 5) {
            case 0:
                if (ConstantMediaSize.ratioType == RatioType._16_9 || ConstantMediaSize.ratioType == RatioType._4_3) {
                    list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_three_theme169_1_1" + SUFFIX));

                } else {
                    list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_three_theme1" + SUFFIX));

                }

                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_three_particle1" + SUFFIX));

                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_three_theme2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_three_particle1" + SUFFIX));


                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_three_ballon1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_three_ballon2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_three_ballon3" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_three_theme3" + SUFFIX));

                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_three_ballon4" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_three_ballon5" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_three_ballon1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_three_theme4" + SUFFIX));

                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_three_theme5" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_three_theme6" + SUFFIX));
                break;
        }
        return list;
    }
}
