package com.ijoysoft.mediasdk.common.global;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.ijoysoft.mediasdk.module.entity.FrameType;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.InnerBorder;
import com.ijoysoft.mediasdk.module.opengl.particle.GlobalParticles;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeConstant;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeEnum;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionSeries;

public class ConstantMediaSize {

    /***********************************尺寸比例***********************************/
    /**
     * 加入volatile保证多线程一致性
     */
    public static volatile int canvasWidth;
    public static volatile int canvasHeight;
    public static volatile int showViewWidth;
    public static volatile int showViewHeight;
    public static volatile int offsetX;
    public static volatile int offsetY;

    public static volatile int minWidth;
    public static volatile int screenWidth;

    public static volatile int showMainViewWidth;
    public static volatile int showMainViewHeight;

    public static volatile int localBitmapWidth;
    public static volatile int localBitmapHeight;

    /**
     * 加入volatile保证多线程一致性
     */
    public static volatile RatioType ratioType = RatioType.NONE;

    public static volatile RatioType calcRatioType;
    //
    public static @ThemeConstant.ThemeType
    int themeConstantype = ThemeConstant.HOT;
    /**
     * 加入volatile保证多线程一致性
     */
    public static volatile ThemeEnum themeType = ThemeEnum.NONE;

    /**
     * 全局粒子系统
     */
    public static volatile GlobalParticles particles = GlobalParticles.NONE;

    /**
     * 全局边框
     */
    public static volatile InnerBorder innerBorder = InnerBorder.Companion.getNONE();

    /**
     * 转场系列
     */
    public static volatile TransitionSeries transitionSeries = TransitionSeries.NONE;

    /**
     * 加入volatile保证多线程一致性
     */
    public static volatile String themePath;
    public static String SUFFIX = "";
    public static final float MAX_VOLUME = 100f;

    public static float _3_4_RATIO = 0.75f;
    public static float _4_3_RATIO = 4f / 3f;
    public static float _9_16_RATIO = 0.5625f;
    public static float _16_9_RATIO = 16f / 9f;


    /***************************************预览*********************************/
    // 图片默认显示时间
    public static final int TIME_DURATION = 3000;
    public static int IMAGE_DURATION = 3000;
    public static final int VIDEO_TRIM = 2000;

    /**
     * 如果有主题的时候，视频的显示最多不过8s
     */
    public static final int THEME_VIDEO_DURATION = 5000;

    public static final int THEME_TIMETYPE_DURATION = 2000;
    // 图片最大显示时长
    public static final int IMAGE_DURATION_MAX = 10;
    public static final int IMAGE_DURATION_MIN = 2;

    public static final int TRANSITION_DURATION = 1800;
    public static final int TRANSITION_PREVIEW = 600;
    // 主题动画结束间断时间
    public static final int END_OFFSET = 1200;
    /* 缩略图宽高 */
    public static final int THUMTAIL_WIDTH = 100; // 200
    public static final int THUMTAIL_HEIGHT = 100; // 150
    // 音频淡入、淡出
    public static final int FADE_IN_TIME = 1500; // 1s 感觉太短了，加长点看看 3s 合适
    public static final int FADE_OUT_TIME = 1500;
    public static final int FADE_TIME = 3000;
    public static final int FADE_SECOND = 1;
    public static final RatioType THEME_DEFAULT_RATIO = RatioType._9_16;
    public static final int THEME_DEFAULT_RATIO_VALUE = RatioType._9_16.getKey();
    public static final int DEFAULT__RATIO = RatioType._16_9.getKey();
    public static final int DEFAULT_BG_BLUR = 50;
//    public static final int DEFAULT_BG_BLUR = 14;

    //采用_720p_去压缩图片
    public static final int[] _9_16 = new int[]{720, 1280};
    public static final int[] _16_9 = new int[]{1280, 720};
    public static final int[] _4_3 = new int[]{960, 720};
    public static final int[] _3_4 = new int[]{720, 960};
    public static final int[] _1_1 = new int[]{720, 720};
    /*************************************合成*********************************/
    /* 合成参数 */
//    public static final int BIT_RATE = 3000000;
    public static int FPS = 25;
    /**
     * 视频合成是，对应的帧数，预览的时候默认是25帧，一帧40ms
     */
    public static int PHTOTO_FPS_TIME = 40;
    // 转场动画时间
    // 纳秒
    public static final long ONE_BILLION = 1000000000;
    public static int heapSize;

    public static final int MIN_RECORD = 800;

    public static void init(Context context) {
        @SuppressLint("WrongConstant")
        WindowManager wm = (WindowManager) context.getSystemService("window");
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int screenW = outMetrics.widthPixels;
        screenWidth = screenW;
        int screenH = outMetrics.heightPixels;
        localBitmapWidth = Math.min(screenW, 1080);
        localBitmapHeight = Math.min(screenH, 1920);
        // 和输出的宽高进行对比
        minWidth = Math.min(outMetrics.heightPixels, outMetrics.widthPixels);
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        heapSize = activityManager.getMemoryClass();
    }


    /**
     * 设置预处理是对比的窗口
     *
     * @param width
     * @param height
     */
    public static void setExportWindow(int width, int height) {
        localBitmapWidth = width;
        localBitmapHeight = height;
    }

    public static void resetExportWindow() {
        WindowManager wm = (WindowManager) MediaSdk.getInstance().getContext().getSystemService("window");
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int screenWidth = outMetrics.widthPixels;
        int screenHeight = outMetrics.heightPixels;
        localBitmapWidth = Math.min(screenWidth, 1080);
        localBitmapHeight = Math.min(screenHeight, 1920);
    }

    public static int getHeapSize() {
        return heapSize;
    }

    public static float getRatioValue(RatioType ratioType) {
        if (ratioType == null) {
            return 1;
        }
        switch (ratioType) {
            case _16_9:
                return _16_9_RATIO;
            case _9_16:
                return _9_16_RATIO;
            case _3_4:
                return _3_4_RATIO;
            case _4_3:
                return _4_3_RATIO;

        }
        return 1;
    }


    public static int getMinWH() {
        int value = Math.min(showViewHeight, showViewWidth);
        value = value == 0 ? 720 : value;
        return value;
    }

    public static int getShowViewWidth() {
        return showViewWidth;
    }

    public static boolean isTheme() {
        return themeType != ThemeEnum.NONE;
    }

    /***
     *默认是25f，用户如果有修改，则进行修改
     * @param frameType
     */
    public static void setFpsFrame(FrameType frameType) {
        if (frameType == null) {
            return;
        }
        FPS = frameType.getFrame();
        PHTOTO_FPS_TIME = (int) Math.ceil(1000f / FPS);
    }

}
