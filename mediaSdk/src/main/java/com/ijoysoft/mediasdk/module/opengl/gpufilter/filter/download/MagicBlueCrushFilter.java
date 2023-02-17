package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.download;


import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.MagicLookupFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;

/**
 * Lookup table滤镜：蓝调
 * Created by luotangkang on 2019年1月17日11:26:14
 */
public class MagicBlueCrushFilter extends MagicLookupFilter {

    public MagicBlueCrushFilter(MagicFilterType magicFilterType, String downPath) {
        super(magicFilterType, downPath);
    }

}