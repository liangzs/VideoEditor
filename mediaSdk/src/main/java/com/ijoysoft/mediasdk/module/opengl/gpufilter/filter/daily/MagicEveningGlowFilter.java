package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily;

import android.content.Context;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;


/**
 * 晚霞
 * Created by luotangkang on 2019/10/28.
 */
public class MagicEveningGlowFilter extends GPUImageFilter {

    public MagicEveningGlowFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.photo_edit_evening_glow));
    }


//    @Override
//    public String getFilterName() {
//        return GpuFilterFactory.NAME_EVENING_GLOW;
//    }
}
