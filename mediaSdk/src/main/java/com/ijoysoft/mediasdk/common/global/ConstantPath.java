package com.ijoysoft.mediasdk.common.global;

import com.ijoysoft.mediasdk.common.utils.FileUtils;
import com.ijoysoft.mediasdk.common.utils.TimeUtil;
import com.ijoysoft.mediasdk.module.entity.AudioMediaItem;

import java.io.File;
import java.util.UUID;

public class ConstantPath {
    /*****************************************应用私有路径***********************************************************/
    public static String ROOTPATH = MediaSdk.getInstance().getContext().getExternalFilesDir(null).getAbsolutePath() + File.separator;
    // 项目路径
    public static String APPPATH =
            ROOTPATH + "ijoysoft" + File.separator;
    // 视频文件合成的清单列表
    public static String MUERGEFILELIST = APPPATH + "filelist.txt";
    // trim裁剪路径
    public static final String VIDEO_TRIM = APPPATH + "trim" + File.separator;

    public static final String[] reg = new String[]{"\\", ":", "*", "（", "）", "?", "<", ">", "|", " ", "、", "；", "，",
            "。", "‘", "'", "/", "(", ")", "&", ";", "{", "}", "$", "'"};


    /**
     * 对外部音乐进行再切割或合并
     * projectId+title+时间戳
     *
     * @return
     */
    public static String createAudioMurgePath(String projectId) {
        String murgePath = APPPATH + projectId + File.separator + "murge" + File.separator;
        FileUtils.createPath(murgePath);
        return murgePath + createUuid() + "_cut_again_preview.mp3";
    }

    public static String createAudioMurgePathOffset(String projectId) {
        String murgePath = APPPATH + projectId + File.separator + "murge" + File.separator;
        FileUtils.createPath(murgePath);
        return murgePath + createUuid() + "_cut_again_offset.mp3";
    }


    public static String createUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     * 获取合成片段路径，清除片段文件
     *
     * @return
     */
    public static String getMurgepath(String projectId) {
        return APPPATH + projectId + File.separator + "murge";
    }

    public static String getCurrentProjectPath(String projectId) {
        return APPPATH + projectId;
    }

    /**
     * 生成音量path
     *
     * @return
     */
    public static String createAudioVolumePath(String projectId) {
        String murgePath = APPPATH + projectId + File.separator + "murge" + File.separator;
        FileUtils.createPath(murgePath);
        return murgePath + createUuid() + "_step1_vol.mp3";
    }

    /**
     * 对视频元素进行音频抽取，转为mp3格式
     *
     * @return
     */
    public static String createExtractVideo2Audio(String projectId, String title) {
        title = title == null ? "" : title;
        String projetPath = APPPATH + projectId;
        FileUtils.createPath(projetPath);
        for (String s : reg) {
            title = title.replace(s, "");
        }
        return projetPath + File.separator + title + "_extract_audio_" + createUuid() + ".mp3";
    }

    /**
     * 生成静音文件
     *
     * @return
     */
    public static String createSilenceAudioPath(String projectId) {
        String projetPath = APPPATH + projectId;
        FileUtils.createPath(projetPath);
        return projetPath + File.separator + "silence_origin_audio_" + TimeUtil.getDayFormatTime() + ".mp3";
    }

    /**
     * 生成最终音频文件，该音频与视频合并后输出最终产品
     *
     * @return
     */
    public static String createAudioOutputPath(String projectId) {
        String projetPath = APPPATH + projectId;
        FileUtils.createPath(projetPath);
        return projetPath + File.separator + "audio_murge_end_" + TimeUtil.getDayFormatTime() + ".mp3";
    }

    /**
     * 生成最终音频文件，该音频与视频合并后输出最终产品
     *
     * @return
     */
    public static String createVideoOutputPath(String projectId) {
        String projetPath = APPPATH + projectId;
        FileUtils.createPath(projetPath);
        return projetPath + File.separator + "videoeditor_output_" + TimeUtil.getDayFormatTime() + ".mp4";
    }

    public static String getVideoTrimPath() {
        FileUtils.createPath(VIDEO_TRIM);
        return VIDEO_TRIM;
    }

    /**
     * 音频文件超多，进行concat的时候超标
     *
     * @return
     */
    public static String createMultiAudios() {
        FileUtils.createPath(VIDEO_TRIM);
        return VIDEO_TRIM + "audio_concat_" + TimeUtil.getNamillion() + ".mp3";
    }

    /**
     * 音频文件超多，进行concat的时候超标
     *
     * @return
     */
    public static String createMultiInsert() {
        FileUtils.createPath(VIDEO_TRIM);
        return VIDEO_TRIM + "audio_insert_" + TimeUtil.getDayFormatTime() + ".mp3";
    }

    /**
     * 改变速度文件
     *
     * @param projectId
     * @return
     */
    public static String createVideoSpeedPath(String projectId) {
        String projetPath = APPPATH + projectId + File.separator;
        FileUtils.createPath(projetPath);
        return projetPath + "speed_" + TimeUtil.getDayFormatTime() + ".mp3";
    }

}
