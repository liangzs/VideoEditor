package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.birthday;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Birthday25PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/birthday25_gradient1", "/birthday25_title1", "/birthday25_top_light"},
            {"/birthday25_gradient2", "/birthday25_title2", "/birthday25_widget2"},
            {"/birthday25_gradient3", "/birthday25_cake3", "/birthday25_top_flags"},
            {"/birthday25_gradient4", "/birthday25_title4", "/birthday25_cake4"},
            {"/birthday25_gradient5", "/birthday25_cheer",},
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
