package com.ijoysoft.mediasdk.module.opengl.gpufilter;

import android.opengl.GLES20;
import android.os.Build;

import com.ijoysoft.mediasdk.common.utils.EasyGlUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;

/**
 * 滤镜只显示一种滤镜，不做重叠滤镜
 * 对于时间轴来说，一个时间点只显示一个滤镜，除非时间设置错了…
 */
public class SlideGpuFilterGroup {
    private GPUImageFilter curFilter;
    private int width, height;
    private int[] fFrame = new int[1];
    private int[] fTexture = new int[1];
    private int curIndex = 0;

    public SlideGpuFilterGroup() {
        curFilter = new GPUImageFilter();
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


    public void changeFilter(GPUImageFilter gpuImageFilter) {
        if (curFilter != null) {
            curFilter.destroy();
        }
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

    public void onDrawOnlyFrame(int textureId) {
        curFilter.onDrawFrame(textureId);
    }


    public interface OnFilterChangeListener {
        void onFilterChange(MagicFilterType type);
    }

    public void onDestroy() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            curFilter.destroy();
        }
    }

    public void setStrenth(float progress) {
        if (curFilter != null) {
            curFilter.setStrength(progress);
        }
    }
}
