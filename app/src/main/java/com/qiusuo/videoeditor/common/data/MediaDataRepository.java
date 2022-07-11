package com.qiusuo.videoeditor.common.data;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.AudioMediaItem;
import com.ijoysoft.mediasdk.module.entity.DoodleItem;
import com.ijoysoft.mediasdk.module.entity.DurationInterval;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaMatrix;
import com.ijoysoft.mediasdk.module.entity.MediaType;
import com.ijoysoft.mediasdk.module.entity.PhotoMediaItem;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.entity.VideoMediaItem;
import com.ijoysoft.mediasdk.module.entity.WaterMarkType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 数据存储
 * 包含外部数据同步到sdk列表数据
 * 后续工作台的数据都同步在这里
 */
public class MediaDataRepository {
    private static final String TAG = "MediaDataRepository";
    private ArrayList<MediaItem> dataOrigin;
    private ArrayList<com.ijoysoft.mediasdk.module.entity.MediaItem> dataOperate;
//    private Project currentProject;
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
//        currentProject = null;
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
//            currentItem.setProjectId(getProjectID());
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
    public synchronized void addMediaItem(LocalMedia mediaItem, boolean isExtract, boolean isTrim) { // 可能要加个锁，防止添加数据后，显示的操作数据与源数据不匹配
        MediaItem dustMedia = mediaExtrange(mediaItem, isExtract, isTrim);
        if (dataOperate.size() > 1) {
            setPreviewFrame(dustMedia, dataOperate.get(dataOperate.size() - 1));
        }
        dataOperate.add(dustMedia);
//        if(dustMedia.getMediaType()==MediaType.VIDEO){
//            Log.i("test",FileUtils.getVideoAudioFormat(dustMedia.getPath()));
//        }
    }

    /**
     * 数据类型转化
     * 这里如果是视频的话，就对视频进行前后帧的本地存储，方便后续使用
     * 这样在纹理加载的时候，要对目标的bitmap进行缩小
     *
     * @param originItem
     * @return
     */

    private MediaItem mediaExtrange(LocalMedia originItem, boolean isExtract, final boolean isTrim) {
        if (originItem.isImage()) {
            PhotoMediaItem dustItem = new PhotoMediaItem();
//            dustItem.setRotation(BytesBitmap.getExifOrientation(originItem.path, originItem.rotation));
//            dustItem.setProjectId(getProjectID());
            dustItem.setPath(originItem.getPath());
            dustItem.setSize(originItem.getSize());
            dustItem.setDuration(ConstantMediaSize.TIME_DURATION);
            dustItem.setTempDuration(ConstantMediaSize.TIME_DURATION);
            dustItem.setWidth(originItem.getWidth());
            dustItem.setHeight(originItem.getHeight());
            dustItem.setName(originItem.getFileName());
            // 检测是否图片是否需要压缩
            dustItem.setFirstFramePath(dustItem.getPath());
//            if (originItem.getWidth() > ConstantMediaSize.screenWidth || originItem.getWidth() > ConstantMediaSize.screenHeight) {
//                dustItem.setFirstFramePath(PathUtil.createFrameImage(getProjectID()));
//                FileUtil.saveBitmap(BitmapUtil.scaleRatioByPathByMatrix(dustItem.getPath(),
//                        ConstantMediaSize.screenWidth, ConstantMediaSize.screenHeight), dustItem.getFirstFramePath());
//            }

            return dustItem;
        } else  {
            final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            final VideoMediaItem dustItem = new VideoMediaItem();
//            dustItem.setProjectId(getProjectID());
            dustItem.setPath(originItem.getPath());
            dustItem.setSize(originItem.getSize());
            dustItem.setDuration(originItem.getDuration());
            dustItem.setTempDuration(originItem.getDuration());
            dustItem.setWidth(originItem.getWidth());
            dustItem.setHeight(originItem.getHeight());
            dustItem.setName(originItem.getFileName());
            try {
                retriever.setDataSource(originItem.getPath());
            } catch (Exception e) {
                e.printStackTrace();
//                dustItem.setWidth(originItem.getWidth());
//                dustItem.setHeight(originItem.getHeight());
//                if (FileUtils.checkExistAudio(dustItem.getPath()) && isExtract) {
//                    String temppath = ConstantPath.createExtractVideo2Audio(getProjectID(), dustItem.getName());
//                    dustItem.setExtracAudioPath(temppath + "_ijoysoft");
//                    FfmpegBackgroundHelper.getInstance()
//                            .extractAudioMp3(new String[]{dustItem.getPath(), temppath, temppath});
//                }
                return dustItem;
            }

            if (originItem.getWidth() == 0 || originItem.getHeight() == 0) {
                if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH) != null) {
                    dustItem.setHeight(Integer
                            .parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)));
                }
                if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT) != null) {
                    dustItem.setHeight(Integer
                            .parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)));
                }
            }
            if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE) != null) {
                int bitRate = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
                dustItem.setBitRate(bitRate);
            }
            String value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION) == null ? "0"
                    : retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
            dustItem.setRotation(Integer.valueOf(value));
//            dustItem.setFirstFramePath(PathUtil.createFrameImage(getProjectID()));
//            dustItem.setVideolastFramePath(PathUtil.createFrameImage(getProjectID()));
            // 做音频抽取动作,这里分是进入主控制台还是进入裁剪子页面
//            if (FileUtils.checkExistAudio(dustItem.getPath()) && isExtract) {
//                String temppath = ConstantPath.createExtractVideo2Audio(getProjectID(), dustItem.getName());
//                dustItem.setExtracAudioPath(temppath + "_ijoysoft");
//                FfmpegBackgroundHelper.getInstance()
//                        .extractAudioMp3(new String[]{dustItem.getPath(), temppath, temppath});
//            }
//            FileUtil.saveBitmap(BitmapUtil.scaleRatioByMatrix(retriever.getFrameAtTime(0),
//                    ConstantMediaSize.screenWidth, ConstantMediaSize.screenHeight), dustItem.getFirstFramePath());
//            FileUtil.saveBitmap(
//                    BitmapUtil.scaleRatioByMatrix(retriever.getFrameAtTime(dustItem.getDuration() * 1000),
//                            ConstantMediaSize.screenWidth, ConstantMediaSize.screenHeight),
//                    dustItem.getVideolastFramePath());
//            // 对视频进行图片多略图提取
//            ThreadPoolMaxThread.getInstance().execute(new Runnable() {
//                @Override
//                public void run() {
//                    if (!isTrim) {
//                        Map<Integer, Bitmap> map = new HashMap<>();
//                        try {
//                            retriever.setDataSource(dustItem.getPath());
//                            int count = (int) (dustItem.getDuration() / 1000);
//                            for (int i = 0; i < count; i++) {
//                                map.put(i, BitmapUtil.scaleImage(retriever.getFrameAtTime(i * 1000000), THUMTAIL_WIDTH,
//                                        THUMTAIL_HEIGHT));
//                            }
//                            dustItem.setThumbnails(map);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    retriever.release();
//                }
//            });
            return dustItem;
        }
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
        mScreenShotBitmap = null;
        isLocal = false;
        isLocalMultiEdit = false;
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
     * 添加单个音频，该音频覆盖整个播放周期
     *
     * @param audioMediaItem
     */
    public void setSingleAudio(AudioMediaItem audioMediaItem) {
        audioList.clear();
        audioList.add(audioMediaItem);
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

    public RatioType calcRatioType(com.ijoysoft.mediasdk.module.entity.MediaItem mediaItem) {
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
}
