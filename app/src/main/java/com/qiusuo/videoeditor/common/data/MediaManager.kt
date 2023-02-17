package com.qiusuo.videoeditor.common.data

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ijoysoft.mediasdk.common.utils.ObjectUtils
import com.qiusuo.videoeditor.common.bean.MediaEntity
import com.qiusuo.videoeditor.common.bean.MediaSet
import com.qiusuo.videoeditor.common.bean.MusicEntity
import com.qiusuo.videoeditor.common.bean.MusicGroupEntity
import com.qiusuo.videoeditor.common.bean.type.MusicSortType
import com.qiusuo.videoeditor.common.constant.DownloadPath
import com.qiusuo.videoeditor.util.SpUtil
import org.w3c.dom.Element
import org.xml.sax.SAXException
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

/**
 * Created by DELL on 2019/4/26.
 * 媒体数据管理类
 * MediaEntity 对象时重复利用，而这里的所
 */

class
object MediaManager {
    //当前的刷新查询到的页数
    private val cutPage = 0

    //缓存集合对象
    private var mMediaSets: List<MediaSet> = ArrayList<MediaSet>() // 文件夹形式
    private var mMediaImageSets: List<MediaSet> = ArrayList<MediaSet>() // 文件夹形式(纯照片)
    private var mMediaVideoSets: List<MediaSet> = ArrayList<MediaSet>() // 文件夹形式(纯视频)
    private var onlineEntities: List<MusicEntity?> = ArrayList<MusicEntity?>() //在线音乐缓存
    private val audioLists: MutableList<MediaEntity?> = ArrayList<MediaEntity?>()
    var previewImages: List<MediaEntity>? = null
    var isHadMediaAdd = false
    fun updateMediaSets(mediaSets: List<MediaSet>) {
        mMediaSets = mediaSets
        //        if (!ObjectUtils.isEmpty(mediaSets)) {
//            mediaSetSorByName(mediaSets);
//        }
    }

    fun getmMediaImageSets(): List<MediaSet> {
        return mMediaImageSets
    }

    val mediaSets: List<Any>
        get() = mMediaSets

    fun getmMediaVideoSets(): List<MediaSet> {
        return mMediaVideoSets
    }

    fun setmMediaImageSets(mMediaImageSets: List<MediaSet>) {
//        mediaSetSorByName(mMediaImageSets);
        this.mMediaImageSets = mMediaImageSets
    }

    fun setmMediaVideoSets(mMediaVideoSets: List<MediaSet>) {
        this.mMediaVideoSets = mMediaVideoSets
    }

    fun getAudioLists(): List<MediaEntity?> {
        return audioLists
    }

    fun getOnlineEntities(): List<MusicEntity?> {
        return onlineEntities
    }

    /**
     * 设置在线数据，进行排序
     *
     * @param musicEntities
     */
    fun setOnlineEntities(musicEntities: List<MusicEntity>) {
        onlineEntities = musicEntities
        if (!ObjectUtils.isEmpty(onlineEntities)) {
            //按日期进行排序
            Collections.sort(onlineEntities, { o1, o2 -> o1!!.getName().compareTo(o2!!.getName()) })
        }
    }

    fun queryOnlineMusic(path: String?): MusicEntity? {
        if (path == null) {
            return null
        }
        if (ObjectUtils.isEmpty(onlineEntities)) {
            val groupEntity: MusicGroupEntity = XmlParserHelper.pullxmlMusic(DownloadPath.MUSIC_DATA_SAVE)
                ?: return null
            onlineEntities = groupEntity.getAllList()
        }
        for (musicEntity in onlineEntities) {
            if (musicEntity!!.getLocalPath().equals(path)) {
                return musicEntity
            }
        }
        return null
    }

