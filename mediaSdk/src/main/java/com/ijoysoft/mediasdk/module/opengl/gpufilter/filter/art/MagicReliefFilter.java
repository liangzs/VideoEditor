package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art;

import android.content.Context;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

import java.nio.FloatBuffer;

/**
 * 浮雕
 * Created by luotangkang on 2019年1月19日11:00:30
 */
public class MagicReliefFilter extends GPUImageFilter {

    private int iResolutionLocation;

    public MagicReliefFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.photo_edit_relief));
    }

    @Override
    protected void onDrawArraysPre() {
        GLES20.glUniform3fv(iResolutionLocation, 1,
                FloatBuffer.wrap(new float[]{(float) mOutputWidth, (float) mOutputHeight, 1.0f}));
    }

    @Override
    protected void onInit() {
        super.onInit();
        iResolutionLocation = GLES20.glGetUniformLocation(mGLProgId, "iResolution");
    }

}
