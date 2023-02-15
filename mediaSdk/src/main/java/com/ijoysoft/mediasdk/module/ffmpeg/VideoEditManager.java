package com.ijoysoft.mediasdk.module.ffmpeg;

import androidx.annotation.FloatRange;
import androidx.core.math.MathUtils;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.ConstantPath;
import com.ijoysoft.mediasdk.common.utils.FileUtils;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.common.utils.TimeUtil;
import com.ijoysoft.mediasdk.module.entity.AudioDuration;
import com.ijoysoft.mediasdk.module.entity.AudioInsertItem;
import com.ijoysoft.mediasdk.module.entity.TransationType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * 上层调用ffmpeg总类
 */
public class VideoEditManager {
    private static final String TAG = "VideoEditManager";

    /**
     * 直接用concat来合成ts文件，这样就不用考虑不同编码的情况问题了
     * ffmpeg -i "concat:input1|input2" -codec copy output
     *
     * @param srcPath  输入
     * @param destFile 输出
     */
    public static void videoMurgeFromConcat(String destFile, CallCmdListener listener, String... srcPath) {
        StringBuilder pathIn = new StringBuilder("concat:");
        for (int i = 0; i < srcPath.length; i++) {
            pathIn.append(pathIn.toString()).append(srcPath);
            if (i < srcPath.length - 1) {
                pathIn.append("|");
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("ffmpeg");
        sb.append(" -i");
        sb.append(" ").append(pathIn);
        sb.append(" -codec");
        sb.append(" copy");
        sb.append(" ").append(destFile);
        String commandStr = sb.toString();
        LogUtils.i(TAG, "VideoMurgeFromFilelist->commandStr:" + commandStr);
        FFmpegCmd.getInstance().execute(commandStr.split(" "), listener);
    }

    /**
     * 从文件中读取合成文件的清单
     * ffmpeg -y -f concat -safe 0 -i file.txt -c copy result.mp4
     * ffmpeg -y -f concat -safe 0 -i file.txt result.mp4 //有的文件会报错，比如和无音频的或者格式不一样的文件，如果直接复制编码会报错，所以去掉编码复制
     *
     * @param srcPath  输入地址
     * @param destFile 输出地址
     */
    public static String[] videoMurgeFromFilelist(String customPath, String destFile, boolean isExecute, boolean isCopy,
                                                  CallCmdListener listener, String... srcPath) {
        if (srcPath.length < 1) {
            return new String[]{};
        }
        FileUtils.checkOrCreateParentFolder(destFile);
        boolean isDiffer = false;
        int suffixIndex = srcPath[0].lastIndexOf(".");
        String suffix;
        if (suffixIndex != -1) {
            suffix = srcPath[0].substring(suffixIndex);
        } else {
            suffix = ".mp3";
        }

        for (String string : srcPath) {
            if (!string.endsWith(suffix)) {
                isDiffer = true;
                break;
            }
        }
        String resultPath = ConstantPath.MUERGEFILELIST;
        if (customPath != null && !customPath.isEmpty()) {
            resultPath = customPath;
        }
        FileUtils.insertVideoFileName(resultPath, srcPath);

        String[] commands = new String[]{"ffmpeg", "-d", "-y", "-f", "concat",
                "-safe", "0", "-i", resultPath, "-c", "copy", destFile};

        if (isDiffer | isCopy) {
            commands = new String[]{"ffmpeg", "-d", "-y", "-f", "concat",
                    "-safe", "0", "-i", resultPath, destFile};
        }
        if (isExecute) {
            FFmpegCmd.getInstance().execute(commands, listener);
        }
        return commands;
    }


//    public static void

    /**
     * 视频合成，考虑不同编码以及涂鸦等用到overlay的情况下，都统一转场这种格式，方便多视频合成
     * ffmpeg.exe -i input.mp4 -codec copy -bsf h264_mp4toannexb -r 25 -s 720x1280
     * -pix_fmt nv21 -ar 44100 -ac 1 -analyzeduration 500 -vcodec libx264 -profile:v baseline -preset
     * ultrafast -b:v 4m -g 30 -acodec libfdk_aac -b:a 512k -f mpegts output.ts
     *
     * @param inputPath 输入地址
     * @param outPath   输出地址
     * @param listener  监听
     */
    public static void concatBeforeChangeCode(String inputPath, String outPath, int width, int height,
                                              CallCmdListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append("ffmpeg");
        sb.append(" -i");
        sb.append(" ").append(inputPath);
        sb.append(" -codec copy");
        sb.append(" -bsf h264_mp4toannexb");
        sb.append(" -r 25");
        sb.append(" -s");
        sb.append(" ").append(width);
        sb.append(" x").append(height);
        sb.append(" -pix_fmt nv21");
        sb.append(" -ar 44100");
        sb.append(" -ac 1");
        sb.append(" -analyzeduration 500");
        sb.append(" -pix_fmt nv21");
        sb.append(" -vcodec libx264");
        sb.append(" -profile:v");
        sb.append(" baseline");
        sb.append(" -preset ultrafast");
        sb.append(" -b:v 4m");
        sb.append(" -g 30");
        sb.append(" -acodec libmp3lame");
        sb.append(" -b:a 512k");
        sb.append(" -f mpegts");
        sb.append(" ").append(outPath);
        String commandStr = sb.toString();
        LogUtils.i(TAG, "VideoMurgeFromFilelist->commandStr:" + commandStr);
        FFmpegCmd.getInstance().execute(commandStr.split(" "), listener);
    }

    /**
     * ffmpeg -i videoPath -i imagePath -filter_complex overlay=0:0 -vcodec libx264 -profile:v baseline -preset ultrafast -b:v 3000k -g 30 -f mp4 outPath
     * 在视频上部添加涂鸦层
     * filter_complex后面不需要加引号，不然在android平台上是会出错的
     *
     * @param videoPath 视频地址
     * @param destPath  输出地址
     */
    public static void murgeScrawl(String videoPath, String doodlePath, String destPath, CallCmdListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append("ffmpeg");
        sb.append(" -i");
        sb.append(" ").append(videoPath);
        sb.append(" -i");
        sb.append(" ").append(doodlePath);
        sb.append(" -filter_complex");
        sb.append(" overlay=0:0");
        sb.append(" -vcodec libx264 -profile:v baseline -preset ultrafast -b:v 3000k -g 25");
        sb.append(" -threads");
        sb.append(" 5");
        sb.append(" -preset");
        sb.append(" ultrafast");
        sb.append(" -f mp4");
        sb.append(" ").append(destPath);
        String commandStr = sb.toString();
        LogUtils.i(TAG, "VideoMurgeFromFilelist->commandStr:" + commandStr);
        FFmpegCmd.getInstance().execute(commandStr.split(" "), listener);

    }

    /**
     * 裁剪音频
     * 发现空格路径,只能是切换成数组的方法，因为用stringbuild不管是用单引号还是双引号包含路径都没有用，
     * 而在window是单引用和双引用都是有效的，这是android上的区别的，android上引用都是会有错误的
     * 精确到毫秒级别
     * 由于自己的失误，-t是持续时间，-to是到指定时间，应该用的时候-to的
     * ffmpeg  -i flac.flac -vn -acodec copy -ss 00:00:00 -to 00:00:16.347 -y  150050638preview.mp3
     * ffmpeg  -i flac.flac -vn -ss 00:00:00 -to 00:00:16.347 -y  150050638preview.mp3
     *
     * @param intputPath 音频源
     * @param destPath   结果存储地址
     * @param startTime  开始时间 00:00:00.000  格式调用timeUtil
     * @param endTime    结束时间00:00:00.000
     * @param listener   监听
     */
    public static String[] cutAudio(String intputPath, String destPath, String startTime, String endTime,
                                    CallCmdListener listener, boolean isExecute) {
        String[] commands = new String[]{"ffmpeg", "-d", "-i", intputPath, "-vn", "-acodec", "copy", "-ss", startTime,
                "-to", endTime, "-y", destPath};
        if (isExecute) {
            FFmpegCmd.getInstance().execute(commands, listener);
        }
        return commands;
    }

    /**
     * 裁剪音频
     * 发现空格路径,只能是切换成数组的方法，因为用stringbuild不管是用单引号还是双引号包含路径都没有用，
     * 而在window是单引用和双引用都是有效的，这是android上的区别的，android上引用都是会有错误的
     * 精确到毫秒级别
     * 由于自己的失误，-t是持续时间，-to是到指定时间，应该用的时候-to的
     * ffmpeg  -i flac.flac -vn -acodec copy -ss 00:00:00 -to 00:00:16.347 -y  150050638preview.mp3
     * ffmpeg  -i flac.flac -vn -ss 00:00:00 -to 00:00:16.347 -y  150050638preview.mp3
     *
     * @param intputPath 音频源
     * @param destPath   结果存储地址
     * @param startTime  开始时间 00:00:00.000  格式调用timeUtil
     * @param endTime    结束时间00:00:00.000
     * @param listener   监听
     */
    public static String[] cutAudioLossle(String intputPath, String destPath, String startTime, String endTime,
                                          int duration, CallCmdListener listener, boolean isExcute) {
        String[] commands = new String[]{"ffmpeg", "-d", "-i", intputPath, "-vn", "-ss", startTime, "-to", endTime,
                "-y", destPath};
        if (isExcute) {
            FFmpegCmd.getInstance().executeProgress(commands, listener, duration);
        }
        return commands;
    }

    /**
     * 修改音频声音
     * 0.5小0.5倍，2则大两倍，-5dp减少5db，+5dp加5db
     * 由于合成的声音变小，所以在原有幅度上增幅10db
     *
     * @param audioOrMusicUrl 音频源
     * @param outUrl          输出地址
     * @param listener        监听回调
     */
    public static String[] changeAudioVolome(String audioOrMusicUrl, float vol, String outUrl, CallCmdListener listener,
                                             boolean isExecute) {
        FileUtils.checkOrCreateParentFolder(outUrl);
        String[] commands = new String[]{"ffmpeg", "-d", "-i", audioOrMusicUrl, "-af"
                , "volume=" + vol + "[1];[1]volume=5dB", outUrl};

//        LogUtils.i(TAG, "command:" + Arrays.toString(command));
        if (isExecute) {
            FFmpegCmd.getInstance().execute(commands, listener);
        }
        return commands;
    }

    /**
     * 修改音频声音
     *
     * @param audioOrMusicUrl 音频源
     * @param vol             0-256
     * @param outUrl          输出地址
     * @param listener        监听回调
     */
    public static void changeAudioVol(String audioOrMusicUrl, float vol, String outUrl, CallCmdListener listener) {
        LogUtils.w(TAG, "audioOrMusicUrl:" + audioOrMusicUrl + "\nvol:" + vol + "\noutUrl:" + outUrl);
        FileUtils.checkOrCreateParentFolder(outUrl);
        StringBuilder sb = new StringBuilder("ffmpeg");
        sb.append(" -i");
        sb.append(" ").append(audioOrMusicUrl);
        sb.append(" -vol");
        sb.append(" ").append(vol);
        sb.append(" -acodec");
        sb.append(" copy");
        sb.append(" ").append(outUrl);
        String command = sb.toString();
        LogUtils.i(TAG, "command:" + command);
        FFmpegCmd.getInstance().execute(command.split(" "), listener);
    }

    /**
     * 音频和视频合成
     * <p>
     * sb.append(" -movflags");
     * sb.append(" faststart");
     * <p>
     * <p>
     * sb.append(" -strict");
     * sb.append(" experimental");
     * <p>
     * -strict experimental
     *
     * @param videoUrl     视频源
     * @param musicOrAudio 音频源
     * @param outputUrl    输出地址
     * @param listener     监听
     */
    public static String[] composeVideoAudio(String videoUrl, String musicOrAudio, String outputUrl,
                                             CallCmdListener listener, boolean isExcute) {
        String[] commands = new String[]{"ffmpeg", "-d", "-i", videoUrl, "-i", musicOrAudio, "-vcodec", "copy", "-threads"
                , "5", "-movflags", "faststart", "-shortest", "-y", outputUrl};
        if (isExcute) {
            FFmpegCmd.getInstance().execute(commands, listener);
        }
        return commands;
    }

    /**
     * 两个音频合并
     *
     * @param audio1    音频1
     * @param audio2    音频2
     * @param outputUrl 输出地址
     * @param listener  监听
     */
    public static void composeAudio(String audio1, String audio2, String outputUrl, CallCmdListener listener) {
        LogUtils.w(TAG, "audio1:" + audio1 + "\naudio2:" + audio2 + "\noutputUrl:" + outputUrl);
        StringBuilder sb = new StringBuilder("ffmpeg");
        sb.append(" -d");
        sb.append(" -i");
        sb.append(" ").append(audio1);
        sb.append(" -i");
        sb.append(" ").append(audio2);
        sb.append(" -filter_complex");
        sb.append(" amix=inputs=2:duration=first:dropout_transition=2");
        sb.append(" -threads");
        sb.append(" 5");
        sb.append(" -preset");
        sb.append(" ultrafast");
        sb.append(" -strict");
        sb.append(" -2");
        sb.append(" ").append(outputUrl);
        String command = sb.toString();
        LogUtils.i(TAG, "command:" + command);
        FFmpegCmd.getInstance().execute(command.split(" "), listener);
    }

    /**
     * 多个音频合并
     * http://ffmpeg.org/ffmpeg-filters.html#amix
     * duration
     * How to determine the end-of-stream.
     * longest
     * The duration of the longest input. (default)
     * shortest
     * The duration of the shortest input.
     * first
     * The duration of the first input.
     *
     * @param outputUrl 输出地址
     * @param listener  监听
     */
    public static String[] anixMultiAudio(String outputUrl, boolean isExecute, CallCmdListener listener, String... paths) {
        if (paths == null || paths.length < 2) {
            return null;
        }
        FileUtils.checkOrCreateParentFolder(outputUrl);
        int length = paths.length;
        StringBuilder sb = new StringBuilder("ffmpeg");
        sb.append(" -d");
        for (String path : paths) {
            sb.append(" -i");
            sb.append(" ").append(path);
        }
        sb.append(" -filter_complex");
        sb.append(" amix=inputs=" + length + ":duration=longest:dropout_transition=" + length);
        sb.append(" -y");
        sb.append(" ").append(outputUrl);
        String command = sb.toString();


        String[] commands = command.split(" ");
        if (isExecute) {
            FFmpegCmd.getInstance().execute(commands, listener);
        }
        return commands;
    }

    /**
     * 多个音频拼接,用了-filter-complex，记得不要用空格和双引号
     * ffmpeg -i 1.mp3 -i 2.mp3 -i 3.mp3 -filter_complex '[0:0][1:0][2:0]concat=n=3:v=0:a=1[a]' -map [a] result1.mp3
     * <p>
     * <p>
     * ---备用ffmpeg -i "concat:input1.mpg|input2.mpg|input3.mpg" -c copy output.mpg
     *
     * @param outputUrl 输出地址
     * @param listener  监听
     */
    public static List<String[]> composeMultiAudio(String outputUrl, CallCmdListener listener, boolean isExcute,
                                                   String... src) {
        FileUtils.checkOrCreateParentFolder(outputUrl);
        List<String[]> listCmd = new ArrayList<>();
        // 这里出现一个问题,concat连接的长度不能超过1024，如果超过了1024则需要进行分包合成
        int len = src.length;
        StringBuilder sb = new StringBuilder();
        List<StringBuilder> list = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < len; i++) {
            sb.append(src[i]);
            count++;
            if (sb.length() > 600) {
                list.add(sb);
                sb = new StringBuilder();
                count = 0;
                continue;
            }
            if (i < len - 1) {
                sb.append("|");
            }
        }
        if (list.size() > 0) {
//            System.out.println("1..");
            List<String> paths = new ArrayList<>();
            if (count == 0 && list.size() == 1) {//刚好凑整，0个
                String[] command = composeMultiAudioConcat(list.get(0).toString(), outputUrl);
                listCmd.add(command);
                if (isExcute) {
                    FFmpegCmd.getInstance().execute(command, listener);
                }
//                System.out.println("0..");
                return listCmd;
            } else if (count == 1) {//只有一个
                paths.add(sb.toString());
//                System.out.println("1.." );
            } else if (count > 1) {//还是有多个，得重新compose
//                System.out.println("多个.." );
                list.add(sb);
            }
            String path;
            for (int i = 0; i < list.size(); i++) {
//                path = "--------------ConstantPath.createMultiAudios()-------" + i + "--------";
//                path = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.mp2";
                path = ConstantPath.createMultiAudios();
                paths.add(path);
                String[] command = composeMultiAudioConcat(list.get(i).toString(), path);
                listCmd.add(command);
                if (isExcute) {
                    FFmpegCmd.getInstance().execute(command, listener);
                }
            }
            //用生成的再拼接-级联
            listCmd.addAll(composeMultiAudio(outputUrl, null, false, paths.toArray(new String[paths.size()])));
        } else {
            String[] command = composeMultiAudioConcat(sb.toString(), outputUrl);
            listCmd.add(command);
            if (isExcute) {
                FFmpegCmd.getInstance().execute(command, listener);
            }
        }
        return listCmd;
    }

    private static String[] composeMultiAudioConcat(String concat, String output) {

        return new String[]{"ffmpeg", "-d", "-i", "concat:" + concat, "-c", "copy", output};
//        StringBuilder sb = new StringBuilder("ffmpeg -d -i concat:");
//        sb.append(concat);
//        sb.append(" -c copy");
//        sb.append(" ").append(output);
////        LogUtils.i(TAG, "command:" + sb.toString());
//        return sb.toString().split(" ");
    }

    /**
     * 抽取视频
     * ffmpeg -i -vcodec copy -an -y output.mp4
     *
     * @param videoUrl 视频地址
     * @param outUrl   输出地址
     * @param listener 监听
     */
    public static void extractVideo(String videoUrl, String outUrl, CallCmdListener listener) {
        StringBuilder sb = new StringBuilder("ffmpeg");
        sb.append(" -d");
        sb.append(" -i");
        sb.append(" ").append(videoUrl);
        sb.append(" -vcodec");
        sb.append(" copy");
        sb.append(" -an");
        sb.append(" -y");
        sb.append(" ").append(outUrl);
        String command = sb.toString();
        LogUtils.i(TAG, "command:" + command);
        FFmpegCmd.getInstance().execute(command.split(" "), listener);
    }

    /**
     * 提取单独的音频
     *
     * @param videoUrl 音频地址
     * @param outUrl   输出
     */
    public static void extractAudioAac(String videoUrl, String outUrl, CallCmdListener listener) {
        StringBuilder sb = new StringBuilder("ffmpeg");
        sb.append(" -d");
        sb.append(" -i");
        sb.append(" ").append(videoUrl);
        sb.append(" -acodec");
        sb.append(" copy");
        sb.append(" -vn");
        sb.append(" -y");
        sb.append(" ").append(outUrl);
        String command = sb.toString();
        LogUtils.i(TAG, "command:" + command);
        FFmpegCmd.getInstance().execute(command.split(" "), listener);
    }

    /**
     * 提取单独的音频
     * ffmpeg -i input.mp4 -c:a  libmp3lame -vn -y ouput.mp3
     *
     * @param videoUrl 音频地址
     * @param outUrl   输出
     */
    public static String[] extractAudioMp3(String videoUrl, String outUrl, CallCmdListener listener, boolean isExcute) {
        FileUtils.checkOrCreateParentFolder(outUrl);
        String[] commands = new String[]{"ffmpeg", "-i", videoUrl, "-c:a", "libmp3lame", "-vn", "-y", outUrl};
        if (isExcute) {
            FFmpegCmd.getInstance().execute(commands, listener);
        }
        return commands;
    }

    /**
     * 提取单独的音频并做裁剪动作
     * ffmpeg -i input.mp4 -c:a  libmp3lame -vn -y ouput.mp3
     *
     * @param videoUrl 音频地址
     * @param outUrl   输出
     */
    public static String[] extractAudioMp3byTrim(String videoUrl, String outUrl, String startTime, String endTime) {
        FileUtils.checkOrCreateParentFolder(outUrl);
        String[] commands = new String[]{"ffmpeg", "-d", "-i", videoUrl, "-c:a", "libmp3lame", "-vn",
                "-ss", startTime,
                "-to", endTime, "-y", outUrl};
        return commands;
    }

    /**
     * 调整视频+音频播放速度
     */
    public static void adjustVideoSpeed(String path, float speed, String output, CallCmdListener listener) {
        FileUtils.checkOrCreateParentFolder(output);
        String filter = String.format(Locale.ENGLISH, "[0:v]setpts=%f*PTS[v];[0:a]atempo=%f[a]", 1 / speed, speed);
        String[] commands = new String[]{"ffmpeg", "-i", path, "-filter_complex"
                , filter, "-map", "[v]", "-map", "[a]", "-y", output};
        FFmpegCmd.getInstance().execute(commands, listener);
    }

    /**
     * 调整音频播放速度
     * atempo 范围是0.5-2
     * 如果超过2倍，则重新乘其乘2的值
     * ffmpeg -i input.mkv -filter:a "atempo=2.0,atempo=2.0" -vn output.mkv
     */
    public static String[] adjustAudioSpeed(String path, @FloatRange(from = 0.25f, to = 4f) float speed, String output, boolean isExcute, CallCmdListener listener) {
        //0.5-2.0
        String[] commands = new String[]{"ffmpeg", "-i", path, "-filter:a", "atempo=" + speed, "-vn", output};
        if (speed > 2.0) {
            commands = new String[]{"ffmpeg", "-i", path, "-filter:a", "atempo=2.0", "atempo=" + TimeUtil.getDecimalTwo(speed / 2.0f), "-vn", output};
        }
        if (speed < 0.5f) {
            commands = new String[]{"ffmpeg", "-i", path, "-filter:a", "atempo=0.5", "atempo=" + TimeUtil.getDecimalTwo(speed / 0.5f), "-vn", output};
        }
        if (isExcute) {
            FFmpegCmd.getInstance().execute(commands, listener);
        }
        return commands;
    }

    /**
     * 图片合成视频，默认是5秒视频
     * ffmpeg -loop 1 -f image2 -i input.jpg -vcodec libx264 -r 10 -t 10 test.mp4
     *
     * @param imputPath 图片地址
     * @param output    输出地址
     * @param duration  时长
     * @param listener  监听
     */
    public static void murgeVideoPhoto(String imputPath, String output, int duration, CallCmdListener listener) {
        StringBuilder sb = new StringBuilder("ffmpeg");
        sb.append(" -loop");
        sb.append(" l");
        sb.append(" -f");
        sb.append(" image2");
        sb.append(" -i");
        sb.append(" ").append(imputPath);
        sb.append(" -vcodec");
        sb.append(" libx264");
        sb.append(" -r");
        sb.append(" 10");
        sb.append(" -t");
        sb.append(" -threads");
        sb.append(" 5");
        sb.append(" -preset");
        sb.append(" ultrafast");
        sb.append(" ").append(duration);
        sb.append(" ").append(output);
        String command = sb.toString();
        LogUtils.i(TAG, "command:" + command);
        FFmpegCmd.getInstance().execute(command.split(" "), listener);
    }

    /**
     * ffmpeg -i jpg/last_frame.jpg  -i gif_temp.gif -filter_complex "overlay" -y gif_temp_1.gif
     * 转场分两种，一种是结合最后一帧，和第一帧进行视频合成，视频不随着转场的过程进行播放
     * 第二种是视频本身是转场的一部分，转场的过程中，视频是在播放的
     * 现在采用第一种方案，就是把转场生成2s的视频文件，然后生成是采用最后一帧和第一帧进行过度的
     */
    public static void murgeTransationGif(String lastFramePath, String outputPath, TransationType transationType) {
        switch (transationType) {
            case MOVE_LEFT:
            case MOVE_RIGHT:
            case MOVE_TOP:
            case MOVE_BOTTOM:
                StringBuilder sb = new StringBuilder("ffmpeg");
                sb.append(" -i");
                sb.append(" ").append(lastFramePath);
                sb.append(" -filter_complex \"overlay\"");
                sb.append(" -y");
                sb.append(" 10");
                sb.append(" -t 2");
                sb.append(" ").append(outputPath);
                String command = sb.toString();
                LogUtils.i(TAG, "command:" + command);
                FFmpegCmd.getInstance().execute(command.split(" "), null);
                break;
            default:
                break;
        }
    }

    /**
     * 平移-此gif需要上文件的尾帧，是的平移的时候，底片显示不是白色
     * 尾帧和头帧进行合成一个2s视频
     * ffmpeg -i jpg/last_frame.jpg -i jpg/first_frame.jpg -i gif_temp.gif -filter_complex "[1:v] [2:v] overlay [cmask];
     * [cmask] format = rgba,chromakey = 0x0000FF:0.3 [ckout]; [0:v] [ckout] overlay" -t 2 -y two_imag_transition.mp4
     * <p>
     * ffmpeg -i project_id_001_null1.jpg -i project_id_001_null2.jpg -i left.gif -filter_complex
     * "[2]scale=720x1280[gifout];[1:v][gifout]overlay[cmask];[cmask]format=rgba,chromakey=0x0000FF:0.3[ckout];[0:v][ckout]overlay" -t 2 -y test.mp4
     * <p>
     *
     * @param firstImagePath  第一帧图片
     * @param secondImagePath 第二帧图片
     * @param transationType  转场类型
     * @param listener        监听
     */
    public static void murgeTransationbyFrames(String firstImagePath, String secondImagePath,
                                               TransationType transationType, String outputPath, int width, int height, CallCmdListener listener) {
        String filter = String.format(Locale.ENGLISH,
                "[2]scale=%dx%d[gifout];[1:v][gifout]overlay[cmask];[cmask]format=rgba,chromakey=0x0000FF:0.3[ckout];[0:v][ckout]overlay",
                width, height);
        switch (transationType) {
            case MOVE_LEFT:
            case MOVE_RIGHT:
            case MOVE_TOP:
            case MOVE_BOTTOM:
                StringBuilder sb = new StringBuilder("ffmpeg");
                sb.append(" -i");
                sb.append(" ").append(secondImagePath);
                sb.append(" -i");
                sb.append(" ").append(firstImagePath);
                sb.append(" -i");
                sb.append(" ").append(FileUtils.getTransationPath(transationType));
                sb.append(" -filter_complex");
                sb.append(" ").append(filter);
                sb.append(" -threads");
                sb.append(" 5");
                sb.append(" -preset");
                sb.append(" ultrafast");
                sb.append(" -t");
                sb.append(" 2");
                sb.append(" -y");
                sb.append(" ").append(outputPath);
                String command = sb.toString();
                LogUtils.i(TAG, "command:" + command);
                FFmpegCmd.getInstance().execute(command.split(" "), listener);
                break;
            case FADE_IN:
            case FADE_OUT:
                murgeTransationbyFade(secondImagePath, outputPath, listener);
                break;
            default:
                break;
        }
    }

    /**
     * 通过fading合成转场
     * 如果是淡出淡出，或者其他效果，则不需要前个文件的底片，展示的效果也正常
     * ffmpeg -loop 1 -i input1.jpg -r 30 -filter_complex "[0:v] fade=in:0:60" -c:v libx264 -pix_fmt yuv420p  -t 2  -y  test.mp4
     *
     * @param firstImagePath 图片源
     * @param outputPath     输出地址
     * @param listener       监听
     */
    public static void murgeTransationbyFade(String firstImagePath, String outputPath, CallCmdListener listener) {
        FileUtils.checkOrCreateParentFolder(outputPath);
        StringBuilder sb = new StringBuilder("ffmpeg");
        sb.append(" -loop");
        sb.append(" 1");
        sb.append(" -i");
        sb.append(" ").append(firstImagePath);
        sb.append(" -r");
        sb.append(" 30");
        sb.append(" -filter_complex");
        sb.append(" fade=in:0:60");
        sb.append(" -c:v");
        sb.append(" libx264");
        sb.append(" -pix_fmt");
        sb.append(" yuv420p");
        sb.append(" -threads");
        sb.append(" 5");
        sb.append(" -preset");
        sb.append(" ultrafast");
        sb.append(" -t");
        sb.append(" 2");
        sb.append(" -y");
        sb.append(" ").append(outputPath);
        String command = sb.toString();
        LogUtils.i(TAG, "command:" + command);
        FFmpegCmd.getInstance().execute(command.split(" "), listener);
    }

    /**
     * //完成目标，再任意位置添加音频，并且默认是淡入淡出功能
     * ffmpeg -i input_10s.mp4 -i 4smp3.mp3 -filter_complex "[1:0] adelay=2000|2000 ,afade=t=in:ss=0:d=3,afade=t=out:st=3:d=3
     * [delay];[0:1][delay] amix=inputs=2"   -c:a aac -strict -2  -c:v copy  -y  result/test.mp4
     * 2000|2000的意思是左声道和右声道都延迟两秒
     * %d不是d%,
     * <p>
     * ffmpeg -i input_10s.mp4 -i 4smp3.mp3 -filter_complex "[1:0] adelay=2000|2000 ,afade=d=3,areverse,afade=d=3, areverse [delay];[0:1][delay] amix=inputs=2" -c:a aac -strict -2  -c:v copy  -y  result/test.mp4
     * 这个有可能有问题，如果filter和-c copy一起用的话会报错，解决方案是音量的淡入重现生成文件在合并，或者-c:a copy 换成-c:a aac -strict -2
     *
     * @param start   开始时间
     * @param end     结束时间
     * @param outPath 输出地址
     */
    public static void insertAudiobyLocation(String videoPath, String audioPath, int start, int end, String outPath,
                                             CallCmdListener listener) {
        FileUtils.checkOrCreateParentFolder(outPath);
        String filter = String.format(Locale.ENGLISH,
                "[1:0]adelay=%d|%d,afade=d=3,areverse,afade=d=3,areverse[delay];[0:1][delay]amix=inputs=2", start,
                start);
        StringBuilder sb = new StringBuilder("ffmpeg");
        sb.append(" -i");
        sb.append(" ").append(videoPath);
        sb.append(" -i");
        sb.append(" ").append(audioPath);
        sb.append(" -filter_complex");
        sb.append(" ").append(filter);
        sb.append(" -c:v");
        sb.append(" copy");
        sb.append(" -c:a aac -strict -2");
        sb.append(" -y");
        sb.append(" ").append(outPath);
        String command = sb.toString();
        LogUtils.i(TAG, "command:" + command);
        FFmpegCmd.getInstance().execute(command.split(" "), listener);
    }

    /**
     * 如果视频本身没有音频通道，或者覆盖视频中的原有音频通道，采用此方法，会用第二文件的音频覆盖第一文件的视频
     * ffmpeg -i input_10s.mp4 -i 4smp3.mp3 -filter_complex "[1:0] adelay=2000|2000 ,afade=t=in:ss=0:d=3,afade=t=out:st=3:d=3" -c:a aac -strict -2  -c:v copy  -y  result/test.mp4
     * [1:0]adelay=%d|%d,afade=d=3,areverse,afade=d=3,areverse   areverse的作用是把音频倒置，设置淡入结束后再把音频重新倒置就好了
     *
     * @param start   开始时间
     * @param end     结束时间
     * @param outPath 输出地址
     */
    public static void insertAudiobyLocationOverride(String videoPath, String audioPath, int start, int end,
                                                     String outPath, CallCmdListener listener) {
        String filter =
                String.format(Locale.ENGLISH, "[1:0]adelay=%d|%d,afade=d=3,areverse,afade=d=3,areverse", start, start);
        StringBuilder sb = new StringBuilder("ffmpeg");
        sb.append(" -d");
        sb.append(" -i");
        sb.append(" ").append(videoPath);
        sb.append(" -i");
        sb.append(" ").append(audioPath);
        sb.append(" -filter_complex");
        sb.append(" ").append(filter);
        sb.append(" -c:v");
        sb.append(" copy");
        sb.append(" -c:a aac -strict -2");
        sb.append(" -y");
        sb.append(" ").append(outPath);
        String command = sb.toString();
        LogUtils.i(TAG, "command:" + command);
        FFmpegCmd.getInstance().execute(command.split(" "), listener);
    }

    /**
     * 修改视频原音的多片段音量 +指的是or，*指的是and
     * ffmpeg -i 20mp3.mp3 -af "volume='if(between(t,0,2),0.1,if(between(t,6,8),0.9,if(between(t,12,14),0.1,0.5)))':eval=frame"  -y 333.mp3
     * 其实上面的指令会在android中执行出错，原因是 Applying option af (set audio filters) with argument volume=if(between(t,0,3),0.0,0.5):eval=frame.
     * ffmpeg 内部会把Setting 'volume' to value 'if(between(t'识别出错，会把逗号前当做一个值，不再android中执行是需要加上引号的，但是安卓上执行需要把引号去掉，
     * 现在去掉之后又出现了一个逗号问题，所以退而求其次，如果视频有和音频混音，那么视频的音量以第一部分的交叉的音量大小为准
     *
     * @param outputUrl 输出地址
     * @param listener  监听
     */
    public static String[] changeMultiVolume(String inputUrl, String outputUrl, CallCmdListener listener,
                                             ArrayList<AudioDuration> list, boolean isExecute) {
        FileUtils.checkOrCreateParentFolder(outputUrl);
        // 先做过滤，如果volume是0.5，即没有做音量调整的，那就没有这么蛋疼闲操心的去改变音量了
        List<AudioDuration> result = (List<AudioDuration>) list.clone();
        Iterator it = result.iterator();
        while (it.hasNext()) {
            AudioDuration audioDuration = (AudioDuration) it.next();
            if (audioDuration.getVolume() == 0.5) {
                it.remove();
            }
        }
        StringBuilder sb = new StringBuilder("ffmpeg");
        sb.append(" -d");
        sb.append(" -i");
        sb.append(" ").append(inputUrl);
        sb.append(" -af");
        sb.append(" volume=");
        int start;
        int end;
        float volume;
        StringBuilder filterBuilder = new StringBuilder();
        if (result.isEmpty()) {
            filterBuilder.append("1");
        }
        // else {
        // filterBuilder.append(result.get(0).getVolume());
        // }
        String endBlacket = "";
        if (result.size() > 0) {
            filterBuilder.append("\'");
            for (int i = 0; i < result.size(); i++) {
                start = TimeUtil.getSecond(result.get(i).getStart());
                end = TimeUtil.getSecond(result.get(i).getEnd());
                volume = result.get(i).getVolume();
                endBlacket += ")";
                if (i != result.size() - 1) {
                    filterBuilder.append("if(between(t,").append(start).append(",").append(end).append("),")
                            .append(volume).append(",");
                } else {
                    filterBuilder.append("if(between(t,").append(start).append(",").append(end).append("),")
                            .append(volume).append(",0.5").append(endBlacket);
                }
            }
            filterBuilder.append("\'");
        }

        sb.append(filterBuilder.toString());
        sb.append(":eval=frame");
        sb.append(" -y");
        sb.append(" ").append(outputUrl);
        String[] command = sb.toString().split(" ");
        LogUtils.i(TAG, "command:" + Arrays.toString(command));
        if (isExecute) {
            FFmpegCmd.getInstance().execute(command, listener);
        }
        return command;
    }

    /**
     * ffmpeg -f lavfi -i aevalsrc=0 -t 20 -acodec libmp3lame 20sblank.mp3
     *
     * @param outputUrl
     * @param duration        精确到以为小数
     * @param callCmdListener
     */
    public static String[] createSilenceAudio(String outputUrl, float duration, CallCmdListener callCmdListener,
                                              boolean isExcute) {
        FileUtils.checkOrCreateParentFolder(outputUrl);
        String[] commands = new String[]{"ffmpeg", "-d", "-f", "lavfi", "-i", "aevalsrc=0", "-t", "" + duration,
                "-acodec", "libmp3lame", "-y", outputUrl};
//        LogUtils.i(TAG, "command:" + Arrays.toString(commands));
        if (isExcute) {
            FFmpegCmd.getInstance().execute(commands, callCmdListener);
        }
        return commands;
    }

    /**
     * ffmpeg -f lavfi -i aevalsrc=0 -t 20 -acodec aac  20sblank.aac
     *
     * @param outputUrl
     * @param duration
     * @param callCmdListener
     */
    public static String[] createSilenceAudioAAC(String outputUrl, float duration, CallCmdListener callCmdListener,
                                                 boolean isExcute) {
        FileUtils.checkOrCreateParentFolder(outputUrl);
        String[] commands = new String[]{"ffmpeg", "-d", "-f", "lavfi", "-i", "aevalsrc=0", "-t", "" + duration,
                "-acodec", "aac", "-y", outputUrl};
        LogUtils.i(TAG, "command:" + Arrays.toString(commands));
        if (isExcute) {
            FFmpegCmd.getInstance().execute(commands, callCmdListener);
        }
        return commands;
    }

    /**
     * 用于音频的插入和成，把各个音频片段插入到静音总线中
     * 如果有淡入淡出标志则加上淡入淡出功能
     * ffmpeg -i 20sblank.mp3  -i 1.mp3 -i 2.mp3 -i 3.mp3 -i 5.mp3  -filter_complex "[1]adelay=0|0,afade=t=in:ss=0:d=3,afade=t=out:st=3:d=3 [del1];
     * [2]adelay=5000|5000[del2];[3]adelay=8000|8000[del3];[4]adelay=9000|9000[del4];[0][del1][del2][del3][del4]amix=5" result.mp3
     * <p>
     * 遗漏一个事情， 对于背景音乐，应该不做淡入处理，因为默认是原音
     *
     * @param outputUrl
     * @param list
     */
    public static List<String[]> inserMultiAudio(String outputUrl, String silencePath, List<AudioInsertItem> list) {
        FileUtils.checkOrCreateParentFolder(outputUrl);
        final int splitCount = 10;
        List<String[]> listCmd = new ArrayList<>();
        int times = list.size() / splitCount;
        if (times == 0) {
            String[] command = insertMultiAudioBydelay(0, list.size(), list, silencePath, outputUrl);
            listCmd.add(command);
            // FFmpegCmd.getInstance().execute(command, listener);
            return listCmd;
        }
        if (list.size() % splitCount > 0) {
            times++;
        }
        String inputPath = silencePath;
        String outPath = "";
        List<String> outputs = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            // outPath = "liangze/create/i"+i+".mp3";
            outPath = ConstantPath.createMultiAudios();
            int start = i * splitCount;
            int end = (i + 1) * splitCount;
            if (i + 1 == times) {
                end = list.size();
            }
            String[] command = insertMultiAudioBydelay(start, end, list, inputPath, outPath);
//            inputPath = outPath;
            listCmd.add(command);
            outputs.add(outPath);
        }
        //多音频混音
        outPath = ConstantPath.createMultiAudios();
        String[] commands = anixMultiAudio(outPath, false, null, outputs.toArray(new String[outputs.size()]));
//        LogUtils.i(TAG, "command:" + Arrays.toString(commands));
        listCmd.add(commands);
        // 改变音量，多次混音导致音量大大降低
        commands = new String[]{"ffmpeg", "-d", "-i", outPath, "-af", "volume=15dB", "-y", outputUrl};
//        LogUtils.i(TAG, "command:" + Arrays.toString(commands));
        listCmd.add(commands);
        return listCmd;
        // FFmpegCmd.getInstance().executeList(listCmd, listener);
    }

