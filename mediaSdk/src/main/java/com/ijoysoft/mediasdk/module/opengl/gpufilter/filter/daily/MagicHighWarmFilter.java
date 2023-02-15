package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily;


import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;


/**
 * 温暖：高
 */
public class MagicHighWarmFilter extends MagicBaseWarmFilter {

    public MagicHighWarmFilter(MagicFilterType magicFilterType) {
        super(magicFilterType);
    }

    @Override
    public void onInitialized() {
        setFloat(mWarmValueUniformLocation, 0.1F);
        super.onInitialized();
    }

}
