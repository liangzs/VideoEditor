package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;


/**
 * 墨水
 * Created by luotangkang on 2019年1月17日11:26:14
 */
public class MagicInkFilter extends GPUImageFilter {

    public MagicInkFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.photo_edit_ink));
    }

}
