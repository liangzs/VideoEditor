package com.qiusuo.videoeditor.common.data;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.ConstantPath;
import com.ijoysoft.mediasdk.common.global.ThreadPoolManager;
import com.ijoysoft.mediasdk.common.global.ThreadPoolMaxThread;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.FileUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.common.utils.TimeUtil;
import com.ijoysoft.mediasdk.module.entity.AudioMediaItem;
import com.ijoysoft.mediasdk.module.entity.DoodleItem;
import com.ijoysoft.mediasdk.module.entity.DurationInterval;
import com.ijoysoft.mediasdk.module.entity.MediaMatrix;
import com.ijoysoft.mediasdk.module.entity.MediaType;
import com.ijoysoft.mediasdk.module.entity.PhotoMediaItem;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.entity.VideoMediaItem;
import com.ijoysoft.mediasdk.module.entity.WaterMarkType;
import com.ijoysoft.mediasdk.module.ffmpeg.FFmpegCmd;
import com.ijoysoft.mediasdk.module.ffmpeg.SingleCmdListener;
import com.ijoysoft.mediasdk.module.ffmpeg.VideoEditManager;
import com.ijoysoft.mediasdk.module.mediacodec.FfmpegBackgroundHelper;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter;
import com.ijoysoft.videoeditor.Event.AppBus;
import com.ijoysoft.videoeditor.Event.SaveLocalEvent;
import com.ijoysoft.videoeditor.base.MyApplication;
import com.ijoysoft.videoeditor.entity.localRepository.AudioDurationData;
import com.ijoysoft.videoeditor.entity.localRepository.AudioDurationDataDao;
import com.ijoysoft.videoeditor.entity.localRepository.AudioMediaItemData;
import com.ijoysoft.videoeditor.entity.localRepository.AudioMediaItemDataDao;
import com.ijoysoft.videoeditor.entity.localRepository.DaoSession;
import com.ijoysoft.videoeditor.entity.localRepository.DoodleItemData;
import com.ijoysoft.videoeditor.entity.localRepository.DoodleItemDataDao;
import com.ijoysoft.videoeditor.entity.localRepository.DurationIntervalData;
import com.ijoysoft.videoeditor.entity.localRepository.DurationIntervalDataDao;
import com.ijoysoft.videoeditor.entity.localRepository.MediaItemData;
import com.ijoysoft.videoeditor.entity.localRepository.MediaItemDataDao;
import com.ijoysoft.videoeditor.entity.localRepository.MediaMatrixData;
import com.ijoysoft.videoeditor.entity.localRepository.MediaMatrixDataDao;
import com.ijoysoft.videoeditor.entity.localRepository.SqliteDataFetchHelper;
import com.ijoysoft.videoeditor.utils.BeanUtil;
import com.ijoysoft.videoeditor.utils.BytesBitmap;
import com.ijoysoft.videoeditor.utils.Dlog;
import com.ijoysoft.videoeditor.utils.FileUtil;
import com.ijoysoft.videoeditor.utils.PathUtil;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.THUMTAIL_HEIGHT;
import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.THUMTAIL_WIDTH;

/**
 * 数据存储
 * 包含外部数据同步到sdk列表数据
 * 后续工作台的数据都同步在这里
 */
public class MediaDataRepository {
    private static final String TAG = "MediaDataRepository";
    private ArrayList<MediaItem> dataOrigin;
    private ArrayList<com.ijoysoft.mediasdk.module.entity.MediaItem> dataOperate;
    private Project currentProject;
    private Bitmap mScreenShotBitmap;
    private List<DoodleItem> doodleList;
    private List<AudioMediaItem> audioList;
    private boolean isLocal;
    private boolean isLocalMultiEdit;// 本地draft多次进入编辑

    public ArrayList<MediaItem> getDataOrigin() {
        return dataOrigin;
    }

    /* 数据源 */
    public void setData(ArrayList<MediaItem> data) {
        this.dataOrigin = null;
        this.dataOrigin = data;
        // 关联上下帧、赋值项目编号
        linkPreviewFrame();
    }

    public void updataOrigin(ArrayList<MediaItem> datas) {
        if (dataOrigin == null) {
            dataOrigin = new ArrayList<>();
        }
        this.dataOrigin.addAll(datas);
        linkPreviewFrame();
    }

    public void clearData() {
        if (dataOrigin != null) {
            dataOrigin.clear();
        }
        if (dataOperate != null) {
            dataOperate.clear();
        }
        if (doodleList != null) {
            doodleList.clear();
        }
        if (audioList != null) {
            audioList.clear();
        }
        currentProject = null;
        mScreenShotBitmap = null;
    }

    /**
     * 覆盖原有数据
     *
     * @param list
     */
    public void overideData(ArrayList<com.ijoysoft.mediasdk.module.entity.MediaItem> list) {
        this.dataOperate = null;
        this.dataOperate = list;
    }

    /**
     * 更新上下帧bitmap
     */
    private void linkPreviewFrame() {
        com.ijoysoft.mediasdk.module.entity.MediaItem lastItem = null;
        com.ijoysoft.mediasdk.module.entity.MediaItem currentItem;
        for (int i = 0; i < dataOperate.size(); i++) {
            currentItem = dataOperate.get(i);
            currentItem.setProjectId(getProjectID());
            if (i > 0) {
                setPreviewFrame(currentItem, lastItem);
            }
            lastItem = currentItem;
        }
    }

