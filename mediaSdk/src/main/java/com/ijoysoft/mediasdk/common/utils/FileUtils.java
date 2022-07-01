/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ijoysoft.mediasdk.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.text.TextUtils;
import android.util.Log;

import com.ijoysoft.mediasdk.common.global.ConstantPath;
import com.ijoysoft.mediasdk.module.entity.TransationType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * File utilities
 */
public class FileUtils {
    // Logging
    private static final String TAG = "FileUtils";

    /**
     * 删除文件
     */
    public static void deleteFile(String deletePath, String videoPath) {
        File file = new File(deletePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    if (f.listFiles().length == 0) {
                        f.delete();
                    } else {
                        deleteFile(f.getAbsolutePath(), videoPath);
                    }
                } else if (!f.getAbsolutePath().equals(videoPath)) {
                    f.delete();
                }
            }
        }
    }

    /**
     * Delete all the files in the specified folder and the folder itself.
     *
     * @param dir The project path
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            final String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                final File f = new File(dir, children[i]);
                if (!deleteDir(f)) {
                    LogUtils.e(TAG, "File cannot be deleted: " + f.getAbsolutePath());
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * Get the name of the file
     *
     * @param filename The full path filename
     * @return The name of the file
     */
    public static String getSimpleName(String filename) {
        final int index = filename.lastIndexOf('/');
        if (index == -1) {
            return filename;
        } else {
            return filename.substring(index + 1);
        }
    }

    /**
     * 视频合成是，检查视频路径是否是存在空格， 需要把空格去掉，然后改文件物理路径并进行保存
     * @param resultPath
     * @param path
     */
    public static void insertVideoFileName(String resultPath, String... path) {
        createOrExistsFile(resultPath);
        StringBuffer content = new StringBuffer();
        for (String p : path) {
            content.append("file ");
            content.append(p);
            content.append(System.getProperty("line.separator"));
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(resultPath);
            fileWriter.write(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return 文件是否存在
     */
    public static boolean checkFileExist(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    /**
     * 检查路径是否存在或者建立
     *
     * @param path
     * @return
     */
    public static boolean createPath(String path) {
        File file = new File(path);
        if (file.isDirectory() && file.exists()) {
            return true;
        }
        if (!file.exists()) {
            file.mkdirs();
            return true;
        }
        return false;
    }

    public static File createFile(String path) {
        File file = new File(path);
        if (file == null) {
            return null;
        }
        if (!file.exists()) {
            try {
                File fileParent = file.getParentFile();
                if (!fileParent.exists()) {
                    fileParent.mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 检查文件是否存在并创建
     *
     * @param filePath
     * @return
     */
    public static boolean createOrExistsFile(String filePath) {
        File file = new File(filePath);
        if (file == null)
            return false;
        if (file.exists())
            return file.isFile();
        try {
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 更改file名字
     */
    public static boolean renameFile(String filePath, String rename) {

        File file = new File(filePath);
        if (file.exists()) {
            File refile = new File(rename);
            if (refile.exists()) {
                refile.delete();
            }
            return file.renameTo(refile);
        }
        return false;
    }

    public static void delete(String path) {
        File refile = new File(path);
        if (refile.exists()) {
            refile.delete();
        }
    }

    public static void  copy(String source, String target, boolean isFolder) throws Exception {
        if (isFolder) {
            File a = new File(source);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (source.endsWith(File.separator)) {
                    temp = new File(source + file[i]);
                } else {
                    temp = new File(source + File.separator + file[i]);
                }
                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(target + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {
                    copy(source + File.separator+ file[i], target + File.separator + file[i], true);
                }
            }
        } else {
            int byteread = 0;
            File oldfile = new File(source);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(source);
                File file = new File(target);
                file.getParentFile().mkdirs();
                file.createNewFile();
                FileOutputStream fs = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        }
    }

    // 获取VideoPath
    public static String getPath(String path, String fileName) {
        String p = ConstantPath.APPPATH + File.separator + path;
        File f = new File(p);
        if (!f.exists() && !f.mkdirs()) {
            return ConstantPath.APPPATH + File.separator + fileName;
        }
        return p + fileName;
    }

    /**
     * 保存bitmap到本地
     *
     * @param bitmap
     */
    public static String saveBitmapToSDCard(Bitmap bitmap, String path) {
        FileOutputStream fos = null;
        try {
            createOrExistsFile(path);
            fos = new FileOutputStream(path);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();
            }
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 复制gif文件到本地
     *
     * @param assetsPath
     * @param savePath
     */
    public static void copyFilesFromAssets(Context context, String assetsPath, String savePath) {
        try {
            String fileNames[] = context.getAssets().list(assetsPath);// 获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {// 如果是目录
                File file = new File(savePath);
                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFromAssets(context, assetsPath + "/" + fileName, savePath + "/" + fileName);
                }
            } else {// 如果是文件
                InputStream is = context.getAssets().open(assetsPath);
                FileOutputStream fos = new FileOutputStream(new File(savePath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                    // buffer字节
                    fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
                }
                fos.flush();// 刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * public static String TRANSATION = PRJECTPATH + File.separator + "transation";
     * public static String TRANSATION_LEFT = TRANSATION + File.separator + "left.gif";
     * public static String TRANSATION_RIGHT = TRANSATION + File.separator + "right.gif";
     * public static String TRANSATION_TOP = TRANSATION + File.separator + "top.gif";
     * public static String TRANSATION_BOTTOM = TRANSATION + File.separator + "bottom.gif";
     *
     * @param transationType
     * @return
     */
    public static String getTransationPath(TransationType transationType) {
        switch (transationType) {
        case MOVE_LEFT:
        case MOVE_BOTTOM:
        case MOVE_RIGHT:
        case MOVE_TOP:
        default:
            return "";
        }
    }

    /**
     * 读取assets
     *
     * @param context
     * @param outputPath
     * @param fileName
     * @param inputPath
     */
    public static void copyFileFromAssets(Context context, String outputPath, String fileName, String inputPath) {
        File file = new File(outputPath, fileName);
        LogUtils.d(TAG, "copyFileFromAssets: " + file.getAbsolutePath());
        try {
            if (!file.exists()) {
                File fileParentDir = file.getParentFile();
                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }
                file.createNewFile();
            } else
                return;
            InputStream in = context.getResources().getAssets().open(inputPath);
            OutputStream out = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            int realLength;
            while ((realLength = in.read(buffer)) > 0) {
                out.write(buffer, 0, realLength);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readAssetsTextFile(Context context, String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getStringFromStream(inputStream);
    }

    private static String getStringFromStream(InputStream inputStream) {
        if (inputStream == null)
            return null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            reader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检查是否有音频
     *
     * @param path
     * @return
     */
    public static boolean checkExistAudio(String path) {
        boolean exist = false;
        MediaExtractor mVideoExtractor = null;
        try {
            mVideoExtractor = new MediaExtractor();
            mVideoExtractor.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int j = 0; j < mVideoExtractor.getTrackCount(); j++) {
            MediaFormat format = mVideoExtractor.getTrackFormat(j);
            if (format.getString(MediaFormat.KEY_MIME).startsWith("audio/")) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    /**
     * 检查是否有音频
     *
     * @param path
     * @return
     */
    public static boolean checkExistVideo(String path) {
        boolean exist = false;
        MediaExtractor mVideoExtractor = null;
        try {
            mVideoExtractor = new MediaExtractor();
            mVideoExtractor.setDataSource(path);
            for (int j = 0; j < mVideoExtractor.getTrackCount(); j++) {
                MediaFormat format = mVideoExtractor.getTrackFormat(j);
                Log.i("test", format.getString(MediaFormat.KEY_MIME));
                if (format.getString(MediaFormat.KEY_MIME).startsWith("video/")) {
                    exist = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mVideoExtractor.release();
        }
        return exist;
    }

    /**
     * 检查是否有音频
     * video/avc  h264
     * video/3gpp
     *  video/mp4v-es
     * @param path
     *
     * audio/*
     * @return
     */
    public static boolean checkVideoH264(String path) {
        boolean exist = false;
        MediaExtractor mVideoExtractor = null;
        try {
            mVideoExtractor = new MediaExtractor();
            mVideoExtractor.setDataSource(path);
            for (int j = 0; j < mVideoExtractor.getTrackCount(); j++) {
                MediaFormat format = mVideoExtractor.getTrackFormat(j);
                if (format.getString(MediaFormat.KEY_MIME).startsWith("video/avc")) {
                    exist = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mVideoExtractor.release();
        }
        return exist;
    }

    /**
     * videoFormat
     * @param path
     * @return
     */
    public static String getVideoAudioFormat(String path){
        String video="",audio="";
        MediaExtractor mVideoExtractor = null;
        try {
            mVideoExtractor = new MediaExtractor();
            mVideoExtractor.setDataSource(path);
            for (int j = 0; j < mVideoExtractor.getTrackCount(); j++) {
                MediaFormat format = mVideoExtractor.getTrackFormat(j);
                if (format.getString(MediaFormat.KEY_MIME).startsWith("video/")) {
                    video=format.getString(MediaFormat.KEY_MIME);
                }
                if (format.getString(MediaFormat.KEY_MIME).startsWith("audio/")) {
                    audio=format.getString(MediaFormat.KEY_MIME);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mVideoExtractor.release();
        }
        return video+"|"+audio;
    }


    // /**
    // * 检查是否有音频
    // *
    // * @param path
    // * @return
    // */
    // public static boolean checkExistVideo(String path) {
    // boolean exist = false;
    // MediaExtractor mVideoExtractor = null;
    // try {
    // mVideoExtractor = new MediaExtractor();
    // mVideoExtractor.setDataSource(path);
    // for (int j = 0; j < mVideoExtractor.getTrackCount(); j++) {
    // MediaFormat format = mVideoExtractor.getTrackFormat(j);
    // if (format.getString(MediaFormat.KEY_MIME).startsWith("video/")) {
    // exist = true;
    // break;
    // }
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // } finally {
    // mVideoExtractor.release();
    // }
    //
    // return exist;
    // }

    /**
     * 改变临时文件的存储文件格式，以防系统读取出通用的格式，进行改动删除
     * 最终只保留合成后的文件为正常文件
     */
    public static String changePrivateFileName(String path) {
        File file = new File(path);
        String destPath = path + "_ijoysoft";
        file.renameTo(new File(destPath));
        return destPath;
    }

}