    public void changeVolume(String inputpath, String outputPath, float volume) {

    }

    /**
     * [0:a]aformat=sample_fmts=fltp:channel_layouts=stereo,volume=0.4[a0];
     *
     * @param start
     * @param end
     * @param list
     * @param silencePath
     * @param output
     * @return
     */
    private static String[] insertMultiAudioBydelay(int start, int end, List<AudioInsertItem> list, String silencePath,
                                                    String output) {
        FileUtils.checkOrCreateParentFolder(output);
        //排序audios
        Collections.sort(list, (o1, o2) -> Integer.compare(o1.getStart(),
                o2.getStart()));
        int fading = ConstantMediaSize.FADE_SECOND;
        StringBuilder sb = new StringBuilder("ffmpeg");
        StringBuilder endBlacket = new StringBuilder("[0]");
        sb.append(" -d");
        sb.append(" -i");
        sb.append(" ").append(silencePath);
        for (int i = start; i < end; i++) {
            sb.append(" -i ").append(list.get(i).getPath());
        }
        sb.append(" -filter_complex ");
        int tempEnd = 0;
        for (int i = start; i < end; i++) {
            if (list.get(i).getStart() > 0) {
                sb.append("[").append((i + 1 - start)).append("]");
                sb.append("adelay=");
                sb.append(list.get(i).getStart()).append("|").append(list.get(i).getStart());
            }
            if (list.get(i).isFade()) {
                tempEnd = TimeUtil.getSecond(list.get(i).getEnd()) - fading;
                if (sb.toString().contains("adelay")) {
                    sb.append(",afade=t=in:ss=0:d=").append(fading);
                } else {
                    sb.append("[1]afade=t=in:ss=0:d=").append(fading);
                }
                sb.append(",afade=t=out:st=").append(tempEnd).append(":d=").append(fading);
            }
            if (list.get(i).getStart() > 0 || list.get(i).isFade()) {
                sb.append("[audio").append(i + 1 - start).append("];");
                endBlacket.append("[audio").append(i + 1 - start).append("]");
            } else {
                endBlacket.append("[").append((i + 1 - start)).append("]");
            }
        }
        sb.append(endBlacket.toString());
        sb.append("amix=").append(end - start + 1);
        sb.append(" -y ");
        sb.append(output);
        String command = sb.toString();
        return command.split(" ");
    }

