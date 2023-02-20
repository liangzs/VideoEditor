package com.qiusuo.videoeditor.common.data;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.mediacodec.PhoneAdatarList;
import com.qiusuo.videoeditor.base.MyApplication;
import com.qiusuo.videoeditor.common.bean.MediaEntity;
import com.qiusuo.videoeditor.common.bean.MediaSet;
import com.qiusuo.videoeditor.util.AndroidUtil;
import com.qiusuo.videoeditor.util.pinyinhelper.PinyinUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wh on 2019/4/25.
 * 媒体库查询类，在子线程中运行，避免阻塞主线程
 * <p>
 * 2022/3/18 移除文件列表的存储，点击文件时改为实时查
 *
 * <p>
 * {@date 2023年2月2日 只做文件夹查询和音频查询}
 * </P>
 */
public class ContentDataLoadTask extends AsyncTask<Void, Void, Void> {
    public static final String[] IMAGE_COLUMN = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.TITLE, MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.DATE_MODIFIED, MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT, MediaStore.Images.Media.ORIENTATION};
    public static final String[] AUDIO_COLUMN = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Images.Media.MIME_TYPE, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.IS_MUSIC,
            MediaStore.Audio.Media.DATE_MODIFIED};
    public static final String[] VIDEO_COLUMN = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION, MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.TITLE, MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.DATE_MODIFIED, MediaStore.Video.Media.WIDTH,
            MediaStore.Video.Media.HEIGHT};
    private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

    private List<MediaSet> mMediaSets = new ArrayList<>();
    private List<MediaSet> mMediaImageSets = new ArrayList<>();
    private List<MediaSet> mMediaVideoSets = new ArrayList<>();

    private List<MediaEntity> audioLists = new ArrayList<>();
    private Timer periodTimer;

    private boolean isOnDestroy;
    private OnContentDataLoadListener mOnContentDataLoadListener;
    private Object meidaObject = new Object();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mOnContentDataLoadListener != null) {
            mOnContentDataLoadListener.onStartLoad();
        }
        if (periodTimer == null) {
            periodTimer = new Timer();

        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (periodTimer != null) {
            periodTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    synchMediaData();
                }
            }, 1000, 2000);
        }
        loadAllImageMediaItem();
        loadAllVideoMediaItem();
        loadMusicMediaItem();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (mOnContentDataLoadListener != null) {
            mOnContentDataLoadListener.onFinishLoad();
            mOnContentDataLoadListener = null;
        }
        synchMediaData();
        synchAudioData();
        if (periodTimer != null) {
            periodTimer.cancel();
            periodTimer = null;
        }
    }

    /*
     * 获取所有的photo
     */
    public void loadAllImageMediaItem() {
        Cursor imageCursor = null;
        String sortBy = MediaStore.Images.Media.DATE_ADDED + " desc ";
        try {
            imageCursor = MyApplication.instance.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_COLUMN, null, null, sortBy);
            imageCursor.moveToFirst(); // 从头开始，防止扫描不出数据
            do {
                createImageMediaItem(imageCursor);
            } while (imageCursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imageCursor != null) {
                imageCursor.close();
            }
        }
    }


    /**
     * 文件夹中不在初始化数据，点击进入文件夹时，如果为空再进行实时查询
     *
     * @param imageCursor
     * @return
     */
    private void createImageMediaItem(Cursor imageCursor) {
        synchronized (meidaObject) {
            String path = imageCursor.getString(1);
            if (path.endsWith(".djvu") || path.endsWith(".tif")) {
                return;
            }

            MediaSet mediaSet = new MediaSet();
            mediaSet.bucketId = imageCursor.getInt(6);
            mediaSet.name = imageCursor.getString(7);

            addMediaSet(mMediaImageSets, mediaSet, true, path);
            addMediaSet(mMediaSets, mediaSet, true, path);
        }
    }


    /**
     * 加入文件夹
     */
    private void addMediaSet(List<MediaSet> mediaSets, MediaSet currentSet, boolean isImage, String path) {
        if (!mediaSets.contains(currentSet)) {
            mediaSets.add(new MediaSet(currentSet.getBucketId(), currentSet.getName(),
                    1, path, isImage));
        } else {
            int setIndex = mediaSets.indexOf(currentSet);
            mediaSets.get(setIndex).count++;
            if (isImage) {
                mediaSets.get(setIndex).photoCount++;
            } else {
                mediaSets.get(setIndex).videoCount++;
            }
        }
    }


    /*
     * 获取媒体中所有的视频文件
     */
    public void loadAllVideoMediaItem() {
        Cursor videoCursor = null;
        String sortBy = MediaStore.Video.Media.DATE_ADDED + " desc";
        try {
            videoCursor = MyApplication.instance.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_COLUMN,
                    null, null, sortBy);
            videoCursor.moveToFirst();
            do {
                createVideoMediaItem(videoCursor);
            } while (videoCursor.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (videoCursor != null) {
                videoCursor.close();
            }
        }
    }


    @SuppressLint("Range")
    private void createVideoMediaItem(Cursor videoCursor) {
        synchronized (meidaObject) {
            String path = videoCursor.getString(1);
            MediaSet mediaSet = new MediaSet();
            mediaSet.bucketId = videoCursor.getInt(6);
            mediaSet.name = videoCursor.getString(7);
            addMediaSet(mMediaVideoSets, mediaSet, false, path);
            addMediaSet(mMediaSets, mediaSet, false, path);
        }
    }

    /**
     * 获取所有的音乐文件
     */
    public List<MediaEntity> loadMusicMediaItem() {
        Cursor audioCursor = null;
        String sortBy = MediaStore.Audio.Media.DATE_ADDED + " desc";
        try {
            audioCursor = MyApplication.instance.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, AUDIO_COLUMN,
                    null, null, sortBy);
            while (audioCursor.moveToNext()) {
                if (isOnDestroy) {
                    break;
                }
                MediaEntity mediaItem = new MediaEntity();
                mediaItem.path = audioCursor.getString(0);
                File file = new File(mediaItem.path);
                if (!file.exists() || file.length() < 10240) {
                    continue;
                }
                if (PhoneAdatarList.checkIsFilterAudio(mediaItem.path)) {
                    continue;
                }
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
                if (mediaItem.size == null || mediaItem.size.equals("0") || mediaItem.title.startsWith(".")) {
                    continue;
                }
                if (mediaItem.isMusic == 1) {
                    audioLists.add(mediaItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (audioCursor != null) {
                audioCursor.close();
            }
        }
        return audioLists;
    }

    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        System.out.println("w" + bitmap.getWidth());
        System.out.println("h" + bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }


    /**
     * 去除视频数据
     *
     * @param mediaItems
     * @return
     */
    private List<MediaEntity> queryRemoveVideo(List<MediaEntity> mediaItems) {
        List<MediaEntity> imageEntities = new ArrayList<>();
        for (MediaEntity mediaItem : mediaItems) {
            if (mediaItem.type == MediaEntity.TYPE_IMAGE) {
                imageEntities.add(mediaItem);
            }
        }
        return imageEntities;
    }

    public void onDestroy() {
        isOnDestroy = true;
        cancelPeriodTimer();
    }

    public void cancelPeriodTimer() {
        if (periodTimer != null) {
            periodTimer.cancel();
            periodTimer = null;
        }
    }


    public List<String> getVideoDates() {
        List<String> list = new ArrayList<>();
        sortByDateforString(list);
        return list;
    }


    public void setmMediaVideoSets(List<MediaSet> mMediaVideoSets) {
        this.mMediaVideoSets = mMediaVideoSets;
    }


    /**
     * 对日期进行排序，日期默认是降序
     */
    private void sortByDateforString(List<String> list) {
        try {
            Collections.sort(list, (o1, o2) -> {
                if (o1 == null || o2 == null) {
                    return 0;
                }
                return o2.toLowerCase().compareTo(o1.toLowerCase());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步数据时加上同步锁
     */
    public void synchMediaData() {
        synchronized (meidaObject) {
            mediaSetSynch(MediaManager.INSTANCE.getMediaSets(), mMediaSets);
            mediaSetSynch(MediaManager.INSTANCE.getmMediaVideoSets(), mMediaVideoSets);
            mediaSetSynch(MediaManager.INSTANCE.getmMediaImageSets(), mMediaImageSets);
        }
    }

    /**
     * mediaset的同步，因为mediamanager会保留childDates等数据，所以不做直接替换
     */
    private void mediaSetSynch(List<MediaSet> mediaManagers, List<MediaSet> dataloads) {
        if (dataloads == null) {
            //可能有崩溃出现
            LogUtils.e("ContentDataLoadTask", "mediaSetSynch, dataloads是空的，可能有崩溃");
            return;
        }
        if (dataloads.isEmpty()) {
            return;
        }
        int i = 0;
        //省略一致情况下的操作
        while (i < mediaManagers.size() && i < dataloads.size()) {
            if (!mediaManagers.get(i).equals(dataloads.get(i))) {
                break;
            }
            i++;
        }
        if (i == dataloads.size()) {
            return;
        }
        if (i == mediaManagers.size()) {
            mediaManagers.addAll(dataloads.subList(i, dataloads.size()));
            return;
        }
        for (; i < dataloads.size(); i++) {
            MediaSet mediaSet = dataloads.get(i);
            if (!mediaManagers.contains(mediaSet)) {
                mediaManagers.add(mediaSet);
            }
        }
    }

    /**
     * 同步数据时加上同步锁
     */
    public void synchPhotoData() {
        synchronized (meidaObject) {
            mediaSetSynch(MediaManager.INSTANCE.getMediaSets(), mMediaSets);
            mediaSetSynch(MediaManager.INSTANCE.getmMediaImageSets(), mMediaImageSets);

        }
    }

    /**
     * 同步数据时加上同步锁
     */
    public void synchVideoData() {
        synchronized (meidaObject) {
            mediaSetSynch(MediaManager.INSTANCE.getMediaSets(), mMediaSets);
            mediaSetSynch(MediaManager.INSTANCE.getmMediaVideoSets(), mMediaVideoSets);
        }
    }


    public void synchAudioData() {
        MediaManager.INSTANCE.setAudioLists(getAudioLists());
    }

    public List<MediaEntity> getAudioLists() {
        return audioLists;
    }

    /**
     * 同步数据
     */
    public void copy2Current() {
        mMediaVideoSets = MediaManager.INSTANCE.getmMediaVideoSets();
    }

    /**
     * 同步视频数据，videotrimdetail
     */
    public void copyVideo2Current() {
        mMediaVideoSets = MediaManager.INSTANCE.getmMediaVideoSets();
    }

    public interface OnContentDataLoadListener {

        void onStartLoad();

        void onFinishLoad();

        /**
         * 每一秒做一次数据同步
         */
        void onPeriodLoad();
    }

    public void setmOnContentDataLoadListener(OnContentDataLoadListener mOnContentDataLoadListener) {
        this.mOnContentDataLoadListener = mOnContentDataLoadListener;
    }

}

