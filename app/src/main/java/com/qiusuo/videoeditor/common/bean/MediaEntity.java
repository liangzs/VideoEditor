package com.qiusuo.videoeditor.common.bean;

import android.media.MediaMetadataRetriever;
import android.os.Parcel;
import android.os.Parcelable;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.ThreadPoolMaxThread;
import com.ijoysoft.mediasdk.module.entity.DurationInterval;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaType;
import com.ijoysoft.mediasdk.module.entity.VideoMediaItem;

import java.io.Serializable;
public class MediaEntity implements Serializable, Cloneable {
    /**
     * 图片类型
     */
    public static final int TYPE_IMAGE = 1;
    /**
     * 视频类型
     */
    public static final int TYPE_VIDEO = 2;

    /*
     * music
     */
    public static final int TYPE_MUSIC = 3;
    /**
     * 唯一标示
     */
    public int id;

    /**
     * 文件名
     */
    public String title;

    public String subscribeText;
    /**
     * 文件路径
     */
    public String path;
    /**
     * 文字模板
     */
    public int textType;
    /**
     * 主标题
     */
    public String mainText = "";
    /**
     * 副标题
     */
    public String extraText = "";

    public int width;

    public int height;
    /**
     * 排序
     */
    public int order;

    /**
     * 时长
     */
    public long duration;
    public long tempDuration;

    /**
     * 大小
     */
    public String size;

    public String mimeType;


    /**
     * 文件类型，图片 视频 音乐
     */

    public int type;

    public int isMusic; // 0 1 1才会在音乐播放器上显示 挑选为1 的显示

    public long dateTaken; // 拍摄时间 /ms 如果为空则等于修改时间
    public long dateAdd; // 添加到数据库的时间 /s
    public long dateModify;// 修改时间 /s
    public int rotation;

    public boolean isSelected = false;
    public long originDuration;
    public String originPath;
    public long lastModify;//本地数据库最后更新时间
    public int bucketId;//相册id
    public String bucketName;//相册名
    public int orientation;//旋转角度
    //中文A-Z排序用到
    public char pinyinChar;
    /**
     * @see MediaItem#getVideoCutInterval()
     */
    private DurationInterval videoCutInterval;


    /**
     * @see MediaItem#getTrimPath()
     */
    private String trimPath;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        MediaEntity entity = (MediaEntity) obj;
        if (path != null && ((MediaEntity) obj).getPath() != null &&
                path.equals(((MediaEntity) obj).getPath()) && id == ((MediaEntity) obj).getId()) {
            return true;
        }
        return false;
    }

    @Override
    public Object clone() {
        MediaEntity mediaTemp = null;
        try {
            mediaTemp = (MediaEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return mediaTemp;
    }


    public boolean isImage() {
        return type == TYPE_IMAGE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubscribeText() {
        return subscribeText;
    }

    public void setSubscribeText(String subscribeText) {
        this.subscribeText = subscribeText;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTextType() {
        return textType;
    }

    public void setTextType(int textType) {
        this.textType = textType;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getExtraText() {
        return extraText;
    }

    public void setExtraText(String extraText) {
        this.extraText = extraText;
    }

    public int getWidth() {
        return width;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getTempDuration() {
        return tempDuration;
    }

    public void setTempDuration(long tempDuration) {
        this.tempDuration = tempDuration;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsMusic() {
        return isMusic;
    }

    public void setIsMusic(int isMusic) {
        this.isMusic = isMusic;
    }

    public long getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(long dateTaken) {
        this.dateTaken = dateTaken;
    }


    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public long getOriginDuration() {
        return originDuration;
    }

    public void setOriginDuration(long originDuration) {
        this.originDuration = originDuration;
    }

    public String getOriginPath() {
        return originPath;
    }

    public void setOriginPath(String originPath) {
        this.originPath = originPath;
    }

    public long getLastModify() {
        return lastModify;
    }

    public void setLastModify(long lastModify) {
        this.lastModify = lastModify;
    }

    public int getBucketId() {
        return bucketId;
    }

    public void setBucketId(int bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }


    /**
     * see com.ijoysoft.videoeditor.activity.PreviewSingleMediaEntityActivity
     * 反序列化
     * 目前仅需用到这些参数，要继续使用务必手动添加
     *
     * @return
     */
    public static final Parcelable.Creator<MediaEntity> CREATOR = new Parcelable.Creator<MediaEntity>() {
        @Override
        public MediaEntity createFromParcel(Parcel in) {
            MediaEntity MediaEntity = new MediaEntity();
            MediaEntity.setId(in.readInt());
            MediaEntity.setType(in.readInt());
            MediaEntity.setPath(in.readString());
            return MediaEntity;
        }

        @Override
        public MediaEntity[] newArray(int size) {
            return new MediaEntity[size];
        }
    };

////    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    /**
//     * see com.ijoysoft.videoeditor.activity.PreviewSingleMediaEntityActivity
//     * 目前仅需用到这些参数，要继续使用务必手动添加
//     * 序列化过程
//     * @return
//     */
////    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(id);
//        dest.writeInt(type);
//        dest.writeString(path == null ? "" : path);
//    }
//
//    /**
//     * 以Serializable返回自己
//     * @return (Serializable)this;
//     */
//    public Serializable getAsSerializable() {
//        return this;
//    }
//
//    /**
//     * 以Parcelable返回自己
//     * @return (Parcelable)this;
//     */
//    public Parcelable getAsParcelable() {
//        return this;
//    }


    public void setType(MediaType mediaType) {
        switch (mediaType) {
            case VIDEO: {
                this.type = TYPE_VIDEO;
            }
            break;
            case AUDIO: {
                this.type = TYPE_MUSIC;
            }
            break;
            case PHOTO: {
                this.type = TYPE_IMAGE;
            }
            break;
        }
    }



    public void loadRotationCurrentThread() {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(path);
            if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION) != null) {
                rotation = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (retriever != null) {
                retriever.release();
            }
        }
    }

    public void loadRotation() {
        //获取旋转比例
        ThreadPoolMaxThread.getInstance().execute(() -> {
            loadRotationCurrentThread();
        });
    }




    public DurationInterval getVideoCutInterval() {
        return videoCutInterval;
    }

    public String getTrimPath() {
        return trimPath;
    }


    /**
     * 获取mediaType类型
     *
     * @return
     */
    public MediaType getMediaType() {
        if (type == TYPE_VIDEO) {
            return MediaType.VIDEO;
        } else if (type == TYPE_IMAGE) {
            return MediaType.PHOTO;
        } else if (type == TYPE_MUSIC) {
            return MediaType.AUDIO;
        }
        return MediaType.PHOTO;
    }

    /**
     * 获取int类型
     *
     * @param mediaItem
     * @return
     */
    public static int getMediaEntityType(MediaItem mediaItem) {
        if (mediaItem.getMediaType() == MediaType.PHOTO) {
            return TYPE_IMAGE;
        } else if (mediaItem.getMediaType() == MediaType.VIDEO) {
            return TYPE_VIDEO;
        } else {
            return TYPE_MUSIC;
        }
    }

    public void setTrimPath(String trimPath) {
        this.trimPath = trimPath;
    }

    public void setVideoCutInterval(DurationInterval videoCutInterval) {
        this.videoCutInterval = videoCutInterval;
    }

}

