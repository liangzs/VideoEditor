package com.ijoysoft.mediasdk.common.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    static DateFormat dateFormat;

    public static String getDayFormatTime1(long value) {
        try {
            dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
            return dateFormat.format(new Date(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDayFormatTime(long value) {
        try {
            dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH);
            return dateFormat.format(new Date(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDayFormatTime() {
        try {
            dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.ENGLISH);
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getNamillion() {
        return "" + System.nanoTime();
    }

    /**
     * 毫秒转00:00:00.000格式
     * 裁剪时间转化
     *
     * @param duration
     * @return
     */
    public static String getCutForamt(float duration) {
        if (duration < 0) {
            return "00:00:00";
        }
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
     * @param duration 毫秒
     * @return
     */
    public static String getTimeShowForamt(float duration) {
        if (duration < 0) {
            return "00:00:00";
        }
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

    public static String getTimeCutForamt(float duration) {
        if (duration < 0) {
            return "00:00";
        }
        int time = (int) (duration / 1000);
        int hour = time / 3600;
        int min = (time - hour * 3600) / 60;
        int second = (time - hour * 3600 - min * 60);
        String h = hour < 10 ? "0" + hour : "" + hour;
        String m = min < 10 ? "0" + min : "" + min;
        String s = second < 10 ? "0" + second : "" + second;
        StringBuilder builder = new StringBuilder();
        builder.append(m).append(":").append(s);
        return builder.toString();
    }

    /**
     * 转化成一个小数的时间,采用四舍五入
     *
     * @return
     */
    public static float getSecondOneDecimal(long duration) {
        BigDecimal b = BigDecimal.valueOf(duration).divide(new BigDecimal(1000));
        float f = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return f;
    }

    /**
     * 转化成一个小数的时间,采用四舍五入
     *
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
     *
     * @return
     */
    public static float getSecondOneDecimal(long duration, int baseTime) {
        BigDecimal b = BigDecimal.valueOf(duration).divide(new BigDecimal(baseTime));
        float f = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return f;
    }


    /**
     * 格式化时长
     *
     * @param time
     * @return
     */
    public static String formatDuration(long time) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        String result = null;

        second = (int) (time / 1000);
        if (second >= 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute >= 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        // 时
        if (hour > 0) {
            if (hour < 10) {
                result = "0" + hour;
            } else {
                result = "" + hour;
            }
        }
        // 分
        if (minute > 0) {
            if (result != null) {
                if (minute < 10) {
                    result += ":0" + minute;
                } else {
                    result += ":" + minute;
                }
            } else {
                if (minute < 10) {
                    result = "0" + minute;
                } else {
                    result = "" + minute;
                }
            }
        } else {
            if (result != null) {
                result += ":00";
            }
        }
        // 秒
        if (second > 0) {
            if (result != null) {
                if (second < 10) {
                    result += ":0" + second;
                } else {
                    result += ":" + second;
                }
            } else {
                if (second < 10) {
                    result = "00:0" + second;
                } else {
                    result = "00:" + second;
                }
            }
        } else {
            if (result != null) {
                result += ":00";
            }
        }
        if (result == null) {
            result = "00:01";
        }
        return result;
    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 小数点两位
     *
     * @return
     */
    public static float getDecimalTwo(float value) {
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        String str = format.format(value);
        return Float.parseFloat(str);
    }
}
