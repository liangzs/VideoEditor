package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily;

import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;


/**
 * 温暖：正常
 */
public class MagicNormalWarmFilter extends MagicBaseWarmFilter {

    public MagicNormalWarmFilter(MagicFilterType magicFilterType) {
        super(magicFilterType);
    }

    @Override
    public void onInitialized() {
        setFloat(mWarmValueUniformLocation, 0.6F);
        super.onInitialized();
    }

//    @Override
//    public String getFilterName() {
//        return GpuFilterFactory.NAME_NORMAL_WARM;
//    }
}
