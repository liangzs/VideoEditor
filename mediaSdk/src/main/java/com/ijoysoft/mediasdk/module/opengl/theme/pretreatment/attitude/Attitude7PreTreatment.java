package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.attitude;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Attitude7PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/attitude7_cyan_gradient", "/attitude7_yellow_gradient", "/attitude7_yaar",
                "/attitude7_hearts"},
            {"/attitude7_orange_gradient", "/attitude7_pink_gradient", "/attitude7_title2"},
            {"/attitude7_pink_gradient", "/attitude7_yellow_gradient",
                    "/attitude7_eyes", "/attitude7_hand", "/attitude7_hahaha"},
            {"/attitude7_yellow_gradient", "/attitude7_orange_gradient", "/attitude7_title4"},
            {"/attitude7_pink_gradient", "/attitude7_orange_gradient", "/attitude7_emojis",
                    "/attitude7_rock"},
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
