package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.MagicDatObstractFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 日落
 */
public class MagicSunsetFilter extends MagicDatObstractFilter {

    public MagicSunsetFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, OpenGlUtils.readShaderFromRawResource(R.raw.sunset),
                "dat/sunset.idx",
                "dat/sunset.dat");
    }
}
