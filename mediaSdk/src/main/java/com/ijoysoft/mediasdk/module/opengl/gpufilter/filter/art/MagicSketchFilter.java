package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 复古
 */
public class MagicSketchFilter extends GPUImageFilter {
    private int mGLStrengthLocation;
    private int mGLSingleStepLocation;

    public MagicSketchFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.mediasdk_filter_sketch));
    }


    public void onInit() {
        super.onInit();
        mGLStrengthLocation = GLES20.glGetUniformLocation(mGLProgId, "strength");
        mGLSingleStepLocation = GLES20.glGetUniformLocation(mGLProgId, "singleStepOffset");
    }

    public void onInitialized() {
        super.onInitialized();
        setFloat(mGLStrengthLocation, 1.0f);
        setFloatVec2(mGLSingleStepLocation, new float[]{1.0f / ConstantMediaSize.showViewWidth, 1.0f / ConstantMediaSize.showViewHeight});
    }
}
