package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.bw;

import android.content.Context;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 保留色彩
 * Created by luotangkang on 2019/6/13.
 */
public class GpuSelectiveColorFilter extends GPUImageFilter {

    public GpuSelectiveColorFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.photo_edit_selective_color_filter));
    }
}