    /**
     * 单项添加,点击
     *
     * @param mediaItem
     */
    public synchronized void addMediaItem(MediaItem mediaItem, boolean isExtract, boolean isTrim) { // 可能要加个锁，防止添加数据后，显示的操作数据与源数据不匹配
        com.ijoysoft.mediasdk.module.entity.MediaItem dustMedia = mediaExtrange(mediaItem, isExtract, isTrim);
        if (dataOperate.size() > 1) {
            setPreviewFrame(dustMedia, dataOperate.get(dataOperate.size() - 1));
        }
        dataOperate.add(dustMedia);
        Dlog.d("remove-position", "add-pos==" + dustMedia.getRotation());
//        if(dustMedia.getMediaType()==MediaType.VIDEO){
//            Log.i("test",FileUtils.getVideoAudioFormat(dustMedia.getPath()));
//        }
    }

    /**
     * 数据类型转化
     *这里如果是视频的话，就对视频进行前后帧的本地存储，方便后续使用
     * 这样在纹理加载的时候，要对目标的bitmap进行缩小
     * @param originItem
     * @return
     */

    private com.ijoysoft.mediasdk.module.entity.MediaItem mediaExtrange(MediaItem originItem, boolean isExtract,
            final boolean isTrim) {
        if (originItem.type == MediaItem.TYPE_IMAGE) {
            PhotoMediaItem dustItem = new PhotoMediaItem();
            dustItem.setRotation(BytesBitmap.getExifOrientation(originItem.path, originItem.rotation));
            dustItem.setProjectId(getProjectID());
            dustItem.setPath(originItem.path);
            dustItem.setSize(Long.valueOf(originItem.size));
            dustItem.setDuration(ConstantMediaSize.TIME_DURATION);
            dustItem.setTempDuration(ConstantMediaSize.TIME_DURATION);
            dustItem.setWidth(originItem.width);
            dustItem.setHeight(originItem.height);
            dustItem.setName(originItem.title);
            // 检测是否图片是否需要压缩
            dustItem.setFirstFramePath(dustItem.getPath());
            if (originItem.width > ConstantMediaSize.screenWidth || originItem.width > ConstantMediaSize.screenHeight) {
                dustItem.setFirstFramePath(PathUtil.createFrameImage(getProjectID()));
                FileUtil.saveBitmap(BitmapUtil.scaleRatioByPathByMatrix(dustItem.getPath(),
                        ConstantMediaSize.screenWidth, ConstantMediaSize.screenHeight), dustItem.getFirstFramePath());
            }

            return dustItem;
        } else if (originItem.type == MediaItem.TYPE_VIDEO) {
            final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            final VideoMediaItem dustItem = new VideoMediaItem();
            dustItem.setProjectId(getProjectID());
            dustItem.setPath(originItem.path);
            dustItem.setSize(Long.valueOf(originItem.size == null ? "0" : originItem.size));
            dustItem.setDuration(originItem.duration);
            dustItem.setTempDuration(originItem.duration);
            dustItem.setWidth(originItem.width);
            dustItem.setHeight(originItem.height);
            dustItem.setName(originItem.title);
            try {
                retriever.setDataSource(originItem.path);
            } catch (Exception e) {
                e.printStackTrace();
                dustItem.setWidth(originItem.width);
                dustItem.setHeight(originItem.height);
                if (FileUtils.checkExistAudio(dustItem.getPath()) && isExtract) {
                    String temppath = ConstantPath.createExtractVideo2Audio(getProjectID(), dustItem.getName());
                    dustItem.setExtracAudioPath(temppath + "_ijoysoft");
                    FfmpegBackgroundHelper.getInstance()
                            .extractAudioMp3(new String[] { dustItem.getPath(), temppath, temppath });
                }
                return dustItem;
            }

            if (originItem.width == 0 || originItem.height == 0) {
                if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH) != null) {
                    originItem.width =
                            Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                    dustItem.setWidth(Integer
                            .valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)));
                }
                if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT) != null) {
                    originItem.height = Integer
                            .valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                    dustItem.setHeight(Integer
                            .valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)));
                }
            } else {
                dustItem.setWidth(originItem.width);
                dustItem.setHeight(originItem.height);
            }
            if(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)!=null){
                int bitRate = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
                dustItem.setBitRate(bitRate);
            }
            String value=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION) == null ? "0"
                    : retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
            Log.i("test", "value:"+value+dustItem.getName() + ":" + dustItem.getRotation());
            dustItem.setRotation(Integer.valueOf(value));
            Log.i("test", dustItem.getName() + ":" + dustItem.getRotation());
            dustItem.setFirstFramePath(PathUtil.createFrameImage(getProjectID()));
            dustItem.setVideolastFramePath(PathUtil.createFrameImage(getProjectID()));
            // 做音频抽取动作,这里分是进入主控制台还是进入裁剪子页面
            if (FileUtils.checkExistAudio(dustItem.getPath()) && isExtract) {
                String temppath = ConstantPath.createExtractVideo2Audio(getProjectID(), dustItem.getName());
                dustItem.setExtracAudioPath(temppath + "_ijoysoft");
                FfmpegBackgroundHelper.getInstance()
                        .extractAudioMp3(new String[] { dustItem.getPath(), temppath, temppath });
            }
            FileUtil.saveBitmap(BitmapUtil.scaleRatioByMatrix(retriever.getFrameAtTime(0),
                    ConstantMediaSize.screenWidth, ConstantMediaSize.screenHeight), dustItem.getFirstFramePath());
            FileUtil.saveBitmap(
                    BitmapUtil.scaleRatioByMatrix(retriever.getFrameAtTime(dustItem.getDuration() * 1000),
                            ConstantMediaSize.screenWidth, ConstantMediaSize.screenHeight),
                    dustItem.getVideolastFramePath());
            // 对视频进行图片多略图提取
            ThreadPoolMaxThread.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    if (!isTrim) {
                        Map<Integer, Bitmap> map = new HashMap<>();
                        try {
                            retriever.setDataSource(dustItem.getPath());
                            int count = (int) (dustItem.getDuration() / 1000);
                            for (int i = 0; i < count; i++) {
                                map.put(i, BitmapUtil.scaleImage(retriever.getFrameAtTime(i * 1000000), THUMTAIL_WIDTH,
                                        THUMTAIL_HEIGHT));
                            }
                            dustItem.setThumbnails(map);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    retriever.release();
                }
            });
            return dustItem;
        }
        return null;
    }

    public void addDataOriginItem(MediaItem mediaItem) {
        dataOrigin.add(mediaItem);
    }

    private void setPreviewFrame(com.ijoysoft.mediasdk.module.entity.MediaItem currentItem,
            com.ijoysoft.mediasdk.module.entity.MediaItem lastItem) {
        if (currentItem == null || lastItem == null) {
            return;
        }
        if (lastItem.getMediaType() == MediaType.PHOTO) {
            currentItem.setPrepareFramePath(lastItem.getFirstFramePath());
            currentItem.setPreRotation(lastItem.getRotation());
        } else {
            currentItem.setPrepareFramePath(lastItem.getVideolastFramePath());
        }
    }

    /**
     * 列表位置调换
     */
    public void swapOperate(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(dataOperate, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(dataOperate, i, i - 1);
            }
        }
        if (fromPosition == 0 || toPosition == 0) {
            int lastIndex = fromPosition == 0 ? toPosition : fromPosition;
            com.ijoysoft.mediasdk.module.entity.MediaItem first = dataOperate.get(0);
            com.ijoysoft.mediasdk.module.entity.MediaItem last = dataOperate.get(lastIndex);
            // if (first.getTransitionFilter() != null) {
            // last.setTransitionFilter(first.getTransitionFilter());
            // }
            // if (first.getTransitionFilter() != null) {
            // first.setTransitionFilter(null);
            // }
            // last.setTransitionFilter(null);
            first.setTransitionFilter(null);

        }
        linkPreviewFrame();
    }

    /**
     * 列表位置调换
     */
    public void swapOrigin(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(dataOrigin, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(dataOrigin, i, i - 1);
            }
        }
    }

    /**
     * 列表位置调换
     */
    public void swap(List<com.ijoysoft.mediasdk.module.entity.MediaItem> list, int fromPosition, int toPosition) {
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
     * 列表位置调换
     */
    public void swapBoth(int fromPosition, int toPosition) {
        swapOperate(fromPosition, toPosition);
        swapOrigin(fromPosition, toPosition);
        //
        linkPreviewFrame();
    }

    /**
     * 移除某单项
     *
     * @param index
     */
    public synchronized void remove(int index) {
        // Log.i("test", new Gson().toJson(dataOperate));
        Dlog.d("remove-position", "dataOperateSize===" + dataOperate.size());
        if (index > dataOperate.size()) {
            return;
        }
        dataOperate.remove(index);
    }

    public void removeBoth(int index) {
        dataOperate.remove(index);
        dataOrigin.remove(index);
    }

    /**
     * 复制其中的一行，原始数据，和media操作数据都同步复制
     * 等待转场方案修复
     *
     * @param index
     */
    public com.ijoysoft.mediasdk.module.entity.MediaItem copyMediaItem(int index) {
        dataOrigin.add(index, (MediaItem) dataOrigin.get(index).clone());
        // 操作mediaItem里面很多对象， 需要进行深度拷贝,并且切换转场头尾帧
        com.ijoysoft.mediasdk.module.entity.MediaItem mediaItemTemp =
                (com.ijoysoft.mediasdk.module.entity.MediaItem) dataOperate.get(index).clone();
        // 改变bitmapframe
        try {
            if (mediaItemTemp.getTransitionFilter() != null) {
                TransitionFilter transitionFilter =
                        TransitionFactory.initFilters(mediaItemTemp.getTransitionFilter().getTransitionType());
                mediaItemTemp.setTransitionFilter(transitionFilter);
            }
            if (mediaItemTemp.getAfilter() != null) {
                mediaItemTemp.setAfilter(MagicFilterFactory.initFilters(mediaItemTemp.getAfilter().getmFilterType()));
            }
            if (mediaItemTemp.getMediaMatrix() != null) {
                mediaItemTemp.setMediaMatrix((MediaMatrix) mediaItemTemp.getMediaMatrix().clone());
            }
            if (mediaItemTemp.getVideoCutInterval() != null) {
                mediaItemTemp.setVideoCutInterval((DurationInterval) mediaItemTemp.getVideoCutInterval().clone());
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        mediaItemTemp.setEqualId(UUID.randomUUID().toString());
        dataOperate.add(index, mediaItemTemp);
        // 插入之后，更新后一帧的bitmapframe值
        setPreviewFrame(dataOperate.get(index + 1), mediaItemTemp);
        return mediaItemTemp;
    }

    public void updateOriginTrimVideo(com.ijoysoft.mediasdk.module.entity.MediaItem operate, MediaItem origin) {
        if (ObjectUtils.isEmpty(operate.getTrimPath())) {
            return;
        }
        final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(operate.getTrimPath());
            origin.tempDuration =
                    (Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
            Dlog.i(TAG, "updateOriginTrimVideo-duration:" + origin.tempDuration);
            operate.setTempDuration(origin.tempDuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        linkPreviewFrame();
    }

    /**
     * 数据存储到本地,综合看，用green是失败的策略，
     * 优势比如上用文件形式xml存储json数据结构
     */
    public void save2Local() {
        long start = System.currentTimeMillis();
        ThreadPoolManager.getThreadPool().shutDown();
        if (ThreadPoolManager.getThreadPool().awaitTermination(60)) {
            ThreadPoolManager.getThreadPool().release();
        }
        Dlog.i(TAG, "save2Local->shutDown:" + (System.currentTimeMillis() - start));
        ThreadPoolManager.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                long temp;
                final DaoSession daoSession = MyApplication.getInstance().getDaoSession();
                clearProjectSqliteData();
                // 数据存储列表
                final List<DurationIntervalData> durationIntervalDataList = new ArrayList<>();
                final List<MediaMatrixData> mediaMatrixDataList = new ArrayList<>();
                final List<MediaItemData> mediaItemDataList = new ArrayList<>();
                final List<DoodleItemData> doodleItemDataList = new ArrayList<>();
                final List<AudioMediaItemData> audioMediaItemDataList = new ArrayList<>();
                // media存储
                final ArrayList<com.ijoysoft.mediasdk.module.entity.MediaItem> operateDataCopy = getDataOperateCopy();
                //如果是视频对象，则把缩略图进行清理
                for(com.ijoysoft.mediasdk.module.entity.MediaItem m: operateDataCopy){
                    if(m instanceof VideoMediaItem){
                        if(!ObjectUtils.isEmpty(((VideoMediaItem) m).getThumbnails())){
                            for(Bitmap bitmap:((VideoMediaItem) m).getThumbnails().values()){
                                bitmap.recycle();
                            }
                        }
                    }
                }
                // 存储封面图片
                final String coverPath = PathUtil.createCoverImage(getProjectID());
                ThreadPoolManager.getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FileUtils.copy(operateDataCopy.get(0).getFirstFramePath(), coverPath, false); // 取备份数据
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                saveMedia2Local(operateDataCopy, mediaItemDataList, mediaMatrixDataList, durationIntervalDataList);
                Dlog.i(TAG, "saveMedia2Local-time:" + (System.currentTimeMillis() - start));
                temp = System.currentTimeMillis();
                // 涂鸦转化
                saveDoodle2Local(doodleItemDataList, durationIntervalDataList);
                Dlog.d("doodle-save", "doodleItemDataList-save=====" + doodleItemDataList.size());
                Dlog.i(TAG, "saveDoodle2Local-time:" + (System.currentTimeMillis() - temp));
                // 音频转化
                saveAudioLocal(audioMediaItemDataList, durationIntervalDataList);
                Dlog.i(TAG, "saveAudioLocal-time:" + (System.currentTimeMillis() - temp));
                if (mediaItemDataList.isEmpty()) {
                    return;
                }
                // 事务处理数据库，保证数据的完整性
                daoSession.runInTx(new Runnable() {
                    @Override
                    public void run() {
                        if (currentProject.getProjectName() == null || currentProject.getProjectName().equals("")) {
                            currentProject.setProjectName(
                                    "AutoDraft_" + TimeUtil.getDayFormatTime(System.currentTimeMillis()));
                        }
                        currentProject.setCoverPath(coverPath);
                        currentProject.setUpdateTime(new Date());
                        currentProject.setDoodleList(doodleItemDataList);
                        currentProject.setAudioList(audioMediaItemDataList);
                        ProjectDao projectDao = daoSession.getProjectDao();
                        if (isLocal) {
                            projectDao.update(currentProject);
                        } else {
                            projectDao.insert(currentProject);
                        }
                        // 保存mediaitemList
                        MediaItemDataDao mediaItemDataDao = daoSession.getMediaItemDataDao();
                        for (MediaItemData mediaItemData : mediaItemDataList) {
                            mediaItemDataDao.insert(mediaItemData);
                        }
                        MediaMatrixDataDao mediaMatrixDataDao = daoSession.getMediaMatrixDataDao();
                        for (MediaMatrixData mediaMatrixData : mediaMatrixDataList) {
                            mediaMatrixDataDao.insert(mediaMatrixData);
                        }
                        // 保存doodle数据
                        Dlog.d("doodle-save", "doodleItemDataList-insert=====" + doodleItemDataList.size());
                        DoodleItemDataDao doodleItemDataDao = daoSession.getDoodleItemDataDao();
                        for (DoodleItemData doodleItemData : doodleItemDataList) {
                            doodleItemDataDao.insert(doodleItemData);
                        }
                        Dlog.d("doodle-save", "doodleItemDataList-result====="
                                + doodleItemDataDao._queryProject_DoodleList(getProjectID()));

                        // 保存audio数据
                        AudioMediaItemDataDao audioMediaItemDataDao = daoSession.getAudioMediaItemDataDao();
                        for (AudioMediaItemData audioMediaItemData : audioMediaItemDataList) {
                            audioMediaItemDataDao.insert(audioMediaItemData);
                        }
                        // 最后保存所有的DurationInterval数据
                        DurationIntervalDataDao durationIntervalDataDao = daoSession.getDurationIntervalDataDao();
                        for (DurationIntervalData durationIntervalData : durationIntervalDataList) {
                            durationIntervalDataDao.insert(durationIntervalData);
                        }
                    }
                });
                Dlog.i(TAG, "save2Local-time:" + (System.currentTimeMillis() - start));
                // TODO: 2019/7/25 注释掉后再运行，数据正确，清数据之前应该阻塞主线程, 60S内执行完？
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                onDestory();
            }
        });

    }

    /**
     * 本地化存储mediaitem，先删除再插入
     * 这里本地文件保存成功之后，给主页发送一个存储存储成功通知
     */
    private void saveMedia2Local(final ArrayList<com.ijoysoft.mediasdk.module.entity.MediaItem> dataOperateCopy,
            List<MediaItemData> mediaItemDataList, List<MediaMatrixData> mediaMatrixDataList,
            List<DurationIntervalData> durationIntervalDataList) {
        // 媒体类转化
        MediaItemData mediaItemData;
        com.ijoysoft.mediasdk.module.entity.MediaItem mediaItem;
        long durationSum = 0;
        for (int i = 0; i < dataOperateCopy.size(); i++) {
            mediaItem = dataOperateCopy.get(i);
            mediaItemData = new MediaItemData();
            try {
                BeanUtil.copyPropertiesExclude(mediaItem, mediaItemData,
                        new String[] { "mediamatrix", "videocutinterval" });// 一定要小写
            } catch (Exception e) {
                e.printStackTrace();
            }
            durationSum += mediaItem.getDuration();
            // 保存media列表的头尾帧,用retriever取视频帧，要500ms，File形式读取bitmap要65ms，所以最终采用bitmap存储
            // 图片帧的方案，假设10个视频，那么头尾帧也要一两秒，所以还是放到线程的io中进行处理
            // 后续这个帧存储，考虑两media相互换位置、直接改变localFrame的命名便可，无需重新写入bitmap文件
            // 此时清除数据，导致后面任务在处理时，数据没了，出现数组越界崩溃
            if (mediaItem.getTransitionFilter() != null) {
                mediaItemData.setTransitionType(mediaItem.getTransitionFilter().getTransitionType());
            }
            if (mediaItem.getAfilter() != null) {
                mediaItemData.setFilterType(mediaItem.getAfilter().getmFilterType());
            }
            // mediaMatrix保存
            mediaItemDataList.add(mediaItemData);
            if (mediaItem.getMediaMatrix() != null) {
                MediaMatrixData mediaMatrixData = new MediaMatrixData(mediaItemData.getMediaId(), getProjectID(),
                        mediaItem.getMediaMatrix().getAngle(), mediaItem.getMediaMatrix().getScale(),
                        mediaItem.getMediaMatrix().getOffsetX(), mediaItem.getMediaMatrix().getOffsetY());
                mediaMatrixDataList.add(mediaMatrixData);
            }
            // VideoCutInterval
            if (mediaItem.getVideoCutInterval() != null) {
                DurationIntervalData durationIntervalData = new DurationIntervalData(getProjectID(),
                        mediaItemData.getMediaId(), mediaItem.getVideoCutInterval().getStartDuration(),
                        mediaItem.getVideoCutInterval().getEndDuration(),
                        mediaItem.getVideoCutInterval().getInterval());
                durationIntervalDataList.add(durationIntervalData);
            }
            AppBus.get().post(new SaveLocalEvent());// 通知maiactivitity更新
        }
        currentProject.setDuration(durationSum);
    }

    /**
     * 清除数据库数据
     */

    private void clearProjectSqliteData() {
        if (currentProject == null) {
            return;
        }
        DaoSession daoSession = MyApplication.getInstance().getDaoSession();

        currentProject.__setDaoSession(daoSession);
        MediaItemDataDao mediaItemDataDao = daoSession.getMediaItemDataDao();
        mediaItemDataDao.deleteInTx(currentProject.getDataSource());

        QueryBuilder<MediaMatrixData> qbMediaMatrix =
                MyApplication.getInstance().getDaoSession().getMediaMatrixDataDao().queryBuilder();
        DeleteQuery<MediaMatrixData> bdMediaMatrix =
                qbMediaMatrix.where(MediaMatrixDataDao.Properties.ProjectId.eq(getProjectID())).buildDelete();
        bdMediaMatrix.executeDeleteWithoutDetachingEntities();

        QueryBuilder<AudioDurationData> qbAudioDurationData =
                MyApplication.getInstance().getDaoSession().getAudioDurationDataDao().queryBuilder();
        DeleteQuery<AudioDurationData> bdAudioDurationData =
                qbAudioDurationData.where(AudioDurationDataDao.Properties.ProjectId.eq(getProjectID())).buildDelete();
        bdAudioDurationData.executeDeleteWithoutDetachingEntities();

        QueryBuilder<AudioMediaItemData> qbAudioMediaItemData =
                MyApplication.getInstance().getDaoSession().getAudioMediaItemDataDao().queryBuilder();
        DeleteQuery<AudioMediaItemData> bdAudioMediaItemData =
                qbAudioMediaItemData.where(AudioMediaItemDataDao.Properties.ProjectId.eq(getProjectID())).buildDelete();
        bdAudioMediaItemData.executeDeleteWithoutDetachingEntities();

        QueryBuilder<DoodleItemData> qbDoodleItemData =
                MyApplication.getInstance().getDaoSession().getDoodleItemDataDao().queryBuilder();
        DeleteQuery<DoodleItemData> bdDoodleItemData =
                qbDoodleItemData.where(DoodleItemDataDao.Properties.ProjectId.eq(getProjectID())).buildDelete();
        bdDoodleItemData.executeDeleteWithoutDetachingEntities();

        QueryBuilder<DurationIntervalData> qbDurationIntervalData =
                MyApplication.getInstance().getDaoSession().getDurationIntervalDataDao().queryBuilder();
        DeleteQuery<DurationIntervalData> bdDurationIntervalData = qbDurationIntervalData
                .where(DurationIntervalDataDao.Properties.ProjectId.eq(getProjectID())).buildDelete();
        bdDurationIntervalData.executeDeleteWithoutDetachingEntities();
    }

    /**
     * 本地化存储doodle 先删除再插入
     */
    private void saveDoodle2Local(List<DoodleItemData> doodleItemDataList,
            List<DurationIntervalData> durationIntervalDataList) {
        DoodleItemData doodleItemData;
        DoodleItem doodleItem;
        for (int i = 0; i < doodleList.size(); i++) {
            doodleItem = doodleList.get(i);
            doodleItemData = new DoodleItemData();
            try {
                // 记得字段一定要用小写！
                BeanUtil.copyPropertiesExclude(doodleItem, doodleItemData, new String[] { "durationinterval" });
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 保存duration
            DurationIntervalData durationIntervalData = new DurationIntervalData(getProjectID(),
                    doodleItemData.getDoodleId(), doodleItem.getDurationInterval().getStartDuration(),
                    doodleItem.getDurationInterval().getEndDuration(), doodleItem.getDurationInterval().getInterval());
            durationIntervalDataList.add(durationIntervalData);
            doodleItemData.setDurationInterval(durationIntervalData);
            doodleItemDataList.add(doodleItemData);

        }
    }

    /**
     * 本地化audio 先删除再插入
     */
    private void saveAudioLocal(List<AudioMediaItemData> audioDurationDataList,
            List<DurationIntervalData> durationIntervalDataList) {
        AudioMediaItemData audioMediaItemData;
        AudioMediaItem audioMediaItem;
        for (int i = 0; i < audioList.size(); i++) {
            audioMediaItem = audioList.get(i);
            audioMediaItemData = new AudioMediaItemData();
            try {
                BeanUtil.copyPropertiesExclude(audioMediaItem, audioMediaItemData, new String[] { "durationinterval" });
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 真是受够这个sql框架，子类保存不能联动保存的
            DurationIntervalData durationIntervalData = new DurationIntervalData(getProjectID(),
                    audioMediaItemData.getAudioId(), audioMediaItem.getDurationInterval().getStartDuration(),
                    audioMediaItem.getDurationInterval().getEndDuration(),
                    audioMediaItem.getDurationInterval().getInterval());
            durationIntervalDataList.add(durationIntervalData);
            audioMediaItemData.setDurationInterval(durationIntervalData);
            audioDurationDataList.add(audioMediaItemData);
        }
    }

    /**
     * 转化本地数据为当前逻辑数据
     */
    public void local2Current(Project project) {
        isLocal = true;
        isLocalMultiEdit = true;
        long start = System.currentTimeMillis();
        if (dataOrigin == null) {
            dataOrigin = new ArrayList<>();
        }
        dataOrigin.clear();
        currentProject = project;
        // mediaItem转化
        project.resetDataSource();
        List<MediaItemData> mediaItemDataList = project.getDataSource();
        com.ijoysoft.mediasdk.module.entity.MediaItem mediaOperate;
        MediaItem mediaOrigin;
        for (MediaItemData mediaItemData : mediaItemDataList) {
            if (mediaItemData.getMediaType() == MediaType.VIDEO) {
                mediaOperate = new VideoMediaItem();
            } else {
                mediaOperate = new PhotoMediaItem();
            }
            try {
                BeanUtil.copyPropertiesExclude(mediaItemData, mediaOperate, new String[] { "firstframe",
                        "videolastframe", "transitiontype", "filtertype", "mediamatrix", "videocutinterval" });
                // 数据库升级字段兼容
                mediaOperate.setTempDuration(mediaItemData.getDuration());
                if (mediaOperate.getMediaType() == MediaType.PHOTO) {
                    String path = TextUtils.isEmpty(mediaItemData.getFirstFramePath()) ? mediaItemData.getPath()
                            : mediaItemData.getFirstFramePath();
                    mediaOperate.setFirstFramePath(path);
                } else {
                    String path =
                            TextUtils.isEmpty(mediaItemData.getVideolastFrame()) ? mediaItemData.getVideolastFramePath()
                                    : mediaItemData.getVideolastFrame();
                    mediaOperate.setVideolastFramePath(path);
                    path = TextUtils.isEmpty(mediaItemData.getFirstFrame()) ? mediaItemData.getFirstFramePath()
                            : mediaItemData.getFirstFrame();
                    mediaOperate.setFirstFramePath(path);
                }

                // operation转化
                mediaOperate.setTransitionFilter(TransitionFactory.initFilters(mediaItemData.getTransitionType()));
                mediaOperate.setAfilter(MagicFilterFactory.initFilters(mediaItemData.getFilterType()));
                // mediamatrix
                if (mediaItemData.getMediaMatrix() != null) {
                    MediaMatrix matrixOperate = new MediaMatrix();
                    BeanUtil.copyProperties(mediaItemData.getMediaMatrix(), matrixOperate);
                    mediaOperate.setMediaMatrix(matrixOperate);
                }
                // durationInterval
                if (mediaItemData.getVideoCutInterval() != null) {
                    DurationInterval interval = new DurationInterval();
                    BeanUtil.copyProperties(mediaItemData.getVideoCutInterval(), interval);
                    mediaOperate.setVideoCutInterval(interval);
                }
                dataOperate.add(mediaOperate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // origin转化
            mediaOrigin = new MediaItem();
            mediaOrigin.path = mediaItemData.getPath();
            mediaOrigin.duration = mediaItemData.getDuration();
            mediaOrigin.type =
                    mediaItemData.getMediaType() == MediaType.VIDEO ? MediaItem.TYPE_VIDEO : MediaItem.TYPE_IMAGE;
            mediaOrigin.width = mediaItemData.getWidth();
            mediaOrigin.height = mediaItemData.getHeight();
            mediaOrigin.rotation = mediaItemData.getRotation();
            mediaOrigin.title = mediaItemData.getName();
            dataOrigin.add(mediaOrigin);
        }
        // 链接preMediaframe,新版本这个可以去掉
        linkPreviewFrame();

        // doodle
        doodleList.clear();
        List<DoodleItemData> doodleItemDataList = project.getDoodleList();
        Dlog.d("doodle-save", "doodleItemDataList---tocurrent==" + doodleItemDataList.size());
        for (DoodleItemData doodleItemData : doodleItemDataList) {
            DoodleItem doodleItem = new DoodleItem();
            try {
                BeanUtil.copyPropertiesExclude(doodleItemData, doodleItem, new String[] { "durationinterval" });
                // durationInterval
                DurationInterval interval = new DurationInterval();
                BeanUtil.copyProperties(doodleItemData.getDurationInterval(), interval);
                doodleItem.setDurationInterval(interval);
                doodleList.add(doodleItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // audio
        project.resetAudioList();
        audioList.clear();
        List<AudioMediaItemData> audioMediaItemDataList = project.getAudioList();
        for (AudioMediaItemData audioMediaItemData : audioMediaItemDataList) {
            AudioMediaItem audioMediaItem = new AudioMediaItem();
            try {
                BeanUtil.copyPropertiesExclude(audioMediaItemData, audioMediaItem, new String[] { "durationinterval" });
                // durationInterval
                DurationInterval interval = new DurationInterval();
                BeanUtil.copyProperties(audioMediaItemData.getDurationInterval(), interval);
                audioMediaItem.setDurationInterval(interval);
                audioList.add(audioMediaItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onDestory() {
        if (dataOperate != null) {
            dataOperate.clear();
        }
        if (dataOrigin != null) {
            dataOrigin.clear();
        }
        if (doodleList != null) {
            doodleList.clear();
        }
        if (audioList != null) {
            audioList.clear();
        }
        currentProject = null;
        mScreenShotBitmap = null;
        isLocal = false;
        isLocalMultiEdit = false;
    }

    /**
     * 清除数据
     */
    public void clearDraft() {
        DaoSession daoSession = MyApplication.getInstance().getDaoSession();
        String projectPath = ConstantPath.getCurrentProjectPath(getProjectID());
        FileUtil.deleteFile(new File(projectPath));
        ProjectDao projectDao = daoSession.getProjectDao();
        projectDao.deleteByKey(getProjectID());
        // 清理数据库数据
        clearProjectSqliteData();
    }

    public ArrayList<MediaItem> getDataOriginCopy() {
        ArrayList<MediaItem> list = new ArrayList<>();
        for (int i = 0; i < dataOrigin.size(); i++) {
            MediaItem mediaItem = (MediaItem) dataOrigin.get(i).clone();
            list.add(mediaItem);
        }
        return list;
    }

    public ArrayList<com.ijoysoft.mediasdk.module.entity.MediaItem> getDataOperateCopy() {
        ArrayList<com.ijoysoft.mediasdk.module.entity.MediaItem> list = new ArrayList<>();
        for (int i = 0; i < dataOperate.size(); i++) {
            com.ijoysoft.mediasdk.module.entity.MediaItem mediaItem =
                    (com.ijoysoft.mediasdk.module.entity.MediaItem) dataOperate.get(i).clone();
            list.add(mediaItem);
        }
        // return (ArrayList<com.ijoysoft.mediasdk.module.entity.MediaItem>) dataOperate.clone(); 返回对象与原对象地址一致，失败
        return list;
    }

    public ArrayList<com.ijoysoft.mediasdk.module.entity.MediaItem> getDataOperate() {
        return dataOperate;
    }

    private MediaDataRepository() {
        dataOperate = new ArrayList<>();
        doodleList = new ArrayList<>();
        audioList = new ArrayList<>();
    }

    private static class SingleTonHoler {
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
        if (currentProject == null) {
            currentProject = new Project();
            currentProject.randomProjectId();
            currentProject.setCreateTime(new Date());
        }
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

    public List<DoodleItem> getDoodleTypeList() {
        List<DoodleItem> doodleTypeList = new ArrayList<>();
        for (DoodleItem d : doodleList) {
            if (d.getWaterMarkType() == WaterMarkType.DOODLE) {
                doodleTypeList.add(d);
            }
        }
        return doodleTypeList;
    }

    /**
     * 移除doodle
     *
     * @param doodleItem
     */
    public void removeDoodle(DoodleItem doodleItem) {
        doodleList.remove(doodleItem);
    }

    public void addDoodle(DoodleItem doodleItem) {
        doodleList.add(doodleItem);
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

    public List<DoodleItem> getDoodlePicture() {
        List<DoodleItem> doodlePictureList = new ArrayList<>();
        for (DoodleItem d : doodleList) {
            if (d.getWaterMarkType() == WaterMarkType.PICTURE) {
                doodlePictureList.add(d);
            }
        }
        return doodlePictureList;
    }

    public void setDoodleList(List<DoodleItem> doodleList) {
        this.doodleList = doodleList;
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
        audioList.clear();
        audioList.add(audioMediaItem);
        dealAudioDuration(audioList);
    }

    /**
     * 清除单音频
     */
    public void removeSingleAudio() {
        if (audioList.size() > 0) {
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
        return audioList;
    }

    public List<AudioMediaItem> getAudioListCopy() {
        List<AudioMediaItem> audioMediaItems = new ArrayList<>();
        for (int i = 0; i < audioList.size(); i++) {
            AudioMediaItem audioMediaItem = (AudioMediaItem) audioList.get(i).clone();
            audioMediaItem.setDurationInterval((DurationInterval) audioMediaItem.getDurationInterval().clone());
            audioMediaItems.add(audioMediaItem);
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
            audioMediaItem.setTitle(cutMusicItem.getTitle());
            audioMediaItem.setVolume((float) cutMusicItem.getVolume() / 100f);
            // 改变文件名并赋值
            audioMediaItem.setPath(cutMusicItem.getPath());
            audioMediaItem.setDurationInterval(cutMusicItem.getDurationInterval());
            audioMediaItem.setProjectId(MediaDataRepository.getInstance().getProjectID());
            audioMediaItems.add(audioMediaItem);
        }
        setMultiAudio(audioMediaItems);
        return audioMediaItems;
    }

    /**
     * 音频对象转化，并且后台再重处理裁剪合并音频文件
     *
     * @param list
     * @return
     */
    public List<AudioMediaItem> audioExchangeAndDealAudio(List<CutMusicItem> list) {
        List<AudioMediaItem> audioMediaItems = audioExchange(list);
        dealAudioDuration(audioMediaItems);
        return audioMediaItems;
    }

    /**
     * 处理每一个音频片段，如果物理时长大于逻辑时长，则进行裁剪
     * 如果物理时长小于逻辑时长，则进行裁剪合并
     */
    public void dealAudioDuration(List<AudioMediaItem> audioList) {
        if (audioList == null || audioList.isEmpty()) {
            return;
        }
        for (final AudioMediaItem mediaItem : audioList) {
            Dlog.i(TAG, "dealAudioDuration:" + mediaItem.toString());
            if (mediaItem.getDuration() > mediaItem.getDurationInterval().getInterval()) {
                if(mediaItem.getDurationInterval().getInterval()==0){
                    mediaItem.setMurgePath(mediaItem.getPath());
                    continue;
                }
                mediaItem.setMurgePath(ConstantPath.createAudioMurgePath(mediaItem));
                VideoEditManager.cutAudio(mediaItem.getPath(), mediaItem.getMurgePath(), TimeUtil.getCutForamt(0),
                        TimeUtil.getCutForamt(mediaItem.getDurationInterval().getInterval()), new SingleCmdListener() {
                            @Override
                            public void next() {
                                mediaItem.setMurgePath(FileUtil.changePrivateFileName(mediaItem.getMurgePath()));
                            }
                        }, true);
            } else {
                int count = (int) (mediaItem.getDurationInterval().getInterval() / mediaItem.getDuration());
                int offset = (int) (mediaItem.getDurationInterval().getInterval() % mediaItem.getDuration());
                Dlog.i(TAG, "count:" + count + ",offset:" + offset);
                mediaItem.setMurgePath(ConstantPath.createAudioMurgePath(mediaItem));
                // 差值大于100ms才进行切割
                if (offset >= 500) {
                    mediaItem.setMurgeOffsetPath(ConstantPath.createAudioMurgePathOffset(mediaItem));
                    final String[] array = new String[count + 1];
                    for (int i = 0; i < array.length; i++) {
                        array[i] = mediaItem.getPath();
                    }
                    array[count] = mediaItem.getMurgeOffsetPath();
                    List<String[]> cmdList = new ArrayList<>();
                    cmdList.add(VideoEditManager.cutAudio(mediaItem.getPath(), mediaItem.getMurgeOffsetPath(),
                            TimeUtil.getCutForamt(0), TimeUtil.getCutForamt(offset), null, false));
                    // 如果数量超过5
                    if (count > 5) {
                        cmdList.add(VideoEditManager.videoMurgeFromFilelist(null, mediaItem.getMurgePath(), false,false, null,
                                array));
                    } else {
                        cmdList.add(VideoEditManager.composeMultiAudio(mediaItem.getMurgePath(), null, false, array));
                    }

                    FFmpegCmd.getInstance().executeList(cmdList, new SingleCmdListener() {

                        @Override
                        public void next() {
                            mediaItem
                                    .setMurgeOffsetPath(FileUtil.changePrivateFileName(mediaItem.getMurgeOffsetPath()));
                            mediaItem.setMurgePath(FileUtil.changePrivateFileName(mediaItem.getMurgePath()));
                        }
                    });
                } else {
                    String[] array = new String[count];
                    for (int i = 0; i < array.length; i++) {
                        array[i] = mediaItem.getPath();
                    }
                    if (count > 5) {
                        VideoEditManager.videoMurgeFromFilelist(null, mediaItem.getMurgePath(), false,false,
                                new SingleCmdListener() {
                                    @Override
                                    public void next() {
                                        mediaItem
                                                .setMurgePath(FileUtil.changePrivateFileName(mediaItem.getMurgePath()));
                                    }
                                }, array);
                        return;
                    }
                    VideoEditManager.composeMultiAudio(mediaItem.getMurgePath(), new SingleCmdListener() {
                        @Override
                        public void next() {
                            mediaItem.setMurgePath(FileUtil.changePrivateFileName(mediaItem.getMurgePath()));
                        }
                    }, true, array);
                }
            }
        }
    }

    /**
     * 最小单位为5个
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

    public RatioType calcPreviewRatio() {
        float scale;
        float temp = 0;
        for (int i = 0; i < dataOperate.size(); i++) {
            scale = (float) dataOperate.get(i).getWidth() / (float) dataOperate.get(i).getHeight();
            if (dataOperate.get(i).getRotation() == 90 || dataOperate.get(i).getRotation() == 270) {
                scale = (float) dataOperate.get(i).getHeight() / (float) dataOperate.get(i).getWidth();
            }
            if (scale > 1.33) {
                Dlog.i(TAG, "scale:" + scale + "dataOperate.get(i).getWidth():" + dataOperate.get(i).getWidth() + ","
                        + dataOperate.get(i).getHeight() + "," + dataOperate.get(i).getRotation());
                return RatioType._16_9;
            }
            if (scale > temp) {
                temp = scale;
            }
        }
        Dlog.i(TAG, "scale:" + temp);
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

    /**
     * 清除异常数据
     */
    public void clearHistoryCrashData() {
        ThreadPoolMaxThread.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                File file = new File(PathUtil.APPTPATH);
                if (file == null || !file.exists()) {
                    return;
                }
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (PathUtil.checkIsInnerPath(f.getPath())) {
                            continue;
                        }
                        if (!SqliteDataFetchHelper.checkExistProject(f.getName())) {
                            FileUtil.deleteFile(f);
                        }
                    }
                }
            }
        });
    }

    public RatioType calcRatioType(com.ijoysoft.mediasdk.module.entity.MediaItem mediaItem) {
        float scale;
        scale = (float) mediaItem.getWidth() / (float) mediaItem.getHeight();
        if (mediaItem.getRotation() == 90 || mediaItem.getRotation() == 270) {
            scale = (float) mediaItem.getHeight() / (float) mediaItem.getWidth();
        }
        Dlog.i(TAG, "scale:" + scale);
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
}