    /**
     * 裁剪视频 精确到毫秒级别
     * ffmpeg -ss 00:05:43 -t 124  -i 1.mp4   -vcodec copy -acodec copy -avoid_negative_ts 1 -y result.mp4   音视频同步
     * 这里需要进行细分，因为出现了这样的情况，音频的copy出了问题，视频是没有问题，但是音频出现了这个中情况：MP4文件竟然包含了amr_nb这种编码
     * 但是这种编码只支持3gp的情况的，所以使用copy就出现了错误，所以音频也做一次检查，如果是aac格式则直接复制，如果不是aac则不作复制
     * acc对应的android系统格式是：mp4a-latm
     * <p>
     * ffmpeg -ss 00:00:00 -t 10  -i HUAWEI_SUPERpm4.mp4   -vf  "scale=720:-2" -y result.mp4
     *
     * @param intputPath 视频源
     * @param destPath   结果目标地址
     * @param startTime  开始时间 00:00:02  .400 格式调用timeUtil
     * @param listener   监听回调
     */
    public static String[] cutVideo(String intputPath, String destPath, String startTime, CallCmdListener listener,
                                    int totalTime, int cutWidth, boolean isExcute) {
        FileUtils.checkOrCreateParentFolder(destPath);
        String[] commands = dealVideoCut(intputPath, destPath, startTime, totalTime, cutWidth, true);

        LogUtils.i(TAG, "command:" + Arrays.toString(commands));
        if (isExcute) {
            FFmpegCmd.getInstance().executeProgress(commands, listener, totalTime);
        }
        return commands;
    }

