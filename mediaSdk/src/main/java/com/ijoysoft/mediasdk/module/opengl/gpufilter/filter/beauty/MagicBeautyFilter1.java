package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.beauty;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

public class MagicBeautyFilter1 extends GPUImageFilter {

    private int texelWidthOffset;
    private int texelHeightOffset;

    public MagicBeautyFilter1(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.mediasdk_filter_beauty1));
    }

    protected void onInit() {
        super.onInit();
        texelWidthOffset = GLES20.glGetUniformLocation(getProgram(), "texelWidthOffset");
        texelHeightOffset = GLES20.glGetUniformLocation(getProgram(), "texelHeightOffset");
    }

    private void setTexelSize(final float w, final float h) {
        setFloat(texelWidthOffset, 1F / w);
        setFloat(texelHeightOffset, 1F / h);
    }


    @Override
    protected void onInitialized() {
        super.onInitialized();
    }

}
