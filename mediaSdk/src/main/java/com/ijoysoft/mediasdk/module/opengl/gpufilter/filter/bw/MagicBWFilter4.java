package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.bw;


import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.MagicLookupFilter64;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 可爱
 * Created by luotangkang on 2019年1月17日11:26:14
 */
public class MagicBWFilter4 extends MagicLookupFilter64 {

    public MagicBWFilter4(MagicFilterType magicFilterType) {
        super(magicFilterType, "filter/lut_black_white.png");
    }

}
