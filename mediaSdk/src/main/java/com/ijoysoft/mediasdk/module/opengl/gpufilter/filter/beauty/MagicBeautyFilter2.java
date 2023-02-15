package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.beauty;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;


public class MagicBeautyFilter2 extends GPUImageFilter {
    private final int level;
    private int mSingleStepOffsetLocation;
    private int mParamsLocation;

    public MagicBeautyFilter2(MagicFilterType magicFilterType, int level) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.mediasdk_filter_beauty2));
        this.level = level;
    }

    protected void onInit() {
        super.onInit();
        mSingleStepOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "singleStepOffset");
        mParamsLocation = GLES20.glGetUniformLocation(getProgram(), "params");
        setBeautyLevel(level);
    }

    private void setTexelSize(final float w, final float h) {
        setFloatVec2(mSingleStepOffsetLocation, new float[]{2.0f / w, 2.0f / h});
    }


    public void setBeautyLevel(int level) {
        setInteger(mParamsLocation, level);
    }

    @Override
    protected void onInitialized() {
        super.onInitialized();
    }


//    @Override
//    public String getFilterName() {
//        if (thumbDrawableId == R.drawable.filter_thumb_people2) {
//            return GpuFilterFactory.NAME_BEAUTY2;
//        } else {
//            return GpuFilterFactory.NAME_BEAUTY3;
//        }
//    }
}

