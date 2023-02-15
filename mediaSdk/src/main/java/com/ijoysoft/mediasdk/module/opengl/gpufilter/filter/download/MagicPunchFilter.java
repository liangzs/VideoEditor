package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.download;


import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.MagicLookupFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;

/**
 * Lookup table滤镜：低光
 * Created by luotangkang on 2019年1月17日11:26:14
 */
public class MagicPunchFilter extends MagicLookupFilter {

    public MagicPunchFilter(MagicFilterType magicFilterType, String downPath) {
        super(magicFilterType, downPath);
    }
}
