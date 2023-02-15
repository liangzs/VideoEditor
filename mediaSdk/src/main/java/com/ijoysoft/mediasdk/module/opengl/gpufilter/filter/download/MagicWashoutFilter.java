package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.download;


import android.content.Context;

import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.MagicLookupFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;

/**
 * Lookup table滤镜：拿铁
 * Created by luotangkang on 2019年1月17日11:26:14
 */
public class MagicWashoutFilter extends MagicLookupFilter {

    public MagicWashoutFilter(MagicFilterType magicFilterType, String downPath) {
        super(magicFilterType, downPath);
    }

}
