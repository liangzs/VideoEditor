package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.birthday;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Birthday27PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/birthday27_title", "/birthday27_purple_light", "/birthday27_SSAO_right", "/birthday27_balloons"},
            {"/birthday27_title", "/birthday27_purple_light", "/birthday27_SSAO_left"},
            {"/birthday27_purple_light", "/birthday27_SSAO_right", "/birthday27_gray_balloon3", "/birthday27_balloon_left", "/birthday27_gray_balloon2", "/birthday27_gray_balloon1", "/birthday27_balloon_right", "/birthday27_balloon_right"},
            {"/birthday27_top_star", "/birthday27_title", "/birthday27_purple_light", "/birthday27_SSAO_left"},
            {"/birthday27_flags", "/birthday27_purple_light", "/birthday27_SSAO_right"},
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
