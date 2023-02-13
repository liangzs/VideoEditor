package com.qiusuo.videoeditor.module.select

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.annotation.IntDef
import androidx.lifecycle.*
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.FileUtils
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.common.utils.ObjectUtils
import com.ijoysoft.mediasdk.module.mediacodec.PhoneAdatarList
import com.ijoysoft.videoeditor.activity.SelectClipActivity
import com.ijoysoft.videoeditor.activity.videotrim.VideoTrimActivity
import com.ijoysoft.videoeditor.base.MyApplication
import com.ijoysoft.videoeditor.entity.*
import com.ijoysoft.videoeditor.model.ContentDataLoadTask
import com.ijoysoft.videoeditor.model.ContentDataObserver
import com.ijoysoft.videoeditor.model.ContentDataObserver.onUriChange
import com.ijoysoft.videoeditor.model.MediaManager
import com.ijoysoft.videoeditor.utils.Dlog
import com.ijoysoft.videoeditor.utils.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.util.*

/**
 * 照片和视频的新增的广播监听放到此类
 */
class LoadMediaViewModel : ViewModel() {

    val pageSize = 200;

    var currentPage = 0;

    /**
     * 最大页数
     */
    var pageMax = 1;

    @FolderType
    var folderType = ALL

    @MediaMode
    var mediaMode = MIX


