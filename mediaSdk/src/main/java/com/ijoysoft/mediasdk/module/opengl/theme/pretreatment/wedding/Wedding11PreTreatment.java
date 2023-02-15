package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.wedding;


import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Wedding11PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/wedding11_title1"},
            {"/wedding11_title2"},
            {"/wedding11_title3"},
            {"/wedding11_title4"},
    };


    @Override
    protected String[][] getSources() {
        return SRCS;
    }

    /**
     * Opengl转场时间
     * @return {@link com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment#THEME_TWO_DURATION}
     */
    @Override
    public int createDuration() {
        return THEME_TWO_DURATION;
    }


    private static final String[][] DYNAMIC_MIMAP_BITMAP_SOURCES = new String[][] {
            //全局用的
            {"/wedding11_heart_dynamic"},{},{},{}
    };

    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return DYNAMIC_MIMAP_BITMAP_SOURCES;
    }
}
