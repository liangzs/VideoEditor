package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.friend;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Friend2PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/friend2_gradient1", "/friend2_title1"},
            {"/friend2_gradient2", "/friend2_title2"},
            {"/friend2_gradient3", "/friend2_title3"},
            {"/friend2_gradient4", "/friend2_title4"},
            {"/friend2_gradient5", "/friend2_title5"},
    };


    private static final String[][] SRCS_16_9 = new String[][]{
            {"/friend2_gradient1_16_9", "/friend2_title1"},
            {"/friend2_gradient2_16_9", "/friend2_title2"},
            {"/friend2_gradient3_16_9", "/friend2_title3"},
            {"/friend2_gradient4_16_9", "/friend2_title4"},
            {"/friend2_gradient5_16_9", "/friend2_title5"},
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

    @Override
    protected String[] getSources_16_9(int index) {
        return SRCS_16_9[index];
    }
}