    val IMAGE_COLUMN = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.MIME_TYPE,
        MediaStore.Images.Media.TITLE, MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_TAKEN,
        MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.DATE_MODIFIED, MediaStore.Images.Media.WIDTH,
        MediaStore.Images.Media.HEIGHT, MediaStore.Images.Media.ORIENTATION)
    val VIDEO_COLUMN = arrayOf(MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION, MediaStore.Images.Media.MIME_TYPE,
        MediaStore.Images.Media.TITLE, MediaStore.Video.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_TAKEN,
        MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.DATE_MODIFIED, MediaStore.Video.Media.WIDTH,
        MediaStore.Video.Media.HEIGHT)


    /**
     * 时间标题
     */
    val mImageDates: MutableList<String> = ArrayList() // 存放照片日期
    val mVideoDates: MutableList<String> = ArrayList() // 存放视频日期
    val allDates: MutableList<String> = ArrayList()

    /**
     * 时间标题对应的集合
     */
    val mImageHashMap: MutableMap<String, MutableList<MediaEntity>> = HashMap()
    val mVideoHashMap: MutableMap<String, MutableList<MediaEntity>> = HashMap()
    val mAllHashMap: MutableMap<String, MutableList<MediaEntity>> = HashMap()

    //总文件夹
    //private val mMediaSets: MutableList<MediaSet> = ArrayList()
    //private val mMediaImageSets: MutableList<MediaSet> = ArrayList()
    //private val mMediaVideoSets: MutableList<MediaSet> = ArrayList()

    //排序类型,名称日期等
    var mediaSortType: MediaSortType = MediaSortType.SORT_DATE

    //显示类型grid显示还是日期显示
    var mMediaShowType = MediaShowType.GRID

    //针对grid显示时候的all
    var imageList: MutableList<MediaEntity> = ArrayList()
    var videoList: MutableList<MediaEntity> = ArrayList()
    var allList: MutableList<MediaEntity> = ArrayList()

    //纯照片类型，时间标题MutableList<String>和时间标题对应的 MutableMap<String, MutableList<MediaEntity>>
    var photoLiveData: MutableLiveData<Triple<MutableList<String>, MutableMap<String, MutableList<MediaEntity>>, MutableList<MediaEntity>>> = MutableLiveData();
    var videoLiveData: MutableLiveData<Triple<MutableList<String>, MutableMap<String, MutableList<MediaEntity>>, MutableList<MediaEntity>>> = MutableLiveData();
    var allLiveData: MutableLiveData<Triple<MutableList<String>, MutableMap<String, MutableList<MediaEntity>>, MutableList<MediaEntity>>> = MutableLiveData();

    var mediaFolder: MediaFolder? = null

    /**
     * 加载更多图片,针对只有照片的
     */
    fun loadMorePhoto() {
        currentPage += 1;
        viewModelScope.launch {
            loadAllImageMediaItem(currentPage)
        }
    }

    /**
     * 加载更多视频
     */
    fun loadMoreVideo() {
        currentPage += 1;
        viewModelScope.launch {
            loadAllImageMediaItem(currentPage)
        }
    }

    /**
     * 加载更多
     */
    fun loadMoreMedia(@MediaMode mediaMode: Int): Job {
        return viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var imageCount = 0
                var videoCount = 0
                when (mediaMode) {
                    MIX -> {
                        imageCount = loadAllImageMediaItem(currentPage)
                        videoCount = loadAllVideoMediaItem(currentPage)
                    }
                    VIDEO -> videoCount = loadAllVideoMediaItem(currentPage)
                    PHOTO -> imageCount = loadAllImageMediaItem(currentPage)
                }
                if (imageCount > 0 || videoCount > 0) {
                    currentPage += 1
                    pageMax = currentPage + 1
                } else {
                    pageMax = currentPage;
                }
                //进行排序
                sortData()
            }
            when (mediaMode) {
                MIX -> {
                    allLiveData.postValue(Triple(allDates, mAllHashMap, allList))
                    photoLiveData.postValue(Triple(mImageDates, mImageHashMap, imageList))
                    videoLiveData.postValue(Triple(mVideoDates, mVideoHashMap, videoList))
                }
                VIDEO -> {
                    allLiveData.postValue(Triple(allDates, mAllHashMap, allList))
                    videoLiveData.postValue(Triple(mVideoDates, mVideoHashMap, videoList))
                }
                PHOTO -> {
                    allLiveData.postValue(Triple(allDates, mAllHashMap, allList))
                    photoLiveData.postValue(Triple(mImageDates, mImageHashMap, imageList))
                }
            }


        }
    }

    /**
     * 对集合内的数据进行重新排序，比如all中是包含了图片和视频，要重新按照时间进行重排
     */
    fun sortData() {
        //降序排序,sortDescending会改变序列，sortedDescending不会改变序列，只返回新的集合
        mImageDates.sortDescending()
        mVideoDates.sortDescending()
        allDates.sortDescending()
        mImageHashMap.forEach {
            it.value.sortWith(getSortItem())
        }
        mVideoHashMap.forEach {
            it.value.sortWith(getSortItem())
        }
        mAllHashMap.forEach {
            it.value.sortWith(getSortItem())
        }
        imageList.sortWith(getSortItem())
        videoList.sortWith(getSortItem())
        allList.sortWith(getSortItem())
    }

    /**
     * 排列的项
     */
    fun getSortItem(): Comparator<MediaEntity> {
        val compare: Comparator<MediaEntity> = Comparator { o1: MediaEntity, o2: MediaEntity ->
            when (mediaSortType) {
                MediaSortType.SORT_DATE -> o2.dateAdd.compareTo(o1.dateAdd)
                MediaSortType.SORT_NAME -> o1.title.compareTo(o2.title)
                MediaSortType.SORT_SIZE -> o1.size.compareTo(o2.size)
            }
        }
        return compare;
    }

    /**
     * 加载文件夹数据,封装到mediaset中
     */
    fun loadFolderData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loadAllImageMediaItem(currentPage)
                loadAllVideoMediaItem(currentPage)
            }
        }
    }

    /**
     * 检测是否初始数据，需要拉取数据
     */
    fun checkLoadData(@MediaMode mediaMode: Int): Job? {
        val job = if (currentPage == 0) {
            loadMoreMedia(mediaMode)
        } else null
        /**
         * 出现崩溃再次进来是，文件夹数据不存在需要重新查询
         */
        if (ObjectUtils.isEmpty(MediaManager.getInstance().mediaSets)) {
            val task = ContentDataLoadTask();
            task.execute()
        }
        return job
    }


    /**
     * 获取当前图片,分页操作
     */
    fun loadAllImageMediaItem(page: Int): Int {
        var imageCursor: Cursor? = null
        var count = 0;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                val queryArgs = Bundle()
                // 设置倒序
                queryArgs.putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, ContentResolver.QUERY_SORT_DIRECTION_DESCENDING)
                // 设置倒序条件--文件添加时间
                queryArgs.putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(MediaStore.Images.Media.DATE_ADDED))
                // 分页设置
                //queryArgs.putInt(ContentResolver.QUERY_ARG_OFFSET, page * pageSize)
                //queryArgs.putInt(ContentResolver.QUERY_ARG_LIMIT, pageSize)
                queryArgs.putString(ContentResolver.QUERY_ARG_SQL_LIMIT, "$pageSize offset ${page * pageSize}")


                imageCursor = MyApplication.app.getContentResolver().query(uri, IMAGE_COLUMN, queryArgs, null)
                //val sortBy = MediaStore.Images.Media.DATE_ADDED + " desc "
                //imageCursor = MyApplication.app.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                //    ContentDataLoadTask.IMAGE_COLUMN, null, null, sortBy)
            } else {
                val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                var selection: String? = null
                var selectionArgs: Array<String>? = null
                // 倒序+分页
                val sortOrder =
                    MediaStore.Images.Media.DATE_ADDED + " DESC limit " + pageSize + " offset " + page * pageSize
                imageCursor = MyApplication.app.getContentResolver().query(uri, IMAGE_COLUMN, selection, selectionArgs, sortOrder)
            }
            count = imageCursor?.count ?: 0;
            imageCursor?.moveToFirst() // 从头开始，防止扫描不出数据
            do {
                createImageMediaItem(imageCursor, false, false)
            } while (imageCursor!!.moveToNext())
        } catch (e: Exception) {
            Dlog.e(this, "error ---eror")
            e.printStackTrace()
        } finally {
            imageCursor?.close()
        }
        return count;
    }


    /*
     * 获取媒体中所有的视频文件，分页操作
     */
    fun loadAllVideoMediaItem(page: Int): Int {
        var videoCursor: Cursor? = null
        var count = 0;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                val queryArgs = Bundle()
                // 设置倒序
                queryArgs.putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, ContentResolver.QUERY_SORT_DIRECTION_DESCENDING)
                // 设置倒序条件--文件添加时间
                queryArgs.putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(MediaStore.Video.Media.DATE_ADDED))
                // 分页设置
                queryArgs.putInt(ContentResolver.QUERY_ARG_OFFSET, page * pageSize)
                queryArgs.putInt(ContentResolver.QUERY_ARG_LIMIT, pageSize)
                //queryArgs.putString(ContentResolver.QUERY_ARG_SQL_LIMIT, "$pageSize offset ${page * pageSize}")
                videoCursor = MyApplication.app.getContentResolver().query(uri, VIDEO_COLUMN, queryArgs, null)
            } else {
                val uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                var selection: String? = null
                var selectionArgs: Array<String>? = null
                // 倒序+分页
                val sortOrder =
                    MediaStore.Video.Media.DATE_ADDED + " DESC limit " + pageSize + " offset " + page * pageSize
                videoCursor = MyApplication.app.getContentResolver().query(uri, VIDEO_COLUMN, selection, selectionArgs, sortOrder)
            }
            count = videoCursor?.count ?: 0;
            videoCursor?.moveToFirst() // 从头开始，防止扫描不出数据
            do {
                createVideoMediaItem(videoCursor, false, false)
            } while (videoCursor!!.moveToNext())
        } catch (e: Exception) {
            Dlog.e(this, "error ---eror")
            e.printStackTrace()
        } finally {
            videoCursor?.close()
        }
        return 0;
    }

    /**
     * 查询单个数据
     */
    fun queryLatestImageMedia() {
        val sortBy = MediaStore.Images.Media.DATE_ADDED + " desc "
        var imageCursor: Cursor? = null
        try {
            imageCursor = MyApplication.app.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentDataLoadTask.IMAGE_COLUMN, null, null, sortBy)
            var num = 0
            while (imageCursor!!.moveToNext() && num < 5) {
                createImageMediaItem(imageCursor, true, false)
                num++
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            imageCursor?.close()
        }
        allLiveData.postValue(Triple(allDates, mAllHashMap, allList))
        photoLiveData.postValue(Triple(mImageDates, mImageHashMap, imageList))
    }

    /**
     * 查询单个数据
     */
    fun queryLatestVideoMedia() {
        val sortBy = MediaStore.Video.Media.DATE_ADDED + " desc"
        val videoCursor: Cursor? = MyApplication.app.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, ContentDataLoadTask.VIDEO_COLUMN,
            null, null, sortBy)
        try {
            var num = 0
            while (videoCursor!!.moveToNext() && num < 3) {
                createVideoMediaItem(videoCursor, true, false)
                num++
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            if (videoCursor != null) {
                videoCursor.close()
            }
        }
        allLiveData.postValue(Triple(allDates, mAllHashMap, allList))
        videoLiveData.postValue(Triple(mVideoDates, mVideoHashMap, videoList))
    }

    /**
     * 封装媒体对象
     */
    fun createImageMediaItem(imageCursor: Cursor?, latest: Boolean, isBucket: Boolean): MediaEntity? {
        val mediaItem = MediaEntity()
        mediaItem.path = imageCursor!!.getString(1)
        if (mediaItem.path.endsWith(".djvu") || mediaItem.path.endsWith(".tif")) {
            return null
        }
        val file = File(mediaItem.path)
        if (!file.exists() || file.length() == 0L) {
            return null
        }
        mediaItem.id = imageCursor.getInt(0)
        mediaItem.size = if (imageCursor.getString(2) == null) "0" else imageCursor.getString(2)
        mediaItem.subscribeText = imageCursor.getString(3)
        mediaItem.mimeType = imageCursor.getString(4)
        mediaItem.duration = ConstantMediaSize.IMAGE_DURATION.toLong()
        mediaItem.type = MediaEntity.TYPE_IMAGE
        mediaItem.title = imageCursor.getString(5)
        mediaItem.dateTaken = imageCursor.getLong(8)
        if (mediaItem.dateTaken == 0L) { //如果数据库取不到dateTaken，则同步为文件的最后修改时间
            mediaItem.setDateTaken(File(mediaItem.getPath()).lastModified())
        }
        mediaItem.dateAdd = imageCursor.getLong(9)
        mediaItem.dateModify = imageCursor.getLong(10)
        mediaItem.width = if (imageCursor.getString(11) == null) 0 else imageCursor.getString(11).toInt()
        mediaItem.height = if (imageCursor.getString(12) == null) 0 else imageCursor.getString(12).toInt()
        mediaItem.rotation = imageCursor.getInt(13)
        if (mediaItem.dateModify == 0L && mediaItem.dateAdd != 0L) {
            mediaItem.dateModify = mediaItem.dateAdd
        }
        if (mediaItem.dateModify == 0L) {
            mediaItem.dateModify = file.lastModified()
            if (mediaItem.dateModify == 0L) {
                return null
            }
        }
        if (mediaItem.dateModify.toString().length < 13) {
            mediaItem.dateModify *= 1000
        } else if (mediaItem.dateModify.toString().length > 13) {
            mediaItem.dateModify /= 1000
        }
        dealMediaData(mediaItem, mImageDates, mImageHashMap, latest)
        dealMediaData(mediaItem, allDates, mAllHashMap, latest)
        if (!isBucket) {
            if (!imageList.contains(mediaItem)) {
                imageList.add(mediaItem);
            }
            if (!allList.contains(mediaItem)) {
                allList.add(mediaItem);
            }
        }
        return mediaItem
    }

    /**
     * 同步map，list
     */
    private fun dealMediaData(mediaItem: MediaEntity, dates: MutableList<String>, maps: MutableMap<String, MutableList<MediaEntity>>, latest: Boolean) {
        val date = TimeUtils.format(mediaItem.dateModify, TimeUtils.Date_PATTER)
        val dateIndex = dates.indexOf(date)
        if (dateIndex < 0) {
            if (latest) {
                dates.add(0, date)
            } else {
                dates.add(date)
            }
            val mediaItems = ArrayList<MediaEntity>()
            mediaItems.add(mediaItem)
            maps[date] = mediaItems
            //addMediaSet(mediaSets, mediaSet, mediaItem)
        } else {
            val items = maps[date]!!
            if (items.contains(mediaItem) || ObjectUtils.isEmpty(items)) {
                return
            }
            if (latest) {
                items.add(0, mediaItem)
            } else {
                items.add(mediaItem)
            }
            //addMediaSet(mediaSets, mediaSet, mediaItem)
        }
    }

    /**
     * 根据文件夹id，改为实时查询
     */
    fun queryMediaSetBybucket(bucketId: Int) {
        var sortBy = MediaStore.Images.Media.DATE_ADDED + " desc "
        var selectBucket = MediaStore.Images.Media.BUCKET_ID + "=" + bucketId
        val allDates: MutableList<String> = ArrayList()
        val photoDates: MutableList<String> = ArrayList()
        val videoDates: MutableList<String> = ArrayList()
        val allMap: MutableMap<String, MutableList<MediaEntity>> = HashMap()
        val photoMap: MutableMap<String, MutableList<MediaEntity>> = HashMap()
        val videoMap: MutableMap<String, MutableList<MediaEntity>> = HashMap()
        var cursor: Cursor? = null
        try {
            cursor = MyApplication.app.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_COLUMN, selectBucket, null, sortBy)
            while (cursor!!.moveToNext()) {
                val mediaEntity = createImageMediaItem(cursor, false, true)
                if (mediaEntity != null) {
                    dealmediaSetSplitDate(allDates, allMap, mediaEntity)
                    dealmediaSetSplitDate(photoDates, photoMap, mediaEntity)
                }
            }
            sortBy = MediaStore.Video.Media.DATE_ADDED + " desc "
            selectBucket = MediaStore.Video.Media.BUCKET_ID + "=" + bucketId
            cursor = MyApplication.app.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_COLUMN,
                selectBucket, null, sortBy)
            while (cursor!!.moveToNext()) {
                val mediaEntity: MediaEntity? = createVideoMediaItem(cursor, false, true)
                dealmediaSetSplitDate(allDates, allMap, mediaEntity)
                dealmediaSetSplitDate(videoDates, videoMap, mediaEntity)
            }
            //查询结束之后，对查询结果进行排序
            sortByDateforString(allDates)
            sortByDateforString(photoDates)
            sortByDateforString(videoDates)
            allMap.forEach {
                mediaEntitySortByDate(it.value)
            }
            photoMap.forEach {
                mediaEntitySortByDate(it.value)
            }
            videoMap.forEach {
                mediaEntitySortByDate(it.value)
            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        val folder = MediaFolder()
        folder.bucketId = bucketId
        folder.allDates = allDates
        folder.photoDates = photoDates
        folder.videoDates = videoDates
        folder.allMap = allMap
        folder.photoMap = photoMap
        folder.videoMap = videoMap
        mediaFolder = folder;
    }


    /**
     * 对日期进行排序，日期默认是降序
     */
    private fun sortByDateforString(list: List<String>) {
        try {
            Collections.sort(list) { o1: String?, o2: String? ->
                if (o1 == null || o2 == null) {
                    return@sort 0
                }
                o2.lowercase(Locale.getDefault()).compareTo(o1.lowercase(Locale.getDefault()))
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    private fun dealmediaSetSplitDate(dates: MutableList<String>, map: MutableMap<String, MutableList<MediaEntity>>, curMediaItem: MediaEntity?) {
        curMediaItem?.let {
            val curDate = TimeUtils.format(curMediaItem.dateModify, TimeUtils.Date_PATTER)
            if (!dates.contains(curDate)) {
                dates.add(curDate)
            }
            var mediaItems = map[curDate]
            if (mediaItems == null) {
                mediaItems = ArrayList()
                map[curDate] = mediaItems
            }
            mediaItems.add(curMediaItem)
        }

    }


    @SuppressLint("Range")
    private fun createVideoMediaItem(videoCursor: Cursor?, latest: Boolean, isBucket: Boolean): MediaEntity? {
        val mediaItem = MediaEntity()
        videoCursor?.let {
            if (videoCursor.count == 0) {
                return null
            }
            mediaItem.id = videoCursor.getInt(0)
            mediaItem.path = videoCursor.getString(1)
            mediaItem.originPath = mediaItem.path
            mediaItem.size = videoCursor.getString(2)
            //            mediaItem.duration = videoCursor.getLong(3);
            mediaItem.duration = videoCursor.getLong(videoCursor.getColumnIndex(MediaStore.Video.Media.DURATION))
            if (latest) {
                LogUtils.i("ContentDataLoadTask", String.format("New video duration: %d", mediaItem.duration))
            }
            mediaItem.originDuration = mediaItem.duration
            mediaItem.mimeType = videoCursor.getString(4)
            mediaItem.type = MediaEntity.TYPE_VIDEO
            mediaItem.title = videoCursor.getString(5)
            mediaItem.dateTaken = videoCursor.getLong(8)
            mediaItem.dateAdd = videoCursor.getLong(9)
            mediaItem.dateModify = videoCursor.getLong(10)
            mediaItem.width = videoCursor.getInt(11)
            mediaItem.height = videoCursor.getInt(12)
            val file = File(mediaItem.path)
            if (mediaItem.duration < 0 || file.length() == 0L) {
                return null
            }
            if (mediaItem.dateModify == 0L && mediaItem.dateAdd != 0L) {
                mediaItem.dateModify = mediaItem.dateAdd
            }
            if (mediaItem.dateModify == 0L) {
                mediaItem.dateModify = file.lastModified()
                if (mediaItem.dateModify == 0L) {
                    return null
                }
            }
            if (mediaItem.dateModify.toString().length < 13) {
                mediaItem.dateModify *= 1000
            } else if (mediaItem.dateModify.toString().length > 13) {
                mediaItem.dateModify /= 1000
            }
            if (PhoneAdatarList.checkIsFilterVideo(mediaItem.path)) {
                return null
            }
            if (mediaItem.size == null || mediaItem.size == "0") {
//            LogUtils.i("test", mediaItem.toString());
                if (!FileUtils.checkExistVideo(mediaItem.path)) {
                    return null
                }
            }
            //if (!isBucket) {
            //    val mediaSet = MediaSet()
            //    mediaSet.bucketId = videoCursor.getInt(6)
            //    mediaSet.name = videoCursor.getString(7)
            dealMediaData(mediaItem, allDates, mAllHashMap, latest)
            dealMediaData(mediaItem, mVideoDates, mVideoHashMap, latest)
            //}
        }
        if (!isBucket) {
            if (!videoList.contains(mediaItem)) {
                videoList.add(mediaItem);
            }
            if (!allList.contains(mediaItem)) {
                allList.add(mediaItem);
            }
        }

        return mediaItem
    }

    /**
     * 获取当前文件夹数据
     *
     * @return
     */
    fun getCurrentFolderDates(): List<String?>? {
        if (folderType == ALL) {
            when (mediaMode) {
                MIX -> return allDates
                PHOTO -> return mImageDates
                VIDEO -> return mVideoDates
            }
            return null
        }
        if (mediaFolder == null) {
            return null
        }
        when (mediaMode) {
            MIX -> return mediaFolder!!.allDates
            PHOTO -> return mediaFolder!!.photoDates
            VIDEO -> return mediaFolder!!.videoDates
        }
        return null
    }

    /**
     * 获取当前文件夹数据
     *
     * @return
     */
    fun getCurrentFolderMap(): MutableMap<String, MutableList<MediaEntity>>? {
        if (folderType == ALL) {
            when (mediaMode) {
                MIX -> return mAllHashMap
                PHOTO -> return mImageHashMap
                VIDEO -> return mVideoHashMap
            }
            return null
        }
        if (mediaFolder == null) {
            return null
        }
        when (mediaMode) {
            MIX -> return mediaFolder!!.allMap
            PHOTO -> return mediaFolder!!.photoMap
            VIDEO -> return mediaFolder!!.videoMap
        }
        return null
    }


    /**
     * 获取当前文件夹的所有对象
     * 因为还有取消选择的情况，所以最大数量限制在其他地方处理
     */
    fun getCurrentFolderPhotos(): List<MediaEntity> {
        val listAll: MutableList<MediaEntity> = ArrayList()
        for (date in getCurrentFolderDates()!!) {
            val list = getCurrentFolderMap()!![date]
            if (!ObjectUtils.isEmpty(list)) {
                listAll.addAll(list!!)
            }
        }
        return listAll
    }

    /**
     * 获取时间标题
     */
    fun getCurrentDateTitle(@MediaMode mediaMode: Int): List<String> {
        var list: List<String> = emptyList();
        when (mediaMode) {
            MIX -> {
                list = if (folderType == ALL) {
                    allDates
                } else mediaFolder!!.allDates
            }
            PHOTO -> {
                list = if (folderType == ALL) {
                    mImageDates
                } else mediaFolder!!.photoDates
            }
            VIDEO -> {
                list = if (folderType == ALL) {
                    mVideoDates
                } else mediaFolder!!.videoDates
            }
        }
        return list;
    }

    /**
     * 获取时间对应的集合
     */
    fun getCurrentDateMap(@MediaMode mediaMode: Int): Map<String, MutableList<MediaEntity>> {
        var list: Map<String, MutableList<MediaEntity>> = emptyMap()
        when (mediaMode) {
            MIX -> {
                list = if (folderType == ALL) {
                    mAllHashMap
                } else mediaFolder!!.allMap
            }
            PHOTO -> {
                list = if (folderType == ALL) {
                    mImageHashMap
                } else mediaFolder!!.photoMap
            }
            VIDEO -> {
                list = if (folderType == ALL) {
                    mVideoHashMap
                } else mediaFolder!!.videoMap
            }
        }
        return list;
    }


    /**
     * 获取grid显示方案下的所有数据
     */
    fun getCurrentGridList(@MediaMode mediaMode: Int): List<MediaEntity> {
        var data: List<MediaEntity> = emptyList();
        when (mediaMode) {
            MIX -> {
                data = if (folderType == ALL) {
                    allList
                } else calcMapList(mediaFolder!!.allMap)
            }
            PHOTO -> {
                data = if (folderType == ALL) {
                    imageList
                } else calcMapList(mediaFolder!!.photoMap)
            }
            VIDEO -> {
                data = if (folderType == ALL) {
                    videoList
                } else calcMapList(mediaFolder!!.videoMap)
            }
        }
        return data;
    }

    /**
     * 重新拼装数据
     */
    fun calcMapList(map: MutableMap<String, MutableList<MediaEntity>>): MutableList<MediaEntity> {
        return ArrayList<MediaEntity>().also { list ->
            map.forEach {
                list.addAll(it.value)
            }
        }
    }

    /**
     * 获取当前文件夹的所有对象
     * 因为还有取消选择的情况，所以最大数量限制在其他地方处理
     */
    fun getMediaByDate(date: String?): List<MediaEntity>? {
        return getCurrentFolderMap()!![date]
    }


    /**
     * 对当前文件夹进行重排序
     */
    fun mediaFolderSort(mediaSortType: MediaSortType) {
        this.mediaSortType = mediaSortType
        if (mediaFolder != null && mediaFolder!!.bucketId != -1) {
            sortMediaMap(mediaFolder!!.allMap)
            sortMediaMap(mediaFolder!!.photoMap)
            sortMediaMap(mediaFolder!!.videoMap)
            nativeSort(mediaSortType)
        } else {
            sortData()
        }
        allLiveData.postValue(Triple(allDates, mAllHashMap, allList))
        photoLiveData.postValue(Triple(mImageDates, mImageHashMap, imageList))
        videoLiveData.postValue(Triple(mVideoDates, mVideoHashMap, videoList))
    }


    /**
     * GRID原始数据排序
     *
     * @param mediaSortType
     */
    fun nativeSort(mediaSortType: MediaSortType) {
        this.mediaSortType = mediaSortType
        if (mMediaShowType == MediaShowType.GRID) {
            if (mediaFolder != null) {
                allList.clear()
                videoList.clear()
                imageList.clear()
                //                LogUtils.i("updateData", "imageEntities 1 clear thread:" + Thread.currentThread().getName());
                if (folderType == FOLDER) {
                    //O(n)，直接排序话就是O(n^2)了
                    for (date in mediaFolder!!.allDates) {
                        allList.addAll(mediaFolder!!.allMap[date]!!)
                    }
                    mediaEntitySortByDate(allList)
                    for (entity in allList) {
                        if (entity.type == MediaEntity.TYPE_VIDEO) {
                            videoList.add(entity)
                        } else {
                            imageList.add(entity)
                            //                            LogUtils.i("updateData", "imageEntities 2 add thread:" + Thread.currentThread().getName());
                        }
                    }
                } else {
                    for (date in allDates) {
                        allList.addAll(mAllHashMap.get(date)!!)
                    }
                    mediaEntitySortByDate(allList)
                    for (entity in allList) {
                        if (entity.type == MediaEntity.TYPE_VIDEO) {
                            videoList.add(entity)
                        } else {
                            imageList.add(entity)
                        }
                    }
                }
            }
        } else {
            if (mediaFolder != null) {
                allList.clear()
                videoList.clear()
                imageList.clear()
                //                LogUtils.i("updateData", "imageEntities 4 clear thread:" + Thread.currentThread().getName());
                if (folderType == FOLDER) {
                    //O(n)，直接排序话就是O(n^2)了
                    for (date in mediaFolder!!.allDates) {
                        val list: List<MediaEntity> = ArrayList(mediaFolder!!.allMap[date])
                        for (entity in list) {
                            allList.add(entity)
                            if (entity.type == MediaEntity.TYPE_VIDEO) {
                                videoList.add(entity)
                            } else {
                                imageList.add(entity)
                            }
                        }
                    }
                } else {
                    //O(n)，直接排序话就是O(n^2)了
                    for (date in allDates) {
                        val list: List<MediaEntity> = ArrayList(mAllHashMap.get(date))
                        for (entity in list) {
                            allList.add(entity)
                            if (entity.type == MediaEntity.TYPE_VIDEO) {
                                videoList.add(entity)
                            } else {
                                imageList.add(entity)
                            }
                        }
                    }
                }
            }
        }
    }


    fun nativeSortVideo(mediaSortType: MediaSortType) {
        this.mediaSortType = mediaSortType
        if (mediaFolder != null) {
            videoList.clear()
            //O(n)，直接排序话就是O(n^2)了
            if (folderType == FOLDER) {
                for (date in mediaFolder!!.videoDates) {
                    videoList.addAll(mediaFolder!!.videoMap[date]!!)
                }
            } else {
                for (date in mVideoDates) {
                    videoList.addAll(mVideoHashMap[date]!!)
                }
            }
        }
        if (mMediaShowType == MediaShowType.GRID) {
            mediaEntitySortByDate(videoList)
        }
    }

    fun nativeSortPhoto(mediaSortType: MediaSortType) {
        this.mediaSortType = mediaSortType
        if (mediaFolder != null) {
            imageList.clear()
            //            LogUtils.i("updateData", "imageEntities 7 clear thread:" + Thread.currentThread().getName());
            //O(n)，直接排序话就是O(n^2)了
            if (folderType == FOLDER) {
                for (date in mediaFolder!!.photoDates) {
                    imageList.addAll(mediaFolder!!.photoMap[date]!!)
                    //                    LogUtils.i("updateData", "imageEntities 8 addAll thread:" + Thread.currentThread().getName());
                }
            } else {
                for (date in mImageDates) {
                    imageList.addAll(mImageHashMap[date]!!)
                }
            }
        }
        if (mMediaShowType == MediaShowType.GRID) {
            mediaEntitySortByDate(imageList)
        }
    }

    /**
     * 当前是否打开了文件夹
     */
    fun checkIsFolder(): Boolean {
        return mediaFolder?.let {
            it.bucketId != -1
        } ?: false
    }

    /**
     * 对当前文件夹进行重排序
     */
    fun mediaFolderViewChange(mediaShowType: MediaShowType) {
        mMediaShowType = mediaShowType
        if (mediaFolder != null) {
            sortMediaMap(mediaFolder!!.allMap)
            sortMediaMap(mediaFolder!!.photoMap)
            sortMediaMap(mediaFolder!!.videoMap)
            nativeSort(mediaSortType!!)
        }
    }

    fun sortMediaMap(data: Map<String?, List<MediaEntity?>>) {
        data.forEach { mediaEntitySortByDate(it.value) }
    }

    /**
     * 根据修改时间进行重新排序
     */
    fun mediaEntitySortByDate(mediaEntities: List<MediaEntity?>) {
        if (ObjectUtils.isEmpty(mediaEntities)) {
            return
        }
        try {
            when (mediaSortType) {
                MediaSortType.SORT_DATE -> Collections.sort(mediaEntities) { o1: MediaEntity?, o2: MediaEntity? ->
                    if (o1!!.dateModify > o2!!.dateModify) {
                        return@sort -1
                    } else if (o1.dateModify < o2.dateModify) {
                        return@sort 1
                    }
                    0
                }
                MediaSortType.SORT_NAME -> Collections.sort(mediaEntities) { o1: MediaEntity?, o2: MediaEntity? -> o1!!.title.compareTo(o2!!.title) }
                MediaSortType.SORT_SIZE -> Collections.sort(mediaEntities) { o1: MediaEntity?, o2: MediaEntity? ->
                    if (o1!!.size == null || o2!!.size == null) {
                        return@sort 0
                    }
                    if (o1.size.toLong() > o2!!.size.toLong()) {
                        return@sort -1
                    } else if (o1.size.toLong() < o2.size.toLong()) {
                        return@sort 1
                    }
                    0
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置展示方式
     */
    fun setMediaView(mediaSortType: MediaSortType, mediaShowType: MediaShowType) {
        this.mediaSortType = mediaSortType
        this.mMediaShowType = mediaShowType
    }

    /**
     * 是否为最大页数
     */
    fun isPageMax(): Boolean {
        return currentPage == pageMax;
    }

    var contentDataObserver: ContentDataObserver? = null;

    /**
     * 做一个媒体更新的监听，图片、视频文件增加的广播监听
     */
    init {
        contentDataObserver = ContentDataObserver(MyApplication.getInstance(), Handler(Looper.getMainLooper()))
        contentDataObserver?.let {
            it.setOnUriChange(onUriChange { uri ->
                LogUtils.i("onChanged", "uri.toString():$uri")
                if (uri.toString().contains("images")) {
                    queryLatestImageMedia()
                }
                if (uri.toString().contains("video")) {
                    queryLatestVideoMedia()
                }
            })
            MyApplication.getInstance().contentResolver.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, it)
            MyApplication.getInstance().contentResolver.registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, true, it)
        }

    }


    /**
     * 去除媒体更新监听
     */
    override fun onCleared() {
        super.onCleared()
        contentDataObserver?.let {
            MyApplication.getInstance().contentResolver.unregisterContentObserver(it)
            contentDataObserver = null
        }
        //清除引用
        SelectClipActivity.vmOwner = null
        VideoTrimActivity.vmOwner = null

    }


    companion object {
        const val ALL = 10;
        const val FOLDER = 11

        const val MIX = 0
        const val PHOTO = 1
        const val VIDEO = 2
    }

    @IntDef(ALL, FOLDER)
    @Retention(RetentionPolicy.SOURCE)
    annotation class FolderType


    @IntDef(MIX, PHOTO, VIDEO)
    @Retention(RetentionPolicy.SOURCE)
    annotation class MediaMode


}