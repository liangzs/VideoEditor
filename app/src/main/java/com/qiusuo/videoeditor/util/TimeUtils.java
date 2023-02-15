package com.qiusuo.videoeditor.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.TimeZone;

public class TimeUtils {

    /**
     * 线程私有的SimpleDateFormatter
     * 解决view的ondraw内生成过多对象的问题
     */
    private static ThreadLocal<SimpleDateFormat> threadSafeDateFormatter = new ThreadLocal<>();

    /**
     * 时区无关
     * 线程私有的SimpleDateFormatter
     * 解决view的ondraw内生成过多对象的问题
     */
    private static ThreadLocal<SimpleDateFormat> threadSafeDateWithOutZoneFormatter = new ThreadLocal<>();


    /**
     * 据说读是安全的 ，写是不安全的，
     * 0时区
     */
    private static final TimeZone READ_ONLY_GMT = TimeZone.getTimeZone("GMT");

    public static final String DEFAULT_PATTER = "yyyy-MM-dd HH:mm:ss";
    public static final String MYWORK_TIME_PATTER = "MM/dd/yyyy HH:mm";
    public static final String Date_PATTER = "yyyy-MM-dd";
    public static final String Min_SEC_PATTER = "mm:ss";
    public static final String MIN_SEC_MILL_PATTER = "mm:ss.S";// 带小数形式格式
    public static final String MIN_HOUR_MILL_PATTER = "HH:mm:ss.S";// 带小数形式格式
    public static final String HOUR_MIN_SEC_PATTER = "HH:mm:ss";
    public static final String SEC_PATTER = "ss";
    private static final String CLOCK_PATTER = "HH:mm";
    private static final String WEEK_PATTER = "EE";
    private static final String DATE_PATTER[] = {"MMMM dd,yyyy", "yyyy,MMMM dd", "dd MMMM,yyyy"};
    private static final String WEEK_AND_DATE_PATTER[] = {"EE\nMMM dd", "EE\ndd MMM"};
    private static final String WEEK_AND_DATE_PATTER1[] = {"EE MM/dd yyyy", "EE dd/MM yyyy"};
    private static final String FILE_NAME_FORMAT = "yyyyMMdd_HHmmss";
    private static final String MONTH_DAY[] = {"MM/dd", "dd/MM"};



    public static final int ONE_HOUR = 3600000;

    public static SimpleDateFormat getThreadSafeDateFormatter() {
        if (threadSafeDateFormatter.get() == null) {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.setTimeZone(TimeZone.getDefault());
            threadSafeDateFormatter.set(sdf);
        }
        return threadSafeDateFormatter.get();
    }

    public static SimpleDateFormat getThreadSafeDateWithOutZoneFormatter() {
        if (threadSafeDateWithOutZoneFormatter.get() == null) {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.setTimeZone(READ_ONLY_GMT);
            threadSafeDateWithOutZoneFormatter.set(sdf);
        }
        return threadSafeDateWithOutZoneFormatter.get();
    }