    /**
     * 分割视频
     * ffmpeg -i input.mp4 -vcodec copy -acodec copy -to 00:00:05.400  -y output.mp4
     *
     * @param intputPath    视频源
     * @param destPathLeft  结果目标地址
     * @param split         开始时间 00:00:02  .400 格式调用timeUtil
     * @param destPathRight 这里遇到一很大的問題就是被微信壓縮過的視頻，-ss進行取不到關鍵幀了
     */
    public static List<String[]> splitVideo(String intputPath, final String destPathLeft, String destPathRight,
                                            int split, int totalTime, int cutWidth) {
        FileUtils.checkOrCreateParentFolder(destPathLeft);
        String splitTime = TimeUtil.getCutForamt(split);
        final List<String[]> list = new ArrayList<>();
        list.add(dealVideoCut(intputPath, destPathLeft, splitTime, totalTime - split, cutWidth, false));
        list.add(dealVideoCut(intputPath, destPathRight, splitTime, totalTime - split, cutWidth, true));
        return list;
    }

    private static String change2mkv(String path) {
        String temp = path.substring(0, path.lastIndexOf("."));
        return temp + ".mkv";
    }

    private static String change2mp4(String path) {
        String temp = path.substring(0, path.lastIndexOf("."));
        return temp + ".mp4";
    }

