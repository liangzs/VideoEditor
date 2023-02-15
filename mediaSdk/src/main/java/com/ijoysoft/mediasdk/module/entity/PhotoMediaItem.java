package com.ijoysoft.mediasdk.module.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextPaint;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.MediaSdk;

import java.lang.ref.SoftReference;
import java.util.Locale;

public class PhotoMediaItem extends MediaItem {
    private String title;
    private long size;

    /**
     * 裁剪区域
     */
    private Rect cropRect;

    public PhotoMediaItem() {
        super();
        mediaType = MediaType.PHOTO;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Rect getCropRect() {
        return cropRect;
    }

    public void setCropRect(Rect cropRect) {
        this.cropRect = cropRect;
    }

    /**
     * 额外数据，目前用来存放MediaEntity
     */
    private Object extra;

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    /**
     * 默认图，软引用做缓存
     */
    private volatile static SoftReference<Bitmap> defaultBitmap;


    /**
     * 生成默认图
     */
    public static Bitmap createDefaultBitmap() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置参数
        //可变
        options.inMutable = true;
        //确定宽高
        options.outHeight = 720;
        options.outWidth = 720;
        Bitmap bitmap = BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.default_image, options);
        Canvas canvas = new Canvas(bitmap);
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(24);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#CCffffff"));
        //获取文字
        String hint = MediaSdk.getInstance().getResouce().getString(R.string.clip_lost);
        //测量宽度
        float width = textPaint.measureText(hint);
        //绘制文字
        canvas.drawText(hint, (bitmap.getWidth() - width) / 2f, 417, textPaint);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            defaultBitmapLocate = MediaSdk.getInstance().getResouce().getConfiguration().getLocales().get(0);
        } else {
            defaultBitmapLocate = MediaSdk.getInstance().getResouce().getConfiguration().locale;
        }
        defaultBitmap = new SoftReference<Bitmap>(bitmap);

        //返回bitmap保证强引用存在
        return bitmap;
    }

    /**
     * 获取默认图
     */
    public static Bitmap getDefaultBitmap() {
        Bitmap strongReference = null;
        if (defaultBitmap != null) {
            //先获取一波强引用，防止检测到有之后被回收。
            strongReference = defaultBitmap.get();
        }
        if (checkDefaultBitmapNotAvailable()) {
            synchronized (PhotoMediaItem.class) {
                if (checkDefaultBitmapNotAvailable()) {
                    strongReference = createDefaultBitmap();
                }
            }
        }
        return defaultBitmap.get();
    }

    /**
     * 图片地区
     */
    private volatile static Locale defaultBitmapLocate;

    /**
     * 检查缓存是否失效
     */
    public static boolean checkDefaultBitmapNotAvailable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return
                    //缓存容器为空
                    defaultBitmap == null
                            //缓存为空
                            || defaultBitmap.get() == null
                            //地区或语言不一致
                            || !MediaSdk.getInstance().getResouce().getConfiguration().getLocales().get(0).equals(defaultBitmapLocate);
        } else {
            return defaultBitmap == null
                    || defaultBitmap.get() == null
                    || !MediaSdk.getInstance().getResouce().getConfiguration().locale.equals(defaultBitmapLocate);
        }
    }

    /**
     * 判断是否是默认图
     */
    public static boolean assertDefaultBitmap(Bitmap bitmap) {
        if (defaultBitmap == null) {
            return false;
        }
        Bitmap bitmap1 = defaultBitmap.get();
        return bitmap == bitmap1;

    }
}
