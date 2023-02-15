package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.rakshabandhan;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class Rakshabandhan1PreTreatment extends BasePreTreatment {
    @Override
    public int afterPreRotation() {
        return 0;
    }

//    @Override
//    public Bitmap addFrame(String path, int rotation, int index) {
//        long start = System.currentTimeMillis();
//        Bitmap bitmap = scaleBitmap(path);
//        int num = FRAME_BORDER;
//        int width = bitmap.getWidth() + num;
//        int height = bitmap.getHeight() + num;
//        LogUtils.i("test", "width:" + width + ",height:" + height + ",");
//        if (rotation == 90 || rotation == 270) {
//            height = bitmap.getWidth() + num;
//            width = bitmap.getHeight() + num;
//        }
//        // 背图
//        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
//        Canvas canvas = new Canvas(newBitmap);
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
//        // 生成白色的
//        paint.setColor(Color.WHITE);
//        if (rotation != 0) {
//            Matrix m = new Matrix();
//            m.postRotate(rotation);
//            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
//        }
//        canvas.drawBitmap(bitmap, num / 2, num / 2, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
//        // 画正方形的
//        canvas.drawRect(0, 0, width, height, paint);
//        bitmap.recycle();
//        Log.i("addBitmapFrame:", "time:" + (System.currentTimeMillis() - start));
//        if (index == 0) {
//            return addFirstFrame(newBitmap);
//        }
//        return newBitmap;
//    }

//    @Override
//    public Bitmap addFirstFrame(Bitmap bit) {
//        Bitmap bit2 = BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), "/bd_one_theme1_pre);
//        int width = bit.getWidth() + 2 * FRAME_BORDER;
//        int bit2RealHeith = bit2.getHeight() * width / bit2.getWidth();
//        int height = bit.getHeight() + bit2RealHeith - FRAME_BORDER / 2;
//        //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
//        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
//        Canvas canvas = new Canvas(bitmap);
//        Bitmap resizeBmp = Bitmap.createScaledBitmap(bit2, width, bit2RealHeith, true);
//        canvas.drawBitmap(bit, FRAME_BORDER, bit2RealHeith - FRAME_BORDER, null);
//        canvas.drawBitmap(resizeBmp, 0, 0, null);
//        //将canvas传递进去并设置其边框
//        bit.recycle();
//        bit2.recycle();
//        return bitmap;
//    }

    private final int MIPCOUNT = 6;

    @Override
    public int getMipmapsCount() {
        return MIPCOUNT;
    }



    private static final String[][] SRCS = new String[][]{
            {"/rb_one_action1", "/rb_one_circle"},
            {"/rb_one_action2_part", "/rb_one_action2", "/rb_one_circle"},
            {"/rb_one_action3", "/rb_one_text", "/rb_one_circle"},
            {"/rb_one_action4_part", "/rb_one_action4", "/rb_one_circle"},
            {"/rb_one_action5", "/rb_one_text", "/rb_one_circle"},
            {"/rb_one_action6", "/rb_one_circle"}
    };

    @Override
    public List<Bitmap> getRatio916(int index) {
//        index = 5;
        List<Bitmap> list = new ArrayList<>();
//        Resources resources = MediaSdk.getInstance().getResouce();

        String[] src = SRCS[index % MIPCOUNT];
        for (String resource : src) {
//            list.add(BitmapFactory.decodeResource(resources, resource));
            list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + resource + SUFFIX));
        }
        return list;
    }


}
