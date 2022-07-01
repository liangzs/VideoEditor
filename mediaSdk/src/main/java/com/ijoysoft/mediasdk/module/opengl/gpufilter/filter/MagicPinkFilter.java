package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.MagicDatObstractFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 粉嫩(⊙o⊙)…
 */
public class MagicPinkFilter extends MagicDatObstractFilter {

    public MagicPinkFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, OpenGlUtils.readShaderFromRawResource(R.raw.pink),
                "dat/pink.idx",
                "dat/pink.dat");
    }
}
