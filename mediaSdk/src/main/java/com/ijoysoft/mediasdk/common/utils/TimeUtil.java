package com.ijoysoft.mediasdk.common.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    static DateFormat dateFormat;

    public static String getDayFormatTime(long value) {
        try {
            dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            return dateFormat.format(new Date(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDayFormatTime() {
        try {
            dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 毫秒转00:00:00.000格式
     * 裁剪时间转化
     *
     * @param duration
     * @return
     */
    public static String getCutForamt(float duration) {
        int time = (int) (duration / 1000);
        int ms = (int) (duration % 1000);
        int hour = time / 3600;
        int min = (time - hour * 3600) / 60;
        int second = (time - hour * 3600 - min * 60);
        String h = hour < 10 ? "0" + hour : "" + hour;
        String m = min < 10 ? "0" + min : "" + min;
        String s = second < 10 ? "0" + second : "" + second;
        StringBuilder builder = new StringBuilder();
        builder.append(h).append(":").append(m).append(":").append(s);
        if (ms > 0) {
            builder.append(".").append(ms);
        }
        return builder.toString();
    }

    /**
     * 毫秒转00:00:00格式
     * 裁剪时间转化
     *
     * @param duration  毫秒
     * @return
     */
    public static String getTimeShowForamt(float duration) {
        int time = (int) (duration / 1000);
        int hour = time / 3600;
        int min = (time - hour * 3600) / 60;
        int second = (time - hour * 3600 - min * 60);
        String h = hour < 10 ? "0" + hour : "" + hour;
        String m = min < 10 ? "0" + min : "" + min;
        String s = second < 10 ? "0" + second : "" + second;
        StringBuilder builder = new StringBuilder();
        builder.append(h).append(":").append(m).append(":").append(s);
        return builder.toString();
    }

    /**
     * 转化成一个小数的时间,采用四舍五入
     * @return
     */
    public static float getSecondOneDecimal(long duration) {
        BigDecimal b = BigDecimal.valueOf(duration).divide(new BigDecimal(1000));
        float f = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return f;
    }
    /**
     * 转化成一个小数的时间,采用四舍五入
     * @return
     */
    public static float getSecondOneDecimal(float duration) {
        BigDecimal b = BigDecimal.valueOf(duration).divide(new BigDecimal(1000));
        float f = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return f;
    }


    /**
     * 四舍五入转化成秒
     *
     * @return
     */
    public static int getSecond(float duration) {
        return (int) Math.rint(duration / 1000.0f);
    }
    /**
     * 转化成一个小数的时间,采用四舍五入
     * @return
     */
    public static float getSecondOneDecimal(long duration,int baseTime) {
        BigDecimal b = BigDecimal.valueOf(duration).divide(new BigDecimal(baseTime));
        float f = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return f;
    }
}
