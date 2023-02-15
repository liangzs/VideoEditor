package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.friend;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Friend3PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/friend3_red_gradient", "/friend3_blue_gradient", "/friend3_title1"},
            {"/friend3_red_gradient", "/friend3_blue_gradient", "/friend3_title2"},
            {"/friend3_red_gradient", "/friend3_blue_gradient", "/friend3_title3"},
            {"/friend3_red_gradient", "/friend3_blue_gradient", "/friend3_title4"},
            {"/friend3_red_gradient", "/friend3_blue_gradient", "/friend3_title5"},
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
