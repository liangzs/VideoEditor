package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.bw;

import android.content.Context;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 最大值
 * 黑白
 */
public class MagicBWFilter3 extends GPUImageFilter {

    public MagicBWFilter3(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.photo_edit_bw3));
    }

}
