package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.beauty;

import android.content.Context;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;


/**
 * 默认0.33f, 0.63f, 0.4F, 0.35F
 * 美肤 ***
 */
public class MagicBeautyFilter extends GPUImageFilter {
    private final float value1;
    private final float value2;
    private final float value3;
    private final float value4;
    private int mSingleStepOffsetLocation;
    protected int mParamsLocation;

    public MagicBeautyFilter(MagicFilterType magicFilterType, float value1, float value2, float value3, float value4) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.beautify_fragment_low));
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }

    @Override
    protected void onInit() {
        super.onInit();
        mSingleStepOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "singleStepOffset");
        mParamsLocation = GLES20.glGetUniformLocation(getProgram(), "params");
        setFloatVec4(mParamsLocation, new float[]{value1, value2, value3, value4});
    }

    @Override
    public void onDisplaySizeChanged(final int width, final int height) {
        super.onDisplaySizeChanged(width, height);
        setTexelSize(width, height);
    }

    @Override
    protected void onInitialized() {
        super.onInitialized();
    }


    private void setTexelSize(final float w, final float h) {
        setFloatVec2(mSingleStepOffsetLocation, new float[]{2.0f / w, 2.0f / h});
    }

//    @Override
//    public String getFilterName() {
//        if (thumbDrawableId == R.drawable.filter_thumb_people4) {
//            return GpuFilterFactory.NAME_BEAUTY4;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_people5) {
//            return GpuFilterFactory.NAME_BEAUTY5;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_people6) {
//            return GpuFilterFactory.NAME_BEAUTY6;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_people7) {
//            return GpuFilterFactory.NAME_BEAUTY7;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_people8) {
//            return GpuFilterFactory.NAME_BEAUTY8;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_people9) {
//            return GpuFilterFactory.NAME_BEAUTY9;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_people10) {
//            return GpuFilterFactory.NAME_BEAUTY10;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_people11) {
//            return GpuFilterFactory.NAME_BEAUTY11;
//        } else {
//            return GpuFilterFactory.NAME_BEAUTY12;
//        }
//    }
}
