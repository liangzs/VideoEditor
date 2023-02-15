package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;


import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Common17PreTreatment extends Common8PreTreatment {




    private static final String[][] GIF_SRCS = new String[][]{
            {"/common17_rainbow_dynamic"}
    };

    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return GIF_SRCS;
    }
}
