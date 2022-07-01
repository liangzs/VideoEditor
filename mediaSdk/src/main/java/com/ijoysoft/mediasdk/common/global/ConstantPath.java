package com.ijoysoft.mediasdk.common.global;

import android.os.Environment;

import com.ijoysoft.mediasdk.common.utils.FileUtils;
import com.ijoysoft.mediasdk.common.utils.TimeUtil;
import com.ijoysoft.mediasdk.module.entity.AudioMediaItem;

import java.io.File;

public class ConstantPath {
    public static String ROOTPATH = Environment.getExternalStorageDirectory().getPath();
    // 项目路径
    public static String APPPATH =
            ROOTPATH + File.separator + "ijoysoft" + File.separator + "videoEditor" + File.separator;
    // 视频文件合成的清单列表
    public static String MUERGEFILELIST =
            ROOTPATH + File.separator + "ijoysoft" + File.separator + "videoEditor" + File.separator + "filelist.txt";


    private static final String[]   reg = new String[] { "\\", ":", "*", "（","）","?", "<", ">",
            "|", " ", "、", "；", "，", "。", "‘", "'","/","(",")","&",";","{","}","$","'"};
    /**/

    /**
     * 对外部音乐进行再切割或合并
     * projectId+title+时间戳
     *
     * @return
     */
    public static String createAudioMurgePath(AudioMediaItem mediaItem) {

        String murgePath = APPPATH + mediaItem.getProjectId() + File.separator + "murge";

        FileUtils.createPath(murgePath);
        String title = mediaItem.getTitle();
        for (String s : reg) {
            title = title.replace(s, "");
        }
        return murgePath + File.separator + title + "_murge_preview_" + TimeUtil.getDayFormatTime() + ".mp3";
    }

    public static String createAudioMurgePathOffset(AudioMediaItem mediaItem) {
        String murgePath = APPPATH + mediaItem.getProjectId() + File.separator + "murge";
        FileUtils.createPath(murgePath);
        String title = mediaItem.getTitle();
        for (String s : reg) {
            title = title.replace(s, "");
        }
        return murgePath + File.separator + title + "_murge_preview_offset_" + TimeUtil.getDayFormatTime() + ".mp3";
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
    public static String createAudioVolumePath(String originPath) {
        String temPath = originPath.substring(0, originPath.indexOf(".mp3"));
        return temPath + "_step1_volume.mp3";
    }

    /**
     * 对视频元素进行音频抽取，转为mp3格式
     *
     * @return
     */
    public static String createExtractVideo2Audio(String projectId, String title) {
        String projetPath = APPPATH + projectId;
        FileUtils.createPath(projetPath);
        for (String s : reg) {
            title = title.replace(s, "");
        }
        return projetPath + File.separator + title + "_extract_audio_" + TimeUtil.getDayFormatTime() + ".mp3";
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

}
