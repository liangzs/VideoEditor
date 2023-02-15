package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art;

import android.content.Context;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;


/**
 * Created by luotangkang on 2019/01/25.
 */
public class MagicBlueOrangeFilter extends GPUImageFilter {

    public MagicBlueOrangeFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.photo_edit_blue_orange));
    }

}
