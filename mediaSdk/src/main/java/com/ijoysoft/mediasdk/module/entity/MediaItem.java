package com.ijoysoft.mediasdk.module.entity;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.ijoysoft.mediasdk.common.utils.NumberUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;

import org.libpag.PAGFile;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.UUID;

/**
 * 媒体基类，记录视频，照片
 * 图片的源不再通过transitionfilter进行推动，图片源应该单独做一个纯afilter进行推动
 * 把transition和图片源进行分解
 */
public class MediaItem implements Serializable, Cloneable {

    @Override
    public int hashCode() {
        if (id == 0) {
            return path.hashCode();
        }
        return id | (mediaType.hashCode() << (NumberUtils.INT_BIT_COUNT / 2));
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof VideoMediaItem) {
            return mediaType == ((MediaItem) obj).getMediaType() && equalId.equals(((MediaItem) obj).getEqualId());
        }
        return id == ((MediaItem) obj).getId()
                && mediaType == ((MediaItem) obj).getMediaType();
    }

    private String projectId;
    protected int id;
    protected String name;
    protected String path;
    //物理保存缩放结果，旋转操作依然采用逻辑保存
    private String cropPath;
    protected MediaType mediaType;
    // 分辨率
    private int width;
    private int height;

    // 视频转的方向
    private int rotation;
    //是否经过压缩
    private int afterRotation;
    private long duration;
    // 因为duration和TempDuration在editclip视频裁剪和转场模块混用了，所以单独给个值赋予视频裁剪专用
    public long videoOriginDuration;

    private long tempDuration;

    /**
     * 临时存储
     */
    private long noneThemeDuration;
    // 前一张纹理的角度
    private String whyError;
    /**
     * 转场效果
     */
    private TransitionFilter transitionFilter;

    private GPUImageFilter afilter;
    private int filterProgress;
    // 当前播放进度
    public int currentSeek;

    // 旋转缩放平移
    private MediaMatrix mediaMatrix;

    /**
     * 对于视频裁剪，进行逻辑裁剪,放基类算了，放videoMediaItem转型太多
     * 最终放弃这个方案，采取物理裁剪方案，实时播放采取被裁剪后文件，该文件只记录值
     * 原因，mediaplayer没有实际播放完毕，状态不好控制，使得工作台播放不出来
     */
    private DurationInterval videoCutInterval;

    /**
     * 视频有裁剪记录后，对被裁结果进行覆盖
     */
    private String trimPath;
    private String equalId;
    //场景bitmapt
    private Bitmap bitmap;

    private Bitmap tempBitmap;

    /**
     * 静态控件
     */
    private List<Bitmap> mimapBitmaps;

    /**
     * 动态控件
     */
    private List<List<GifDecoder.GifFrame>> dynamicMitmaps;

    //pag文件
    private List<PAGFile> themePags;

    //对于选中片段，采用sdk-entiry，这样可以省去和上层entity同步转换
    public boolean isSelected;

    private String matrixValue;
    //视频变速（加减速）
    private float speed;

    //pag转场文件的长度
    private long pagDuration;
    private WeakReference<Bitmap> thumCache;

    /**
     * 如果文件是视频，渲染时走视频渲染接口
     */
    private int videoTexture = -1;


    public String getPath() {
        return path;
    }

    public String getShowPath() {
        if (ObjectUtils.isEmpty(cropPath)) {
            return path;
        }
        return cropPath;
    }

    /**
     * 如果有裁剪记录则取裁剪路径
     *
     * @return
     */
    public String getVideoPath() {
        if (!ObjectUtils.isEmpty(trimPath)) {
            return trimPath;
        }
        return path;
    }


    public void setPath(String path) {
        this.path = path;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getAfterRotation() {
        return afterRotation;
    }

    public void setAfterRotation(int afterRotation) {
        this.afterRotation = afterRotation;
    }


    /**
     * 加上matrix后的rotation
     *
     * @return
     */
    public int getRotationWithMatrix() {
        if (mediaMatrix != null) {
            return mediaMatrix.getAngle() + rotation;
        }
        return rotation;
    }


    public long getDuration() {
        return duration;
    }

    /**
     * 如果是视频类，获取变速后的最终时长
     *
     * @return
     */
    public long getVideoSpeedDuration() {
        if (speed != 0) {
            return (long) (duration * 1f / speed);
        }
        return duration;
    }

    public long getSpeedDuration() {
        if (speed == 0) {
            return duration;
        }
        return (long) (duration / speed);
    }

    public void setDuration(long duration) {
        this.duration = duration;
        tempDuration = duration;
    }

    public void setDurationOnly(long duration) {
        this.duration = duration;
    }

    public int getCurrentSeek() {
        return currentSeek;
    }

    public void setCurrentSeek(int currentSeek) {
        this.currentSeek = currentSeek;
    }

    public TransitionFilter getTransitionFilter() {
        return transitionFilter;
    }

    public void setTransitionFilter(TransitionFilter transitionFilter) {
        this.transitionFilter = transitionFilter;
    }


    public String getWhyError() {
        return whyError;
    }

    public void setWhyError(String whyError) {
        this.whyError = whyError;
    }


    public void setAfilter(GPUImageFilter afilter) {
        if (this.afilter != null) {
            this.afilter.destroy();
        }
        this.afilter = null;
        this.afilter = afilter;
    }

    public long getVideoOriginDuration() {
        return videoOriginDuration;
    }

    public void setVideoOriginDuration(long videoOriginDuration) {
        this.videoOriginDuration = videoOriginDuration;
    }

    public MediaItem() {
        equalId = UUID.randomUUID().toString();
        filterProgress = 100;
    }

    public MediaItem(String path) {
        this.path = path;
        equalId = UUID.randomUUID().toString();
    }

    /**
     * 用于索引查找
     *
     * @param id
     * @param mediaType
     */
    public MediaItem(int id, MediaType mediaType) {
        this.id = id;
        this.mediaType = mediaType;
    }

    public String getCropPath() {
        return cropPath;
    }

    public void setCropPath(String cropPath) {
        this.cropPath = cropPath;
    }

    public String getMatrixValue() {
        return matrixValue;
    }

    public void setMatrixValue(String matrixValue) {
        this.matrixValue = matrixValue;
    }

    @Override
    public Object clone() {
        MediaItem mediaTemp = null;
        try {
            mediaTemp = (MediaItem) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return mediaTemp;
    }

    public MediaMatrix getMediaMatrix() {
        return mediaMatrix;
    }

    public void setMediaMatrix(MediaMatrix mediaMatrix) {
        this.mediaMatrix = mediaMatrix;
    }

    public DurationInterval getVideoCutInterval() {
        return videoCutInterval;
    }

    public void setVideoCutInterval(DurationInterval videoCutInterval) {
        this.videoCutInterval = videoCutInterval;
    }

    public String getTrimPath() {
        return trimPath;
    }

    public void setTrimPath(String trimPath) {
        this.trimPath = trimPath;
    }


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getEqualId() {
        return equalId;
    }

    public void setEqualId(String equalId) {
        this.equalId = equalId;
    }

    public long getTempDuration() {
        return tempDuration;
    }

    public void setTempDuration(long tempDuration) {
        this.tempDuration = tempDuration;
    }

    public GPUImageFilter getAfilter() {
        return afilter;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getTempBitmap() {
        return tempBitmap;
    }

    public void setTempBitmap(Bitmap tempBitmap) {
        this.tempBitmap = tempBitmap;
    }

    public List<Bitmap> getMimapBitmaps() {
        return mimapBitmaps;
    }

    public void setMimapBitmaps(List<Bitmap> mimapBitmaps) {
        this.mimapBitmaps = mimapBitmaps;
    }

    public List<List<GifDecoder.GifFrame>> getDynamicMitmaps() {
        return dynamicMitmaps;
    }

    public void setDynamicMitmaps(List<List<GifDecoder.GifFrame>> dynamicMitmaps) {
        this.dynamicMitmaps = dynamicMitmaps;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isImage() {
        return mediaType == MediaType.PHOTO;
    }

    public boolean isVideo() {
        return mediaType == MediaType.VIDEO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFilterProgress() {
        return filterProgress;
    }

    public void setFilterProgress(int filterProgress) {
        this.filterProgress = filterProgress;
    }

    @Override
    public String toString() {
        return "MediaItem{" + "id='" + id + '\'' + "projectId='" + projectId + '\'' + ", name='" + name + '\'' + ", path='" + path + '\''
                + ", mediaType=" + mediaType + ", width=" + width + ", height=" + height + ", rotation=" + rotation
                + ", duration=" + duration + ", transitionFilter="
                + transitionFilter + ", afilter=" + afilter + ", currentSeek=" + currentSeek + ", mediaMatrix="
                + mediaMatrix + ", videoCutInterval=" + videoCutInterval + ", trimPath='" + trimPath + '\''
                + ", equalId='" + equalId + '\'' + '}';
    }

    /**
     * 计算真实计算分辨率
     *
     * @return int{width, height}
     */
    public int[] getRealWidthHeight() {
        int[] result = new int[2];
        if (rotation == 90 || rotation == 270) {
            result[0] = this.height;
            result[1] = this.width;
        } else {
            result[1] = this.width;
            result[0] = this.height;
        }
        return result;
    }

    public WeakReference<Bitmap> getThumCache() {
        return thumCache;
    }

    public boolean isThumbEmpty() {
        if (thumCache == null || thumCache.get() == null || thumCache.get() == null || thumCache.get().isRecycled()) {
            return true;
        }
        return false;
    }

    public void setThumCache(WeakReference<Bitmap> thumCache) {
        this.thumCache = thumCache;
    }


    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public long getPagDuration() {
        return pagDuration;
    }

    public void setPagDuration(long pagDuration) {
        this.pagDuration = pagDuration;
    }

    public int getVideoTexture() {
        return videoTexture;
    }

    public void setVideoTexture(int videoTexture) {
        this.videoTexture = videoTexture;
    }

    public long getNoneThemeDuration() {
        return noneThemeDuration;
    }

    public void setNoneThemeDuration(long noneThemeDuration) {
        this.noneThemeDuration = noneThemeDuration;
    }

    public void synNothemeDuration() {
        if (duration != 0) {
            noneThemeDuration = duration;
        }
    }

    public void resumeNothemeDuration() {
        if (noneThemeDuration != 0) {
            setDuration(noneThemeDuration);
        }
    }

    public long getFinalDuration() {
        if (getSpeed() != 0) {
            return (long) (tempDuration / getSpeed());
        }
        return tempDuration;
    }

    public List<PAGFile> getThemePags() {
        return themePags;
    }

    public void setThemePags(List<PAGFile> themePags) {
        this.themePags = themePags;
    }
}
