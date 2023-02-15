package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.wedding;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Wedding12PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/wedding12_padding_9_16", "/wedding12_rose", "/wedding12_title1", "/wedding12_pink_gradient", "/wedding12_green_gradient", "/wedding12_yellow_gradient"},
            {"/wedding12_padding_9_16", "/wedding12_rose", "/wedding12_title2", "/wedding12_orange_gradient", "/wedding12_green_gradient", "/wedding12_yellow_gradient"},
            {"/wedding12_padding_9_16", "/wedding12_rose", "/wedding12_title3", "/wedding12_orange_gradient", "/wedding12_yellow_gradient"},
            {"/wedding12_padding_9_16", "/wedding12_rose", "/wedding12_title4", "/wedding12_pink_gradient", "/wedding12_blue_gradient", "/wedding12_yellow_gradient"},
    };

    private static final String[][] SRCS_16_9 = new String[][]{
            {"/wedding12_padding_16_9", "/wedding12_rose", "/wedding12_title1", "/wedding12_pink_gradient", "/wedding12_green_gradient", "/wedding12_yellow_gradient"},
            {"/wedding12_padding_16_9", "/wedding12_rose", "/wedding12_title2", "/wedding12_orange_gradient", "/wedding12_green_gradient", "/wedding12_yellow_gradient"},
            {"/wedding12_padding_16_9", "/wedding12_rose", "/wedding12_title3", "/wedding12_orange_gradient", "/wedding12_yellow_gradient"},
            {"/wedding12_padding_16_9", "/wedding12_rose", "/wedding12_title4", "/wedding12_pink_gradient", "/wedding12_blue_gradient", "/wedding12_yellow_gradient"},
    };

    private static final String[][] SRCS_1_1 = new String[][]{
            {"/wedding12_padding_1_1", "/wedding12_rose", "/wedding12_title1", "/wedding12_pink_gradient", "/wedding12_green_gradient", "/wedding12_yellow_gradient"},
            {"/wedding12_padding_1_1", "/wedding12_rose", "/wedding12_title2", "/wedding12_orange_gradient", "/wedding12_green_gradient", "/wedding12_yellow_gradient"},
            {"/wedding12_padding_1_1", "/wedding12_rose", "/wedding12_title3", "/wedding12_orange_gradient", "/wedding12_yellow_gradient"},
            {"/wedding12_padding_1_1", "/wedding12_rose", "/wedding12_title4", "/wedding12_pink_gradient", "/wedding12_blue_gradient", "/wedding12_yellow_gradient"},
    };


    @Override
    protected String[][] getSources() {
        return SRCS;
    }

    @Override
    protected String[] getSources_1_1(int index) {
        return SRCS_1_1[index];
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
