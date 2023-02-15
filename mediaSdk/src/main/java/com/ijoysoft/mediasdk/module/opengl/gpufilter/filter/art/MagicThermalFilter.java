package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art;

import android.content.Context;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;


/**
 * 热量
 * Created by luotangkang on 2019/10/28.
 */
public class MagicThermalFilter extends GPUImageFilter {

    private int iResolution;

    public MagicThermalFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.photo_edit_thermal));
    }

    @Override
    protected void onInit() {
        super.onInit();
        iResolution = GLES20.glGetUniformLocation(mGLProgId, "iResolution");
    }

    @Override
    public void onDisplaySizeChanged(int width, int height) {
        super.onDisplaySizeChanged(width, height);
        setFloatVec3(iResolution, new float[]{width, height, 1.0F});
    }


}
