package com.ijoysoft.mediasdk.common.utils

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.global.ThreadPoolMaxThread
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.MediaType
import com.ijoysoft.mediasdk.module.entity.VideoMediaItem
import com.ijoysoft.mediasdk.module.playControl.MediaSeriesFrameCallBack
import java.lang.ref.WeakReference

/**
 * 当前播放页面的数据转换或者获取
 * 考虑到用了线程回调的隐式强引用，所以移除静态方法，改为对象方法，记得清理回调
 */
class MediaViewUtil {
    //视频裁剪所有的长度10帧
    private var videoTrimFixCallback: MediaSeriesFrameCallBack? = null

    //单文件固定10帧
    private var singleFixCallback: MediaSeriesFrameCallBack? = null

    //单片段所有帧
    private var singleAllCallback: MediaSeriesFrameCallBack? = null

    //获取第一帧
    private var firstCoverCallback: MediaSeriesFrameCallBack? = null

    //所有的帧
    private var allFrameCallback: MediaSeriesFrameCallBack? = null

    /**
     * 获取视频裁剪时的固定10帧图片
     * 方案：首为取一帧图
     * 中间八张按照时间分布去取，小于10s取全部
     *
     * @param callBack
     */
    fun getVideoTrimFrame(mediaList: List<MediaItem?>, currentMediaItem: MediaItem,
                          totalTime: Int, callBack: MediaSeriesFrameCallBack?) {
        videoTrimFixCallback = callBack
        if (totalTime < 10000 && mediaList.size > 0) {
            getMediaSecondFrame(mediaList, totalTime, videoTrimFixCallback)
            return
        }
        val frames = LongArray(10)
        if (totalTime <= 10000) {
            for (i in 0 until totalTime / 1000) {
                frames[i] = i.toLong()
            }
        } else {
            frames[9] = (totalTime / 1000).toLong()
            val timeTrim = (totalTime - 2000) / 8000
            for (i in 1..8) {
                frames[i] = (i * timeTrim).toLong()
            }
        }
        // 单个文件时候
        if (mediaList.size == 1) {
            ThreadPoolMaxThread.getInstance().execute {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource((currentMediaItem as VideoMediaItem).finalPath)
                var bitmap: Bitmap?
                for (i in frames.indices) {
                    if (i > 0 && frames[i] == 0L) {
                        continue
                    }
                    bitmap = BitmapUtil.aspectScaleBitmap(retriever.getFrameAtTime(frames[i] * 1000000), 150, 200)
                    if (bitmap != null) {
                        if (videoTrimFixCallback != null) {
                            videoTrimFixCallback!!.callback(i, bitmap)
                        }
                    }
                }
                retriever.release()
            }
            return
        }
        // 多个媒体文件
        ThreadPoolMaxThread.getInstance().execute {
            var duration: Long = 0
            var currentDuration: Int
            var index = 0
            var bitmap: Bitmap?
            val retriever = MediaMetadataRetriever()
            for (i in mediaList.indices) {
                val path = mediaList[i]!!.path
                retriever.setDataSource(path)
                currentDuration = duration.toInt() / 1000
                duration += mediaList[i]!!.duration
                while (index < 10 && frames[index] <= duration / 1000) {
                    if (index > 0 && frames[index] == 0L) {
                        break
                    }
                    bitmap = BitmapUtil.aspectScaleBitmap(
                        retriever.getFrameAtTime((frames[index] - currentDuration) * 1000000), 150, 200)
                    if (bitmap != null && videoTrimFixCallback != null) {
                        videoTrimFixCallback!!.callback(index, bitmap)
                    }
                    index++
                }
            }
            retriever.release()
        }
    }

    /**
     * 固定10帧
     *
     * @param mediaItem
     */
    fun getSingleFileFrame(mediaItem: VideoMediaItem, callBack: MediaSeriesFrameCallBack?) {
        singleFixCallback = callBack
        val frameSize = 10
        // 单个文件时候
        ThreadPoolMaxThread.getInstance().execute {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(mediaItem.finalPath)
            var bitmap: Bitmap?
            for (i in 0 until frameSize) {
                if (i * 1000 > mediaItem.duration) {
                    break
                }
                bitmap = BitmapUtil.aspectScaleBitmap(retriever.getFrameAtTime((i * 1000000).toLong()), 150, 200)
                if (bitmap != null && singleFixCallback != null) {
                    singleFixCallback!!.callback(i, bitmap)
                }
            }
            retriever.release()
        }
    }

