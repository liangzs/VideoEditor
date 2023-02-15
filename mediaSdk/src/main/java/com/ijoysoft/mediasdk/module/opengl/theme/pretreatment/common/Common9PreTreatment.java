package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;


import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Common9PreTreatment extends Common6PreTreatment {







    private static final String[][] GIF_SRCS = new String[][]{
            {"/common9_sound_line_dynamic", "/common9_sun_dynamic"}
    };

    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return GIF_SRCS;
    }
}
