package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art;


import android.content.Context;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;


/**
 * 三角形马赛克
 * Created by luotangkang on 2019年1月18日19:28:35
 */
public class MagicTrianglesMosaicFilter extends GPUImageFilter {

    public MagicTrianglesMosaicFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.photo_edit_triangles_mosaic));
    }

}
