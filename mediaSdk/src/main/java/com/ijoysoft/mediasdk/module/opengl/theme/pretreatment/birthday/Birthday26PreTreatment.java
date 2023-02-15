package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.birthday;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Birthday26PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/birthday26_bottom_gradient", "/birthday26_blue_gradient", "/birthday26_purple_gradient", "/birthday26_title1"},
            {"/birthday26_bottom_gradient", "/birthday26_pink_gradient", "/birthday26_yellow_gradient", "/birthday26_title2"},
            {"/birthday26_bottom_gradient", "/birthday26_purple_gradient", "/birthday26_yellow_gradient", "/birthday26_title3"},
            {"/birthday26_bottom_gradient", "/birthday26_pink_gradient", "/birthday26_blue_gradient", "/birthday26_title4"},
            {"/birthday26_bottom_gradient", "/birthday26_purple_gradient", "/birthday26_yellow_gradient", "/birthday26_title5"},
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
