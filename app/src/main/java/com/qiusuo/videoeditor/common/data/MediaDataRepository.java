package com.qiusuo.videoeditor.common.data;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.innerBorder;
import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.isTheme;
import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.ratioType;
import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.themeType;
import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.transitionSeries;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.offline.DownloadHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.ConstantPath;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.global.ThreadPoolManager;
import com.ijoysoft.mediasdk.common.global.ThreadPoolMaxThread;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.ColorUtil;
import com.ijoysoft.mediasdk.common.utils.FileUtils;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.AudioMediaItem;
import com.ijoysoft.mediasdk.module.entity.BGInfo;
import com.ijoysoft.mediasdk.module.entity.DoodleItem;
import com.ijoysoft.mediasdk.module.entity.DurationInterval;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaMatrix;
import com.ijoysoft.mediasdk.module.entity.MediaType;
import com.ijoysoft.mediasdk.module.entity.PhotoMediaItem;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.entity.VideoMediaItem;
import com.ijoysoft.mediasdk.module.entity.WaterMarkType;
import com.ijoysoft.mediasdk.module.mediacodec.BackroundTask;
import com.ijoysoft.mediasdk.module.mediacodec.FfmpegBackgroundHelper;
import com.ijoysoft.mediasdk.module.mediacodec.FfmpegTaskType;
import com.ijoysoft.mediasdk.module.opengl.InnerBorder;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.FilterHelper;
import com.ijoysoft.mediasdk.module.opengl.particle.GlobalParticles;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeConstant;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeEnum;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeHelper;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.IPretreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.LocalPreTreatmentTemplate;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionSeries;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;
import com.ijoysoft.mediasdk.module.playControl.GlideGifDecoder;
import com.ijoysoft.mediasdk.view.BackgroundType;
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository;
import com.qiusuo.videoeditor.base.MyApplication;
import com.qiusuo.videoeditor.common.bean.CutMusicItem;
import com.qiusuo.videoeditor.common.bean.MediaEntity;
import com.qiusuo.videoeditor.common.bean.MusicEntity;
import com.qiusuo.videoeditor.common.bean.Project;
import com.qiusuo.videoeditor.common.bean.SlideshowEntity;
import com.qiusuo.videoeditor.common.bean.SpBgInfo;
import com.qiusuo.videoeditor.common.bean.ThemeGroupEntity;
import com.qiusuo.videoeditor.common.bean.ThemeResGroupEntity;
import com.qiusuo.videoeditor.common.constant.DownloadPath;
import com.qiusuo.videoeditor.util.AndroidUtil;
import com.qiusuo.videoeditor.util.BytesBitmap;
import com.qiusuo.videoeditor.util.FileUtil;
import com.qiusuo.videoeditor.util.IOUtil;
import com.qiusuo.videoeditor.util.SpUtil;
import com.qiusuo.videoeditor.util.ZipUtils;

import org.libpag.PAGFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 数据存储
 * 包含外部数据同步到sdk列表数据
 * 后续工作台的数据都同步在这里
 */
public class MediaDataRepository {
    private static final String TAG = "MediaDataRepository";
    //    private ArrayList<MediaEntity> dataOrigin;
    private List<MediaItem> dataOperate;
    private Project currentProject;
    private Bitmap mScreenShotBitmap;
    /**
     * 贴纸
     */
    private List<DoodleItem> doodleList;
    private ArrayList<AudioMediaItem> audioList;
    private ArrayList<AudioMediaItem> audioListBak;
    //录音模块，和外音并列,单独控制
    private List<AudioMediaItem> recordList;
    //现在音频有两种现象，一直主题音乐，而是外音（包括多段音乐）
    private AudioMediaItem defaultAudio;
    private AudioMediaItem themeAudio;
    private boolean isLocal;
    private boolean isLocalMultiEdit;// 本地draft多次进入编辑
    private ThemeGroupEntity mThemeGroupEntity;
    private ThemeResGroupEntity mThemeResGroupEntity;
    private SlideshowEntity mSlideshowEntity;
    private IPretreatment preTreatment;//添加主题模块
    private boolean hasEdit;
    //针对主题，把挂载在片段中的控件抽取出来，重复利用
    private ConcurrentHashMap<Integer, List<Bitmap>> mimaps;

    private int videoCount = 0;

    private int photoCount = 0;

    private MediaEntity tempMediaEntity;
    /**
     * gif控件
     * gif的bitmap较多，不适合重复加载，
     * 以string为key进行缓存，重复利用
     */
    private ConcurrentHashMap<String, List<GifDecoder.GifFrame>> dynamicMimaps;
    /**
     * gif加载的同步锁
     */
    private Object dynamicMipmapsLoadLock = new Object();

    //挂载在时间轴的控件
    private ArrayList<Bitmap> widgetMimaps;
    //预处理模糊
    private SpBgInfo mSpBgInfo = SpBgInfo.defaultBgInfo();
    //草稿进入编辑，记录用户判断封面缩略图是否更新
    private MediaItem draftMediaItem;


    private static final Type CORP_RECT_MAP_TYPE = new TypeToken<Map<Integer, Rect>>() {
    }.getType();

    /**
     * 转场系列
     */
    public TransitionSeries currentTransitionSeries = TransitionSeries.NONE;

    private float zoomScale = 1f;

    //挂载主题上的pag文件
    private List<PAGFile> themePags;

    // 检测是否抽取视频源音
    public void checkExtractVideoAudio() {
        if (ObjectUtils.isEmpty(dataOperate)) {
            return;
        }
        // 做音频抽取动作,这里分是进入主控制台还是进入裁剪子页面
        for (int i = 0; i < dataOperate.size(); i++) {
            MediaItem dustItem = dataOperate.get(i);
            if (dustItem instanceof VideoMediaItem) {
                if (FileUtils.checkExistAudio(dustItem.getPath()) && ObjectUtils.isEmpty(((VideoMediaItem) dustItem).getExtractTaskId())) {
                    String temppath = ConstantPath.createExtractVideo2Audio(getProjectID(), dustItem.getName());
                    ((VideoMediaItem) dustItem).setExtracAudioPath(FileUtils.removeEndiff(temppath));
                    BackroundTask backroundTask = new BackroundTask(new String[]{dustItem.getPath(), temppath, temppath}, FfmpegTaskType.EXTRACT_AUDIO);
                    ((VideoMediaItem) dustItem).setExtractTaskId(backroundTask.getId());
                    FfmpegBackgroundHelper.getInstance().exeCuteTask(backroundTask);
                }
            }
        }
    }


    public void clearData() {
        recyleBitmaps();
        if (doodleList != null) {
            doodleList.clear();
        }
        if (audioList != null) {
            audioList.clear();
        }
        if (recordList != null) {
            recordList.clear();
        }
        if (themePags != null) {
            themePags.clear();
            themePags = null;
        }
        mScreenShotBitmap = null;
        mThemeGroupEntity = null;
        mThemeResGroupEntity = null;
        mSlideshowEntity = null;
        defaultAudio = null;
        themeAudio = null;
        ConstantMediaSize.themeType = ThemeEnum.NONE;
        hasEdit = false;
        currentProject = null;
        ConstantMediaSize.particles = GlobalParticles.NONE;
        innerBorder = InnerBorder.Companion.getNONE();
        mSpBgInfo = SpBgInfo.defaultBgInfo();
        //重置视频和照片数量计数
        clear();
    }