    /**
     * 格式化时间 默认 xxxx年xx月xx日xx时xx分xx秒
     */
    public static String format(long time, String patter) {
        try {
            SimpleDateFormat dateFormat = getThreadSafeDateFormatter();
            dateFormat.applyPattern(patter);
            return dateFormat.format(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 计算时间长短的格式化方法
     * 此方法消除了时区可能造成的问题
     * @param time 时间
     * @param patter 格式
     * @return 格式化时间
     */
    public static String formatWithoutZone(long time, String patter) {
        try {
            SimpleDateFormat dateFormat = getThreadSafeDateWithOutZoneFormatter();
            dateFormat.setTimeZone(READ_ONLY_GMT);
            dateFormat.applyPattern(patter);
            return dateFormat.format(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 格式化时间18：00
     */
    public static String formatClock(long time, String ISO8601Time) {
        SimpleDateFormat dateFormat = getThreadSafeDateFormatter();
        dateFormat.applyPattern(CLOCK_PATTER);
        dateFormat.setTimeZone(TimeZone.getTimeZone(parseTimeZone(ISO8601Time)));
        return dateFormat.format(time);
    }



    /**
     * 带小时格式
     *
     * @param mmTime
     * @return
     */
    public static String mm2hsm(long mmTime) {
//        // 1.小时
//        // sec=1000ms
//        // min = 60sec
//        // hour = 60min
//        int h = (int) (mmTime / (60 * 60 * 1000));
//        int min = (int) (mmTime % (60 * 60 * 1000) / (60 * 1000));
//        int sec = (int) ((mmTime % (60 * 60 * 1000) % (60 * 1000)) / 1000);
//
//        Formatter formatter = mm2hsmFormatter.get();
//        if (formatter == null) {
//            mm2hsmFormatter.set(new Formatter());
//            formatter = mm2hsmFormatter.get();
//        }
//        String result = formatter.format("%02d:%02d:%02d", h, min, sec).toString();
//        return result;
        return mm2hsmHourNoLimit(mmTime);
    }

    /**
     * 带小时格式
     * 小时无限制
     */
    public static String mm2hsmHourNoLimit(long mmTime) {
        // 1.小时
        // sec=1000ms
        // min = 60sec
        // hour = 60min
        int h = (int) (mmTime / (60 * 60 * 1000));
        int min = (int) (mmTime % (60 * 60 * 1000) / (60 * 1000));
        int sec = (int) ((mmTime % (60 * 60 * 1000) % (60 * 1000)) / 1000);

        Formatter formatter = new Formatter();
        if (h < 10) {
            return formatter.format("%02d:%02d:%02d", h, min, sec).toString();
        }
        return formatter.format("%d:%02d:%02d", h, min, sec).toString();
    }

    /**
     * 线程安全
     */
    static NumberFormat twoNumber = new DecimalFormat("00");

    /**
     * 分钟无限制
     */
    public static String[] getMM_SS_s(long mmTime) {
        // 1.小时
        // sec=1000ms
        // min = 60sec
        // hour = 60min
        int min = (int) (mmTime  / (60 * 1000));
        int sec = (int) (mmTime % (60*1000) / 1000);
        int mSec = (int) (mmTime % (60*10000) / 100);
        return new String[] {twoNumber.format(min), twoNumber.format(sec), String.valueOf(mSec)};
    }

    /**
     * 格式化时间18：00
     */
    public static String formatClock(long time) {
        return format(time, CLOCK_PATTER);
    }

    private static String parseTimeZone(String iso8601Time) {
        if (TextUtils.isEmpty(iso8601Time) || iso8601Time.length() < 19) {
            return TimeZone.getDefault().getID();
        } else {
            String GMT = iso8601Time.substring(19);
            return "GMT" + GMT;
        }
    }

    /**
     * 格式化星期（简写）
     */
    public static String formatWeek(long time) {
        return format(time, WEEK_PATTER);
    }

    public static String formatForFileName() {
        return format(System.currentTimeMillis(), FILE_NAME_FORMAT);
    }

    public static long formateStringToLong(String time, String patter) {
        long mtime = 0;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patter);
            mtime = simpleDateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mtime;
    }

    public static String getDay2MsFormat() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String caculateTime(long time) {
        int totalSecond = (int) (time / 1000);
        int hours = (totalSecond / 3600);
        int min = (totalSecond / 60);
        if (hours > 0) {
            min = min - hours * 60;
        }
        int second = totalSecond % 60;
        String hoursString;
        String minString;
        String secondString;
        if (hours < 10) {
            hoursString = "0" + hours;
        } else {
            hoursString = hours + "";
        }
        if (min < 10) {
            minString = "0" + min;
        } else {
            minString = min + "";
        }

        if (second < 10) {
            secondString = "0" + second;
        } else {
            secondString = second + "";
        }
        return hoursString + ":" + minString + ":" + secondString;
    }


    /**
     * 最大单位为分钟的格式化方式
     */
    public static String calculateTimeOnlyMinuteAndSecond(long time) {
        int totalSecond = (int) (time / 1000);
        int min = (totalSecond / 60);
        int second = totalSecond % 60;
        String minString;
        String secondString;
        if (min < 10) {
            minString = "0" + min;
        } else {
            minString = min + "";
        }

        if (second < 10) {
            secondString = "0" + second;
        } else {
            secondString = second + "";
        }
        return minString + ":" + secondString;
    }

    public static String caculateTimeMinSecond(long time) {
        int totalSecond = (int) (time / 1000);
        int min = (totalSecond / 60);
        int second = totalSecond % 60;
        long millSecond = (time % 1000);
        String minString;
        String secondString;

        if (min < 10) {
            minString = "0" + min;
        } else {
            minString = min + "";
        }

        if (second < 10) {
            secondString = "0" + second;
        } else {
            secondString = second + "";
        }
        return minString + ":" + secondString + ":" + getMillSecondOneDecimal(millSecond);
    }

    /**
     * 毫秒转秒保留一位数
     *
     * @return
     */
    public static int getMillSecondOneDecimal(long duration) {
        BigDecimal b = BigDecimal.valueOf(duration).divide(new BigDecimal(100));
        int i = b.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        return i;
    }

    public static String formate(long time) {
        String formateTime = TimeUtils.format(time, MIN_SEC_MILL_PATTER);
        if (formateTime.length() > 8) {
            formateTime = formateTime.substring(0, 7);
        }
        return formateTime;
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
     * 获取指定天数
     *
     * @param format
     * @return
     */
    public static Date getDate(String format) {
        try {
            getThreadSafeDateFormatter().applyPattern(Date_PATTER);
            return getThreadSafeDateFormatter().parse(format);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
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
}