    /**
     * 裁中间，要两头
     *
     * @param inputPath 视频源
     * @param
     * @param end       00:00:02  .400 格式调用timeUtil
     */
    public static List<String[]> cutVideoDeleteMiddle(String inputPath, String outputPath, int start, int end,
                                                      int totalTime, int cutWidth) {
        FileUtils.checkOrCreateParentFolder(outputPath);
        String cutStartTime = TimeUtil.getCutForamt(start);
        String cutEndTime = TimeUtil.getCutForamt(end);
//        List<FFmpegListProgress> helpers = new ArrayList<>();
        String suffix;
        if (inputPath.lastIndexOf(".") != -1) {
            suffix = inputPath.substring(inputPath.lastIndexOf("."));
        } else {
            suffix = ".mp3";
        }
        final String outputPath1 = ConstantPath.getVideoTrimPath() + "murge_1" + suffix;
        final String outputPath2 = ConstantPath.getVideoTrimPath() + "murge_2" + suffix;
        final List<String[]> list = new ArrayList<>();

        list.add(dealVideoCut(inputPath, outputPath1, cutStartTime, totalTime, cutWidth, false));
        list.add(dealVideoCut(inputPath, outputPath2, cutEndTime, totalTime, cutWidth, true));
        list.add(videoMurgeFromFilelist(null, outputPath, false, false, null, outputPath1, outputPath2));
        return list;
    }

