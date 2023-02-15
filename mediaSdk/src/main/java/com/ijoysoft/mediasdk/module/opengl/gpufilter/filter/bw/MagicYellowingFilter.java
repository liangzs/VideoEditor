package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.bw;

import android.content.Context;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 泛黄
 * Created by luotangkang on 2019/10/28.
 */
public class MagicYellowingFilter extends GPUImageFilter {

    public MagicYellowingFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.photo_edit_yellowing));
    }
}
