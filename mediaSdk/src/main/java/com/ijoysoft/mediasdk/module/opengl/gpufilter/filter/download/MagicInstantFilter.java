package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.download;


import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.MagicLookupFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;

/**
 * Lookup table滤镜：即时出相
 * Created by luotangkang on 2019年1月17日11:26:14
 */
public class MagicInstantFilter extends MagicLookupFilter {

    public MagicInstantFilter(MagicFilterType magicFilterType, String downPath) {
        super(magicFilterType, downPath);
    }
}