    /**
     * 从起始点到cutStartTime进行裁剪
     *
     * @param inputPath
     * @param outputPath
     * @param cutStartTime
     * @param listener
     * @param isExcute
     * @return
     */
    public static String[] cutVideoTo(String inputPath, String outputPath, String cutStartTime, int totalTime,
                                      CallCmdListener listener, boolean isExcute, int cutWidth) {
        FileUtils.checkOrCreateParentFolder(outputPath);
        String[] commands = dealVideoCut(inputPath, outputPath, cutStartTime, totalTime, cutWidth, false);

        LogUtils.i(TAG, "command:" + Arrays.toString(commands));
        if (isExcute) {
            FFmpegCmd.getInstance().executeProgress(commands, listener, totalTime);
        }
        return commands;
    }

    /**
     * 从起始点到cutStartTime进行裁剪
     *
     * @param inputPath
     * @param outputPath
     * @param cutStartTime
     * @param listener
     * @param isExcute
     * @return
     */
    public static String[] cutVideoToCopy(String inputPath, String outputPath, String cutStartTime, int totalTime,
                                          CallCmdListener listener, boolean isExcute, int cutWidth) {
        FileUtils.checkOrCreateParentFolder(outputPath);
        String[] commands = new String[]{"ffmpeg", "-d", "-i", inputPath, "-vcodec", "copy", "-acodec", "copy", "-to",
                cutStartTime, "-y", outputPath};
        if (cutWidth != 0) {
            commands = new String[]{"ffmpeg", "-d", "-i", inputPath, "-vf", "scale=" + cutWidth + ":-2", "-to",
                    cutStartTime, "-y", outputPath};
        }
        LogUtils.i(TAG, "command:" + Arrays.toString(commands));
        if (isExcute) {
            FFmpegCmd.getInstance().executeProgress(commands, listener, totalTime);
        }
        return commands;
    }

