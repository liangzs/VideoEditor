package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.attitude;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Attitude8PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/attitude8_padding", "/attitude8_lamp_left", "/attitude8_lamp_right",
                "/attitude8_rb_gadient", "/attitude8_cyan_gradient"},
            {"/attitude8_padding", "/attitude8_rb_gadient", "/attitude8_yellow_gadient", "/attitude8_title2"},
            {"/attitude8_padding", "/attitude8_rb_gadient",
                    "/attitude8_cyan_gradient"},
            {"/attitude8_padding", "/attitude8_yr_gradient", "/attitude8_yellow_gadient", "/attitude8_title4"},
            {"/attitude8_padding", "/attitude8_yr_gradient", "/attitude8_cyan_gradient", "/attitude8_circle"},
    };

    private static final String PADDING_9_16_PATH = "/attitude8_padding";

    private static final String PADDING_16_9_PATH = "/attitude8_padding_16_9";

    private static final String PADDING_1_1_PATH = "/attitude8_padding_1_1";



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
