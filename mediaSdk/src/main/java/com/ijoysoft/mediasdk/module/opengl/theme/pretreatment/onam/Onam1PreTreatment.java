package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.onam;

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

/**
 * 欧南节1
 */
public class Onam1PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/onam1_circle"},
            {"/onam1_top_padding", "/onam1_lt_wave", "/onam1_rt_wave", "/onam1_double_light_table", "/onam1_light_table"},
            {"/onam1_bottom_padding"},
            {"/onam1_padding", "/onam1_circle"},
            {"/onam1_top_tie", "/onam1_lb_wave", "/onam1_rb_wave", "/onam1_orange_flower", "/onam1_pink_flower", "/onam1_green_flower", "/onam1_white_flower"},
            {"/onam1_title", "/onam1_circle"},
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
