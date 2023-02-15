package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.sky;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

import java.nio.FloatBuffer;

public class MagicContractFilter extends GPUImageFilter {

    private int iResolutionLocation;

    public MagicContractFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.mediasdk_filter_contract));
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
