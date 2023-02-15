package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.birthday;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Birthday20PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/birthday20_bottom_gradient", "/birthdya20_title", "/birthday20_pink_gradient"},
            {"/birthday20_bottom_gradient", "/birthdya20_title", "/birthday20_pink_gradient"},
            {"/birthday20_bottom_gradient", "/birthdya20_title", "/birthday20_pink_gradient"},
            {"/birthday20_bottom_gradient", "/birthdya20_title", "/birthday20_pink_gradient"},
            {"/birthday20_bottom_gradient", "/birthdya20_title", "/birthday20_pink_gradient"},
    };


    private static final String PADDING_9_16_PATH = "/birthday20_bottom_gradient";

    private static final String PADDING_16_9_PATH = "/birthday20_bottom_gradient_16_9";

    private static final String PADDING_1_1_PATH = "/birthday20_bottom_gradient_1_1";


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
