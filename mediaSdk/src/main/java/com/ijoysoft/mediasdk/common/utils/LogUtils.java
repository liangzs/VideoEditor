package com.ijoysoft.mediasdk.common.utils;

import android.os.Looper;
import android.util.Log;

/**
 * 系统的日志处理类，对android默认日志做了封装，根据系统配置的日志级别打印日志
 * 新版本的logcat Tag的区域有限，无法再完整显示TAG，将tag恢复原样，改变添加的栈信息加到message中
 */
public class LogUtils {
    private static boolean DEBUG = false;

    /**
     * 不再需要，有进程名称了，占用区域
     */
    @Deprecated
    private static final String SNS_TAG = "ijoysoft ";


    private static final String NULL = "null";

    public static void setDEBUG(boolean debug) {
        DEBUG = debug;
    }

    /**
     * @param msg
     * @return
     */
    private static String dealString(String msg) {
        return addStackTraceInfo(msg == null ? NULL : msg);
    }

    /**
     * 获取tag,加入了类名\方法名\行号
     * 新版本的logcat Tag的区域有限，无法再完整显示TAG，将tag恢复原样，改变添加的栈信息加到message中
     */
    private static String addStackTraceInfo(String msg) {
        //获取类名\方法名\行号\
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String methodName = caller.getMethodName();
        int lineNumber = caller.getLineNumber();
        return String.format("%s:%s:%s", methodName, lineNumber, msg);
    }

    public static void i(String tag, String s) {
        if (DEBUG) {
            Log.i(tag, dealString(s));
        }
    }

    public static void e(String tag, Object s) {
        if (null != s) {
            e(tag, dealString(String.valueOf(s)));
        }
    }

    public static void e(String tag, String s) {
        if (DEBUG) {
            Log.e(tag, dealString(s));
        }
    }

    public static void e(String tag, String s, Throwable tr) {
        if (DEBUG) {
            Log.e(tag, dealString(s), tr);
        }
    }

    public static void d(String tag, String s) {
        if (DEBUG) {
            Log.d(tag, dealString(s));
        }
    }

    public static void w(String tag, String s) {
        if (DEBUG) {
            Log.w(tag, dealString(s));
        }
    }

    public static void w(String tag, String s, Throwable tr) {
        if (DEBUG) {
            Log.w(tag, dealString(s), tr);
        }
    }

    public static void v(String tag, String s) {
        if (DEBUG) {
            Log.v(tag, dealString(s));
        }
    }

    public static void v(String tag, String s, Throwable tr) {
        if (DEBUG) {
            Log.v(tag, dealString(s), tr);
        }
    }

    public static void i(Object tag, String s) {
        if (DEBUG) {
            Log.i(tag.getClass().getSimpleName(), dealString(s));
        }
    }


    public static void e(Object tag, String s) {
        if (DEBUG) {
            Log.e(tag.getClass().getSimpleName(), dealString(s));
        }
    }


    public static void d(Object tag, String s) {
        if (DEBUG) {
            Log.d(tag.getClass().getSimpleName(), dealString(s));
        }
    }

    public static void v(Object tag, String s) {
        if (DEBUG) {
            Log.v(tag.getClass().getSimpleName(), dealString(s));
        }
    }

    public static void w(Object tag, String s) {
        if (DEBUG) {
            Log.w(tag.getClass().getSimpleName(), dealString(s));
        }
    }


    public static void assertNotMainThreadOrLogError(String tag) {
        if (DEBUG && Looper.getMainLooper() == Looper.myLooper()) {
            e(tag, "assertNotMainThreadOrLogError: this action could not be executed at the main thread");
            int i = 3;
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            while (FileUtils.class.getName().equals(trace[i].getClassName())) {
                i++;
            }
            e(tag, "assertNotMainThreadOrLogError: at " + trace[i].toString());
            e(tag, "assertNotMainThreadOrLogError: at " + trace[i + 1].toString());
        }
    }
}
