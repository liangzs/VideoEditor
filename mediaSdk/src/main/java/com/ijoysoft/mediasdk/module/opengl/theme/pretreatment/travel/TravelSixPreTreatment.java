package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.travel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class TravelSixPreTreatment extends BasePreTreatment {
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
        int num = FRAME_BORDER;
        int width = bitmap.getWidth() + num;
        int height = bitmap.getHeight() + num;
        int conor = 5;
        int moveX = 0, moveY = 0;
        moveY = 2 * num;
        switch (pretreatConfig.getIndex() % 5) {
            case 0:
            case 4:
                conor = 6;
                moveX = 3 * num;

                break;
            case 1:
            case 3:
                moveX = width / 2 + 3 * num;
                conor = -4;
                break;
            case 2:
                conor = 0;
                moveX = width / 2 - num;
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
        int color = Color.WHITE;
        paint.setColor(color);
        canvas.drawBitmap(bitmap, num / 2, num / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        // 画正方形的
        canvas.drawRect(0, 0, width, height, paint);
        Matrix m = new Matrix();
        m.postRotate(conor);
        bitmap.recycle();
        bitmap = Bitmap.createBitmap(newBitmap, 0, 0, newBitmap.getWidth(), newBitmap.getHeight(), m, true);

//        return  bitmap;
        newBitmap.recycle();
        String borderPath = ConstantMediaSize.themePath + "/travel_six_theme_border1" + SUFFIX;
        switch (pretreatConfig.getIndex()) {
            case 2:
            case 4:
                borderPath = ConstantMediaSize.themePath + "/travel_six_theme_border2" + SUFFIX;
                break;
        }
        return ConcatPhoto(bitmap, borderPath, moveX, moveY);
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
    private static Bitmap ConcatPhoto(Bitmap bit1, String path, int offsetX, int offsetY) {
        Bitmap bit2 = BitmapUtil.decriptImage(path);

        int lockWidth = bit1.getWidth() / 7;
        int lockHeight = (int) (bit2.getHeight() * lockWidth / bit2.getWidth());

        int height = lockHeight + bit1.getHeight() - offsetY;
        int width = bit1.getWidth();

        //cut: 充值y轴绘制起始坐标，逻辑上从moveY开始绘制，moveY - 0 这个区间绘制的内容实际上被丢弃， 下方的moveX 同理相反，增加空白区域
        float cut = 0, add = 0;
        //剪切不需要的多于区域，让照片最大化
        if (ConstantMediaSize.ratioType.getRatioValue() > 1 && bit1.getWidth() > bit1.getHeight() /*&& ConstantMediaSize.ratioType.getRatioValue() > bit1.getWidth() / (float) bit1.getHeight()*/) {
            cut = (float) (-lockHeight * 0.3);
            height += cut;
            offsetY -= cut;
            LogUtils.i("BDThreePreTreatment", "ConcatPhoto resize cut: " + cut);
        }
        if (ConstantMediaSize.ratioType.getRatioValue() < 1 /*&& bit1.getWidth() > bit1.getHeight() && ConstantMediaSize.ratioType.getRatioValue() > bit1.getWidth() / (float) bit1.getHeight()*/) {
            add = 200 * (1f - ConstantMediaSize.ratioType.getRatioValue());
            offsetX += add;
            width += 2 * add;
            LogUtils.i("BDThreePreTreatment", "ConcatPhoto resize add: " + add);
        }

        //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        Canvas canvas = new Canvas(bitmap);
        Bitmap resizeBmp = Bitmap.createScaledBitmap(bit2, lockWidth, lockHeight, true);
        canvas.drawBitmap(bit1, add, lockHeight - offsetY, null);
        canvas.drawBitmap(resizeBmp, offsetX, cut, null);
        //将canvas传递进去并设置其边框
        bit1.recycle();
        bit2.recycle();
        return bitmap;
    }

    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
//        index = 4;
        switch (index % 5) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_six_theme1_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_six_theme1_2" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_six_theme2_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_six_theme2_2" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_six_theme3_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_six_theme3_1" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_six_theme4_1" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_six_theme5_1" + SUFFIX));
                break;
        }
        return list;
    }

}
