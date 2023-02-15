package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.birthday;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Birthday24PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/birthday24_green_gradient", "/birthday24_blue_gradient", "/birthday24_on_no", "/birthday24_smile2", "/birthday24_hart1", "/birthday24_hart2"},
            {"/birthday24_pink_gradient", "/birthday24_yellow_gradient", "/birthday24_title1", "/birthday24_blur_heart", "/birthday24_heart"},
            {"/birthday24_pink_gradient", "/birthday24_blue_gradient", "/birthday24_emojis", "/birthday24_hart1", "/birthday24_hart2"},
            {"/birthday24_green_gradient", "/birthday24_yellow_gradient", "/birthday24_title2"},
            {"/birthday24_pink_gradient", "/birthday24_yellow_gradient", "/birthday24_lb_smile", "/birthday24_rb_smile", "/birthday24_blur_heart", "/birthday24_heart"},
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
