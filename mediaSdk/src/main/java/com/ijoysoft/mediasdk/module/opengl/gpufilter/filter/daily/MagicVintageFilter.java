package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.MagicDatObstractFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 典雅
 */
public class MagicVintageFilter extends MagicDatObstractFilter {

    public MagicVintageFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, OpenGlUtils.readShaderFromRawResource(R.raw.mediasdk_filter_vintage),
                "dat/vintage.idx",
                "dat/vintage.dat");
    }
}
