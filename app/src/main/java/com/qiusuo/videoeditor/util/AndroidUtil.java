package com.qiusuo.videoeditor.util;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.tabs.TabLayout;
import com.ijoysoft.mediasdk.common.utils.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

public class AndroidUtil {
    private static final String TAG = "AndroidUtil";

    /**
     * 被弃用
     *
     * @param context
     * @param activity
     * @return
     */
    public static boolean isExistMainActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        if (cmpName != null) {// 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10); // 获取从栈顶开始往下查找的10个activity
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) {// 说明它已经启动了
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    private static long lastClickTime = 0;// 上次点击的时间
    private static int spaceTime = 1000;// 时间间隔
    private static int spaceTime1 = 500;// 时间间隔
    private static int spaceTime2 = 200;// 时间间隔

    /**
     * 是否快速点击
     *
     * @return
     */
    public static boolean isFastClick() {
        long currentTime = System.currentTimeMillis();// 当前系统时间
        boolean isFastClick;
        if (currentTime - lastClickTime > spaceTime) {
            isFastClick = false;
        } else {
            isFastClick = true;
        }
        lastClickTime = currentTime;
        return isFastClick;
    }

    public static boolean chekcFastClick() {
        long currentTime = System.currentTimeMillis();// 当前系统时间
        boolean isFastClick;
        long deltaTime = currentTime - lastClickTime;
//        LogUtils.v("chekcFastClick", "deltaTime=" + deltaTime);
        if (deltaTime > spaceTime1) {
            isFastClick = false;
        } else {
            isFastClick = true;
        }
        lastClickTime = currentTime;
        return isFastClick;
    }

    /**
     * 限制点击状态，第一次点击如果做了页面跳转则不再处理
     *
     * @param lifecycleOwner lifecycleOwner
     * @return chekcFastClick && lifecycle >= resumed
     */
    public static boolean checkFastClickAtResumed(LifecycleOwner lifecycleOwner) {
        return chekcFastClick() || !lifecycleOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED);
    }

    public static boolean chekcFastClickShort() {
        long currentTime = System.currentTimeMillis();// 当前系统时间
        boolean isFastClick;
        if (currentTime - lastClickTime > spaceTime2) {
            isFastClick = false;
        } else {
            isFastClick = true;
        }
        lastClickTime = currentTime;
        return isFastClick;
    }

    /**
     * 调节tablayout指示线宽度
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip, int topDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("slidingTabIndicator");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());
        int top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, topDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            params.topMargin = top;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    /**
     * 判断是否平板设备
     *
     * @param context
     * @return true:平板,false:手机
     */
    public static boolean isTabletDevice(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean checkLargeMemory() {
        Runtime r = Runtime.getRuntime();
        if (r.freeMemory() / (1024 * 1024) < 5) {// 小于5M就提示内存不足
            return true;
        }
        return false;
    }

    /**
     * 获取手机内部空间总大小
     *
     * @return 大小，字节为单位
     */
    static public long getTotalInternalMemorySize() {
        // 获取内部存储根目录
        File path = Environment.getExternalStorageDirectory();
        // 系统的空间描述类
        StatFs stat = new StatFs(path.getPath());
        // 每个区块占字节数
        long blockSize = stat.getBlockSize();
        // 区块总数
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获取手机内部可用空间大小
     * 减去固定50M的大小预留空间
     * 20*1024*1024
     *
     * @return 大小，字节为单位
     */
    static public long getAvailableInternalMemorySize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        // 获取可用区块数量
        long availableBlocks = stat.getAvailableBlocks();
        return (availableBlocks * blockSize - 52428800);
    }


    /**
     * 做音频裁剪时，核查一下是否有足够的空间进行裁剪的存放
     *
     * @return
     */
    static public long getAvailableTrimMemorySize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        // 获取可用区块数量
        long availableBlocks = stat.getAvailableBlocks();
        return (availableBlocks * blockSize - 10485760);
    }

