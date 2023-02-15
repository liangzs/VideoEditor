package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.onam;

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

/**
 * 欧南节2
 */
public class Onam2PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/onam2_ship", "/onam2_sun", "/onam2_sea"},
            {"/onam2_lt_cloud", "/onam2_rt_cloud", "/onam2_sea_gulls", "/onam2_sea_gulls"},
            {"/onam2_title", "/onam2_sun"},
            {"/onam2_lb_cloud", "/onam2_rb_cloud", "/onam2_ship"},
            {"/onam2_ship", "/onam2_lt_cloud", "/onam2_rt_cloud", "/onam2_sea"},
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
}
