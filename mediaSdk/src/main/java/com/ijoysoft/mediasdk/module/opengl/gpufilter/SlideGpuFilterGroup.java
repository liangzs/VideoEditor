package com.ijoysoft.mediasdk.module.opengl.gpufilter;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.EasyGlUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterFactory;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;

/**
 * 滤镜只显示一种滤镜，不做重叠滤镜
 * 对于时间轴来说，一个时间点只显示一个滤镜，除非时间设置错了…
 */
public class SlideGpuFilterGroup {
    private MagicFilterType[] types = new MagicFilterType[] { MagicFilterType.NONE, MagicFilterType.ANTIQUE,
            MagicFilterType.INKWELL, MagicFilterType.BRANNAN, MagicFilterType.N1977, MagicFilterType.SKETCH,
            MagicFilterType.HEFE, MagicFilterType.HUDSON, MagicFilterType.NASHVILLE, MagicFilterType.COOL };
    private GPUImageFilter curFilter;
    private int width, height;
    private int[] fFrame = new int[1];
    private int[] fTexture = new int[1];
    private int curIndex = 0;

    public SlideGpuFilterGroup() {
        initFilter();
    }

    private void initFilter() {
        curFilter = getFilter(getCurIndex());
    }

    private GPUImageFilter getFilter(int index) {
        GPUImageFilter filter = MagicFilterFactory.initFilters(types[index]);
        if (filter == null) {
            filter = new GPUImageFilter();
        }
        return filter;
    }

    public void init() {
        curFilter.init();
    }

    public void onSizeChanged(int width, int height) {
        this.width = width;
        this.height = height;
        GLES20.glDeleteFramebuffers(1, fFrame, 0);
        GLES20.glDeleteTextures(1, fTexture, 0);
        GLES20.glGenFramebuffers(1, fFrame, 0);
        EasyGlUtils.genTexturesWithParameter(1, fTexture, 0, GLES20.GL_RGBA, width, height);
        // fTexture = FBOManager.getInstance().fboTransactionNoRecyle(1);
        onFilterSizeChanged(width, height);
    }

    public void changeFilter(MagicFilterType type) {
        curFilter.destroy();
        curFilter = MagicFilterFactory.initFilters(type);
        if (curFilter == null) {
            curFilter = new GPUImageFilter();
        }
        curFilter.init();
        curFilter.onDisplaySizeChanged(width, height);
        curFilter.onInputSizeChanged(width, height);
    }

    public void changeFilter(GPUImageFilter gpuImageFilter) {
        curFilter.destroy();
        curFilter = gpuImageFilter;
        if (curFilter == null) {
            curFilter = new GPUImageFilter();
        }
        curFilter.init();
        curFilter.onDisplaySizeChanged(width, height);
        curFilter.onInputSizeChanged(width, height);
    }

    private void onFilterSizeChanged(int width, int height) {
        curFilter.onInputSizeChanged(width, height);
        curFilter.onDisplaySizeChanged(width, height);
    }

    public int getOutputTexture() {
        return fTexture[0];
    }

    public void onDrawFrame(int textureId) {
        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
        curFilter.onDrawFrame(textureId);
        EasyGlUtils.unBindFrameBuffer();
    }

    public void onDrawOnlyFrame(int textureId){
        curFilter.onDrawFrame(textureId);
    }


    public void destroy() {
        curFilter.destroy();
    }

    private int getLeftIndex() {
        int leftIndex = curIndex - 1;
        if (leftIndex < 0) {
            leftIndex = types.length - 1;
        }
        return leftIndex;
    }

    private int getRightIndex() {
        int rightIndex = curIndex + 1;
        if (rightIndex >= types.length) {
            rightIndex = 0;
        }
        return rightIndex;
    }

    private int getCurIndex() {
        return curIndex;
    }

    private void increaseCurIndex() {
        curIndex++;
        if (curIndex >= types.length) {
            curIndex = 0;
        }
    }

    private void decreaseCurIndex() {
        curIndex--;
        if (curIndex < 0) {
            curIndex = types.length - 1;
        }
    }

    public interface OnFilterChangeListener {
        void onFilterChange(MagicFilterType type);
    }

    public void onDestroy() {
        curFilter.destroy();
    }
}
