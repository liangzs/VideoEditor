package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art;

import android.content.Context;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

import java.nio.FloatBuffer;

/**
 * 乐高积木
 */
public class MagicLogefiedFilter extends GPUImageFilter {

    private int iResolutionLocation;

    public MagicLogefiedFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.photo_edit_logefied));
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
