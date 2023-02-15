package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.attitude;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Attitude4PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {Attitude4PreTreatment.PADDING_9_16_PATH, "/attitude4_title1",
                    "/attitude4_particle1", "/attitude4_particle2", "/attitude4_particle3", "/attitude4_particle4",
                    "/attitude4_pink_bird"},
            {Attitude4PreTreatment.PADDING_9_16_PATH, "/attitude4_title2",
                    "/attitude4_light1", "/attitude4_light2", "/attitude4_light3", "/attitude4_light4",
                    "/attitude4_gray_heart", "/attitude4_pink_heart", "/attitude4_white_heart"},
            {Attitude4PreTreatment.PADDING_9_16_PATH, "/attitude4_title3",
                    "/attitude4_particle1", "/attitude4_particle2", "/attitude4_particle3", "/attitude4_particle4",
                    "/attitude4_orange_bird"},
            {Attitude4PreTreatment.PADDING_9_16_PATH, "/attitude4_title4",
                    "/attitude4_light1", "/attitude4_light2", "/attitude4_light3", "/attitude4_light4",
                    "/attitude4_gray_heart", "/attitude4_pink_heart", "/attitude4_white_heart"},
            {Attitude4PreTreatment.PADDING_9_16_PATH, "/attitude4_title5",
                    "/attitude4_particle1", "/attitude4_particle2", "/attitude4_particle3", "/attitude4_particle4",
                    "/attitude4_blue_bird"},
    };

    private static final String PADDING_9_16_PATH = "/attitude4_padding";
    private static final String PADDING_16_9_PATH = "/attitude4_padding_16_9";
    private static final String PADDING_1_1_PATH = "/attitude4_padding_1_1";

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
    protected String[] getSource(int index) {
        SRCS[index][0] = PADDING_9_16_PATH;
        return SRCS[index];
    }

    @Override
    protected String[] getSources_1_1(int index) {
        SRCS[index][0] = PADDING_1_1_PATH;
        return SRCS[index];
    }

    @Override
    protected String[] getSources_16_9(int index) {
        SRCS[index][0] = PADDING_16_9_PATH;
        return SRCS[index];
    }
}
