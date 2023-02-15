package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art;

import android.content.Context;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;


/**
 * 磨砂玻璃
 * Created by luotangkang on 2019/10/28.
 */
public class MagicFrostedFilter extends GPUImageFilter {

    public MagicFrostedFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.photo_edit_frosted));
    }


}
