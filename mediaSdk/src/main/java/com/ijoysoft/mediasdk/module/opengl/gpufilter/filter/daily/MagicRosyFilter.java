package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.MagicDatObstractFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 红润
 */
public class MagicRosyFilter extends MagicDatObstractFilter {

    public MagicRosyFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, OpenGlUtils.readShaderFromRawResource(R.raw.mediasdk_filter_rosy),
                "dat/rosy.idx",
                "dat/rosy.dat");
    }
}
