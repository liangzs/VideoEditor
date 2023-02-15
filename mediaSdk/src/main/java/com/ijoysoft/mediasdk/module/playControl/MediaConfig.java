package com.ijoysoft.mediasdk.module.playControl;

import android.os.Parcel;
import android.os.Parcelable;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.BGInfo;
import com.ijoysoft.mediasdk.module.entity.FrameType;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.entity.ResolutionType;
import com.ijoysoft.mediasdk.module.opengl.InnerBorder;
import com.ijoysoft.mediasdk.module.opengl.particle.GlobalParticles;

/**
 * 参数配置
 */
public class MediaConfig implements Parcelable {
    private RatioType ratioType;
    private int duration;
    private String projectId;
    private boolean isFading;
    private int currentDuration;
    private ResolutionType resolutionType;
    private FrameType frameType;
    private BGInfo mBGInfo;

    /**
     * 播放默认是裁剪，倒转物理文件，逻辑变速播放
     */
    //是否自动播放,默认false
    private boolean autoPlay = false;
    //是否原路径播放
    private boolean originPathPlay = false;
    //是否无变度播放(用于片段裁预览编辑）
    private boolean noSpeedPlay = false;

    private boolean isTriming;

    /**
     * 额外粒子
     */
    private GlobalParticles globalParticles = GlobalParticles.NONE;

    /**
     * 边框
     */
    private InnerBorder innerBorder = InnerBorder.Companion.getNONE();

    public RatioType getRatioType() {
        return ratioType;
    }

    public void setRatioType(RatioType ratioType) {
        this.ratioType = ratioType;
    }

    public void setResolutionType(ResolutionType resolutionType) {
        this.resolutionType = resolutionType;
    }

    public boolean isFading() {
        return isFading;
    }

