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
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class BDEighteenPreTreatment extends BasePreTreatment {
    private final int MIPCOUNT = 5;

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
        if(pretreatConfig.isVideo()){
            return null;
        }
        long start = System.currentTimeMillis();
        Bitmap bitmap = fitAndCutRatioBitmap(pretreatConfig);
        int num = FRAME_BORDER*2;
        int width = bitmap.getWidth() + num;
        int height = bitmap.getHeight() + num;
        if (pretreatConfig.getRotation() == 90 || pretreatConfig.getRotation() == 270) {
            height = bitmap.getWidth() + num;
            width = bitmap.getHeight() + num;
        }
        int conor = 5;
        float moveX = 0, moveY = 0;
        switch (pretreatConfig.getIndex() % MIPCOUNT) {
            case 0:
            case 2:
            case 4:
                conor = 6;
                moveX = num / 2;
                moveY = -num;
                break;
            case 1:
            case 3:
                conor = -6;
                moveX = -num / 2;
                moveY = -num;
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
        if(pretreatConfig.getRotation()!=0){
            Matrix m = new Matrix();
            m.postRotate(pretreatConfig.getRotation());
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        }
        canvas.drawBitmap(bitmap, num / 2, num / 2, paint);
        bitmap.recycle();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        bitmap = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_eightteen_bg" + SUFFIX);
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

        return ConcatPhoto(bitmap, moveX, moveY);
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
    private Bitmap ConcatPhoto(Bitmap bit1, float offsetX, float offsetY) {
        Bitmap bit2 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_eightteen_bg_head" + SUFFIX);
        int width = bit1.getWidth();
        int bit2RealHeith = (int) (bit2.getHeight() * width / bit2.getWidth());
        int height = bit1.getHeight() + bit2RealHeith;

        //cut: 充值y轴绘制起始坐标，逻辑上从moveY开始绘制，moveY - 0 这个区间绘制的内容实际上被丢弃， 下方的moveX 同理相反，增加空白区域
        float cut = 0, add = 0;
        float moveY = 0, moveX = 0;
        //剪切不需要的多于区域，让照片最大化
        if (ConstantMediaSize.ratioType.getRatioValue() > 1 && bit1.getWidth() > bit1.getHeight() /*&& ConstantMediaSize.ratioType.getRatioValue() > bit1.getWidth() / (float) bit1.getHeight()*/) {
            cut = moveY = -200 * (ConstantMediaSize.ratioType.getRatioValue() - 1f);
            height += 2* moveY;
            LogUtils.i("BDThreePreTreatment", "ConcatPhoto resize cut: " + cut);
        }
        if (ConstantMediaSize.ratioType.getRatioValue() < 1 /*&& bit1.getWidth() > bit1.getHeight() && ConstantMediaSize.ratioType.getRatioValue() > bit1.getWidth() / (float) bit1.getHeight()*/) {
            add = moveX = (int) (200 * (1f - ConstantMediaSize.ratioType.getRatioValue()));
            width += 2 * moveX;
            LogUtils.i("BDThreePreTreatment", "ConcatPhoto resize add: " + add);
        }
        offsetY += moveY;
        offsetX += moveX;

        //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        Canvas canvas = new Canvas(bitmap);
        Bitmap resizeBmp = Bitmap.createScaledBitmap(bit2, width, bit2RealHeith, true);
        canvas.drawBitmap(bit1, add, bit2RealHeith + offsetY, null);
        canvas.drawBitmap(resizeBmp, offsetX, cut, null);
        //将canvas传递进去并设置其边框
        bit1.recycle();
        bit2.recycle();
        return bitmap;
    }

    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
        Resources resources = MediaSdk.getInstance().getResouce();
//        index = 4;
        switch (index % MIPCOUNT) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_eightteen_theme1_1" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_eightteen_theme2_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_eightteen_theme2_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_eightteen_theme2_3" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_eightteen_theme3_1" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_eightteen_theme4_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_eightteen_theme4_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_eightteen_theme4_3" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_eightteen_theme5_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_eightteen_theme5_2" + SUFFIX));
                break;
        }
        return list;
    }
}
