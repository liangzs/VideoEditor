package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.attitude;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Attitude5PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/attitude5_title1"},
            {"/attitude5_title2", "/attitude5_emoji_happt"},
            {"/attitude5_title3", "/attitude5_emoji_confidence"},
            {"/attitude5_title4", "/attitude5_emoji_angry"},
            {"/attitude5_title5"},
            {"/attitude5_title6"},
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
