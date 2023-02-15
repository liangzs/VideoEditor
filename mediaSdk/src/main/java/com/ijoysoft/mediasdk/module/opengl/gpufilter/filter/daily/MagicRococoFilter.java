package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.MagicDatObstractFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 洛可可
 */
public class MagicRococoFilter extends MagicDatObstractFilter {

    public MagicRococoFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, OpenGlUtils.readShaderFromRawResource(R.raw.mediasdk_filter_rococo),
                "dat/rococo.idx",
                "dat/rococo.dat");
    }
}
