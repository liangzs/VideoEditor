package com.qiusuo.videoeditor.common.data

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Rect
import android.media.MediaMetadataRetriever
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.global.ConstantPath
import com.ijoysoft.mediasdk.common.global.MediaSdk
import com.ijoysoft.mediasdk.common.global.ThreadPoolManager
import com.ijoysoft.mediasdk.common.global.ThreadPoolMaxThread
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.common.utils.ColorUtil
import com.ijoysoft.mediasdk.common.utils.FileUtils
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.common.utils.ObjectUtils
import com.ijoysoft.mediasdk.module.entity.AudioMediaItem
import com.ijoysoft.mediasdk.module.entity.BGInfo
import com.ijoysoft.mediasdk.module.entity.DoodleItem
import com.ijoysoft.mediasdk.module.entity.DurationInterval
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.MediaMatrix
import com.ijoysoft.mediasdk.module.entity.MediaType
import com.ijoysoft.mediasdk.module.entity.PhotoMediaItem
import com.ijoysoft.mediasdk.module.entity.PretreatConfig
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.entity.VideoMediaItem
import com.ijoysoft.mediasdk.module.entity.WaterMarkType
import com.ijoysoft.mediasdk.module.mediacodec.BackroundTask
import com.ijoysoft.mediasdk.module.mediacodec.FfmpegBackgroundHelper
import com.ijoysoft.mediasdk.module.mediacodec.FfmpegTaskType
import com.ijoysoft.mediasdk.module.opengl.InnerBorder.Companion.NONE
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.FilterHelper
import com.ijoysoft.mediasdk.module.opengl.particle.GlobalParticles
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeConstant
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeEnum
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeHelper
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.IPretreatment
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.LocalPreTreatmentTemplate
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionSeries
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType
import com.ijoysoft.mediasdk.module.playControl.GifDecoder
import com.ijoysoft.mediasdk.module.playControl.GlideGifDecoder.Companion.getGif
import com.ijoysoft.mediasdk.view.BackgroundType
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository.randomTranGroup
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository.randomTransitionFilter
import com.qiusuo.videoeditor.base.MyApplication
import com.qiusuo.videoeditor.common.bean.CutMusicItem
import com.qiusuo.videoeditor.common.bean.MediaEntity
import com.qiusuo.videoeditor.common.bean.SpBgInfo
import com.qiusuo.videoeditor.common.bean.ThemeEntity
import com.qiusuo.videoeditor.common.bean.ThemeGroupEntity
import com.qiusuo.videoeditor.common.bean.ThemeResGroupEntity
import com.qiusuo.videoeditor.common.constant.DownloadPath
import com.qiusuo.videoeditor.common.data.MediaManager.createDefaultMusic
import com.qiusuo.videoeditor.common.data.MediaManager.queryOnlineMusic
import com.qiusuo.videoeditor.common.room.Project
import com.qiusuo.videoeditor.util.AndroidUtil
import com.qiusuo.videoeditor.util.BytesBitmap
import com.qiusuo.videoeditor.util.FileUtil
import com.qiusuo.videoeditor.util.IOUtil
import com.qiusuo.videoeditor.util.SpUtil
import com.qiusuo.videoeditor.util.SpUtil.getBoolean
import com.qiusuo.videoeditor.util.SpUtil.getInt
import com.qiusuo.videoeditor.util.SpUtil.getObject
import com.qiusuo.videoeditor.util.SpUtil.setObject
import org.libpag.PAGFile
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.Collections
import java.util.Random
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 数据存储
 * 包含外部数据同步到sdk列表数据
 * 后续工作台的数据都同步在这里
 */
object MediaDataRepository {
    //    private ArrayList<MediaEntity> dataOrigin;
    var dataOperate: MutableList<MediaItem?> = mutableListOf()
    var currentProject: Project? = null
    private var mScreenShotBitmap: Bitmap? = null

    /**
     * 贴纸
     */
    val doodleList: MutableList<DoodleItem>?
    var extAudioList: ArrayList<AudioMediaItem?>?
        private set
    private var audioListBak: ArrayList<AudioMediaItem?>? = null

    //录音模块，和外音并列,单独控制
    private var recordList: MutableList<AudioMediaItem?>?

    //现在音频有两种现象，一直主题音乐，而是外音（包括多段音乐）
    private var defaultAudio: AudioMediaItem? = null
    var themeAudio: AudioMediaItem? = null
        private set
    var isLocal = false
        private set
    var isLocalMultiEdit = false // 本地draft多次进入编辑
    private var mThemeGroupEntity: ThemeGroupEntity? = null
    private var mThemeResGroupEntity: ThemeResGroupEntity? = null
    var currentSlideShow: ThemeEntity? = null
        private set
    private var preTreatment: IPretreatment? = null //添加主题模块
    var isHasEdit = false

    //针对主题，把挂载在片段中的控件抽取出来，重复利用
    var mimaps: ConcurrentHashMap<Int?, List<Bitmap?>?>? = null
        private set
    var videoCount = 0
        private set
    var photoCount = 0
        private set

    @JvmField
    var tempMediaEntity: MediaEntity? = null

    /**
     * gif控件
     * gif的bitmap较多，不适合重复加载，
     * 以string为key进行缓存，重复利用
     */
    private var dynamicMimaps: ConcurrentHashMap<String?, MutableList<GifDecoder.GifFrame?>?>? = null

    /**
     * gif加载的同步锁
     */
    private val dynamicMipmapsLoadLock = Any()

    //挂载在时间轴的控件
    private var widgetMimaps: ArrayList<Bitmap?>? = null

    /**
     * 获取当前编辑的背景参数
     *
     * @return
     */
    //预处理模糊
    var currentBackgroundInfo = SpBgInfo.defaultBgInfo()
        private set

    //草稿进入编辑，记录用户判断封面缩略图是否更新
    private val draftMediaItem: MediaItem? = null

    /**
     * 转场系列
     */
    var currentTransitionSeries = TransitionSeries.NONE
    var zoomScale = 1f

    //挂载主题上的pag文件
    private var themePags: MutableList<PAGFile?>? = null

    // 检测是否抽取视频源音
    fun checkExtractVideoAudio() {
        if (ObjectUtils.isEmpty(dataOperate)) {
            return
        }
        // 做音频抽取动作,这里分是进入主控制台还是进入裁剪子页面
        for (i in dataOperate!!.indices) {
            val dustItem = dataOperate!![i]
            if (dustItem is VideoMediaItem) {
                if (FileUtils.checkExistAudio(dustItem.getPath()) && ObjectUtils.isEmpty(dustItem.extractTaskId)) {
                    val temppath = ConstantPath.createExtractVideo2Audio(projectID, dustItem.getName())
                    dustItem.extracAudioPath = FileUtils.removeEndiff(temppath)
                    val backroundTask = BackroundTask(arrayOf(dustItem.getPath(), temppath, temppath), FfmpegTaskType.EXTRACT_AUDIO)
                    dustItem.extractTaskId = backroundTask.id
                    FfmpegBackgroundHelper.getInstance().exeCuteTask(backroundTask)
                }
            }
        }
    }

    fun clearData() {
        recyleBitmaps()
        doodleList?.clear()
        if (extAudioList != null) {
            extAudioList!!.clear()
        }
        if (recordList != null) {
            recordList!!.clear()
        }
        if (themePags != null) {
            themePags!!.clear()
            themePags = null
        }
        mScreenShotBitmap = null
        mThemeGroupEntity = null
        mThemeResGroupEntity = null
        currentSlideShow = null
        defaultAudio = null
        themeAudio = null
        ConstantMediaSize.themeType = ThemeEnum.NONE
        isHasEdit = false
        currentProject = null
        ConstantMediaSize.particles = GlobalParticles.NONE
        ConstantMediaSize.innerBorder = NONE
        currentBackgroundInfo = SpBgInfo.defaultBgInfo()
        //重置视频和照片数量计数
        clear()
    }

