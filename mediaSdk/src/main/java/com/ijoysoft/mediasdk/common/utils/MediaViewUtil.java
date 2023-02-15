package com.ijoysoft.mediasdk.common.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import com.ijoysoft.mediasdk.common.global.ThreadPoolMaxThread;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaType;
import com.ijoysoft.mediasdk.module.entity.VideoMediaItem;
import com.ijoysoft.mediasdk.module.playControl.MediaSeriesFrameCallBack;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.THUMTAIL_WIDTH;

/**
 * 当前播放页面的数据转换或者获取
 * 考虑到用了线程回调的隐式强引用，所以移除静态方法，改为对象方法，记得清理回调
 */
public class MediaViewUtil {
    //视频裁剪所有的长度10帧
    private MediaSeriesFrameCallBack videoTrimFixCallback;
    //单文件固定10帧
    private MediaSeriesFrameCallBack singleFixCallback;
    //单片段所有帧
    private MediaSeriesFrameCallBack singleAllCallback;
    //获取第一帧
    private MediaSeriesFrameCallBack firstCoverCallback;
    //所有的帧
    private MediaSeriesFrameCallBack allFrameCallback;


    public MediaViewUtil() {
    }

    /**
     * 获取视频裁剪时的固定10帧图片
     * 方案：首为取一帧图
     * 中间八张按照时间分布去取，小于10s取全部
     *
     * @param callBack
     */
    public void getVideoTrimFrame(final List<MediaItem> mediaList, MediaItem currentMediaItem,
                                  int totalTime, MediaSeriesFrameCallBack callBack) {
        this.videoTrimFixCallback = callBack;
        if (totalTime < 10000 && mediaList.size() > 0) {
            getMediaSecondFrame(mediaList, totalTime, videoTrimFixCallback);
            return;
        }

        final long[] frames = new long[10];
        if (totalTime <= 10000) {
            for (int i = 0; i < totalTime / 1000; i++) {
                frames[i] = i;
            }
        } else {
            frames[9] = totalTime / 1000;
            int timeTrim = (totalTime - 2000) / 8000;
            for (int i = 1; i < 9; i++) {
                frames[i] = i * timeTrim;
            }
        }
        // 单个文件时候
        if (mediaList.size() == 1) {
            ThreadPoolMaxThread.getInstance().execute(() -> {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                try {
                    retriever.setDataSource(((VideoMediaItem) currentMediaItem).getFinalPath());
                } catch (Exception e) {
                    retriever.release();
                    return;
                }
                Bitmap bitmap;
                for (int i = 0; i < frames.length; i++) {
                    if (i > 0 && frames[i] == 0) {
                        continue;
                    }
                    bitmap = BitmapUtil.aspectScaleBitmap(retriever.getFrameAtTime(frames[i] * 1000000), 150, 200);
                    if (bitmap != null) {
                        if (videoTrimFixCallback != null) {
                            videoTrimFixCallback.callback(i, bitmap);
                        }
                    }
                }
                retriever.release();
            });
            return;
        }
        // 多个媒体文件
        ThreadPoolMaxThread.getInstance().execute(() -> {
            long duration = 0;
            int currentDuration;
            int index = 0;
            Bitmap bitmap;
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            for (int i = 0; i < mediaList.size(); i++) {
                try {
                    String path = mediaList.get(i).getPath();
                    retriever.setDataSource(path);
                } catch (Exception e) {
                    retriever.release();
                    e.printStackTrace();
                    return;
                }
                currentDuration = (int) duration / 1000;
                duration += mediaList.get(i).getDuration();
                while (index < 10 && frames[index] <= duration / 1000) {
                    if (index > 0 && frames[index] == 0) {
                        break;
                    }
                    bitmap = BitmapUtil.aspectScaleBitmap(
                            retriever.getFrameAtTime((frames[index] - currentDuration) * 1000000), 150, 200);
                    if (bitmap != null && videoTrimFixCallback != null) {
                        videoTrimFixCallback.callback(index, bitmap);
                    }
                    index++;
                }
            }
            retriever.release();
        });
    }


