package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.birthday;

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
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class BDThirteenPreTreatment extends BasePreTreatment {
    @Override
    public int afterPreRotation() {
        return 0;
    }

    @Override
    public Bitmap addFrame(PretreatConfig pretreatConfig) {
        if(pretreatConfig.isVideo()){
            return null;
        }
        Bitmap bitmap = fitRatioBitmap(pretreatConfig);
        int num = FRAME_BORDER / 2;
        int width = bitmap.getWidth() + num;
        int height = bitmap.getHeight() + num;
        // 背图
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        // 生成白色的
        int color = Color.WHITE;
        switch (pretreatConfig.getIndex() % 5) {
            case 0:
            case 2:
            case 4:
                color = Color.argb(255, 0, 237, 255);
                break;
            case 1:
            case 3:
                color = Color.argb(255, 255, 209, 68);
                break;
        }
        paint.setColor(color);
        canvas.drawBitmap(bitmap, num / 2, num / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        // 画正方形的
        canvas.drawRect(0, 0, width, height, paint);
        bitmap.recycle();
        return newBitmap;
    }


    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
        Resources resources = MediaSdk.getInstance().getResouce();
//        index = 0;
        switch (index % 5) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_thirteen_theme1_1" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_thirteen_theme2_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_thirteen_theme2_2" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_thirteen_theme3_1" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_thirteen_theme4_1" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_thirteen_theme4_2" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_thirteen_theme4_3" + SUFFIX));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_thirteen_theme4_4" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_thirteen_theme3_1" + SUFFIX));
                break;
        }
        return list;
    }

//    @Override
//    public Bitmap addText(String text) {
//        int WIDTH = 30;
//        int HEIGHT = 30;
//        //最大水平的波形高度
//        float WAVE_HEIGHT = 50;
//        //小格相交的总的点数
//        int COUNT = (WIDTH + 1) * (HEIGHT + 1);
//        float[] verts = new float[COUNT * 2];
//        float[] origs = new float[COUNT * 2];
//
//        float k = 0;
//        float period = 4;
//
//        Resources resources = MediaSdk.getInstance().getResouce();
//        Bitmap mbitmap = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_thirteen_text" + SUFFIX);
//        int bitmapwidth = mbitmap.getWidth();
//        int bitmapheight = mbitmap.getHeight();
//        Bitmap newBitmap = Bitmap.createBitmap(bitmapwidth, bitmapheight, Bitmap.Config.ARGB_4444);
//        Canvas canvas = new Canvas(newBitmap);
//        canvas.drawBitmap(mbitmap, 0, 0, null);
//        mbitmap.recycle();
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        // text color - #3D3D3D
//        paint.setColor(Color.rgb(61, 61, 61));
//        float scale = resources.getDisplayMetrics().density;
//        // text size in pixels
//        paint.setTextSize((int) (25 * scale));
//        // text shadow
//        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
//        // draw text to the Canvas center
//        Rect bounds = new Rect();
//        paint.getTextBounds(text, 0, text.length(), bounds);
//        int x = (bitmapwidth - bounds.width()) / 2;
//        int y = (bitmapheight + bounds.height()) / 2;
//        canvas.drawText(text, x, y, paint);
//
//        //绘画波浪形
//        Bitmap newBitmap1 = Bitmap.createBitmap((int) (bitmapwidth), (int) (bitmapheight + WAVE_HEIGHT), Bitmap.Config.ARGB_4444);
//        canvas = new Canvas(newBitmap1);
//        COUNT = (WIDTH + 1) * (HEIGHT + 1);
//        verts = new float[COUNT * 2];
//        origs = new float[COUNT * 2];
//
//        int index = 0;
//        for (int i = 0; i < HEIGHT + 1; i++) {
//            float fy = bitmapheight / (float) HEIGHT * i;
//            for (int j = 0; j < WIDTH + 1; j++) {
//                float fx = bitmapwidth / (float) WIDTH * j;
//                //偶数位记录x坐标  奇数位记录Y坐标
//                origs[index * 2 + 0] = verts[index * 2 + 0] = fx;
//                origs[index * 2 + 1] = verts[index * 2 + 1] = fy;
//                index++;
//            }
//        }
//        for (int i = 0; i < HEIGHT + 1; i++) {
//            for (int j = 0; j < WIDTH + 1; j++) {
//                //把每一个水平像素通过正弦公式转换成正弦曲线
//                //WAVE_HEIGHT表示波峰跟波低的垂直距离，皱褶后会向上超过水平线，所以往下偏移WAVE_HEIGHT / 2
//                //5表示波浪的密集度，表示波峰波谷总共有五个,对应上面左图的1,2,3,4,5
//                //j就是水平像的X轴坐标
//                //K决定正弦曲线起始点(x=0)点的Y坐标，k=0就是从波峰波谷的中间开始左->右绘制曲线
//                float yOffset = WAVE_HEIGHT / 2 + WAVE_HEIGHT / 2 * (float) Math.sin((float) j / WIDTH * period * Math.PI + k);
//                verts[(i * (WIDTH + 1) + j) * 2 + 1] = origs[(i * (WIDTH + 1) + j) * 2 + 1] + yOffset;//
//            }
//        }
//        canvas.drawBitmapMesh(newBitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
//        return newBitmap1;
//    }
}