    /**
     * 回收数据
     */
    fun recyleBitmaps() {
        val dataList: List<MediaItem?>? = dataOperate
        dataOperate = mutableListOf()
        if (dataList != null) {
            for (i in dataList.indices) {
                clearMediaItem(dataList[i])
            }
        }
        val tempMips = mimaps
        mimaps = ConcurrentHashMap()
        val tempDynamic = dynamicMimaps
        dynamicMimaps = ConcurrentHashMap()
        val tempwidget: List<Bitmap?>? = widgetMimaps
        widgetMimaps = ArrayList()
        if (!ObjectUtils.isEmpty(tempMips)) {
            for (i in 0 until tempMips!!.size) {
                val bitmaps = tempMips[i]
                if (!ObjectUtils.isEmpty(bitmaps)) {
                    for (j in bitmaps!!.indices) {
                        if (bitmaps.get(j) != null && !bitmaps.get(j)!!.isRecycled) {
                            try {
                                bitmaps.get(j)!!.recycle()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }

        //重置gif缓存
        if (!ObjectUtils.isEmpty(tempDynamic)) {
            LogUtils.i(javaClass.simpleName + " dynamicMimaps operation", "ready to clear dynamicMimaps")
            for (gif: MutableList<GifDecoder.GifFrame?>? in tempDynamic!!.values) {
                for (frame: GifDecoder.GifFrame? in gif!!) {
                    //回收bitmap
                    frame!!.image.recycle()
                }
                gif.clear()
            }
            LogUtils.i(javaClass.simpleName + " dynamicMimaps operation", "dynamicMimaps cleared")
        }

        //重置时间轴控件缓存
        if (!ObjectUtils.isEmpty(tempwidget)) {
            for (j in tempwidget!!.indices) {
                if (tempwidget.get(j) != null && !tempwidget.get(j)!!.isRecycled) {
                    tempwidget.get(j)!!.recycle()
                }
            }
        }
    }

    val themeGroupEntity: ThemeGroupEntity
        get() {
            if (mThemeGroupEntity == null) {
//            mThemeGroupEntity = XmlParserHelper.pullxmlThemeGroup(DownloadPath.THEME_DATA_SAVE);
            }
            return mThemeGroupEntity ?: ThemeGroupEntity()
        }

    fun setThemeLocalData() {
//        mThemeGroupEntity = XmlParserHelper.pullxmlThemeGroup(DownloadPath.THEME_DATA_SAVE);
    }

    fun setThemeGoupEntity(themeGroupEntity: ThemeGroupEntity?) {
        mThemeGroupEntity = themeGroupEntity
    }

    var themeResGroupEntity: ThemeResGroupEntity?
        get() {
            if (mThemeResGroupEntity == null) {
                val str = IOUtil.readFile(File(DownloadPath.THEME_RESOURCE_SAVE), "utf-8")
                mThemeResGroupEntity = Gson().fromJson(str, ThemeResGroupEntity::class.java)
            }
            return mThemeResGroupEntity
        }
        set(resGroupEntity) {
            mThemeResGroupEntity = resGroupEntity
        }

    /**
     * 改变主题
     *
     * @param slideEntity
     */
    @SuppressLint("WrongConstant")
    fun setCurrentSlideEntity(slideEntity: ThemeEntity) {
        currentBackgroundInfo.backgrondType = BackgroundType.SELF
        currentBackgroundInfo.blurLevel = 50
        //当且仅当同一主题，然后已选了自定义歌曲时，不进行歌曲切换
        if (!ThemeRepository.checkLockTheme(slideEntity.themeEnum)) {
            ThreadPoolManager.getThreadPool().execute({
                try {
//                    ZipUtils.UnZipFolder(DownloadHelper.getDownloadPath(slideEntity.getZipPath()), slideEntity.getPath());
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
        }
        if (ConstantMediaSize.themeType != slideEntity.themeEnum) {
            if (extAudioList!!.size > 0) {
                audioListBak = extAudioList!!.clone() as ArrayList<AudioMediaItem?>
            }
            extAudioList!!.clear()
            themeAudio = AudioMediaItem()
            themeAudio!!.projectId = projectID
            // 改变文件名并赋值
            themeAudio!!.path = slideEntity.musicLocalPath
            val musicEntity = queryOnlineMusic(slideEntity.musicLocalPath)
            themeAudio!!.title = slideEntity.name
            if (musicEntity != null) {
                themeAudio!!.title = musicEntity.name
                themeAudio!!.type = musicEntity.type
            } else {
                themeAudio!!.title = FileUtil.getFileName(themeAudio!!.path)
            }
            themeAudio!!.originPath = themeAudio!!.path
            themeAudio!!.duration = slideEntity.musicDuration.toLong()
            if (slideEntity.musicDuration == 0) {
                ThreadPoolManager.getThreadPool().execute({
                    themeAudio!!.setDuration(AndroidUtil.getAudioTime(slideEntity.musicLocalPath))
                    if (themeAudio!!.getDuration() == 0L) {
                        themeAudio!!.setDuration(2500)
                    }
                    themeAudio!!.setOriginDuration(themeAudio!!.getDuration())
                    themeAudio!!.setCutEnd(themeAudio!!.getDuration())
                })
            }
            themeAudio!!.cutEnd = themeAudio!!.duration
            themeAudio!!.volume = 100f
        }
        //如果是主题设置默认比例，不能用none
        if (ConstantMediaSize.ratioType == RatioType.NONE) {
            ConstantMediaSize.ratioType = ConstantMediaSize.THEME_DEFAULT_RATIO
        }
        ConstantMediaSize.themePath = slideEntity.path + File.separator + slideEntity.name
        ConstantMediaSize.themeType = slideEntity.themeEnum
        ConstantMediaSize.themeConstantype = slideEntity.themeConstanType
        if (slideEntity.themeEnum!!.particleType != null) {
            ConstantMediaSize.particles = slideEntity.themeEnum!!.particleType
        }
        //        if (currentProject != null) {
//            currentProject.setThemeEnum(slideEntity.getThemeEnum());
//            currentProject.setThemeMusicPath(themeAudio.getPath());
//            currentProject.setThemeZipPath(ConstantMediaSize.themePath);
//        }
        if (ConstantMediaSize.themeType == ThemeEnum.NONE) {
            themeAudio = null
        }
        currentSlideShow = slideEntity
        preTreatment = ThemeHelper.createPreTreatment(ConstantMediaSize.themeType)
    }

    /**
     * 创建主题音乐
     */
    private fun themeAudio() {}
    fun getWidgetMimaps(): List<Bitmap?>? {
        return widgetMimaps
    }

    /**
     * 从本地加载数据后，赋值当前主题
     *
     * @param themeEnum
     */
    fun setCurrentSlideEntity(themeEnum: ThemeEnum, themeMusicPath: String?) {
        if (themeEnum != ThemeEnum.NONE && extAudioList!!.isEmpty()) {
            themeAudio = AudioMediaItem()
            themeAudio!!.path = themeMusicPath
            val musicEntity = queryOnlineMusic(themeMusicPath)
            themeAudio!!.title = themeEnum.getName()
            if (musicEntity != null) {
                themeAudio!!.title = musicEntity.name
                themeAudio!!.type = musicEntity.type
                themeAudio!!.duration = musicEntity.duration.toLong()
            }
            if (themeAudio!!.duration == 0L) {
                ThreadPoolManager.getThreadPool().execute({ themeAudio!!.setDuration(AndroidUtil.getAudioTime(themeAudio!!.getPath())) })
            }
            themeAudio!!.originPath = themeAudio!!.path
            themeAudio!!.cutEnd = themeAudio!!.duration
            themeAudio!!.volume = 100f
            if (themeEnum.particleType != null) {
                ConstantMediaSize.particles = themeEnum.particleType
            }
        }
        currentSlideShow = ThemeEntity()
        currentSlideShow!!.themeEnum = themeEnum
    }

    /**
     * 覆盖原有数据
     *
     * @param list
     */
    fun overideData(list: MutableList<MediaItem?>) {
        dataOperate = list
        photoCount = 0
        videoCount = 0
        for (mediaItem: MediaItem? in list) {
            if (mediaItem!!.isVideo) {
                videoCount++
            } else {
                photoCount++
            }
        }
    }

    /**
     * 覆盖原有数据
     *
     * @param list
     */
    fun updateData(list: List<MediaItem?>?) {
        list?.let {
            dataOperate = it.toMutableList()
        }

    }

    /**
     * 检查视频类资源是否正在做音频抽取动作
     * listOrigin是copy类，是原来的
     * change是修改过后
     */
    fun checkDeleteVideoIsExtracting(listCopy: ArrayList<MediaItem>) {
        if (listCopy.size == dataOperate!!.size) {
            return
        }
        val removes: MutableList<BackroundTask?> = ArrayList()
        for (mediaItem: MediaItem in listCopy) {
            if (mediaItem.mediaType == MediaType.VIDEO && !dataOperate!!.contains(mediaItem)) {
                if (mediaItem.finalDuration > 300000) {
                    removes.add(BackroundTask((mediaItem as VideoMediaItem).extractTaskId))
                }
            }
        }
        if (!ObjectUtils.isEmpty(removes)) {
            FfmpegBackgroundHelper.getInstance().removeTask(removes)
        }
    }

    /**
     * 单项添加,点击
     */
    fun addMediaItem(mediaEntity: MediaEntity, isExtract: Boolean, addMediaCallback: AddMediaCallback?) { // 可能要加个锁，防止添加数据后，显示的操作数据与源数据不匹配
        if (preTreatment == null) {
            preTreatment = BasePreTreatment()
        }
        if (mediaEntity.type == MediaEntity.TYPE_IMAGE) {
            addMediaItemPhoto(mediaEntity, addMediaCallback, null)
            photoCount++
            LogUtils.v("MediaDataRepository", "photoCount++: $photoCount")
        } else {
            addMediaItemVideo(mediaEntity, isExtract, addMediaCallback, null)
            videoCount++
            LogUtils.v("MediaDataRepository", "videoCount++: $videoCount")
        }
    }

    fun addMediaItemWhole(mediaEntity: MediaEntity, isExtract: Boolean, successCallback: AddMediaSuccessCallback?) { // 可能要加个锁，防止添加数据后，显示的操作数据与源数据不匹配
        if (preTreatment == null) {
            preTreatment = BasePreTreatment()
        }
        if (mediaEntity.type == MediaEntity.TYPE_IMAGE) {
            addMediaItemPhoto(mediaEntity, null, successCallback)
            photoCount++
            LogUtils.v("MediaDataRepository", "photoCount++: $photoCount")
        } else {
            addMediaItemVideo(mediaEntity, isExtract, null, successCallback)
            videoCount++
            LogUtils.v("MediaDataRepository", "videoCount++: $videoCount")
        }
    }

    interface AddMediaCallback {
        fun resultData(mediaItem: MediaItem?)
    }

    interface AddMediaSuccessCallback {
        fun resultData(mediaItem: MediaItem?)
    }

    interface LoadDBSuccess {
        fun onSuccess(loss: Boolean)
        fun fail() {}
        fun fail(msg: String?) {}
    }
    /**
     * 视频支持主题的展示，所以设置一个展示的最小值[ConstantMediaSize.THEME_VIDEO_DURATION]
     *
     * @param originItem
     * @param isExtract
     * @param addMediaCallback
     * @param addMediaSuccessCallback
     * @param getInfoBlocked
     */
    /**
     * 数据类型转化
     * 这里如果是视频的话，就对视频进行前后帧的本地存储，方便后续使用
     * 这样在纹理加载的时候，要对目标的bitmap进行缩小
     *
     * @param originItem
     * @return
     */
    @JvmOverloads
    fun addMediaItemVideo(originItem: MediaEntity, isExtract: Boolean, addMediaCallback: AddMediaCallback?, addMediaSuccessCallback: AddMediaSuccessCallback?, getInfoBlocked: Boolean = false) {
        val dustItem = VideoMediaItem()
        dustItem.projectId = projectID
        dustItem.path = originItem.path
        dustItem.size = (if (originItem.size == null) "0" else originItem.size).toLong()
        dustItem.duration = originItem.duration
        dustItem.setVideoOriginDuration(if (originItem.originDuration == 0L) originItem.duration else originItem.originDuration)
        dustItem.width = originItem.width
        dustItem.height = originItem.height
        dustItem.name = originItem.title
        dustItem.id = originItem.id
        dustItem.videoCutInterval = originItem.videoCutInterval
        dustItem.trimPath = originItem.trimPath
        //如果主题为none并且无转场值为none，则进行随机转场赋值,把视频裁剪过滤功能过滤掉即视频裁剪不加转场
//        if (!checkIsTheme() && dustItem.getTransitionFilter() == null && isExtract) {
//            dustItem.setTransitionFilter(TransitionRepository.INSTANCE.randomTransitionFilter(currentProject.getTranGroup(), dataOperate.size()));
//        }
        val index = dataOperate!!.size
        dataOperate!!.add(dustItem)
        addMediaCallback?.resultData(dustItem)
        //转场系列处理
        if (currentTransitionSeries !== TransitionSeries.NONE) {
            dustItem.transitionFilter = TransitionFactory.initFilters(
                currentTransitionSeries.array.get((index) % currentTransitionSeries.array.size)
            )
        }
        if (FileUtils.checkExistAudio(dustItem.path) && isExtract) {
            val temppath = ConstantPath.createExtractVideo2Audio(projectID, dustItem.name)
            dustItem.extracAudioPath = FileUtils.removeEndiff(temppath)
            val backroundTask = BackroundTask(arrayOf(dustItem.path, temppath, temppath), FfmpegTaskType.EXTRACT_AUDIO)
            dustItem.extractTaskId = backroundTask.id
            FfmpegBackgroundHelper.getInstance().exeCuteTask(backroundTask)
        }
        val runnable = Runnable {
            dealMimaps(index, dustItem)
            val retriever: MediaMetadataRetriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(if (originItem.getTrimPath() != null) originItem.getTrimPath() else originItem.path)
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH) != null) {
                    originItem.width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)!!.toInt()
                }
                if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT) != null) {
                    originItem.height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)!!.toInt()
                }
                dustItem.setWidth(originItem.width)
                dustItem.setHeight(originItem.height)
                if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE) != null) {
                    val bitRate: Int = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)!!.toInt()
                    dustItem.setBitRate(bitRate)
                }
                if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION) != null) {
                    originItem.rotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)!!.toInt()
                }
                if (dustItem.getDuration() == 0L && retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) != null) {
                    dustItem.setDuration(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toLong())
                    dustItem.setVideoOriginDuration(if (originItem.originDuration == 0L) originItem.duration else originItem.originDuration)
                    //                    if (checkIsTheme()) {
//                        dustItem.setDuration(Math.min(dustItem.getDuration(), ConstantMediaSize.THEME_VIDEO_DURATION));
//                        if (preTreatment instanceof BaseTimePreTreatment) {
//                            dustItem.setDuration(Math.min(dustItem.getDuration(), ConstantMediaSize.THEME_TIMETYPE_DURATION));
//                        }
//                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                retriever.release()
            }
            dustItem.setRotation(originItem.rotation)
            dustItem.setAfterRotation(originItem.rotation)
            if (addMediaSuccessCallback != null) {
                addMediaSuccessCallback.resultData(dustItem)
            }
        }
        if (getInfoBlocked) {
            runnable.run()
        } else {
            ThreadPoolManager.getThreadPool().execute(runnable)
        }
    }

    fun addMediaItemVideo(videoPath: String?): Boolean {
        dataOperate = ArrayList()
        isLocal = false
        val dustItem = VideoMediaItem()
        dustItem.projectId = projectID
        dustItem.path = videoPath
        dataOperate?.add(dustItem)
        // 对视频进行图片多略图提取
        ThreadPoolMaxThread.getInstance().execute(object : Runnable {
            override fun run() {
                val retriever = MediaMetadataRetriever()
                try {
                    retriever.setDataSource(videoPath)
                    var value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    if (value != null) {
                        dustItem.duration = value.toLong()
                        dustItem.setVideoOriginDuration(dustItem.duration)
                    }
                    value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                    if (value != null) {
                        dustItem.width = value.toInt()
                    }
                    value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                    if (value != null) {
                        dustItem.height = value.toInt()
                    }
                    value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)
                    if (value != null) {
                        dustItem.rotation = value.toInt()
                    }
                    dustItem.name = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    if (FileUtils.checkExistAudio(dustItem.path)) {
                        val temppath = ConstantPath.createExtractVideo2Audio(projectID, dustItem.name)
                        dustItem.extracAudioPath = FileUtils.removeEndiff(temppath)
                        val backroundTask = BackroundTask(arrayOf(dustItem.path, temppath, temppath), FfmpegTaskType.EXTRACT_AUDIO)
                        dustItem.extractTaskId = backroundTask.id
                        FfmpegBackgroundHelper.getInstance().exeCuteTask(backroundTask)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    retriever.release()
                    return
                }
                retriever.release()
            }
        })
        return true
    }

    /**
     * 图片处理
     *
     * @param originItem
     * @param addMediaCallback
     * @param addMediaSuccessCallback
     */
    fun addMediaItemPhoto(originItem: MediaEntity, addMediaCallback: AddMediaCallback?, addMediaSuccessCallback: AddMediaSuccessCallback?) {
        val dustItem = PhotoMediaItem()
        try {
            dustItem.rotation = BytesBitmap.getExifOrientation(originItem.path, originItem.rotation)
            if (!ObjectUtils.isEmpty(originItem.size)) {
                //512 X 512 竟然size出现了这种值
                dustItem.size = originItem.size.toLong()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        dustItem.projectId = projectID
        dustItem.path = originItem.path
        if (getBoolean(SpUtil.IMAGE_DURATION_APPLY_ALL, false)) {
            dustItem.duration = ConstantMediaSize.IMAGE_DURATION.toLong()
            dustItem.tempDuration = ConstantMediaSize.IMAGE_DURATION.toLong()
        } else {
            dustItem.duration = originItem.getDuration()
            dustItem.tempDuration = originItem.getDuration()
        }
        dustItem.width = originItem.width
        dustItem.height = originItem.height
        dustItem.name = originItem.title
        dustItem.id = originItem.id
        //这个一定需要在dataOperate.add(dustItem)之前
        val index = dataOperate!!.size
        dataOperate!!.add(dustItem)
        addMediaCallback?.resultData(dustItem)
        //转场系列处理
        if (currentTransitionSeries !== TransitionSeries.NONE) {
            dustItem.transitionFilter = TransitionFactory.initFilters(
                currentTransitionSeries.array.get((index) % currentTransitionSeries.array.size)
            )
        }
        preTreatmentMediaItem(index, dustItem, true, true, addMediaSuccessCallback)
        if (preTreatment is BaseTimePreTreatment && ObjectUtils.isEmpty(widgetMimaps)) {
            val mimap = (preTreatment as BaseTimePreTreatment).createMimapBitmaps()
            //修复create产生null后抛出npe
            if (mimap != null) {
                widgetMimaps = ArrayList(mimap)
            }
        }
        //        dustItem.setTransitionFilter(TransitionFactory.initFilters(TransitionType.PAG_CARTON));
    }

    /**
     * 预处理动作
     */
    fun preTreatmentMediaItem(index: Int, mediaItem: MediaItem?, newThead: Boolean, createDuration: Boolean, addMediaSuccessCallback: AddMediaSuccessCallback?) {
        if (!newThead) {
            preTreatmentMediaItemImpl(index, mediaItem, createDuration, addMediaSuccessCallback)
            return
        }
        ThreadPoolManager.getThreadPool().execute(object : Runnable {
            override fun run() {
                preTreatmentMediaItemImpl(index, mediaItem, createDuration, addMediaSuccessCallback)
                //                mediaItem.setTransitionFilter(preTreatment.createTransition(index));
            }
        })
    }

    private fun preTreatmentMediaItemImpl(index: Int, mediaItem: MediaItem?, createDuration: Boolean, addMediaSuccessCallback: AddMediaSuccessCallback?) {
        if (createDuration && mediaItem!!.isImage) {
            val duration = preTreatment!!.dealDuration(index)
            if (duration == 0L) { //clear none主题，则进行最小转场时长限制
                if (mediaItem.duration == 0L) {
                    mediaItem.duration = ConstantMediaSize.TRANSITION_DURATION.toLong()
                }
            } else {
                mediaItem.duration = duration
            }
        }
        val path = if (ObjectUtils.isEmpty(mediaItem!!.cropPath)) mediaItem.path else mediaItem.cropPath
        if (mediaItem.isImage) {
            try {
                mediaItem.bitmap = preTreatment!!.addFrame(PretreatConfig.Builder().setRatioType(ConstantMediaSize.ratioType).setPath(path).setRotation(mediaItem.rotation).setIndex(index).setBackgroundType(currentBackgroundInfo.backgrondType).setColorValue(currentBackgroundInfo.colorValue).setBlurLevel(currentBackgroundInfo.blurLevel).setVideo(mediaItem.isVideo).build())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            LogUtils.v("MediaDataRepository", "index:" + index + "," + "bitmap:" + mediaItem.bitmap)
        }
        if (ConstantMediaSize.isTheme()) {
            mediaItem.transitionFilter = preTreatment!!.createTransition(index)
        }
        dealMimaps(index, mediaItem)
        addMediaSuccessCallback?.resultData(mediaItem)
    }

    /**
     * 主题挂载控件
     *
     * @param index
     * @param mediaItem
     */
    private fun dealMimaps(index: Int, mediaItem: MediaItem?) {
        if (preTreatment == null) {
            preTreatment = BasePreTreatment()
        }
        if (mimaps == null) {
            mimaps = ConcurrentHashMap()
        }
        if (preTreatment is BaseTimePreTreatment && ObjectUtils.isEmpty(widgetMimaps)) {
            val mimap = (preTreatment as BaseTimePreTreatment).createMimapBitmaps()
            //修复create产生null后抛出npe
            if (mimap != null) {
                widgetMimaps = ArrayList(mimap)
            }
        }
        //从pretreatment中获取静态控件
        val realIndex = if (preTreatment!!.mipmapsCount == 0) 0 else index % preTreatment!!.mipmapsCount
        if (retryMimap(mimaps!!.get(realIndex))) {
            var temp = preTreatment!!.getMimapBitmaps(ConstantMediaSize.ratioType, index)
            temp = temp ?: ArrayList()
            mimaps!![realIndex] = temp
        }
        //从pretreatment中获取动态控件
        val finalDMMBS = preTreatment!!.getFinalDynamicMimapBitmapSources(ConstantMediaSize.ratioType)
        if (finalDMMBS != null && realIndex < finalDMMBS.size) {
            preTrementGif()
            //从缓存中获取
            val sources = finalDMMBS.get(realIndex)
            if (sources.size > 0) {
                val gifs: MutableList<List<GifDecoder.GifFrame?>> = ArrayList(sources.size)
                LogUtils.i(javaClass.simpleName + " dynamicMimaps operation", "ready to get gif from dynamicMimaps")
                for (gifPath: String in sources) {
                    if (dynamicMimaps!!.get(gifPath) != null) {
                        gifs.add(ArrayList(dynamicMimaps!!.get(gifPath)))
                    }
                }
                //设置gif
                mediaItem!!.dynamicMitmaps = gifs
            }
        }
        if (ObjectUtils.isEmpty(themePags) || pagContainNull()) {
            val pagFiles = preTreatment!!.themePags
            if (pagFiles != null) {
                themePags = ArrayList(pagFiles)
            }
        }
        mediaItem!!.themePags = themePags
        mediaItem.tempBitmap = preTreatment!!.getTempBitmap(mediaItem.bitmap, index)
        mediaItem.mimapBitmaps = mimaps!!.get(realIndex)
        if (mediaItem.isImage) {
            mediaItem.afterRotation = preTreatment!!.afterPreRotation()
        }
    }

    /**
     * 预加载gif控件
     */
    fun preTrementGif() {
        if (ObjectUtils.isEmpty(dynamicMimaps)) {
            synchronized(dynamicMipmapsLoadLock) {
                //缓存初始化
                loadGifs(preTreatment)
            }
        }
    }

    /**
     * @return
     */
    private fun pagContainNull(): Boolean {
        if (!ObjectUtils.isEmpty(themePags)) {
            for (file: PAGFile? in themePags!!) {
                if (file == null) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 检测主题mimap控件是否缺失
     *
     * @return
     */
    private fun retryMimap(bitmaps: List<Bitmap?>?): Boolean {
        if (ObjectUtils.isEmpty(bitmaps)) {
            return true
        }
        //再做一次检测，出现有的素材bitmap为null情况，所以需增加鲁棒性
        for (i in bitmaps!!.indices) {
            if (bitmaps.get(i) == null || bitmaps.get(i)!!.isRecycled) {
                return true
            }
        }
        return false
    }

    /**
     * 预处理主元素图片，只这对主片段元素，如裁剪
     */
    fun dealPreCropBitmap(index: Int, mediaItem: MediaItem) {
        val path = if (ObjectUtils.isEmpty(mediaItem.cropPath)) mediaItem.path else mediaItem.cropPath
        try {
            mediaItem.bitmap = preTreatment!!.addFrame(PretreatConfig.Builder().setRatioType(ConstantMediaSize.ratioType).setPath(path).setRotation(mediaItem.rotation).setIndex(index).setBackgroundType(currentBackgroundInfo.backgrondType).setColorValue(currentBackgroundInfo.colorValue).setBlurLevel(currentBackgroundInfo.blurLevel).setVideo(mediaItem.isVideo).build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 对于裁剪后，对主元素进行预处理
     *
     * @param index
     */
    fun cropPreTreament(index: Int) {
        val mediaItem = dataOperate!!.get(index)
        val path = if (ObjectUtils.isEmpty(mediaItem!!.cropPath)) mediaItem.path else mediaItem.cropPath
        mediaItem.bitmap = preTreatment!!.addFrame(PretreatConfig.Builder().setRatioType(ConstantMediaSize.ratioType).setPath(path).setRotation(mediaItem.rotation).setIndex(index).setBackgroundType(currentBackgroundInfo.backgrondType).setColorValue(currentBackgroundInfo.colorValue).setBlurLevel(currentBackgroundInfo.blurLevel).setVideo(mediaItem.isVideo).build())
    }

    /**
     * 如果是无主题的时候，添加默认音乐
     */
    fun setDefaultMusic() {
        if (!checkIsTheme() && ObjectUtils.isEmpty(extAudioList)) {
            val musicEntity = createDefaultMusic()
            val audioMediaItem = AudioMediaItem()
            audioMediaItem.projectId = projectID
            audioMediaItem.path = musicEntity!!.path
            audioMediaItem.duration = musicEntity.duration.toLong()
            audioMediaItem.originDuration = musicEntity.duration.toLong()
            audioMediaItem.originPath = musicEntity.path
            audioMediaItem.cutEnd = musicEntity.duration.toLong()
            audioMediaItem.title = musicEntity.name
            audioMediaItem.size = musicEntity.size
            defaultAudio = audioMediaItem
            setSingleAudio(defaultAudio)
        }
    }
    /**
     * 数据回收恢复音乐
     */
    //    public void restoreMusic() {
    //        if (checkIsTheme()) {
    //            setCurrentSlideEntity()
    //            return;
    //        }
    //        setDefaultMusic();
    //    }
    /**
     * 通过music入口，移除默认音乐
     */
    fun removeDefaultMusic() {
        defaultAudio = null
    }

    /**
     * 列表位置调换
     */
    fun swapOperate(fromPosition: Int, toPosition: Int) {
        LogUtils.i("swapOperate", "fromPosition:$fromPosition,toPosition:$toPosition")
        preTreatment = ThemeRepository.createPreTreatment(ConstantMediaSize.themeType)
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(dataOperate, i, i + 1)
                if (ConstantMediaSize.themeType != ThemeEnum.NONE) {
                    if (dataOperate!!.get(i)!!.isImage) {
                        dataOperate!!.get(i)!!.duration = preTreatment!!.dealDuration(i)
                    }
                    if (dataOperate!!.get(i + 1)!!.isImage) {
                        dataOperate!!.get(i + 1)!!.duration = preTreatment!!.dealDuration(i + 1)
                    }
                    exchangeMimaps(dataOperate!!.get(i), dataOperate!!.get(i + 1))
                }
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(dataOperate, i, i - 1)
                if (ConstantMediaSize.themeType != ThemeEnum.NONE) {
                    if (dataOperate!!.get(i)!!.isImage) {
                        dataOperate!!.get(i)!!.duration = preTreatment!!.dealDuration(i)
                    }
                    if (dataOperate!!.get(i - 1)!!.isImage) {
                        dataOperate!!.get(i - 1)!!.duration = preTreatment!!.dealDuration(i - 1)
                    }
                    exchangeMimaps(dataOperate!!.get(i), dataOperate!!.get(i - 1))
                }
            }
        }
    }

    /**
     * 交换挂载在mediatem的集合
     *
     * @param from
     * @param to
     */
    private fun exchangeMimaps(from: MediaItem?, to: MediaItem?) {
        val fromMip = from!!.mimapBitmaps
        val fromgif = from.dynamicMitmaps
        from.mimapBitmaps = to!!.mimapBitmaps
        from.dynamicMitmaps = to.dynamicMitmaps
        to.mimapBitmaps = fromMip
        to.dynamicMitmaps = fromgif
    }

    /**
     * 列表位置调换
     */
    fun swap(list: List<MediaItem?>?, fromPosition: Int, toPosition: Int) {
        if (ObjectUtils.isEmpty(list)) {
            return
        }
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(list, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(list, i, i - 1)
            }
        }
    }

    /**
     * 改变比例时，需要改变预处理的控件
     */
    fun changeRatio(ratioType: RatioType?) {
        ConstantMediaSize.ratioType = ratioType
        preTreatment = ThemeHelper.createPreTreatment(ConstantMediaSize.themeType)
        if (preTreatment is BaseTimePreTreatment && ((preTreatment) as BaseTimePreTreatment).existRatio()) {
            if (widgetMimaps != null) {
                widgetMimaps!!.clear()
            }
            val mimap = (preTreatment as BaseTimePreTreatment).createMimapBitmaps()
            //修复create产生null后抛出npe
            if (mimap != null) {
                widgetMimaps!!.addAll(mimap)
            }
        }
        if ((preTreatment!!.javaClass.simpleName == BasePreTreatment::class.java.simpleName) || (preTreatment is BaseTimePreTreatment) || (preTreatment is DownloadedPreTreatmentTemplate)) {
            return
        }
        //重建静态控件
        if (mimaps == null) {
            mimaps = ConcurrentHashMap()
        }
        var mipCount = preTreatment!!.getMipmapsCount()
        //gif加载
        preTrementGif()
        for (i in 0 until mipCount) {
            var temp = preTreatment!!.getMimapBitmaps(ratioType, i)
            temp = temp ?: ArrayList()
            mimaps!![i] = temp
        }
        mipCount = if (mipCount == 0) 1 else mipCount
        for (i in dataOperate!!.indices) {
            dataOperate!!.get(i)!!.mimapBitmaps = mimaps!!.get(i % mipCount)
            updateTreament()
        }
        //从pretreatment中获取动态控件
        val finalDMMBS = preTreatment!!.getFinalDynamicMimapBitmapSources(ratioType)
        if (finalDMMBS != null) {
            LogUtils.i(javaClass.simpleName + " dynamicMimaps operation", "dynamicMimaps synchronized")
            for (i in dataOperate!!.indices) {
                if (i % preTreatment!!.getMipmapsCount() >= finalDMMBS.size) {
                    continue
                }
                //从缓存中获取
                val sources = finalDMMBS.get(i % preTreatment!!.getMipmapsCount())
                if (sources.size > 0) {
                    val gifs: MutableList<List<GifDecoder.GifFrame?>> = ArrayList(sources.size)
                    LogUtils.i(javaClass.simpleName + " dynamicMimaps operation", "ready to get gif from dynamicMimaps")
                    for (gifPath: String? in sources) {
                        gifs.add(ArrayList(dynamicMimaps!!.get((gifPath)!!)))
                    }
                    //设置gif
                    dataOperate!!.get(i)!!.dynamicMitmaps = gifs
                }
            }
        }
    }

    /**
     * 改变比例和背景时，需要改变预处理的控件
     */
    fun changeBackground(backgroundType: Int, groupIndex: Int, customPath: String?) {
        currentBackgroundInfo.backgrondType = backgroundType
        currentBackgroundInfo.groupIndex = groupIndex
        currentBackgroundInfo.customPath = customPath
        //        SharedPreferencesUtil.setObject(ContactUtils.EDIT_BACKGROUND, mSpBgInfo);
    }

    /**
     * 改变比例和背景时，需要改变预处理的控件
     */
    fun changeBackground(spBgInfo: SpBgInfo) {
        currentBackgroundInfo = spBgInfo
        zoomScale = spBgInfo.zoomScale
    }

    /**
     * 更新预处理
     */
    fun updateTreament() {
        preTreatment = ThemeHelper.createPreTreatment(ConstantMediaSize.themeType)
        for (i in dataOperate!!.indices) {
            if (dataOperate!!.get(i)!!.isVideo) {
                continue
            }
            dataOperate!!.get(i)!!.bitmap = preTreatment!!.addFrame(PretreatConfig.Builder().setRatioType(ConstantMediaSize.ratioType).setPath(dataOperate!!.get(i)!!.path).setRotation(dataOperate!!.get(i)!!.rotation).setIndex(i).setCustomPath(currentBackgroundInfo.customPath).setBackgroundType(currentBackgroundInfo.backgrondType).setColorValue(currentBackgroundInfo.colorValue).setBlurLevel(currentBackgroundInfo.blurLevel).setVideo(dataOperate!!.get(i)!!.isVideo).build())
        }
    }

    /**
     * 移除某单项
     *
     * @param index
     */
    fun remove(index: Int): MediaItem? {
        var index = index
        if (index >= dataOperate!!.size || index == -1) {
            return null
        }
        //交换到最后一个在删除，保证时长，控件，转场等主题元素在正确位置上
        while (index + 1 < dataOperate!!.size) {
            swapOperate(index++, index)
        }
        if (dataOperate!!.get(index) is VideoMediaItem) {
            // 对比较长的视频才进行取消5min以上的
            if (dataOperate!!.get(index)!!.getFinalDuration() > 300000 && !ObjectUtils.isEmpty((dataOperate!!.get(index) as VideoMediaItem?)!!.extractTaskId)) {
                FfmpegBackgroundHelper.getInstance().removeTask((dataOperate!!.get(index) as VideoMediaItem?)!!.extractTaskId)
            }
        }
        val mediaItem = dataOperate!!.removeAt(index)
        if (mediaItem!!.isVideo) {
            videoCount--
            LogUtils.v("MediaDataRepository", "photoCount--: $photoCount")
        } else {
            photoCount--
            LogUtils.v("MediaDataRepository", "photoCount--: $photoCount")
        }
        if (!ObjectUtils.isEmpty(dataOperate) && dataOperate!!.get(0)!!.transitionFilter != null) {
            dataOperate!!.get(0)!!.transitionFilter = null
        }
        //如果是主题，则需要把
        return mediaItem
    }

    /**
     * 移除某单项
     *
     * @param index
     */
    fun removeDataOperateSublist(subList: MutableList<MediaItem>, index: Int): MediaItem? {
        if (index >= subList.size || index == -1) {
            return null
        }
        if (subList.get(index) is VideoMediaItem) {
            // 对比较长的视频才进行取消5min以上的
            if (subList.get(index).finalDuration > 300000 && !ObjectUtils.isEmpty((subList.get(index) as VideoMediaItem).extractTaskId)) {
                FfmpegBackgroundHelper.getInstance().removeTask((subList.get(index) as VideoMediaItem).extractTaskId)
            }
        }
        val mediaItem: MediaItem = subList.removeAt(index)
        if (mediaItem.isVideo) {
            videoCount--
            LogUtils.v("MediaDataRepository", "videoCount--: $videoCount")
        } else {
            photoCount--
            LogUtils.v("MediaDataRepository", "photoCount++: $photoCount")
        }
        return mediaItem
    }

    /**
     * 复制其中的一行，原始数据，和media操作数据都同步复制
     * 等待转场方案修复
     *
     * @param index
     */
    fun copyMediaItem(index: Int): MediaItem? {
        return copyMediaItemToPosition(index, index)
    }

    /**
     * 复制其中的一行，原始数据，和media操作数据都同步复制
     * 等待转场方案修复
     * 到指定目标位置
     */
    fun copyMediaItemToPosition(index: Int, position: Int): MediaItem? {
        if (index > dataOperate!!.size) {
            return null
        }
        // 操作mediaItem里面很多对象， 需要进行深度拷贝,并且切换转场头尾帧
        val mediaItemTemp = dataOperate!!.get(index)!!.clone() as MediaItem
        if (preTreatment == null || ConstantMediaSize.themeType.themePreTreatmentClass != preTreatment!!.javaClass) {
            preTreatment = ThemeHelper.createPreTreatment(ConstantMediaSize.themeType)
        }
        // 改变bitmapframe
        try {
            //是主题则按照主题的来创建转场
            if (ConstantMediaSize.themeType != null || ConstantMediaSize.themeType != ThemeEnum.NONE) {
                mediaItemTemp.transitionFilter = preTreatment!!.createTransition(dataOperate!!.size)
            }
            //否则复制转场
            if (mediaItemTemp.transitionFilter != null) {
                val transitionFilter = TransitionFactory.initFilters(mediaItemTemp.transitionFilter.transitionType)
                mediaItemTemp.transitionFilter = transitionFilter
            }
            if (mediaItemTemp.afilter != null) {
                mediaItemTemp.afilter = FilterHelper.initFilters(mediaItemTemp.afilter.getmFilterType(), GpuFilterFactory.getDownPathLocal(mediaItemTemp.afilter.getmFilterType()))
            }
            if (mediaItemTemp.mediaMatrix != null) {
                mediaItemTemp.mediaMatrix = mediaItemTemp.mediaMatrix.clone() as MediaMatrix?
            }
            if (mediaItemTemp.videoCutInterval != null) {
                mediaItemTemp.videoCutInterval = mediaItemTemp.videoCutInterval.clone() as DurationInterval?
            }

            //主题控件
            mediaItemTemp.mimapBitmaps = preTreatment!!.getMimapBitmaps(ConstantMediaSize.ratioType, dataOperate!!.size)
            val realIndex = if (preTreatment!!.mipmapsCount == 0) 0 else dataOperate!!.size % preTreatment!!.mipmapsCount
            val finalDMMBS = preTreatment!!.getFinalDynamicMimapBitmapSources(ConstantMediaSize.ratioType)
            if (finalDMMBS != null && realIndex < finalDMMBS.size) {
                preTrementGif()
                LogUtils.i(javaClass.simpleName + " dynamicMimaps operation", "dynamicMimaps synchronized")

                //从缓存中获取
                val sources = finalDMMBS.get(realIndex)
                if (sources.size > 0) {
                    val gifs: MutableList<List<GifDecoder.GifFrame?>> = ArrayList(sources.size)
                    LogUtils.i(javaClass.simpleName + " dynamicMimaps operation", "ready to get gif from dynamicMimaps")
                    for (gifPath: String? in sources) {
                        gifs.add(ArrayList(dynamicMimaps!!.get((gifPath)!!)))
                    }
                    //设置gif
                    mediaItemTemp.dynamicMitmaps = gifs
                }
            }
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
        }
        mediaItemTemp.equalId = UUID.randomUUID().toString()
        //删除id以防止排序受影响
        if (mediaItemTemp is PhotoMediaItem) {
            var unPassed = true
            val random = Random()
            //id 唯一性检查
            while (unPassed) {
                mediaItemTemp.setId(-Math.abs(random.nextInt()))
                unPassed = dataOperate!!.contains(mediaItemTemp)
            }
        } else {
            mediaItemTemp.id = 0
        }
        insertMediaItem(mediaItemTemp, position)
        if (mediaItemTemp.isVideo) {
            videoCount++
            LogUtils.v("MediaDataRepository", "videoCount++: $videoCount")
        } else {
            photoCount++
            LogUtils.v("MediaDataRepository", "photoCount++: $photoCount")
        }
        return mediaItemTemp
    }

    /**
     * 插入mediaItem
     *
     * @param mediaItem 素材
     * @param index     下标
     */
    fun insertMediaItem(mediaItem: MediaItem?, index: Int) {
        dataOperate!!.add(mediaItem)
        if (index < dataOperate!!.size - 1) {
            var prev = dataOperate!!.size - 2
            while (prev >= index) {
                swapOperate(prev, prev + 1)
                prev--
            }
        }
    }

    /**
     * 更新裁剪后的时长
     *
     * @param operate
     */
    fun updateOriginTrimVideo(operate: MediaItem) {
        val retriever = MediaMetadataRetriever()
        try {
            if (ObjectUtils.isEmpty(operate.trimPath)) {
                return
            }
            retriever.setDataSource(operate.trimPath)
            val tempDuration = (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toLong())
            operate.duration = tempDuration
            //时间计算
            operate.tempDuration = tempDuration
            retriever.release()
        } catch (e: Exception) {
            e.printStackTrace()
            retriever.release()
        }
    }

    /**
     * 更新反序后的信息
     *
     * @param operate
     */
    fun updateVideoReverse(operate: MediaItem) {
        val retriever = MediaMetadataRetriever()
        try {
            if (ObjectUtils.isEmpty((operate as VideoMediaItem).reversePath)) {
                return
            }
            retriever.setDataSource(operate.reversePath)
            val tempDuration = (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toLong())
            operate.setTempDuration(tempDuration)
            operate.setDuration(tempDuration)
            var value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            if (value != null) {
                operate.setWidth(value.toInt())
            }
            value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
            if (value != null) {
                operate.setHeight(value.toInt())
            }
            value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)
            if (value != null) {
                operate.setRotation(value.toInt())
            }
            retriever.release()
            //清楚缩略图
            operate.thumbnails.clear()
        } catch (e: Exception) {
            e.printStackTrace()
            retriever.release()
        }
    }

    /**
     * 数据存储到本地,综合看，用green是失败的策略，
     * 优势比如上用文件形式xml存储json数据结构
     */
    @Synchronized
    fun save2Local() {
    }

    fun onDestroyCurrentThread() {
        if (dataOperate != null) {
            dataOperate!!.clear()
        }
        doodleList?.clear()
        if (extAudioList != null) {
            extAudioList!!.clear()
        }
        if (recordList != null) {
            recordList!!.clear()
        }
        mScreenShotBitmap = null
        isLocal = false
        isLocalMultiEdit = false
        isHasEdit = false
        ConstantMediaSize.themeType = ThemeEnum.NONE
        ConstantMediaSize.themeConstantype = ThemeConstant.HOT
        //重置默认比例
        ConstantMediaSize.ratioType = RatioType.NONE
        ConstantMediaSize.particles = GlobalParticles.NONE
        ConstantMediaSize.innerBorder = NONE
        ConstantMediaSize.transitionSeries = TransitionSeries.NONE
        currentTransitionSeries = ConstantMediaSize.transitionSeries
        preTreatment = null
        zoomScale = 1f
        videoCount = 0
        photoCount = 0
        if (mimaps != null) {
            mimaps!!.clear()
        }
        if (dynamicMimaps != null) {
            dynamicMimaps!!.clear()
        }
        if (widgetMimaps != null) {
            widgetMimaps!!.clear()
        }
    }

    fun onDestory() {
        onDestroyCurrentThread()
    }

    /**
     * 清除数据
     */
    fun clearDraft() {
//        FileUtil.deleteFile(PathUtil.getVideoTrimPath());
//        PathUtil.getVideoTrimPath();
//        deleteProject(currentProject);
    }

    val dataOperateCopy: ArrayList<MediaItem>
        get() {
            val list = ArrayList<MediaItem>()
            for (i in dataOperate!!.indices) {
                val mediaItem = dataOperate!!.get(i)!!.clone() as MediaItem
                list.add(mediaItem)
            }
            return list
        }
    val dataSaveCopy: List<MediaItem>
        get() {
            val list = CopyOnWriteArrayList<MediaItem>()
            for (i in dataOperate!!.indices) {
                val mediaItem = dataOperate!!.get(i)!!.clone() as MediaItem
                list.add(mediaItem)
            }
            return list
        }


    init {
        dataOperate = ArrayList()
        doodleList = ArrayList()
        extAudioList = ArrayList()
        recordList = ArrayList()
        ConstantMediaSize.ratioType = RatioType.getRatioType(getInt(SpUtil.EDIT_RATIO_SELECT, RatioType.NONE.key))
    }


    val projectID: String
        get() {
            if (currentProject != null) {
                return currentProject!!.projectId
            } else {
                establishProject()
                return currentProject!!.projectId
            }
        }

    /**
     * 进入选定工作台或者创建新的工作台
     */
    fun establishProject() {
        isLocal = false
        //        if (currentProject == null) {
//            currentProject = new Project();
//            currentProject.randomProjectId();
//            currentProject.setCreateTime(new Date());
//            if (themeAudio != null) {
//                currentProject.setThemeEnum(ConstantMediaSize.themeType);
//                currentProject.setThemeZipPath(ConstantMediaSize.themePath);
//                currentProject.setThemeMusicPath(themeAudio.getPath());
//            }
//            if (checkIsTheme()) {
//                currentProject.setTranGroup(TransitionRepository.TransiGroup.GROUP_NONE);
//            } else {
//                currentProject.setTranGroup(TransitionRepository.INSTANCE.randomTranGroup());
//            }
//            ExecutorFactory.io().execute(() -> currentProject.setProjectName(PathUtil.murgeOutputPathRename()));
//        }
    }

    fun getmScreenShotBitmap(): Bitmap? {
        return mScreenShotBitmap
    }

    fun setmScreenShotBitmap(mScreenShotBitmap: Bitmap?) {
        this.mScreenShotBitmap = mScreenShotBitmap
    }

    /**
     * 根据类型过滤数据
     *
     * @param waterMarkType
     * @return
     */
    fun getDoodleByType(waterMarkType: WaterMarkType): List<DoodleItem> {
        val doodleTypeList: MutableList<DoodleItem> = ArrayList()
        for (d: DoodleItem in doodleList!!) {
            if (d.waterMarkType == waterMarkType) {
                doodleTypeList.add(d)
            }
        }
        return doodleTypeList
    }

    val doodleTypeList: List<DoodleItem>
        get() {
            val doodleTypeList: MutableList<DoodleItem> = ArrayList()
            for (d: DoodleItem in doodleList!!) {
                if (d.waterMarkType == WaterMarkType.DOODLE) {
                    doodleTypeList.add(d)
                }
            }
            return doodleTypeList
        }
    val doodleCharacter: List<DoodleItem>
        get() {
            val doodleCharaterList: MutableList<DoodleItem> = ArrayList()
            for (d: DoodleItem in doodleList!!) {
                if (d.waterMarkType == WaterMarkType.CHARACTER) {
                    doodleCharaterList.add(d)
                }
            }
            return doodleCharaterList
        }
    val doodleSticker: List<DoodleItem>
        get() {
            val doodlePictureList: MutableList<DoodleItem> = ArrayList()
            for (d: DoodleItem in doodleList!!) {
                if (d.waterMarkType == WaterMarkType.PICTURE) {
                    doodlePictureList.add(d)
                }
            }
            return doodlePictureList
        }

    /**
     * 添加doodle
     *
     * @param doodleItem
     */
    fun addDoodleItem(doodleItem: DoodleItem) {
        doodleItem.projectId = projectID
        doodleList!!.add(doodleItem)
    }

    /**
     * 添加单个音频，该音频覆盖整个播放周期
     *
     * @param audioMediaItem
     */
    fun setSingleAudio(audioMediaItem: AudioMediaItem?) {
        // 检查是否有远程服务合成任务
        extAudioList!!.clear()
        extAudioList!!.add(audioMediaItem)
    }

    /**
     * 清除单音频
     */
    fun removeSingleAudio() {
        if (extAudioList!!.size > 0) {
            if (!ObjectUtils.isEmpty(extAudioList!!.get(0)!!.extractTaskId)) {
                FfmpegBackgroundHelper.getInstance().removeTask(extAudioList!!.get(0)!!.extractTaskId)
            }
            extAudioList!!.removeAt(0)
        }
    }

    /**
     * 添加多片段音频
     */
    fun setMultiAudio(list: List<AudioMediaItem?>?) {
        extAudioList!!.clear()
        extAudioList!!.addAll((list)!!)
    }

    fun hasAudio(): Boolean {
        return extAudioList!!.size > 0
    }


    fun getAudioList(): List<AudioMediaItem?>? {
        if (!ObjectUtils.isEmpty(extAudioList) && extAudioList!!.get(0) !== themeAudio) {
            return extAudioList
        }
        if (themeAudio != null) {
            val list: MutableList<AudioMediaItem?> = ArrayList()
            list.add(themeAudio)
            return list
        }
        return extAudioList
    }

    val audioListCopy: List<AudioMediaItem>
        get() {
            val audioMediaItems: MutableList<AudioMediaItem> = ArrayList()
            if (ObjectUtils.isEmpty(extAudioList)) {
                if (themeAudio != null) {
                    audioMediaItems.add(themeAudio!!)
                }
                return audioMediaItems
            }
            for (i in extAudioList!!.indices) {
                if (extAudioList!!.get(i) != null) {
                    val audioMediaItem = extAudioList!!.get(i)!!.clone() as AudioMediaItem
                    if (audioMediaItem.durationInterval != null) {
                        audioMediaItem.durationInterval = audioMediaItem.durationInterval.clone() as DurationInterval?
                    }
                    audioMediaItems.add(audioMediaItem)
                }
            }
            return audioMediaItems
        }

    /**
     * 音频类型转化+音频重整和合成
     *
     * @param list
     * @return
     */
    fun audioExchange(list: List<CutMusicItem>?): List<AudioMediaItem?>? {
        val audioMediaItems: MutableList<AudioMediaItem?> = ArrayList()
        if (list == null) {
            return null
        }
        var audioMediaItem: AudioMediaItem
        for (cutMusicItem: CutMusicItem in list) {
            audioMediaItem = AudioMediaItem()
            audioMediaItem.projectId = projectID
            audioMediaItem.duration = cutMusicItem.duration
            audioMediaItem.originDuration = cutMusicItem.originDuration
            audioMediaItem.title = cutMusicItem.title
            audioMediaItem.volume = cutMusicItem.volume
            audioMediaItem.originPath = cutMusicItem.originPath
            audioMediaItem.size = cutMusicItem.size
            audioMediaItem.path = cutMusicItem.path
            audioMediaItem.durationInterval = cutMusicItem.durationInterval
            audioMediaItem.cutStart = cutMusicItem.cutStart
            audioMediaItem.cutEnd = cutMusicItem.cutEnd
            audioMediaItems.add(audioMediaItem)
        }
        return audioMediaItems
    }

    /**
     * 音频对象转化，并且后台再重处理裁剪合并音频文件
     *
     * @param list
     * @return
     */
    fun audioExchangeAndDealAudio(list: List<CutMusicItem>?, hasRemoveOrigin: Boolean): List<AudioMediaItem?>? {
        // 对音频文件进行排序
        val audioMediaItems = audioExchange(list)
        // dealAudioDuration(audioMediaItems);
        if (!hasRemoveOrigin) {
//            chanVideoOriginAudioPeriod(audioList);
        }
        setMultiAudio(audioMediaItems)
        return audioMediaItems
    }

    /**
     * 最小单位为5个
     *
     * @param paths
     * @param commandList
     */
    private fun splitMultiAudioMurge(paths: Array<String>, commandList: List<Array<String>>) {
        if (paths.size < 5) {
            return
        }
        val arrayCount = paths.size / 5
        val temp = paths.size % 5
        val num = if (temp == 0) arrayCount else arrayCount + 1
        val newPaths = arrayOfNulls<String>(num)
        for (i in 0 until num) {
        }
    }

    /**
     * @return
     */
    fun calcPreviewRatio(): RatioType {
        var scale: Float
        var temp = 0f
        for (i in dataOperate!!.indices) {
            scale = dataOperate!!.get(i)!!.width.toFloat() / dataOperate!!.get(i)!!.height.toFloat()
            if (dataOperate!!.get(i)!!.rotation == 90 || dataOperate!!.get(i)!!.rotation == 270) {
                scale = dataOperate!!.get(i)!!.height.toFloat() / dataOperate!!.get(i)!!.width.toFloat()
            }
            if (scale > 1.33) {
                return RatioType._16_9
            }
            if (scale > temp) {
                temp = scale
            }
        }
        if (temp > 1 && temp <= 1.33) {
            return RatioType._4_3
        } else if (temp == 1f) {
            return RatioType._1_1
        } else return if (temp < 1 && temp >= 0.75) {
            RatioType._3_4
        } else {
            RatioType._9_16
        }
    }

    fun calcPreviewRatio(mediaItem: MediaItem): RatioType {
        var scale: Float
        var temp = 0f
        scale = mediaItem.width.toFloat() / mediaItem.height.toFloat()
        if (mediaItem.rotation == 90 || mediaItem.rotation == 270) {
            scale = mediaItem.height.toFloat() / mediaItem.width.toFloat()
        }
        if (scale > 1.33) {
            return RatioType._16_9
        }
        if (scale > temp) {
            temp = scale
        }
        if (temp > 1 && temp <= 1.33) {
            return RatioType._4_3
        } else if (temp == 1f) {
            return RatioType._1_1
        } else return if (temp < 1 && temp >= 0.75) {
            RatioType._3_4
        } else {
            RatioType._9_16
        }
    }

    fun calcPreviewRatio(width: Float, height: Float, rotation: Int): RatioType {
        var scale: Float
        var temp = 0f
        scale = width / height
        if (rotation == 90 || rotation == 270) {
            scale = height / width
        }
        if (scale > 1.33) {
            return RatioType._16_9
        }
        if (scale > temp) {
            temp = scale
        }
        if (temp > 1 && temp <= 1.33) {
            return RatioType._4_3
        } else if (temp == 1f) {
            return RatioType._1_1
        } else return if (temp < 1 && temp >= 0.75) {
            RatioType._3_4
        } else {
            RatioType._9_16
        }
    }

    fun calcRatioType(mediaItem: MediaItem): RatioType {
        var scale: Float
        scale = mediaItem.width.toFloat() / mediaItem.height.toFloat()
        if (mediaItem.rotation == 90 || mediaItem.rotation == 270) {
            scale = mediaItem.height.toFloat() / mediaItem.width.toFloat()
        }
        if (scale > 1.33) {
            return RatioType._16_9
        }
        if (scale > 1 && scale <= 1.33) {
            return RatioType._4_3
        } else if (scale == 1f) {
            return RatioType._1_1
        } else return if (scale < 1 && scale >= 0.75) {
            RatioType._3_4
        } else {
            RatioType._9_16
        }
    }

    /**
     * 检测是否有视频
     *
     * @return
     */
    fun checkExistVideo(): Boolean {
        for (mediaItem: MediaItem? in dataOperate!!) {
            if (mediaItem is VideoMediaItem) {
                return true
            }
        }
        return false
    }

    fun checkExistAudio(): Boolean {
        return !ObjectUtils.isEmpty(extAudioList)
    }

    /**
     * 是否为主题
     *
     * @return
     */
    fun checkIsTheme(): Boolean {
        return ConstantMediaSize.themeType != ThemeEnum.NONE
    }

    /**
     * 还原默认音乐
     */
    fun reSetBackMusic() {
        if (!ObjectUtils.isEmpty(audioListBak)) {
            extAudioList = audioListBak
        }
    }

    /**
     * 检测视频全静音
     *
     * @return
     */
    fun checkNotSilenceVoice(): Boolean {
        for (mediaItem: MediaItem? in dataOperate!!) {
            if (mediaItem is VideoMediaItem) {
                if (mediaItem.volume > 0) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 因为主控台实时在渲染，把主bitmap回收经常会出问题
     * 所以bitmap的手动回收放在退出图片选择页面，其他的让系统自动去回收
     *
     * @param mediaItem
     */
    fun clearMediaItem(mediaItem: MediaItem?) {
        if (mediaItem == null) {
            return
        }
        val start = System.currentTimeMillis()
        try {
            //不是默认图才回收
            if ((mediaItem.bitmap != null) && !mediaItem.bitmap.isRecycled && !PhotoMediaItem.assertDefaultBitmap(mediaItem.bitmap)) {
                mediaItem.bitmap.recycle()
            }
            if (mediaItem.tempBitmap != null && !mediaItem.tempBitmap.isRecycled) {
                mediaItem.tempBitmap.recycle()
            }
            //清理视频缓存
            if (mediaItem is VideoMediaItem) {
                if (!ObjectUtils.isEmpty(mediaItem.thumbnails)) {
                    for (bitmap: Bitmap? in mediaItem.thumbnails.values) {
                        bitmap?.recycle()
                    }
                    mediaItem.thumbnails.clear()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        LogUtils.v("MediaDataRepository", "clearMediaItem:" + (System.currentTimeMillis() - start))
    }

    /**
     * 清理数据
     *
     * @param list
     */
    fun clearMediaItem(list: List<MediaItem?>?) {
        if (list != null) {
            for (i in list.indices) {
                clearMediaItem(list.get(i))
            }
        }
    }

    /**
     * 主题修改,重新预处理数据
     * 检测当前主题是否为非主题，如果为非主题切换为主题，则进行时间轴的备份存储
     *
     * @return
     */
    fun changeTheme(slideshowEntity: ThemeEntity): List<MediaItem?>? {
        if (ConstantMediaSize.themeType == ThemeEnum.NONE) {
            bakNoThemeDuration()
        }
        setCurrentSlideEntity(slideshowEntity)
        preTreatment = ThemeRepository.createPreTreatment(slideshowEntity.themeEnum)
        if (!ObjectUtils.isEmpty(mimaps)) {
            for (i in 0 until mimaps!!.size) {
                val bitmaps = mimaps!!.get(i)
                if (!ObjectUtils.isEmpty(bitmaps)) {
                    for (j in bitmaps!!.indices) {
                        if (bitmaps.get(j) != null && !bitmaps.get(j)!!.isRecycled) {
                            bitmaps.get(j)!!.recycle()
                        }
                    }
                }
            }
            mimaps!!.clear()
        }
        //重置gif缓存
        if (!ObjectUtils.isEmpty(dynamicMimaps)) {
            LogUtils.i(javaClass.simpleName + " dynamicMimaps operation", "ready to clear dynamicMimaps")
            for (gif: MutableList<GifDecoder.GifFrame?>? in dynamicMimaps!!.values) {
                for (frame: GifDecoder.GifFrame? in gif!!) {
                    //回收bitmap
                    frame!!.image.recycle()
                }
                gif.clear()
            }
            dynamicMimaps!!.clear()
            LogUtils.i(javaClass.simpleName + " dynamicMimaps operation", "dynamicMimaps cleared")
        }
        if (themePags != null) {
            themePags!!.clear()
            themePags = null
        }

        //重置时间轴控件缓存
        if (!ObjectUtils.isEmpty(widgetMimaps)) {
            for (j in widgetMimaps!!.indices) {
                if (widgetMimaps!!.get(j) != null && !widgetMimaps!!.get(j)!!.isRecycled) {
                    widgetMimaps!!.get(j)!!.recycle()
                }
            }
            widgetMimaps!!.clear()
        }
        preTrementGif()
        for (i in dataOperate!!.indices) {
            val dustItem = dataOperate!!.get(i)
            preTreatmentMediaItem(i, dustItem, true, true, null)
        }
        //在EditorActivity页面中切换主题时加载widgetMimaps
        if (preTreatment is BaseTimePreTreatment && ObjectUtils.isEmpty(widgetMimaps)) {
            val mimap = (preTreatment as BaseTimePreTreatment).createMimapBitmaps()
            //修复create产生null后抛出npe
            if (mimap != null) {
                widgetMimaps = ArrayList(mimap)
            }
        }
        if (!ObjectUtils.isEmpty(themeAudio)) {
            themeAudio!!.durationInterval = null
        }
        setCurrentSlideEntity(slideshowEntity)
        return dataOperate
    }

    /**
     * 备份无主题时的时间轴
     */
    private fun bakNoThemeDuration() {
        for (mediaItem: MediaItem? in dataOperate!!) {
            mediaItem!!.synNothemeDuration()
        }
    }

    /**
     * 移除主题
     */
    fun clearThemeNone(random: Boolean): List<MediaItem?>? {
        LogUtils.d("changeTheme", "execute")
        if (random) {
            currentProject!!.tranGroup = randomTranGroup()
        } else {
            currentProject!!.tranGroup = TransitionRepository.TransiGroup.GROUP_NONE
        }
        ConstantMediaSize.themeType = ThemeEnum.NONE
        ConstantMediaSize.themeConstantype = ThemeConstant.HOT
        if (!ObjectUtils.isEmpty(audioListBak)) {
            extAudioList = audioListBak
        }
        if (ObjectUtils.isEmpty(extAudioList) && random) {
            setDefaultMusic()
        }
        themeAudio = null
        preTreatment = ThemeHelper.createPreTreatment(ThemeEnum.NONE)
        //这两块LogUtils.d中间进度有点长，看看什么情况
        LogUtils.d("changeTheme", "step1")
        for (i in dataOperate!!.indices) {
            val dustItem = dataOperate!!.get(i)
            dustItem!!.transitionFilter = null
            preTreatmentMediaItem(i, dustItem, false, true, null)
            dustItem.resumeNothemeDuration()
        }
        LogUtils.d("changeTheme", "preTreatmentMediaItem")
        if (!ObjectUtils.isEmpty(mimaps)) {
            for (i in 0 until mimaps!!.size) {
                val bitmaps = mimaps!!.get(i)
                if (!ObjectUtils.isEmpty(bitmaps)) {
                    for (j in bitmaps!!.indices) {
                        if (bitmaps.get(j) != null && !bitmaps.get(j)!!.isRecycled) {
                            bitmaps.get(j)!!.recycle()
                        }
                    }
                }
            }
            mimaps!!.clear()
        }
        //重置gif缓存
        if (!ObjectUtils.isEmpty(dynamicMimaps)) {
            LogUtils.i(javaClass.simpleName + " dynamicMimaps operation", "ready to clear dynamicMimaps")
            for (gif: MutableList<GifDecoder.GifFrame?>? in dynamicMimaps!!.values) {
                for (frame: GifDecoder.GifFrame? in gif!!) {
                    //回收bitmap
                    frame!!.image.recycle()
                }
                gif.clear()
            }
            dynamicMimaps!!.clear()
            LogUtils.i(javaClass.simpleName + " dynamicMimaps operation", "dynamicMimaps cleared")
        }

        //重置时间轴控件缓存
        if (!ObjectUtils.isEmpty(widgetMimaps)) {
            for (j in widgetMimaps!!.indices) {
                if (widgetMimaps!!.get(j) != null && !widgetMimaps!!.get(j)!!.isRecycled) {
                    widgetMimaps!!.get(j)!!.recycle()
                }
            }
            widgetMimaps!!.clear()
        }
        if (!ObjectUtils.isEmpty(themeAudio)) {
            themeAudio!!.durationInterval = null
        }
        //        if (currentProject != null) {
//            currentProject.setThemeEnum(ThemeEnum.NONE);
//            currentProject.setThemeMusicPath("");
//            currentProject.setThemeZipPath("");
//        }
        return dataOperate
    }

    /**
     * 针对主题音乐，如果播放边界发生变化，则进行重置
     */
    fun resetMusic() {
        if (!ObjectUtils.isEmpty(themeAudio)) {
            themeAudio!!.durationInterval = null
        }
        if (!ObjectUtils.isEmpty(defaultAudio)) {
            defaultAudio!!.durationInterval = null
        }
    }

    /**
     * 加载主题全部的gif
     *
     * @param preTreatment 主题预处理类
     */
    private fun loadGifs(preTreatment: IPretreatment?) {
        LogUtils.v("MediaDataRepository", "..............................................")
        if (preTreatment == null) {
            return
        }
        //获取地址源loadGifs
        val sources = preTreatment.getFinalDynamicMimapBitmapSources(ConstantMediaSize.ratioType)
            ?: return
        if (dynamicMimaps == null) {
            dynamicMimaps = ConcurrentHashMap()
        } else {
            dynamicMimaps!!.clear()
        }
        if (ObjectUtils.isEmpty(sources)) {
            return
        }

        //遍历地址并加载
        for (source: Array<String> in sources) {
            for (gifPath: String in source) {
                if (!dynamicMimaps!!.containsKey(gifPath)) {
                    try {
                        val frames = loadGif(gifPath).toMutableList()
                        dynamicMimaps!![gifPath] = frames
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    /**
     * 加载gif
     *
     * @param gifPath gif在主题文件夹下的名字
     */
    @Throws(RuntimeException::class)
    private fun loadGif(gifPath: String): List<GifDecoder.GifFrame?> {
        var list: List<GifDecoder.GifFrame?> = mutableListOf()
        if (gifPath.endsWith(LocalPreTreatmentTemplate.END_SUFFIX)) {
            val resourceId = gifPath.replace(LocalPreTreatmentTemplate.END_SUFFIX, "").toInt()
            list = getGif(MyApplication.instance, resourceId).frames
        } else {
            list = getGif(MyApplication.instance, ConstantMediaSize.themePath + gifPath + ConstantMediaSize.SUFFIX)!!.frames
        }
        if (!ObjectUtils.isEmpty(list)) {
            return list
        }
        val mGifDecoder = GifDecoder()
        var inputStream: InputStream? = null
        try {
            inputStream = FileInputStream(ConstantMediaSize.themePath + gifPath)
            //这里面的方法会对inpustream进行close
            val status = mGifDecoder.read(inputStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val frames = mGifDecoder.frames
        //填充delay时间
        for (i in frames.indices) {
            if (frames.get(i)!!.delay == 0) {
                frames.get(i)!!.delay = frames.get(i - 1)!!.delay
            }
        }
        return frames
    }

    /**
     * 预处理渲染信息，包含自定义背景等
     * 执行线程任务[ThreadPoolManager]
     */
    fun preTrementInfo() {
        if (currentBackgroundInfo.backgrondType == BackgroundType.IMAGE) {
            val bitmap = BitmapUtil.getSmallBitmapByWH(currentBackgroundInfo.customPath, 0, ConstantMediaSize.localBitmapWidth, ConstantMediaSize.localBitmapHeight)
            currentBackgroundInfo.blurBitmap = BitmapUtil.blurBitmap(MediaSdk.getInstance().context, currentBackgroundInfo.blurLevel / 4, bitmap)
        }
    }

    val backroundInfoCopy: BGInfo
        /**
         * 获取当前编辑的背景参数
         *
         * @return
         */
        get() {
            val bgInfo = BGInfo()
            bgInfo.backroundType = currentBackgroundInfo.backgrondType
            bgInfo.blurLevel = currentBackgroundInfo.blurLevel
            bgInfo.customBitmap = currentBackgroundInfo.blurBitmap
            var hexColor = "#FF000000"
            if (currentBackgroundInfo.colorValue != 0) {
                hexColor = "#" + Integer.toHexString(currentBackgroundInfo.colorValue)
            }
            if (hexColor.length == 7) {
                hexColor = hexColor.replace("#", "#FF")
            }
            val rgba = ColorUtil.hex2Rgb(hexColor)
            bgInfo.rgbs = rgba
            bgInfo.zoomScale = currentBackgroundInfo.zoomScale
            return bgInfo
        }

    /**
     * 存储裁剪数据
     *
     * @param dataOperate
     */
    fun saveCropInfo(dataOperate: List<MediaItem>) {
        val cropInfo: MutableMap<Int, Rect> = HashMap()
        for (i in dataOperate.indices) {
            if (dataOperate.get(i) is PhotoMediaItem) {
                val photoMediaItem = dataOperate.get(i) as PhotoMediaItem
                if (photoMediaItem.cropRect != null) {
                    cropInfo[i] = photoMediaItem.cropRect
                }
            }
        }
        setObject<Any>(SpUtil.SAVE_LOCAL_CROP_RECT + projectID, if (cropInfo.isEmpty()) null else cropInfo)
    }

    /**
     * 加载裁剪数据
     *
     * @param dataOperate
     */
    fun loadCropInfo(dataOperate: List<MediaItem>) {
        val cropInfo = getObject(SpUtil.SAVE_LOCAL_CROP_RECT + projectID, CORP_RECT_MAP_TYPE) as Map<Int, Rect>?
        if (cropInfo == null || cropInfo.size > dataOperate.size) {
            return
        }
        for (entry: Map.Entry<Int, Rect> in cropInfo.entries) {
            (dataOperate.get(entry.key) as PhotoMediaItem).cropRect = entry.value
        }
    }

    fun getRecordList(): List<AudioMediaItem?>? {
        return recordList
    }

    val recordListCopy: List<AudioMediaItem>
        get() {
            val audioMediaItems: MutableList<AudioMediaItem> = ArrayList()
            if (ObjectUtils.isEmpty(recordList)) {
                return audioMediaItems
            }
            for (i in recordList!!.indices) {
                if (recordList!!.get(i) != null) {
                    val audioMediaItem = recordList!!.get(i)!!.clone() as AudioMediaItem
                    if (audioMediaItem.durationInterval != null) {
                        audioMediaItem.durationInterval = audioMediaItem.durationInterval.clone() as DurationInterval?
                    }
                    audioMediaItems.add(audioMediaItem)
                }
            }
            return audioMediaItems
        }

    fun setRecordList(recordList: MutableList<AudioMediaItem?>) {
        this.recordList = recordList
        for (audioMediaItem: AudioMediaItem? in recordList) {
            audioMediaItem!!.record = true
        }
    }

    /**
     * 设置随机转场
     */
    fun randomAllTransition(): Boolean {
        currentProject!!.tranGroup = randomTranGroup()
        var changeTime = false
        for (i in dataOperate!!.indices) {
            dataOperate!!.get(i)!!.transitionFilter = randomTransitionFilter(currentProject!!.tranGroup, i)
            if (dataOperate!!.get(i)!!.finalDuration < ConstantMediaSize.TRANSITION_DURATION) {
                dataOperate!!.get(i)!!.duration = ConstantMediaSize.TRANSITION_DURATION.toLong()
                changeTime = true
            }
        }
        if (changeTime) {
            resetMusic()
        }
        return changeTime
    }

    /**
     * 移除随机转场
     */
    fun removeAllTransition() {
        currentProject!!.tranGroup = TransitionRepository.TransiGroup.GROUP_NONE
        for (i in dataOperate!!.indices) {
            dataOperate!!.get(i)!!.transitionFilter = TransitionFactory.initFilters(TransitionType.NONE)
        }
    }

    /**
     * 指定id的mediaItem位置
     * 不支持MusicMediaItem
     */
    fun indexOfMediaEntity(mediaEntity: MediaEntity): Int {
        if (mediaEntity.type == MediaEntity.TYPE_IMAGE) {
            val mediaItem = MediaItem(mediaEntity.id, MediaType.PHOTO)
            return dataOperate!!.indexOf(mediaItem)
        } else if (mediaEntity.type == MediaEntity.TYPE_VIDEO) {
            var i = dataOperate!!.size - 1
            while (i > -1) {
                if ((dataOperate!!.get(i)!!.path == mediaEntity.getPath())) {
                    break
                }
                i--
            }
            return i
        } else {
            return -1
        }
    }

    /**
     * 寻找mediaEntity被添加后可能出现的位置
     *
     * @param mediaEntity
     * @param originSize  起始寻找位置
     */
    fun indexOfMediaEntity(originSize: Int, mediaEntity: MediaEntity): Int {
        var index = originSize
        while (index < dataOperate!!.size) {
            val mediaItem = dataOperate!!.get(index)
            if (mediaEntity.id == mediaItem!!.id) {
                return index
            }
            if ((mediaEntity.path == mediaItem.path)) {
                return index
            }
            index++
        }
        return -1
    }

    /**
     * 图片处理
     */
    fun addMediaItemPhotoWithOutPretreatment(originItem: MediaEntity, addMediaCallback: AddMediaCallback?) {
        val dustItem = PhotoMediaItem()
        dustItem.path = originItem.path
        if (!ObjectUtils.isEmpty(originItem.size)) {
            dustItem.size = originItem.size.toLong()
        }
        dustItem.width = originItem.width
        dustItem.height = originItem.height
        dustItem.name = originItem.title
        dustItem.id = originItem.id
        //这个一定需要在dataOperate.add(dustItem)之前
        val index = dataOperate!!.size
        dataOperate!!.add(dustItem)
        dustItem.extra = originItem
        addMediaCallback?.resultData(dustItem)
    }

    /**
     * 指定id的mediaItem位置
     */
    fun indexOfMediaItemId(id: Int): Int {
        return dataOperate!!.indexOf(MediaItem(id, MediaType.PHOTO))
    }

    val audioListKotlin: List<AudioMediaItem?>?
        /**
         * kotlin获取audioList时调用[.getAudioList]
         * 但该方法不符合逻辑需求，故创建此方法代替kotlin中获取aduioList
         *
         * @return
         */
        get() = extAudioList

    /**
     * 是否为baseTimePreTreatment主题
     *
     * @return
     */
    fun checkIsBeatTheme(): Boolean {
        if (preTreatment == null) {
            return false
        }
        return if (preTreatment is BaseTimePreTreatment) {
            true
        } else false
    }

    fun getInstance(): MediaDataRepository {
        return this;
    }

    /**
     * 当音乐只有一首时更新单个音乐的播放范围（总长度）
     *
     * @param totalTime 总长度
     */
    fun updateSingleAudioDurationInterval(totalTime: Int) {
        if (extAudioList!!.size == 1) {
            extAudioList!!.get(0)!!.durationInterval = DurationInterval(0, totalTime)
        }
    }

    fun clear() {
        dataOperate!!.clear()
        photoCount = 0
        videoCount = 0
        LogUtils.v("MediaDataRepository", "photoCount: 0")
        LogUtils.v("MediaDataRepository", "videoCount: 0")
    }

    private val TAG = "MediaDataRepository"
    private val CORP_RECT_MAP_TYPE = object : TypeToken<Map<Int?, Rect?>?>() {}.type
}