    fun insertImage(cr: ContentResolver, title: String?, description: String?): String? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, title)
        values.put(MediaStore.Images.Media.DESCRIPTION, description)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        var url: Uri? = null
        var stringUrl: String? = null /* value to be returned */
        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } catch (e: Exception) {
            if (url != null) {
                cr.delete(url, null, null)
                url = null
            }
        }
        if (url != null) {
            stringUrl = url.toString()
        }
        return stringUrl
    }

    @Synchronized
    fun setAudioLists(audioLists: List<MediaEntity?>?) {
        if (!ObjectUtils.isEmpty(audioLists)) {
            this.audioLists.clear()
            this.audioLists.addAll(audioLists!!)
            initSortMusic()
        }
        //AppBus.get().post(MusicNotifyEvent())
    }

    /**
     * 音频赋值成功后进行排序
     */
    fun initSortMusic() {
        val musicSortType: MusicSortType = MusicSortType.getMusicSortType(SpUtil.getInt(SpUtil.MUSIC_SORT, 0))
        val isreverse: Boolean = SpUtil.getBoolean(SpUtil.MUSIC_RE_SORT, false)
        setSortMusic(musicSortType, isreverse)
    }

    /**
     * 默认是desc 降序，reverse后是asc升序
     *
     * @param musicSortType
     * @param reverse
     */
    @Synchronized
    fun setSortMusic(musicSortType: MusicSortType, reverse: Boolean) {
        if (ObjectUtils.isEmpty(audioLists)) {
            return
        }
        when (musicSortType) {
            MusicSortType.SORT_NAME -> sortByName(reverse)
            MusicSortType.SORT_DATE -> sortDate(reverse)
            MusicSortType.SORT_DURATION -> sortByDuration(reverse)
        }
    }

    /**
     * 根据名字排序
     *
     * @param reverse
     */
    private fun sortByName(reverse: Boolean) {
        Collections.sort(audioLists, Comparator<Any> { o1, o2 ->
            var flag = 1
            if (reverse) {
                flag = -1
            }
            if (o1.pinyinChar < o2.pinyinChar) {
                return@Comparator -1 * flag
            } else if (o1.pinyinChar > o2.pinyinChar) {
                return@Comparator 1 * flag
            }
            0
        })
    }

    /**
     * 根据时长排序
     *
     * @param reverse
     */
    private fun sortByDuration(reverse: Boolean) {
        Collections.sort(audioLists, Comparator<Any> { o1, o2 ->
            var flag = 1
            if (reverse) {
                flag = -1
            }
            if (o1.duration > o2.duration) {
                return@Comparator -1 * flag
            } else if (o1.duration < o2.duration) {
                return@Comparator 1 * flag
            }
            0
        })
    }

    /**
     * 根据日期排序
     *
     * @param reverse
     */
    private fun sortDate(reverse: Boolean) {
        Collections.sort(audioLists, Comparator<Any> { o1, o2 ->
            var flag = 1
            if (reverse) {
                flag = -1
            }
            if (o1.dateModify > o2.dateModify) {
                return@Comparator -1 * flag
            } else if (o1.dateModify < o2.dateModify) {
                return@Comparator 1 * flag
            }
            0
        })
    }

    //
    private class MusicJudge(private val musicType: Int) : CollectionUtil.Judge<MusicEntity?> {
        fun canRemove(music: MusicEntity): Boolean {
            return musicType != music.getType()
        }
    }

    fun needLoadData(): Boolean {
//       return hadMediaAdd&&
        return false
    }

    /**
     * 数据监听放全局，添加接口，但是绑定接口记得解除接口,不然泄露
     */
    //    private ContentDataObserver mContentDataObserver;
    private var mediaCallback: MediaCallback? = null

    //    public void initContentDataObserver() {
    //        mContentDataObserver = new ContentDataObserver(MyApplication.getInstance(), new Handler(Looper.getMainLooper()));
    //        mContentDataObserver.setOnUriChange(new ContentDataObserver.onUriChange() {
    //            @Override
    //            public void onChanged(Uri uri) {
    //                LogUtils.i("onChanged", "uri.toString():" + uri.toString());
    //                if (uri.toString().contains("images")) {
    ////                    if (mediaCallback != null && !ObjectUtils.isEmpty(mImageDates)) {
    ////                        mediaCallback.imageCallback();
    ////                    } else {
    ////                        hadMediaAdd = true;
    ////                    }
    //                }
    //                if (uri.toString().contains("video")) {
    ////                    if (mediaCallback != null && !ObjectUtils.isEmpty(mVideoDates)) {
    ////                        mediaCallback.videoCallback();
    ////                    } else {
    ////                        hadMediaAdd = true;
    ////                    }
    //                }
    //            }
    //        });
    //        MyApplication.getInstance().getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true,
    //                mContentDataObserver);
    //        MyApplication.getInstance().getContentResolver().registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, true,
    //                mContentDataObserver);
    //        MyApplication.getInstance().getContentResolver().registerContentObserver(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true,
    //                mContentDataObserver);
    //    }
    //    public void destroyDataObserver() {
    //        if (mContentDataObserver != null && MyApplication.getInstance().getContentResolver() != null) {
    //            MyApplication.getInstance().getContentResolver().unregisterContentObserver(mContentDataObserver);
    //        }
    //        mContentDataObserver = null;
    //    }
    fun setMediaCallback(mediaCallback: MediaCallback?) {
        this.mediaCallback = mediaCallback
    }

    interface MediaCallback {
        fun imageCallback()
        fun videoCallback()
    }

    /**
     * 当前类型的文件夹
     *
     * @return
     */
    fun getCurrentMediaSets(selectFragmtnType: Int): List<MediaSet>? {
        when (selectFragmtnType) {
            SelectFragmentEvent.SELECT_ALL -> return mMediaSets
            SelectFragmentEvent.SELECT_PHOTO -> return mMediaImageSets
            SelectFragmentEvent.SELECT_VIDEO -> return mMediaVideoSets
        }
        return null
    }

    private var mMediaSortType: MediaSortType = MediaSortType.SORT_DATE
    fun getmMediaSortType(): MediaSortType {
        return mMediaSortType
    }

    fun setMediaView(mediaSortType: MediaSortType, mediaShowType: MediaShowType) {
        mMediaSortType = mediaSortType
        mMediaShowType = mediaShowType
    }

    private var mMediaShowType: MediaShowType = MediaShowType.GRID

    /**
     * 创建all文件夹
     *
     * @return
     */
    fun createDefaultMediaSet(selectFragmtnType: Int): MediaSet {
        val mediaSet = MediaSet()
        mediaSet.bucketId = -1
        mediaSet.name = ActivityLifecycle.get().getApplication().getString(R.string.all)
        var count = 0
        var list: List<MediaSet>? = null
        when (selectFragmtnType) {
            SelectFragmentEvent.SELECT_ALL -> list = mMediaSets
            SelectFragmentEvent.SELECT_PHOTO -> list = mMediaImageSets
            SelectFragmentEvent.SELECT_VIDEO -> list = mMediaVideoSets
        }
        for (set in list) {
            count += set.count
        }
        mediaSet.setCoverpath(list!![0].getCoverpath())
        mediaSet.setCount(count)
        return mediaSet
    }

    companion object {
        /**
         * 每次查询的文件数量
         */
        const val pageSize = 100

        //    private MediaFolder mediaFolder;
        //
        //    public int getMediaMode() {
        //        return mediaMode;
        //    }
        //
        //    public void setMediaMode(int mediaMode) {
        //        this.mediaMode = mediaMode;
        //    }
        @Volatile
        var instance: MediaManager? = null
            get() {
                if (field == null) {
                    synchronized(MediaManager::class.java) {
                        if (field == null) {
                            field = MediaManager()
                        }
                    }
                }
                return field
            }
            private set

        /**
         * 获取在线音乐
         *
         * @param context
         * @return
         */
        fun getOnlineMusic(context: Context, musicType: Int): List<MusicEntity> {
            val musicEntities: MutableList<MusicEntity> = ArrayList<MusicEntity>()
            try {
                val listType = object : TypeToken<ArrayList<MusicEntity?>?>() {}.type
                //在线音乐使用json配置在本地
                val json: String = IOUtil.readStream(context.resources.assets.open("music.json"))
                val musics: List<MusicEntity> = Gson().fromJson<List<MusicEntity>>(json, listType)
                CollectionUtil.removeElement(musics, MusicJudge(musicType))
                musicEntities.addAll(musics)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return musicEntities
        }

        val onlineMusic: List<Any>
            get() {
                val mMusicGroupEntity: MusicGroupEntity = XmlParserHelper.pullxmlMusic(DownloadPath.MUSIC_DATA_SAVE)
                return mMusicGroupEntity.getAllList()
            }

        fun requireMusic(): List<MusicEntity>? {
            var musicGroupEntity: MusicGroupEntity? = null
            if (FileUtil.exists(DownloadPath.MUSIC_DATA_SAVE)) {
                musicGroupEntity = XmlParserHelper.pullxmlMusic(DownloadPath.MUSIC_DATA_SAVE)
            } else {
                try {
                    MyApplication.getInstance().getAssets().open("music.xml").use { `is` -> musicGroupEntity = XmlParserHelper.pullxmlMusic(`is`) }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return if (musicGroupEntity == null) {
                null
            } else musicGroupEntity.getAllList()
        }

        fun getVersion(path: String?): String {
            var version = ""
            val file = File(path)
            if (!file.exists()) {
                return version
            }
            var `is`: InputStream? = null
            try {
                val factory = DocumentBuilderFactory.newInstance()
                val builder = factory.newDocumentBuilder()
                `is` = FileInputStream(file.absolutePath)
                val document = builder.parse(`is`)
                val element = document.documentElement
                val items = element.getElementsByTagName("item")
                for (j in 0 until items.length) {
                    val personNode = items.item(j) as Element
                    version = personNode.getAttribute("version")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ParserConfigurationException) {
                e.printStackTrace()
            } catch (e: SAXException) {
                e.printStackTrace()
            } finally {
                try {
                    `is`!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return version
        }

        fun getAudioFileVoiceTime(filePath: String?): Long {
            var mediaPlayerDuration = 0L
            if (filePath == null || filePath.isEmpty()) {
                return 0
            }
            val mediaPlayer = MediaPlayer()
            try {
                mediaPlayer.setDataSource(filePath)
                mediaPlayer.prepare()
                mediaPlayerDuration = mediaPlayer.duration.toLong()
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
            if (mediaPlayer != null) {
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.release()
            }
            return mediaPlayerDuration
        }

        fun getAudioFileVoiceTime(filePath: String?, onPreparedListener: MediaPlayer.OnPreparedListener?) {
            if (filePath == null || filePath.isEmpty()) {
                return
            }
            val mediaPlayer = MediaPlayer()
            try {
                mediaPlayer.setDataSource(filePath)
                mediaPlayer.prepare()
                mediaPlayer.setOnPreparedListener(onPreparedListener)
            } catch (ioException: IOException) {
                Log.d("asdada", "duration1===")
                ioException.printStackTrace()
            } finally {
//            if (mediaPlayer != null) {
//                mediaPlayer.stop();
//                mediaPlayer.reset();
//                mediaPlayer.release();
//            }
            }
        }
    }
}