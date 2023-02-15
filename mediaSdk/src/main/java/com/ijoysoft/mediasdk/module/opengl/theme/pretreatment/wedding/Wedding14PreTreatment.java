package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.wedding;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Wedding14PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/wedding14_top_9_16", "/wedding14_title1", "/wedding14_ring", "/wedding14_pink_gradient", "/wedding14_yellow_gradient", "/wedding14_red_gradient", "/wedding14_yellow_gradient", "/wedding14_cyan_gradient", "/wedding14_blue_gradient"},
            {"/wedding14_top_9_16", "/wedding14_title2", "/wedding14_ring", "/wedding14_pink_gradient", "/wedding14_yellow_gradient", "/wedding14_red_gradient", "/wedding14_yellow_gradient", "/wedding14_cyan_gradient", "/wedding14_blue_gradient"},
            {"/wedding14_top_9_16", "/wedding14_title3", "/wedding14_ring", "/wedding14_pink_gradient", "/wedding14_yellow_gradient", "/wedding14_red_gradient", "/wedding14_yellow_gradient", "/wedding14_cyan_gradient", "/wedding14_blue_gradient"},
            {"/wedding14_top_9_16", "/wedding14_title4", "/wedding14_ring", "/wedding14_pink_gradient", "/wedding14_yellow_gradient", "/wedding14_red_gradient", "/wedding14_yellow_gradient", "/wedding14_cyan_gradient", "/wedding14_blue_gradient"},
            {"/wedding14_top_9_16", "/wedding14_title5", "/wedding14_ring", "/wedding14_pink_gradient", "/wedding14_yellow_gradient", "/wedding14_red_gradient", "/wedding14_yellow_gradient", "/wedding14_cyan_gradient", "/wedding14_blue_gradient"},
    };

    private static final String[][] SRCS_16_9 = new String[][]{
            {"/wedding14_top_16_9", "/wedding14_title1", "/wedding14_ring", "/wedding14_pink_gradient", "/wedding14_yellow_gradient", "/wedding14_red_gradient", "/wedding14_yellow_gradient", "/wedding14_cyan_gradient", "/wedding14_blue_gradient"},
            {"/wedding14_top_16_9", "/wedding14_title2", "/wedding14_ring", "/wedding14_pink_gradient", "/wedding14_yellow_gradient", "/wedding14_red_gradient", "/wedding14_yellow_gradient", "/wedding14_cyan_gradient", "/wedding14_blue_gradient"},
            {"/wedding14_top_16_9", "/wedding14_title3", "/wedding14_ring", "/wedding14_pink_gradient", "/wedding14_yellow_gradient", "/wedding14_red_gradient", "/wedding14_yellow_gradient", "/wedding14_cyan_gradient", "/wedding14_blue_gradient"},
            {"/wedding14_top_16_9", "/wedding14_title4", "/wedding14_ring", "/wedding14_pink_gradient", "/wedding14_yellow_gradient", "/wedding14_red_gradient", "/wedding14_yellow_gradient", "/wedding14_cyan_gradient", "/wedding14_blue_gradient"},
            {"/wedding14_top_16_9", "/wedding14_title5", "/wedding14_ring", "/wedding14_pink_gradient", "/wedding14_yellow_gradient", "/wedding14_red_gradient", "/wedding14_yellow_gradient", "/wedding14_cyan_gradient", "/wedding14_blue_gradient"},
    };




    @Override
    protected String[][] getSources() {
        return SRCS;
    }


    @Override
    protected String[] getSources_16_9(int index) {
        return SRCS_16_9[index];
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
