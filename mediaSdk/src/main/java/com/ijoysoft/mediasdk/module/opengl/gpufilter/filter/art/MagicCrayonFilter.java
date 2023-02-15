package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art;

import android.content.Context;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

public class MagicCrayonFilter extends GPUImageFilter {

    private int mSingleStepOffsetLocation;

    public MagicCrayonFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.photo_edit_crayon));
    }

    @Override
    protected void onInit() {
        super.onInit();
        mSingleStepOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "singleStepOffset");
    }


    private void setTexelSize(final float w, final float h) {
        setFloatVec2(mSingleStepOffsetLocation, new float[]{1.0f / w, 1.0f / h});
    }


    @Override
    public void onDisplaySizeChanged(int width, int height) {
        super.onDisplaySizeChanged(width, height);
        setTexelSize(width, height);
    }

}