    /**
     * 判断SD卡是否可用
     *
     * @return true : 可用<br>false : 不可用
     */
    public static boolean isSDCardEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取手机外部总空间大小
     *
     * @return 总大小，字节为单位
     */
    static public long getTotalExternalMemorySize() {
        if (isSDCardEnable()) {
            // 获取SDCard根目录
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return -1;
        }
    }

    /**
     * 获取SD卡剩余空间
     *
     * @return SD卡剩余空间
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getAvailableExternalMemorySize() {
        if (!isSDCardEnable())
            return "sdcard unable!";
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize, availableBlocks;
        availableBlocks = stat.getAvailableBlocksLong();
        blockSize = stat.getBlockSizeLong();
        long size = availableBlocks * blockSize;
        return String.valueOf(size);
    }

    /**
     * 获取SD卡信息
     *
     * @return SDCardInfo
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getSDCardInfo() {
        SDCardInfo sd = new SDCardInfo();
        if (!isSDCardEnable())
            return "sdcard unable!";
        sd.isExist = true;
        StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
        sd.totalBlocks = sf.getBlockCountLong();
        sd.blockByteSize = sf.getBlockSizeLong();
        sd.availableBlocks = sf.getAvailableBlocksLong();
        sd.availableBytes = sf.getAvailableBytes();
        sd.freeBlocks = sf.getFreeBlocksLong();
        sd.freeBytes = sf.getFreeBytes();
        sd.totalBytes = sf.getTotalBytes();
        return sd.toString();
    }

    public static class SDCardInfo {
        boolean isExist;
        long totalBlocks;
        long freeBlocks;
        long availableBlocks;
        long blockByteSize;
        long totalBytes;
        long freeBytes;
        long availableBytes;

        @Override
        public String toString() {
            return "isExist=" + isExist + "\ntotalBlocks=" + totalBlocks + "\nfreeBlocks=" + freeBlocks
                    + "\navailableBlocks=" + availableBlocks + "\nblockByteSize=" + blockByteSize + "\ntotalBytes="
                    + totalBytes + "\nfreeBytes=" + freeBytes + "\navailableBytes=" + availableBytes;
        }
    }


    public static long getAudioTime(String filePath) {
        long mediaPlayerDuration = 0L;
        if (filePath == null || filePath.isEmpty()) {
            return 0;
        }
        long start = System.currentTimeMillis();
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            FileInputStream fileInputStream = new FileInputStream(new File(filePath).getAbsolutePath());
            retriever.setDataSource(fileInputStream.getFD());
            mediaPlayerDuration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            retriever.release();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        LogUtils.i(TAG, "getAudioFileVoiceTime:" + (System.currentTimeMillis() - start));
        return mediaPlayerDuration;
    }

    /**
     * 通过mediaplayer
     *
     * @param filePath
     * @return
     */
    public static long getAudioDurationByMediaplayer(String filePath) {
        long mediaPlayerDuration = 0L;
        if (filePath == null || filePath.isEmpty()) {
            return 0;
        }
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayerDuration = mediaPlayer.getDuration();
        } catch (IOException ioException) {
            LogUtils.e(TAG, ioException.getMessage());
        } finally {
            try {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return mediaPlayerDuration;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }


    public static boolean isInstallApp(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO: handle exception
            return false;
        }
    }

    /**
     * 获取地区资源
     *
     * @param context       环境
     * @param desiredLocale 地区
     * @return 资源
     */
    @NonNull
    public static Resources getLocalizedResources(Context context, Locale desiredLocale) {
        Configuration conf = context.getResources().getConfiguration();
        conf = new Configuration(conf);
        conf.setLocale(desiredLocale);
        Context localizedContext = context.createConfigurationContext(conf);
        return localizedContext.getResources();

    }

    /**
     * 是否为中文语言环境
     *
     * @param context
     * @return
     */
    public static boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

}
