package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.attitude;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Attitude6PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/attitude6_pink_gradient", "/attitude6_lb_salute", "/attitude6_rb_salute",
                "/attitude6_light1", "/attitude6_light2", "/attitude6_light3", "/attitude6_light4"},
            {"/attitude6_green_gradient", "/attitude6_title2"},
            {"/attitude6_blue_gradient", "/attitude6_cry_smile",
                    "/attitude6_light1", "/attitude6_light2", "/attitude6_light3", "/attitude6_light4"},
            {"/attitude6_pink_gradient", "/attitude6_title4"},
            {"/attitude6_green_gradient", "/attitude6_left_love", "/attitude6_right_love",
                    "/attitude6_light1", "/attitude6_light2", "/attitude6_light3", "/attitude6_light4"},
            {"/attitude6_blue_gradient", "/attitude6_title6",
                    "/attitude6_light1", "/attitude6_light2", "/attitude6_light3", "/attitude6_light4"},
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
