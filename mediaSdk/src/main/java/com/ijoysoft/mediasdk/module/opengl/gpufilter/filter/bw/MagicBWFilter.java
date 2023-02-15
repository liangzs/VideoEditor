package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.bw;

import android.content.Context;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 0.3,0.59,0.11
 * 黑白
 */
public class MagicBWFilter extends GPUImageFilter {

    public MagicBWFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.photo_edit_bw));
    }



//    @Override
//    public String getFilterName() {
//        return GpuFilterFactory.NAME_BW;
//    }
}