    /**
     * 从cutStartTime点到结尾进行裁剪
     * * ffmpeg -ss 00:05:43 -t 124  -i 1.mp4   -vcodec copy -acodec copy -avoid_negative_ts 1 -y result.mp4   音视频同步
     * "-acodec", "copy",
     *
     * @param inputPath
     * @param outputPath
     * @param cutEndTime
     * @param listener
     * @param isExcute
     * @return
     */
    public static String[] cutVideoFrom(String inputPath, String outputPath, String cutEndTime, int totalTime,
                                        CallCmdListener listener, boolean isExcute, int cutWidth) {
        FileUtils.checkOrCreateParentFolder(outputPath);
        String[] commands = dealVideoCut(inputPath, outputPath, cutEndTime, totalTime, cutWidth, true);
        LogUtils.i(TAG, "command:" + Arrays.toString(commands));
        if (isExcute) {
            FFmpegCmd.getInstance().executeProgress(commands, listener, totalTime);
        }
        return commands;
    }

    // public static String[] cutVideo(String inputPath, String outputPath, String cutEndTime, String endTime, int
    // totalTime,
    // CallCmdListener listener, boolean isExcute) {
    // String[] commands = new String[]{"ffmpeg", "-d", "-i", inputPath, "-vcodec", "copy", "-acodec", "copy", "-ss",
    // cutEndTime, "-to", endTime, "-y", outputPath};
    // LogUtils.i(TAG, "command:" + Arrays.toString(commands));
    // if (isExcute) {
    // FFmpegCmd.getInstance().executeProgress(commands, listener, totalTime);
    // }
    // return commands;
    // }

    /**
     * ffmpeg -i output.mp4 -c:v libx264 -vf scale=352:640 -r 60 -c:a copy -strict experimental -f mpegts 3.ts
     * <p>
     * ffmpeg -i input2.mp4   -c:v libx264 -c:a copy  -f mpegts -y 3.ts
     *
     * @param inputPath
     * @param outputPath
     * @param changeScale
     * @param width
     * @param height
     * @param listener    如果主MP4和其他格式文件进行合并时，音频编辑不统一其实音频也会编码失败的，音频只保留了一文件的音频
     * @return
     */
    public static String[] changeScaleAsTs(String inputPath, String outputPath, boolean changeScale, int width,
                                           int height, boolean isExcute, CallCmdListener listener) {
        FileUtils.checkOrCreateParentFolder(outputPath);
        String audioFormat = "copy";
        if (!inputPath.endsWith(".mp4")) {
            audioFormat = "libmp3lame";
        }
        String[] commands = new String[]{"ffmpeg", "-d", "-i", inputPath, "-max_muxing_queue_size", "1024", "-c:v",
                "libx264", "-c:a", audioFormat, "-strict", "experimental", "-f", "mpegts", "-y", outputPath};
        if (changeScale) {
            commands = new String[]{"ffmpeg", "-d", "-i", inputPath, "-max_muxing_queue_size", "1024", "-c:v",
                    "libx264", "-vf", "scale=", width + ":" + height, "-c:a", audioFormat, "-strict", "experimental",
                    "-f", "mpegts", "-y", outputPath};
        }
        StringBuilder sb = new StringBuilder();
        for (String str : commands) {
            sb.append(str).append(" ");
        }
        LogUtils.i(TAG, "command:" + sb.toString());
        if (isExcute) {
            FFmpegCmd.getInstance().execute(commands, listener);
        }
        return commands;
    }


    /**
     * ffmpeg -i input.mp4 -b:v 10000k -vf scale=1080:1920 video_1080_1920.mp4
     * 修改video的分辨率和码率值
     */
    public static String[] videoBitResolution(String inputPath, String outPath, float bitRate, int[] resolution, boolean isExcute, CallCmdListener listener) {
        //resulution 不为奇数
        if (bitRate < 0) {
            throw new IllegalArgumentException("bitRate must >0");
        }
        if (resolution == null || resolution.length < 2 || resolution[0] < 0 || resolution[1] < 0) {
            throw new IllegalArgumentException("resolution must >0");
        }
        if (resolution[0] % 2 == 1 || resolution[1] % 2 == 1) {
            throw new IllegalArgumentException("resolution must even number! width:" + resolution[0] + "  height:" + resolution[1]);
        }
        FileUtils.checkOrCreateParentFolder(outPath);

        String[] commands = new String[]{"ffmpeg", "-d", "-i", inputPath, "-y", "-b:v", bitRate + "k",
                "-vf", "scale=" + resolution[0] + ":" + resolution[1], "-vsync", "2", "-y", outPath};
        if (isExcute) {
            FFmpegCmd.getInstance().execute(commands, listener);
        }
        return commands;
    }