    /**
     * 单片段视频的秒帧图
     *
     * @param callBack
     * @return
     */
    fun getVideoFrameSpecifySecond(currentMediaItem: MediaItem?, currentPosition: Int, callBack: MediaSeriesFrameCallBack?) {
        singleAllCallback = callBack
        if (currentMediaItem !is VideoMediaItem) {
            LogUtils.e("", "current media is not video,can't get video frame")
            return
        }
        ThreadPoolMaxThread.getInstance().execute {
            var retriever: MediaMetadataRetriever? = null
            val second = currentPosition / 1000
            try {
                retriever = MediaMetadataRetriever()
                retriever.setDataSource(currentMediaItem.finalPath)
                val bitmap = retriever.getFrameAtTime((second * 1000000).toLong())
                if (singleAllCallback != null) {
                    singleAllCallback!!.callback(second, bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                retriever?.release()
            }
        }
    }

    /**
     * 获取播放源的时长和第一帧
     *
     * @param callBack
     */
    fun getCoverFrameAndDuration(mediaList: List<MediaItem?>, currentMediaItem: MediaItem,
                                 totalTime: Int, callBack: MediaSeriesFrameCallBack?) {
        firstCoverCallback = callBack
        if (ObjectUtils.isEmpty(mediaList)) {
            return
        }
        val bitmap: Bitmap?
        if (mediaList[0]!!.isImage) {
            if (mediaList[0]!!.bitmap == null || mediaList[0]!!.bitmap.isRecycled) {
                if (firstCoverCallback != null) {
                    firstCoverCallback!!.callback(totalTime, null)
                }
                return
            }
            if (!mediaList[0]!!.isThumbEmpty) {
                if (firstCoverCallback != null) {
                    firstCoverCallback!!.callback(totalTime, mediaList[0]!!.thumCache.get())
                }
                return
            }
            bitmap = BitmapUtil.centerSquareScaleBitmap(mediaList[0]!!.bitmap, ConstantMediaSize.THUMTAIL_WIDTH)
            mediaList[0]!!.thumCache = WeakReference(bitmap)
            if (firstCoverCallback != null) {
                firstCoverCallback!!.callback(totalTime, bitmap)
            }
            return
        }
        if ((mediaList[0] as VideoMediaItem?)!!.thumbnails != null && (mediaList[0] as VideoMediaItem?)!!.thumbnails[0] != null) {
            bitmap = (mediaList[0] as VideoMediaItem?)!!.thumbnails[0]
            if (firstCoverCallback != null) {
                firstCoverCallback!!.callback(totalTime, bitmap)
            }
            return
        }
        ThreadPoolMaxThread.getInstance().execute {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource((currentMediaItem as VideoMediaItem).finalPath)
            var frameSecond = 1000000
            if (currentMediaItem.getDuration() > 3000) {
                frameSecond = 3000000
            }
            if (firstCoverCallback != null) {
                firstCoverCallback!!.callback(totalTime, retriever.getFrameAtTime(frameSecond.toLong()))
            }
            retriever.release()
        }
    }

    /**
     * 获取整体的每秒帧图
     */
    fun getMediaSecondFrame(mediaList: List<MediaItem?>, totalTime: Int, callBack: MediaSeriesFrameCallBack?) {
        allFrameCallback = callBack
        if (ObjectUtils.isEmpty(mediaList)) {
            return
        }
        if (totalTime < 1000) {
            ThreadPoolMaxThread.getInstance().execute {
                val bitmap: Bitmap
                if (mediaList[0]!!.isImage) {
                    if (!mediaList[0]!!.isThumbEmpty) {
                        if (allFrameCallback != null) {
                            allFrameCallback!!.callback(0, mediaList[0]!!.thumCache.get())
                            allFrameCallback!!.onFinish()
                        }
                        return@execute
                    }
                    bitmap = BitmapUtil.centerSquareScaleBitmap(mediaList[0]!!.bitmap,
                        ConstantMediaSize.THUMTAIL_WIDTH)
                    mediaList[0]!!.thumCache = WeakReference(bitmap)
                    if (allFrameCallback != null) {
                        allFrameCallback!!.callback(0, bitmap)
                        allFrameCallback!!.onFinish()
                    }
                    return@execute
                }
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(mediaList[0]!!.path)
                bitmap = BitmapUtil.centerSquareScaleBitmap(retriever.getFrameAtTime(0), ConstantMediaSize.THUMTAIL_WIDTH)
                if (allFrameCallback != null) {
                    allFrameCallback!!.callback(0, bitmap)
                    allFrameCallback!!.onFinish()
                }
                retriever.release()
            }
            return
        }
        ThreadPoolMaxThread.getInstance().execute {
            var bitmap: Bitmap? = null
            var remainMs = 0
            var index = 0
            var frameSize: Int
            var photoDuration: Int
            val tooShortBitmaps: MutableList<Bitmap?> = ArrayList()
            for (i in mediaList.indices) {
//                    LogUtils.d(TAG, "getMediaDataPerSecondFrame----" + i);
                mediaList.size
                if (mediaList[i]!!.mediaType == MediaType.PHOTO) {
                    photoDuration = (mediaList[i]!!.finalDuration + remainMs).toInt()
                    frameSize = photoDuration / 1000
                    remainMs = if (photoDuration % 1000 != 0) {
                        photoDuration % 1000
                    } else {
                        0
                    }
                    for (j in 0 until frameSize) {
                        //取缩略图缓存
                        if (!mediaList[i]!!.isThumbEmpty) {
                            if (allFrameCallback != null) {
                                allFrameCallback!!.callback(index, mediaList[i]!!.thumCache.get())
                            }
                            index++
                            continue
                        }
                        val originBitmap = mediaList[i]!!.bitmap ?: continue
                        val width = originBitmap.width
                        val height = originBitmap.height
                        bitmap = if (width != 0 && height != 0) {
                            BitmapUtil.rotateBitmap(
                                BitmapUtil.centerSquareScaleBitmap(originBitmap, ConstantMediaSize.THUMTAIL_WIDTH),
                                mediaList[i]!!.afterRotation)
                        } else {
                            BitmapUtil.rotateBitmap(
                                BitmapUtil.centerSquareScaleBitmap(originBitmap, ConstantMediaSize.THUMTAIL_WIDTH),
                                mediaList[i]!!.afterRotation)
                        }
                        if (allFrameCallback != null) {
                            allFrameCallback!!.callback(index, bitmap)
                        }
                        if (mediaList[i]!!.isThumbEmpty) {
                            mediaList[i]!!.thumCache = WeakReference(bitmap)
                        }
                        index++
                    }
                } else {
                    val retriever = MediaMetadataRetriever()
                    frameSize = (mediaList[i]!!.finalDuration / 1000).toInt()
                    if (mediaList[i]!!.finalDuration % 1000 != 0L) {
                        remainMs += (mediaList[i]!!.finalDuration % 1000).toInt()
                    }
                    for (j in 0 until frameSize) {
                        if ((mediaList[i] as VideoMediaItem?)!!.thumbnails != null && (mediaList[i] as VideoMediaItem?)!!.thumbnails[j] != null && ObjectUtils.isEmpty(mediaList[i]!!.trimPath)) {
                            if (allFrameCallback != null) {
                                allFrameCallback!!.callback(index, (mediaList[i] as VideoMediaItem?)!!.thumbnails[j])
                            }
                            index++
                            continue
                        }
                        retriever.setDataSource((mediaList[i] as VideoMediaItem?)!!.finalPath)
                        val videoOriginBitmap = retriever.getFrameAtTime(j * 1000000L)
                        if (videoOriginBitmap == null) {
                            index++
                            continue
                        }
                        bitmap = BitmapUtil.centerSquareScaleBitmap(videoOriginBitmap, ConstantMediaSize.THUMTAIL_WIDTH)
                        (mediaList[i] as VideoMediaItem?)!!.thumbnails[j] = bitmap
                        if (allFrameCallback != null) {
                            allFrameCallback!!.callback(index, bitmap)
                        }
                        index++
                    }
                    if (frameSize == 0) {
                        retriever.setDataSource(mediaList[i]!!.path)
                        bitmap = BitmapUtil.rotateBitmap(
                            BitmapUtil.centerSquareScaleBitmap(retriever.getFrameAtTime(0), ConstantMediaSize.THUMTAIL_WIDTH),
                            mediaList[i]!!.afterRotation)
                    }
                    tooShortBitmaps.add(bitmap)
                    retriever.release()
                }
            }
            if (remainMs / 1000 > 0) {
                for (i in 0 until remainMs / 1000) {
                    if (tooShortBitmaps.size > i) {
                        if (allFrameCallback != null) {
                            allFrameCallback!!.callback(index, tooShortBitmaps[i])
                        }
                        index++
                    }
                }
            }
            if (allFrameCallback != null) {
                allFrameCallback!!.onFinish()
            }
        }
    }

    /**
     * 清理回调，谨防泄露
     */
    fun onDestroy() {
        videoTrimFixCallback = null
        singleFixCallback = null
        singleAllCallback = null
        firstCoverCallback = null
        allFrameCallback = null
    }
}