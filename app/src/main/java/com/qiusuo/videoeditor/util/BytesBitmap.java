package com.qiusuo.videoeditor.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by DELL on 2019/7/16.
 */
public class BytesBitmap {

    public static byte[] getBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap getBitmap(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
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
    
    
    public static int getExifOrientation(String filepath) {
        short degree = 0;
        ExifInterface exif = null;
        
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException var4) {
            ;
        }
        
        if(exif != null) {
            int orientation = exif.getAttributeInt("Orientation", -1);
            if(orientation != -1) {
                switch(orientation) {
                    case 3:
                        degree = 180;
                    case 4:
                    case 5:
                    case 7:
                    default:
                        break;
                    case 6:
                        degree = 90;
                        break;
                    case 8:
                        degree = 270;
                }
            }
        }
        
        return degree;
    }
    
    
}
