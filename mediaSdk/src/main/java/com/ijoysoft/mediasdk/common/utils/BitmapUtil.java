package com.ijoysoft.mediasdk.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ijoysoft.mediasdk.BuildConfig;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;

import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class BitmapUtil {
    private static final String TAG = "BitmapUtil";

    /**
     * 通过资源id转化成Bitmap
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap ReadBitmapById(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 缩放Bitmap满屏
     *
     * @param bitmap
     * @param screenWidth
     * @param screenHight
     * @return
     */
    public static Bitmap getBitmap(Bitmap bitmap, int screenWidth, int screenHight) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scale = (float) screenWidth / w;
        float scale2 = (float) screenHight / h;
        // scale = scale < scale2 ? scale : scale2;
        matrix.postScale(scale, scale);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return bmp;// Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    /**
     * 按最大边按一定大小缩放图片
     */
    public static Bitmap scaleImage(byte[] buffer, float size) {
        // 获取原图宽度
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        options.inInputShareable = true;
        Bitmap bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
        // 计算缩放比例
        float reSize = options.outWidth / size;
        if (options.outWidth < options.outHeight) {
            reSize = options.outHeight / size;
        }
        // 如果是小图则放大
        if (reSize <= 1) {
            int newWidth = 0;
            int newHeight = 0;
            if (options.outWidth > options.outHeight) {
                newWidth = (int) size;
                newHeight = options.outHeight * (int) size / options.outWidth;
            } else {
                newHeight = (int) size;
                newWidth = options.outWidth * (int) size / options.outHeight;
            }
            bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            bm = scaleImage(bm, newWidth, newHeight, true);
            if (bm == null) {
                LogUtils.e(TAG, "convertToThumb, decode fail:" + null);
                return null;
            }
            return bm;
        }
        // 缩放
        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) reSize;
        bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
        if (bm == null) {
            LogUtils.e(TAG, "convertToThumb, decode fail:" + null);
            return null;
        }
        return bm;
    }

    /**
     * 检查图片是否超过一定值，是则缩小
     */
    public static Bitmap convertToThumb(byte[] buffer, float size) {
        // 获取原图宽度
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        options.inInputShareable = true;
        Bitmap bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
        // 计算缩放比例
        float reSize = options.outWidth / size;
        if (options.outWidth > options.outHeight) {
            reSize = options.outHeight / size;
        }
        if (reSize <= 0) {
            reSize = 1;
        }
        LogUtils.d(TAG, "convertToThumb, reSize:" + reSize);
        // 缩放
        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) reSize;
        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
            bm = null;
            LogUtils.e(TAG, "convertToThumb, recyle");
        }
        bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
        if (bm == null) {
            LogUtils.e(TAG, "convertToThumb, decode fail:" + null);
            return null;
        }
        return bm;
    }


    /**
     * Bitmap --> byte[]
     *
     * @param bmp
     * @return
     */
    private static byte[] readBitmap(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    /**
     * Bitmap --> byte[]
     *
     * @return
     */
    public static byte[] readBitmapFromBuffer(byte[] buffer, float size) {
        return readBitmap(convertToThumb(buffer, size));
    }

    /**
     * 以屏幕宽度为基准，显示图片
     *
     * @return
     */
    public static Bitmap decodeStream(Context context, Intent data, float size) {
        Bitmap image = null;
        try {
            Uri dataUri = data.getData();
            // 获取原图宽度
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPurgeable = true;
            options.inInputShareable = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(dataUri), null, options);
            // 计算缩放比例
            float reSize = (int) (options.outWidth / size);
            if (reSize <= 0) {
                reSize = 1;
            }
            // 缩放
            options.inJustDecodeBounds = false;
            options.inSampleSize = (int) reSize;
            image = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(dataUri), null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 按新的宽高缩放图片
     *
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleImage(Bitmap bm, int newWidth, int newHeight, boolean isRecycle) {
        if (bm == null) {
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        if (isRecycle) {
            bm.recycle();
        }
        return newbm;
    }

    /**
     * 按新的宽高缩放图片
     *
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleRatioByPathByMatrix(String path, int rotation, int newWidth, int newHeight) {
        Bitmap bm = BitmapFactory.decodeFile(path);
        if (bm == null) {
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        float temp = Math.min(scaleWidth, scaleHeight);
        Matrix matrix = new Matrix();
        matrix.postScale(temp, temp);
        matrix.postRotate(rotation);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        if (bm != null && !bm.isRecycled() && temp != 1.0f) {// 如果temp是1.0那么会直接是用原图，会执行recycle，最后会报错
            bm.recycle();
        }
        return newbm;
    }

    public static Bitmap getSmallBitmapByWH(String filePath, int rotation, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        LogUtils.i(TAG, "inSampleSize:" + options.inSampleSize + "reqWidth:" + reqWidth + "," + reqHeight + ",rotation:" + rotation + ",filePath:" + filePath);
        //如果图片太小，则对图片进行放大
        if (options.inSampleSize == 0) {
            return fitBitmap(BitmapFactory.decodeFile(filePath), rotation, reqWidth);
        }
        options.inJustDecodeBounds = false;
        // 避免出现内存溢出的情况，进行相应的属性设置。
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = true;
        Bitmap bitmap;
        if (filePath.endsWith(".gif")) {
            try {
                bitmap = Glide.with(MediaSdk.getInstance().getContext()).asBitmap().load(filePath).submit().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                bitmap = null;
            }
        } else {
            bitmap = BitmapFactory.decodeFile(filePath, options);
        }
        if (rotation != 0 && bitmap != null) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmp;
        }
        return bitmap;
    }


//    /**
//     * 取具体帧文件
//     *
//     * @param path
//     * @param frameIndex
//     * @return
//     */
//    public static Bitmap getGifFrame(String path, int frameIndex) {
//
//        GifDecoder mGifDecoder = new GifDecoder();
//        InputStream inputStream = null;
//        try {
//            inputStream = new FileInputStream(path);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        //这里面的方法会对inpustream进行close
//        mGifDecoder.read(inputStream);
//        return mGifDecoder.getFrame(frameIndex);
//    }


    public static Bitmap getGifByGlide(Context context, String path) {
        try {
//            return Glide.with(context).asBitmap().load(path).submit().get();
            GifDrawable gifDrawable = Glide.with(context).asGif().load(path).submit().get();
            return gifDrawable.getFirstFrame();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            inSampleSize = 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((height / inSampleSize) >= reqHeight
                    || (width / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        if (height < reqHeight / 2 && width < reqWidth / 2) {
            inSampleSize = 0;
        }

        return inSampleSize;
    }

    /**
     * 按新的宽高缩放图片
     *
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleRatioByMatrix(Bitmap bm, int newWidth, int newHeight) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {// 5.0以下手机进行

        }
        if (bm == null) {
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        float temp = Math.min(scaleWidth, scaleHeight);
        Matrix matrix = new Matrix();
        matrix.postScale(temp, temp);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        if (bm != null && !bm.isRecycled() && temp != 1.0f) {// 如果temp是1.0那么会直接是用原图，会执行recycle，最后会报错
            bm.recycle();
            bm = null;
        }
        return newbm;
    }

    public static Bitmap scaleRatioByPathByScale(String path, int newWidth, int newHeight) {
        try {
            // Decode image size
            File f = new File(path);
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_WIDTH = newWidth;
            final int REQUIRED_HIGHT = newHeight;
            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            LogUtils.i(TAG, "o.outWidth:" + o.outWidth + ",o.outHeight" + o.outHeight + ",scale:" + scale + ","
                    + newWidth + "," + newHeight);
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * fuction: 设置固定的宽度，高度随之变化，使图片不会变形
     *
     * @param target   需要转化bitmap参数
     * @param newWidth 设置新的宽度
     * @return
     */
    public static Bitmap fitBitmap(Bitmap target, int rotation, int newWidth) {
        if (target == null || target.getWidth() == 0 || target.getHeight() == 0) {
            return null;
        }
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        int newHeight = (int) (scaleWidth * height);
        matrix.postScale(scaleWidth, scaleWidth);
        matrix.postRotate(rotation);
        // Bitmap result = Bitmap.createBitmap(target,0,0,width,height,
        LogUtils.i(TAG, "width:" + width + ",height:" + height + ",scaleWidth:" + scaleWidth + ",");
        Bitmap bmp = Bitmap.createBitmap(target, 0, 0, width, height, matrix, true);
        return bmp;// Bitmap.createBitmap(target, 0, 0, width, height, matrix,
    }


    /**
     * 适配图片为crop或者inside类型
     *
     * @param isCrop is crop 或者inside显示
     * @return
     */
    public static Bitmap fitBitmap(String filePath, int rotation, int newWidth, int newHeight, boolean isCrop) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, newWidth, newHeight);
        //如果图片太小，则对图片进行放大
        if (options.inSampleSize == 0) {
            return fitBitmap(BitmapFactory.decodeFile(filePath), rotation, Math.max(newWidth, newHeight));
        }
        options.inJustDecodeBounds = false;
        // 避免出现内存溢出的情况，进行相应的属性设置。
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = true;
//        Bitmap target = BitmapFactory.decodeFile(filePath);
        Bitmap target = BitmapFactory.decodeFile(filePath, options);

        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        if (rotation == 90 || rotation == 270) {
            scaleHeight = ((float) newHeight) / width;
            scaleWidth = ((float) newWidth) / height;
        }
        float scale = isCrop ? Math.max(scaleWidth, scaleHeight) : Math.min(scaleWidth, scaleHeight);
        matrix.postScale(scale, scale);
        matrix.postRotate(rotation);
        LogUtils.i(TAG, "width:" + width + ",height:" + height + ",scaleWidth:" + scaleWidth + ",");
        Bitmap bmp = Bitmap.createBitmap(target, 0, 0, width, height, matrix, true);
        return bmp;// Bitmap.createBitmap(target, 0, 0, width, height, matrix,
    }

    /**
     * 根据指定的宽度平铺图像
     *
     * @param width
     * @param src
     * @return
     */
    public static Bitmap createRepeater(int width, Bitmap src) {
        int count = (width + src.getWidth() - 1) / src.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for (int idx = 0; idx < count; ++idx) {
            canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
        }
        return bitmap;
    }

    /**
     * 图片的质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        if (baos != null) {
            try {
                baos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (isBm != null) {
            try {
                isBm.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (image != null && !image.isRecycled()) {
            image.recycle();
            image = null;
        }
        return bitmap;
    }

    /**
     * 图片按比例大小压缩方法(根据Bitmap图片压缩)
     *
     * @param image
     * @return
     */
    public static Bitmap getImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        if (isBm != null) {
            try {
                isBm.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (image != null && !image.isRecycled()) {
            image.recycle();
            image = null;
        }
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 通过资源id转化成Bitmap 全屏显示
     *
     * @param context
     * @param drawableId
     * @param screenWidth
     * @param screenHight
     * @return
     */
    public static Bitmap ReadBitmapById(Context context, int drawableId, int screenWidth, int screenHight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inInputShareable = true;
        options.inPurgeable = true;
        InputStream stream = context.getResources().openRawResource(drawableId);
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
        return getBitmap(bitmap, screenWidth, screenHight);
    }

    private static void mkDirs(String fileName) {
        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }

    /**
     * 保存bitmap为png
     *
     * @param bitmap
     * @param outputFilePath
     */
    public static void savePNGBitmap(final Bitmap bitmap, final String outputFilePath) {
        mkDirs(outputFilePath);
        try {
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(outputFilePath));
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从中心裁剪图片
     *
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0 || bitmap.isRecycled()) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            // 压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            // 从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

    /**
     * 从中心裁剪图片,按宽高
     *
     * @param bitmap 原图
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap aspectScaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        if (null == bitmap || newWidth <= 0 || newHeight <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > newWidth && heightOrg > newHeight) {
            // 压缩到一个最小长度是edgeLength的bitmap
            int longerWidth = (int) (newWidth * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int longgerHeight = (int) (newHeight * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerWidth : newWidth;
            int scaledHeight = widthOrg > heightOrg ? newHeight : longgerHeight;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            // 从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - newWidth) / 2;
            int yTopLeft = (scaledHeight - newHeight) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, newWidth, newHeight);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

    public static Bitmap rotateBitmap(Bitmap b, int degree) {
        if (b == null || b.isRecycled()) {
            return b;
        }
        int degrees = degree;
        Bitmap b2 = b;
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate((float) (degrees), (float) b.getWidth() / 2.0F, (float) b.getHeight() / 2.0F);
            try {
                b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
            } catch (OutOfMemoryError var5) {
            }
        }
        return b2;

    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int rotation, int width, int height) {
        int degrees = rotation;
        Bitmap b2 = bitmap;
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate((float) (degrees), (float) bitmap.getWidth() / 2.0F, (float) bitmap.getHeight() / 2.0F);
            if (width > bitmap.getWidth() || height > bitmap.getHeight()) {
                width = bitmap.getWidth();
                height = bitmap.getHeight();
            }
            try {
                b2 = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);
            } catch (OutOfMemoryError var5) {
            }
        }
        return b2;
    }

    /**
     * 图片加解密
     * 混入字节加密
     */
    public void encriptImage(String inPath, String outPath) {
        try {
            //获取图片的字节流
            Bitmap bitmap = BitmapFactory.decodeFile(inPath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bytes = baos.toByteArray();
            FileOutputStream fops = new FileOutputStream(outPath);
            //混入的字节流
            byte[] bytesAdd = MediaSdk.ENCRYPT_KEY.getBytes();
            fops.write(bytesAdd);
            fops.write(bytes);
            fops.flush();
            fops.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDecript() {
        String path = "F:\\newyear1";
        Bitmap bitmap = decriptImage(path);
        System.out.println("sucess:" + bitmap.getWidth());
    }

    /**
     * 移除混入的字节解密图片
     */
    public static Bitmap decriptImage(String path) {
        Bitmap result = null;
        FileInputStream stream = null;
        ByteArrayOutputStream out = null;
        try {
            stream = new FileInputStream(path);
            out = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            int i = 0;
            while ((n = stream.read(b)) != -1) {
                if (i == 0) {
                    if (n < MediaSdk.ENCRYPT_KEY.length()) {
                        LogUtils.e(BitmapUtil.class.getSimpleName(), "The file wasn't encrypted! path: " + path);
                        break;
                    }
                    String encryptKey = new String(b, 0, MediaSdk.ENCRYPT_KEY.length());
                    if (MediaSdk.ENCRYPT_KEY.equals(encryptKey)) {
                        //第一次写文件流的时候，移除我们之前混入的字节
                        out.write(b, MediaSdk.ENCRYPT_KEY.length(), n - MediaSdk.ENCRYPT_KEY.length());
                    } else {
                        LogUtils.e(BitmapUtil.class.getSimpleName(), "The file wasn't encrypted! path: " + path);
                    }
                } else {
                    out.write(b, 0, n);
                }
                i++;
            }

            //获取字节流显示图片
            byte[] bytes = out.toByteArray();
            result = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (result == null) {
                Thread.sleep(50);
                result = decriptRetryImage(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result == null) {
                LogUtils.e(BitmapUtil.class.getSimpleName(), "bitmap load failed, path: " + path);
            } else {
                //频繁打印的日志放至-verbose级别中
                LogUtils.v(BitmapUtil.class.getSimpleName(), "bitmap load success, path: " + path);
            }
        }
        return result;
    }


    /**
     * 移除混入的字节解密图片
     */
    public static Bitmap decriptRetryImage(String path) {
        Bitmap result = null;
        FileInputStream stream = null;
        ByteArrayOutputStream out = null;
        try {
            stream = new FileInputStream(path);
            out = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            int i = 0;
            while ((n = stream.read(b)) != -1) {
                if (i == 0) {
                    if (n < MediaSdk.ENCRYPT_KEY.length()) {
                        LogUtils.e(BitmapUtil.class.getSimpleName(), "The file wasn't encrypted! path: " + path);
                        break;
                    }
                    String encryptKey = new String(b, 0, MediaSdk.ENCRYPT_KEY.length());
                    if (MediaSdk.ENCRYPT_KEY.equals(encryptKey)) {
                        //第一次写文件流的时候，移除我们之前混入的字节
                        out.write(b, MediaSdk.ENCRYPT_KEY.length(), n - MediaSdk.ENCRYPT_KEY.length());
                    } else {
                        LogUtils.e(BitmapUtil.class.getSimpleName(), "The file wasn't encrypted! path: " + path);
                    }
                } else {
                    out.write(b, 0, n);
                }
                i++;
            }
            //获取字节流显示图片
//            LogUtils.w(BitmapUtil.class.getSimpleName(), "BitmapFactory load failed, use Glide instead");
//            result = Glide.with(MediaSdk.getInstance().getContext()).asBitmap().load(out.toByteArray()).submit().get();
            byte[] bytes = out.toByteArray();
            result = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result == null) {
                LogUtils.e(BitmapUtil.class.getSimpleName(), "decriptRetryImage bitmap load failed, path: " + path);
            } else {
                //频繁打印的日志放至-verbose级别中
                LogUtils.v(BitmapUtil.class.getSimpleName(), "decriptRetryImage bitmap load success, path: " + path);
            }
        }
        return result;
    }

    /**
     * 加载无混入字节的图片
     */
    public static Bitmap loadImage(String path) {
        Bitmap result = null;
        try {
            FileInputStream stream = null;
            stream = new FileInputStream(new File(path));
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            while ((n = stream.read(b)) != -1) {
                out.write(b, 0, n);
            }
            stream.close();
            out.close();
            //获取字节流显示图片
            byte[] bytes = out.toByteArray();
            result = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (result == null) {
                LogUtils.e(BitmapUtil.class.getSimpleName(), "bitmap load failed, path: " + path);
            } else {
                //频繁打印的日志放至-verbose级别中
                LogUtils.v(BitmapUtil.class.getSimpleName(), "bitmap load success, path: " + path);
            }
        }
        return result;
    }

    public static int getExifOrientation(String filepath, int rotation) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            ex.printStackTrace();
            return rotation;
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }


    // 将Bitmap转换成InputStream

    public static InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    // Drawable转换成InputStream

    public static InputStream Drawable2InputStream(Resources resources, int id) {
        Bitmap bmp = BitmapFactory.decodeResource(resources, id);
        return Bitmap2InputStream(bmp);

    }

    /**
     * 返回模糊图片
     *
     * @param context
     * @param radius
     * @param original
     * @return
     */
    public static Bitmap blurBitmap(Context context, @IntRange(from = 1, to = 25) int radius, Bitmap original) {
        try {
            RenderScript renderScript = RenderScript.create(context);
            // 使用Renderscript和in/out位图创建分配(in/out)
            Allocation input = Allocation.createFromBitmap(renderScript, original);
            Allocation output = Allocation.createTyped(renderScript, input.getType());
            // 使用Renderscript创建一个固有的模糊脚本
            ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
            // 设置模糊半径:0 < radius <= 25
            if (radius < 0 || radius > 25) {
                radius = 5;
            }
            scriptIntrinsicBlur.setRadius(radius);
            // 执行渲染脚本
            scriptIntrinsicBlur.setInput(input);
            scriptIntrinsicBlur.forEach(output);
            // 将out分配创建的最终位图复制到original
            output.copyTo(original);
            return original;
        } catch (Exception e) {
            return original;
        }

    }

    /**
     * glide模糊
     * 时间耗的太久了
     * * @param context
     *
     * @param radius
     * @param original
     * @return
     */
    public static Bitmap blurBitmap(@IntRange(from = 1, to = 100) int radius, Bitmap original, Context context) {
        try {
            Bitmap bitmap = Glide.with(context).asBitmap() //必须
                    .load(original)
                    .transform(new BlurTransformation((int) (radius * 0.6f), 1))
                    .into(original.getWidth(), original.getHeight())
                    .get();
            return bitmap;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static interface BitmapInterfac {
        void onSuccess(Bitmap bitmap);
    }


    /**
     * 圆角bitmap
     *
     * @param bitmap 图片源
     * @param ratio  比例，当比例为1时，图片为正方形时，输出圆形
     * @return 裁剪后bitmap
     */
    public static Bitmap getRoundedBitmap(Bitmap bitmap, float ratio) {
        Bitmap output = null;
        try {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError error) {
            try {
                output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
            } catch (OutOfMemoryError e) {
                return null;
            }
        }
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        float cornerRadius = Math.min(bitmap.getWidth(), bitmap.getHeight()) * 0.5f * ratio;
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    public static Bitmap drawableToBitmap(Context mContext, int drawableInt) {
        Bitmap bitmap;

        Drawable drawable = mContext.getResources().getDrawable(drawableInt);

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        } else if (drawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;

            bitmap = Bitmap.createBitmap(140, 140, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            gradientDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            gradientDrawable.draw(canvas);
            return bitmap;
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap createCircle(int radius, @ColorInt int color, Paint.Style style) {
        Bitmap bitmap = Bitmap.createBitmap(radius * 2, radius * 2, Bitmap.Config.ARGB_8888);
        Paint p = new Paint();
        p.setAntiAlias(true); //去锯齿
        p.setColor(color);

        p.setStyle(style);

        Canvas canvas = new Canvas(bitmap); //bitmap就是我们原来的图,比如头像

//        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        canvas.drawCircle(radius, radius, radius, p);

        return bitmap;
    }
}
