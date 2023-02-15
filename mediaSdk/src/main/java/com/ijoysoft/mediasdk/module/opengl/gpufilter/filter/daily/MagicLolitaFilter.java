package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.MagicDatObstractFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 洛丽塔
 */
public class MagicLolitaFilter extends MagicDatObstractFilter {

    public MagicLolitaFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, OpenGlUtils.readShaderFromRawResource(R.raw.mediasdk_filter_lolita),
                "dat/lolita.idx",
                "dat/lolita.dat");
    }
}
