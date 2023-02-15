package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.bw;

import android.content.Context;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.MagicLookupFilter64;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 可爱
 * Created by luotangkang on 2019年1月17日11:26:14
 */
public class MagicSteelFilter extends MagicLookupFilter64 {

    public MagicSteelFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, R.raw.lut_steel);
    }

}
