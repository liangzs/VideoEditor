package com.ijoysoft.mediasdk.module.mediacodec;

import android.os.Build;

public class PhoneAdatarList {
    private static final String TAG = "   ";

    /* 视频合成兼容清单----针对合成最后一步时音视频合成无视频数据 */
    private static String[] phones = new String[]{"SM-G5500"};

    /**
     * 阿拉伯语言
     */
    public static String[] ARAB = new String[]{"ur_PK", "fa", "ar","iw_IL","ur_IN","ar_EG","ar_EG_#u-nu-latn","fa_IR"};
    public static String[] ARAB_TAB = new String[]{"ur", "fa", "ar","iw"};

    /**
     * popupwindow背景暗化导致surfaceview黑屏
     */
    private static String[] phones_background_dark =
            new String[]{"DLI-AL10", "TRT-TL10", "Redmi Note 5A", "AUM-AL00", "KOB-W09", "vivo Y69A"};

    /**
     * 系统mediaplayer对此格式支持不好，崩溃
     */
    public static String[] video_query_filter =
            new String[]{".wmv", ".mod", ".rmvb", ".vob", ".mpg", ".flv", "avi"};
    /**
     * 系统mediaplayer对此格式支持不好，崩溃
     */
    public static String[] audio_query_filter = new String[]{".flv"};

    /**
     * 对于有些手机，音视频合成的时候，音频编码复制时竟然会发生变音即频率发生变化。
     * 所以针对这些手机，进行过滤。
     */
    public static String[] video_audio_murge = new String[]{"V1732A"};
    /**
     * 进入渲染页面中，如果没有自动播放，虽然渲染了两次，都没有画面渲染出来，所以对于这些来数
     */
    public static String[] alway_auto_play_video = new String[]{"OPPO A53m"};

    /**
     * 有部分手机，进入子页面然后切换回来的时候，gl.viewport()没有效果，导致形状拉伸
     */
    public static String[] background_filter_shape = new String[]{"DUK-AL20"};
    /**
     * 针对一些机型过滤avi，flv格式文件
     */
    public static String[] video_query_filter_avi_flv = new String[]{};

    /**
     * 动态改变
     */
    // static {
    // if (isHuawei()) {
    // video_query_filter = new String[] { ".wmv", ".mod", ".rmvb", ".vob", ".mpg", ".avi", ".flv" };
    // }
    // }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    public static boolean videoAudioMurgeList() {
        String phoneType = getSystemModel();
        for (String string : phones) {
            if (string.equals(phoneType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCloseBackgroundDark() {
        // if (Build.VERSION.SDK_INT <= 23) {
        // return false;
        // }
        // for (String string : phones_background_dark) {
        // String phoneType = getSystemModel();
        // Log.d("isCloseBackgroundDark", "phoneType===" + phoneType);
        // if (string.equals(phoneType)) {
        // return false;
        // }
        // }
        return true;
    }

    /**
     * 视频过滤
     *
     * @param path
     * @return
     */
    public static boolean checkIsFilterVideo(String path) {
        for (String string : video_query_filter) {
            if (path.toLowerCase().endsWith(string)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 音频过滤过滤
     *
     * @param path
     * @return
     */
    public static boolean checkIsFilterAudio(String path) {
        for (String string : audio_query_filter) {
            if (path.toLowerCase().endsWith(string)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isHuawei() {
        String manufacturer = Build.MANUFACTURER;
        // 这个字符串可以自己定义,例如判断华为就填写huawei,魅族就填写meizu
        if ("huawei".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }

    public static boolean checkMurgeAudioToneChange() {
        String phoneType = getSystemModel();
        for (String string : video_audio_murge) {
            if (string.equals(phoneType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkAlwayAutoPlayVideo() {
        String phoneType = getSystemModel();
        for (String string : alway_auto_play_video) {
            if (string.equals(phoneType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkSwithShape() {
        String phoneType = getSystemModel();
        for (String string : background_filter_shape) {
            if (string.equals(phoneType)) {
                return true;
            }
        }
        return false;
    }

}
