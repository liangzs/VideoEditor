package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.popular;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.MagicDatObstractFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 小甜心
 */
public class MagicSweetyFilter extends MagicDatObstractFilter {

    public MagicSweetyFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, OpenGlUtils.readShaderFromRawResource(R.raw.mediasdk_filter_sweety),
                "dat/sweety.idx",
                "dat/sweety.dat");
    }
}