    /**
     * 固定10帧
     *
     * @param mediaItem
     */
    public void getSingleFileFrame(final VideoMediaItem mediaItem, MediaSeriesFrameCallBack callBack) {
        this.singleFixCallback = callBack;
        int frameSize = 10;
        // 单个文件时候
        ThreadPoolMaxThread.getInstance().execute(() -> {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                retriever.setDataSource(mediaItem.getFinalPath());
            } catch (Exception e) {
                retriever.release();
                return;
            }
            Bitmap bitmap;
            for (int i = 0; i < frameSize; i++) {
                if (i * 1000 > mediaItem.getDuration()) {
                    break;
                }
                bitmap = BitmapUtil.aspectScaleBitmap(retriever.getFrameAtTime(i * 1000000), 150, 200);
                if (bitmap != null && singleFixCallback != null) {
                    singleFixCallback.callback(i, bitmap);
                }
            }
            retriever.release();
        });
    }

    /**
     * 单片段视频的秒帧图
     *
     * @param callBack
     * @return
     */
    public void getVideoFrameSpecifySecond(final MediaItem currentMediaItem, int currentPosition, final MediaSeriesFrameCallBack callBack) {
        this.singleAllCallback = callBack;
        if (!(currentMediaItem instanceof VideoMediaItem)) {
            LogUtils.e("", "current media is not video,can't get video frame");
            return;
        }
        ThreadPoolMaxThread.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                MediaMetadataRetriever retriever = null;
                int second = currentPosition / 1000;
                try {
                    retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(((VideoMediaItem) currentMediaItem).getFinalPath());
                    Bitmap bitmap = retriever.getFrameAtTime(second * 1000000);
                    if (singleAllCallback != null) {
                        singleAllCallback.callback(second, bitmap);
                    }
                    retriever.release();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (retriever != null) {
                        retriever.release();
                    }
                }

            }
        });
    }


    /**
     * 获取播放源的时长和第一帧
     *
     * @param callBack
     */
    public void getCoverFrameAndDuration(final List<MediaItem> mediaList, MediaItem currentMediaItem,
                                         int totalTime, MediaSeriesFrameCallBack callBack) {
        this.firstCoverCallback = callBack;
        if (ObjectUtils.isEmpty(mediaList)) {
            return;
        }
        Bitmap bitmap;
        if (mediaList.get(0).isImage()) {
            if (mediaList.get(0).getBitmap() == null || mediaList.get(0).getBitmap().isRecycled()) {
                if (firstCoverCallback != null) {
                    firstCoverCallback.callback(totalTime, null);
                }
                return;
            }
            if (!mediaList.get(0).isThumbEmpty()) {
                if (firstCoverCallback != null) {
                    firstCoverCallback.callback(totalTime, mediaList.get(0).getThumCache().get());
                }
                return;
            }
            bitmap = BitmapUtil.centerSquareScaleBitmap(mediaList.get(0).getBitmap(), THUMTAIL_WIDTH);
            mediaList.get(0).setThumCache(new WeakReference<>(bitmap));
            if (firstCoverCallback != null) {
                firstCoverCallback.callback(totalTime, bitmap);
            }
            return;
        }
        if (((VideoMediaItem) mediaList.get(0)).getThumbnails() != null && ((VideoMediaItem) mediaList.get(0)).getThumbnails().get(0) != null) {
            bitmap = ((VideoMediaItem) mediaList.get(0)).getThumbnails().get(0);
            if (firstCoverCallback != null) {
                firstCoverCallback.callback(totalTime, bitmap);
            }
            return;
        }
        ThreadPoolMaxThread.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                try {
                    retriever.setDataSource(((VideoMediaItem) currentMediaItem).getFinalPath());
                    int frameSecond = 1000000;
                    if (currentMediaItem.getDuration() > 3000) {
                        frameSecond = 3000000;
                    }
                    if (firstCoverCallback != null) {
                        firstCoverCallback.callback(totalTime, retriever.getFrameAtTime(frameSecond));
                    }
                    retriever.release();
                } catch (Exception e) {
                    retriever.release();
                    return;
                }
            }
        });


    }


    /**
     * 获取整体的每秒帧图
     */
    public void getMediaSecondFrame(final List<MediaItem> mediaList, int totalTime, final MediaSeriesFrameCallBack callBack) {
        this.allFrameCallback = callBack;
        if (ObjectUtils.isEmpty(mediaList)) {
            return;
        }
        if (totalTime < 1000) {
            ThreadPoolMaxThread.getInstance().execute(() -> {
                Bitmap bitmap;
                if (mediaList.get(0).isImage()) {
                    if (!mediaList.get(0).isThumbEmpty()) {
                        if (allFrameCallback != null) {
                            allFrameCallback.callback(0, mediaList.get(0).getThumCache().get());
                            allFrameCallback.onFinish();
                        }
                        return;
                    }
                    bitmap = BitmapUtil.centerSquareScaleBitmap(mediaList.get(0).getBitmap(),
                            THUMTAIL_WIDTH);
                    mediaList.get(0).setThumCache(new WeakReference<>(bitmap));
                    if (allFrameCallback != null) {
                        allFrameCallback.callback(0, bitmap);
                        allFrameCallback.onFinish();
                    }
                    return;
                }
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(mediaList.get(0).getPath());
                bitmap = BitmapUtil.centerSquareScaleBitmap(retriever.getFrameAtTime(0), THUMTAIL_WIDTH);
                if (allFrameCallback != null) {
                    allFrameCallback.callback(0, bitmap);
                    allFrameCallback.onFinish();
                }
                retriever.release();
            });
            return;
        }

        ThreadPoolMaxThread.getInstance().execute(() -> {
            Bitmap bitmap = null;
            int remainMs = 0;
            int index = 0;
            int frameSize;
            int photoDuration;
            List<Bitmap> tooShortBitmaps = new ArrayList<>();
            for (int i = 0; i < mediaList.size(); i++) {
//                    LogUtils.d(TAG, "getMediaDataPerSecondFrame----" + i);
                mediaList.size();
                if (mediaList.get(i).getMediaType() == MediaType.PHOTO) {
                    photoDuration = (int) (mediaList.get(i).getFinalDuration() + remainMs);
                    frameSize = photoDuration / 1000;
                    if (photoDuration % 1000 != 0) {
                        remainMs = photoDuration % 1000;
                    } else {
                        remainMs = 0;
                    }
                    for (int j = 0; j < frameSize; j++) {
                        //取缩略图缓存
                        if (!mediaList.get(i).isThumbEmpty()) {
                            if (allFrameCallback != null) {
                                allFrameCallback.callback(index, mediaList.get(i).getThumCache().get());
                            }
                            index++;
                            continue;
                        }
                        Bitmap originBitmap = mediaList.get(i).getBitmap();
                        if (originBitmap == null) {
                            continue;
                        }
                        int width = originBitmap.getWidth();
                        int height = originBitmap.getHeight();
                        if (width != 0 && height != 0) {
                            bitmap = BitmapUtil.rotateBitmap(
                                    BitmapUtil.centerSquareScaleBitmap(originBitmap, THUMTAIL_WIDTH),
                                    mediaList.get(i).getAfterRotation());
                        } else {
                            bitmap = BitmapUtil.rotateBitmap(
                                    BitmapUtil.centerSquareScaleBitmap(originBitmap, THUMTAIL_WIDTH),
                                    mediaList.get(i).getAfterRotation());
                        }
                        if (allFrameCallback != null) {
                            allFrameCallback.callback(index, bitmap);
                        }
                        if (mediaList.get(i).isThumbEmpty()) {
                            mediaList.get(i).setThumCache(new WeakReference<>(bitmap));
                        }
                        index++;
                    }
                } else {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    frameSize = (int) (mediaList.get(i).getFinalDuration() / 1000);
                    if (mediaList.get(i).getFinalDuration() % 1000 != 0) {
                        remainMs += (int) (mediaList.get(i).getFinalDuration() % 1000);
                    }
                    for (int j = 0; j < frameSize; j++) {
                        if (((VideoMediaItem) mediaList.get(i)).getThumbnails() != null
                                && ((VideoMediaItem) mediaList.get(i)).getThumbnails().get(j) != null
                                && ObjectUtils.isEmpty(mediaList.get(i).getTrimPath())) {
                            if (allFrameCallback != null) {
                                allFrameCallback.callback(index, ((VideoMediaItem) mediaList.get(i)).getThumbnails().get(j));
                            }
                            index++;
                            continue;
                        }
                        try {
                            retriever.setDataSource(((VideoMediaItem) mediaList.get(i)).getFinalPath());
                            Bitmap videoOriginBitmap = retriever.getFrameAtTime(j * 1000000L);
                            if (videoOriginBitmap == null) {
                                index++;
                                continue;
                            }
                            bitmap = BitmapUtil.centerSquareScaleBitmap(videoOriginBitmap, THUMTAIL_WIDTH);
                            ((VideoMediaItem) mediaList.get(i)).getThumbnails().put(j, bitmap);
                            if (allFrameCallback != null) {
                                allFrameCallback.callback(index, bitmap);
                            }
                            index++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (frameSize == 0) {
                        retriever.setDataSource(mediaList.get(i).getPath());
                        bitmap = BitmapUtil.rotateBitmap(
                                BitmapUtil.centerSquareScaleBitmap(retriever.getFrameAtTime(0), THUMTAIL_WIDTH),
                                mediaList.get(i).getAfterRotation());

                    }
                    tooShortBitmaps.add(bitmap);
                    retriever.release();
                }
            }
            if (remainMs / 1000 > 0) {
                for (int i = 0; i < remainMs / 1000; i++) {
                    if (tooShortBitmaps.size() > i) {
                        if (allFrameCallback != null) {
                            allFrameCallback.callback(index, tooShortBitmaps.get(i));
                        }
                        index++;
                    }
                }
            }
            if (allFrameCallback != null) {
                allFrameCallback.onFinish();
            }
        });
    }

    /**
     * 清理回调，谨防泄露
     */
    public void onDestroy() {
        videoTrimFixCallback = null;
        singleFixCallback = null;
        singleAllCallback = null;
        firstCoverCallback = null;
        allFrameCallback = null;

    }

}
