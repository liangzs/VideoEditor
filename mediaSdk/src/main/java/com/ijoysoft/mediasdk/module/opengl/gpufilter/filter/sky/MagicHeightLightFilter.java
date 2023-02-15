package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.sky;

import android.content.Context;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 高亮
 * Created by luotangkang on 2019年1月17日11:26:14
 */
public class MagicHeightLightFilter extends GPUImageFilter {

    public MagicHeightLightFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.photo_edit_height_light));
    }

}