    public void setFading(boolean fading) {
        isFading = fading;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCurrentDuration() {
        return currentDuration;
    }

    public void setCurrentDuration(int currentDuration) {
        this.currentDuration = currentDuration;
    }

    public boolean isTriming() {
        return isTriming;
    }

    public void setTriming(boolean triming) {
        isTriming = triming;
    }

    public BGInfo getBGInfo() {
        if (mBGInfo == null) {
            mBGInfo= new BGInfo();
        }
        return mBGInfo;
    }

    public boolean isAutoPlay() {
        return autoPlay;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    public boolean isOriginPathPlay() {
        return originPathPlay;
    }

    public void setOriginPathPlay(boolean originPathPlay) {
        this.originPathPlay = originPathPlay;
    }

    public boolean isNoSpeedPlay() {
        return noSpeedPlay;
    }

    public void setNoSpeedPlay(boolean noSpeedPlay) {
        this.noSpeedPlay = noSpeedPlay;
    }

    public void setBGInfo(BGInfo BGInfo) {
        mBGInfo = BGInfo;
    }

    /**
     * 选中的码率
     *
     * @return
     */
    public int getMatchRate() {
        if (resolutionType == null) {
            return 0;
        }
        return resolutionType.getMatchRate(frameType);
    }


    private MediaConfig(Builder builder) {
        this.ratioType = builder.ratioType;
        this.duration = builder.duration;
        this.projectId = builder.projectId;
        this.isFading = builder.isFading;
        this.mBGInfo = builder.mBGInfo;
        this.currentDuration = builder.currentDuration;
        this.resolutionType = builder.resolutionType;
        this.frameType = builder.frameType;
        if (resolutionType == null) {
            this.resolutionType = ResolutionType._720p_;
        }
        this.originPathPlay=builder.originPathPlay;
        if (frameType == null) {
            this.frameType = FrameType._25F;
        }
    }

    /**
     * 获取导出的width，根据definiType取出选定的宽高
     * 默认1:1,合成失败后做降级处理，默然使用480作为输出
     * <p>
     * 以最小边做720p，1080p的边
     *
     * @return
     */
    public int[] getExportWidth(boolean small) {
        if (small) {
            resolutionType = ResolutionType._480p_;
        }
        //横向
        if (ratioType.isWidthLargeHeight()) {
            int height = resolutionType.getWidth();
            float wDevideH = (ratioType.getWidthRatio() * 1f) / (ratioType.getHeightRatio() * 1f);
            int width = (int) (height * wDevideH);
            width = width % 2 == 1 ? width + 1 : width;
            LogUtils.i("getExportWidth", "width:" + width + ",height:" + height);
            return new int[]{width, height};
        }
        //竖向
        int width = resolutionType.getWidth();
        float hDevideW = (ratioType.getHeightRatio() * 1f) / (ratioType.getWidthRatio() * 1f);
        int height = (int) (width * hDevideW);
        height = height % 2 == 1 ? height + 1 : height;
        LogUtils.i("getExportWidth", "width:" + width + ",height:" + height);
        return new int[]{width, height};
    }


    /**
     * builder
     */
    public static class Builder {
        private RatioType ratioType;
        private int duration;
        private String projectId;
        private boolean isFading;
        private int currentDuration;
        private ResolutionType resolutionType;
        private FrameType frameType;
        private BGInfo mBGInfo;
        private boolean autoPlay = false;
        private boolean originPathPlay = false;
        private boolean noSpeedPlay = false;

        public Builder() {

        }

        public Builder setRatioType(RatioType ratioType) {
            this.ratioType = ratioType;
            return this;
        }


        public Builder setCurrentDuration(int currentDuration) {
            this.currentDuration = currentDuration;
            return this;
        }

        public Builder setAutoPlay(boolean autoPlay) {
            this.autoPlay = autoPlay;
            return this;
        }

        public Builder setOriginPathPlay(boolean originPathPlay) {
            this.originPathPlay = originPathPlay;
            return this;
        }

        public Builder setNoSpeedPlay(boolean noSpeedPlay) {
            this.noSpeedPlay = noSpeedPlay;
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder setProjectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder setFading(boolean fading) {
            isFading = fading;
            return this;
        }


        public Builder setResolutionType(ResolutionType resolutionType) {
            this.resolutionType = resolutionType;
            return this;
        }

        public Builder setFrameType(FrameType frameType) {
            this.frameType = frameType;
            return this;
        }


        public Builder setBGInfo(BGInfo BGInfo) {
            mBGInfo = BGInfo;
            return this;

        }

        public MediaConfig build() {
            return new MediaConfig(this);
        }

    }

    public ResolutionType getResolutionType() {
        return resolutionType;
    }

    public FrameType getFrameType() {
        return frameType;
    }

    public GlobalParticles getGlobalParticles() {
        return globalParticles;
    }

    public void setGlobalParticles(GlobalParticles globalParticles) {
        this.globalParticles = globalParticles;
    }


    public InnerBorder getInnerBorder() {
        return innerBorder;
    }

    public void setInnerBorder(InnerBorder innerBorder) {
        this.innerBorder = innerBorder;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ratioType == null ? -1 : this.ratioType.ordinal());
        dest.writeInt(this.duration);
        dest.writeString(this.projectId);
        dest.writeByte(this.isFading ? (byte) 1 : (byte) 0);
        dest.writeInt(this.currentDuration);
        dest.writeInt(this.resolutionType == null ? -1 : this.resolutionType.ordinal());
        dest.writeInt(this.frameType == null ? -1 : this.frameType.ordinal());
        dest.writeSerializable(this.mBGInfo);
        dest.writeByte(this.autoPlay ? (byte) 1 : (byte) 0);
        dest.writeByte(this.originPathPlay ? (byte) 1 : (byte) 0);
        dest.writeByte(this.noSpeedPlay ? (byte) 1 : (byte) 0);
        dest.writeInt(this.globalParticles == null ? -1 : this.globalParticles.ordinal());
        dest.writeParcelable(this.innerBorder, flags);
    }

    public void readFromParcel(Parcel source) {
        int tmpRatioType = source.readInt();
        this.ratioType = tmpRatioType == -1 ? null : RatioType.values()[tmpRatioType];
        this.duration = source.readInt();
        this.projectId = source.readString();
        this.isFading = source.readByte() != 0;
        this.currentDuration = source.readInt();
        int tmpResolutionType = source.readInt();
        this.resolutionType = tmpResolutionType == -1 ? null : ResolutionType.values()[tmpResolutionType];
        int tmpFrameType = source.readInt();
        this.frameType = tmpFrameType == -1 ? null : FrameType.values()[tmpFrameType];
        this.mBGInfo = (BGInfo) source.readSerializable();
        this.autoPlay = source.readByte() != 0;
        this.originPathPlay = source.readByte() != 0;
        this.noSpeedPlay = source.readByte() != 0;
        int tmpGlobalParticles = source.readInt();
        this.globalParticles = tmpGlobalParticles == -1 ? null : GlobalParticles.values()[tmpGlobalParticles];
        this.innerBorder = source.readParcelable(InnerBorder.class.getClassLoader());
    }

    protected MediaConfig(Parcel in) {
        int tmpRatioType = in.readInt();
        this.ratioType = tmpRatioType == -1 ? null : RatioType.values()[tmpRatioType];
        this.duration = in.readInt();
        this.projectId = in.readString();
        this.isFading = in.readByte() != 0;
        this.currentDuration = in.readInt();
        int tmpResolutionType = in.readInt();
        this.resolutionType = tmpResolutionType == -1 ? null : ResolutionType.values()[tmpResolutionType];
        int tmpFrameType = in.readInt();
        this.frameType = tmpFrameType == -1 ? null : FrameType.values()[tmpFrameType];
        this.mBGInfo = (BGInfo) in.readSerializable();
        this.autoPlay = in.readByte() != 0;
        this.originPathPlay = in.readByte() != 0;
        this.noSpeedPlay = in.readByte() != 0;
        int tmpGlobalParticles = in.readInt();
        this.globalParticles = tmpGlobalParticles == -1 ? null : GlobalParticles.values()[tmpGlobalParticles];
        this.innerBorder = in.readParcelable(InnerBorder.class.getClassLoader());
    }

    public static final Creator<MediaConfig> CREATOR = new Creator<MediaConfig>() {
        @Override
        public MediaConfig createFromParcel(Parcel source) {
            return new MediaConfig(source);
        }

        @Override
        public MediaConfig[] newArray(int size) {
            return new MediaConfig[size];
        }
    };
}