    /**
     * ffmpeg -i result.mp4 result.gif
     *
     * @return
     */
    public static String[] video2Gif(String inputPath, String outPath, long startTime, long endTime, boolean isExcute, CallCmdListener listener) {

        if (ObjectUtils.isEmpty(inputPath) || ObjectUtils.isEmpty(outPath)) {
            throw new IllegalArgumentException("inputPath| outPath  must not null");
        }
        FileUtils.checkOrCreateParentFolder(outPath);
        String[] commands = new String[]{"ffmpeg", "-d", "-i", inputPath, "-ss", TimeUtil.getCutForamt(startTime), "-to", TimeUtil.getCutForamt(endTime), "-y", outPath};
        if (isExcute) {
            FFmpegCmd.getInstance().execute(commands, listener);
        }
        return commands;
    }

    /**
     * 先加测是否复制视频码流，然后检测是否复制音频码流，最后检测宽高是否为偶数
     *
     * @return
     */
    private static String[] dealVideoCut(String inputPath, String outputPath, String startTime, int totalTime, int cutWidth, boolean from) {
        String[] commands;
        if (from) {
            if (FileUtils.checkVideoH264(inputPath) || inputPath.endsWith(".mp4")) {
                commands = new String[]{"ffmpeg", "-d", "-ss", startTime, "-t",
                        String.valueOf(TimeUtil.getSecondOneDecimal(totalTime)), "-i", inputPath, "-vcodec", "copy",
                        "-acodec", "copy", "-avoid_negative_ts", "1", "-y", outputPath};
                // 如果是MP4文件的同时，再次检测此文件的音频是否是aac的音频
                if (!FileUtils.checkVideoAacMp3(inputPath) && inputPath.endsWith(".mp4")) {
                    commands = new String[]{"ffmpeg", "-d", "-ss", startTime, "-t",
                            String.valueOf(TimeUtil.getSecondOneDecimal(totalTime)), "-i", inputPath, "-vcodec", "copy",
                            "-avoid_negative_ts", "1", "-y", outputPath};
                }
            } else {
                commands = new String[]{"ffmpeg", "-d", "-ss", startTime, "-t",
                        String.valueOf(TimeUtil.getSecondOneDecimal(totalTime)), "-i", inputPath, "-acodec", "copy", "-avoid_negative_ts",
                        "1", "-y", outputPath};
            }
            if (cutWidth % 2 != 0) {
                commands = new String[]{"ffmpeg", "-d", "-ss", startTime, "-t",
                        String.valueOf(TimeUtil.getSecondOneDecimal(totalTime)), "-i", inputPath, "-vf",
                        "scale=" + cutWidth + ":-2", "-y", outputPath};
            }
            return commands;
        }


        if (FileUtils.checkVideoH264(inputPath) || inputPath.endsWith(".mp4")) {
            commands = new String[]{"ffmpeg", "-d", "-i", inputPath, "-vcodec", "copy", "-acodec", "copy", "-to",
                    startTime, "-y", outputPath};

            if (!FileUtils.checkVideoAacMp3(inputPath) && inputPath.endsWith(".mp4")) {
                commands = new String[]{"ffmpeg", "-d", "-i", inputPath, "-vcodec", "copy", "-to",
                        startTime, "-y", outputPath};
            }
        } else {
            commands = new String[]{"ffmpeg", "-d", "-i", inputPath, "-to", startTime, "-y", outputPath};
        }
        if (cutWidth % 2 != 0) {
            commands = new String[]{"ffmpeg", "-d", "-i", inputPath, "-to", startTime, "-vf",
                    "scale=" + cutWidth + ":-2", "-y", outputPath};
        }
        return commands;
    }


    /**
     * ffmpeg  -i india.mp4  -vf reverse -af areverse  -ss 00:00:06 -to 00:00:15 -c:v libx264  -y result.mp4
     * 指定编码为mp4
     * preset与编码速度和质量有关，可优化编码速度（每秒编码帧数）和压缩效率（比特流中每比特的质量）之间的权衡。
     * 以x265为例，preset有ultrafast，superfast，veryfast，faster，fast，medium，slow，slower，veryslow，placebo这10个级别，
     * 每个级别的preset对应一组编码参数，不同级别的preset对应的编码参数集不一致。preset级别越高，编码速度越慢，解码后的质量也越高；
     * 级别越低，速度也越快，解码后的图像质量也就越差，从左到右，编码速度越来越慢，编码质量越来越好，其中placebo不建议使用，没有太大意义
     * 对视频的质量要求很严格时才使用veryslow。ultrafast产生的视频可能会非常大，使用前也需要仔细思考
     */
    public static String[] videoReverse(String inputPath, String outPath, boolean isExcute, CallCmdListener listener) {
        if (ObjectUtils.isEmpty(inputPath) || ObjectUtils.isEmpty(outPath)) {
            throw new IllegalArgumentException("inputPath| outPath  must not null");
        }
        FileUtils.checkOrCreateParentFolder(outPath);
//        String[] commands = new String[]{"ffmpeg", "-d", "-i", inputPath, "-vf", "reverse",
//                "-y", outPath};
        String[] commands = new String[]{"ffmpeg", "-d", "-i", inputPath, "-vf", "reverse", "-af", "areverse",
                "-preset", "ultrafast", "-y", outPath};
//        String[] commands = new String[]{"ffmpeg", "-d", "-i", inputPath, "-vf", "reverse", "-af", "areverse",
//                "-preset", "superfast", "-y", outPath};
//        String[] commands = new String[]{"ffmpeg", "-d", "-i", inputPath, "-ss", TimeUtil.getCutForamt(startTime),
//                "-to", TimeUtil.getCutForamt(endTime), "-vf", "reverse", "-af", "areverse",
//                "-c:v", "libx264", "-y", outPath};
        if (isExcute) {
            FFmpegCmd.getInstance().execute(commands, listener);
        }
        return commands;
    }

    /**
     * 裁剪视频，然后剔除音频
     *
     * @param inputPath
     * @param outputPath
     * @param startTime
     * @param totalTime
     * @param cutWidth
     * @return
     */
    public static String[] cutVideoRemoveAudio(String inputPath, String outputPath, String startTime, int totalTime, int cutWidth) {
        FileUtils.checkOrCreateParentFolder(outputPath);
        String[] commands;
        if (FileUtils.checkVideoH264(inputPath) || inputPath.endsWith(".mp4")) {
            commands = new String[]{"ffmpeg", "-d", "-ss", startTime, "-t",
                    String.valueOf(TimeUtil.getSecondOneDecimal(totalTime)), "-i", inputPath, "-vcodec", "copy",
                    "-an", "-avoid_negative_ts", "1", "-y", outputPath};
        } else {
            commands = new String[]{"ffmpeg", "-d", "-ss", startTime, "-t",
                    String.valueOf(TimeUtil.getSecondOneDecimal(totalTime)), "-i", inputPath, "-an", "-avoid_negative_ts",
                    "1", "-y", outputPath};
        }
        if (cutWidth % 2 != 0) {
            commands = new String[]{"ffmpeg", "-d", "-ss", startTime, "-t",
                    String.valueOf(TimeUtil.getSecondOneDecimal(totalTime)), "-i", inputPath, "-vf",
                    "scale=" + cutWidth + ":-2", "-an", "-y", outputPath};
        }
        return commands;
    }
}
