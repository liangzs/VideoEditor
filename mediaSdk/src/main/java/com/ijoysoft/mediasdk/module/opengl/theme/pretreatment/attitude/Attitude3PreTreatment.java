package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.attitude;

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

/**
 * 态度3
 */
public class Attitude3PreTreatment extends DownloadedPreTreatmentTemplate {

/*
                    "/attitude3_blue_particel", "/attitude3_yellow_particle2", "/attitude3_pink_particle2",
                            "/attitude3_red_particle", "/attitude3_yellow_particle", "/attitude3_cyan_particle",
                            "/attitude3_red_shine", "/attitude3_orange_shine", "/attitude3_cyan_shine"
*/

    private static final String[][] SRCS = new String[][]{
            {"/attitude3_gray_gradient", "/attitude3_blue_gradient", "/attitude3_title1", "/attitude3_light1", "/attitude3_light2", "/attitude3_light3",
                    "/attitude3_cyan_bird"},
            {"/attitude3_gray_gradient", "/attitude3_cyan_light", "/attitude3_blue_gradient", "/attitude3_emoji_love", "/attitude3_emoji_confidence"},
            {"/attitude3_gray_gradient", "/attitude3_pink_gradient", "/attitude3_yellow_gradient", "/attitude3_title_3", "/attitude3_light1", "/attitude3_light2", "/attitude3_light3",
                    "/attitude3_pink_bird"},
            {"/attitude3_gray_gradient", "/attitude3_pink_gradient", "/attitude3_yellow_gradient", "/attitude3_spectrum"},
            {"/attitude3_gray_gradient", "/attitude3_pink_gradient", "/attitude3_blue_gradient", "/attitude3_title_5", "/attitude3_light1", "/attitude3_light2", "/attitude3_light3",
                    "/attitude3_yello_bird"}
    };

    private static final String[][] SRCS_16_9 = new String[][]{
            {"/attitude3_gray_gradient_16_9", "/attitude3_blue_gradient", "/attitude3_title1", "/attitude3_light1", "/attitude3_light2", "/attitude3_light3",
                    "/attitude3_cyan_bird"},
            {"/attitude3_gray_gradient_16_9", "/attitude3_cyan_light", "/attitude3_blue_gradient", "/attitude3_emoji_love", "/attitude3_emoji_confidence"},
            {"/attitude3_gray_gradient_16_9", "/attitude3_pink_gradient", "/attitude3_yellow_gradient", "/attitude3_title_3", "/attitude3_light1", "/attitude3_light2", "/attitude3_light3",
                    "/attitude3_pink_bird"},
            {"/attitude3_gray_gradient_16_9", "/attitude3_pink_gradient", "/attitude3_yellow_gradient", "/attitude3_spectrum_16_9"},
            {"/attitude3_gray_gradient_16_9", "/attitude3_pink_gradient", "/attitude3_blue_gradient", "/attitude3_title_5", "/attitude3_light1", "/attitude3_light2", "/attitude3_light3",
                    "/attitude3_yello_bird"}
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
