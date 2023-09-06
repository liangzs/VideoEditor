package com.qiusuo.videoeditor.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.qiusuo.videoeditor.common.bean.CutMusicItem;
import com.qiusuo.videoeditor.common.bean.MediaEntity;
import com.qiusuo.videoeditor.common.data.ContentDataLoadTask;
import com.qiusuo.videoeditor.util.pinyinhelper.PinyinUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by wh on 2019/5/21.
 */
public class LoadMediaUtils {

    public static MediaEntity loadSingleMedia(Context context, Uri photoUri) {
        MediaEntity mediaItem = new MediaEntity();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(photoUri, ContentDataLoadTask.IMAGE_COLUMN, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                mediaItem.id = cursor.getInt(0);
                mediaItem.path = cursor.getString(1);
                mediaItem.size = cursor.getString(2) == null ? "0" : cursor.getString(2);
                mediaItem.subscribeText = cursor.getString(3);
                mediaItem.mimeType = cursor.getString(4);
                mediaItem.type = MediaEntity.TYPE_IMAGE;
                mediaItem.title = cursor.getString(5);
                mediaItem.bucketId = cursor.getInt(6);
                mediaItem.bucketName = cursor.getString(7);
                mediaItem.dateTaken = cursor.getLong(8);
                mediaItem.dateAdd = cursor.getLong(9);
                mediaItem.dateModify = cursor.getLong(10);
                mediaItem.width = Integer.valueOf(cursor.getString(11) == null ? "0" : cursor.getString(11));
                mediaItem.height = Integer.valueOf(cursor.getString(12) == null ? "0" : cursor.getString(12));
                mediaItem.rotation = cursor.getInt(13);
            }
            cursor.close();
        }
        return mediaItem;
    }


    public static MediaEntity loadSingleVideoMedia(Context context, Uri videoUri) {
        MediaEntity mediaItem = new MediaEntity();
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null || videoUri == null) {
            return null;
        }
        try {
            Cursor cursor = contentResolver.query(videoUri, ContentDataLoadTask.VIDEO_COLUMN, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    mediaItem.id = cursor.getInt(0);
                    mediaItem.path = cursor.getString(1);
                    mediaItem.size = cursor.getString(2) == null ? "0" : cursor.getString(2);
                    mediaItem.duration = cursor.getLong(3);
                    mediaItem.mimeType = cursor.getString(4);
                    mediaItem.type = MediaEntity.TYPE_VIDEO;
                    mediaItem.title = cursor.getString(5);
                    mediaItem.dateTaken = cursor.getLong(8);
                    mediaItem.dateAdd = cursor.getLong(9);
                    mediaItem.dateModify = cursor.getLong(10);
                    mediaItem.width = Integer.valueOf(cursor.getString(11) == null ? "0" : cursor.getString(11));
                    mediaItem.height = Integer.valueOf(cursor.getString(12) == null ? "0" : cursor.getString(12));
//                mediaItem.rotation = Integer.valueOf(cursor.getString(13) == null ? "0" : cursor.getString(13));
                    break;
                } while (cursor.moveToNext());

            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mediaItem;
    }

    /**
     * 获取所有的音乐文件
     */
    public static MediaEntity loadSingleAudioMedia(Context context, Uri audioUri) {
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null || audioUri == null) {
            return null;
        }
        MediaEntity mediaItem = null;
        Cursor audioCursor = null;
        try {
            audioCursor = contentResolver.query(audioUri, ContentDataLoadTask.AUDIO_COLUMN, null, null, null);
            if (audioCursor != null && audioCursor.moveToFirst()) {
                mediaItem = new MediaEntity();
                mediaItem.path = audioCursor.getString(0);
                File file = new File(mediaItem.path);
                mediaItem.size = audioCursor.getString(1);
                mediaItem.duration = audioCursor.getLong(2);
                if (mediaItem.duration == 0) {
                    mediaItem.duration = AndroidUtil.getAudioTime(mediaItem.path);
                    LogUtils.v("ContentDataLoadTask", "mediaItem.duration:" + mediaItem.duration);
                }
                mediaItem.id = audioCursor.getInt(3);
                mediaItem.bucketId = audioCursor.getInt(4);
                mediaItem.mimeType = audioCursor.getString(5);
                mediaItem.title = audioCursor.getString(6);
                mediaItem.isMusic = audioCursor.getInt(7);
                mediaItem.dateModify = audioCursor.getLong(8);
                mediaItem.type = MediaEntity.TYPE_MUSIC;
                mediaItem.pinyinChar = '#';
                String pinyin = PinyinUtils.getPingYin(mediaItem.title);
                if (!ObjectUtils.isEmpty(pinyin)) {
                    mediaItem.pinyinChar = pinyin.charAt(0);
                    if (mediaItem.pinyinChar < 'A' || mediaItem.pinyinChar > 'Z') {
                        mediaItem.pinyinChar = '#';
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (audioCursor != null) {
                audioCursor.close();
            }
        }
        return mediaItem;
    }


    public static CutMusicItem loadInnerAudio(String path) {
        CutMusicItem cutMusicItem = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(path);
            String duration = retriever
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            cutMusicItem = new CutMusicItem();
            cutMusicItem.setTitle(FileUtil.getFileName(path));
            cutMusicItem.setPath(path);
            cutMusicItem.setOriginPath(path);
            cutMusicItem.setDuration(Long.valueOf(duration));
            cutMusicItem.setOriginDuration(cutMusicItem.getDuration());
            cutMusicItem.setVolume(100);
            cutMusicItem.setSize(new File(path).length());
            cutMusicItem.setCutEnd(cutMusicItem.getDuration());
            cutMusicItem.setCutStart(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return cutMusicItem;
    }

    public static Uri getUri(Context context, String url) {
        File tempFile = new File(url);
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            try {
                return FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", tempFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            return Uri.fromFile(tempFile);
        }
        return null;
    }


    public static MediaEntity loadSingleVideoMedia(Context context, String path) {
        if (path != null) {
            Uri uri = getUri(context, path);
            LogUtils.i("loadSingleVideoMedia", uri.getPath());
            return loadSingleVideoMedia(context, uri);
        }
        return null;
    }

    public static long scanDuration(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(path);
            String duration = retriever
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            return Long.parseLong(duration);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                retriever.release();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
