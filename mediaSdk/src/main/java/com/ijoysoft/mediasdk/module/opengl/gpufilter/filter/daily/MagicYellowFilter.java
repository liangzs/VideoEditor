package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily;

import android.content.Context;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;


/**
 * 黄色
 * Created by luotangkang on 2019/10/28.
 */
public class MagicYellowFilter extends GPUImageFilter {

    public MagicYellowFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.photo_edit_yellow));
    }


//    @Override
//    public String getFilterName() {
//        return GpuFilterFactory.NAME_YELLOW;
//    }
}
