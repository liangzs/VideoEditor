package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.sky;

import android.content.Context;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;


/**
 * 雾(混合灰色)
 * Created by luotangkang on 2019/10/28.
 */
public class MagicFogFilter extends GPUImageFilter {

    public MagicFogFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.photo_edit_fog));
    }

}
