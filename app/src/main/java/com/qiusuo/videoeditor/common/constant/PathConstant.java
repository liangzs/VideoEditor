package com.qiusuo.videoeditor.common.constant;

import android.os.Environment;

import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.FileUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.common.utils.TimeUtil;
import com.qiusuo.videoeditor.base.MyApplication;

import java.io.File;

/**
 * 路径管理，文件命名管理
 * 后续文件夹的创建判断，放到主mainactivity去提前做，省的每个都做判断
 *
 * @TODO 适配android11 路径分为两大类别，一种挂私有路径android/data下，另外一种挂公共路径document下
 */
public class PathConstant {
    /*****************************************应用私有路径***********************************************************/
    private static String adapterRootPath() {
        String storageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(storageDirectory + "/Android/data/" + MyApplication.instance.getPackageName() + "/files/");
        try {
            if (!file.exists() && !file.mkdirs()) {
                File externalCacheDir = MyApplication.instance.getExternalFilesDir((String) null);
                if (externalCacheDir != null) {
                    file = externalCacheDir;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath() + "/";
    }

    public static final String PRIVATE_ROOTPATH = adapterRootPath();
    // 项目路径
    public static final String APPTPATH =
            PRIVATE_ROOTPATH + "ijoysoft" + File.separator;
    // 涂鸦
    public static final String DOODLE = APPTPATH + "doodle" + File.separator;
    // trim裁剪路径
    public static final String VIDEO_TRIM = APPTPATH + "trim" + File.separator;
    /* 工作台路径 */
    public static final String MY_WORK_PATH_HISTOR = APPTPATH + "work" + File.separator;
    //音乐
    public static final String MUSIC = APPTPATH + "musicOnline";
    public static String[] INNER_PATH = new String[]{VIDEO_TRIM, MY_WORK_PATH_HISTOR, MUSIC + File.separator};
    public static final String VIDEO_LOG_PATH = APPTPATH + "log" + File.separator + "log.txt"; // 裁剪路径
    //下载工作目录
    public static final String workDir = MyApplication.instance.getFilesDir().getParentFile().getAbsolutePath();
    public static final String DEFAULT_MUSIC = MUSIC + "/music/general/Happy-19";

    /*****************************************应用公有路径***********************************************************/
    //dcument
    public static final String PUBLIC_ROOTPATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
    //movies
    public static final String PUBLIC_VIDEO_ROOTPATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath();

    public static final String PUBLIC_MUSIC_ROOTPATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();

    //record
    public static final String MY_WORK_PATH =
            PUBLIC_VIDEO_ROOTPATH + File.separator + "1PhotoVideoMakerTab" + File.separator;
    public static final String IMAGE_PATH =
            PUBLIC_ROOTPATH + File.separator + "Camera" + File.separator;
    public static final String VIDEO_PATH =
            PUBLIC_ROOTPATH + File.separator + "Video" + File.separator;
    public static final String[] reg = new String[]{"\\", ":", "*", "（", "）", "?", "<", ">", "|", " ", "、", "；", "，",
            "。", "‘", "'", "/", "(", ")", "&", ";", "{", "}", "$", "'"};


    public static final String MUSIC_PATH = PUBLIC_MUSIC_ROOTPATH + File.separator + "VideoMakerTab" + File.separator;


    /**
     * 原视频是什么格式，则吧保留什么格式！
     *
     * @return
     */
    public static String createVideoTrimPath(String projectId, String path) {
        String projetPath = APPTPATH + projectId + File.separator;
        FileUtils.createPath(projetPath);
        String suffix = path.substring(path.lastIndexOf("."));
        return projetPath + "trim_" + TimeUtil.getDayFormatTime() + suffix;
    }

    public static boolean checkIsInnerPath(String path) {
        path = path + File.separator;
        for (String str : INNER_PATH) {
            if (str.equals(path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 笔画涂鸦编辑显示路径
     * projectId+yyyyMMdd_HHmmss
     *
     * @return
     */
    public static String createDoodleEditPath() {
        String doodle = APPTPATH + "doodle" + File.separator;
        FileUtils.createPath(doodle);
        return doodle + "doodle_" + TimeUtil.getDayFormatTime();
    }

    /**
     * 笔画涂鸦路径命名规则
     * projectId+yyyyMMdd_HHmmss
     *
     * @return
     */
    public static String createDoodlePath(String projectId) {
        String doodle = APPTPATH + projectId + File.separator + "doodle" + File.separator;
        FileUtils.createPath(doodle);
        return doodle + "doodle_" + TimeUtil.getDayFormatTime();
    }

    /**
     * 文字涂鸦路径命名规则
     * projectId+yyyyMMdd_HHmmss
     *
     * @return
     */
    public static String createDoodleTextPath(String projectId) {
        String doodle = APPTPATH + projectId + File.separator + "textBitmap" + File.separator;
        FileUtils.createPath(doodle);
        return doodle + "textBitmap_" + TimeUtil.getDayFormatTime();
    }

    /**
     * 贴图涂鸦路径命名规则
     * projectId+yyyyMMdd_HHmmss
     *
     * @return
     */
    public static String createDoodleStickerPath(String projectId) {
        String doodle = APPTPATH + projectId + File.separator + "sticker" + File.separator;
        FileUtils.createPath(doodle);
        return doodle + "sticker_" + TimeUtil.getDayFormatTime();
    }

    public static String createBlankItemPath(String projectId) {
        String doodle = APPTPATH + projectId + File.separator + "blank" + File.separator;
        FileUtils.createPath(doodle);
        return doodle + "blank_" + TimeUtil.getDayFormatTime();
    }

    /**
     * 文件裁剪保存路径
     *
     * @return
     */
    public static String createCropPath(String projectId) {
        String crop = APPTPATH + projectId + File.separator + "crop" + File.separator;
        FileUtils.createPath(crop);
        return crop + "crop_" + TimeUtil.getDayFormatTime();
    }

    /**
     * 文件裁剪保存路径
     *
     * @return
     */
    public static String createInnerCropPath() {
        String stickerPath = MediaSdk.getInstance().getContext().getExternalFilesDir("sticker").getAbsolutePath();
        FileUtils.createPath(stickerPath);
        return stickerPath + File.separator + TimeUtil.getDayFormatTime();
    }


    /**
     * 音频裁剪路径命名规则
     * projectId/
     * title_yyyyMMdd_HHmmss
     *
     * @return
     */
    public static String createTrimAudioPath(String projectId) {
        String projetPath = APPTPATH + projectId + File.separator;
        FileUtils.createPath(projetPath);
        return projetPath + TimeUtil.getDayFormatTime() + "audio_cut_preview.mp3";
    }

    public static String createCustomOutpath(String name) {
        FileUtils.createPath(MY_WORK_PATH);
        // 检查文件是否存在
        String path = MY_WORK_PATH + name + ".mp4";
        if (FileUtils.checkFileExist(path)) {
            File file = new File(MY_WORK_PATH);
            String[] fileList = file.list();
            int index = 1;
            for (String str : fileList) {
                if (str.contains(name)) {
                    try {
                        String substr = str.substring(name.length(), str.lastIndexOf("."));
                        if (!ObjectUtils.isEmpty(substr)) {
                            int value = Integer.valueOf(substr);
                            index = value >= index ? value + 1 : index;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return MY_WORK_PATH + name + "" + index + ".mp4";
        }
        return MY_WORK_PATH + name + ".mp4";
    }

    public static String createMurgeOutputPath() {
        // return MY_WORK_PATH + "ijoysoft_" + projectId + "_" + TimeUtil.getDayFormatTime() + ".mp4";
        return MY_WORK_PATH + "Video_" + TimeUtil.getDayFormatTime(System.currentTimeMillis()) + "_by_photoVideoMaker"
                + ".mp4";
    }


    public static String getVideoTrimPath() {
        FileUtils.createPath(VIDEO_TRIM);
        return VIDEO_TRIM;
    }


    public static String change2TrimFormat(String originPath, String destpath) {
        String suffix = originPath.substring(originPath.lastIndexOf("."));
        destpath = destpath.replace(".mp4", suffix);
        return destpath;
    }

    public static String createCoverImage(String projectId) {
        String projetPath = APPTPATH + projectId + File.separator;
        FileUtils.createPath(projetPath);
        return projetPath + "draft_cover_" + TimeUtil.getDayFormatTime();
    }


    public static String getImageChildPath() {
        return "IMG_" + TimeUtil.getDayFormatTime() + ".jpg";
    }

    public static String getVideoChildPath() {
        return "VIDEO_" + TimeUtil.getDayFormatTime() + ".mp4";
    }

    public static String getVideoTrimAudioPath(String projectId) {
        String projetPath = APPTPATH + projectId + File.separator;
        FileUtils.createPath(projetPath);
        return projetPath + TimeUtil.getDayFormatTime() + "_video_trim.mp3";
    }


    public static String getVideoTrimSplitPathLeft() {
        return APPTPATH + "filelistLeft.txt";
    }

    public static String getVideoTrimSplitPathRight() {
        return APPTPATH + "filelistRight.txt";
    }


    /**
     * 去除空格
     *
     * @param path
     * @return
     */
    public static String musciPathDeal(String path) {
        return path.replace(" ", "");
    }


    /**
     * 输出地址
     *
     * @return
     */
    public static String createMp3outPath() {
        FileUtils.createPath(MUSIC_PATH);
        return MUSIC_PATH + TimeUtil.getDayFormatTime() + "video2mp3.mp3";
    }
}
