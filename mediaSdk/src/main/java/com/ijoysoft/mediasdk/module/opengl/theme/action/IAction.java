package com.ijoysoft.mediasdk.module.opengl.theme.action;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.entity.BGInfo;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;

import java.util.List;

public interface IAction {
    void prepare();

    void drawFrame();


    /**
     * 视频渲染的补充
     */
    default void drawVideoFrame(int videoTeture) {

    }

    void drawMatrixFrame();

    /**
     * 计算出在当前片段的节点
     *
     * @param currentDuration
     */
    void seek(int currentDuration);

    void drawWiget();

    void initWidget();

    void drawLast();

    void init(Bitmap bitmap, int width, int height);


    void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height);

    void init(MediaItem mediaItem, int width, int height);

    ActionStatus getStatus();

    //添加滤镜模块
    void initFilter(MagicFilterType filterType, int width, int height);

    void onDestroy();

    /**
     * 获取当前纹理
     *
     * @return
     */
    int getTexture();

    //最后停留的角度
    int getConor();

    void setPreTeture(List<Integer> listTexture, List<Integer> listCornor, List<float[]> pos, List<GPUImageFilter> listAfilters);

    void setPreTeture(int texture, AFilter aFilter);

    void setPreTeture(int texture);

    void setPreTeture(int texture, int frame, int frameTexture);

    float[] getPos();

    float getEnterProgress();

    int getEnterTime();

    //改变frameIndex进度

    void drawFrameIndex();

    void setFilter(GPUImageFilter filter);

    void drawFramePreview();

    /**
     * 获取总时间
     *
     * @return
     */
    int getTotalTime();


    void updateVideoTexture(int texture);

    default void setIsPureColor(BGInfo bgInfo) {
    }
}
