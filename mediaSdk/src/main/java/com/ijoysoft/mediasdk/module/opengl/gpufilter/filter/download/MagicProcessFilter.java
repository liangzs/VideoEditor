package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.download;


import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.MagicLookupFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;

/**
 * Lookup table滤镜：冲压
 * Created by luotangkang on 2019年1月17日11:26:14
 */
public class MagicProcessFilter extends MagicLookupFilter {

    public MagicProcessFilter(MagicFilterType magicFilterType, String downPath) {
        super(magicFilterType, downPath);
    }
}
