package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 玻璃破碎了
 */
public class MagicCrackedFilter extends GPUImageFilter {
    private int iResolutionLocation;

    public MagicCrackedFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.cracked));
    }

    @Override
    protected void onInitialized() {
        super.onInitialized();
        iResolutionLocation = GLES20.glGetUniformLocation(mGLProgId, "iResolution");
        setFloatVec3(iResolutionLocation, new float[]{(float) ConstantMediaSize.showViewWidth, (float) ConstantMediaSize.showViewHeight, 1.0f});
    }
}
