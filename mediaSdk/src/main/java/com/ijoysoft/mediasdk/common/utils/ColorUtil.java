package com.ijoysoft.mediasdk.common.utils;


import android.graphics.Color;

import java.util.Locale;

/**
 * Color工具类（color整型、rgb数组、16进制互相转换）
 */

public class ColorUtil {

    /**
     * Color的Int整型转Color的16进制颜色值【方案一】
     * colorInt - -12590395
     * return Color的16进制颜色值——#3FE2C5
     */
    public static String int2Hex(int colorInt) {
        String hexCode = "";
        hexCode = String.format(Locale.ENGLISH,"#%06X", Integer.valueOf(16777215 & colorInt));
        return hexCode;
    }

    /**
     * Color的Int整型转Color的16进制颜色值【方案二】
     * colorInt - -12590395
     * return Color的16进制颜色值——#3FE2C5
     */
    public static String int2Hex2(int colorInt) {
        String hexCode = "";
        int[] rgb = int2Rgb(colorInt);
        hexCode = rgb2Hex(rgb);
        return hexCode;
    }

    /**
     * Color的Int整型转Color的rgb数组
     * colorInt - -12590395
     * return Color的rgb数组 —— [63,226,197]
     */
    public static int[] int2Rgb(int colorInt) {
        int[] rgb = new int[]{0, 0, 0, 0};

        int red = Color.red(colorInt);
        int green = Color.green(colorInt);
        int blue = Color.blue(colorInt);
        int alpha = Color.alpha(colorInt);
        rgb[0] = red;
        rgb[1] = green;
        rgb[2] = blue;
        rgb[3] = alpha;

        return rgb;
    }

    /**
     * rgb数组转Color的16进制颜色值
     * rgb - rgb数组——[63,226,197]
     * return Color的16进制颜色值——#3FE2C5
     */
    public static String rgb2Hex(int[] rgb) {
        String hexCode = "#";
        for (int i = 0; i < rgb.length; i++) {
            int rgbItem = rgb[i];
            if (rgbItem < 0) {
                rgbItem = 0;
            } else if (rgbItem > 255) {
                rgbItem = 255;
            }
            String[] code = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
            int lCode = rgbItem / 16;//先获取商，例如，255 / 16 == 15
            int rCode = rgbItem % 16;//再获取余数，例如，255 % 16 == 15
            hexCode += code[lCode] + code[rCode];//FF
        }
        return hexCode;
    }

    /**
     * Color的16进制颜色值 转 Color的Int整型
     * colorHex - Color的16进制颜色值——#3FE2C5
     * return colorInt - -12590395
     */
    public static int hex2Int(String colorHex) {
        int colorInt = 0;
        colorInt = Color.parseColor(colorHex);
        return colorInt;
    }

    /**
     * Color的16进制颜色值 转 rgb数组
     * colorHex - Color的16进制颜色值——#3FE2C5
     * return Color的rgba数组 —— [63,226,197,1]
     */
    public static int[] hex2Rgb(String colorHex) {
        int colorInt = hex2Int(colorHex);
        return int2Rgb(colorInt);
    }

    /**
     * Color的rgb数组转Color的Int整型
     * rgb - Color的rgb数组 —— [63,226,197]
     * return colorInt - -12590395
     */
    public static int rgb2Int(int[] rgb) {
        int colorInt = 0;
        colorInt = Color.rgb(rgb[0], rgb[1], rgb[2]);
        return colorInt;
    }

}