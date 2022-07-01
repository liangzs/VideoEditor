package com.ijoysoft.mediasdk.common.global;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.ijoysoft.mediasdk.module.entity.RatioType;


public class ConstantMediaSize {

    /***********************************尺寸比例***********************************/
    public static int currentScreenWidth;
    public static int currentScreenHeight;
    public static int showViewWidth;
    public static int showViewHeight;
    public static int showMainViewWidth;
    public static int showMainViewHeight;
    public static int offsetX;
    public static int offsetY;

    public static int minWidth;
    public static int screenWidth;
    public static int screenHeight;
    /**
     * 三种画幅的具体显示尺寸
     */
    public static int mode_por_width_9_16;
    public static int mode_por_height_9_16;
    public static int mode_por_width_1_1;
    public static int mode_por_height_1_1;
    public static int mode_por_width_16_9;
    public static int mode_por_height_16_9;


    public static float _3_4_RATIO = 0.75f;
    public static float _4_3_RATIO = 4 / 3;
    public static float _9_16_RATIO = 0.5625f;
    public static float _16_9_RATIO = 16 / 9;

    /**
     * 输出情况现在暂且考虑高清，标清、超清不考虑
     * 其实16:9可以采用960:540也未尝不可
     */
    public static final int[] HD_1_1 = new int[]{720, 720};
    public static final int[] HD_16_9 = new int[]{1280, 720};
    public static final int[] HD_9_16 = new int[]{720, 1280};
    public static final int[] HD_4_3 = new int[]{960, 720};
    public static final int[] HD_3_4 = new int[]{720, 960};


    /***************************************预览*********************************/
    //图片默认显示时间
    public static final int TIME_DURATION = 2000;
    //图片最大显示时长
    public static final int IMAGE_DURATION_MAX = 10;
    public static final int IMAGE_DURATION_MIN = 2;
    /**
     * 视频合成是，对应的帧数，预览的时候默认是25帧，一帧40ms
     */
    public static final int PHTOTO_FPS = 25;
    public static final int PHTOTO_FPS_TIME = 40;
    //转场动画时间
    public static final int TRANSITION_DURATION = 1800;
    /*缩略图宽高*/
    public static final int THUMTAIL_WIDTH = 80;  //200
    public static final int THUMTAIL_HEIGHT = 80;   //150
    //音频淡入、淡出
    public static final int FADE_IN_TIME = 1500; //1s 感觉太短了，加长点看看 3s 合适
    public static final int FADE_OUT_TIME = 1500;
    public static final int FADE_TIME = 3000;
    public static final int FADE_SECOND = 1;

    /*************************************合成*********************************/
    /*合成参数*/
    public static final int BIT_RATE = 3000000;
    public static final int FPS = 25;
    //纳秒
    public static final long ONE_BILLION = 1000000000;
    public static int heapSize;


    public static void init(Context context) {
        DisplayMetrics mDisplayMetrics = context.getResources()
                .getDisplayMetrics();
        @SuppressLint("WrongConstant") WindowManager wm = (WindowManager) context.getSystemService("window");
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;
        minWidth = Math.min(outMetrics.heightPixels, outMetrics.widthPixels);
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        heapSize = activityManager.getMemoryClass();
    }

    public static int getCurrentScreenWidth() {
        return currentScreenWidth == 0 ? 720 : currentScreenWidth;
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

}


