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
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class VTFifteenPreTreatment extends BasePreTreatment {
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
        int num = FRAME_BORDER * 2;
        int width = bitmap.getWidth() + num;
        int height = bitmap.getHeight() + num;
        int conor = 5;
        switch (pretreatConfig.getIndex() % 5) {
            case 0:
            case 2:
            case 4:
                conor = 5;
                break;
            case 1:
            case 3:
                conor = -5;
                break;
            default:
                break;
        }
        // 背图
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        // 生成白色的
        canvas.drawBitmap(bitmap, num / 2, num / 2, paint);
        bitmap.recycle();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        bitmap = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_border" + SUFFIX);
        Bitmap resizeBmp = Bitmap.createScaledBitmap(bitmap, width, height, true);
        canvas.drawBitmap(resizeBmp, 0, 0, paint);
        resizeBmp.recycle();
        bitmap.recycle();
        Matrix m = new Matrix();
        m.postRotate(conor);
        bitmap = Bitmap.createBitmap(newBitmap, 0, 0, newBitmap.getWidth(), newBitmap.getHeight(), m, true);
        newBitmap.recycle();
        return ConcatPhoto(bitmap, ConstantMediaSize.themePath + "/vt_fifteen_head" + SUFFIX, pretreatConfig.getIndex());
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
    private static Bitmap ConcatPhoto(Bitmap bit1, String res, int index) {
        int num = FRAME_BORDER * 2;
        Bitmap bit2 = BitmapUtil.decriptImage(res);
        int width = bit1.getWidth();
        int bit2RealHeith = bit2.getHeight() * width / bit2.getWidth();
        int height = bit1.getHeight() + bit2RealHeith;
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
        switch (index % 5) {
            case 0:
            case 2:
            case 4:
                moveX += -num;
                break;
            case 1:
            case 3:
                moveX += -2.0f * num;
                break;
            default:
                break;
        }
        //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        Canvas canvas = new Canvas(bitmap);
        Bitmap resizeBmp = Bitmap.createScaledBitmap(bit2, (int) (width * 1.2f), bit2RealHeith, true);
        canvas.drawBitmap(bit1, add, moveY + bit2RealHeith * 0.65f, null);
        canvas.drawBitmap(resizeBmp, moveX, cut, null);
        //将canvas传递进去并设置其边框
        bit1.recycle();
        bit2.recycle();
        return bitmap;
    }


    @Override
    public List<Bitmap> getRatio916(int index) {
//        index = 0;
        List<Bitmap> list = new ArrayList<>();
        Resources resources = MediaSdk.getInstance().getResouce();
        switch (index % 5) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_theme1_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_particle1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_particle2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_particle3" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_particle4" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_theme2_3" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_theme2_4" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_theme2_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_theme2_2" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_theme3_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_particle6" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_particle7" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_particle8" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_particle9" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_theme4_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_theme2_3" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_theme4_2" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_theme5_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_particle6" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_particle1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_particle5" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_fifteen_particle7" + SUFFIX));
                break;
        }
        return list;
    }

    @Override
    public long dealDuration(int index) {
        return 5400;
    }
}