    /**
     * 回收数据
     */
    public void recyleBitmaps() {
        List<MediaItem> dataList = dataOperate;
        dataOperate = new ArrayList();
        if (dataList != null) {
            for (int i = 0; i < dataList.size(); i++) {
                clearMediaItem(dataList.get(i));
            }
        }

        ConcurrentHashMap<Integer, List<Bitmap>> tempMips = mimaps;
        mimaps = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, List<GifDecoder.GifFrame>> tempDynamic = dynamicMimaps;
        dynamicMimaps = new ConcurrentHashMap<>();
        List<Bitmap> tempwidget = widgetMimaps;
        widgetMimaps = new ArrayList<>();
        if (!ObjectUtils.isEmpty(tempMips)) {
            for (int i = 0; i < tempMips.size(); i++) {
                List<Bitmap> bitmaps = tempMips.get(i);
                if (!ObjectUtils.isEmpty(bitmaps)) {
                    for (int j = 0; j < bitmaps.size(); j++) {
                        if (bitmaps.get(j) != null && !bitmaps.get(j).isRecycled()) {
                            try {
                                bitmaps.get(j).recycle();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }
        }

        //重置gif缓存
        if (!ObjectUtils.isEmpty(tempDynamic)) {
            LogUtils.i(getClass().getSimpleName() + " dynamicMimaps operation", "ready to clear dynamicMimaps");
            for (List<GifDecoder.GifFrame> gif : tempDynamic.values()) {
                for (GifDecoder.GifFrame frame : gif) {
                    //回收bitmap
                    frame.image.recycle();
                }
                gif.clear();
            }
            LogUtils.i(getClass().getSimpleName() + " dynamicMimaps operation", "dynamicMimaps cleared");
        }

        //重置时间轴控件缓存
        if (!ObjectUtils.isEmpty(tempwidget)) {
            for (int j = 0; j < tempwidget.size(); j++) {
                if (tempwidget.get(j) != null && !tempwidget.get(j).isRecycled()) {
                    tempwidget.get(j).recycle();
                }
            }
        }
    }


    public ThemeGroupEntity getThemeGroupEntity() {
        if (mThemeGroupEntity == null) {
//            mThemeGroupEntity = XmlParserHelper.pullxmlThemeGroup(DownloadPath.THEME_DATA_SAVE);
        }
        if (mThemeGroupEntity == null) {
            return new ThemeGroupEntity();
        }
        return mThemeGroupEntity;
    }

    public void setThemeLocalData() {
//        mThemeGroupEntity = XmlParserHelper.pullxmlThemeGroup(DownloadPath.THEME_DATA_SAVE);
    }

    public void setThemeGoupEntity(ThemeGroupEntity themeGroupEntity) {
        this.mThemeGroupEntity = themeGroupEntity;
    }


    public ThemeResGroupEntity getThemeResGroupEntity() {
        if (mThemeResGroupEntity == null) {
            String str = IOUtil.readFile(new File(DownloadPath.THEME_RESOURCE_SAVE), "utf-8");
            mThemeResGroupEntity = new Gson().fromJson(str, ThemeResGroupEntity.class);
        }
        return mThemeResGroupEntity;
    }

    public void setThemeResGroupEntity(ThemeResGroupEntity resGroupEntity) {
        this.mThemeResGroupEntity = resGroupEntity;
    }

    /**
     * 改变主题
     *
     * @param slideEntity
     */
    @SuppressLint("WrongConstant")
    public void setCurrentSlideEntity(SlideshowEntity slideEntity) {
        mSpBgInfo.setBackgrondType(BackgroundType.SELF);
        mSpBgInfo.setBlurLevel(50);
        //当且仅当同一主题，然后已选了自定义歌曲时，不进行歌曲切换
        if (!ThemeFactory.checkLockTheme(slideEntity.getThemeEnum())) {

            ThreadPoolManager.getThreadPool().execute(() -> {
                try {
//                    ZipUtils.UnZipFolder(DownloadHelper.getDownloadPath(slideEntity.getZipPath()), slideEntity.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        if (ConstantMediaSize.themeType != slideEntity.getThemeEnum()) {
            if (audioList.size() > 0) {
                audioListBak = (ArrayList<AudioMediaItem>) audioList.clone();
            }
            audioList.clear();
            themeAudio = new AudioMediaItem();
            themeAudio.setProjectId(getProjectID());
            // 改变文件名并赋值
            themeAudio.setPath(slideEntity.getMusicLocalPath());
            MusicEntity musicEntity = MediaManager.INSTANCE.queryOnlineMusic(slideEntity.getMusicLocalPath());
            themeAudio.setTitle(slideEntity.getName());
            if (musicEntity != null) {
                themeAudio.setTitle(musicEntity.getName());
                themeAudio.setType(musicEntity.getType());
            } else {
                themeAudio.setTitle(FileUtil.getFileName(themeAudio.getPath()));
            }
            themeAudio.setOriginPath(themeAudio.getPath());
            themeAudio.setDuration(slideEntity.getMusicDuration());
            if (slideEntity.getMusicDuration() == 0) {
                ThreadPoolManager.getThreadPool().execute(() -> {
                    themeAudio.setDuration(AndroidUtil.getAudioTime(slideEntity.getMusicLocalPath()));
                    if (themeAudio.getDuration() == 0) {
                        themeAudio.setDuration(2500);

                    }
                    themeAudio.setOriginDuration(themeAudio.getDuration());
                    themeAudio.setCutEnd(themeAudio.getDuration());
                });
            }
            themeAudio.setCutEnd(themeAudio.getDuration());
            themeAudio.setVolume(100);
        }
        //如果是主题设置默认比例，不能用none
        if (ratioType == RatioType.NONE) {
            ratioType = ConstantMediaSize.THEME_DEFAULT_RATIO;
        }
        ConstantMediaSize.themePath = slideEntity.getPath() + File.separator + slideEntity.getName();
        ConstantMediaSize.themeType = slideEntity.getThemeEnum();
        ConstantMediaSize.themeConstantype = slideEntity.getThemeConstanType();
        if (slideEntity.getThemeEnum().getParticleType() != null) {
            ConstantMediaSize.particles = slideEntity.getThemeEnum().getParticleType();
        }
//        if (currentProject != null) {
//            currentProject.setThemeEnum(slideEntity.getThemeEnum());
//            currentProject.setThemeMusicPath(themeAudio.getPath());
//            currentProject.setThemeZipPath(ConstantMediaSize.themePath);
//        }
        if (themeType == ThemeEnum.NONE) {
            themeAudio = null;
        }
        this.mSlideshowEntity = slideEntity;
        preTreatment = ThemeHelper.createPreTreatment(ConstantMediaSize.themeType);
    }


    /**
     * 创建主题音乐
     */
    private void themeAudio() {

    }

    public List<Bitmap> getWidgetMimaps() {
        return widgetMimaps;
    }

    /**
     * 从本地加载数据后，赋值当前主题
     *
     * @param themeEnum
     */
    public void setCurrentSlideEntity(ThemeEnum themeEnum, String themeMusicPath) {
        if (themeEnum != ThemeEnum.NONE && audioList.isEmpty()) {
            themeAudio = new AudioMediaItem();
            themeAudio.setPath(themeMusicPath);
            MusicEntity musicEntity = MediaManager.INSTANCE.queryOnlineMusic(themeMusicPath);
            themeAudio.setTitle(themeEnum.getName());
            if (musicEntity != null) {
                themeAudio.setTitle(musicEntity.getName());
                themeAudio.setType(musicEntity.getType());
                themeAudio.setDuration(musicEntity.getDuration());
            }
            if (themeAudio.getDuration() == 0) {
                ThreadPoolManager.getThreadPool().execute(() -> {
                    themeAudio.setDuration(AndroidUtil.getAudioTime(themeAudio.getPath()));
                });
            }
            themeAudio.setOriginPath(themeAudio.getPath());
            themeAudio.setCutEnd(themeAudio.getDuration());
            themeAudio.setVolume(100);
            if (themeEnum.getParticleType() != null) {
                ConstantMediaSize.particles = themeEnum.getParticleType();
            }
        }
        mSlideshowEntity = new SlideshowEntity();
        mSlideshowEntity.setThemeEnum(themeEnum);
    }

    @Nullable
    public SlideshowEntity getCurrentSlideShow() {
        return mSlideshowEntity;
    }

    public AudioMediaItem getThemeAudio() {
        return themeAudio;
    }

    /**
     * 覆盖原有数据
     *
     * @param list
     */
    public void overideData(List<MediaItem> list) {
        this.dataOperate = list;
        photoCount = 0;
        videoCount = 0;
        for (MediaItem mediaItem : list) {
            if (mediaItem.isVideo()) {
                videoCount++;
            } else {
                photoCount++;
            }
        }
    }

    /**
     * 覆盖原有数据
     *
     * @param list
     */
    public void updateData(List<MediaItem> list) {
        this.dataOperate = (ArrayList<MediaItem>) list;
    }

    /**
     * 检查视频类资源是否正在做音频抽取动作
     * listOrigin是copy类，是原来的
     * change是修改过后
     */
    public void checkDeleteVideoIsExtracting(ArrayList<MediaItem> listCopy) {
        if (listCopy.size() == dataOperate.size()) {
            return;
        }
        List<BackroundTask> removes = new ArrayList<>();
        for (MediaItem mediaItem : listCopy) {
            if (mediaItem.getMediaType() == MediaType.VIDEO && !dataOperate.contains(mediaItem)) {
                if (mediaItem.getFinalDuration() > 300000) {
                    removes.add(new BackroundTask(((VideoMediaItem) mediaItem).getExtractTaskId()));
                }
            }
        }
        if (!ObjectUtils.isEmpty(removes)) {
            FfmpegBackgroundHelper.getInstance().removeTask(removes);
        }
    }


    /**
     * 单项添加,点击
     */
    public void addMediaItem(MediaEntity mediaEntity, boolean isExtract, AddMediaCallback addMediaCallback) { // 可能要加个锁，防止添加数据后，显示的操作数据与源数据不匹配
        if (preTreatment == null) {
            preTreatment = new BasePreTreatment();
        }
        if (mediaEntity.type == MediaEntity.TYPE_IMAGE) {
            addMediaItemPhoto(mediaEntity, addMediaCallback, null);
            photoCount++;
            LogUtils.v("MediaDataRepository", "photoCount++: " + photoCount);
        } else {
            addMediaItemVideo(mediaEntity, isExtract, addMediaCallback, null);
            videoCount++;
            LogUtils.v("MediaDataRepository", "videoCount++: " + videoCount);
        }

    }

    public void addMediaItemWhole(MediaEntity mediaEntity, boolean isExtract, AddMediaSuccessCallback successCallback) { // 可能要加个锁，防止添加数据后，显示的操作数据与源数据不匹配
        if (preTreatment == null) {
            preTreatment = new BasePreTreatment();
        }
        if (mediaEntity.type == MediaEntity.TYPE_IMAGE) {
            addMediaItemPhoto(mediaEntity, null, successCallback);
            photoCount++;
            LogUtils.v("MediaDataRepository", "photoCount++: " + photoCount);
        } else {
            addMediaItemVideo(mediaEntity, isExtract, null, successCallback);
            videoCount++;
            LogUtils.v("MediaDataRepository", "videoCount++: " + videoCount);
        }
    }

    public interface AddMediaCallback {
        void resultData(MediaItem mediaItem);
    }

    public interface AddMediaSuccessCallback {
        void resultData(MediaItem mediaItem);
    }

    public interface LoadDBSuccess {
        void onSuccess(boolean loss);

        default void fail() {
        }

        default void fail(String msg) {
        }

    }

    /**
     * 数据类型转化
     * 这里如果是视频的话，就对视频进行前后帧的本地存储，方便后续使用
     * 这样在纹理加载的时候，要对目标的bitmap进行缩小
     *
     * @param originItem
     * @return
     */


    public void addMediaItemVideo(final MediaEntity originItem, final boolean isExtract, AddMediaCallback addMediaCallback, AddMediaSuccessCallback addMediaSuccessCallback) {
        addMediaItemVideo(originItem, isExtract, addMediaCallback, addMediaSuccessCallback, false);
    }

    /**
     * 视频支持主题的展示，所以设置一个展示的最小值{@link ConstantMediaSize#THEME_VIDEO_DURATION}
     *
     * @param originItem
     * @param isExtract
     * @param addMediaCallback
     * @param addMediaSuccessCallback
     * @param getInfoBlocked
     */
    public void addMediaItemVideo(final MediaEntity originItem, final boolean isExtract, AddMediaCallback addMediaCallback, AddMediaSuccessCallback addMediaSuccessCallback, boolean getInfoBlocked) {
        final VideoMediaItem dustItem = new VideoMediaItem();
        dustItem.setProjectId(getProjectID());
        dustItem.setPath(originItem.path);
        dustItem.setSize(Long.parseLong(originItem.size == null ? "0" : originItem.size));
        dustItem.setDuration(originItem.duration);
        dustItem.setVideoOriginDuration(originItem.originDuration == 0 ? originItem.duration : originItem.originDuration);
        dustItem.setWidth(originItem.width);
        dustItem.setHeight(originItem.height);
        dustItem.setName(originItem.title);
        dustItem.setId(originItem.id);
        dustItem.setVideoCutInterval(originItem.getVideoCutInterval());
        dustItem.setTrimPath(originItem.getTrimPath());
        //如果主题为none并且无转场值为none，则进行随机转场赋值,把视频裁剪过滤功能过滤掉即视频裁剪不加转场
//        if (!checkIsTheme() && dustItem.getTransitionFilter() == null && isExtract) {
//            dustItem.setTransitionFilter(TransitionRepository.INSTANCE.randomTransitionFilter(currentProject.getTranGroup(), dataOperate.size()));
//        }

        final int index = dataOperate.size();
        dataOperate.add(dustItem);
        if (addMediaCallback != null) {
            addMediaCallback.resultData(dustItem);
        }
        //转场系列处理
        if (currentTransitionSeries != TransitionSeries.NONE) {
            dustItem.setTransitionFilter(TransitionFactory.initFilters(
                    currentTransitionSeries.getArray()[(index) % currentTransitionSeries.getArray().length]
            ));
        }
        if (FileUtils.checkExistAudio(dustItem.getPath()) && isExtract) {
            String temppath = ConstantPath.createExtractVideo2Audio(getProjectID(), dustItem.getName());
            dustItem.setExtracAudioPath(FileUtils.removeEndiff(temppath));
            BackroundTask backroundTask = new BackroundTask(new String[]{dustItem.getPath(), temppath, temppath}, FfmpegTaskType.EXTRACT_AUDIO);
            dustItem.setExtractTaskId(backroundTask.getId());
            FfmpegBackgroundHelper.getInstance().exeCuteTask(backroundTask);
        }
        Runnable runnable = () -> {
            dealMimaps(index, dustItem);
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                retriever.setDataSource(originItem.getTrimPath() != null ? originItem.getTrimPath() : originItem.path);
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH) != null) {
                    originItem.width = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                }
                if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT) != null) {
                    originItem.height = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                }
                dustItem.setWidth(originItem.width);
                dustItem.setHeight(originItem.height);
                if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE) != null) {
                    int bitRate = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
                    dustItem.setBitRate(bitRate);
                }
                if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION) != null) {
                    originItem.rotation = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
                }
                if (dustItem.getDuration() == 0 && retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) != null) {
                    dustItem.setDuration(Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
                    dustItem.setVideoOriginDuration(originItem.originDuration == 0 ? originItem.duration : originItem.originDuration);
//                    if (checkIsTheme()) {
//                        dustItem.setDuration(Math.min(dustItem.getDuration(), ConstantMediaSize.THEME_VIDEO_DURATION));
//                        if (preTreatment instanceof BaseTimePreTreatment) {
//                            dustItem.setDuration(Math.min(dustItem.getDuration(), ConstantMediaSize.THEME_TIMETYPE_DURATION));
//                        }
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                retriever.release();
            }
            dustItem.setRotation(originItem.rotation);
            dustItem.setAfterRotation(originItem.rotation);
            if (addMediaSuccessCallback != null) {
                addMediaSuccessCallback.resultData(dustItem);
            }
        };
        if (getInfoBlocked) {
            runnable.run();
        } else {
            ThreadPoolManager.getThreadPool().execute(runnable);
        }

    }

    public boolean addMediaItemVideo(String videoPath) {
        dataOperate = new ArrayList<>();
        isLocal = false;
        final VideoMediaItem dustItem = new VideoMediaItem();
        dustItem.setProjectId(getProjectID());
        dustItem.setPath(videoPath);
        dataOperate.add(dustItem);
        // 对视频进行图片多略图提取
        ThreadPoolMaxThread.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                try {
                    retriever.setDataSource(videoPath);
                    String value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    if (value != null) {
                        dustItem.setDuration(Long.parseLong(value));
                        dustItem.setVideoOriginDuration(dustItem.getDuration());
                    }
                    value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                    if (value != null) {
                        dustItem.setWidth(Integer.parseInt(value));
                    }
                    value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                    if (value != null) {
                        dustItem.setHeight(Integer.parseInt(value));
                    }
                    value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
                    if (value != null) {
                        dustItem.setRotation(Integer.parseInt(value));
                    }
                    dustItem.setName(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
                    if (FileUtils.checkExistAudio(dustItem.getPath())) {
                        String temppath = ConstantPath.createExtractVideo2Audio(getProjectID(), dustItem.getName());
                        dustItem.setExtracAudioPath(FileUtils.removeEndiff(temppath));
                        BackroundTask backroundTask = new BackroundTask(new String[]{dustItem.getPath(), temppath, temppath}, FfmpegTaskType.EXTRACT_AUDIO);
                        dustItem.setExtractTaskId(backroundTask.getId());
                        FfmpegBackgroundHelper.getInstance().exeCuteTask(backroundTask);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    retriever.release();
                    return;
                }
                retriever.release();
            }
        });
        return true;
    }


    /**
     * 图片处理
     *
     * @param originItem
     * @param addMediaCallback
     * @param addMediaSuccessCallback
     */
    public void addMediaItemPhoto(MediaEntity originItem, AddMediaCallback addMediaCallback, AddMediaSuccessCallback addMediaSuccessCallback) {
        PhotoMediaItem dustItem = new PhotoMediaItem();

        try {
            dustItem.setRotation(BytesBitmap.getExifOrientation(originItem.path, originItem.rotation));
            if (!ObjectUtils.isEmpty(originItem.size)) {
                //512 X 512 竟然size出现了这种值
                dustItem.setSize(Long.parseLong(originItem.size));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dustItem.setProjectId(getProjectID());
        dustItem.setPath(originItem.path);
        if (SpUtil.INSTANCE.getBoolean(SpUtil.IMAGE_DURATION_APPLY_ALL, false)) {
            dustItem.setDuration(ConstantMediaSize.IMAGE_DURATION);
            dustItem.setTempDuration(ConstantMediaSize.IMAGE_DURATION);
        } else {
            dustItem.setDuration(originItem.getDuration());
            dustItem.setTempDuration(originItem.getDuration());
        }

        dustItem.setWidth(originItem.width);
        dustItem.setHeight(originItem.height);
        dustItem.setName(originItem.title);
        dustItem.setId(originItem.id);
        //这个一定需要在dataOperate.add(dustItem)之前
        final int index = dataOperate.size();
        dataOperate.add(dustItem);
        if (addMediaCallback != null) {
            addMediaCallback.resultData(dustItem);
        }
        //转场系列处理
        if (currentTransitionSeries != TransitionSeries.NONE) {
            dustItem.setTransitionFilter(TransitionFactory.initFilters(
                    currentTransitionSeries.getArray()[(index) % currentTransitionSeries.getArray().length]
            ));
        }
        preTreatmentMediaItem(index, dustItem, true, true, addMediaSuccessCallback);
        if (preTreatment instanceof BaseTimePreTreatment && ObjectUtils.isEmpty(widgetMimaps)) {
            List<Bitmap> mimap = ((BaseTimePreTreatment) preTreatment).createMimapBitmaps();
            //修复create产生null后抛出npe
            if (mimap != null) {
                widgetMimaps = new ArrayList<>(mimap);
            }
        }
//        dustItem.setTransitionFilter(TransitionFactory.initFilters(TransitionType.PAG_CARTON));
    }

    /**
     * 预处理动作
     */
    public void preTreatmentMediaItem(int index, MediaItem mediaItem, boolean newThead, boolean createDuration, AddMediaSuccessCallback addMediaSuccessCallback) {
        if (!newThead) {
            preTreatmentMediaItemImpl(index, mediaItem, createDuration, addMediaSuccessCallback);
            return;
        }
        ThreadPoolManager.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                preTreatmentMediaItemImpl(index, mediaItem, createDuration, addMediaSuccessCallback);
//                mediaItem.setTransitionFilter(preTreatment.createTransition(index));
            }
        });
    }

    private void preTreatmentMediaItemImpl(int index, MediaItem mediaItem, boolean createDuration, AddMediaSuccessCallback addMediaSuccessCallback) {
        if (createDuration && mediaItem.isImage()) {
            long duration = preTreatment.dealDuration(index);
            if (duration == 0) {//clear none主题，则进行最小转场时长限制
                if (mediaItem.getDuration() == 0) {
                    mediaItem.setDuration(ConstantMediaSize.TRANSITION_DURATION);
                }
            } else {
                mediaItem.setDuration(duration);
            }
        }
        String path = ObjectUtils.isEmpty(mediaItem.getCropPath()) ? mediaItem.getPath() : mediaItem.getCropPath();
        if (mediaItem.isImage()) {
            try {
                mediaItem.setBitmap(preTreatment.addFrame(new PretreatConfig.Builder().setRatioType(ratioType).setPath(path).setRotation(mediaItem.getRotation()).setIndex(index).setBackgroundType(mSpBgInfo.getBackgrondType()).setColorValue(mSpBgInfo.getColorValue()).setBlurLevel(mSpBgInfo.getBlurLevel()).setVideo(mediaItem.isVideo()).build()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogUtils.v("MediaDataRepository", "index:" + index + "," + "bitmap:" + mediaItem.getBitmap());
        }
        if (isTheme()) {
            mediaItem.setTransitionFilter(preTreatment.createTransition(index));
        }
        dealMimaps(index, mediaItem);
        if (addMediaSuccessCallback != null) {
            addMediaSuccessCallback.resultData(mediaItem);
        }

    }

    /**
     * 主题挂载控件
     *
     * @param index
     * @param mediaItem
     */
    private void dealMimaps(int index, MediaItem mediaItem) {
        if (preTreatment == null) {
            preTreatment = new BasePreTreatment();
        }
        if (mimaps == null) {
            mimaps = new ConcurrentHashMap<>();
        }
        if (preTreatment instanceof BaseTimePreTreatment && ObjectUtils.isEmpty(widgetMimaps)) {
            List<Bitmap> mimap = ((BaseTimePreTreatment) preTreatment).createMimapBitmaps();
            //修复create产生null后抛出npe
            if (mimap != null) {
                widgetMimaps = new ArrayList<>(mimap);
            }
        }
        //从pretreatment中获取静态控件
        final int realIndex = preTreatment.getMipmapsCount() == 0 ? 0 : index % preTreatment.getMipmapsCount();

        if (retryMimap(mimaps.get(realIndex))) {
            List<Bitmap> temp = preTreatment.getMimapBitmaps(ratioType, index);
            temp = temp == null ? new ArrayList<>() : temp;
            mimaps.put(realIndex, temp);
        }
        //从pretreatment中获取动态控件
        String[][] finalDMMBS = preTreatment.getFinalDynamicMimapBitmapSources(ratioType);
        if (finalDMMBS != null && realIndex < finalDMMBS.length) {
            preTrementGif();
            //从缓存中获取
            String[] sources = finalDMMBS[realIndex];
            if (sources.length > 0) {
                List<List<GifDecoder.GifFrame>> gifs = new ArrayList<>(sources.length);
                LogUtils.i(getClass().getSimpleName() + " dynamicMimaps operation", "ready to get gif from dynamicMimaps");
                for (String gifPath : sources) {
                    if (dynamicMimaps.get(gifPath) != null) {
                        gifs.add(new ArrayList<>(dynamicMimaps.get(gifPath)));
                    }
                }
                //设置gif
                mediaItem.setDynamicMitmaps(gifs);
            }
        }
        if (ObjectUtils.isEmpty(themePags) || pagContainNull()) {
            List<PAGFile> pagFiles = preTreatment.getThemePags();
            if (pagFiles != null) {
                themePags = new ArrayList<>(pagFiles);
            }
        }
        mediaItem.setThemePags(themePags);
        mediaItem.setTempBitmap(preTreatment.getTempBitmap(mediaItem.getBitmap(), index));
        mediaItem.setMimapBitmaps(mimaps.get(realIndex));
        if (mediaItem.isImage()) {
            mediaItem.setAfterRotation(preTreatment.afterPreRotation());
        }
    }

    /**
     * 预加载gif控件
     */
    public void preTrementGif() {
        if (ObjectUtils.isEmpty(dynamicMimaps)) {
            synchronized (dynamicMipmapsLoadLock) {
                //缓存初始化
                loadGifs(preTreatment);
            }
        }
    }

    /**
     * @return
     */
    private boolean pagContainNull() {
        if (!ObjectUtils.isEmpty(themePags)) {
            for (PAGFile file : themePags) {
                if (file == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检测主题mimap控件是否缺失
     *
     * @return
     */
    private boolean retryMimap(List<Bitmap> bitmaps) {
        if (ObjectUtils.isEmpty(bitmaps)) {
            return true;
        }
        //再做一次检测，出现有的素材bitmap为null情况，所以需增加鲁棒性
        for (int i = 0; i < bitmaps.size(); i++) {
            if (bitmaps.get(i) == null || bitmaps.get(i).isRecycled()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 预处理主元素图片，只这对主片段元素，如裁剪
     */
    public void dealPreCropBitmap(int index, MediaItem mediaItem) {
        String path = ObjectUtils.isEmpty(mediaItem.getCropPath()) ? mediaItem.getPath() : mediaItem.getCropPath();
        try {
            mediaItem.setBitmap(preTreatment.addFrame(new PretreatConfig.Builder().setRatioType(ratioType).setPath(path).setRotation(mediaItem.getRotation()).setIndex(index).setBackgroundType(mSpBgInfo.getBackgrondType()).setColorValue(mSpBgInfo.getColorValue()).setBlurLevel(mSpBgInfo.getBlurLevel()).setVideo(mediaItem.isVideo()).build()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对于裁剪后，对主元素进行预处理
     *
     * @param index
     */
    public void cropPreTreament(int index) {
        MediaItem mediaItem = dataOperate.get(index);
        String path = ObjectUtils.isEmpty(mediaItem.getCropPath()) ? mediaItem.getPath() : mediaItem.getCropPath();

        mediaItem.setBitmap(preTreatment.addFrame(new PretreatConfig.Builder().setRatioType(ratioType).setPath(path).setRotation(mediaItem.getRotation()).setIndex(index).setBackgroundType(mSpBgInfo.getBackgrondType()).setColorValue(mSpBgInfo.getColorValue()).setBlurLevel(mSpBgInfo.getBlurLevel()).setVideo(mediaItem.isVideo()).build()));
    }

    /**
     * 如果是无主题的时候，添加默认音乐
     */
    public void setDefaultMusic() {
        if (!checkIsTheme() && ObjectUtils.isEmpty(audioList)) {
            MusicEntity musicEntity = MediaManager.INSTANCE.createDefaultMusic();
            AudioMediaItem audioMediaItem = new AudioMediaItem();
            audioMediaItem.setProjectId(getProjectID());
            audioMediaItem.setPath(musicEntity.getPath());
            audioMediaItem.setDuration(musicEntity.getDuration());
            audioMediaItem.setOriginDuration(musicEntity.getDuration());
            audioMediaItem.setOriginPath(musicEntity.getPath());
            audioMediaItem.setCutEnd(musicEntity.getDuration());
            audioMediaItem.setTitle(musicEntity.getName());
            audioMediaItem.setSize(musicEntity.getSize());
            defaultAudio = audioMediaItem;
            setSingleAudio(defaultAudio);
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
    public void removeDefaultMusic() {
        defaultAudio = null;
    }

    /**
     * 列表位置调换
     */
    public void swapOperate(int fromPosition, int toPosition) {
        LogUtils.i("swapOperate", "fromPosition:" + fromPosition + ",toPosition:" + toPosition);
        preTreatment = ThemeFactory.createPreTreatment(ConstantMediaSize.themeType);
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(dataOperate, i, i + 1);
                if (ConstantMediaSize.themeType != ThemeEnum.NONE) {
                    if (dataOperate.get(i).isImage()) {
                        dataOperate.get(i).setDuration(preTreatment.dealDuration(i));
                    }
                    if (dataOperate.get(i + 1).isImage()) {
                        dataOperate.get(i + 1).setDuration(preTreatment.dealDuration(i + 1));
                    }
                    exchangeMimaps(dataOperate.get(i), dataOperate.get(i + 1));

                }
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(dataOperate, i, i - 1);
                if (ConstantMediaSize.themeType != ThemeEnum.NONE) {
                    if (dataOperate.get(i).isImage()) {
                        dataOperate.get(i).setDuration(preTreatment.dealDuration(i));
                    }
                    if (dataOperate.get(i - 1).isImage()) {
                        dataOperate.get(i - 1).setDuration(preTreatment.dealDuration(i - 1));
                    }
                    exchangeMimaps(dataOperate.get(i), dataOperate.get(i - 1));
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
    private void exchangeMimaps(MediaItem from, MediaItem to) {
        List<Bitmap> fromMip = from.getMimapBitmaps();
        List<List<GifDecoder.GifFrame>> fromgif = from.getDynamicMitmaps();
        from.setMimapBitmaps(to.getMimapBitmaps());
        from.setDynamicMitmaps(to.getDynamicMitmaps());
        to.setMimapBitmaps(fromMip);
        to.setDynamicMitmaps(fromgif);
    }


    /**
     * 列表位置调换
     */
    public void swap(List<MediaItem> list, int fromPosition, int toPosition) {
        if (ObjectUtils.isEmpty(list)) {
            return;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
    }

    /**
     * 改变比例时，需要改变预处理的控件
     */
    public void changeRatio(RatioType ratioType) {
        ConstantMediaSize.ratioType = ratioType;
        preTreatment = ThemeHelper.createPreTreatment(ConstantMediaSize.themeType);
        if (preTreatment instanceof BaseTimePreTreatment && ((BaseTimePreTreatment) (preTreatment)).existRatio()) {
            if (widgetMimaps != null) {
                widgetMimaps.clear();
            }
            List<Bitmap> mimap = ((BaseTimePreTreatment) preTreatment).createMimapBitmaps();
            //修复create产生null后抛出npe
            if (mimap != null) {
                widgetMimaps.addAll(mimap);
            }
        }
        if (preTreatment.getClass().getSimpleName().equals(BasePreTreatment.class.getSimpleName()) || (preTreatment instanceof BaseTimePreTreatment) || (preTreatment instanceof DownloadedPreTreatmentTemplate)) {
            return;
        }
        //重建静态控件
        if (mimaps == null) {
            mimaps = new ConcurrentHashMap<>();
        }
        int mipCount = preTreatment.getMipmapsCount();
        //gif加载
        preTrementGif();
        for (int i = 0; i < mipCount; i++) {
            List<Bitmap> temp = preTreatment.getMimapBitmaps(ratioType, i);
            temp = temp == null ? new ArrayList<>() : temp;
            mimaps.put(i, temp);
        }
        mipCount = mipCount == 0 ? 1 : mipCount;
        for (int i = 0; i < dataOperate.size(); i++) {
            dataOperate.get(i).setMimapBitmaps(mimaps.get(i % mipCount));
            updateTreament();
        }
        //从pretreatment中获取动态控件
        String[][] finalDMMBS = preTreatment.getFinalDynamicMimapBitmapSources(ratioType);
        if (finalDMMBS != null) {
            LogUtils.i(getClass().getSimpleName() + " dynamicMimaps operation", "dynamicMimaps synchronized");

            for (int i = 0; i < dataOperate.size(); i++) {
                if (i % preTreatment.getMipmapsCount() >= finalDMMBS.length) {
                    continue;
                }
                //从缓存中获取
                String[] sources = finalDMMBS[i % preTreatment.getMipmapsCount()];
                if (sources.length > 0) {
                    List<List<GifDecoder.GifFrame>> gifs = new ArrayList<>(sources.length);
                    LogUtils.i(getClass().getSimpleName() + " dynamicMimaps operation", "ready to get gif from dynamicMimaps");
                    for (String gifPath : sources) {
                        gifs.add(new ArrayList<>(dynamicMimaps.get(gifPath)));
                    }
                    //设置gif
                    dataOperate.get(i).setDynamicMitmaps(gifs);
                }
            }
        }

    }

    /**
     * 改变比例和背景时，需要改变预处理的控件
     */
    public void changeBackground(int backgroundType, int groupIndex, String customPath) {
        mSpBgInfo.setBackgrondType(backgroundType);
        mSpBgInfo.setGroupIndex(groupIndex);
        mSpBgInfo.setCustomPath(customPath);
//        SharedPreferencesUtil.setObject(ContactUtils.EDIT_BACKGROUND, mSpBgInfo);
    }

    /**
     * 改变比例和背景时，需要改变预处理的控件
     */
    public void changeBackground(SpBgInfo spBgInfo) {
        this.mSpBgInfo = spBgInfo;
        zoomScale = spBgInfo.getZoomScale();
    }

    /**
     * 更新预处理
     */
    public void updateTreament() {
        preTreatment = ThemeHelper.createPreTreatment(ConstantMediaSize.themeType);
        for (int i = 0; i < dataOperate.size(); i++) {
            if (dataOperate.get(i).isVideo()) {
                continue;
            }
            dataOperate.get(i).setBitmap(preTreatment.addFrame(new PretreatConfig.Builder().setRatioType(ratioType).setPath(dataOperate.get(i).getPath()).setRotation(dataOperate.get(i).getRotation()).setIndex(i).setCustomPath(mSpBgInfo.getCustomPath()).setBackgroundType(mSpBgInfo.getBackgrondType()).setColorValue(mSpBgInfo.getColorValue()).setBlurLevel(mSpBgInfo.getBlurLevel()).setVideo(dataOperate.get(i).isVideo()).build()));
        }
    }

    /**
     * 移除某单项
     *
     * @param index
     */
    public MediaItem remove(int index) {
        if (index >= dataOperate.size() || index == -1) {
            return null;
        }
        //交换到最后一个在删除，保证时长，控件，转场等主题元素在正确位置上
        while (index + 1 < dataOperate.size()) {
            swapOperate(index++, index);
        }
        if (dataOperate.get(index) instanceof VideoMediaItem) {
            // 对比较长的视频才进行取消5min以上的
            if (dataOperate.get(index).getFinalDuration() > 300000 && !ObjectUtils.isEmpty(((VideoMediaItem) dataOperate.get(index)).getExtractTaskId())) {
                FfmpegBackgroundHelper.getInstance().removeTask(((VideoMediaItem) dataOperate.get(index)).getExtractTaskId());
            }
        }
        MediaItem mediaItem = dataOperate.remove(index);
        if (mediaItem.isVideo()) {
            videoCount--;
            LogUtils.v("MediaDataRepository", "photoCount--: " + photoCount);
        } else {
            photoCount--;
            LogUtils.v("MediaDataRepository", "photoCount--: " + photoCount);
        }
        if (!ObjectUtils.isEmpty(dataOperate) && dataOperate.get(0).getTransitionFilter() != null) {
            dataOperate.get(0).setTransitionFilter(null);
        }
        //如果是主题，则需要把
        return mediaItem;
    }


    /**
     * 移除某单项
     *
     * @param index
     */
    public MediaItem removeDataOperateSublist(List<MediaItem> subList, int index) {
        if (index >= subList.size() || index == -1) {
            return null;
        }
        if (subList.get(index) instanceof VideoMediaItem) {
            // 对比较长的视频才进行取消5min以上的
            if (subList.get(index).getFinalDuration() > 300000 && !ObjectUtils.isEmpty(((VideoMediaItem) subList.get(index)).getExtractTaskId())) {
                FfmpegBackgroundHelper.getInstance().removeTask(((VideoMediaItem) subList.get(index)).getExtractTaskId());
            }
        }
        MediaItem mediaItem = subList.remove(index);
        if (mediaItem.isVideo()) {
            videoCount--;
            LogUtils.v("MediaDataRepository", "videoCount--: " + videoCount);
        } else {
            photoCount--;
            LogUtils.v("MediaDataRepository", "photoCount++: " + photoCount);
        }
        return mediaItem;
    }


    /**
     * 复制其中的一行，原始数据，和media操作数据都同步复制
     * 等待转场方案修复
     *
     * @param index
     */
    public MediaItem copyMediaItem(int index) {
        return copyMediaItemToPosition(index, index);
    }

    /**
     * 复制其中的一行，原始数据，和media操作数据都同步复制
     * 等待转场方案修复
     * 到指定目标位置
     */
    public MediaItem copyMediaItemToPosition(int index, int position) {
        if (index > dataOperate.size()) {
            return null;
        }
        // 操作mediaItem里面很多对象， 需要进行深度拷贝,并且切换转场头尾帧
        MediaItem mediaItemTemp = (MediaItem) dataOperate.get(index).clone();
        if (preTreatment == null || ConstantMediaSize.themeType.getThemePreTreatmentClass() != preTreatment.getClass()) {
            preTreatment = ThemeHelper.createPreTreatment(ConstantMediaSize.themeType);
        }
        // 改变bitmapframe
        try {
            //是主题则按照主题的来创建转场
            if (themeType != null || themeType != ThemeEnum.NONE) {
                mediaItemTemp.setTransitionFilter(preTreatment.createTransition(dataOperate.size()));
            }
            //否则复制转场
            if (mediaItemTemp.getTransitionFilter() != null) {
                TransitionFilter transitionFilter = TransitionFactory.initFilters(mediaItemTemp.getTransitionFilter().getTransitionType());
                mediaItemTemp.setTransitionFilter(transitionFilter);
            }
            if (mediaItemTemp.getAfilter() != null) {
                mediaItemTemp.setAfilter(FilterHelper.initFilters(mediaItemTemp.getAfilter().getmFilterType(), GpuFilterFactory.getDownPathLocal(mediaItemTemp.getAfilter().getmFilterType())));
            }
            if (mediaItemTemp.getMediaMatrix() != null) {
                mediaItemTemp.setMediaMatrix((MediaMatrix) mediaItemTemp.getMediaMatrix().clone());
            }
            if (mediaItemTemp.getVideoCutInterval() != null) {
                mediaItemTemp.setVideoCutInterval((DurationInterval) mediaItemTemp.getVideoCutInterval().clone());
            }

            //主题控件
            mediaItemTemp.setMimapBitmaps(preTreatment.getMimapBitmaps(ratioType, dataOperate.size()));

            final int realIndex = preTreatment.getMipmapsCount() == 0 ? 0 : dataOperate.size() % preTreatment.getMipmapsCount();
            String[][] finalDMMBS = preTreatment.getFinalDynamicMimapBitmapSources(ratioType);
            if (finalDMMBS != null && realIndex < finalDMMBS.length) {
                preTrementGif();
                LogUtils.i(getClass().getSimpleName() + " dynamicMimaps operation", "dynamicMimaps synchronized");

                //从缓存中获取
                String[] sources = finalDMMBS[realIndex];
                if (sources.length > 0) {
                    List<List<GifDecoder.GifFrame>> gifs = new ArrayList<>(sources.length);
                    LogUtils.i(getClass().getSimpleName() + " dynamicMimaps operation", "ready to get gif from dynamicMimaps");
                    for (String gifPath : sources) {
                        gifs.add(new ArrayList<>(dynamicMimaps.get(gifPath)));
                    }
                    //设置gif
                    mediaItemTemp.setDynamicMitmaps(gifs);
                }
            }

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        mediaItemTemp.setEqualId(UUID.randomUUID().toString());
        //删除id以防止排序受影响
        if (mediaItemTemp instanceof PhotoMediaItem) {
            boolean unPassed = true;
            Random random = new Random();
            //id 唯一性检查
            while (unPassed) {
                mediaItemTemp.setId(-Math.abs(random.nextInt()));
                unPassed = dataOperate.contains(mediaItemTemp);
            }
        } else {
            LogUtils.e(TAG, "Copy Video is not supported!");
            mediaItemTemp.setId(0);
        }

        insertMediaItem(mediaItemTemp, position);
        if (mediaItemTemp.isVideo()) {
            videoCount++;
            LogUtils.v("MediaDataRepository", "videoCount++: " + videoCount);
        } else {
            photoCount++;
            LogUtils.v("MediaDataRepository", "photoCount++: " + photoCount);
        }
        return mediaItemTemp;
    }

    /**
     * 插入mediaItem
     *
     * @param mediaItem 素材
     * @param index     下标
     */
    public void insertMediaItem(MediaItem mediaItem, int index) {
        dataOperate.add(mediaItem);
        if (index < dataOperate.size() - 1) {
            int prev = dataOperate.size() - 2;
            while (prev >= index) {
                swapOperate(prev, prev + 1);
                prev--;
            }
        }

    }

    /**
     * 更新裁剪后的时长
     *
     * @param operate
     */
    public void updateOriginTrimVideo(final MediaItem operate) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (ObjectUtils.isEmpty(operate.getTrimPath())) {
                return;
            }
            retriever.setDataSource(operate.getTrimPath());
            long tempDuration = (Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
            operate.setDuration(tempDuration);
            //时间计算
            operate.setTempDuration(tempDuration);
            retriever.release();
        } catch (Exception e) {
            e.printStackTrace();
            retriever.release();
        }
    }

    /**
     * 更新反序后的信息
     *
     * @param operate
     */
    public void updateVideoReverse(final MediaItem operate) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (ObjectUtils.isEmpty(((VideoMediaItem) operate).getReversePath())) {
                return;
            }
            retriever.setDataSource(((VideoMediaItem) operate).getReversePath());
            long tempDuration = (Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
            operate.setTempDuration(tempDuration);
            operate.setDuration(tempDuration);
            String value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            if (value != null) {
                operate.setWidth(Integer.parseInt(value));
            }
            value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            if (value != null) {
                operate.setHeight(Integer.parseInt(value));
            }
            value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
            if (value != null) {
                operate.setRotation(Integer.parseInt(value));
            }
            retriever.release();
            //清楚缩略图
            ((VideoMediaItem) operate).getThumbnails().clear();
        } catch (Exception e) {
            e.printStackTrace();
            retriever.release();
        }
    }

    /**
     * 数据存储到本地,综合看，用green是失败的策略，
     * 优势比如上用文件形式xml存储json数据结构
     */
    public synchronized void save2Local() {
    }


    public void onDestroyCurrentThread() {
        if (dataOperate != null) {
            dataOperate.clear();
        }
        if (doodleList != null) {
            doodleList.clear();
        }
        if (audioList != null) {
            audioList.clear();
        }
        if (recordList != null) {
            recordList.clear();
        }
        mScreenShotBitmap = null;
        isLocal = false;
        isLocalMultiEdit = false;
        hasEdit = false;
        ConstantMediaSize.themeType = ThemeEnum.NONE;
        ConstantMediaSize.themeConstantype = ThemeConstant.HOT;
        //重置默认比例
        ratioType = RatioType.NONE;
        ConstantMediaSize.particles = GlobalParticles.NONE;
        innerBorder = InnerBorder.Companion.getNONE();
        currentTransitionSeries = transitionSeries = TransitionSeries.NONE;
        preTreatment = null;
        zoomScale = 1f;
        videoCount = 0;
        photoCount = 0;
        if (mimaps != null) {
            mimaps.clear();
        }
        if (dynamicMimaps != null) {
            dynamicMimaps.clear();
        }
        if (widgetMimaps != null) {
            widgetMimaps.clear();
        }
    }

    public void onDestory() {
        onDestroyCurrentThread();
    }


    /**
     * 清除数据
     */
    public void clearDraft() {
//        FileUtil.deleteFile(PathUtil.getVideoTrimPath());
//        PathUtil.getVideoTrimPath();
//        deleteProject(currentProject);
    }


    public ArrayList<MediaItem> getDataOperateCopy() {
        ArrayList<MediaItem> list = new ArrayList<>();
        for (int i = 0; i < dataOperate.size(); i++) {
            MediaItem mediaItem = (MediaItem) dataOperate.get(i).clone();
            list.add(mediaItem);
        }
        return list;
    }

    public List<MediaItem> getDataSaveCopy() {
        CopyOnWriteArrayList<MediaItem> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < dataOperate.size(); i++) {
            MediaItem mediaItem = (MediaItem) dataOperate.get(i).clone();
            list.add(mediaItem);
        }
        return list;
    }

    public List<MediaItem> getDataOperate() {
        return dataOperate;
    }

    private MediaDataRepository() {
        dataOperate = new ArrayList<>();
        doodleList = new ArrayList<>();
        audioList = new ArrayList<>();
        recordList = new ArrayList<>();
        ratioType = RatioType.getRatioType(SpUtil.INSTANCE.getInt(SpUtil.EDIT_RATIO_SELECT, RatioType.NONE.getKey()));
    }


    static class SingleTonHoler {
        private static MediaDataRepository INSTANCE = new MediaDataRepository();
    }

    public static MediaDataRepository getInstance() {
        return SingleTonHoler.INSTANCE;
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    public String getProjectID() {
        if (currentProject != null) {
            return currentProject.getProjectId();
        } else {
            establishProject();
            return currentProject.getProjectId();
        }
    }

    /**
     * 进入选定工作台或者创建新的工作台
     */
    public void establishProject() {
        isLocal = false;
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

    public Bitmap getmScreenShotBitmap() {
        return mScreenShotBitmap;
    }

    public void setmScreenShotBitmap(Bitmap mScreenShotBitmap) {
        this.mScreenShotBitmap = mScreenShotBitmap;
    }

    public List<DoodleItem> getAllDoodle() {
        return doodleList;
    }

    /**
     * 根据类型过滤数据
     *
     * @param waterMarkType
     * @return
     */
    public List<DoodleItem> getDoodleByType(WaterMarkType waterMarkType) {
        List<DoodleItem> doodleTypeList = new ArrayList<>();
        for (DoodleItem d : doodleList) {
            if (d.getWaterMarkType() == waterMarkType) {
                doodleTypeList.add(d);
            }
        }
        return doodleTypeList;
    }


    public List<DoodleItem> getDoodleTypeList() {
        List<DoodleItem> doodleTypeList = new ArrayList<>();
        for (DoodleItem d : doodleList) {
            if (d.getWaterMarkType() == WaterMarkType.DOODLE) {
                doodleTypeList.add(d);
            }
        }
        return doodleTypeList;
    }


    public List<DoodleItem> getDoodleCharacter() {
        List<DoodleItem> doodleCharaterList = new ArrayList<>();
        for (DoodleItem d : doodleList) {
            if (d.getWaterMarkType() == WaterMarkType.CHARACTER) {
                doodleCharaterList.add(d);
            }
        }
        return doodleCharaterList;
    }

    public List<DoodleItem> getDoodleSticker() {
        List<DoodleItem> doodlePictureList = new ArrayList<>();
        for (DoodleItem d : doodleList) {
            if (d.getWaterMarkType() == WaterMarkType.PICTURE) {
                doodlePictureList.add(d);
            }
        }
        return doodlePictureList;
    }


    /**
     * 添加doodle
     *
     * @param doodleItem
     */
    public void addDoodleItem(DoodleItem doodleItem) {
        doodleItem.setProjectId(getProjectID());
        doodleList.add(doodleItem);
    }

    /**
     * 添加单个音频，该音频覆盖整个播放周期
     *
     * @param audioMediaItem
     */
    public void setSingleAudio(AudioMediaItem audioMediaItem) {
        // 检查是否有远程服务合成任务
        audioList.clear();
        audioList.add(audioMediaItem);
    }

    /**
     * 清除单音频
     */
    public void removeSingleAudio() {
        if (audioList.size() > 0) {
            if (!ObjectUtils.isEmpty(audioList.get(0).getExtractTaskId())) {
                FfmpegBackgroundHelper.getInstance().removeTask(audioList.get(0).getExtractTaskId());
            }
            audioList.remove(0);
        }
    }

    /**
     * 添加多片段音频
     */
    public void setMultiAudio(List<AudioMediaItem> list) {
        audioList.clear();
        audioList.addAll(list);
    }

    public boolean hasAudio() {
        return audioList.size() > 0;
    }

    public List<DoodleItem> getDoodleList() {
        return doodleList;
    }

    public List<AudioMediaItem> getAudioList() {
        if (!ObjectUtils.isEmpty(audioList) && audioList.get(0) != themeAudio) {
            return audioList;
        }
        if (themeAudio != null) {
            List<AudioMediaItem> list = new ArrayList<>();
            list.add(themeAudio);
            return list;
        }
        return audioList;
    }


    public ArrayList<AudioMediaItem> getExtAudioList() {
        return audioList;
    }

    public List<AudioMediaItem> getAudioListCopy() {
        List<AudioMediaItem> audioMediaItems = new ArrayList<>();
        if (ObjectUtils.isEmpty(audioList)) {
            if (themeAudio != null) {
                audioMediaItems.add(themeAudio);
            }
            return audioMediaItems;
        }
        for (int i = 0; i < audioList.size(); i++) {
            if (audioList.get(i) != null) {
                AudioMediaItem audioMediaItem = (AudioMediaItem) audioList.get(i).clone();
                if (audioMediaItem.getDurationInterval() != null) {
                    audioMediaItem.setDurationInterval((DurationInterval) audioMediaItem.getDurationInterval().clone());
                }
                audioMediaItems.add(audioMediaItem);
            }
        }
        return audioMediaItems;
    }

    /**
     * 音频类型转化+音频重整和合成
     *
     * @param list
     * @return
     */
    public List<AudioMediaItem> audioExchange(List<CutMusicItem> list) {
        List<AudioMediaItem> audioMediaItems = new ArrayList<>();
        if (list == null) {
            return null;
        }
        AudioMediaItem audioMediaItem;
        for (CutMusicItem cutMusicItem : list) {
            audioMediaItem = new AudioMediaItem();
            audioMediaItem.setProjectId(getProjectID());
            audioMediaItem.setDuration(cutMusicItem.getDuration());
            audioMediaItem.setOriginDuration(cutMusicItem.getOriginDuration());
            audioMediaItem.setTitle(cutMusicItem.getTitle());
            audioMediaItem.setVolume(cutMusicItem.getVolume());
            audioMediaItem.setOriginPath(cutMusicItem.getOriginPath());
            audioMediaItem.setSize(cutMusicItem.getSize());
            audioMediaItem.setPath(cutMusicItem.getPath());
            audioMediaItem.setDurationInterval(cutMusicItem.getDurationInterval());
            audioMediaItem.setCutStart(cutMusicItem.getCutStart());
            audioMediaItem.setCutEnd(cutMusicItem.getCutEnd());
            audioMediaItems.add(audioMediaItem);
        }
        return audioMediaItems;
    }


    /**
     * 音频对象转化，并且后台再重处理裁剪合并音频文件
     *
     * @param list
     * @return
     */
    public List<AudioMediaItem> audioExchangeAndDealAudio(List<CutMusicItem> list, boolean hasRemoveOrigin) {
        // 对音频文件进行排序
        List<AudioMediaItem> audioMediaItems = audioExchange(list);
        // dealAudioDuration(audioMediaItems);
        if (!hasRemoveOrigin) {
//            chanVideoOriginAudioPeriod(audioList);
        }
        setMultiAudio(audioMediaItems);
        return audioMediaItems;
    }


    /**
     * 最小单位为5个
     *
     * @param paths
     * @param commandList
     */
    private void splitMultiAudioMurge(String[] paths, List<String[]> commandList) {
        if (paths.length < 5) {
            return;
        }
        int arrayCount = paths.length / 5;
        int temp = paths.length % 5;
        int num = temp == 0 ? arrayCount : arrayCount + 1;
        String[] newPaths = new String[num];
        for (int i = 0; i < num; i++) {

        }
    }

    public boolean isLocal() {
        return isLocal;
    }

    public boolean isLocalMultiEdit() {
        return isLocalMultiEdit;
    }

    public void setLocalMultiEdit(boolean localMultiEdit) {
        isLocalMultiEdit = localMultiEdit;
    }

    /**
     * @return
     */
    public RatioType calcPreviewRatio() {
        float scale;
        float temp = 0;
        for (int i = 0; i < dataOperate.size(); i++) {
            scale = (float) dataOperate.get(i).getWidth() / (float) dataOperate.get(i).getHeight();
            if (dataOperate.get(i).getRotation() == 90 || dataOperate.get(i).getRotation() == 270) {
                scale = (float) dataOperate.get(i).getHeight() / (float) dataOperate.get(i).getWidth();
            }
            if (scale > 1.33) {
                return RatioType._16_9;
            }
            if (scale > temp) {
                temp = scale;
            }
        }
        if (temp > 1 && temp <= 1.33) {
            return RatioType._4_3;
        } else if (temp == 1) {
            return RatioType._1_1;
        } else if (temp < 1 && temp >= 0.75) {
            return RatioType._3_4;
        } else {
            return RatioType._9_16;
        }
    }

    public RatioType calcPreviewRatio(MediaItem mediaItem) {
        float scale;
        float temp = 0;
        scale = (float) mediaItem.getWidth() / (float) mediaItem.getHeight();
        if (mediaItem.getRotation() == 90 || mediaItem.getRotation() == 270) {
            scale = (float) mediaItem.getHeight() / (float) mediaItem.getWidth();
        }
        if (scale > 1.33) {
            return RatioType._16_9;
        }
        if (scale > temp) {
            temp = scale;
        }
        if (temp > 1 && temp <= 1.33) {
            return RatioType._4_3;
        } else if (temp == 1) {
            return RatioType._1_1;
        } else if (temp < 1 && temp >= 0.75) {
            return RatioType._3_4;
        } else {
            return RatioType._9_16;
        }
    }

    public RatioType calcPreviewRatio(float width, float height, int rotation) {
        float scale;
        float temp = 0;
        scale = width / height;
        if (rotation == 90 || rotation == 270) {
            scale = (float) height / (float) width;
        }
        if (scale > 1.33) {
            return RatioType._16_9;
        }
        if (scale > temp) {
            temp = scale;
        }
        if (temp > 1 && temp <= 1.33) {
            return RatioType._4_3;
        } else if (temp == 1) {
            return RatioType._1_1;
        } else if (temp < 1 && temp >= 0.75) {
            return RatioType._3_4;
        } else {
            return RatioType._9_16;
        }
    }


    public RatioType calcRatioType(MediaItem mediaItem) {
        float scale;
        scale = (float) mediaItem.getWidth() / (float) mediaItem.getHeight();
        if (mediaItem.getRotation() == 90 || mediaItem.getRotation() == 270) {
            scale = (float) mediaItem.getHeight() / (float) mediaItem.getWidth();
        }
        if (scale > 1.33) {
            return RatioType._16_9;
        }
        if (scale > 1 && scale <= 1.33) {
            return RatioType._4_3;
        } else if (scale == 1) {
            return RatioType._1_1;
        } else if (scale < 1 && scale >= 0.75) {
            return RatioType._3_4;
        } else {
            return RatioType._9_16;
        }
    }

    /**
     * 检测是否有视频
     *
     * @return
     */
    public boolean checkExistVideo() {
        for (MediaItem mediaItem : dataOperate) {
            if (mediaItem instanceof VideoMediaItem) {
                return true;
            }
        }
        return false;
    }

    public boolean checkExistAudio() {
        return !ObjectUtils.isEmpty(audioList);
    }

    /**
     * 是否为主题
     *
     * @return
     */
    public boolean checkIsTheme() {
        return themeType != ThemeEnum.NONE;
    }

    /**
     * 还原默认音乐
     */
    public void reSetBackMusic() {
        if (!ObjectUtils.isEmpty(audioListBak)) {
            audioList = audioListBak;
        }
    }

    /**
     * 检测视频全静音
     *
     * @return
     */
    public boolean checkNotSilenceVoice() {
        for (MediaItem mediaItem : dataOperate) {
            if (mediaItem instanceof VideoMediaItem) {
                if (((VideoMediaItem) mediaItem).getVolume() > 0) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 因为主控台实时在渲染，把主bitmap回收经常会出问题
     * 所以bitmap的手动回收放在退出图片选择页面，其他的让系统自动去回收
     *
     * @param mediaItem
     */
    public void clearMediaItem(MediaItem mediaItem) {
        if (mediaItem == null) {
            return;
        }
        long start = System.currentTimeMillis();
        try {
            //不是默认图才回收
            if (mediaItem.getBitmap() != null && !mediaItem.getBitmap().isRecycled() && !PhotoMediaItem.assertDefaultBitmap(mediaItem.getBitmap())) {
                mediaItem.getBitmap().recycle();
            }
            if (mediaItem.getTempBitmap() != null && !mediaItem.getTempBitmap().isRecycled()) {
                mediaItem.getTempBitmap().recycle();
            }
            //清理视频缓存
            if (mediaItem instanceof VideoMediaItem) {
                if (!ObjectUtils.isEmpty(((VideoMediaItem) mediaItem).getThumbnails())) {
                    for (Bitmap bitmap : ((VideoMediaItem) mediaItem).getThumbnails().values()) {
                        if (bitmap != null) {
                            bitmap.recycle();
                        }
                    }
                    ((VideoMediaItem) mediaItem).getThumbnails().clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.v("MediaDataRepository", "clearMediaItem:" + (System.currentTimeMillis() - start));
    }

    /**
     * 清理数据
     *
     * @param list
     */
    public void clearMediaItem(List<MediaItem> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                clearMediaItem(list.get(i));
            }
        }
    }


    /**
     * 主题修改,重新预处理数据
     * 检测当前主题是否为非主题，如果为非主题切换为主题，则进行时间轴的备份存储
     *
     * @return
     */
    public List<MediaItem> changeTheme(SlideshowEntity slideshowEntity) {
        if (ConstantMediaSize.themeType == ThemeEnum.NONE) {
            bakNoThemeDuration();
        }
        setCurrentSlideEntity(slideshowEntity);
        preTreatment = ThemeFactory.createPreTreatment(slideshowEntity.getThemeEnum());
        if (!ObjectUtils.isEmpty(mimaps)) {
            for (int i = 0; i < mimaps.size(); i++) {
                List<Bitmap> bitmaps = mimaps.get(i);
                if (!ObjectUtils.isEmpty(bitmaps)) {
                    for (int j = 0; j < bitmaps.size(); j++) {
                        if (bitmaps.get(j) != null && !bitmaps.get(j).isRecycled()) {
                            bitmaps.get(j).recycle();
                        }
                    }
                }
            }
            mimaps.clear();
        }
        //重置gif缓存
        if (!ObjectUtils.isEmpty(dynamicMimaps)) {
            LogUtils.i(getClass().getSimpleName() + " dynamicMimaps operation", "ready to clear dynamicMimaps");
            for (List<GifDecoder.GifFrame> gif : dynamicMimaps.values()) {
                for (GifDecoder.GifFrame frame : gif) {
                    //回收bitmap
                    frame.image.recycle();
                }
                gif.clear();
            }
            dynamicMimaps.clear();
            LogUtils.i(getClass().getSimpleName() + " dynamicMimaps operation", "dynamicMimaps cleared");
        }
        if (themePags != null) {
            themePags.clear();
            themePags = null;
        }

        //重置时间轴控件缓存
        if (!ObjectUtils.isEmpty(widgetMimaps)) {
            for (int j = 0; j < widgetMimaps.size(); j++) {
                if (widgetMimaps.get(j) != null && !widgetMimaps.get(j).isRecycled()) {
                    widgetMimaps.get(j).recycle();
                }
            }
            widgetMimaps.clear();
        }
        preTrementGif();
        for (int i = 0; i < dataOperate.size(); i++) {
            MediaItem dustItem = dataOperate.get(i);
            preTreatmentMediaItem(i, dustItem, true, true, null);
        }
        //在EditorActivity页面中切换主题时加载widgetMimaps
        if (preTreatment instanceof BaseTimePreTreatment && ObjectUtils.isEmpty(widgetMimaps)) {
            List<Bitmap> mimap = ((BaseTimePreTreatment) preTreatment).createMimapBitmaps();
            //修复create产生null后抛出npe
            if (mimap != null) {
                widgetMimaps = new ArrayList<>(mimap);
            }
        }
        if (!ObjectUtils.isEmpty(themeAudio)) {
            themeAudio.setDurationInterval(null);
        }
        setCurrentSlideEntity(slideshowEntity);
        return dataOperate;
    }

    /**
     * 备份无主题时的时间轴
     */
    private void bakNoThemeDuration() {
        for (MediaItem mediaItem : dataOperate) {
            mediaItem.synNothemeDuration();
        }
    }

    /**
     * 移除主题
     */
    public List<MediaItem> clearThemeNone(boolean random) {
        LogUtils.d("changeTheme", "execute");
        if (random) {
            currentProject.setTranGroup(TransitionRepository.INSTANCE.randomTranGroup());
        } else {
            currentProject.setTranGroup(TransitionRepository.TransiGroup.GROUP_NONE);
        }
        ConstantMediaSize.themeType = ThemeEnum.NONE;
        ConstantMediaSize.themeConstantype = ThemeConstant.HOT;
        if (!ObjectUtils.isEmpty(audioListBak)) {
            audioList = audioListBak;
        }
        if (ObjectUtils.isEmpty(audioList) && random) {
            setDefaultMusic();
        }
        themeAudio = null;
        preTreatment = ThemeHelper.createPreTreatment(ThemeEnum.NONE);
        //这两块LogUtils.d中间进度有点长，看看什么情况
        LogUtils.d("changeTheme", "step1");
        for (int i = 0; i < dataOperate.size(); i++) {
            MediaItem dustItem = dataOperate.get(i);
            dustItem.setTransitionFilter(null);
            preTreatmentMediaItem(i, dustItem, false, true, null);
            dustItem.resumeNothemeDuration();
        }
        LogUtils.d("changeTheme", "preTreatmentMediaItem");
        if (!ObjectUtils.isEmpty(mimaps)) {
            for (int i = 0; i < mimaps.size(); i++) {
                List<Bitmap> bitmaps = mimaps.get(i);
                if (!ObjectUtils.isEmpty(bitmaps)) {
                    for (int j = 0; j < bitmaps.size(); j++) {
                        if (bitmaps.get(j) != null && !bitmaps.get(j).isRecycled()) {
                            bitmaps.get(j).recycle();
                        }
                    }
                }
            }
            mimaps.clear();
        }
        //重置gif缓存
        if (!ObjectUtils.isEmpty(dynamicMimaps)) {
            LogUtils.i(getClass().getSimpleName() + " dynamicMimaps operation", "ready to clear dynamicMimaps");
            for (List<GifDecoder.GifFrame> gif : dynamicMimaps.values()) {
                for (GifDecoder.GifFrame frame : gif) {
                    //回收bitmap
                    frame.image.recycle();
                }
                gif.clear();
            }
            dynamicMimaps.clear();
            LogUtils.i(getClass().getSimpleName() + " dynamicMimaps operation", "dynamicMimaps cleared");
        }

        //重置时间轴控件缓存
        if (!ObjectUtils.isEmpty(widgetMimaps)) {
            for (int j = 0; j < widgetMimaps.size(); j++) {
                if (widgetMimaps.get(j) != null && !widgetMimaps.get(j).isRecycled()) {
                    widgetMimaps.get(j).recycle();
                }
            }
            widgetMimaps.clear();
        }

        if (!ObjectUtils.isEmpty(themeAudio)) {
            themeAudio.setDurationInterval(null);
        }
//        if (currentProject != null) {
//            currentProject.setThemeEnum(ThemeEnum.NONE);
//            currentProject.setThemeMusicPath("");
//            currentProject.setThemeZipPath("");
//        }
        return dataOperate;

    }


    /**
     * 针对主题音乐，如果播放边界发生变化，则进行重置
     */
    public void resetMusic() {
        if (!ObjectUtils.isEmpty(themeAudio)) {
            themeAudio.setDurationInterval(null);
        }
        if (!ObjectUtils.isEmpty(defaultAudio)) {
            defaultAudio.setDurationInterval(null);
        }
    }

    public boolean isHasEdit() {
        return hasEdit;
    }

    public void setHasEdit(boolean hasEdit) {
        this.hasEdit = hasEdit;
    }

    /**
     * 加载主题全部的gif
     *
     * @param preTreatment 主题预处理类
     */
    private void loadGifs(IPretreatment preTreatment) {
        LogUtils.v("MediaDataRepository", "..............................................");
        if (preTreatment == null) {
            return;
        }
        //获取地址源loadGifs
        String[][] sources = preTreatment.getFinalDynamicMimapBitmapSources(ratioType);
        if (sources == null) {
            return;
        }
        if (dynamicMimaps == null) {
            dynamicMimaps = new ConcurrentHashMap<>();
        } else {
            dynamicMimaps.clear();
        }
        if (ObjectUtils.isEmpty(sources)) {
            return;
        }

        //遍历地址并加载
        for (String[] source : sources) {
            for (String gifPath : source) {
                if (!dynamicMimaps.containsKey(gifPath)) {
                    try {
                        List<GifDecoder.GifFrame> frames = loadGif(gifPath);
                        dynamicMimaps.put(gifPath, frames);
                    } catch (Exception e) {
                        e.printStackTrace();
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
    private List<GifDecoder.GifFrame> loadGif(String gifPath) throws RuntimeException {
        List<GifDecoder.GifFrame> list = new ArrayList();
        if (gifPath.endsWith(LocalPreTreatmentTemplate.END_SUFFIX)) {
            int resourceId = Integer.parseInt(gifPath.replace(LocalPreTreatmentTemplate.END_SUFFIX, ""));
            list = GlideGifDecoder.Companion.getGif(MyApplication.instance, resourceId).getFrames();
        } else {
            list = GlideGifDecoder.Companion.getGif(MyApplication.instance, ConstantMediaSize.themePath + gifPath + ConstantMediaSize.SUFFIX).getFrames();
        }
        if (!ObjectUtils.isEmpty(list)) {
            return list;
        }
        GifDecoder mGifDecoder = new GifDecoder();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(ConstantMediaSize.themePath + gifPath);
            //这里面的方法会对inpustream进行close
            int status = mGifDecoder.read(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<GifDecoder.GifFrame> frames = mGifDecoder.getFrames();
        //填充delay时间
        for (int i = 0; i < frames.size(); i++) {
            if (frames.get(i).delay == 0) {
                frames.get(i).delay = frames.get(i - 1).delay;
            }
        }
        return frames;
    }

    public MediaEntity getTempMediaEntity() {
        return tempMediaEntity;
    }

    public void setTempMediaEntity(MediaEntity tempMediaEntity) {
        this.tempMediaEntity = tempMediaEntity;
    }

    /**
     * 预处理渲染信息，包含自定义背景等
     * 执行线程任务{@link ThreadPoolManager}
     */
    public void preTrementInfo() {
        if (mSpBgInfo.getBackgrondType() == BackgroundType.IMAGE) {
            Bitmap bitmap = BitmapUtil.getSmallBitmapByWH(mSpBgInfo.getCustomPath(), 0, ConstantMediaSize.localBitmapWidth, ConstantMediaSize.localBitmapHeight);
            mSpBgInfo.setBlurBitmap(BitmapUtil.blurBitmap(MediaSdk.getInstance().getContext(), mSpBgInfo.getBlurLevel() / 4, bitmap));
        }
    }

    /**
     * 获取当前编辑的背景参数
     *
     * @return
     */
    public SpBgInfo getCurrentBackgroundInfo() {
        return mSpBgInfo;
    }

    /**
     * 获取当前编辑的背景参数
     *
     * @return
     */
    public BGInfo getBackroundInfoCopy() {
        BGInfo bgInfo = new BGInfo();
        bgInfo.setBackroundType(mSpBgInfo.getBackgrondType());
        bgInfo.setBlurLevel(mSpBgInfo.getBlurLevel());
        bgInfo.setCustomBitmap(mSpBgInfo.getBlurBitmap());
        String hexColor = "#FF000000";
        if (mSpBgInfo.getColorValue() != 0) {
            hexColor = "#" + Integer.toHexString(mSpBgInfo.getColorValue());
        }
        if (hexColor.length() == 7) {
            hexColor = hexColor.replace("#", "#FF");
        }
        int[] rgba = ColorUtil.hex2Rgb(hexColor);
        bgInfo.setRgbs(rgba);
        bgInfo.setZoomScale(mSpBgInfo.getZoomScale());
        return bgInfo;
    }


    /**
     * 存储裁剪数据
     *
     * @param dataOperate
     */
    public void saveCropInfo(List<MediaItem> dataOperate) {
        Map<Integer, Rect> cropInfo = new HashMap<>();
        for (int i = 0; i < dataOperate.size(); i++) {
            if (dataOperate.get(i) instanceof PhotoMediaItem) {
                PhotoMediaItem photoMediaItem = (PhotoMediaItem) dataOperate.get(i);
                if (photoMediaItem.getCropRect() != null) {
                    cropInfo.put(i, photoMediaItem.getCropRect());
                }
            }
        }
        SpUtil.INSTANCE.setObject(SpUtil.SAVE_LOCAL_CROP_RECT + getProjectID(), cropInfo.isEmpty() ? null : cropInfo);
    }

    /**
     * 加载裁剪数据
     *
     * @param dataOperate
     */
    public void loadCropInfo(List<MediaItem> dataOperate) {
        Map<Integer, Rect> cropInfo = (Map<Integer, Rect>) SpUtil.INSTANCE.getObject(SpUtil.SAVE_LOCAL_CROP_RECT + getProjectID(), CORP_RECT_MAP_TYPE);
        if (cropInfo == null || cropInfo.size() > dataOperate.size()) {
            return;
        }
        for (Map.Entry<Integer, Rect> entry : cropInfo.entrySet()) {
            ((PhotoMediaItem) dataOperate.get(entry.getKey())).setCropRect(entry.getValue());
        }
    }

    public List<AudioMediaItem> getRecordList() {
        return recordList;
    }

    public List<AudioMediaItem> getRecordListCopy() {
        List<AudioMediaItem> audioMediaItems = new ArrayList<>();
        if (ObjectUtils.isEmpty(recordList)) {
            return audioMediaItems;
        }
        for (int i = 0; i < recordList.size(); i++) {
            if (recordList.get(i) != null) {
                AudioMediaItem audioMediaItem = (AudioMediaItem) recordList.get(i).clone();
                if (audioMediaItem.getDurationInterval() != null) {
                    audioMediaItem.setDurationInterval((DurationInterval) audioMediaItem.getDurationInterval().clone());
                }
                audioMediaItems.add(audioMediaItem);
            }
        }
        return audioMediaItems;
    }

    public void setRecordList(List<AudioMediaItem> recordList) {
        this.recordList = recordList;
        for (AudioMediaItem audioMediaItem : recordList) {
            audioMediaItem.setRecord(true);
        }
    }

    /**
     * 设置随机转场
     */
    public boolean randomAllTransition() {
        currentProject.setTranGroup(TransitionRepository.INSTANCE.randomTranGroup());
        boolean changeTime = false;
        for (int i = 0; i < dataOperate.size(); i++) {
            dataOperate.get(i).setTransitionFilter(TransitionRepository.INSTANCE.randomTransitionFilter(currentProject.getTranGroup(), i));
            if (dataOperate.get(i).getFinalDuration() < ConstantMediaSize.TRANSITION_DURATION) {
                dataOperate.get(i).setDuration(ConstantMediaSize.TRANSITION_DURATION);
                changeTime = true;
            }
        }
        if (changeTime) {
            resetMusic();
        }
        return changeTime;
    }

    /**
     * 移除随机转场
     */
    public void removeAllTransition() {
        currentProject.setTranGroup(TransitionRepository.TransiGroup.GROUP_NONE);
        for (int i = 0; i < dataOperate.size(); i++) {
            dataOperate.get(i).setTransitionFilter(TransitionFactory.initFilters(TransitionType.NONE));
        }
    }


    /**
     * 指定id的mediaItem位置
     * 不支持MusicMediaItem
     */
    public int indexOfMediaEntity(MediaEntity mediaEntity) {
        if (mediaEntity.type == MediaEntity.TYPE_IMAGE) {
            MediaItem mediaItem = new MediaItem(mediaEntity.id, MediaType.PHOTO);
            return dataOperate.indexOf(mediaItem);
        } else if (mediaEntity.type == MediaEntity.TYPE_VIDEO) {
            int i = dataOperate.size() - 1;
            for (; i > -1; i--) {
                if (dataOperate.get(i).getPath().equals(mediaEntity.getPath())) {
                    break;
                }
            }
            return i;
        } else {
            return -1;
        }
    }

    /**
     * 寻找mediaEntity被添加后可能出现的位置
     *
     * @param mediaEntity
     * @param originSize  起始寻找位置
     */
    public int indexOfMediaEntity(int originSize, MediaEntity mediaEntity) {
        int index = originSize;
        while (index < dataOperate.size()) {
            MediaItem mediaItem = dataOperate.get(index);
            if (mediaEntity.id == mediaItem.getId()) {
                return index;
            }
            if (mediaEntity.path.equals(mediaItem.getPath())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * 图片处理
     */
    public void addMediaItemPhotoWithOutPretreatment(MediaEntity originItem, AddMediaCallback addMediaCallback) {
        PhotoMediaItem dustItem = new PhotoMediaItem();
        dustItem.setPath(originItem.path);
        if (!ObjectUtils.isEmpty(originItem.size)) {
            dustItem.setSize(Long.parseLong(originItem.size));
        }
        dustItem.setWidth(originItem.width);
        dustItem.setHeight(originItem.height);
        dustItem.setName(originItem.title);
        dustItem.setId(originItem.id);
        //这个一定需要在dataOperate.add(dustItem)之前
        final int index = dataOperate.size();
        dataOperate.add(dustItem);
        dustItem.setExtra(originItem);
        if (addMediaCallback != null) {
            addMediaCallback.resultData(dustItem);
        }
    }

    /**
     * 指定id的mediaItem位置
     */
    public int indexOfMediaItemId(int id) {
        return dataOperate.indexOf(new MediaItem(id, MediaType.PHOTO));
    }

    /**
     * kotlin获取audioList时调用{@link #getAudioList}
     * 但该方法不符合逻辑需求，故创建此方法代替kotlin中获取aduioList
     *
     * @return
     */
    public List<AudioMediaItem> getAudioListKotlin() {
        return audioList;
    }

    /**
     * 是否为baseTimePreTreatment主题
     *
     * @return
     */
    public boolean checkIsBeatTheme() {
        if (preTreatment == null) {
            return false;
        }
        if (preTreatment instanceof BaseTimePreTreatment) {
            return true;
        }
        return false;
    }


    /**
     * 替换mediaItem中的转场
     *
     * @param currentTransitionSeries
     */
    public void setCurrentTransitionSeries(TransitionSeries currentTransitionSeries) {
        this.currentTransitionSeries = currentTransitionSeries;
        ConstantMediaSize.transitionSeries = currentTransitionSeries;
        //i 从1 开始， 第一个没转场
        for (int i = 1; i < dataOperate.size(); i++) {
            MediaItem item = dataOperate.get(i);
            //取余循环
            item.setTransitionFilter(TransitionFactory.initFilters(currentTransitionSeries.getArray()[(i - 1) % currentTransitionSeries.getArray().length]));
            // 增加时长
            //时长计算用TempDuration算得，所以设置TempDuration
            if (item.getFinalDuration() < ConstantMediaSize.TRANSITION_DURATION && item.getTransitionFilter().getTransitionType() != TransitionType.NONE) {
                item.setDuration(ConstantMediaSize.TRANSITION_DURATION);
            }
        }
    }

    /**
     * 当音乐只有一首时更新单个音乐的播放范围（总长度）
     *
     * @param totalTime 总长度
     */
    public void updateSingleAudioDurationInterval(int totalTime) {
        if (audioList.size() == 1) {
            audioList.get(0).setDurationInterval(new DurationInterval(0, totalTime));
        }
    }

    public int getVideoCount() {
        return videoCount;
    }

    public int getPhotoCount() {
        return photoCount;
    }

    public void clear() {
        dataOperate.clear();
        photoCount = 0;
        videoCount = 0;
        LogUtils.v("MediaDataRepository", "photoCount: 0");
        LogUtils.v("MediaDataRepository", "videoCount: 0");
    }

    public float getZoomScale() {
        return zoomScale;
    }

    public void setZoomScale(float zoomScale) {
        this.zoomScale = zoomScale;
    }

    public ConcurrentHashMap<Integer, List<Bitmap>> getMimaps() {
        return mimaps;
    }
}
