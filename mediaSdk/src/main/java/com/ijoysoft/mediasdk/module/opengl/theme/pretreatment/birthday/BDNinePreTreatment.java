package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.birthday;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class BDNinePreTreatment extends BasePreTreatment {
    @Override
    public int afterPreRotation() {
        return 0;
    }

    /**
     * 固定了宽高之后，拼接不在受素材大小的影响了
     *
     * @return
     */
    @Override
    public Bitmap addFrame(PretreatConfig pretreatConfig) {
        if(pretreatConfig.isVideo()){
            return null;
        }
        Bitmap bitmap = fitAndCutRatioBitmap(pretreatConfig);
        int num = FRAME_BORDER * 2;
        int width = bitmap.getWidth() + num;
        int height = bitmap.getHeight() + num;

        int conor = 5;
        float moveY = 3 * num;
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
        bitmap = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_nine_border" + SUFFIX);
        Bitmap resizeBmp = Bitmap.createScaledBitmap(bitmap, width, height, true);
        canvas.drawBitmap(resizeBmp, 0, 0, paint);
        resizeBmp.recycle();
        bitmap.recycle();
        Matrix m = new Matrix();
        m.postRotate(conor);
        bitmap = Bitmap.createBitmap(newBitmap, 0, 0, newBitmap.getWidth(), newBitmap.getHeight(), m, true);
        newBitmap.recycle();
        return ConcatPhoto(bitmap);
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
    private Bitmap ConcatPhoto(Bitmap bit1) {
        Bitmap bit2 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_nine_head" + SUFFIX);
        int height = bit2.getHeight() + bit1.getHeight();
        int width = bit1.getWidth();
        int offsetX = (bit1.getWidth() - bit2.getWidth()) / 2;
        //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bit1, 0, FRAME_BORDER * 10, null);
        canvas.drawBitmap(bit2, offsetX, 0, null);
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
//        index = 4;
        switch (index % MIPCOUNT) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_nine_theme1_1" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_nine_theme2_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_nine_theme2_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_nine_theme2_3" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_nine_theme3_1" + SUFFIX));
                break;
            case 3:
//                list.add(BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.bd_nine_theme4_1));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_nine_theme4_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_nine_theme4_2" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_nine_theme5_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_nine_theme5_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_nine_theme5_3" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_nine_theme5_4" + SUFFIX));
                break;
        }
        return list;
    }

}
