package com.qiusuo.videoeditor.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.StrictMode;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.ijoysoft.mediasdk.common.global.ThreadPoolMaxThread;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static final Object SIMPLE_LOCK = new Object();
    private static final String TAG = "FileUtil";

    public static String getRootDir(Context context) {
        ;
        File file = Environment.getExternalStorageDirectory();
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED && file.exists()) {
            return file.getAbsolutePath();
        }
        List<String> list = getStrongePathList(context);
        for (String str : list) {
            if (str != null && !str.equals(file.getAbsolutePath())) {
                return str;
            }
        }
        return file.getAbsolutePath();
    }

    public static String getStrongePath(Context context, String name) {
        return getRootDir(context) + File.separator + name + File.separator;
    }

    /**
     * 创建文件
     *
     * @param path      文件路径
     * @param deleteOld 当文件存在时, 是否删除旧文件重新创建
     */
    public static void createFile(String path, boolean deleteOld) {
        ;
        synchronized (SIMPLE_LOCK) {
            File file = new File(path);
            boolean isDirectory = path.endsWith(File.separator);
            if (file.exists()) {
                if (!deleteOld) {
                    return;
                }
                file.delete();
            }
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try {
                if (isDirectory) {
                    file.mkdir();
                    return;
                }
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建文件 如果文件存在, 则在文件名后加 (_1, _2, _3...)
     *
     * @param path
     * @return
     */
    public static String createFileAutoIncrement(String path) {
        ;
        synchronized (SIMPLE_LOCK) {
            final String extension = getFileExtension(path, true);
            final String str = path.substring(0, path.length() - extension.length());
            File file = new File(path);
            int i = 1;
            while (file.exists()) {
                file = new File(str + "_" + (i++) + extension);
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file.getAbsolutePath();
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean exists(String path) {
        ;
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
     * 删除文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        ;
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file2 : files) {
                    deleteFile(file2);

                }
            }
        }
        file.delete();
    }

    /**
     * 删除文件
     */
    public static void deleteFile(String path) {
        ;
        if (ObjectUtils.isEmpty(path)) {
            return;
        }
        File file = new File(path);
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file2 : files) {
                    Log.i("test", "deleteFile:" + file2.getPath());
                    deleteFile(file2.getPath());
                }
            }
        }
        file.delete();
    }

    /* 删除媒体库中数据，删除音视频文件时应调用此方法，否则数据库缓存数据导致错误 */
    public static void deleteMediaStoreItem(Context context, File file) {
        ;
        if (file.isFile()) {
            String filePath = file.getPath();
            if (filePath.endsWith(".mp4")) {
                int res = context.getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        MediaStore.Video.Media.DATA + "= \"" + filePath + "\"", null);
                if (res > 0) {
                    // file.delete();
                } else {
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } else if (filePath.endsWith(".mp3")) {
                int res = context.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        MediaStore.Audio.Media.DATA + "= \"" + filePath + "\"", null);
                if (res > 0) {
                    file.delete();
                }
            } else {
                file.delete();
            }
        }
    }

    /* 更新媒体库标题数据 */
    public static void updateMediaStoreItem(Context context, String oldTitle, String newTitle) {
        ;
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.TITLE, newTitle);
        int res = context.getContentResolver().update(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values,
                MediaStore.Video.Media.TITLE + "= \"" + oldTitle + "\"", null);
    }

    public static void updateMediaStoreItemDate(Context context, String oldDateTaken, String newDateTaken) {
        ;
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.DATE_TAKEN, newDateTaken);
        int res = context.getContentResolver().update(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values,
                MediaStore.Video.Media.DATE_TAKEN + "=\"" + oldDateTaken + "\"", null);
    }

    /* 媒体库插入新媒体 */
    public static void insertMediaStoreItem(Context context, String path) {
        ;
        // ContentValues values = new ContentValues();
        // values.put(MediaStore.Video.Media.DATA, path);
        // context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

        ContentValues values = new ContentValues();
        // 媒体文件的标题
        values.put(MediaStore.Video.VideoColumns.TITLE, "我的媒体文件");
        // 时间戳
        values.put(MediaStore.Video.VideoColumns.DATE_ADDED, System.currentTimeMillis() / 1000);
        // 文件类型
        values.put(MediaStore.Video.VideoColumns.MIME_TYPE, "video/mp4");
        // 指定文件路径。必须是绝对路径
        values.put(MediaStore.Video.Media.DATA, path);

        // 把文件插入到媒体库ContentProvider中
        ContentResolver resolver = context.getContentResolver();
        Uri uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

        // 发送广播。通知此媒体文件已经可以用啦
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }

    /**
     * @param context  环境
     * @param filename filename是我们的文件全名，包括后缀哦
     */
    public static void updateGallery(Context context, String filename) {
        ;
        MediaScannerConnection.scanFile(context, new String[]{filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        LogUtils.i("MediaScannerConnection", "onScanCompleted: " + path);
                    }
                });
    }


    public static void updateGalleryByBroadcast(Context context, String path) {
        ;
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(new File(path)));
        context.sendBroadcast(intent);
    }

    /**
     * 获取文件名(不带后缀)
     * 不影响性能
     *
     * @param path
     * @return
     */
    public static String getFileName(String path) {
        if (TextUtils.isEmpty(path)) {
            return path;
        }
        int index1 = path.lastIndexOf(File.separator);
        if (index1 < 0 || index1 == path.length() - 1) {
            index1 = 0;
        }
        int index2 = path.lastIndexOf(".");
        if (index2 <= index1) {
            index2 = path.length();
        }
        return path.substring(index1 + 1, index2);
    }

    /**
     * 获取最后一级文件目录名(不带后缀)
     *
     * @param path /sdcard/aaa/bbb/cccc/ddd.png
     * @return cccc
     */
    public static String getDirName(String path) {
        ;
        if (TextUtils.isEmpty(path)) {
            return path;
        }
        File file = new File(path).getParentFile();
        if (file != null) {
            return file.getName();
        }
        return null;
    }

    public static String getFileMimeType(String fileName) {
        ;
        return URLConnection.getFileNameMap().getContentTypeFor(fileName);
    }

    /**
     * 获取文件后缀名
     *
     * @param path
     * @param containDot 是否包含点 如果是返回(.jpg) 否则 (jpg)
     * @return
     */
    public static String getFileExtension(String path, boolean containDot) {
        ;
        if (!TextUtils.isEmpty(path) && path.contains(".")) {
            int i = path.lastIndexOf('.');
            if (i > 0 && i < path.length() - 1) {
                return path.substring(containDot ? i : i + 1);
            }
        }
        return "";
    }


    /**
     * 获取所有sd卡路径
     *
     * @param context
     * @return
     */
    public static List<String> getStrongePathList(Context context) {
        ;
        List<String> pathList = new ArrayList<String>();
        String[] paths = null;
        StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Method mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");
            paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final long minSize = 128 * 1024 * 1024;
        if (paths != null) {
            for (String path : paths) {
                if (queryStrongInfo(path).totalSize >= minSize) {
                    pathList.add(path);
                }
            }
        }
        return pathList;
    }

    /**
     * 获取指定目录的存储信息
     *
     * @param path
     * @return
     */
    @SuppressWarnings("deprecation")
    public static StrongeInfo queryStrongInfo(String path) {
        ;
        StrongeInfo info = new StrongeInfo();
        try {
            StatFs stat = new StatFs(path);
            long blockSize = stat.getBlockSize();
            info.availableSize = blockSize * stat.getAvailableBlocks();
            info.totalSize = blockSize * stat.getBlockCount();
            info.usedSize = info.totalSize - info.availableSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 描述：获取可用内存.
     *
     * @param context
     * @return
     */
    public static long getAvailMemory(Context context) {
        ;
        // 获取android当前可用内存大小
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        // 当前系统可用内存 ,将获得的内存大小规格化
        return memoryInfo.availMem;
    }

    /**
     * 描述：总内存.
     *
     * @param context
     * @return
     */
    public static long getTotalMemory(Context context) {
        ;
        // 系统内存信息文件
        String file = "/proc/meminfo";
        String memInfo;
        String[] strs;
        long memory = 0;

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader, 8192);
            // 读取meminfo第一行，系统内存大小
            memInfo = bufferedReader.readLine();
            strs = memInfo.split("\\s+");
            // 获得系统总内存，单位KB
            memory = Integer.valueOf(strs[1]).intValue();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Byte转位KB或MB
        return memory * 1024;
    }

    public static void saveBitmap(Bitmap bitmap, String storePath) {
        ;
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        File f = new File(storePath);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            bitmap.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * png为全文件，无压缩
     *
     * @param bitmap
     * @param storePath
     */
    public static void saveBitmapPng(Bitmap bitmap, String storePath) {
        ;
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        File f = new File(storePath);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            bitmap.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新文件到媒体库(文件操作完成后，将其在媒体库内更新)
     *
     * @param context
     * @param filePath 文件全路径
     */
    public static void scanFile(Context context, String filePath) {
        ;
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        context.sendBroadcast(scanIntent);
    }


    public static String getVideoDuration(String path) {
        ;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        String duration = "0";
        try {
            mediaMetadataRetriever.setDataSource(path);
            duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            mediaMetadataRetriever.release();
        } catch (Exception e) {
            e.printStackTrace();
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }

        return duration;
    }

    public static class StrongeInfo {
        /**
         * 可用空间大小
         */
        public long availableSize;
        /**
         * 总大小
         */
        public long totalSize;
        /**
         * 已用空间大小
         */
        public long usedSize;
    }


    public static void reName(String path, String toName) {
        ;
        File file = new File(path);
        File reNameFile = new File(toName);
        file.renameTo(reNameFile);
    }

    // 拷贝文件
    public static void copyFile(File source, File dest) {
        ;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(source);
            outputStream = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void copyFile(String source, String dest) {
        ;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(source);
            outputStream = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static void copyFileBackGround(String source, String dest, Runnable callback) {
        ;
        ThreadPoolMaxThread.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                copyFile(source, dest);
                callback.run();
            }
        });
    }

    /**
     * 获取手机中所有sd卡路径
     */
    public static List<String> initRootFiles(Context context) {
        ;
        String[] paths = null;
        List<String> pathList = new ArrayList<>();
        StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Method mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");
            paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (paths != null) {
            for (String path : paths) {
                if (isDirWritable(path)) {
                    pathList.add(path);
                }
            }
        }
        return pathList;
    }

    public static boolean isDirWritable(String path) {
        ;
        boolean result = true;
        File file = new File(path, System.currentTimeMillis() + ".txt");
        if (file.exists()) {
            result = file.delete();
        }
        try {
            if (result && file.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(new byte[]{0x01});
                fos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            result = file.delete();
        }
        return result;
    }

    /**
     * 检查外部是否MP3
     *
     * @param path
     * @return
     */
    public static boolean checkAudioLossle(String path) {
        ;
        if (path == null || path.isEmpty()) {
            return false;
        }
        if (checkExistAudioMp3(path)) {
            return false;
        }
        if (!path.endsWith(".mp3")) {
            return true;
        }
        return false;
    }

    /**
     * 检查内部是否MP3
     *
     * @param path
     * @return
     */
    public static boolean checkExistAudioMp3(String path) {
        ;
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
//             Log.i("test", format.getString(MediaFormat.KEY_MIME));
            if (format.getString(MediaFormat.KEY_MIME).startsWith("audio/mpeg")) {
                exist = true;
                break;
            }
        }
        return exist;
    }


    private static Uri getFileContentUri(Context context, File file) {
        ;
        String volumeName = "external";
        String filePath = file.getAbsolutePath();
        String[] projection = new String[]{MediaStore.Files.FileColumns._ID};
        Uri uri = null;

        Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri(volumeName), projection,
                MediaStore.Images.Media.DATA + "=? ", new String[]{filePath}, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
                uri = MediaStore.Files.getContentUri(volumeName, id);
            }
            cursor.close();
        }

        return uri;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        ;
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        Uri uri = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/images/media");
                uri = Uri.withAppendedPath(baseUri, "" + id);
            }

            cursor.close();
        }

        if (uri == null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, filePath);
            uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }

        return uri;
    }

    private static Uri getVideoContentUri(Context context, File videoFile) {
        ;
        Uri uri = null;
        String filePath = videoFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Video.Media._ID}, MediaStore.Video.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/video/media");
                uri = Uri.withAppendedPath(baseUri, "" + id);
            }

            cursor.close();
        }

        if (uri == null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.DATA, filePath);
            uri = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        }

        return uri;
    }

    private static Uri getAudioContentUri(Context context, File audioFile) {
        ;
        Uri uri = null;
        String filePath = audioFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media._ID}, MediaStore.Audio.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/audio/media");
                uri = Uri.withAppendedPath(baseUri, "" + id);
            }

            cursor.close();
        }
        if (uri == null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Audio.Media.DATA, filePath);
            uri = context.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
        }

        return uri;
    }

    private static Uri forceGetFileUri(File shareFile) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                @SuppressLint("PrivateApi")
                Method rMethod = StrictMode.class.getDeclaredMethod("disableDeathOnFileUriExposure");
                rMethod.invoke(null);
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }

        return Uri.parse("file://" + shareFile.getAbsolutePath());
    }

}
